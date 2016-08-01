package datacoupler;

import backend.AppProperties;
import database_v2.utils.InternalDatabase;
import database_v2.exceptions.*;
import database_v2.utils.Rasterizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import models.Coordinate;
import models.Location;
import models.Route;
import models.event.Event;
import models.users.User;
import notifications.NotificationDispatcher;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * The DataCoupler class is responsible for all logic to couple events to relevant routes and or
 * locations. This class offers for this three methods: coupleEvent, coupleRoute and coupleLocation.
 * The fist one will process an event and look for routes and locations in the database that match
 * coupling criteria. The other two methods offer the inverse logic, where the coupler will look for
 * relevant events given a route or location.
 */
public class DataCoupler {

    private final InternalDatabase db;
    private final Rasterizer rasterizer;
    private final NotificationDispatcher notifs;
    private final ThreadPoolTaskExecutor executor;
    private final double distanceThreshold;
    private final Map<Integer, Integer> dayOfWeekMap;

    /**
     * Create a new DataCoupler instance. This class is managed by the Spring container so use of
     * this constructor otherwise is strongly discouraged.
     *
     * @param database Access to the database.
     * @param executor A thread managed by Spring
     * @param rasterizer Preprocessor module to work with a logical grid over the area
     * @param notifs Module to send notifications of matched events
     */
    public DataCoupler(InternalDatabase database, ThreadPoolTaskExecutor executor,
            Rasterizer rasterizer, NotificationDispatcher notifs) {
        this.db = database;
        this.executor = executor;
        this.rasterizer = rasterizer;
        this.notifs = notifs;
        this.distanceThreshold = AppProperties.instance()
                .getPropAsDouble(AppProperties.PROP_KEY.MATCH_ROUTE_DIST);
        dayOfWeekMap = new HashMap<>();
        dayOfWeekMap.put(Calendar.MONDAY, 0);
        dayOfWeekMap.put(Calendar.TUESDAY, 1);
        dayOfWeekMap.put(Calendar.WEDNESDAY, 2);
        dayOfWeekMap.put(Calendar.THURSDAY, 3);
        dayOfWeekMap.put(Calendar.FRIDAY, 4);
        dayOfWeekMap.put(Calendar.SATURDAY, 5);
        dayOfWeekMap.put(Calendar.SUNDAY, 6);
    }

    /**
     * Do all couple logic based upon this event. This will first couple the event to relevant
     * routes. This is done as follows.
     * <p>
     * The grid point of the event is calculated and then a query is composed to let the database
     * handle the heavy lifting. This query will look on grid point, active on the day of the event,
     * overlaps of the event time and the route time and if the route is even active at all. So the
     * results of this query will be quite small. For these routes, we further calculate how far the
     * event actually lies from them. Only if this distance is smaller then a predefined threshold,
     * the event will be coupled.
     * <p>
     * Then we look for relevant locations. This is much easier. A query will let the database
     * handle the calculations of the radius and only active locations will be returned.
     *
     * @param event The event to be matched.
     */
    public void coupleEvent(Event event) {
        EventCoupleTask task = new EventCoupleTask(event);
        executor.execute(task);
    }

    /**
     * Do the coupling algorithm for a given route. This will look for all events that match
     * coupling criteria and make the link persistent.
     *
     * @param routeId Id of the route that will be coupled
     * @param route The route itself
     * @param gridpoints set of all grid points this route runs through.
     */
    public void coupleRoute(int routeId, Route route, Set<Pair<Integer, Integer>> gridpoints) {
        RouteCoupleTask task = new RouteCoupleTask(routeId, route, gridpoints);
        executor.execute(task);
    }

    /**
     * Do the coupling algorithm for a given location. This will look for all events that match
     * coupling criteria and make the link persistent.
     * @param locationId id of the location that will be coupled
     * @param location The location itself
     */
    public void coupleLocation(int locationId, Location location) {
        LocationCoupleTask task = new LocationCoupleTask(locationId, location);
        executor.execute(task);
    }

    private void coupleToRoute(Pair<Integer, Route> routePair, String eventId, Event event) {
        Route route = routePair.getValue1();
        if (pointOnRoute(route, event.getCoordinates())) {
            try {
                db.coupleEventToRoute(routePair.getValue0(), eventId);
                Pair<Integer, User> user = db.getUserOfRoute(routePair.getValue0());
                notifs.addNotification(user.getValue0(), user.getValue1(), eventId, event);
            } catch (DataAccessException | ForeignKeyNotFoundException ex) {
                LogFactory.getLog(DataCoupler.class).error("Could not match event", ex);
            } catch (AlreadyExistsException ex) {
                LogFactory.getLog(this.getClass()).debug("Event already coupled to this route", ex);
            } catch (RecordNotFoundException ex) {
                LogFactory.getLog(DataCoupler.class).error("database invalid: Route belongs to non existing user", ex);
            }
        }
    }

    private void coupleToLocation(Pair<Integer, Location> locationPair, Event event, String eventId) {
        try {
            db.coupleEventToLocation(locationPair.getValue0(), eventId);
            Pair<Integer, User> user = db.getUserOfLocation(locationPair.getValue0());
            notifs.addNotification(user.getValue0(), user.getValue1(), eventId, event);
        } catch (DataAccessException | ForeignKeyNotFoundException ex) {
            LogFactory.getLog(DataCoupler.class).error("Could not match event");
        } catch (AlreadyExistsException ex) {
            LogFactory.getLog(this.getClass()).debug("Event already coupled to this route");
        } catch (RecordNotFoundException ex) {
            LogFactory.getLog(DataCoupler.class).error("database invalid: location belongs to non existing user");
        }
    }

    private boolean pointOnRoute(Route route, Coordinate point) {
        List<Coordinate> wp = route.getFullWaypoints();

        for (int i = 0; i < wp.size() - 1; i++) {
            if (distanceToLine(wp.get(i), wp.get(i + 1), point) <= distanceThreshold) {
                return true;
            }
        }
        return false;
    }

    private boolean eventDuringRoute(boolean[] routeDays, Date begin, Date end, Event event)
            throws DataAccessException, RecordNotFoundException {

        // we get the day of the event
        Calendar eventCal = Calendar.getInstance();
        eventCal.setTimeInMillis(event.getLastEditTimeMillis());
        // convert the day from the undefined calendar to day of week
        int eventDay = dayOfWeekMap.get(eventCal.get(Calendar.DAY_OF_WEEK));
        if (!routeDays[eventDay]) {
            // the day doesn't match, so return false
            return false;
        }
        Date eventBegin, eventEnd;
        try {
            SimpleDateFormat toString = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat fromString = new SimpleDateFormat("HH:mm:ss");

            eventBegin = new Date(event.getLastEditTimeMillis());
            eventBegin = fromString.parse(toString.format(eventBegin));

            long eventDuration = AppProperties.instance()
                    .getPropAsLong(AppProperties.PROP_KEY.EVENT_DURATION) * 60 * 1000;
            eventEnd = new Date(event.getLastEditTimeMillis() + eventDuration);
            eventEnd = fromString.parse(toString.format(eventEnd));
            // passed midnight
            if (eventEnd.getTime() > 86400000L) {
                eventEnd = new Date(86400000L);
            }

        } catch (ParseException ex) {
            Logger.getLogger(getClass()).error(ex);
            return false;
        }

        return (begin.before(eventEnd) || begin.equals(eventEnd))
                && (end.after(eventBegin) || end.equals(eventBegin));
    }

    private double distanceToLine(Coordinate begin, Coordinate end, Coordinate p) {
        double squaredLength = begin.getSquaredCartDistance(end);
        // approx for really short pieces of the route
        if (squaredLength <= 10.0) {
            return begin.distance(p);
        }
        double t = ((p.getX() - begin.getX()) * (end.getX() - begin.getX())
                + (p.getY() - begin.getY()) * (end.getY() - begin.getY())) / squaredLength;
        t = Math.max(0, Math.min(1, t));
        double projX = begin.getX() + t * (end.getX() - begin.getX());
        double projY = begin.getY() + t * (end.getY() - begin.getY());
        return Math.sqrt(dist2(p.getX(), p.getY(), projX, projY));
    }

    private double dist2(double x1, double y1, double x2, double y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    private class EventCoupleTask implements Runnable {

        private final Event event;

        public EventCoupleTask(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            try {
                int eventTypeId = db.getEventTypeId(event.getType());
                String eventId = db.getEventId(event);

                // PART 1: ROUTE MATCHING
                // first we get the candidate routes that lie in the same grid as
                // the event. On all these routes we apply further refinement
                Pair<Integer, Integer> eventGrid = rasterizer.coordToRaster(event.getCoordinates());
                // this list contains all routes within the event grid and that
                // have the eventType listed as relevant.
                List<Pair<Integer, Route>> routes
                        = db.getRouteSelection(eventGrid, event);
                routes.forEach(route -> {
                    coupleToRoute(route, eventId, event);
                });

                // PART 2: LOCATION MATCHING
                // here we simply get the locations for which the event falls in
                // their radius. If the EventType matches with the one of the event,
                // then al criteria are fulfilled for an event to be relevant
                List<Pair<Integer, Location>> locations
                        = db.getLocationSelection(event.getCoordinates());
                locations.forEach(locationPair
                        -> coupleToLocation(locationPair, event, eventId));
            } catch (DataAccessException ex) {
                LogFactory.getLog(DataCoupler.class)
                        .error("something went wrong with the database while coupling event.", ex);
            } catch (RecordNotFoundException ex) {
                throw new RuntimeException("an invalid event was passed to the coupler."
                        + "Make sure the events are first correctly inserted in the database.");
            }
        }

    }

    private class RouteCoupleTask implements Runnable {

        private final int routeId;
        private final Route route;
        private final Set<Pair<Integer, Integer>> gridpoints;

        public RouteCoupleTask(int routeId, Route route, Set<Pair<Integer, Integer>> gridpoints) {
            this.routeId = routeId;
            this.route = route;
            this.gridpoints = gridpoints;
        }

        @Override
        public void run() {
            try {
                // fist we make sure there are no older events in the database
                // and the cached object
                db.deleteOldEventsOfRoute(routeId);

                Triplet<boolean[], Date, Date> timeInfo
                        = db.getTimeInfoOfRoute(routeId);
                // we only get the events that lie in the gridpoints where the route passes
                for (Pair<Integer, Integer> gridpoint : gridpoints) {
                    List<Pair<String, Event>> foundEvents = db.getEventsByGridpoint(gridpoint);
                    for (Pair<String, Event> eventPair : foundEvents) {
                        Event event = eventPair.getValue1();

                        boolean eventDuringRoute = eventDuringRoute(
                                timeInfo.getValue0(),
                                timeInfo.getValue1(),
                                timeInfo.getValue2(),
                                event
                        );
                        if (eventDuringRoute && pointOnRoute(route, event.getCoordinates())) {
                            // on this point we have found a match between a route and
                            // event.

                            // fist we make the connection in the database
                            db.coupleEventToRoute(
                                    routeId,
                                    eventPair.getValue0()
                            );
                        }
                    }
                }
            } catch (DataAccessException | ForeignKeyNotFoundException | RecordNotFoundException ex) {
                LogFactory.getLog(getClass()).error(ex);
            } catch (AlreadyExistsException ex) {
                LogFactory.getLog(getClass()).info(ex);
            }
        }

    }

    private class LocationCoupleTask implements Runnable {

        private final int locationId;
        private final Location location;

        public LocationCoupleTask(int locationId, Location location) {
            this.locationId = locationId;
            this.location = location;
        }

        @Override
        public void run() {
            try {
                // we clear the database and cached object from all old relevant events
                db.deleteOldEventsOfLocation(locationId);

                // frist we get all events that lie within the radius of the event.
                List<Pair<String, Event>> foundEvents
                        = db.getEventsByRadius(
                                location.getAddress().getCoordinates().getX(),
                                location.getAddress().getCoordinates().getY(),
                                location.getRadius());
                // couple all these events
                for (Pair<String, Event> eventPair : foundEvents) {
                    // fist we make the coupling in the database
                    db.coupleEventToLocation(locationId, eventPair.getValue0());
                }
            } catch (DataAccessException | ForeignKeyNotFoundException ex) {
                LogFactory.getLog(getClass()).error(ex);
            } catch (AlreadyExistsException ex) {
                LogFactory.getLog(getClass()).info(ex);
            }
        }

    }

}

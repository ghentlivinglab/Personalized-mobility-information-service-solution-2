package database_v2.utils;

import backend.AppProperties;
import database_v2.DAOLayer.CRUDdao;
import database_v2.DAOLayer.EventDAO;
import database_v2.controlLayer.*;
import database_v2.exceptions.*;
import database_v2.mappers.*;
import database_v2.models.Attribute;
import database_v2.models.TableJoin;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.relational.*;
import database_v2.searchTerms.*;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.internet.AddressException;
import models.Coordinate;
import models.Location;
import models.Route;
import models.event.*;
import models.users.Password;
import models.users.User;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * Control layer above the DAO's to handle some specific database tasks, mainly for the data
 * coupler.
 */
public class InternalDatabase {

    private final DataAccessProvider dap;
    private final DatabaseCache cache;

    private final LocationMapper locationMapper;
    private final RouteMapper routeMapper;
    private final EventMapper eventMapper;

    private final Map<Integer, Attribute> dayAttributes;

    /**
     * Create a new instance, handled by Spring
     *
     * @param dap Access to the database
     * @param cache Access to cached users.
     */
    public InternalDatabase(DataAccessProvider dap, DatabaseCache cache) {
        this.dap = dap;
        this.cache = cache;
        // mappers
        locationMapper = new LocationMapper();
        routeMapper = new RouteMapper();
        eventMapper = new EventMapper();

        dayAttributes = new HashMap<>();
        dayAttributes.put(Calendar.MONDAY, TravelDBModel.getMonAttribute());
        dayAttributes.put(Calendar.TUESDAY, TravelDBModel.getTueAttribute());
        dayAttributes.put(Calendar.WEDNESDAY, TravelDBModel.getWedAttribute());
        dayAttributes.put(Calendar.THURSDAY, TravelDBModel.getThuAttribute());
        dayAttributes.put(Calendar.FRIDAY, TravelDBModel.getFriAttribute());
        dayAttributes.put(Calendar.SATURDAY, TravelDBModel.getSatAttribute());
        dayAttributes.put(Calendar.SUNDAY, TravelDBModel.getSunAttribute());
    }

    /**
     * USE OF THIS CONSTRUTOR IS ONLY RESERVED FOR TESTS, DO NOT USE THIS IN PRODUCTION CODE
     *
     * @param dap Access to the persist layer.
     * @param cache Access to all cached objects
     * @param locationMapper mapper for locations
     * @param routeMapper mapper for routes
     * @param eventMapper mapper for events
     */
    public InternalDatabase(DataAccessProvider dap, DatabaseCache cache,
            LocationMapper locationMapper, RouteMapper routeMapper, EventMapper eventMapper) {
        this.dap = dap;
        this.cache = cache;
        this.locationMapper = locationMapper;
        this.routeMapper = routeMapper;
        this.eventMapper = eventMapper;

        dayAttributes = new HashMap<>();
        dayAttributes.put(Calendar.MONDAY, TravelDBModel.getMonAttribute());
        dayAttributes.put(Calendar.TUESDAY, TravelDBModel.getTueAttribute());
        dayAttributes.put(Calendar.WEDNESDAY, TravelDBModel.getWedAttribute());
        dayAttributes.put(Calendar.THURSDAY, TravelDBModel.getThuAttribute());
        dayAttributes.put(Calendar.FRIDAY, TravelDBModel.getFriAttribute());
        dayAttributes.put(Calendar.SATURDAY, TravelDBModel.getSatAttribute());
        dayAttributes.put(Calendar.SUNDAY, TravelDBModel.getSunAttribute());
    }

    /**
     * Perform a query on the relational database to do a first selection for routes that could be
     * relevant for an event. The database will actually do the heavy lifting for routes. The query
     * will look for all routes that lie in the grid point of the event, that are active and for who
     * the time checks out (time has two components: day of the week and hour interval)
     *
     * @param gridpoint The grid point coordinates of the event
     * @param event The event for whom this function will do a preselection
     * @return A list of all routes matching the given criteria with their id.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public List<Pair<Integer, Route>> getRouteSelection(
            Pair<Integer, Integer> gridpoint, Event event)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // we get the day of the event
            Calendar eventCal = Calendar.getInstance();
            eventCal.setTimeInMillis(event.getLastEditTimeMillis());
            int eventDay = eventCal.get(Calendar.DAY_OF_WEEK);

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
                return new ArrayList<>();
            }

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteDBModel.getActiveAttribute(), true),
                    new SimpleSearchTerm(RouteGridpointDBModel.GRID_X_ATTRIBUTE, gridpoint.getValue0()),
                    new SimpleSearchTerm(RouteGridpointDBModel.GRID_Y_ATTRIBUTE, gridpoint.getValue1()),
                    new SimpleSearchTerm(dayAttributes.get(eventDay), true),
                    new TimeIntervalSearchTerm(
                            TravelDBModel.getBeginTimeAttribute(),
                            TravelDBModel.getEndTimeAttribute(),
                            new Time(eventBegin.getTime()), new Time(eventEnd.getTime()))
            );
            TableJoin join
                    = new TableJoin(RouteDBModel.class, RouteGridpointDBModel.class,
                            RouteGridpointDBModel.ROUTE_ID_ATTRIBUTE);
            join = new TableJoin(join, TravelDBModel.class, RouteDBModel.getTravelIdAttribute());

            List<RouteDBModel> foundRoutes = cd.complexSearch(join, search, RouteDBModel.class);
            List<Pair<Integer, Route>> out = new ArrayList<>();
            for (RouteDBModel dbRoute : foundRoutes) {
                out.add(new Pair<>(
                        dbRoute.getId(),
                        routeMapper.toDomainModel(dbRoute, dac, cache)
                ));
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Make the binding between an event and a route for which it is relevant persistent in the
     * database.
     *
     * @param routeId The id of the route for which the event is relevant.
     * @param eventId The id of the event.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws ForeignKeyNotFoundException No route was found with this id.
     * @throws AlreadyExistsException There was already a binding between this route and event.
     */
    public void coupleEventToRoute(int routeId, String eventId)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            RouteEventDBModel link = new RouteEventDBModel(routeId, eventId, Boolean.FALSE);
            cd.create(link);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Remove all relevant events of the given route. Use this function when the route is updated
     * (it may take another path, where the events are no longer relevant.
     *
     * @param routeId The id of the route of which the events must be deleted.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public void deleteOldEventsOfRoute(int routeId)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteEventDBModel.getRouteIdAttribute(), routeId)
            );
            cd.delete(RouteEventDBModel.class, search);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Get a list of all locations and their id's by their radius. A location will be returned if
     * the passed coordinate lies within the radius of the location and if the location is marked as
     * active.
     *
     * @param coordinate The coordinate that must lie in the radius of the location.
     * @return A list of matching locations and their id's.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public List<Pair<Integer, Location>> getLocationSelection(
            Coordinate coordinate)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(LocationDBModel.getActiveAttribute(), true),
                    new RadiusSearchTerm(
                            LocationDBModel.getRadiusAttribute(),
                            AddressDBModel.CARTESIAN_X_ATTRIBUTE,
                            AddressDBModel.CARTESIAN_Y_ATTRIBUTE,
                            coordinate
                    )
            );
            // join the location table with the address table
            TableJoin join = new TableJoin(
                    LocationDBModel.class,
                    AddressDBModel.class,
                    LocationDBModel.getAddressIdAttribute()
            );

            List<LocationDBModel> dbModels = cd.complexSearch(join, search, LocationDBModel.class);
            List<Pair<Integer, Location>> out = new ArrayList<>();
            for (LocationDBModel dblocation : dbModels) {
                try {
                    out.add(new Pair<>(
                            dblocation.getId(),
                            locationMapper.toDomainModel(dblocation, dac, cache)
                    ));
                } catch (RecordNotFoundException ex) {
                    LogFactory.getLog(getClass()).error(
                            "Database corruption detected: location maps to non existing address", ex);
                }
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Make the binding between an event and a route for which it is relevant persistent in the
     * database.
     *
     * @param locationId The id of the location for which the event is relevant.
     * @param eventId The id of the event.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws ForeignKeyNotFoundException No location was found with this id.
     * @throws AlreadyExistsException There already was a binding between this location and event.
     */
    public void coupleEventToLocation(int locationId, String eventId)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            LocationEventDBModel link = new LocationEventDBModel(locationId, eventId, Boolean.FALSE);
            cd.create(link);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Remove all relevant events of the given location. Use this function when the location is
     * updated (it may now have a smaller radius so some events will no longer be relevant).
     *
     * @param locationId The id of the location which the relevant events must decoupled.
     * @throws DataAccessException Something went wrong in the underlying database.
     */
    public void deleteOldEventsOfLocation(int locationId)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(LocationEventDBModel.getLocationIdAttribute(), locationId)
            );
            cd.delete(LocationEventDBModel.class, search);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * All info about the time of a route (day of the week and time interval) is not stored in the
     * route itself but in its travel. This function can be used to retrieve this information for a
     * route.
     *
     * @param routeId The id of the route for which you want time info
     * @return A triplet of data. Fist is an array of booleans, indicating the days of the route (0:
     * mon,..., 6: sun). The second is the start time and the last the end time.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No route was found for this id.
     */
    public Triplet<boolean[], Date, Date> getTimeInfoOfRoute(int routeId)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            TableJoin join = new TableJoin(
                    TravelDBModel.class, RouteDBModel.class,
                    RouteDBModel.getTravelIdAttribute()
            );
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteDBModel.getRouteIdAttribute(), routeId)
            );
            List<TravelDBModel> dbTravels = cd.complexSearch(join, search, TravelDBModel.class);

            if (dbTravels.isEmpty()) {
                throw new RecordNotFoundException();
            }

            return new Triplet<>(
                    dbTravels.get(0).getDaysArray(),
                    dbTravels.get(0).getBeginTime(),
                    dbTravels.get(0).getEndTime()
            );
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Get the user to whom a specific route belongs. Caution, this user object is not a valid user
     * object in the sense that no dependencies are filled in. Only the data from the user table is
     * filled in.
     *
     * @param routeId The id of the route of which you want the owner.
     * @return An incomplete user object and its id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No route was found with that id.
     */
    public Pair<Integer, User> getUserOfRoute(int routeId)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteDBModel.getRouteIdAttribute(), routeId)
            );
            TableJoin join = new TableJoin(
                    AccountDBModel.class, TravelDBModel.class, TravelDBModel.getAccountIdAttribute());
            join = new TableJoin(join, RouteDBModel.class, RouteDBModel.getTravelIdAttribute());

            List<AccountDBModel> foundAccs = cd.complexSearch(join, search, AccountDBModel.class);
            if (foundAccs.isEmpty()) {
                throw new RecordNotFoundException();
            }

            AccountDBModel foundAcc = foundAccs.get(0);
            Password pass = new Password(foundAcc.getPassword(), foundAcc.getSalt());
            return new Pair<>(foundAcc.getId(), new User(
                    foundAcc.getFirstname(),
                    foundAcc.getLastname(),
                    pass,
                    foundAcc.getEmail(),
                    foundAcc.getEmailValidated(),
                    null, // locations
                    null, // travels
                    foundAcc.getMuteNotifications(),
                    foundAcc.getRefreshToken(),
                    foundAcc.getIsOperator(),
                    foundAcc.getIsAdmin(),
                    foundAcc.getPushToken()
            ));
        } catch (SQLException | AddressException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Get the user to whom a specific location belongs. Caution, this user object is not a valid
     * user object in the sense that no dependencies are filled in. Only the data from the user
     * table is filled in.
     *
     * @param locationId The id of the location of which you want the owner.
     * @return An incomplete user object and its id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No location was found with that id.
     */
    public Pair<Integer, User> getUserOfLocation(int locationId)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(LocationDBModel.getLocationIdAttribute(), locationId)
            );

            TableJoin join = new TableJoin(
                    AccountDBModel.class, LocationDBModel.class, LocationDBModel.getAccountIdAttribute());

            List<AccountDBModel> foundAccs = cd.complexSearch(join, search, AccountDBModel.class);
            if (foundAccs.isEmpty()) {
                throw new RecordNotFoundException();
            }
            AccountDBModel foundAcc = foundAccs.get(0);
            Password pass = new Password(foundAcc.getPassword(), foundAcc.getSalt());
            return new Pair<>(foundAcc.getId(), new User(
                    foundAcc.getFirstname(),
                    foundAcc.getLastname(),
                    pass,
                    foundAcc.getEmail(),
                    foundAcc.getEmailValidated(),
                    null, // locations
                    null, // travels
                    foundAcc.getMuteNotifications(),
                    foundAcc.getRefreshToken(),
                    foundAcc.getIsOperator(),
                    foundAcc.getIsAdmin(),
                    foundAcc.getPushToken()
            ));
        } catch (SQLException | AddressException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Get the id of an EventType.
     *
     * @param eventType The eventType of which you want the id
     * @return The id of the eventType
     * @throws RecordNotFoundException The EventType was not in the cache
     */
    public int getEventTypeId(EventType eventType)
            throws RecordNotFoundException {
        int cached = cache.getEventTypeIdIfPresent(eventType);
        if (cached != 0) {
            return cached;
        }
        throw new RecordNotFoundException();
    }

    /**
     * Get the id of a cached event. This method will only work if the event was properly inserted
     * to the database.
     *
     * @param event The event from which you want the id.
     * @return The id of the event.
     * @throws RecordNotFoundException That event was not found in the cache.
     */
    public String getEventId(Event event) throws RecordNotFoundException {
        String cachedId = cache.getEventIdIfPresent(event);
        if (cachedId != null) {
            return cachedId;
        }
        throw new RecordNotFoundException();
    }

    /**
     * Get all events that lie within a certain grid point.
     *
     * @param gridPoint the grid point where all events must lie into
     * @return List of all events in the grid point with their id's.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public List<Pair<String, Event>> getEventsByGridpoint(Pair<Integer, Integer> gridPoint)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();
            List<EventDBModel> foundEvents = ed.getEvents(
                    gridPoint.getValue0(), gridPoint.getValue1());
            List<Pair<String, Event>> out = new ArrayList<>();
            for (EventDBModel dbEvent : foundEvents) {
                Event cached = cache.getEventIfPresent(dbEvent.getId());
                if (cached == null) {
                    cached = eventMapper.getAppModel(dbEvent, dac, cache);
                    cache.addEvent(dbEvent.getId(), cached);
                }
                out.add(new Pair<>(
                        dbEvent.getId(),
                        cached
                ));
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Get all events that lie within a certain radius around a certain point.
     *
     * @param cartX cartesian x value of the center of the radius
     * @param cartY cartesian y value of the center of the radius
     * @param radius Radius wherein the events must lie
     * @return All events and their id's in the given radius.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public List<Pair<String, Event>> getEventsByRadius(double cartX, double cartY, int radius)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();
            List<EventDBModel> foundEvents = ed.getEventsInRadius(cartX, cartY, radius);
            List<Pair<String, Event>> out = new ArrayList<>();
            for (EventDBModel dbEvent : foundEvents) {
                Event cached = cache.getEventIfPresent(dbEvent.getId());
                if (cached == null) {
                    cached = eventMapper.getAppModel(dbEvent, dac, cache);
                    cache.addEvent(dbEvent.getId(), cached);
                }
                out.add(new Pair<>(
                        dbEvent.getId(),
                        cached
                ));
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

}

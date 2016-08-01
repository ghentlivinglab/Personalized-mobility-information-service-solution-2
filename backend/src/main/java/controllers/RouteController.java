package controllers;

import DTO.mappers.EventMapper;
import DTO.mappers.RouteMapper;
import DTO.models.EventDTO;
import DTO.models.EventTypeDTO;
import DTO.models.RouteDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.mail.internet.AddressException;
import models.Route;
import models.Travel;
import models.event.Event;
import models.event.EventType;
import models.users.User;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles incoming requests related to routes.
 */
@CrossOrigin
@RestController
@RequestMapping("/user/{user_id}/travel/{travel_id}/route")
public class RouteController {

    private final Database database;
    private final RouteMapper routemapper;
    private final EventMapper eventmapper;

    /**
     *
     * @param database Interface to communicate with the database.
     */
    public RouteController(Database database) {
        this.database = database;
        this.routemapper = new RouteMapper();
        this.eventmapper = new EventMapper();
    }

    /**
     * Method to get all routes given a certain userid and travelid
     * @param userIdString The id of the requested user
     * @param travelIdString The id of the the travel where the routes are requested of
     * @param accessToken
     * @return List of RouteDTO with all routes of the given user/travel
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<RouteDTO>> getRoutes(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);
            // begin with fetching the user
            User user = database.getUser(userId);

            // we get the appropriate travel from that user
            if (!user.getTravels().containsKey(travelId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Travel travel = user.getTravels().get(travelId);

            // we iterate over all the routes from that travel
            ArrayList<RouteDTO> routes = new ArrayList<>();
            for (Integer routeId : travel.getRoutes().keySet()) {
                // convert each route to a routeDTO and add it to the list
                routes.add(routemapper.convertToDTO(travel.getRoutes().get(routeId), routeId));
            }
            // return the list of routes
            return new ResponseEntity<>(routes, HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 

    }

    /**
     * Add a route for a given user to a given travel
     * @param userIdString The id of the user to add the route
     * @param travelIdString the id of the travel to add the route
     * @param routedto RouteDTO of the route that needs to be added
     * @param accessToken
     * @return RouteDTO of the added route
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RouteDTO> addRoute(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @RequestBody RouteDTO routedto,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);
            // we begin with fetching the user
            User user = database.getUser(userId);

            // we get the appropriate travel from that user
            if (!user.getTravels().containsKey(travelId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Travel travel = user.getTravels().get(travelId);

            // now we make a new route object from the given DTO
            Route route = routemapper.convertFromDTO(routedto);

            // we make the route persistent
            int routeId = database.createRoute(travelId, route);

            // and we update the model
            travel.addRoute(routeId, route);

            // we need to add all EventTypes that are relevant for this route
            for (EventTypeDTO notifyEventType : routedto.getNotifyEventTypes()) {
                // we make the link between this type and route persistent in the db.
                // this will return a valid EventType object
                Pair<Integer, EventType> validEventtype = database.createRouteEventtype(routeId, notifyEventType.getType());

                // now we update the route model
                route.addEventType(validEventtype.getValue0(), validEventtype.getValue1());
            }

            // at last we return the updated route model (converted to DTO)
            return new ResponseEntity<>(routemapper.convertToDTO(route, routeId), HttpStatus.CREATED);
        } catch (NumberFormatException | DataAccessException | AddressException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException | ForeignKeyNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    /**
     * Get a route given an user, travel and route
     * @param userIdString The id of the user
     * @param travelIdString The id of the travel
     * @param routeIdString The id of the route that we need to send
     * @param accessToken
     * @return RouteDTO of the requested route
     */
    @RequestMapping(value = "/{route_id}", method = RequestMethod.GET)
    public ResponseEntity<RouteDTO> getRoute(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @PathVariable("route_id") String routeIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);
            int routeId = Integer.parseInt(routeIdString);
            // we begin with fetching the user
            User user = database.getUser(userId);

            // we get the appropriate travel from that user
            if (!user.getTravels().containsKey(travelId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Travel travel = user.getTravels().get(travelId);

            // from the travel we get the requested route
            if (!travel.getRoutes().containsKey(routeId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Route route = travel.getRoutes().get(routeId);

            // we convert the route to a DTO and return it
            return new ResponseEntity<>(routemapper.convertToDTO(route, routeId), HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    
    /**
     * Get all events from a certain route of an user
     * @param userIdString The id of the user
     * @param travelIdString The id of the travel
     * @param routeIdString The id of the route that we need to send
     * @param accessToken
     * @return List of events related to this route
     */
    @RequestMapping(value = "/{route_id}/events", method = RequestMethod.GET)
    public ResponseEntity getRouteEvents(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @PathVariable("route_id") String routeIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);
            int routeId = Integer.parseInt(routeIdString);
            // we begin with fetching the user
            User user = database.getUser(userId);

            
            // we get the appropriate travel from that user
            if (!user.getTravels().containsKey(travelId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Set<Pair<String, Event>> dbEvents = database.getRecentEventsOfRoute(routeId);
            List<EventDTO> out = new ArrayList<>();
            dbEvents.stream().forEach(e -> out.add(
                    eventmapper.convertToDTO(e.getValue1(), e.getValue0())));
            return new ResponseEntity(out, HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    

    /**
     * Update a given route with new data
     * @param userIdString The id of the user
     * @param travelIdString The id of the travel
     * @param routeIdString The id of the route that we need to send
     * @param routedto The DTO with the updated data
     * @param accessToken
     * @return RouteDTO of the updated route
     */
    @RequestMapping(value = "/{route_id}", method = RequestMethod.PUT)
    public ResponseEntity<RouteDTO> editRoute(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @PathVariable("route_id") String routeIdString,
            @RequestBody RouteDTO routedto,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);
            int routeId = Integer.parseInt(routeIdString);
            // fist we get the relevant user object
            User user = database.getUser(userId);

            // it's an update we are doing here, so the route must already exist
            // in the user. Here we save the old route so we can compare what has
            // changed.
            Route oldRoute = user.getTravels()
                    .get(travelId)
                    .getRoutes()
                    .get(routeId);

            // we convert the DTO to an application model. This data must now
            // be stored in the database and the model should be updated
            Route newRoute = routemapper.convertFromDTO(routedto);

            // first make the new route data persistent
            database.updateRoute(travelId, routeId, newRoute);

            // then we update the model
            user.getTravels()
                    .get(travelId)
                    .addRoute(routeId, newRoute);

            // before handling the relevant EventTypes, we need the updated list

            for(EventTypeDTO eventTypeDTO: routedto.getNotifyEventTypes()) {
                Pair<Integer, EventType> et = database.getEventtype(eventTypeDTO.getType());
                newRoute.addEventType(et.getValue0(), et.getValue1());
            }

            // now we handle the EventTypes. We need two loops. First we check if
            // all the new EventTypes are present in the old route. If this is not
            // the case, then we add it. Then we check if all the types of the old
            // route are in the new one. If this is not the case, we delete it.
            for (Entry<Integer, EventType> newEt : newRoute.getNotifyForEventTypes().entrySet()) {
                if (!oldRoute.getNotifyForEventTypes().containsKey(newEt.getKey())) {
                    // first make it persistent
                    Pair<Integer, EventType> valid = database.createRouteEventtype(
                            routeId,
                            newEt.getValue().getType()
                    );
                    // then update the model
                    newEt.setValue(
                            valid.getValue1()
                    );
                }
            }
            for (Entry<Integer, EventType> oldEt : oldRoute.getNotifyForEventTypes().entrySet()) {
                if (!newRoute.getNotifyForEventTypes().containsKey(oldEt.getKey())) {
                    // we only have to make it persistent since oldRoute will no
                    // longer be used
                    database.deleteRouteEventtype(
                            routeId,
                            oldEt.getKey()
                    );
                }
            }

            // return the updated model
            return new ResponseEntity<>(routemapper.convertToDTO(newRoute, routeId), HttpStatus.OK);
        } catch (AddressException | NumberFormatException | DataAccessException  ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException | ForeignKeyNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Delete a certain route
     * @param userIdString The id of the user
     * @param travelIdString The id of the travel
     * @param routeIdString The id of the route that we need to delete
     * @param accessToken
     * @return Statuscode 204
     */
    @RequestMapping(value = "/{route_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRoute(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @PathVariable("route_id") String routeIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);
            int routeId = Integer.parseInt(routeIdString);
            // first we fetch the user
            User user = database.getUser(userId);
            // we get the appropriate travel from that user
            if (!user.getTravels().containsKey(travelId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Travel travel = user.getTravels().get(travelId);

            // from the travel we check if the requested route exists
            if (!travel.getRoutes().containsKey(routeId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // then we delete the route
            database.deleteRoute(routeId);

            // and we update the model
            travel.removeRoute(routeId);

            // return the correct status code
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

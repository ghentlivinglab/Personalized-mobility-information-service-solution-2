package DTO.mappers;

import DTO.models.CoordinateDTO;
import DTO.models.EventTypeDTO;
import DTO.models.NotifyDTO;
import DTO.models.RouteDTO;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.internet.AddressException;
import models.Coordinate;
import models.Route;
import models.Transportation;
import models.event.EventType;
import models.services.Email;
import models.services.Service;

/**
 * Class to convert Route to RouteDTO and vice versa
 */
public class RouteMapper {

    /**
     * Convert Route to RouteDTO object
     *
     * @param route Route object
     * @param routeId Integer of routeid
     * @return RouteDTO representation
     */
    public RouteDTO convertToDTO(Route route, int routeId) {
        return convertToDTO(route, Integer.toString(routeId));
    }

    /**
     * Convert Route to RouteDTO object
     *
     * @param route Route object
     * @param routeId String of routeid
     * @return RouteDTO representation
     */
    public RouteDTO convertToDTO(Route route, String routeId) {
        //converting waypoints
        CoordinateMapper coormapper = new CoordinateMapper();

        // user waypoints
        int userWpSize = route.getUserWaypoints().size();
        CoordinateDTO[] userWaypoints = new CoordinateDTO[userWpSize];
        for (int i = 0; i < userWpSize; i++) {
            userWaypoints[i] = coormapper.convertToDTO(route.getUserWaypoints().get(i));
        }
        // full waypoints
        int fullWpSize = route.getFullWaypoints().size();
        CoordinateDTO[] fullWaypoints = new CoordinateDTO[fullWpSize];
        for (int i = 0; i < fullWpSize; i++) {
            fullWaypoints[i] = coormapper.convertToDTO(route.getFullWaypoints().get(i));
        }

        //converting eventtypes
        EventTypeMapper eventtypemapper = new EventTypeMapper();
        int size = route.getNotifyForEventTypes().size();
        EventTypeDTO[] eventtypes = new EventTypeDTO[size];
        int i = 0;
        for (EventType eventtype : route.getNotifyForEventTypes().values()) {
            eventtypes[i] = eventtypemapper.convertToDTO(eventtype);
            i++;
        }

        NotifyDTO notify = new NotifyDTO();
        for (Service service : route.getServices()) {
            if (service.getClass() == Email.class) {
                Email email = (Email) service;
                notify.setEmail(email.isValidated());
            }
        }

        return new RouteDTO(
                routeId,
                userWaypoints,
                fullWaypoints,
                route.getTransportationType().toString(),
                eventtypes,
                notify,
                !route.isMuted()); //if it's muted, it isn't active
    }

    /**
     * Convert RouteDTO to Route object
     *
     * @param routedto RouteDTO object
     * @return Route representation
     * @throws DataAccessException
     * @throws RecordNotFoundException
     * @throws AddressException
     */
    public Route convertFromDTO(RouteDTO routedto)
            throws DataAccessException, RecordNotFoundException, AddressException {
        CoordinateMapper coormapper = new CoordinateMapper();
        ArrayList<Coordinate> userWaypoints = new ArrayList<>();
        for (CoordinateDTO waypoint : routedto.getUserWaypoints()) {
            userWaypoints.add(coormapper.convertFromDTO(waypoint));
        }
        List<Coordinate> fullWaypoints = new ArrayList<>();
        for (CoordinateDTO waypoint : routedto.getFullWaypoints()) {
            fullWaypoints.add(coormapper.convertFromDTO(waypoint));
        }

        ArrayList<Service> services = new ArrayList<>();
        if (routedto.getNotify().isEmail()) {
            //TODO - NO WAY ATM
        }

        // We need the database to convert eventType names to actual models
        // This step is handled in the controller itself
        Map<Integer, EventType> eventTypes = new HashMap<>();
        return new Route(
                userWaypoints,
                fullWaypoints,
                Transportation.fromString(routedto.getTransportationType()),
                eventTypes,
                !routedto.isMuted(),
                services //TODO
        );
    }
}

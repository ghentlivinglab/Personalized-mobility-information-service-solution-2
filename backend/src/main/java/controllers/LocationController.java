package controllers;

import DTO.mappers.EventMapper;
import DTO.mappers.LocationMapper;
import DTO.models.EventDTO;
import DTO.models.EventTypeDTO;
import DTO.models.LocationDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import models.Location;
import models.event.Event;
import models.event.EventType;
import models.exceptions.InvalidCountryCodeException;
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
 * Handles incoming requests related to points of interest aka locations.
 */
@CrossOrigin
@RestController
@RequestMapping("/user/{user_id}/point_of_interest")
public class LocationController {

    private final Database database;
    private final LocationMapper locationmapper;
    private final EventMapper eventmapper;

    /**
     *
     * @param database Interface to communicate with the database.
     */
    public LocationController(Database database) {
        this.database = database;
        this.locationmapper = new LocationMapper();
        this.eventmapper = new EventMapper();
    }

    /**
     * Method to get all locations given a certain userid
     * @param userIdString
     * @param accessToken
     * @return List of locations (aka points of interest) of a certain user
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<LocationDTO>> getLocations(@PathVariable("user_id") String userIdString,
                                                        @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            // begin with fetching the user
            User user = database.getUser(userId);
            // we iterate over all his locations and convert them to DTO models
            List<LocationDTO> locations = new ArrayList<>();
            for (Entry<Integer, Location> location : user.getLocations().entrySet()) {
                locations.add(
                        locationmapper.convertToDTO(location.getValue(), location.getKey())
                );
            }
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Add a certain location given a userid and locationDTO
     * @param userIdString The id of the user
     * @param locationdto DTO of the location to add
     * @param accessToken
     * @return LocatoinDTO with the added location
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LocationDTO> addLocation(
            @PathVariable("user_id") String userIdString,
            @RequestBody LocationDTO locationdto,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            // begin with fetching the user
            User user = database.getUser(userId);

            // here the new location is parsed to an application model
            Location location = locationmapper.convertFromDTO(locationdto);

            // fist we make this location persistent
            int locationId = database.createLocation(userId, location);
            // and then we update the model
            user.addLocation(locationId, location);

            // at last we need to add all EventTypes that are relevant for this Location
            if (locationdto.getNotifyEventTypes() != null) {
                for (EventTypeDTO eventTypeDTO : locationdto.getNotifyEventTypes()) {
                    // make the link between this type and location persistent. The database
                    // will return a valid EventType object
                    Pair<Integer, EventType> validEventtype
                            = database.createLocationEventtype(locationId, eventTypeDTO.getType());
                    // now update the model accordingly
                    location.addEventType(
                            validEventtype.getValue0(),
                            validEventtype.getValue1()
                    );
                }
            }
            return new ResponseEntity<>(locationmapper.convertToDTO(location, locationId), HttpStatus.CREATED);
        } catch (DataAccessException | InvalidCountryCodeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException | ForeignKeyNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    /**
     * Get a location/poi given an userid and locationid
     * @param userIdString The id of the user
     * @param locationIdString The id of the requested location
     * @param accessToken
     * @return LocationDTO of the requested location
     */
    @RequestMapping(value = "/{location_id}", method = RequestMethod.GET)
    public ResponseEntity<LocationDTO> getLocation(
            @PathVariable("user_id") String userIdString,
            @PathVariable("location_id") String locationIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int locationId = Integer.parseInt(locationIdString);

            // begin with fetching the user
            User user = database.getUser(userId);

            // from that user we get the requested location. If the model doesn't
            // contain the location, 404 is immediately returned
            if (!user.getLocations().containsKey(locationId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Location location = user.getLocations().get(locationId);

            // finally we convert the location to its DTO and return it.
            return new ResponseEntity<>(locationmapper.convertToDTO(location, locationId), HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Get all events from a certain location of an user
     * @param userIdString The id of the user
     * @param locationIdString The id of the requested location
     * @param accessToken
     * @return List of events related to this location
     */
    @RequestMapping(value = "/{location_id}/events", method = RequestMethod.GET)
    public ResponseEntity getLocationEvents(
            @PathVariable("user_id") String userIdString,
            @PathVariable("location_id") String locationIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int locationId = Integer.parseInt(locationIdString);

            // begin with fetching the user
            User user = database.getUser(userId);
            

            // from that user we get the requested location. If the model doesn't
            // contain the location, 404 is immediately returned
            if (!user.getLocations().containsKey(locationId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Set<Pair<String, Event>> dbEvents = database.getRecentEventsOfLocation(locationId);
            List<EventDTO> out = new ArrayList<>(dbEvents.size());
            dbEvents.stream().forEach(e -> out.add(
                    eventmapper.convertToDTO(e.getValue1(), e.getValue0())));
            return new ResponseEntity<>(out, HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 

    }

    /**
     * Update a location
     * @param userIdString The id of the user
     * @param locationIdString The id of the location
     * @param locationdto DTO of the updated location
     * @param accessToken
     * @return LocationDTO of the updated location
     */
    @RequestMapping(value = "/{location_id}", method = RequestMethod.PUT)
    public ResponseEntity<LocationDTO> editLocation(
            @PathVariable("user_id") String userIdString,
            @PathVariable("location_id") String locationIdString,
            @RequestBody LocationDTO locationdto,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int locationId = Integer.parseInt(locationIdString);

            // begin with fetching the user
            User user = database.getUser(userId);

            // we are editing so the location should already exist. We save this
            // old location so we can compare to know what has changed.
            if (!user.getLocations().containsKey(locationId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Location oldLocation = user.getLocations().get(locationId);

            // Here we convert the DTO to a new application model. The data of this
            // model should replace the old location
            Location newLocation = locationmapper.convertFromDTO(locationdto);

            // first we make the data of the location itself persistent
            database.updateLocation(userId, locationId, newLocation);

            // then we add it to the model
            user.addLocation(locationId, newLocation);

            // before handling the relevant EventTypes, we need the updated list
            for(EventTypeDTO eventTypeDTO: locationdto.getNotifyEventTypes()) {
                Pair<Integer, EventType> et = database.getEventtype(eventTypeDTO.getType());
                newLocation.addEventType(et.getValue0(), et.getValue1());
            }

            // now we have to handle the relevant EventTypes. For this we need two
            // loops. First we go over all the new types and check if the old location
            // already had them. If this is not the case, we will add the eventType.
            // Then we iterate over all the old EventTypes. If there is one that
            // is not in the new Location, then we delete it.
            for (Entry<Integer, EventType> newEt : newLocation.getNotifyForEventTypes().entrySet()) {
                if (!oldLocation.getNotifyForEventTypes().containsKey(newEt.getKey())) {
                    // first we make the link between this location and EventType
                    // persistent
                    Pair<Integer, EventType> valid = database.createLocationEventtype(
                            locationId, // the id of the location
                            newEt.getValue().getType() // the name of the EventType
                    );
                    // and then we update the model
                    newEt.setValue(
                            valid.getValue1()
                    );
                }
            }
            for (Entry<Integer, EventType> oldEt : oldLocation.getNotifyForEventTypes().entrySet()) {
                if (!newLocation.getNotifyForEventTypes().containsKey(oldEt.getKey())) {
                    // first we delete the link for the database
                    database.deleteLocationEventtype(
                            locationId, // the id of the location
                            oldEt.getKey()
                    );
                    // we only have to make it persistent since oldLocation will
                    // not be used anymore.
                }
            }

            // at last we can return the location
            return new ResponseEntity<>(locationmapper.convertToDTO(newLocation, locationId), HttpStatus.CREATED);
        } catch (InvalidCountryCodeException | NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException | ForeignKeyNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Delete a certain location of a user
     * @param userIdString The id of the user
     * @param locationIdString The id of the location that we want to delete
     * @param accessToken
     * @return Statuscode 204
     */
    @RequestMapping(value = "/{location_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLocation(
            @PathVariable("user_id") String userIdString,
            @PathVariable("location_id") String locationIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int locationId = Integer.parseInt(locationIdString);

            // begin with fetching the user
            User user = database.getUser(userId);

            // check if this location even belongs to this user
            if(! user.getLocations().containsKey(locationId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // we make the deletion of the travel persistent
            database.deleteLocation(locationId);
            // and then we update the model
            user.removeLocation(locationId);

            // return the appropriate status code
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException | RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

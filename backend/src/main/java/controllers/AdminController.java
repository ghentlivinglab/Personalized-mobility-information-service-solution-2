package controllers;

import DTO.mappers.EventMapper;
import DTO.mappers.EventTypeMapper;
import DTO.mappers.LocationMapper;
import DTO.mappers.RouteMapper;
import DTO.mappers.TravelMapper;
import DTO.mappers.UserMapper;
import DTO.models.DataDumpDTO;
import DTO.models.EventDTO;
import DTO.models.EventTypeDTO;
import DTO.models.LocationDTO;
import DTO.models.RouteDTO;
import DTO.models.TravelDTO;
import DTO.models.TravelDumpDTO;
import DTO.models.UserDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.Location;
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
 * Handles incoming requests related to admins
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final Database database;
    private final UserMapper usermapper;
    private final TravelMapper travelmapper;
    private final RouteMapper routemapper;
    private final LocationMapper locationmapper;
    private final EventMapper eventmapper;
    private final EventTypeMapper eventtypemapper;

    /**
     * 
     * @param database Interface to communicate with the database.
     */
    public AdminController(Database database) {
        this.database = database;
        this.usermapper = new UserMapper();
        this.travelmapper = new TravelMapper();
        this.routemapper = new RouteMapper();
        this.locationmapper = new LocationMapper();
        this.eventmapper = new EventMapper();
        this.eventtypemapper = new EventTypeMapper();
    }
    
    /**
     * Get a list of all admins
     * @param accessToken
     * @return List of admins
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity getAdmins(@RequestHeader("Authorization") String accessToken) {
        try {
 
            // first we get all users from the database
            List<Pair<Integer, User>> allUsers = database.listAllAdmins();
            // and convert them to DTO models
            List<UserDTO> out = new ArrayList<>(allUsers.size());
            allUsers.stream().forEach((user) -> {
                out.add(usermapper.convertToDTO(
                        user.getValue1(),
                        Integer.toString(user.getValue0())
                ));
            });
            return new ResponseEntity(out, HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Make an user/operator an admin
     * @param user_email
     * @param accessToken
     * @return Statuscode 201
     */
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public ResponseEntity createAdmin(@RequestBody String user_email,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            Pair<Integer, User> user = database.getUserByEmail(user_email);
            user.getValue1().makeAdmin();
            database.updateUser(user.getValue0(), user.getValue1());
            
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Make an admin a normal user
     * @param user_email
     * @param accessToken
     * @return Statuscode 204
     */
    @RequestMapping(value = "/admin", method = RequestMethod.PUT)
    public ResponseEntity deleteAdmin(@RequestBody String user_email,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            Pair<Integer, User> user = database.getUserByEmail(user_email);
            user.getValue1().makeUser();
            database.updateUser(user.getValue0(), user.getValue1());
            
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Get a list of all operators
     * @param accessToken
     * @return List of operators
     */
    @RequestMapping(value = "/operator", method = RequestMethod.GET)
    public ResponseEntity getOperators(@RequestHeader("Authorization") String accessToken) {
        try {
 
            // first we get all users from the database
            List<Pair<Integer, User>> allUsers = database.listAllOperators(); 
            // and convert them to DTO models
            List<UserDTO> out = new ArrayList<>(allUsers.size());
            allUsers.stream().forEach((user) -> {
                out.add(usermapper.convertToDTO(
                        user.getValue1(),
                        Integer.toString(user.getValue0())
                ));
            });
            return new ResponseEntity(out, HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Make an user/admin an operator
     * @param user_email
     * @param accessToken
     * @return Statuscode 201
     */
    @RequestMapping(value = "/operator", method = RequestMethod.POST)
    public ResponseEntity createOperator(@RequestBody String user_email,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            Pair<Integer, User> user = database.getUserByEmail(user_email);
            user.getValue1().makeOperator();
            database.updateUser(user.getValue0(), user.getValue1());
            
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Make an operator a normal user
     * @param user_email
     * @param accessToken
     * @return Statuscode 204
     */
    @RequestMapping(value = "/operator", method = RequestMethod.PUT)
    public ResponseEntity deleteOperator(@RequestBody String user_email,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            Pair<Integer, User> user = database.getUserByEmail(user_email);
            user.getValue1().makeUser();
            database.updateUser(user.getValue0(), user.getValue1());
            
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Delete an user from the database
     * @param userId
     * @param accessToken
     * @return Statuscode 204
     */
    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser (@PathVariable("user_id") String userId,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            
            database.deleteUser(Integer.parseInt(userId));
            
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Returns a datadump (defined in API)
     * @param accessToken
     * @return Datadump
     */
    @RequestMapping(value = "/data_dump", method = RequestMethod.GET)
    public ResponseEntity getDataDump(@RequestHeader("Authorization") String accessToken) {
        try {
 
            // first we get all users from the database
            List<Pair<Integer, User>> allUsers = database.listAllUsers();
            // and convert them to DTO models
            List<UserDTO> users = new ArrayList<>(allUsers.size());
            allUsers.stream().forEach((user) -> {
                users.add(usermapper.convertToDTO(
                        user.getValue1(),
                        Integer.toString(user.getValue0())
                ));
            });
            
            List <Pair<Integer, Travel>> allTravels = database.listAlltravels();
            List<TravelDumpDTO> travels = new ArrayList<>();
            allTravels.stream().forEach((travel) -> {
                TravelDTO traveldto = travelmapper.convertToDTO(
                        travel.getValue1(), 
                        travel.getValue0());
                Map <Integer, Route> allRoutes = travel.getValue1().getRoutes();
                List<RouteDTO> routes = new ArrayList<>();
                allRoutes.entrySet().stream().forEach((route) -> {
                    routes.add(routemapper.convertToDTO(
                            route.getValue(), 
                            route.getKey()));
                });
                travels.add(new TravelDumpDTO(traveldto, routes));
            });
            
            
            
            List <Pair<Integer, Location>> allLocations = database.listAllLocations();
            List<LocationDTO> locations = new ArrayList<>();
            allLocations.stream().forEach((location) -> {
                locations.add(locationmapper.convertToDTO(
                        location.getValue1(), 
                        location.getValue0()));
            });
            
            List<Pair<String, Event>> allEvents = database.listAllEvents();
            List<EventDTO> events = new ArrayList<>();
            allEvents.stream().forEach((event) -> {
                events.add(eventmapper.convertToDTO(
                        event.getValue1(),
                        event.getValue0()
                ));
            });
            
            List<Pair<Integer, EventType>> allEventTypes = database.getEventtypes();
            List<EventTypeDTO> eventTypes = new ArrayList<>();
            allEventTypes.stream().forEach((eventType) -> {
                eventTypes.add(eventtypemapper.convertToDTO(
                        eventType.getValue1()
                ));
            });
            
            return new ResponseEntity<>(new DataDumpDTO(users, travels, locations, events, eventTypes), HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
}

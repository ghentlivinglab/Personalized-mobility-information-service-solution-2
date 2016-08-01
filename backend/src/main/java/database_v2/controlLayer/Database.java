package database_v2.controlLayer;

import database_v2.exceptions.*;
import java.util.List;
import java.util.Set;
import models.Location;
import models.Route;
import models.Transportation;
import models.Travel;
import models.event.Event;
import models.event.EventType;
import models.users.User;
import org.javatuples.Pair;

/**
 * This is a facade to offer all needed database functionalities to the rest of the application,
 * while protecting the underlying structure. All other components should only have a reference to
 * an instance of a Database. Ideally, this should be achieved by making a Spring bean and using
 * dependency injection with all classes that need database access.
 * <p>
 * The database should provide all functions while keeping an acceptable performance. It's
 * recommended that implementations of this interface have some sort of caching.
 */
public interface Database {

    /**
     * Get a user application model with all data filled in as it is stored in the database. This
     * includes everything from locations to travels. The returned user object is also cached, so
     * multiple calls to this method with the same parameter will result in exactly the same user
     * object.
     *
     * @param userId The id of the user to be fetched
     * @return An application user model object that matches the given id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No user was found with that id.
     */
    User getUser(int userId) throws DataAccessException, RecordNotFoundException;

    /**
     *
     * @param email
     * @return
     * @throws DataAccessException
     * @throws RecordNotFoundException
     */
    Pair<Integer, User> getUserByEmail(String email)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Add a new user to the database. !!! Dependencies are not automatically handled. So all the
     * travels, locations etc. of the user are not stored to in the database. You will need to use
     * appropriated methods to save them.
     * <p>
     * This method does not change anything to the application model.
     *
     * @param user the user to be stored, Travels en Locations are NOT stored.
     * @return the newly assigned id of the user
     * @throws DataAccessException something went wrong with the underlying database
     * @throws AlreadyExistsException Another account already exists with this email
     */
    int createUser(User user) throws DataAccessException, AlreadyExistsException;

    /**
     * Update the data of the user with the given id to match the given User application model. !!!
     * Dependencies are not automatically handled. So all the travels, locations etc. of the user
     * are not stored to in the database. You will need to use appropriated methods to save them.
     *
     * @param userId The id of the user to be updated
     * @param user The application model containing the new data
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws AlreadyExistsException The updated data violates some unique constraints (email is
     * already in use)
     * @throws RecordNotFoundException No user was found with this id.
     */
    void updateUser(int userId, User user) throws DataAccessException, AlreadyExistsException, RecordNotFoundException;

    /**
     * Deletes user from the database system. All related data will also be deleted
     *
     * @param userId The id of the user to be deleted
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws RecordNotFoundException No user found with this id.
     */
    void deleteUser(int userId) throws DataAccessException, RecordNotFoundException;

    /**
     * Fetch a list of all users currently stored in the database. !!! These User objects are NOT a
     * complete representation of all the data in the database. All travel and locations are left
     * out of the object, only the direct data of the user is passed. Non of these objects will be
     * cached.
     *
     * @return A list of pairs of id's and (incomplete) user objects
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, User>> listAllUsers() throws DataAccessException;

    /**
     * Get all users in the database that have admin rights.
     *
     * @return List of admin users and their id's
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, User>> listAllAdmins() throws DataAccessException;

    /**
     * Get all users in the database that have operator rights (except the admins).
     *
     * @return List of users with their id's
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, User>> listAllOperators() throws DataAccessException;

    /**
     * Add a new travel to the database for the user. !!! Dependencies are not automatically
     * handled. So all the routes that this travel may contain are NOT stored in the database. You
     * will need to use the appropriate methods to save them.
     * <p>
     * This method can apply changes to the addresses of this travel to match the data like it is
     * stored in the database.
     *
     * @param userId the id of the users to which this travel belongs
     * @param travel the travel to be stored; it's routes are NOT stored.
     * @return the newly assigned id of the travel
     * @throws DataAccessException something went wrong with the underlying database
     * @throws ForeignKeyNotFoundException No user was found with this id
     */
    int createTravel(int userId, Travel travel)
            throws DataAccessException, ForeignKeyNotFoundException;

    /**
     * Update the data of the travel with the given id to match the given Travel application model.
     * !!! Dependencies are not automatically handled. So if there were any updates on the routes of
     * this travel, then this will NOT be updated in the database.
     *
     * @param userId The id of the user to which this travel belongs
     * @param travelId The id of the travel to be updated
     * @param travel The tavel will be updated to match the data as stored in this application model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws ForeignKeyNotFoundException No user was found with this id
     * @throws RecordNotFoundException No travel was found with this id.
     */
    void updateTravel(int userId, int travelId, Travel travel)
            throws DataAccessException, ForeignKeyNotFoundException, RecordNotFoundException;

    /**
     * Delete the travel from the database with the given id. All related data will also be deleted.
     *
     * @param travelId The id of the travel to be deleted
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No travel found with the given id
     */
    void deleteTravel(int travelId) throws DataAccessException, RecordNotFoundException;

    /**
     * List all travels from the database. The route dependencies will not be filled in to save
     * memory space. Addresses will be correctly fetched from the database. This function is meant
     * for data dumps.
     *
     * @return A list of (incomplete) travel objects with their id's
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, Travel>> listAlltravels() throws DataAccessException;

    /**
     * Add a new location to the database for the user. All dependencies like the address are taken
     * care of. The address of the location can undergo some changes to reflect the data as it is
     * stored in the database.
     *
     * @param userId The id of the user to which this location is added
     * @param location The location to be added
     * @return The newly assigned location id
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws ForeignKeyNotFoundException no user found with this id
     * @throws AlreadyExistsException The user already has this location
     */
    int createLocation(int userId, Location location)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException;

    /**
     * Update the data of the location with the given id to match the given Location application
     * model. All dependencies are handled with this method.
     *
     * @param userId The id of the user to which this location belongs
     * @param locationId The id of the location to be updated
     * @param location The application model containing the updated data.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws ForeignKeyNotFoundException No user was found with this id;
     * @throws AlreadyExistsException The user already has a location that matches the update
     * location.
     * @throws RecordNotFoundException No location was found with the given id
     */
    void updateLocation(int userId, int locationId, Location location)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException, RecordNotFoundException;

    /**
     * Delete the location with the given id from the database. All related data will also be
     * deleted.
     *
     * @param locationId The id of the location to be deleted
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws RecordNotFoundException No location was found with this id
     */
    void deleteLocation(int locationId) throws DataAccessException, RecordNotFoundException;

    /**
     * Get a full list of all locations that are stored in the database. This function will fill in
     * all data of the locations, except for relevant events.
     *
     * @return List of all locations
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, Location>> listAllLocations() throws DataAccessException;

    /**
     * Add a new route to the database for the given travel. !!! The EventType dependencies are NOT
     * automatically handled. If you want to store the EventTypes that are relevant for this route,
     * you will need to use the appropriate methods.
     *
     * @param travelId the id of the travel to which this route belongs
     * @param route the route to be added
     * @return The newly assigned id of the route
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws ForeignKeyNotFoundException No travel was found with this id
     */
    int createRoute(int travelId, Route route)
            throws DataAccessException, ForeignKeyNotFoundException;

    /**
     * Update the data of the route with the given id to match the given route application model.
     * !!! The EventType dependencies are NOT automatically handled. So is there were any updates on
     * relevant eventTypes, this will not be saved to the database. You will need to use the
     * appropriate methods to save any changes to relevant eventTypes
     *
     * @param travelId The id of the travel to which this route belongs
     * @param routeId The id of the route to be updated
     * @param route This application model contains all updated data
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws ForeignKeyNotFoundException No travel was found with this id
     * @throws RecordNotFoundException No route was found with this id.
     */
    void updateRoute(int travelId, int routeId, Route route)
            throws DataAccessException, ForeignKeyNotFoundException, RecordNotFoundException;

    /**
     * Delete the route with the given id from the database. All related data will also be deleted.
     *
     * @param routeId The id of the route to be deleted
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws RecordNotFoundException No route was found with this id.
     */
    void deleteRoute(int routeId) throws DataAccessException, RecordNotFoundException;

    /**
     * List all routes from the database. This will fill in all the data from the route, except for
     * the relevant events. This function can be used for data dumps.
     *
     * @return A list of (incomplete) routes and their id's.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, Route>> listAllRoutes() throws DataAccessException;

    /**
     * Add this EventType as relevant for the indicated route. If the EventType did not already
     * exist in the database, then it is added and defaulted as relevant for all possible
     * transportation types.
     *
     * @param routeId The id of the route to which this eventType belongs
     * @param eventType The name of the EventType to be added
     * @return a pair of a valid EventType application model and its id.
     * @throws DataAccessException something went wrong with the underlying database
     * @throws ForeignKeyNotFoundException No route found with the given id
     * @throws AlreadyExistsException The route already contains this eventType
     */
    Pair<Integer, EventType> createRouteEventtype(int routeId, String eventType)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException;

    /**
     * Add this EventType as relevant for the indicated location. If the EventType did not already
     * exist in the database, then it is added and defaulted as relevant for all possible
     * transportation types.
     *
     * @param locationId The id of the location for which this eventType is relevant
     * @param eventType The name of the eventType
     * @return a pair of a valid EventType application model and its id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws ForeignKeyNotFoundException No location found with the given id
     * @throws AlreadyExistsException The location already contains this eventType
     */
    Pair<Integer, EventType> createLocationEventtype(int locationId, String eventType)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException;

    /**
     * Indicate that the eventType with given id is no longer relevant for the route with routeId.
     *
     * @param routeId The route id for which the EventType is no longer relevant
     * @param eventtypeId The id of the eventType to be decoupled from the route
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws RecordNotFoundException Either the route or the eventType did not exist, or the
     * eventType was not relevant for the route to begin with.
     */
    void deleteRouteEventtype(int routeId, int eventtypeId)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Indicate that the eventType with given id is no longer relevant for the location with
     * locationId.
     *
     * @param locationId The location id for which the EventType is no longer relevant.
     * @param eventtypeId The id of the eventType to be decoupled from the location
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException Either the route or the eventType did not exist, or the
     * eventType was not relevant for the location to begin with.
     */
    void deleteLocationEventtype(int locationId, int eventtypeId)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Get a valid EventType based on its name. This function will look in the database to fill in
     * which transport types are relevant for this eventType. If no eventType matching the given
     * data was found, then a new EventType will be created. It's relevant transport types will be
     * defaulted to all possible vehicles.
     *
     * @param eventType The name of the eventType
     * @return A valid EventType application model paired with its id.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    Pair<Integer, EventType> getEventtype(String eventType)
            throws DataAccessException;

    /**
     * Get all EventTypes and their id's as they are stored in the database. These types will have
     * correctly filled in relevant transportation types.
     *
     * @return A list of all eventTypes paired with their id's.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, EventType>> getEventtypes() throws DataAccessException;

    /**
     * Get a list of all EventTypes and their id's that are relevant for the given transportation
     * type. these types will have correctly filled in relevant transportation types.
     *
     * @param transport The transportation type to filter the EventTypes
     * @return A list of all matching eventTypes paired with their id's.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<Integer, EventType>> getEventtypes(Transportation transport)
            throws DataAccessException;

    /**
     * Get an event application model with all data filled in as it is stored in the database. This
     * method will correctly fill in all dependencies (a valid EventType).
     *
     * @param eventId The is of the event to be fetched.
     * @return An event application model.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No event was found with that id.
     */
    Event getEvent(String eventId) throws DataAccessException, RecordNotFoundException;

    /**
     * Add a new Event to the database. This method can apply changes to the EventType of the Event
     * to make the data consistent with the database.
     *
     * @param event The event to be added
     * @return The newly assigned id of the event
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    String createEvent(Event event) throws DataAccessException;

    /**
     * Update the data of the existing event in the database with the given id to match the data
     * from the passed event model. This method can apply changes to the EventType to match the data
     * of the database.
     *
     * @param eventId The id of the event to be updated
     * @param event an event application model containing the new data
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws RecordNotFoundException No event was found with that id.
     */
    void updateEvent(String eventId, Event event)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Remove the event with given id from the database.
     *
     * @param eventId The id of the event to be deleted.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No event was found with that id.
     */
    void deleteEvent(String eventId)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Get a list of all the stored events and their id's.
     *
     * @return List of paired id's and their events.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<Pair<String, Event>> listAllEvents()
            throws DataAccessException;

    void updateUserEmail(int userId, User user, String oldEmail)
            throws DataAccessException, AlreadyExistsException, RecordNotFoundException;

    /**
     * Get a limited list of all events that are considered to be recent and ongoing. This list is
     * only limited to the events that are currently in the cache. Strictly speaking, this may be
     * missing some events that are also considered to be recent, although in practice the cache
     * will be large enough to keep all recent events.
     * <p>
     * Ideal use of this function is to show events to not logged in users as a very quick
     * estimation (no database calls needed).
     *
     * @return A list of events from the cache that are considered to be recent.
     */
    List<Pair<String, Event>> getRecentEvents();

    /**
     * Get all events that are relevant for a location and that are considered to be recent.
     *
     * @param locationId The id of the location for which you want relevant and recent events.
     * @return List of relevant events and their id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No location or event was found with the given id.
     */
    Set<Pair<String, Event>> getRecentEventsOfLocation(int locationId)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Get all events that are relevant for a route and that are considered to be recent.
     *
     * @param routeId The id of the route for which you want relevant and recent events.
     * @return List of relevant events and their id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No route or event was found with the given id.
     */
    Set<Pair<String, Event>> getRecentEventsOfRoute(int routeId)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Get all events that are relevant to at least one of this users locations or routes and that
     * are considered to be recent.
     *
     * @param userId The id of the user for which you want all recent and relevant events.
     * @return List of relevant events and their id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No user was found with that id.
     */
    Set<Pair<String, Event>> getRecentEventsOfUser(int userId)
            throws DataAccessException, RecordNotFoundException;

}

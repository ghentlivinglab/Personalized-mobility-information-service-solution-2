package database_v2.controlLayer;

import java.util.Map;
import models.address.City;
import models.event.Event;
import models.event.EventType;
import models.users.User;
import org.javatuples.Pair;

/**
 * The database cache provides a caching layer above the database itself. Some of these caches will
 * be Loading caches, that are smart enough to handle cache misses by themselves. Others must be
 * maintained manually (adding and removing will be done by other code).
 */
public interface DatabaseCache {

    /**
     * Get a user from the database cache, if present. Otherwise null is returned.
     *
     * @param userId The is of the user to be fetched
     * @return A user application model - or null on cache misses
     */
    User getUserIfPresent(int userId);

    /**
     * Get a user by email from the cache, if present. Otherwise null is returned.
     *
     * @param email The email of the user you're looking for.
     * @return The found user object, or null on cache misses.
     */
    Pair<Integer, User> getUserIfPresent(String email);

    /**
     * Remove the user from the cache if present. No action will be taken if no user was present
     * with the given id.
     *
     * @param userId The id of the user to be deleted.
     */
    void deleteUser(int userId);

    /**
     * Add a specific user application model object to the database cache. Replaces an older entry
     * if it existed with the same id.
     *
     * @param userId The id of the user to be cached
     * @param user The user object to be cached
     */
    void addUser(int userId, User user);

    /**
     * Get the cached city application model, if present. Otherwise null is returned.
     *
     * @param cityId the id of the requested city.
     * @return object of the city application model - or null on cache miss.
     */
    City getCityIfPresent(int cityId);

    /**
     * get the cached city application model, if present.
     *
     * @param postalCode the postal code of the requested city
     * @param country the country code of the requested city
     * @return A city application model - or null on cache miss
     */
    City getCityIfPresent(String postalCode, String country);

    /**
     * Get the id that belongs to this city application model. Only valid city models obtained via
     * the database layer are stored in this cache. On cache misses zero is returned.
     *
     * @param city The city model of which the id is requested
     * @return The id of the city model - or 0 on cache miss.
     */
    int getCityIdIfPresent(City city);

    /**
     * Add this city object to the cache. Replaces an older object if present for the given id.
     *
     * @param cityId The id of the city object to be cached
     * @param city The city application model to be cached.
     */
    void addCity(int cityId, City city);

    /**
     * Get the cached eventType application model if present in the cache, otherwise null is
     * returned.
     *
     * @param eventtypeId The id of the requested EventType
     * @return An EventType application model - or null on cache miss.
     */
    EventType getEventTypeIfPresent(int eventtypeId);

    /**
     * Get the cached eventType application model if present in the cache, otherwise null is
     * returned.
     *
     * @param eventType The name of the EventType
     * @return An EventType application model - or null on cache miss.
     */
    EventType getEventTypeIfPresent(String eventType);

    /**
     * Get the id of the application model. If no object was found, then zero will be returned.
     *
     * @param eventtype The EventType application model of which you want the id
     * @return The id of the requested eventType
     */
    int getEventTypeIdIfPresent(EventType eventtype);

    /**
     * Add a specific EventType application model object to the cache. Is an older object is present
     * for this id, then this object will be replaced.
     *
     * @param eventtypeId the id of the EventType
     * @param eventtype The application model object to be cached
     */
    void addEventType(int eventtypeId, EventType eventtype);

    /**
     * Get the cached version of an event. Returns null on cache misses.
     *
     * @param eventId The id of the event to be fetched from cache
     * @return The cached event, or null if not present.
     */
    Event getEventIfPresent(String eventId);

    /**
     * Get the id belonging to a specific event in the cache. Returns null on cache misses.
     *
     * @param event The event object of which you want to know the id.
     * @return The id of the event or null on cache misses.
     */
    String getEventIdIfPresent(Event event);

    /**
     * Add an event object to the cache.
     *
     * @param eventId The id of the event to be added.
     * @param event The event object to be cached.
     */
    void addEvent(String eventId, Event event);

    /**
     * Remove an event from the cache. If it was already removed, then nothing happens.
     *
     * @param eventId the id of the event to be removed.
     */
    void deleteEvent(String eventId);
    
    
    void emailUserChanged (String email);

    /**
     * Get a view (unmodifiable) of all events that are cached at the moment.
     *
     * @return Map view of events
     */
    Map<String, Event> getAllEvents();

}

package database_v2.controlLayer.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import database_v2.controlLayer.DatabaseCache;
import java.util.Collections;
import java.util.Map;
import models.address.City;
import models.event.Event;
import models.event.EventType;
import models.users.User;
import org.javatuples.Pair;

/**
 * A full implementation of the DatabaseCache interface. The main cache is used to store users.
 * These objects will include all the data for that user. Extra caches are present for cities and
 * EventTypes. These objects may be used between users, so its useful to stores references to all
 * circulating objects.
 */
public class DatabaseCacheImpl implements DatabaseCache {

    private final Cache<Integer, User> userCache;
    private final Cache<String, Pair<Integer, User>> userDescCache;

    private final Cache<Integer, City> cityCache;
    private final Cache<String, City> cityDescCache;
    private final Cache<City, Integer> cityIdCache;

    private final Cache<Integer, EventType> eventtypeCache;
    private final Cache<String, EventType> eventtypeDescCache;
    private final Cache<EventType, Integer> eventtypeIdCache;

    private final Cache<String, Event> eventCache;
    private final Cache<Event, String> eventIdCache;

    /**
     * Create a new instance. This will create a set of new caches. This class should be a
     * singleton, so creation and dependency injection is handled by Spring.
     *
     */
    public DatabaseCacheImpl() {
        eventtypeCache = CacheBuilder.newBuilder()
                .build();

        eventtypeDescCache = CacheBuilder.newBuilder()
                .build();

        eventtypeIdCache = CacheBuilder.newBuilder()
                .weakKeys()
                .build();

        cityCache = CacheBuilder.newBuilder()
                .softValues()
                .build();

        cityDescCache = CacheBuilder.newBuilder()
                .softValues()
                .build();

        cityIdCache = CacheBuilder.newBuilder()
                .weakKeys()
                .build();

        userCache = CacheBuilder.newBuilder()
                .build();

        userDescCache = CacheBuilder.newBuilder()
                .build();

        eventCache = CacheBuilder.newBuilder()
                .build();

        eventIdCache = CacheBuilder.newBuilder()
                .weakKeys()
                .build();
    }

    @Override
    public User getUserIfPresent(int userId) {
        return userCache.getIfPresent(userId);
    }

    @Override
    public Pair<Integer, User> getUserIfPresent(String email) {
        return userDescCache.getIfPresent(email);
    }

    @Override
    public void deleteUser(int userId) {
        User cached = userCache.getIfPresent(userId);
        if (cached == null) {
            return;
        }
        userDescCache.invalidate(cached.getEmailAsString());
        userCache.invalidate(userId);
    }

    @Override
    public void addUser(int userId, User user) {
        // add the user to the main cache
        userCache.put(userId, user);
        // and to the cache that maps email to users
        userDescCache.put(user.getEmailAsString(), new Pair<>(userId, user));
    }

    @Override
    public City getCityIfPresent(int cityId) {
        return cityCache.getIfPresent(cityId);
    }

    @Override
    public City getCityIfPresent(String postalCode, String country) {
        String cityDesc = postalCode + ";" + country;
        return cityDescCache.getIfPresent(cityDesc);
    }

    @Override
    public int getCityIdIfPresent(City city) {
        Integer out = cityIdCache.getIfPresent(city);
        if (out != null) {
            return out;
        } else {
            return 0;
        }
    }

    @Override
    public void addCity(int cityId, City city) {
        cityCache.put(cityId, city);
        String cityDesc = city.getPostalCode() + ";" + city.getCountry();
        cityDescCache.put(cityDesc, city);
        cityIdCache.put(city, cityId);
    }

    @Override
    public EventType getEventTypeIfPresent(int eventtypeId) {
        return eventtypeCache.getIfPresent(eventtypeId);
    }

    @Override
    public EventType getEventTypeIfPresent(String eventType) {
        return eventtypeDescCache.getIfPresent(eventType);
    }

    @Override
    public int getEventTypeIdIfPresent(EventType eventtype) {
        Integer out = eventtypeIdCache.getIfPresent(eventtype);
        if (out != null) {
            return out;
        } else {
            return 0;
        }
    }

    @Override
    public void addEventType(int eventtypeId, EventType eventtype) {
        eventtypeCache.put(eventtypeId, eventtype);
        eventtypeDescCache.put(eventtype.getType(), eventtype);
        eventtypeIdCache.put(eventtype, eventtypeId);
    }

    @Override
    public Event getEventIfPresent(String eventId) {
        return eventCache.getIfPresent(eventId);
    }

    @Override
    public String getEventIdIfPresent(Event event) {
        return eventIdCache.getIfPresent(event);
    }

    @Override
    public void addEvent(String eventId, Event event) {
        eventCache.put(eventId, event);
        eventIdCache.put(event, eventId);
    }

    @Override
    public void deleteEvent(String eventId) {
        Event cached = eventCache.getIfPresent(eventId);
        if(cached == null) {
            return;
        }
        eventCache.invalidate(eventId);
        eventIdCache.invalidate(cached);
    }

    @Override
    public void emailUserChanged(String email) {
        userDescCache.invalidate(email);
    }

    @Override
    public Map<String, Event> getAllEvents() {
        return Collections.unmodifiableMap(eventCache.asMap());
    }

}

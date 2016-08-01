package database_v2.controlLayer.impl;

import java.util.HashMap;
import java.util.Map;
import models.address.City;
import models.event.Event;
import models.event.EventType;
import models.users.User;
import org.javatuples.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class DatabaseCacheImplTest {

    private DatabaseCacheImpl cache;
    private int userId;
    private User user;
    private int cityId;
    private String cityPostal, country;
    private City city;
    private int eventTypeId;
    private String etDesc;
    private EventType et;
    private String eventId;
    private Event event;

    public DatabaseCacheImplTest() {
    }

    @Before
    public void setUp() {
        cache = new DatabaseCacheImpl();
        // USER
        userId = 1;
        user = mock(User.class);
        when(user.getEmailAsString()).thenReturn("test@test.com");
        cache.addUser(userId, user);
        // CITY
        cityId = 2;
        cityPostal = "1234";
        country = "BE";
        city = mock(City.class);
        when(city.getPostalCode()).thenReturn(cityPostal);
        when(city.getCountry()).thenReturn(country);
        cache.addCity(cityId, city);
        // ET
        eventTypeId = 3;
        etDesc = "TEST";
        et = mock(EventType.class);
        when(et.getType()).thenReturn(etDesc);
        cache.addEventType(eventTypeId, et);
        // event
        eventId = "eventId";
        event = mock(Event.class);
        cache.addEvent(eventId, event);
    }

    /**
     * Test of getUserIfPresent method, of class DatabaseCacheImpl.
     */
    @Test
    public void testGetUserIfPresent_int() {
        assertEquals(user, cache.getUserIfPresent(userId));
    }

    @Test
    public void testGetUserIfPresent_string() {
        assertEquals(new Pair<>(userId, user), cache.getUserIfPresent("test@test.com"));
    }

    @Test
    public void testDeleteUser() {
        cache.deleteUser(userId);
        assertNull(cache.getUserIfPresent(userId));
        assertNull(cache.getUserIfPresent("test@test.com"));
    }
    
    @Test
    public void testGetCityIfPresent_int() {
        assertEquals(city, cache.getCityIfPresent(cityId));
    }
    
    @Test
    public void testGetCityIfPresent_string() {
        assertEquals(city, cache.getCityIfPresent(cityPostal, country));
    }
    
    @Test
    public void testGetCityIdIfPresent() {
        assertEquals(cityId, cache.getCityIdIfPresent(city));
        assertEquals(0, cache.getCityIdIfPresent(mock(City.class)));
    }

    @Test
    public void testGetEventType_int() {
        assertEquals(et, cache.getEventTypeIfPresent(eventTypeId));
    }
    
    @Test
    public void testGetEventType_string() {
        assertEquals(et, cache.getEventTypeIfPresent(etDesc));
    }
    
    @Test
    public void testGetEventTypeIdIfPresent() {
        assertEquals(eventTypeId, cache.getEventTypeIdIfPresent(et));
        assertEquals(0, cache.getEventTypeIdIfPresent(mock(EventType.class)));
    }
    
    @Test
    public void testGetEventIfPresent() {
        assertEquals(event, cache.getEventIfPresent(eventId));
    }
    
    @Test
    public void testGetEventIdIfPresent() {
        assertEquals(eventId, cache.getEventIdIfPresent(event));
        assertNull(cache.getEventIdIfPresent(mock(Event.class)));
    }
    
    @Test
    public void testDeleteEvent() {
        cache.deleteEvent(eventId);
        assertNull(cache.getEventIfPresent(eventId));
        assertNull(cache.getEventIdIfPresent(event));
        cache.deleteEvent("eventId2");
    }
    
    @Test
    public void testEmailUserChanged() {
        cache.emailUserChanged("test@test.com");
        assertNull(cache.getUserIfPresent("test@test.com"));
    }
    
    @Test
    public void testGetAllEvents() {
        Map<String, Event> exp = new HashMap<>();
        exp.put(eventId, event);
        assertEquals(exp, cache.getAllEvents());
    }
}

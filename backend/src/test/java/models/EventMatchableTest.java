/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import models.event.Event;
import models.event.EventType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class EventMatchableTest {
    
    public EventMatchableTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getNotifyForEventTypes method, of class EventMatchable.
     */
    @Test
    public void testGetNotifyForEventTypes() {
        System.out.println("getNotifyForEventTypes");
        EventMatchable instance = new EventMatchableImpl();
        Map<Integer, EventType> expResult = new HashMap<>();
        Map<Integer, EventType> result = instance.getNotifyForEventTypes();
        assertEquals(expResult, result);
    }

    /**
     * Test of addEventType method, of class EventMatchable.
     */
    @Test
    public void testAddEventType() {
        System.out.println("addEventType");
        Integer id = 1;
        EventType eventtype = new EventType ("Jam", Arrays.asList(new Transportation[]{Transportation.CAR}));
        EventMatchable instance = new EventMatchableImpl();
        instance.addEventType(id, eventtype);
        assertFalse(instance.getNotifyForEventTypes().isEmpty());
    }

    /**
     * Test of deleteEventType method, of class EventMatchable.
     */
    @Test
    public void testDeleteEventType() {
        System.out.println("deleteEventType");
        Integer id = 1;
        EventType eventtype = new EventType ("Jam", Arrays.asList(new Transportation[]{Transportation.CAR}));
        EventMatchable instance = new EventMatchableImpl();
        instance.addEventType(id, eventtype);
        instance.deleteEventType(id);
        assertTrue(instance.getNotifyForEventTypes().isEmpty());
    }

    public class EventMatchableImpl extends EventMatchable {

        public EventMatchableImpl() {
            super(true, new HashMap<>());
        }
    }
    
}

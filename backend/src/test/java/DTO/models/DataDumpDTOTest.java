/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.models;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class DataDumpDTOTest {
    
    public DataDumpDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getUsers method, of class DataDumpDTO.
     */
    @Test
    public void testGetUsers() {
        System.out.println("getUsers");
        DataDumpDTO instance = new DataDumpDTO();
        List<UserDTO> expResult = null;
        List<UserDTO> result = instance.getUsers();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsers method, of class DataDumpDTO.
     */
    @Test
    public void testSetUsers() {
        System.out.println("setUsers");
        List<UserDTO> users = new ArrayList<>();
        DataDumpDTO instance = new DataDumpDTO();
        instance.setUsers(users);
        assertEquals(users,instance.getUsers());
    }

    /**
     * Test of getTravels method, of class DataDumpDTO.
     */
    @Test
    public void testGetTravels() {
        System.out.println("getTravels");
        DataDumpDTO instance = new DataDumpDTO();
        List<TravelDumpDTO> expResult = null;
        List<TravelDumpDTO> result = instance.getTravels();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTravels method, of class DataDumpDTO.
     */
    @Test
    public void testSetTravels() {
        System.out.println("setTravels");
        List<TravelDumpDTO> travels = new ArrayList<>();
        DataDumpDTO instance = new DataDumpDTO();
        instance.setTravels(travels);
        assertEquals(travels,instance.getTravels());
    }

    /**
     * Test of getLocations method, of class DataDumpDTO.
     */
    @Test
    public void testGetLocations() {
        System.out.println("getLocations");
        DataDumpDTO instance = new DataDumpDTO();
        List<LocationDTO> expResult = null;
        List<LocationDTO> result = instance.getLocations();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocations method, of class DataDumpDTO.
     */
    @Test
    public void testSetLocations() {
        System.out.println("setLocations");
        List<LocationDTO> locations = new ArrayList<>();
        DataDumpDTO instance = new DataDumpDTO();
        instance.setLocations(locations);
        assertEquals(locations,instance.getLocations());
    }

    /**
     * Test of getEvents method, of class DataDumpDTO.
     */
    @Test
    public void testGetEvents() {
        System.out.println("getEvents");
        DataDumpDTO instance = new DataDumpDTO();
        List<EventDTO> expResult = null;
        List<EventDTO> result = instance.getEvents();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEvents method, of class DataDumpDTO.
     */
    @Test
    public void testSetEvents() {
        System.out.println("setEvents");
        List<EventDTO> events = new ArrayList<>();
        DataDumpDTO instance = new DataDumpDTO();
        instance.setEvents(events);
        assertEquals(events,instance.getEvents());
    }

    /**
     * Test of getEventTypes method, of class DataDumpDTO.
     */
    @Test
    public void testGetEventTypes() {
        System.out.println("getEventTypes");
        DataDumpDTO instance = new DataDumpDTO();
        List<EventTypeDTO> expResult = null;
        List<EventTypeDTO> result = instance.getEventTypes();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventTypes method, of class DataDumpDTO.
     */
    @Test
    public void testSetEventTypes() {
        System.out.println("setEventTypes");
        List<EventTypeDTO> eventTypes = new ArrayList<>();
        DataDumpDTO instance = new DataDumpDTO();
        instance.setEventTypes(eventTypes);
        assertEquals(eventTypes,instance.getEventTypes());
    }
    
}

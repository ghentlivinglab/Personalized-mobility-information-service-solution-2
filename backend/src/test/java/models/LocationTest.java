/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.mail.internet.AddressException;
import models.address.Street;
import models.address.Address;
import models.address.City;
import models.event.EventType;
import models.exceptions.InvalidCountryCodeException;
import models.services.Email;
import models.services.Service;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class LocationTest {

    public LocationTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class) 
    public void testConstructor_1(){
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(null, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testConstructor_2(){
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, null, 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
    }
    
    @Test
    public void testConstructor_3(){
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),null);
    }
    
    /**
     * Test of getAddress method, of class Location.
     */
    @Test
    public void testGetAddress() throws InvalidCountryCodeException {
        System.out.println("getAddress");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        Address expResult = address;
        Address result = instance.getAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Location.
     */
    @Test
    public void testGetName() throws InvalidCountryCodeException {
        System.out.println("getName");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        String expResult = "home";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRadius method, of class Location.
     */
    @Test
    public void testGetRadius() throws InvalidCountryCodeException {
        System.out.println("getRadius");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        int expResult = 1;
        int result = instance.getRadius();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMuted method, of class Location.
     */
    @Test
    public void testGetMuteNptofications() throws InvalidCountryCodeException {
        System.out.println("getMuted");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        boolean expResult = false;
        boolean result = instance.isMuted();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAddress method, of class Location.
     */
    @Test
    public void testSetAddress() throws InvalidCountryCodeException {
        System.out.println("setAddress");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address addressNew = new Address(street, 22, null, new Coordinate(50, 60));
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        instance.setAddress(addressNew);
        assertEquals(instance.getAddress(), addressNew);
    }

    /**
     * Test of setName method, of class Location.
     */
    @Test
    public void testSetName() throws InvalidCountryCodeException {
        System.out.println("setName");
        String name = "name";
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    /**
     * Test of setRadius method, of class Location.
     */
    @Test
    public void testSetRadius() throws InvalidCountryCodeException {
        System.out.println("setRadius");
        int radius = 10;
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        instance.setRadius(radius);
        assertEquals(radius, instance.getRadius());
    }


    /**
     * Test of getServices method, of class Location.
     */
    @Test
    public void testGetServices() throws InvalidCountryCodeException {
        System.out.println("getServices");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        List<Service> expResult = new ArrayList<Service>();
        List<Service> result = instance.getServices();
        assertEquals(expResult, result);
    }

    /**
     * Test of addService method, of class Location.
     */
    @Test
    public void testAddService() throws InvalidCountryCodeException, AddressException {
        System.out.println("addService");
        Service service = new Email("email@email.com");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        instance.addService(service);
        assertFalse(instance.getServices().isEmpty());
    }

    /**
     * Test of removeService method, of class Location.
     */
    @Test
    public void testRemoveService() throws AddressException, InvalidCountryCodeException {
        System.out.println("removeService");
        Service service = new Email("email@email.com");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location instance = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        instance.addService(service);
        instance.removeService(service);
        assertFalse(!instance.getServices().isEmpty());
    }

}

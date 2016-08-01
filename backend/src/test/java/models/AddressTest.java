/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import models.address.Street;
import models.address.Address;
import models.address.City;
import models.exceptions.InvalidCountryCodeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class AddressTest {

    public AddressTest() {
    }

    @Test( expected = IllegalArgumentException.class)
    public void testConstructor_1(){
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = new Address(null, 1, null, new Coordinate(50, 50));
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testConstructor_2(){
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = new Address(street, 1, null, null);
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testConstructor_3(){
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = new Address(null, 1, new Coordinate(50, 50));
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testConstructor_4(){
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = new Address(street, 1, null);
    }
    
    /**
     * Test of equals method, of class Address.
     */
    @Test
    public void testEqualsTrue() throws InvalidCountryCodeException {
        System.out.println("equals");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = new Address(street, 1, null, new Coordinate(50, 50));
        Address instance = new Address(street, 1, null, new Coordinate(50, 50));
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Address.
     */
    @Test
    public void testEqualsFalse() throws InvalidCountryCodeException {
        System.out.println("equals");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = new Address(street, 10, null, new Coordinate(50, 50));
        Address instance = new Address(street, 1, null, new Coordinate(50, 50));
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Address.
     */
    @Test
    public void testEqualsFalse_1() throws InvalidCountryCodeException {
        System.out.println("equals");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = "Hallo";
        Address instance = new Address(street, 1, null, new Coordinate(50, 50));
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Address.
     */
    @Test
    public void testEqualsFalse_2() throws InvalidCountryCodeException {
        System.out.println("equals");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Object obj = null;
        Address instance = new Address(street, 1, null, new Coordinate(50, 50));
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getStreet method, of class Address.
     */
    @Test
    public void testGetStreet() throws InvalidCountryCodeException {
        System.out.println("getStreet");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Address instance = new Address(street, 10, null, new Coordinate(50, 50));
        Street expResult = street;
        Street result = instance.getStreet();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHouseNumber method, of class Address.
     */
    @Test
    public void testGetHouseNumber() throws InvalidCountryCodeException {
        System.out.println("getHouseNumber");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Address instance = new Address(street, 10, null, new Coordinate(50, 50));
        int expResult = 10;
        int result = instance.getHouseNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinates method, of class Address.
     */
    @Test
    public void testGetCoordinates() throws InvalidCountryCodeException {
        System.out.println("getCoordinates");
        City ghent = new City("Ghent", "9000", "Be");
        Street street = new Street("Veldstraat", ghent);
        Coordinate coord = new Coordinate(50, 50);
        Address instance = new Address(street, 10, null, coord);
        Coordinate expResult = coord;
        Coordinate result = instance.getCoordinates();
        assertEquals(expResult, result);
    }

    @Test 
    public void testEquals_True() {
        Street instance1 = new Street("stropstraat",new City("Ghent","9000","BE"));
        Street instance2 = new Street("stropstraat",new City("Ghent","9000","BE"));
        assertTrue(instance1.equals(instance2));
    }
    
    @Test 
    public void testEquals_False_1() {
        Street instance1 = new Street("stropstraat",new City("Ghent","9000","BE"));
        Street instance2 = new Street("stropstraatje",new City("Ghent","9000","BE"));
        assertFalse(instance1.equals(instance2));
    }
    
    @Test 
    public void testEquals_False_2() {
        Street instance1 = new Street("stropstraat",new City("Ghent","9000","BE"));
        Object instance2 = "test";
        assertFalse(instance1.equals(instance2));
    }
    
    @Test 
    public void testEquals() {
        Street instance1 = new Street("stropstraat",new City("Ghent","9000","BE"));
        Object instance2 = null;
        assertFalse(instance1.equals(instance2));
    }
}

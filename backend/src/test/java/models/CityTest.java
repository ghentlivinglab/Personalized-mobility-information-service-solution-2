/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import models.address.City;
import models.exceptions.InvalidCountryCodeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class CityTest {
    
    public CityTest() {
    }

    /**
     * Test of the constructor. 
     * @throws InvalidCountryCodeException 
     */
    @Test
    public void TestConstructor() throws InvalidCountryCodeException{
        System.out.println("constructor test");
        City instance = new City("Ghent","9000","BE");
    }
    
    /**
     * Test of the constructor. 
     * @throws InvalidCountryCodeException 
     */
    @Test ( expected = InvalidCountryCodeException.class)
    public void TestConstructorInvalidCountryCodeException() throws InvalidCountryCodeException{
        System.out.println("constructor test");
        City instance = new City("Ghent","9000","BEL");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void TestConstructor_IllegalArgumentException_1() throws InvalidCountryCodeException{
        System.out.println("constructor test");
        City instance = new City(null,"9000","BEL");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void TestConstructor_IllegalArgumentException_2() throws InvalidCountryCodeException{
        System.out.println("constructor test");
        City instance = new City("Ghent",null,"BEL");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void TestConstructor_IllegalArgumentException_3() throws InvalidCountryCodeException{
        System.out.println("constructor test");
        City instance = new City("Ghent","9000",null);
    }
    
    /**
     * Test of getCityName method, of class City.
     */
    @Test
    public void testGetCityName() throws InvalidCountryCodeException {
        System.out.println("getCityName");
        City instance = new City("Ghent","9000","BE");
        String expResult = "Ghent";
        String result = instance.getCityName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPostalCode method, of class City.
     */
    @Test
    public void testGetPostalCode() throws InvalidCountryCodeException {
        System.out.println("getPostalCode");
        City instance = new City("Ghent","9000","BE");
        String expResult = "9000";
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCountry method, of class City.
     */
    @Test
    public void testGetCountry() throws InvalidCountryCodeException {
        System.out.println("getCountry");
        City instance = new City("Ghent","9000","BE");
        String expResult = "BE";
        String result = instance.getCountry();
        assertEquals(expResult, result);
    }
    
}

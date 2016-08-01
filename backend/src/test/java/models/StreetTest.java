/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import models.address.Street;
import models.address.City;
import models.exceptions.InvalidCountryCodeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class StreetTest {
    
    public StreetTest() {
    }

    @Test ( expected = IllegalArgumentException.class)
    public void testConstructor_1(){
        Street instance = new Street(null,new City("Ghent","9000","BE"));
    }
    
    @Test ( expected = IllegalArgumentException.class)
    public void testConstructor_2(){
        Street instance = new Street("Jozef Plateaustraat",null);
    }
    
    /**
     * Test of getName method, of class Street.
     */
    @Test
    public void testGetName() throws InvalidCountryCodeException {
        System.out.println("getName");
        Street instance = new Street("Veldstraat",new City("Ghent","9000","BE"));
        String expResult = "Veldstraat";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCity method, of class Street.
     */
    @Test
    public void testGetCity() throws InvalidCountryCodeException {
        System.out.println("getCity");
        City ghent = new City("Ghent","9000","BE");
        Street instance = new Street("Veldstraat",ghent);
        City expResult = ghent;
        City result = instance.getCity();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Street.
     */
    @Test
    public void testEqualsTrue() throws InvalidCountryCodeException {
        System.out.println("equals");
        City ghent = new City("Ghent","9000","BE");
        Street instance = new Street("Veldstraat",ghent);
        Object obj = new Street("Veldstraat",ghent);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Street.
     */
    @Test
    public void testEqualsFalse() throws InvalidCountryCodeException {
        System.out.println("equals");
        City ghent = new City("Gent","9000","BE");
        Street instance = new Street("Veldstraat",ghent);
        Object obj = new Street("Bagattenstraat",ghent);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
}

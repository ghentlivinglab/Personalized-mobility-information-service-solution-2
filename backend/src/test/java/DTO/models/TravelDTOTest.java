/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class TravelDTOTest {
    
    public TravelDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        TravelDTO instance = new TravelDTO();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_1() {
        System.out.println("equals");
        Object obj = new TravelDTO();
        TravelDTO instance = new TravelDTO();
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_2() {
        System.out.println("equals");
        TravelDTO instance = new TravelDTO();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_3() {
        System.out.println("equals");
        TravelDTO instance = new TravelDTO();
        Object obj = 1234;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_4() {
        System.out.println("equals");
        TravelDTO instance = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        Object obj = new TravelDTO("id1", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_5() {
        System.out.println("equals");
        TravelDTO instance = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        Object obj = new TravelDTO("id", "name1", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_6() {
        System.out.println("equals");
        TravelDTO instance = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        Object obj = new TravelDTO("id", "name", new String[]{"00:00:11","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_7() {
        System.out.println("equals");
        TravelDTO instance = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        Object obj = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{true}, new AddressDTO(), new AddressDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_8() {
        AddressDTO address = new AddressDTO("straat", "10", "City", "BE", "9012", new CoordinateDTO());
        System.out.println("equals");
        TravelDTO instance = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        Object obj = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, address, new AddressDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class TravelDTO.
     */
    @Test
    public void testEquals_9() {
        System.out.println("equals");
        AddressDTO address = new AddressDTO("straat", "10", "City", "BE", "9012", new CoordinateDTO());
        TravelDTO instance = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), new AddressDTO());
        Object obj = new TravelDTO("id", "name", new String[]{"00:00:00","00:01:00"}, 
                true, new boolean []{}, new AddressDTO(), address);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}

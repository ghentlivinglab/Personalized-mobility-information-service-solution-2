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
public class SourceDTOTest {
    
    public SourceDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getName method, of class SourceDTO.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        SourceDTO instance = new SourceDTO();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIconURL method, of class SourceDTO.
     */
    @Test
    public void testGetIconURL() {
        System.out.println("getIconURL");
        SourceDTO instance = new SourceDTO();
        String expResult = "";
        String result = instance.getIconURL();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class SourceDTO.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "name";
        SourceDTO instance = new SourceDTO();
        instance.setName(name);
        assertEquals(name,instance.getName());
    }

    /**
     * Test of setIconURL method, of class SourceDTO.
     */
    @Test
    public void testSetIconURL() {
        System.out.println("setIconURL");
        String iconURL = "url";
        SourceDTO instance = new SourceDTO();
        instance.setIconURL(iconURL);
        assertEquals(iconURL,instance.getIconURL());
    }

    /**
     * Test of equals method, of class SourceDTO.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        SourceDTO instance = new SourceDTO();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class SourceDTO.
     */
    @Test
    public void testEquals_1() {
        System.out.println("equals");
        Object obj = 1235;
        SourceDTO instance = new SourceDTO();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class SourceDTO.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        SourceDTO instance = new SourceDTO();
        int expResult = 34445;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

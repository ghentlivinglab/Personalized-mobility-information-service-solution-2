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
public class AddressDTOTest {
    
    public AddressDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of equals method, of class AddressDTO.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        AddressDTO instance = new AddressDTO();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class AddressDTO.
     */
    @Test
    public void testEquals_1() {
        System.out.println("equals");
        Object obj = 1234;
        AddressDTO instance = new AddressDTO();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}

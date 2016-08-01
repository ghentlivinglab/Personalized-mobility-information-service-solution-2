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
public class VerificationDTOTest {
    
    public VerificationDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getEmailPin method, of class VerificationDTO.
     */
    @Test
    public void testGetEmailPin() {
        System.out.println("getEmailPin");
        VerificationDTO instance = new VerificationDTO();
        String expResult = null;
        String result = instance.getEmailPin();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmailPin method, of class VerificationDTO.
     */
    @Test
    public void testSetEmailPin() {
        System.out.println("setEmailPin");
        String emailPin = "pin";
        VerificationDTO instance = new VerificationDTO("pin1");
        instance.setEmailPin(emailPin);
        assertEquals("pin",instance.getEmailPin());
    }
    
}

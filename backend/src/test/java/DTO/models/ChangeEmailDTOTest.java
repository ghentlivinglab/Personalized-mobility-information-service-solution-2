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
public class ChangeEmailDTOTest {
    
    public ChangeEmailDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getOldEmail method, of class ChangeEmailDTO.
     */
    @Test
    public void testGetOldEmail() {
        System.out.println("getOldEmail");
        ChangeEmailDTO instance = new ChangeEmailDTO("old@email.com","new@email.com");
        String expResult = "old@email.com";
        String result = instance.getOldEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOldEmail method, of class ChangeEmailDTO.
     */
    @Test
    public void testSetOldEmail() {
        System.out.println("setOldEmail");
        String oldEmail = "newold@email.com";
        ChangeEmailDTO instance = new ChangeEmailDTO();
        instance.setOldEmail(oldEmail);
        assertEquals(oldEmail,instance.getOldEmail());
    }

    /**
     * Test of getNewEmail method, of class ChangeEmailDTO.
     */
    @Test
    public void testGetNewEmail() {
        System.out.println("getNewEmail");
        ChangeEmailDTO instance = new ChangeEmailDTO("old@email.com","new@email.com");
        String expResult = "new@email.com";
        String result = instance.getNewEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNewEmail method, of class ChangeEmailDTO.
     */
    @Test
    public void testSetNewEmail() {
        System.out.println("setNewEmail");
        String newEmail = "newnew@email.com";
        ChangeEmailDTO instance = new ChangeEmailDTO();
        instance.setNewEmail(newEmail);
        assertEquals(newEmail,instance.getNewEmail());
    }
    
}

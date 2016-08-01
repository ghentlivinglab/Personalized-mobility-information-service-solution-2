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
public class ChangePasswordDTOTest {
    
    public ChangePasswordDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getOldPassword method, of class ChangePasswordDTO.
     */
    @Test
    public void testGetOldPassword() {
        System.out.println("getOldPassword");
        ChangePasswordDTO instance = new ChangePasswordDTO("old", "new");
        String expResult = "old";
        String result = instance.getOldPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOldPassword method, of class ChangePasswordDTO.
     */
    @Test
    public void testSetOldPassword() {
        System.out.println("setOldPassword");
        String oldPassword = "new old";
        ChangePasswordDTO instance = new ChangePasswordDTO();
        instance.setOldPassword(oldPassword);
        assertEquals(oldPassword, instance.getOldPassword());
    }

    /**
     * Test of getNewPassword method, of class ChangePasswordDTO.
     */
    @Test
    public void testGetNewPassword() {
        System.out.println("getNewPassword");
        ChangePasswordDTO instance = new ChangePasswordDTO("old", "new");
        String expResult = "new";
        String result = instance.getNewPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNewPassword method, of class ChangePasswordDTO.
     */
    @Test
    public void testSetNewPassword() {
        System.out.println("setNewPassword");
        String newPassword = "new new";
        ChangePasswordDTO instance = new ChangePasswordDTO("old", "new");
        instance.setNewPassword(newPassword);
        assertEquals(newPassword,instance.getNewPassword());
    }
    
}

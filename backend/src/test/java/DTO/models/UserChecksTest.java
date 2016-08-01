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
public class UserChecksTest {
    
    public UserChecksTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of isEmail method, of class UserChecks.
     */
    @Test
    public void testIsEmail() {
        System.out.println("isEmail");
        UserChecks instance = new UserChecksImpl();
        boolean expResult = false;
        boolean result = instance.isEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmail method, of class UserChecks.
     */
    @Test
    public void testSetEmail() {
        System.out.println("setEmail");
        boolean email = true;
        UserChecks instance = new UserChecksImpl();
        instance.setEmail(email);
        assertEquals(email,instance.isEmail());
    }

    /**
     * Test of equals method, of class UserChecks.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        UserChecks instance = new UserChecksImpl();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class UserChecks.
     */
    @Test
    public void testEquals_1() {
        System.out.println("equals");
        Object obj = 1234;
        UserChecks instance = new UserChecksImpl();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class UserChecks.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        UserChecks instance = new UserChecksImpl();
        int expResult = 511;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
    

    public class UserChecksImpl extends UserChecks {
    }
    
}

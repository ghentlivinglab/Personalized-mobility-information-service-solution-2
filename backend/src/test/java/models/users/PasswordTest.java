/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.users;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import models.exceptions.InvalidPasswordException;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author hannedesutter
 */
public class PasswordTest {
    
    public PasswordTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test of isValid method, of class Password.
     */
    @Test
    public void testIsValid() throws Exception {
        System.out.println("isValid");
        Password.isValid("password1");
    }
    
    /**
     * Test of isValid method, of class Password.
     * @throws java.lang.Exception The exception should be thrown.
     */
    @Test(expected = InvalidPasswordException.class)
    public void testIsValidException() throws Exception {
        System.out.println("isValid");
        Password.isValid("p");
    }

    /**
     * Test of checkLength method, of class Password.
     * 
     */
    @Test
    public void testCheckLength() throws Exception {
        System.out.println("checkLength");
        Password.checkLength("password1");
    }

    /**
     * Test of checkLength method, of class Password.
     * @throws java.lang.Exception the exception should be thrown
     */
    @Test(expected = InvalidPasswordException.class)
    public void testCheckLengthException() throws Exception {
        System.out.println("checkLength");
        Password.checkLength("not8");
    }
    
    /**
     * Test of checkContainsNumber method, of class Password.
     */
    @Test
    public void testCheckContainsNumber() throws Exception {
        System.out.println("checkContainsNumber");
        Password.checkContainsNumber("passwor1s");
    }
    
    /**
     * Test of checkContainsNumber method, of class Password.
     * @throws java.lang.Exception the exception should be thrown
     */
    @Test(expected = InvalidPasswordException.class)
    public void testCheckContainsNumberException() throws Exception {
        System.out.println("checkContainsNumber");
        Password.checkContainsNumber("notanumberinthispassword");
    }

    /**
     * Test of getStringPassword method, of class Password.
     */
    @Test
    public void testGetStringPassword() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    }

    /**
     * Test of setPassword method, of class Password.
     */
    @Test
    public void testSetPassword() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("setPassword");
        String password = "wachtwoord12";
        Password instance = new Password("wachtwoord2");
        instance.setPassword(password);
        assertTrue(instance.checkSamePassword(password));
    }

    /**
     * Test of checkSamePassword method, of class Password.
     */
    @Test
    public void testCheckSamePassword_True() throws Exception {
        System.out.println("checkSamePassword");
        String password = "wachtwoord1";
        Password instance = new Password("wachtwoord1");
        boolean expResult = true;
        boolean result = instance.checkSamePassword(password);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkSamePassword method, of class Password.
     */
    @Test
    public void testCheckSamePassword_False() throws Exception {
        System.out.println("checkSamePassword");
        String password = "wachtwoord2";
        Password instance = new Password("wachtwoord1");
        boolean expResult = false;
        boolean result = instance.checkSamePassword(password);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSalt method, of class Password.
     */
    @Test
    public void testGetSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
    }

    /**
     * Test of equals method, of class Password.
     */
    @Test
    public void testEquals_False_1() {
        System.out.println("equals");
        Object obj = new Password("wachtwoord2");
        Password instance = new Password("wachtwoord1");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Password.
     */
    @Test
    public void testEquals_False_2() {
        System.out.println("equals");
        Object obj = "hallo";
        Password instance = new Password("wachtwoord1");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Password.
     */
    @Test
    public void testEquals_False_3() {
        System.out.println("equals");
        Object obj = null;
        Password instance = new Password("wachtwoord1");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}

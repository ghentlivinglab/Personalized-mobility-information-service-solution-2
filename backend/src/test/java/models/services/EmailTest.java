/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.services;

import models.services.Email;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class EmailTest {
    
    public EmailTest() {
    }

    @Test
    public void testConstructor_1() throws AddressException {
        Email instance = new Email(new InternetAddress("email@email.com"));
    }
    
    @Test (expected = AddressException.class)
    public void testConstructor_Exception_1() throws AddressException {
        Email instance = new Email(new InternetAddress("email"));
    }
    
    @Test 
    public void testConstructor_2() throws AddressException{
        Email instance = new Email("email@email.com");
    }
    
    @Test (expected = AddressException.class)
    public void testConstructor_Exception_2() throws AddressException{
        Email instance = new Email("email");
    }
    
    @Test
    public void testConstructor_3() throws AddressException{
        Email instance = new Email (new InternetAddress("email@email.com"),true,false);
    }
    
    @Test (expected = AddressException.class)
    public void testConstructor_Exception_3() throws AddressException{
        Email instance = new Email (new InternetAddress("email"),true,false);
    }
    
    @Test (expected = NullPointerException.class)
    public void testConstructor_Exception_5() throws Exception{
        Email instance = new Email(null,true,false); 
    }
    
    @Test
    public void testCopyConstructor() throws AddressException{
        Email instance1 = new Email("email@email.com");
        Email instance2 = new Email(instance1);
        assertEquals(instance1, instance2);
    }
    
    /**
     * Test of getEmailAddress method, of class Email.
     */
    @Test
    public void testGetEmailAddress() throws AddressException {
        System.out.println("getEmailAddress");
        Email instance = new Email(new InternetAddress("email@email.com"));
        InternetAddress expResult = new InternetAddress("email@email.com");
        InternetAddress result = instance.getEmailAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of validateEmail method, of class Email.
     */
    @Test
    public void testValidateEmail() throws Exception {
        System.out.println("validateEmail");
        Email instance = new Email("email@email.com");
        instance.validateEmail();
    }

    /**
     * Test of equals method, of class Email.
     */
    @Test
    public void testEquals() throws AddressException {
        System.out.println("equals");
        Object obj = new Email("email@email.com");
        Email instance = new Email("email@gmail.com");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testHashCode() throws AddressException {
        Object obj = new Email("email@email.com");
        assertEquals( obj.hashCode(), -1799599134);
    }

}

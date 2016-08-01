/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.services;

import javax.mail.internet.AddressException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class ServiceTest {
    
    @Before
    public void setUp() {
    }

    /**
     * Test of isValidated method, of class Service.
     */
    @Test
    public void testIsValidated() throws AddressException {
        System.out.println("isValidated");
        Service instance = new Email("email@email.com");
        boolean expResult = false;
        boolean result = instance.isValidated();
        assertEquals(expResult, result);
    }

    /**
     * Test of validate method, of class Service.
     */
    @Test
    public void testValidate() throws AddressException {
        System.out.println("validate");
        Service instance = new Email("email@email.com");
        instance.validate();
        assertTrue(instance.isValidated());
    }

    /**
     * Test of getName method, of class Service.
     */
    @Test
    public void testGetName() throws AddressException {
        System.out.println("getName");
        Service instance = new Email("email@email.com");
        String expResult = "email";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Service.
     */
    @Test
    public void testSetName() throws AddressException {
        System.out.println("setName");
        String name = "emailtje";
        Service instance = new Email("email@email.com");
        instance.setName(name);
        assertEquals(instance.getName(),name);
    }

    /**
     * Test of getKey method, of class Service.
     */
    @Test
    public void testGetKey() throws AddressException {
        System.out.println("getKey");
        Service instance = new Email("email@email.com");
        String expResult = "email";
        String result = instance.getKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Service.
     */
    @Test
    public void testToString() throws AddressException {
        System.out.println("toString");
        Service instance = new Email("email@email.com");
        String expResult = "email@email.com";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Service.
     */
    @Test
    public void testEquals_True() throws AddressException {
        System.out.println("equals");
        Object obj = new Email("email@email.com");
        Service instance = new Email("email@email.com");
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Service.
     */
    @Test
    public void testEquals_False_1() throws AddressException {
        System.out.println("equals");
        Object obj = null;
        Service instance = new Email("email@email.com");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Service.
     */
    @Test
    public void testEquals_False_2() throws AddressException {
        System.out.println("equals");
        Object obj = new PhoneNumber("0499311921");
        Service instance = new Email("email@email.com");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Service.
     */
    @Test
    public void testEquals_False_3() throws AddressException {
        System.out.println("equals");
        Object obj = new Email("email1@email.com");
        Service instance = new Email("email@email.com");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class Service.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Service instance = new ServiceImpl();
        int expResult = 104601255;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    public class ServiceImpl extends Service {

        public ServiceImpl() {
            super("name");
        }

        public String toString() {
            return "name";
        }
    }
    
}

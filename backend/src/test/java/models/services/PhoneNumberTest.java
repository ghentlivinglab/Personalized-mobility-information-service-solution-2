/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.services;

import models.services.PhoneNumber;
import models.exceptions.InvalidPhoneNumberException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class PhoneNumberTest {
    
    public PhoneNumberTest() {
    }
    
    /**
     * Test of isValid method, of class PhoneNumber.
     */
    @Test
    public void testIsValid() throws Exception {
        System.out.println("isValid");
        String phoneNumber = "0499311921";
        PhoneNumber test = new PhoneNumber(true,phoneNumber);
    }

    /**
     * Test of isValid method, of class PhoneNumber.
     */
    @Test(expected = InvalidPhoneNumberException.class)
    public void testIsValidFalse() throws Exception {
        System.out.println("isValid");
        String phoneNumber = "04919aa";
        PhoneNumber test = new PhoneNumber(phoneNumber);
    }
    
    /**
     * Test of checkLength method, of class PhoneNumber.
     */
    @Test
    public void testCheckLength() throws Exception {
        System.out.println("checkLength");
        String phoneNumber = "0499311921";
        PhoneNumber test = new PhoneNumber(phoneNumber);
    }

    /**
     * Test of checkLength method, of class PhoneNumber.
     * @throws java.lang.Exception
     */
    @Test(expected = InvalidPhoneNumberException.class)
    public void testCheckLengthFalse() throws Exception {
        System.out.println("checkLength");
        String phoneNumber = "04";
        PhoneNumber test = new PhoneNumber(phoneNumber);
    }
    
    /**
     * Test of checkNumbers method, of class PhoneNumber.
     */
    @Test(expected=InvalidPhoneNumberException.class)
    public void testCheckNumbersFalse() throws Exception {
        System.out.println("checkNumbers");
        String phoneNumber = "11o1oo1000";
        PhoneNumber test = new PhoneNumber(phoneNumber);
    }

    /**
     * Test of checkNumbers method, of class PhoneNumber.
     */
    @Test
    public void testCheckNumbersTrue() throws Exception {
        System.out.println("checkNumbers");
        String phoneNumber = "0499311921";
        PhoneNumber test = new PhoneNumber(phoneNumber);
    }
    
    /**
     * Test of equals method, of class PhoneNumber.
     */
    @Test
    public void testEqualsTrue() throws Exception{
        System.out.println("equals");
        Object obj = new PhoneNumber("0499311921");;
        PhoneNumber instance = new PhoneNumber("0499311921");
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals("the phone numbers did not match",expResult, result);
    }

    /**
     * Test of equals method, of class PhoneNumber.
     */
    @Test
    public void testEqualsFalse_1() throws Exception{
        System.out.println("equals");
        Object obj = "";
        PhoneNumber instance = new PhoneNumber("0499311921");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals("the phone numbers did match",expResult, result);
    }
    
    /**
     * Test of equals method, of class PhoneNumber.
     */
    @Test
    public void testEqualsFalse_2() throws Exception{
        System.out.println("equals");
        Object obj = null;
        PhoneNumber instance = new PhoneNumber("0499311921");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals("the phone numbers did match",expResult, result);
    }
    
    @Test
    public void testGetPhoneNumber() {
        PhoneNumber pn = new PhoneNumber("0499322393");
        assertEquals(pn.getPhoneNumber(),"0499322393");
    }
}

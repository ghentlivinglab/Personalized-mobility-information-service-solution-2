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
public class UserDTOTest {
    
    public UserDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getValidated method, of class UserDTO.
     */
    @Test
    public void testGetValidated() {
        System.out.println("getValidated");
        UserDTO instance = new UserDTO();
        ValidatedDTO expResult = new ValidatedDTO();
        ValidatedDTO result = instance.getValidated();
        assertEquals(expResult, result);
    }

    /**
     * Test of getId method, of class UserDTO.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        UserDTO instance = new UserDTO();
        String expResult = "";
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstName method, of class UserDTO.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        UserDTO instance = new UserDTO();
        String expResult = null;
        String result = instance.getFirstName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastName method, of class UserDTO.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");
        UserDTO instance = new UserDTO();
        String expResult = null;
        String result = instance.getLastName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class UserDTO.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        UserDTO instance = new UserDTO();
        String expResult = null;
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmail method, of class UserDTO.
     */
    @Test
    public void testGetEmail() {
        System.out.println("getEmail");
        UserDTO instance = new UserDTO();
        instance.setEmail("");
        String expResult = "";
        String result = instance.getEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of isMuteNotifications method, of class UserDTO.
     */
    @Test
    public void testIsMuteNotifications() {
        System.out.println("isMuteNotifications");
        UserDTO instance = new UserDTO();
        boolean expResult = false;
        boolean result = instance.isMuteNotifications();
        assertEquals(expResult, result);
    }

    /**
     * Test of setValidated method, of class UserDTO.
     */
    @Test
    public void testSetValidated() {
        System.out.println("setValidated");
        ValidatedDTO validated = new ValidatedDTO(true);
        UserDTO instance = new UserDTO();
        instance.setValidated(validated);
        assertEquals(validated,instance.getValidated());
    }

    /**
     * Test of setId method, of class UserDTO.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        String id = "1";
        UserDTO instance = new UserDTO();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of setFirstName method, of class UserDTO.
     */
    @Test
    public void testSetFirstName() {
        System.out.println("setFirstName");
        String firstName = "";
        UserDTO instance = new UserDTO();
        instance.setFirstName(firstName);
        assertEquals(firstName,instance.getFirstName());
    }

    /**
     * Test of setLastName method, of class UserDTO.
     */
    @Test
    public void testSetLastName() {
        System.out.println("setLastName");
        String lastName = "";
        UserDTO instance = new UserDTO();
        instance.setLastName(lastName);
        assertEquals(lastName,instance.getLastName());
    }

    /**
     * Test of setPassword method, of class UserDTO.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "";
        UserDTO instance = new UserDTO();
        instance.setPassword(password);
        assertEquals(password,instance.getPassword());
    }

    /**
     * Test of setEmail method, of class UserDTO.
     */
    @Test
    public void testSetEmail() {
        System.out.println("setEmail");
        String email = "";
        UserDTO instance = new UserDTO();
        instance.setEmail(email);
        assertEquals(email, instance.getEmail());
    }

    /**
     * Test of setMuteNotifications method, of class UserDTO.
     */
    @Test
    public void testSetMuteNotifications() {
        System.out.println("setMuteNotifications");
        boolean muteNotifications = true;
        UserDTO instance = new UserDTO();
        instance.setMuteNotifications(muteNotifications);
        assertTrue(instance.isMuteNotifications());
    }

    /**
     * Test of hashCode method, of class UserDTO.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        UserDTO instance = new UserDTO();
        int expResult = 32182226;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = new UserDTO();
        UserDTO instance = new UserDTO();
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_1() {
        System.out.println("equals");
        Object obj = null;
        UserDTO instance = new UserDTO();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_2() {
        System.out.println("equals");
        UserDTO instance = new UserDTO();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_3() {
        System.out.println("equals");
        UserDTO instance = new UserDTO();
        Object obj = 1234;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_4() {
        System.out.println("equals");
        UserDTO instance = new UserDTO("id","email@email.com",true,new ValidatedDTO());
        Object obj = new UserDTO("id1","email@email.com",true,new ValidatedDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_5() {
        System.out.println("equals");
        UserDTO instance = new UserDTO("id","email@email.com",true,new ValidatedDTO());
        Object obj = new UserDTO("id","email1@email.com",true,new ValidatedDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_6() {
        System.out.println("equals");
        UserDTO instance = new UserDTO("id","email@email.com",true,new ValidatedDTO());
        Object obj = new UserDTO("id","email@email.com",false,new ValidatedDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_7() {
        System.out.println("equals");
        UserDTO instance = new UserDTO("id","email@email.com",true,new ValidatedDTO());
        Object obj = new UserDTO("id","email@email.com",true,new ValidatedDTO(true));
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    
    /**
     * Test of equals method, of class UserDTO.
     */
    @Test
    public void testEquals_8() {
        System.out.println("equals");
        UserDTO instance = new UserDTO("id", "first", "last", "password1", "email@email.com", true, new ValidatedDTO());
        Object obj = new UserDTO("id", "first", "last", "password12", "email@email.com", true, new ValidatedDTO());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}

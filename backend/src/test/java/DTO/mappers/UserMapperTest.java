/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.UserDTO;
import DTO.models.ValidatedDTO;
import models.services.Email;
import models.services.PhoneNumber;
import models.users.Password;
import models.users.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class UserMapperTest {
    
    public UserMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class UserMapper.
     */
    @Test
    public void testConvertToDTO_User_String() throws Exception {
        System.out.println("convertToDTO");
        Email email = new Email("email@email.com");
        email.validate();
        User user = new User(null, null, "wachtwoord1", "email@email.com",false);
        PhoneNumber pn = new PhoneNumber("0499311921");
        pn.validate();
        user.addService(pn);
        user.addService(email);
        String id = "1";
        UserMapper instance = new UserMapper();
        UserDTO expResult = new UserDTO("1", "email@email.com", false, new ValidatedDTO(false));
        UserDTO result = instance.convertToDTO(user,id);
        assertEquals(expResult.getEmail(), result.getEmail());
        assertEquals(expResult.getFirstName(), result.getFirstName());
        assertEquals(expResult.getLastName(), result.getLastName());
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getValidated().isEmail(), result.getValidated().isEmail());
        assertEquals(expResult.isMuteNotifications(),result.isMuteNotifications());
        assertEquals(expResult.isMuteNotifications(),result.isMuteNotifications());
        assertEquals(expResult.getPassword(),result.getPassword());
    }

    /**
     * Test of convertToDTO method, of class UserMapper.
     */
    @Test
    public void testConvertToDTO_User_int() throws Exception {
        System.out.println("convertToDTO");
        Email email = new Email("email@email.com");
        email.validate();
        User user = new User("hanne", "de sutter", "wachtwoord1", "email@email.com",false);
        PhoneNumber pn = new PhoneNumber("0499311921");
        pn.validate();
        user.addService(pn);
        user.addService(email);
        int id = 1;
        UserMapper instance = new UserMapper();
        UserDTO expResult = new UserDTO("1", "email@email.com", false, new ValidatedDTO());
        expResult.setFirstName("hanne");
        expResult.setLastName("de sutter");
        UserDTO result = instance.convertToDTO(user,id);
        assertEquals(expResult.getEmail(), result.getEmail());
        assertEquals(expResult.getFirstName(), result.getFirstName());
        assertEquals(expResult.getLastName(), result.getLastName());
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getValidated().isEmail(), result.getValidated().isEmail());
        assertEquals(expResult.isMuteNotifications(),result.isMuteNotifications());
        assertEquals(expResult.getPassword(),result.getPassword());
    }

    /**
     * Test of convertFromDTO method, of class UserMapper.
     */
    @Test
    public void testConvertFromDTO() throws Exception {
        System.out.println("convertFromDTO");
        UserDTO userdto = new UserDTO("1","","","wachtwoord1","email@email.com", false, new ValidatedDTO(false));
        UserMapper instance = new UserMapper();
        User user = new User("", "", "wachtwoord1", "email@email.com",false);
        PhoneNumber pn = new PhoneNumber("0499311921");
        pn.validate();
        User expResult = user;
        User result = instance.convertFromDTO(userdto,false);
        assertEquals(expResult.getEmail(), result.getEmail());
        assertEquals(expResult.getFirstName(), result.getFirstName());
        assertEquals(expResult.getLastName(), result.getLastName());
        assertEquals(expResult.getServices(), result.getServices());
        assertEquals(expResult.getTravels(), result.getTravels());
        assertTrue(expResult.getPassword().checkSamePassword("wachtwoord1"));
        
        
        
    }
    
}

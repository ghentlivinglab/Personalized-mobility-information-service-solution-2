/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import static database_v2.models.relational.AccountDBModel.PUSH_TOKEN_ATTRIBUTE;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class AccountDBModelTest {
    
    private static final String tableName = "account";

    public static final Attribute isAdminAttribute
            = new Attribute("isAdmin", "is_admin", AttributeType.BOOLEAN, tableName, false);
    public static final Attribute isOperatorAttribute
            = new Attribute("isOperator", "is_operator", AttributeType.BOOLEAN, tableName, false);
    
    public AccountDBModelTest() {
    }
    
    
    @Before
    public void setUp() {
    }

    /**
     * Test of setId method, of class AccountDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        AccountDBModel instance = new AccountDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class AccountDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        AccountDBModel instance = new AccountDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getActiveAttributeList method, of class AccountDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        List<Attribute> out = new ArrayList<>();
        out.add(new Attribute("email", "email", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("password", "password", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("salt", "salt", AttributeType.BYTE, tableName, true));
        out.add(isAdminAttribute);
        out.add(isOperatorAttribute);
        
        out.add(new Attribute("firstname", "first_name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("lastname", "last_name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("muteNotifications", "mute_notifications", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("emailValidated", "email_validated", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("refreshToken", "refresh_token", AttributeType.TEXT, tableName, false));
        out.add(AccountDBModel.PUSH_TOKEN_ATTRIBUTE);
        List<Attribute> result = instance.getActiveAttributeList();
        for(int i=0;i<result.size();i++){
            assertEquals("fail: "+out.get(i).getAttribute()+ ", " + result.get(i).getAttribute(),out.get(i),result.get(i));
        }
        //assertEquals(out, result);
    }

    /**
     * Test of getAllAttributeList method, of class AccountDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        List<Attribute> out = new ArrayList<>();
        out.add(new Attribute("email", "email", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("password", "password", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("salt", "salt", AttributeType.BYTE, tableName, true));
        out.add(new Attribute("refreshToken", "refresh_token", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("firstname", "first_name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("lastname", "last_name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("muteNotifications", "mute_notifications", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("emailValidated", "email_validated", AttributeType.BOOLEAN, tableName, false));
        out.add(isAdminAttribute);
        out.add(isOperatorAttribute);
        out.add(new Attribute("pushToken", "push_token", AttributeType.TEXT, tableName, false));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(out, result);
    }

    /**
     * Test of getTableName method, of class AccountDBModel.
     */
    @Test
    public void testGetTableName() {
        AccountDBModel instance = new AccountDBModel();
        String expResult = "account";
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class AccountDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        AccountDBModel instance = new AccountDBModel();
        String expResult = "accountid";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAccountId method, of class AccountDBModel.
     */
    @Test
    public void testGetAccountId() {
        AccountDBModel instance = new AccountDBModel();
        instance.setId(1);
        Integer expResult = 1;
        Integer result = instance.getAccountId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAccountIdAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetAccountIdAttribute() {
        Attribute expResult =  new Attribute("accountId", "accountid", AttributeType.INTEGER, "account", false);
        Attribute result = AccountDBModel.getAccountIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAccountId method, of class AccountDBModel.
     */
    @Test
    public void testSetAccountId() {
        Integer accountId = 1;
        AccountDBModel instance = new AccountDBModel();
        instance.setAccountId(accountId);
        assertEquals(accountId, instance.getAccountId());
    }

    /**
     * Test of getEmail method, of class AccountDBModel.
     */
    @Test
    public void testGetEmail() {
        AccountDBModel instance =  new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        String expResult = "email@email.com";
        String result = instance.getEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmailAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetEmailAttribute() {
        Attribute expResult = new Attribute("email", "email", AttributeType.TEXT, "account", true);
        Attribute result = AccountDBModel.getEmailAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmail method, of class AccountDBModel.
     */
    @Test
    public void testSetEmail() {
        String email = "email@gmail.com";
        AccountDBModel instance =  new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setEmail(email);
        assertEquals(email,instance.getEmail());
    }

    /**
     * Test of getFirstname method, of class AccountDBModel.
     */
    @Test
    public void testGetFirstname() {
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        String expResult = "firstname";
        String result = instance.getFirstname();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstNameAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetFirstNameAttribute() {
        Attribute expResult = new Attribute("firstname", "first_name", AttributeType.TEXT, "account", false);
        Attribute result = AccountDBModel.getFirstNameAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFirstname method, of class AccountDBModel.
     */
    @Test
    public void testSetFirstname() {
        String firstname = "new name";
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setFirstname(firstname);
        assertEquals(firstname, instance.getFirstname());
    }

    /**
     * Test of getLastname method, of class AccountDBModel.
     */
    @Test
    public void testGetLastname() {
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        String expResult = "lastname";
        String result = instance.getLastname();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastNameAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetLastNameAttribute() {
        Attribute expResult = new Attribute("lastname", "last_name", AttributeType.TEXT, "account", false);
        Attribute result = AccountDBModel.getLastNameAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastname method, of class AccountDBModel.
     */
    @Test
    public void testSetLastname() {
        System.out.println("setLastname");
        String lastname = "last name ";
        AccountDBModel instance = new AccountDBModel();
        instance.setLastname(lastname);
        assertEquals(lastname, instance.getLastname());
    }

    /**
     * Test of getPassword method, of class AccountDBModel.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        String expResult = "password1";
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPasswordAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetPasswordAttribute() {
        Attribute expResult = new Attribute("password", "password", AttributeType.TEXT, "account", true);
        Attribute result = AccountDBModel.getPasswordAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPassword method, of class AccountDBModel.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "wachtwoord1";
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setPassword(password);
        assertEquals(password,instance.getPassword());
    }

    /**
     * Test of getMuteNotifications method, of class AccountDBModel.
     */
    @Test
    public void testGetMuteNotifications() {
        System.out.println("getMuteNotifications");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        Boolean expResult = true;
        Boolean result = instance.getMuteNotifications();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMuteNotificationsAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetMuteNotificationsAttribute() {
        Attribute expResult = new Attribute("muteNotifications", "mute_notifications", AttributeType.BOOLEAN, "account", false);
        Attribute result = AccountDBModel.getMuteNotificationsAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMuteNotifications method, of class AccountDBModel.
     */
    @Test
    public void testSetMuteNotifications() {
        System.out.println("setMuteNotifications");
        Boolean muteNotifications = false;
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setMuteNotifications(muteNotifications);
        assertEquals(muteNotifications,instance.getMuteNotifications());
    }

    /**
     * Test of getEmailValidated method, of class AccountDBModel.
     */
    @Test
    public void testGetEmailValidated() {
        System.out.println("getEmailValidated");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        Boolean expResult = true;
        Boolean result = instance.getEmailValidated();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmailValidatedAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetEmailValidatedAttribute() {
        Attribute expResult = new Attribute("emailValidated", "email_validated", AttributeType.BOOLEAN, "account", false);
        Attribute result = AccountDBModel.getEmailValidatedAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmailValidated method, of class AccountDBModel.
     */
    @Test
    public void testSetEmailValidated() {
        System.out.println("setEmailValidated");
        Boolean emailValidated = false;
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setEmailValidated(emailValidated);
        assertEquals(emailValidated,instance.getEmailValidated());
    }

    /**
     * Test of getRefreshToken method, of class AccountDBModel.
     */
    @Test
    public void testGetRefreshToken() {
        System.out.println("getRefreshToken");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        String expResult = "refreshToken";
        String result = instance.getRefreshToken();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRefreshTokenAttribute method, of class AccountDBModel.
     */
    @Test
    public void testGetRefreshTokenAttribute() {
        Attribute expResult = new Attribute("refreshToken", "refresh_token", AttributeType.TEXT, "account", false);
        Attribute result = AccountDBModel.getRefreshTokenAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRefreshToken method, of class AccountDBModel.
     */
    @Test
    public void testSetRefreshToken() {
        System.out.println("setRefreshToken");
        String refreshToken = "token";
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setRefreshToken(refreshToken);
        assertEquals(refreshToken,instance.getRefreshToken());
    }

    /**
     * Test of getSalt method, of class AccountDBModel.
     */
    @Test
    public void testGetSalt() {
        System.out.println("getSalt");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        byte[] expResult = new byte[10];
        byte[] result = instance.getSalt();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of setSalt method, of class AccountDBModel.
     */
    @Test
    public void testSetSalt() {
        System.out.println("setSalt");
        byte[] salt = new byte [9];
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setSalt(salt);
        assertEquals(salt, instance.getSalt());
    }

    /**
     * Test of getIsAdmin method, of class AccountDBModel.
     */
    @Test
    public void testGetIsAdmin() {
        System.out.println("getIsAdmin");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        Boolean expResult = false;
        Boolean result = instance.getIsAdmin();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsAdmin method, of class AccountDBModel.
     */
    @Test
    public void testSetIsAdmin() {
        System.out.println("setIsAdmin");
        Boolean isAdmin = true;
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setIsAdmin(isAdmin);
        assertEquals(isAdmin, instance.getIsAdmin());
    }

    /**
     * Test of getIsOperator method, of class AccountDBModel.
     */
    @Test
    public void testGetIsOperator() {
        System.out.println("getIsOperator");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        Boolean expResult = false;
        Boolean result = instance.getIsOperator();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsOperator method, of class AccountDBModel.
     */
    @Test
    public void testSetIsOperator() {
        System.out.println("setIsOperator");
        Boolean isOperator = false;
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        instance.setIsOperator(isOperator);
        assertEquals(isOperator, instance.getIsOperator());
    }

    /**
     * Test of equals method, of class AccountDBModel.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class AccountDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        AccountDBModel instance = new AccountDBModel("email@email.com", "firstname", "lastname", "password1",
                true, true, "refreshToken", new byte[10], false, false, "pushToken");
        int expResult = 1249590098;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

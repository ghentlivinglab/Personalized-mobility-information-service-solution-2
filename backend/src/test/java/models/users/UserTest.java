package models.users;

import java.time.LocalTime;
import models.exceptions.InvalidPasswordException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import models.address.Address;
import models.address.City;
import models.Coordinate;
import models.Location;
import models.address.Street;
import models.Travel;
import models.event.EventType;
import models.services.Email;
import models.services.PhoneNumber;
import models.services.Service;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class UserTest {

    public UserTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     */
    @Test
    public void testConstructor1() throws Exception {
        User user1 = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        assertEquals(user1.getFirstName(),"firstName");
        assertEquals(user1.getLastName(),"lastName");
        assertTrue(user1.getPassword().checkSamePassword("wachtwoord1"));
        assertEquals(user1.getEmail().getEmailAddress(),new InternetAddress("email@email.com"));
        assertEquals(user1.getLocations(),new HashMap<>());
        assertEquals(user1.travels,new HashMap<>());
        assertEquals(user1.isMuted(), true);
    }

    /**
     * Test of getFirstName method, of class User.
     */
    @Test
    public void testGetFirstName() throws Exception {
        System.out.println("getFirstName");
        User instance = new User("first_name", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        String expResult = "first_name";
        String result = instance.getFirstName();
        assertEquals("the first name did not match.", expResult, result);
    }

    /**
     * Test of setFirstName method, of class User.
     */
    @Test
    public void testSetFirstName() throws Exception {
        System.out.println("setFirstName");
        String firstName = "new first name";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setFirstName(firstName);
        assertEquals("The first name did not match the new one.", firstName, instance.getFirstName());
    }

    /**
     * Test of getLastName method, of class User.
     */
    @Test
    public void testGetLastName() throws Exception {
        System.out.println("getLastName");
        User instance =new User("firstName", "last_name", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        String expResult = "last_name";
        String result = instance.getLastName();
        assertEquals("the last name did not match.", expResult, result);
    }

    /**
     * Test of setLastName method, of class User.
     */
    @Test
    public void testSetLastName() throws Exception {
        System.out.println("setLastName");
        String lastName = "new last name";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setLastName(lastName);
        assertEquals("The last name did not match the new last name.", lastName, instance.getLastName());
    }

    /**
     * Test of getPassword method, of class User.
     */
    @Test
    public void testGetPassword() throws Exception {
        System.out.println("getPassword");
        Password pass = new Password("wachtwoord1");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        Password expResult = pass;
        Password result = instance.getPassword();
        assertTrue("The password dit not match", result.checkSamePassword("wachtwoord1"));
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPassword_Password() throws Exception {
        System.out.println("setPassword");
        Password password = new Password("password11");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setPassword(password);
        assertEquals("The password did not match the new password", password, instance.getPassword());
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPassword_String() throws Exception {
        System.out.println("setPassword");
        String password = "password11";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setPassword(password);
        assertTrue("The password did not match the new password.",instance.getPassword().checkSamePassword(password));

    }

    @Test(expected = InvalidPasswordException.class)
    public void testSetPassword_StringException() throws Exception {
        System.out.println("setPassword");
        String password = "pass";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setPassword(password);
    }

    /**
     * Test of getEmail method, of class User.
     */
    @Test
    public void testGetEmail() throws Exception {
        System.out.println("getEmail");
        Email email = new Email("email@email.com");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        Email expResult = email;
        Email result = instance.getEmail();
        assertEquals("The email did not match.", expResult.getEmailAddress().toString(), result.getEmailAddress().toString());
    }

    /**
     * Test of setEmail method, of class User.
     */
    @Test
    public void testSetEmail_Email() throws Exception {
        System.out.println("setEmail");
        Email email = new Email("newEmail@mail.com");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setEmail(email);
        assertEquals("The email did not match the new email address", email.getEmailAddress(), instance.getEmail().getEmailAddress());
    }

    /**
     * Test of setEmail method, of class User.
     */
    @Test
    public void testSetEmail_String() throws Exception {
        System.out.println("setEmail");
        String email = "newEmail@mail.com";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setEmail(email);
        assertEquals("The email did not match the new email address", email, instance.getEmail().getEmailAddress().toString());
    }

    /**
     * Test of setEmail method, of class User.
     */
    @Test(expected = AddressException.class)
    public void testSetEmail_StringExeption() throws Exception {
        System.out.println("setEmail");
        String email = "newEmail";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setEmail(email);
    }

    /**
     * Test of getLocations method, of class User.
     */
    @Test
    public void testGetLocations() throws Exception {
        System.out.println("getLocations");
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location location = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        Map<Integer, Location> locations = new HashMap<>();
        locations.put(1, location);
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                locations, new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        Map<Integer, Location> result = instance.getLocations();
        assertEquals("the maps of the location did not match.", locations, result);

    }

    /**
     * Test of addLocation method, of class User.
     */
    @Test
    public void testAddLocation() throws Exception {
        System.out.println("addLocation");
        Integer id = 1;
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location location = new Location(address, "home", 1, false,new HashMap<Integer,EventType>(),new ArrayList<Service>());
        Map<Integer, Location> locations = new HashMap<>();
        locations.put(1, location);
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addLocation(id, location);
        assertFalse("The location was not added.", instance.getLocations().isEmpty());
        assertEquals(locations,instance.getLocations());
    }

    /**
     * Test of removeLocation method, of class User.
     */
    @Test
    public void testRemoveLocation() throws Exception {
        System.out.println("removeLocation");
        Integer id = 1;
        City ghent = new City("Ghent", "9000", "BE");
        Street street = new Street("Veldstraat", ghent);
        Address address = new Address(street, 1, null, new Coordinate(50, 50));
        Location location = new Location(address, "home", 1, 
                false,new HashMap<>(),new ArrayList<>());
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addLocation(id, location);
        instance.removeLocation(id);
        assertTrue("The location was deleted.", instance.getLocations().isEmpty());
    }

    /**
     * Test of addService method, of class User.
     */
    @Test
    public void testAddService() throws Exception {
        System.out.println("addService");
        Service service = new PhoneNumber("0499311921");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addService(service);
        assertEquals("The service was not added.", service, instance.getService(PhoneNumber.KEY));
    }

    /**
     * Test of removeService method, of class User.
     */
    @Test
    public void testRemoveService() throws Exception {
        System.out.println("removeService");
        Service service = new PhoneNumber("0499311921");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addService(service);
        instance.removeService(PhoneNumber.KEY);
        assertTrue(instance.getServices().isEmpty());
    }

    /**
     * Test of addTravel method, of class User.
     */
    @Test
    public void testAddTravel() throws Exception {
        System.out.println("addTravel");
        Integer id = 1;
        Address address1 = new Address(new Street("name", new City("Ghent", "9000", "BE")), 1, new Coordinate(50, 50));
        Address address2 = new Address(new Street("name", new City("Ghent", "9000", "BE")), 100, new Coordinate(50, 50));
        Travel travel = new Travel("name", LocalTime.MIDNIGHT, LocalTime.NOON, false, address1, address2, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addTravel(id, travel);
        assertEquals("The travel was not added.", 1, instance.getTravels().size());
    }

    /**
     * Test of removeTravel method, of class User.
     */
    @Test
    public void testRemoveTravel() throws Exception {
        System.out.println("removeTravel");
        Integer id = 1;
        Address address1 = new Address(new Street("name", new City("Ghent", "9000", "BE")), 1, new Coordinate(50, 50));
        Address address2 = new Address(new Street("name", new City("Ghent", "9000", "BE")), 100, new Coordinate(50, 50));
        Travel travel = new Travel("name", LocalTime.MIDNIGHT, LocalTime.NOON, false, address1, address2, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        HashMap <Integer,Travel> travels = new HashMap<>();
        travels.put(1,travel);
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), travels, true, "refreshToken", false, false, "pushToken");
        instance.removeTravel(id);
        assertTrue(instance.getTravels().isEmpty());
    }

    /**
     * Test of getTravels method, of class User.
     */
    @Test
    public void testGetTravels() throws Exception {
        System.out.println("getTravels");
        Address address1 = new Address(new Street("name", new City("Ghent", "9000", "BE")), 1, new Coordinate(50, 50));
        Address address2 = new Address(new Street("name", new City("Ghent", "9000", "BE")), 100, new Coordinate(50, 50));
        Travel travel1 = new Travel("name2", LocalTime.MIDNIGHT, LocalTime.NOON, false, address1, address2,
                new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        Travel travel2 = new Travel("name1", LocalTime.NOON, LocalTime.MIDNIGHT, true, address2, address1,
                new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        Map<Integer, Travel> travels = new HashMap<>();
        travels.put(1, travel1);
        travels.put(2, travel2);
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), travels, true, "refreshToken", false, false, "pushToken");
        for (Integer i : instance.getTravels().keySet()) {
            assertTrue("The Map is different.", travels.get(i) != null);
        }
    }

    /**
     * Test of getMuteNotifications method, of class User.
     */
    @Test
    public void testGetMuteNotifications() throws Exception {
        System.out.println("getMuteNotifications");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        Boolean expResult = true;
        Boolean result = instance.isMuted();
        assertEquals("The value of muteNotifications is wrong.", expResult, result);

    }

    /**
     * Test of setMuteNotifications method, of class User.
     */
    @Test
    public void testSetMuteNotifications() throws Exception {
        System.out.println("setMuteNotifications");
        Boolean muteNotifications = false;
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setMuted(muteNotifications);
        assertFalse("The value was not changed", instance.isMuted());
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEquals() throws Exception {
        System.out.println("equals");
        Object obj = new User("first", "last", new Password("wachtwoord2"), "email@mail.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", true, false, "pushToken");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true, 
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        assertFalse("The objects did match.", instance.equals(obj));

    }
    
    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEquals_2() throws Exception {
        System.out.println("equals");
        Object obj = null;
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        assertFalse("The objects did match.", instance.equals(obj));

    }
    
    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEquals_3() throws Exception {
        System.out.println("equals");
        Object obj = "String";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        assertFalse("The objects did match.", instance.equals(obj));
    }
    
    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEquals_4() throws Exception {
        System.out.println("equals");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        assertTrue("The objects didn't match.", instance.equals(instance));
    }

    /**
     * Test of getEmailAsString method, of class User.
     */
    @Test
    public void testGetEmailAsString() throws AddressException {
        System.out.println("getEmailAsString");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        String expResult = "email@email.com";
        String result = instance.getEmailAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getService method, of class User.
     */
    @Test
    public void testGetService() throws Exception{
        System.out.println("getService");
        String key = "phonenumber";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addService(new PhoneNumber("0499311921"));
        Service expResult = new PhoneNumber("0499311921");
        Service result = instance.getService(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getService method, of class User.
     */
    @Test
    public void testGetService_null() throws Exception{
        System.out.println("getService");
        String key = "email";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.addService(new PhoneNumber("0499311921"));
        Service expResult = null;
        Service result = instance.getService(key);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServices method, of class User.
     */
    @Test
    public void testGetServices() throws Exception {
        System.out.println("getServices");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        Map<String, Service> expResult = new HashMap<>();
        Map<String, Service> result = instance.getServices();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRefreshToken method, of class User.
     */
    @Test
    public void testSetRefreshToken() throws Exception {
        System.out.println("setRefreshToken");
        String refreshToken = "refreshToken";
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.setRefreshToken(refreshToken);
        assertEquals(instance.getRefreshToken(),refreshToken);
    }

    /**
     * Test of getRefreshToken method, of class User.
     */
    @Test
    public void testGetRefreshToken() throws Exception {
        System.out.println("getRefreshToken");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        String expResult = "refreshToken";
        String result = instance.getRefreshToken();
        assertEquals(expResult, result);
    }

    /**
     * Test of isAdmin method, of class User.
     */
    @Test
    public void testIsAdmin() throws Exception {
        System.out.println("isAdmin");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        boolean expResult = false;
        boolean result = instance.isAdmin();
        assertEquals(expResult, result);
    }

    /**
     * Test of makeAdmin method, of class User.
     */
    @Test
    public void testMakeAdmin() throws Exception{
        System.out.println("makeAdmin");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.makeAdmin();
        assertTrue(instance.isAdmin);
    }

    /**
     * Test of isOperator method, of class User.
     */
    @Test
    public void testIsOperator() throws AddressException {
        System.out.println("isOperator");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        boolean expResult = false;
        boolean result = instance.isOperator();
        assertEquals(expResult, result);
    }

    /**
     * Test of makeOperator method, of class User.
     */
    @Test
    public void testMakeOperator() throws Exception{
        System.out.println("makeOperator");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.makeOperator();
        assertTrue(instance.isOperator());
    }

    /**
     * Test of makeUser method, of class User.
     */
    @Test
    public void testMakeUser() throws Exception {
        System.out.println("makeUser");
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", true, false, "pushToken");
        instance.makeUser();
        assertFalse(instance.isAdmin() || instance.isOperator());
    }

    /**
     * Test of updateData method, of class User.
     */
    @Test
    public void testUpdateData() throws AddressException {
        System.out.println("updateData");
        User user = new User("Name", "Name", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), false, "refreshToken", false, false, "pushToken");
        user.addService(new PhoneNumber("0499311921"));
        User instance = new User("firstName", "lastName", new Password("wachtwoord1"), "email@email.com", true,
                new HashMap<>(), new HashMap<>(), true, "refreshToken", false, false, "pushToken");
        instance.updateData(user);
        assertEquals(user.getFirstName(),instance.getFirstName());
        assertEquals(user.getLastName(),instance.getLastName());
        //assertEquals(user.services,instance.services);
        assertEquals(user.isMuted(),instance.isMuted());
    }

    
}

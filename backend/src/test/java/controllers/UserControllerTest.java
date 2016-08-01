package controllers;

import DTO.models.ChangeEmailDTO;
import DTO.models.ChangePasswordDTO;
import DTO.models.UserDTO;
import DTO.models.VerificationDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.javatuples.Pair;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
 
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Coordinate;
import models.Location;
import models.Route;
import models.Transportation;
import models.Travel;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.event.Event;
import models.event.EventType;
import models.users.User;
 
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import org.springframework.core.task.TaskExecutor;



/**
 * Testclass for testing controllers.UserController
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    
    private MockMvc mockMVC;
 
    @Mock
    private Database databaseMock;
    @Mock
    private TaskExecutor executor;
    @Mock
    private UserAuthenticationInterceptor userAuthenticationInterceptor;
    
    private UserDTO dto;
    private User user;
    private User admin;
    private Event eventLocation;
    private Event eventRoute;
 
    @Before
    public void setUp() throws Exception{
        mockMVC = MockMvcBuilders.standaloneSetup(new UserController(databaseMock, executor))
                .build();
        
        when(userAuthenticationInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class))).thenReturn(Boolean.TRUE);
        
        dto = new UserDTO();
        dto.setFirstName("firstname");
        dto.setLastName("lastname");
        dto.setPassword("password1");
        dto.setEmail("first.last@email.com");
        dto.setMuteNotifications(true);
        
        user = new User("firstname", "lastname", "password1", "first.last@email.com", true);
        admin = new User("firstname", "lastname", "password1", "first.last@email.com", true);
        admin.makeAdmin();
        
        Location location = new Location(new Address(new Street("straat", new City("stad", "9000", "BE")), 0, new Coordinate(50.0, 50.0))
                , "location", 0, true, new HashMap<>(), new ArrayList<>());
        
        LocalTime beginDate = LocalTime.parse("10:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endDate = LocalTime.parse("12:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        Travel travel = new Travel("travel1", beginDate, endDate, false, new Address(new Street("straat", new City("stad", "9000", "BE")), 0, new Coordinate(50.0, 50.0))
                , new Address(new Street("straat", new City("stad", "9000", "BE")), 0, new Coordinate(50.0, 50.0))
                , new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        Route route = new Route(new ArrayList<>(),new ArrayList<>(), Transportation.TRAIN, new HashMap<>(), true, new ArrayList<>());
        
        travel.addRoute(1, route);
        user.addLocation(1, location);
        user.addTravel(1, travel);
        
        
        }

    /**
     * Test of getUsers method, of class UserController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testGetUsers() throws Exception{
        User firstUser = new User("testtest123", "test1@test.be", true);
        
        User secondUser = new User("testtest123", "test2@test.be", false);
        
        when(databaseMock.listAllUsers()).thenReturn(Arrays.asList(new Pair(1,firstUser), new Pair(2, secondUser)));
        when(databaseMock.getUser(1)).thenReturn(admin);
        
        mockMVC.perform(get("/user")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].email", is("test1@test.be")))
                .andExpect(jsonPath("$[0].mute_notifications", is(true)))
                .andExpect(jsonPath("$[0].validated.email", is(false)))
                .andExpect(jsonPath("$[0].password", is("")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].email", is("test2@test.be")))
                .andExpect(jsonPath("$[1].mute_notifications", is(false)))
                .andExpect(jsonPath("$[1].validated.email", is(false)))
                .andExpect(jsonPath("$[1].password", is("")));
        verify(databaseMock, times(1)).listAllUsers();
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getUsers method of class UserController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetUsers_DataAccessException() throws Exception {
        when(databaseMock.listAllUsers()).thenThrow(new DataAccessException(""));
        when(databaseMock.getUser(1)).thenReturn(admin);
        
        mockMVC.perform(get("/user")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).listAllUsers();
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of Getuserevents
     * @throws Exception should not be throwing this
     */
    @Test
    public void testGetUserEvents() throws Exception{
        Date date = new Date();
        String now = Event.DATE_FORMATTER.format(date);
        eventLocation = new Event(new Coordinate(50.0,50.0), false, now, date.getTime(),
                "eventLocation", "address", new EventType("", new ArrayList<>()));
        
        eventRoute = new Event(new Coordinate(50.0,50.0), true, now, date.getTime(), "eventRoute",
                "address", new EventType("", new ArrayList<>()));
        
        Set<Pair<String, Event>> dbResponse = new HashSet<>();
        dbResponse.add(new Pair<>("testlocation", eventLocation));
        when(databaseMock.getRecentEventsOfUser(1)).thenReturn(dbResponse);
        
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        
        
        mockMVC.perform(get("/user/1/events")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testlocation")))
                .andExpect(jsonPath("$[0].active", is(false)))
                .andExpect(jsonPath("$[0].description", is("eventLocation")));
        verify(databaseMock, times(1)).getRecentEventsOfUser(1);
        verifyNoMoreInteractions(databaseMock);
    }

    /**
     * Test of addUser method, of class UserController.
     * This is the test with all data filled in
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testAddUser() throws Exception {
        when(databaseMock.createUser(any(User.class))).thenReturn(1);
        
        
        mockMVC.perform(post("/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.first_name", is("firstname")))
                .andExpect(jsonPath("$.last_name", is("lastname")))
                .andExpect(jsonPath("$.email", is("first.last@email.com")))
                .andExpect(jsonPath("$.mute_notifications", is(true)))
                .andExpect(jsonPath("$.validated.email", is(false)))
                .andExpect(jsonPath("$.password", is("")));
        
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(databaseMock, times(1)).createUser(userCaptor.capture());
        verifyNoMoreInteractions(databaseMock);
        
        User captValue = (User) userCaptor.getValue();
        assertThat(captValue.getFirstName(), is("firstname"));
        assertThat(captValue.getLastName(), is("lastname"));
        assertThat(captValue.getEmail().toString(), is("first.last@email.com"));
        assertThat(captValue.getPassword().checkSamePassword(dto.getPassword()), is(true));
    }
    
    /**
     * Test of addUser method of class UserController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddUser_DataAccessException() throws Exception {
        when(databaseMock.createUser(any(User.class))).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(post("/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).createUser(any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addUser method of class UserController when throwing InvalidPasswordException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddUser_InvalidPasswordException() throws Exception {
        dto.setPassword("pass"); //wrong password
        
        when(databaseMock.createUser(any(User.class))).thenReturn(1);
        
        mockMVC.perform(post("/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    
    /**
     * Test of addUser method of class UserController when throwing AddressException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddUser_AddressException() throws Exception {
        dto.setEmail("firstlast"); //wrong emailaddress
        
        when(databaseMock.createUser(any(User.class))).thenReturn(1);
        
        mockMVC.perform(post("/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addUser method of class UserController when throwing AlreadyExistsException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddUser_AlreadyExistsException() throws Exception {
        
        when(databaseMock.createUser(any(User.class))).thenThrow(new AlreadyExistsException());
        
        mockMVC.perform(post("/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isConflict());
        
        verify(databaseMock, times(1)).createUser(any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }

    /**
     * Test of getUser method, of class UserController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testGetUser() throws Exception {
        
        User firstUser = new User("testtest123", "test1@test.be", true);
        
        when(databaseMock.getUser(1)).thenReturn(firstUser);
        
        mockMVC.perform(get("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.email", is("test1@test.be")))
                .andExpect(jsonPath("$.mute_notifications", is(true)))
                .andExpect(jsonPath("$.validated.email", is(false)))
                .andExpect(jsonPath("$.password", is("")));
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getUser method of class UserController when throwing RecordNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetUser_RecordNotFoundException() throws Exception {
        when(databaseMock.getUser(1)).thenThrow(new RecordNotFoundException());
        
        mockMVC.perform(get("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getUser method of class UserController when throwing NumberFormatException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetUser_NumberFormatException() throws Exception {
        when(databaseMock.getUser(1)).thenThrow(new NumberFormatException());
        
        mockMVC.perform(get("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getUser method of class UserController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetUser_DataAccessException() throws Exception {
        when(databaseMock.getUser(1)).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(get("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }

    /**
     * Test of editUser method, of class UserController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testEditUser() throws Exception {
        dto.setFirstName("lastname"); //change up names & mutenotifications
        dto.setLastName("firstname");
        dto.setMuteNotifications(false);
        String passwordUser = user.getPassword().getStringPassword();
        
        when(databaseMock.getUser(1)).thenReturn(user);
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.first_name", is("lastname")))
                .andExpect(jsonPath("$.last_name", is("firstname")))
                .andExpect(jsonPath("$.email", is("first.last@email.com")))
                .andExpect(jsonPath("$.mute_notifications", is(false)))
                .andExpect(jsonPath("$.validated.email", is(false)))
                .andExpect(jsonPath("$.password", is("")));
        
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).updateUser(idCaptor.capture(),userCaptor.capture());
        verifyNoMoreInteractions(databaseMock);
        
        Integer idValue = idCaptor.getValue();
        assertThat(idValue, is(1));
        
        User captValue = (User) userCaptor.getValue();
        assertThat(captValue.getFirstName(), is("lastname"));
        assertThat(captValue.getLastName(), is("firstname"));
        assertThat(captValue.getEmail().toString(), is("first.last@email.com"));
        assertThat(captValue.getPassword().getStringPassword(), is(passwordUser));
        
    }
    
    /**
     * Test of editUser method of class UserController when throwing RecordNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_RecordNotFoundExceptionGetUser() throws Exception {
        when(databaseMock.getUser(1)).thenThrow(new RecordNotFoundException());
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editUser method of class UserController when throwing RecordNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_RecordNotFoundExceptionUpdateUser() throws Exception {
        when(databaseMock.getUser(1)).thenReturn(user);
        doThrow(new RecordNotFoundException()).when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editUser method of class UserController when throwing NumberFormatException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_NumberFormatException() throws Exception {
        when(databaseMock.getUser(1)).thenReturn(user);
        doThrow(new RecordNotFoundException()).when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/a")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editUser method of class UserController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_DataAccessExceptionGetUser() throws Exception {
        when(databaseMock.getUser(1)).thenThrow(new DataAccessException(""));
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editUser method of class UserController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_DataAccessExceptionUpdateUser() throws Exception {
        when(databaseMock.getUser(1)).thenReturn(user);
        doThrow(new DataAccessException("")).when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editUser method of class UserController when throwing AlreadyExistsException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_AlreadyExistsExceptionUpdateUser() throws Exception {
        when(databaseMock.getUser(1)).thenReturn(user);
        doThrow(new AlreadyExistsException()).when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isConflict());
        
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editUser method of class UserController when throwing AddressException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditUser_AddressException() throws Exception {
        dto.setEmail("firstlast"); //wrong emailaddress
        
        when(databaseMock.getUser(1)).thenReturn(user);
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(put("/user/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    

    /**
     * Test of deleteUser method, of class UserController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testDeleteUser() throws Exception{
        
        doNothing().when(databaseMock).deleteUser(anyInt());
        
        mockMVC.perform(delete("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isNoContent());
        
        verify(databaseMock, times(1)).deleteUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteUser method of class UserController when throwing RecordNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteUser_RecordNotFoundException() throws Exception {
        doThrow(new RecordNotFoundException()).when(databaseMock).deleteUser(anyInt());
        
        mockMVC.perform(delete("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).deleteUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteUser method of class UserController when throwing NumberFormatException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteUser_NumberFormatException() throws Exception {
        doThrow(new NumberFormatException()).when(databaseMock).deleteUser(anyInt());
        
        mockMVC.perform(delete("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).deleteUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteUser method of class UserController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteUser_DataAccessException() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).deleteUser(anyInt());

        mockMVC.perform(delete("/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).deleteUser(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testVerifyUser() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(1,user));
        
        VerificationDTO verification = new VerificationDTO("1234562");
        
        
        mockMVC.perform(post("/user/test@test.be/verify")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verification))
                .header("Authorization", "1")
        )
                .andExpect(status().isForbidden());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testVerifyUserDataAccessException() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).getUserByEmail(anyString());
        
        VerificationDTO verification = new VerificationDTO("1234562");
        
        
        mockMVC.perform(post("/user/test@test.be/verify")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verification))
                .header("Authorization", "1")
        )
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testVerifyUserRecordNotFoundException() throws Exception {
        doThrow(new RecordNotFoundException()).when(databaseMock).getUserByEmail(anyString());      
        
        VerificationDTO verification = new VerificationDTO("1234562");
        
        
        mockMVC.perform(post("/user/test@test.be/verify")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verification))
                .header("Authorization", "1")
        )
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testUserReverifyEmail() throws Exception {
        
        mockMVC.perform(post("/user/1/reverify_email")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("test@test.be"))
                .header("Authorization", "1")
        )
                .andExpect(status().isOk());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    
    @Test
    public void testForgotPasswordUser() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(1,user));
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        
        mockMVC.perform(post("/user/forgot_password")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("test@test.be"))
                .header("Authorization", "1")
        )
                .andExpect(status().isOk());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testForgotPasswordUserDataAccessException() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).getUserByEmail(anyString());
        
        mockMVC.perform(post("/user/forgot_password")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("test@test.be"))
                .header("Authorization", "1")
        )
                .andExpect(status().isOk());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testChangeEmailUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        doNothing().when(databaseMock).updateUserEmail(anyInt(), any(User.class), anyString());
        
        ChangeEmailDTO emails = new ChangeEmailDTO(user.getEmailAsString(), "new@mail.be");
        
        mockMVC.perform(post("/user/1/change_email")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emails))
                .header("Authorization", "1")
        )
                .andExpect(status().isOk());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).updateUserEmail(anyInt(), any(User.class), anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testChangePasswordUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        
        ChangePasswordDTO emails = new ChangePasswordDTO("password1", "password2");
        
        mockMVC.perform(post("/user/1/change_password")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emails))
                .header("Authorization", "1")
        )
                .andExpect(status().isOk());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
}

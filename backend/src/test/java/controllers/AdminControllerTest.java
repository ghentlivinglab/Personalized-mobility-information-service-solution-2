package controllers;

import database_v2.controlLayer.Database;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.javatuples.Pair;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
    
    private MockMvc mockMVC;
 
    @Mock
    private Database databaseMock;
    @Mock
    private AdminAuthenticationInterceptor adminAuthenticationInterceptorMock;
    
    private User admin;
    private User user;
    private User operator;
    
    @Before
    public void setUp() throws Exception{
        mockMVC = MockMvcBuilders.standaloneSetup(new AdminController(databaseMock))
                    .build();
        when(adminAuthenticationInterceptorMock.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class))).thenReturn(Boolean.TRUE);
        
        user = new User("user", "lastname", "user1234", "user@email.com", true);
        admin = new User("admin", "lastname", "admin1234", "admin@email.com", true);
        admin.makeAdmin();
        operator = new User("operator", "lastname", "operator1234", "operator@email.com", true);
        operator.makeOperator();
        
        //common databasemocks
        when(databaseMock.listAllAdmins()).thenReturn(Arrays.asList(new Pair(3,admin)));
        when(databaseMock.listAllOperators()).thenReturn(Arrays.asList(new Pair(2,operator)));
        when(databaseMock.listAllUsers()).thenReturn(Arrays.asList(new Pair(1,user),new Pair(2,operator),new Pair(3,admin)));
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        doNothing().when(databaseMock).deleteUser(anyInt());
        
        //for testing datadump
        Event eventLocation = new Event(new Coordinate(50.0,50.0), false, "2016-05-15T17:24:03.881", 120, "eventLocation", "address", new EventType("", new ArrayList<>()));
        
        Event eventRoute = new Event(new Coordinate(50.0,50.0), true, "2016-05-15T17:24:03.881", 120, "eventRoute", "address", new EventType("", new ArrayList<>()));
        
        Location location = new Location(new Address(new Street("straat", new City("stad", "9000", "BE")), 0, new Coordinate(50.0, 50.0))
                , "location", 0, true, new HashMap<>(), new ArrayList<>());
        
        LocalTime beginDate = LocalTime.parse("10:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endDate = LocalTime.parse("12:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        Travel travel = new Travel("travel1", beginDate, endDate, false, new Address(new Street("straat", new City("stad", "9000", "BE")), 0, new Coordinate(50.0, 50.0))
                , new Address(new Street("straat", new City("stad", "9000", "BE")), 0, new Coordinate(50.0, 50.0))
                , new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        Route route = new Route(new ArrayList<>(),new ArrayList<>(), Transportation.TRAIN, new HashMap<>(), true, new ArrayList<>());
        
        travel.addRoute(1, route);
        
        //mockings for datadump
        when(databaseMock.listAllEvents()).thenReturn(Arrays.asList(new Pair("locationevent",eventLocation), new Pair("locationroute", eventRoute)));
        when(databaseMock.listAlltravels()).thenReturn(Arrays.asList(new Pair(1,travel)));
        when(databaseMock.listAllLocations()).thenReturn(Arrays.asList(new Pair(1,location)));
        when(databaseMock.getEventtypes()).thenReturn(Arrays.asList(new Pair(1,new EventType("testtype", new ArrayList<>()))));
    }
    
    @Test
    public void testGetAdmins() throws Exception {
        mockMVC.perform(get("/admin/admin")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("3")))
                .andExpect(jsonPath("$[0].email", is("admin@email.com")))
                .andExpect(jsonPath("$[0].mute_notifications", is(true)))
                .andExpect(jsonPath("$[0].validated.email", is(false)))
                .andExpect(jsonPath("$[0].password", is("")));
        verify(databaseMock, times(1)).listAllAdmins();
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testGetAdminsDataAccessException() throws Exception {
        when(databaseMock.listAllAdmins()).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/admin/admin")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        verify(databaseMock, times(1)).listAllAdmins();
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testCreateAdmin() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(1,user));
        mockMVC.perform(post("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isCreated());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(user.isAdmin(), is(true));
    }
    
    @Test
    public void testCreateAdminDataAccessException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new DataAccessException(""));
        mockMVC.perform(post("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testCreateAdminRecordNotFoundException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(post("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testCreateAdminAlreadyExistsException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(1,user));
        doThrow(new AlreadyExistsException()).when(databaseMock).updateUser(anyInt(),any(User.class));
        mockMVC.perform(post("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isConflict());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteAdmin() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(3,admin));
        mockMVC.perform(put("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("adminemailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isNoContent());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(admin.isAdmin(), is(false));
    }
    
    @Test
    public void testDeleteAdminDataAccessException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new DataAccessException(""));
        mockMVC.perform(put("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("adminemailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteAdminRecordNotFoundException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(put("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("adminemailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteAdminAlreadyExistsException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(3,admin));
        doThrow(new AlreadyExistsException()).when(databaseMock).updateUser(anyInt(),any(User.class));
        mockMVC.perform(put("/admin/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("adminemailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isConflict());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testGetOperators() throws Exception {
        mockMVC.perform(get("/admin/operator")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("2")))
                .andExpect(jsonPath("$[0].email", is("operator@email.com")))
                .andExpect(jsonPath("$[0].mute_notifications", is(true)))
                .andExpect(jsonPath("$[0].validated.email", is(false)))
                .andExpect(jsonPath("$[0].password", is("")));
        verify(databaseMock, times(1)).listAllOperators();
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testGetOperatorsDataAccessException() throws Exception {
        when(databaseMock.listAllOperators()).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/admin/operator")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        verify(databaseMock, times(1)).listAllOperators();
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testCreateOperator() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(1,user));
        mockMVC.perform(post("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isCreated());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(user.isOperator(), is(true));
    }
    
    @Test
    public void testCreateOperatorDataAccessException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new DataAccessException(""));
        mockMVC.perform(post("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testCreateOperatorRecordNotFoundException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(post("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testCreateOperatorAlreadyExistsException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(1,user));
        doThrow(new AlreadyExistsException()).when(databaseMock).updateUser(anyInt(),any(User.class));
        mockMVC.perform(post("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("useremailtest"))
                .header("Authorization", "1"))
                .andExpect(status().isConflict());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteOperator() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(2,operator));
        mockMVC.perform(put("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("operatoremail"))
                .header("Authorization", "1"))
                .andExpect(status().isNoContent());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(operator.isOperator(), is(false));
    }
    
    @Test
    public void testDeleteOperatorDataAccessException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new DataAccessException(""));
        mockMVC.perform(put("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("operatoremail"))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteOperatorRecordNotFoundException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(put("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("operatoremail"))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteOperatorAlreadyExistsException() throws Exception {
        when(databaseMock.getUserByEmail(anyString())).thenReturn(new Pair(2,operator));
        doThrow(new AlreadyExistsException()).when(databaseMock).updateUser(anyInt(),any(User.class));
        mockMVC.perform(put("/admin/operator")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes("operatoremail"))
                .header("Authorization", "1"))
                .andExpect(status().isConflict());
        
        verify(databaseMock, times(1)).getUserByEmail(anyString());
        verify(databaseMock, times(1)).updateUser(anyInt(), any(User.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDeleteUser() throws Exception {
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void testDeleteUserDataAccessException() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).deleteUser(anyInt());
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testDeleteUserRecordNotFoundException() throws Exception {
        doThrow(new RecordNotFoundException()).when(databaseMock).deleteUser(anyInt());
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testDataDump() throws Exception {
        mockMVC.perform(get("/admin/data_dump")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users", hasSize(3)))
                .andExpect(jsonPath("$.users[0].id", is("1")))
                .andExpect(jsonPath("$.users[1].id", is("2")))
                .andExpect(jsonPath("$.users[2].id", is("3")))
                .andExpect(jsonPath("$.travels", hasSize(1)))
                .andExpect(jsonPath("$.travels[0].travel.id", is("1")))
                .andExpect(jsonPath("$.travels[0].routes", hasSize(1)))
                .andExpect(jsonPath("$.travels[0].routes[0].id", is("1")))
                .andExpect(jsonPath("$.point_of_interests", hasSize(1)))
                .andExpect(jsonPath("$.point_of_interests[0].id", is("1")))
                .andExpect(jsonPath("$.events", hasSize(2)))
                .andExpect(jsonPath("$.events[0].id", is("locationevent")))
                .andExpect(jsonPath("$.events[1].id", is("locationroute")))
                .andExpect(jsonPath("$.eventtypes", hasSize(1)))
                .andExpect(jsonPath("$.eventtypes[0].type", is("testtype")));
        
        verify(databaseMock, times(1)).listAllUsers();
        verify(databaseMock, times(1)).listAlltravels();
        verify(databaseMock, times(1)).listAllLocations();
        verify(databaseMock, times(1)).listAllEvents();
        verify(databaseMock, times(1)).getEventtypes();
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testDataDumpDataAccessException() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).listAllUsers();
        mockMVC.perform(get("/admin/data_dump")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
}

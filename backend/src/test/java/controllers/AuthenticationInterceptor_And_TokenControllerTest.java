package controllers;

import DTO.models.AccessTokenDTO;
import DTO.models.CredentialsDTO;
import DTO.models.RefreshTokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import database_v2.controlLayer.Database;
import java.util.ArrayList;
import models.Coordinate;
import models.event.Event;
import models.event.EventType;
import models.users.User;
import org.javatuples.Pair;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationInterceptor_And_TokenControllerTest {
    
    private MockMvc mockMVC;
 
    @Mock
    private Database databaseMock;
    @Mock
    private TaskExecutor executor;
    
    
    private User user;
    private User admin;
    private User operator;
    static private String refreshTokenUser;
    static private String refreshTokenAdmin;
    static private String refreshTokenOperator;
    static private String accessTokenUser;
    static private String accessTokenAdmin;
    static private String accessTokenOperator;
    
    @Before
    public void setUp() throws Exception{
        String [] userPattern = {"/user/*"};
        String [] eventPattern = {"/event/*","/event"};
        String [] adminPattern = {"/admin/*","/admin/user/*"};
        mockMVC = MockMvcBuilders.standaloneSetup(new TokenController(databaseMock),
                                                 new UserController(databaseMock, executor),
                                                 new EventController(databaseMock),
                                                 new AdminController(databaseMock))
                .addMappedInterceptors(userPattern, new UserAuthenticationInterceptor(databaseMock))
                .addMappedInterceptors(eventPattern, new EventAuthenticationInterceptor(databaseMock))
                .addMappedInterceptors(adminPattern, new AdminAuthenticationInterceptor(databaseMock))
                .build();
        
        user = new User("user", "lastname", "user1234", "user@email.com", true);
        user.setRefreshToken(refreshTokenUser);
        admin = new User("admin", "lastname", "admin1234", "admin@email.com", true);
        admin.makeAdmin();
        admin.setRefreshToken(refreshTokenAdmin);
        operator = new User("operator", "lastname", "operator1234", "operator@email.com", true);
        operator.makeOperator();
        operator.setRefreshToken(refreshTokenOperator);
        
        doNothing().when(databaseMock).updateUser(anyInt(), any(User.class));
        //when(userControllerMock.getUser(anyString(), anyString())).thenReturn(new ResponseEntity(HttpStatus.OK));
    }
    
    @Test
    public void AAA_testAddRefreshToken_AddAccessToken_TokenController() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        CredentialsDTO userCred = new CredentialsDTO(user.getEmailAsString(), "user1234");
        CredentialsDTO operatorCred = new CredentialsDTO(operator.getEmailAsString(), "operator1234");
        CredentialsDTO adminCred = new CredentialsDTO(admin.getEmailAsString(), "admin1234");
        CredentialsDTO wrongUserPassCred  = new CredentialsDTO(user.getEmailAsString(), "password123");
        
        when(databaseMock.getUserByEmail(user.getEmailAsString())).thenReturn(new Pair(1,user));
        when(databaseMock.getUserByEmail(operator.getEmailAsString())).thenReturn(new Pair(2,operator));
        when(databaseMock.getUserByEmail(admin.getEmailAsString())).thenReturn(new Pair(3,admin));
        
        when(databaseMock.getUser(1)).thenReturn(user);
        when(databaseMock.getUser(2)).thenReturn(operator);
        when(databaseMock.getUser(3)).thenReturn(admin);
        
        MvcResult userResult = mockMVC
                .perform(post("/refresh_token/regular")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userCred)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        MvcResult operatorResult = mockMVC
                .perform(post("/refresh_token/regular")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operatorCred)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        MvcResult adminResult = mockMVC
                .perform(post("/refresh_token/regular")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adminCred)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        mockMVC.perform(post("/refresh_token/regular")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongUserPassCred)))
                .andExpect(status().isUnauthorized());
        
        RefreshTokenDTO userRefresh = mapper.readValue(userResult.getResponse().getContentAsString(), RefreshTokenDTO.class);
        RefreshTokenDTO operatorRefresh = mapper.readValue(operatorResult.getResponse().getContentAsString(), RefreshTokenDTO.class);
        RefreshTokenDTO adminRefresh = mapper.readValue(adminResult.getResponse().getContentAsString(), RefreshTokenDTO.class);
        
        AuthenticationInterceptor_And_TokenControllerTest.refreshTokenUser = userRefresh.getToken();
        AuthenticationInterceptor_And_TokenControllerTest.refreshTokenOperator = operatorRefresh.getToken();
        AuthenticationInterceptor_And_TokenControllerTest.refreshTokenAdmin = adminRefresh.getToken();
        user.setRefreshToken(refreshTokenUser);
        operator.setRefreshToken(refreshTokenOperator);
        admin.setRefreshToken(refreshTokenAdmin);
        
        
        MvcResult userAccessResult = mockMVC
                .perform(post("/access_token")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userRefresh))
                .header("Authorization", userRefresh.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        MvcResult operatorAccessResult = mockMVC
                .perform(post("/access_token")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operatorRefresh))
                .header("Authorization", operatorRefresh.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        MvcResult adminAccessResult = mockMVC
                .perform(post("/access_token")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adminRefresh))
                .header("Authorization", adminRefresh.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        mockMVC.perform(post("/access_token")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adminRefresh))
                .header("Authorization", userRefresh.getToken()))
                .andExpect(status().isUnauthorized());
        
       AccessTokenDTO userAccess = mapper.readValue(userAccessResult.getResponse().getContentAsString(), AccessTokenDTO.class);
       AccessTokenDTO operatorAccess = mapper.readValue(operatorAccessResult.getResponse().getContentAsString(), AccessTokenDTO.class);
       AccessTokenDTO adminAccess = mapper.readValue(adminAccessResult.getResponse().getContentAsString(), AccessTokenDTO.class); 
       
       AuthenticationInterceptor_And_TokenControllerTest.accessTokenUser = userAccess.getAccessToken();
       AuthenticationInterceptor_And_TokenControllerTest.accessTokenOperator = operatorAccess.getAccessToken();
       AuthenticationInterceptor_And_TokenControllerTest.accessTokenAdmin = adminAccess.getAccessToken();
    }
    
    @Test
    public void testUserAuthenticationController() throws Exception {
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        
        //using a get request so that we need no real mocking
        
        mockMVC.perform(get("/user/1")
                .header("Authorization", accessTokenUser))
                .andExpect(status().isOk());
        
        mockMVC.perform(get("/user/1"))
                .andExpect(status().isUnauthorized());
        
        mockMVC.perform(get("/user/1")
                .header("Authorization", accessTokenAdmin))
                .andExpect(status().isUnauthorized());
        
        //expiredexception
        String accessWrong = accessTokenUser.substring(0,accessTokenUser.length()-2);
        mockMVC.perform(get("/user/1")
                .header("Authorization", accessWrong))
                .andExpect(status().isUnauthorized());
        
        //accesstoken not valid
        String [] splitted = accessTokenUser.split("&t=");
        accessWrong = splitted[0]+"&t="+splitted[1].substring(2);
        mockMVC.perform(get("/user/1")
                .header("Authorization", accessWrong))
                .andExpect(status().isUnauthorized());
    }
    
    @Test 
    public void tesEventAuthenticationController() throws Exception {
        //using a delete request so that we need no real mocking
        doNothing().when(databaseMock).deleteEvent(anyString());
        
        when(databaseMock.getUser(anyInt())).thenReturn(operator);
        
        mockMVC.perform(delete("/event/eventid")
                .header("Authorization", accessTokenOperator))
                .andExpect(status().isNoContent());
        
        mockMVC.perform(delete("/event/eventid"))
                .andExpect(status().isUnauthorized());
        
        mockMVC.perform(delete("/event/eventid")
                .header("Authorization", accessTokenUser))
                .andExpect(status().isUnauthorized());
        
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        
        mockMVC.perform(delete("/event/eventid")
                .header("Authorization", accessTokenUser))
                .andExpect(status().isUnauthorized());
        
        when(databaseMock.getUser(anyInt())).thenReturn(admin);
        
        mockMVC.perform(delete("/event/eventid")
                .header("Authorization", accessTokenAdmin))
                .andExpect(status().isNoContent());
        
        //expiredexception
        String accessWrong = accessTokenAdmin.substring(0,accessTokenAdmin.length()-2);
        mockMVC.perform(delete("/event/eventid")
                .header("Authorization", accessWrong))
                .andExpect(status().isUnauthorized());
        
        Event event = new Event(new Coordinate(0, 0), true, "2016-05-15T17:24:03.881", 1, "desc", "address", new EventType("typ", new ArrayList<>()));
        when(databaseMock.getEvent(anyString())).thenReturn(event);
        mockMVC.perform(get("/event/eventid")
                .header("Authorization", accessWrong))
                .andExpect(status().isOk());
        
    }
    
    @Test
    public void testAdminAuthenticationController() throws Exception {
        //using a delete request so that we need no real mocking
        doNothing().when(databaseMock).deleteUser(anyInt());
        
        when(databaseMock.getUser(anyInt())).thenReturn(admin);
        
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", accessTokenAdmin))
                .andExpect(status().isNoContent());
        
        mockMVC.perform(delete("/admin/user/1"))
                .andExpect(status().isUnauthorized());
        
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", accessTokenOperator))
                .andExpect(status().isUnauthorized());
        
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", accessTokenUser))
                .andExpect(status().isUnauthorized());
        
        //expiredexception
        String accessWrong = accessTokenAdmin.substring(0,accessTokenAdmin.length()-2);
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", accessWrong))
                .andExpect(status().isUnauthorized());
        
        //accesstoken not valid
        String [] splitted = accessTokenAdmin.split("&t=");
        accessWrong = splitted[0]+"&t="+splitted[1].substring(2);
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", accessWrong))
                .andExpect(status().isUnauthorized());
        
        when(databaseMock.getUser(anyInt())).thenReturn(operator);
        mockMVC.perform(delete("/admin/user/1")
                .header("Authorization", accessTokenOperator))
                .andExpect(status().isUnauthorized());
    }
    
}

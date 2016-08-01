/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DTO.models.AddressDTO;
import DTO.models.CoordinateDTO;
import DTO.models.TravelDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.exceptions.RecordNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Coordinate;
import models.Travel;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.repetition.Repetition;
import models.repetition.RepetitionWeek;
import models.users.User;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.anyInt;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Testclass for testing controllers.TravelController
 */
@RunWith(MockitoJUnitRunner.class)
public class TravelControllerTest {
    
    private MockMvc mockMVC;
 
    @Mock
    private Database databaseMock;
    @Mock
    private UserAuthenticationInterceptor userAuthenticationInterceptor;
    
    private User user;
    private LocalTime beginDate;
    private LocalTime endDate;
    private Address startPoint;
    private Address endPoint;
    private RepetitionWeek recurring;
    private ArrayList<Repetition>recurlist;
    private Travel firstTravel;
    private Travel secondTravel;
    private int nextId = 3;
    private TravelDTO dto;
    private String [] timeInterval;
    
    
    @Before
    public void setUp() throws Exception {
        mockMVC = MockMvcBuilders.standaloneSetup(new TravelController(databaseMock))
                .build();
        when(userAuthenticationInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class))).thenReturn(Boolean.TRUE);
        user = new User("firstname", "lastname", "password1", "first.last@email.com", true);
        beginDate = LocalTime.parse("10:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        endDate = LocalTime.parse("12:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        startPoint = new Address(new Street("teststreet", new City("testcity", "1234", "BE")), 1, new Coordinate(50.0, 60.0));
        endPoint = new Address(new Street("teststreet", new City("testcity", "1234", "BE")), 5, new Coordinate(55.0, 65.0));
        recurring = new RepetitionWeek();
         
        recurlist = new ArrayList<>();
        recurlist.add(recurring);
        
        firstTravel = new Travel("travel1", beginDate, endDate, false, startPoint, endPoint, recurlist, new HashMap<>(), new ArrayList<>());
        secondTravel = new Travel("travel2", beginDate, endDate, true, endPoint, startPoint, recurlist, new HashMap<>(), new ArrayList<>());
        
        user.addTravel(1, firstTravel);
        user.addTravel(2, secondTravel);
        
        dto = new TravelDTO();
        dto.setName("dtotravel");
        timeInterval = new String [2];
        timeInterval[0] = "08:15:30";
        timeInterval[1] = "09:15:30";
        dto.setTimeInterval(timeInterval);
        dto.setArrivalTime(false);
        dto.setRecurring(recurring.getAllWeek());
        dto.setStartpoint(new AddressDTO("dtostart", "1", "dtocity", "DC", "1234", new CoordinateDTO(10.0,20.0)));
        dto.setEndpoint(new AddressDTO("dtoend", "5", "dtocity", "DC", "1234", new CoordinateDTO(15.0,25.0)));
    
        when(databaseMock.getUser(anyInt())).thenReturn(user);
    }
    
    /**
     * Test of getTravels method, of class TravelController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testGetTravels() throws Exception{

        mockMVC.perform(get("/user/1/travel")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("travel1")))
                .andExpect(jsonPath("$[0].time_interval[0]", is("10:15:30")))
                .andExpect(jsonPath("$[0].time_interval[1]", is("12:15:30")))
                .andExpect(jsonPath("$[0].is_arrival_time", is(false)))
                .andExpect(jsonPath("$[0].recurring[0]", is(false)))
                .andExpect(jsonPath("$[0].recurring[1]", is(false)))
                .andExpect(jsonPath("$[0].recurring[2]", is(false)))
                .andExpect(jsonPath("$[0].recurring[3]", is(false)))
                .andExpect(jsonPath("$[0].recurring[4]", is(false)))
                .andExpect(jsonPath("$[0].recurring[5]", is(false)))
                .andExpect(jsonPath("$[0].recurring[6]", is(false)))
                .andExpect(jsonPath("$[0].startpoint.street", is("teststreet")))
                .andExpect(jsonPath("$[0].startpoint.housenumber", is("1")))
                .andExpect(jsonPath("$[0].startpoint.city", is("testcity")))
                .andExpect(jsonPath("$[0].startpoint.country", is("BE")))
                .andExpect(jsonPath("$[0].startpoint.postal_code", is("1234")))
                .andExpect(jsonPath("$[0].startpoint.coordinates.lat", is(50.0)))
                .andExpect(jsonPath("$[0].startpoint.coordinates.lon", is(60.0)))
                .andExpect(jsonPath("$[0].endpoint.street", is("teststreet")))
                .andExpect(jsonPath("$[0].endpoint.housenumber", is("5")))
                .andExpect(jsonPath("$[0].endpoint.city", is("testcity")))
                .andExpect(jsonPath("$[0].endpoint.country", is("BE")))
                .andExpect(jsonPath("$[0].endpoint.postal_code", is("1234")))
                .andExpect(jsonPath("$[0].endpoint.coordinates.lat", is(55.0)))
                .andExpect(jsonPath("$[0].endpoint.coordinates.lon", is(65.0)))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].name", is("travel2")))
                .andExpect(jsonPath("$[1].time_interval[0]", is("10:15:30")))
                .andExpect(jsonPath("$[1].time_interval[1]", is("12:15:30")))
                .andExpect(jsonPath("$[1].is_arrival_time", is(true)))
                .andExpect(jsonPath("$[1].recurring[0]", is(false)))
                .andExpect(jsonPath("$[1].recurring[1]", is(false)))
                .andExpect(jsonPath("$[1].recurring[2]", is(false)))
                .andExpect(jsonPath("$[1].recurring[3]", is(false)))
                .andExpect(jsonPath("$[1].recurring[4]", is(false)))
                .andExpect(jsonPath("$[1].recurring[5]", is(false)))
                .andExpect(jsonPath("$[1].recurring[6]", is(false)))
                .andExpect(jsonPath("$[1].endpoint.street", is("teststreet")))
                .andExpect(jsonPath("$[1].endpoint.housenumber", is("1")))
                .andExpect(jsonPath("$[1].endpoint.city", is("testcity")))
                .andExpect(jsonPath("$[1].endpoint.country", is("BE")))
                .andExpect(jsonPath("$[1].endpoint.postal_code", is("1234")))
                .andExpect(jsonPath("$[1].endpoint.coordinates.lat", is(50.0)))
                .andExpect(jsonPath("$[1].endpoint.coordinates.lon", is(60.0)))
                .andExpect(jsonPath("$[1].startpoint.street", is("teststreet")))
                .andExpect(jsonPath("$[1].startpoint.housenumber", is("5")))
                .andExpect(jsonPath("$[1].startpoint.city", is("testcity")))
                .andExpect(jsonPath("$[1].startpoint.country", is("BE")))
                .andExpect(jsonPath("$[1].startpoint.postal_code", is("1234")))
                .andExpect(jsonPath("$[1].startpoint.coordinates.lat", is(55.0)))
                .andExpect(jsonPath("$[1].startpoint.coordinates.lon", is(65.0)));
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravels method of class TravelController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravels_DataAccessException() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(get("/user/1/travel")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravels method of class TravelController when throwing NumberFormatException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravels_NumberFormatException() throws Exception {
        
        mockMVC.perform(get("/user/a/travel")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravels method of class TravelController when throwing RecordNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravels_RecordNotFoundException() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
        mockMVC.perform(get("/user/1/travel")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }

    /**
     * Test of addTravel method, of class TravelController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testAddTravel() throws Exception{
        when(databaseMock.createTravel(anyInt(), any(Travel.class))).thenReturn(nextId);
        
        mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(Integer.toString(nextId))))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.time_interval[0]", is(dto.getTimeInterval()[0])))
                .andExpect(jsonPath("$.time_interval[1]", is(dto.getTimeInterval()[1])))
                .andExpect(jsonPath("$.is_arrival_time", is(dto.isArrivalTime())))
                .andExpect(jsonPath("$.recurring[0]", is(dto.getRecurring()[0])))
                .andExpect(jsonPath("$.recurring[1]", is(dto.getRecurring()[1])))
                .andExpect(jsonPath("$.recurring[2]", is(dto.getRecurring()[2])))
                .andExpect(jsonPath("$.recurring[3]", is(dto.getRecurring()[3])))
                .andExpect(jsonPath("$.recurring[4]", is(dto.getRecurring()[4])))
                .andExpect(jsonPath("$.recurring[5]", is(dto.getRecurring()[5])))
                .andExpect(jsonPath("$.recurring[6]", is(dto.getRecurring()[6])))
                .andExpect(jsonPath("$.startpoint.street", is(dto.getStartpoint().getStreet())))
                .andExpect(jsonPath("$.startpoint.housenumber", is(dto.getStartpoint().getHouseNumber())))
                .andExpect(jsonPath("$.startpoint.city", is(dto.getStartpoint().getCity())))
                .andExpect(jsonPath("$.startpoint.country", is(dto.getStartpoint().getCountry())))
                .andExpect(jsonPath("$.startpoint.postal_code", is(dto.getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$.startpoint.coordinates.lat", is(dto.getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.startpoint.coordinates.lon", is(dto.getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$.endpoint.street", is(dto.getEndpoint().getStreet())))
                .andExpect(jsonPath("$.endpoint.housenumber", is(dto.getEndpoint().getHouseNumber())))
                .andExpect(jsonPath("$.endpoint.city", is(dto.getEndpoint().getCity())))
                .andExpect(jsonPath("$.endpoint.country", is(dto.getEndpoint().getCountry())))
                .andExpect(jsonPath("$.endpoint.postal_code", is(dto.getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$.endpoint.coordinates.lat", is(dto.getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.endpoint.coordinates.lon", is(dto.getEndpoint().getCoordinates().getLon())));
        
        ArgumentCaptor<Travel> travelCaptor = ArgumentCaptor.forClass(Travel.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).createTravel(idCaptor.capture(),travelCaptor.capture());
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(idCaptor.getValue(), is(1));
        
        Travel captTravel = travelCaptor.getValue();
        assertThat(captTravel.getName(), is(dto.getName())); 
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing DataAccessException() when getting a user
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_DataAccessExceptionGetUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
         mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing DataAccessException() when creating a travel
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_DataAccessExceptionCreateTravel() throws Exception {
        when(databaseMock.createTravel(anyInt(), any(Travel.class)))
                .thenThrow(new DataAccessException(""));
        
         mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).createTravel(anyInt(), any(Travel.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing NumberFormatException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_NumberFormatException() throws Exception {
        
         mockMVC.perform(post("/user/a/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing RecordNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_RecordNotFoundException() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
         mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing InvalidCountryCodeException() causing by the startpoint
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_InvalidCountryCodeExceptionStartpoint() throws Exception {
        dto.getStartpoint().setCountry("DTO");
        
         mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing InvalidCountryCodeException() causing by the endpoint
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_InvalidCountryCodeExceptionEndpoint() throws Exception {
        dto.getEndpoint().setCountry("DTO");
        
         mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of addTravel method of class TravelController when throwing ForeignKeyNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testAddTravel_ForeignKeyNotFoundException() throws Exception {
        when(databaseMock.createTravel(anyInt(), any(Travel.class)))
                .thenThrow(new ForeignKeyNotFoundException());
        
         mockMVC.perform(post("/user/1/travel")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).createTravel(anyInt(), any(Travel.class));
        verifyNoMoreInteractions(databaseMock);
    }
    

    /**
     * Test of getTravel method, of class TravelController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testGetTravel() throws Exception{
        mockMVC.perform(get("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("travel1")))
                .andExpect(jsonPath("$.time_interval[0]", is("10:15:30")))
                .andExpect(jsonPath("$.time_interval[1]", is("12:15:30")))
                .andExpect(jsonPath("$.is_arrival_time", is(false)))
                .andExpect(jsonPath("$.recurring[0]", is(false)))
                .andExpect(jsonPath("$.recurring[1]", is(false)))
                .andExpect(jsonPath("$.recurring[2]", is(false)))
                .andExpect(jsonPath("$.recurring[3]", is(false)))
                .andExpect(jsonPath("$.recurring[4]", is(false)))
                .andExpect(jsonPath("$.recurring[5]", is(false)))
                .andExpect(jsonPath("$.recurring[6]", is(false)))
                .andExpect(jsonPath("$.startpoint.street", is("teststreet")))
                .andExpect(jsonPath("$.startpoint.housenumber", is("1")))
                .andExpect(jsonPath("$.startpoint.city", is("testcity")))
                .andExpect(jsonPath("$.startpoint.country", is("BE")))
                .andExpect(jsonPath("$.startpoint.postal_code", is("1234")))
                .andExpect(jsonPath("$.startpoint.coordinates.lat", is(50.0)))
                .andExpect(jsonPath("$.startpoint.coordinates.lon", is(60.0)))
                .andExpect(jsonPath("$.endpoint.street", is("teststreet")))
                .andExpect(jsonPath("$.endpoint.housenumber", is("5")))
                .andExpect(jsonPath("$.endpoint.city", is("testcity")))
                .andExpect(jsonPath("$.endpoint.country", is("BE")))
                .andExpect(jsonPath("$.endpoint.postal_code", is("1234")))
                .andExpect(jsonPath("$.endpoint.coordinates.lat", is(55.0)))
                .andExpect(jsonPath("$.endpoint.coordinates.lon", is(65.0)));
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravel method of class TravelController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravel_DataAccessException() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(get("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravel method of class TravelController when throwing NumberFormatException() from the userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravel_NumberFormatExceptionUserId() throws Exception {
        
        mockMVC.perform(get("/user/a/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravel method of class TravelController when throwing NumberFormatException() from the travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravel_NumberFormatExceptionTravelId() throws Exception {
        
        mockMVC.perform(get("/user/1/travel/a")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravel method of class TravelController when throwing RecordNotFoundException() from userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravel_RecordNotFoundExceptionUserId() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
        mockMVC.perform(get("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getTravel method of class TravelController when throwing RecordNotFoundException() from travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetTravel_RecordNotFoundExceptionTravelId() throws Exception {
        
        mockMVC.perform(get("/user/1/travel/404")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }

    /**
     * Test of editTravel method, of class TravelController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testEditTravel() throws Exception{
        doNothing().when(databaseMock).updateTravel(anyInt(), anyInt(), any(Travel.class));
        
        mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.time_interval[0]", is(dto.getTimeInterval()[0])))
                .andExpect(jsonPath("$.time_interval[1]", is(dto.getTimeInterval()[1])))
                .andExpect(jsonPath("$.is_arrival_time", is(dto.isArrivalTime())))
                .andExpect(jsonPath("$.recurring[0]", is(dto.getRecurring()[0])))
                .andExpect(jsonPath("$.recurring[1]", is(dto.getRecurring()[1])))
                .andExpect(jsonPath("$.recurring[2]", is(dto.getRecurring()[2])))
                .andExpect(jsonPath("$.recurring[3]", is(dto.getRecurring()[3])))
                .andExpect(jsonPath("$.recurring[4]", is(dto.getRecurring()[4])))
                .andExpect(jsonPath("$.recurring[5]", is(dto.getRecurring()[5])))
                .andExpect(jsonPath("$.recurring[6]", is(dto.getRecurring()[6])))
                .andExpect(jsonPath("$.startpoint.street", is(dto.getStartpoint().getStreet())))
                .andExpect(jsonPath("$.startpoint.housenumber", is(dto.getStartpoint().getHouseNumber())))
                .andExpect(jsonPath("$.startpoint.city", is(dto.getStartpoint().getCity())))
                .andExpect(jsonPath("$.startpoint.country", is(dto.getStartpoint().getCountry())))
                .andExpect(jsonPath("$.startpoint.postal_code", is(dto.getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$.startpoint.coordinates.lat", is(dto.getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.startpoint.coordinates.lon", is(dto.getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$.endpoint.street", is(dto.getEndpoint().getStreet())))
                .andExpect(jsonPath("$.endpoint.housenumber", is(dto.getEndpoint().getHouseNumber())))
                .andExpect(jsonPath("$.endpoint.city", is(dto.getEndpoint().getCity())))
                .andExpect(jsonPath("$.endpoint.country", is(dto.getEndpoint().getCountry())))
                .andExpect(jsonPath("$.endpoint.postal_code", is(dto.getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$.endpoint.coordinates.lat", is(dto.getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.endpoint.coordinates.lon", is(dto.getEndpoint().getCoordinates().getLon())));
        ArgumentCaptor<Travel> travelCaptor = ArgumentCaptor.forClass(Travel.class);
        ArgumentCaptor<Integer> userIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> travelIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).updateTravel(userIdCaptor.capture(), travelIdCaptor.capture(), travelCaptor.capture());
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(userIdCaptor.getValue(), is(1));
        assertThat(travelIdCaptor.getValue(), is(1));
        
        Travel captTravel = travelCaptor.getValue();
        assertThat(captTravel.getName(), is(dto.getName()));
        
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing InvalidCountryCodeException() causing by the startpoint
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_InvalidCountryCodeExceptionStartpoint() throws Exception {
        dto.getStartpoint().setCountry("DTO");
        
         mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing NumberFormatException() from GetUser()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_NumberFormatExceptionGetUser() throws Exception {
        
        mockMVC.perform(put("/user/a/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing NumberFormatException() from the travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_NumberFormatExceptionTravelId() throws Exception {
        
        mockMVC.perform(put("/user/1/travel/a")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing DataAccessException() when getting a user
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_DataAccessExceptionGetUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
         mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing DataAccessException() when creating a travel
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_DataAccessExceptionUpdateTravel() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).updateTravel(anyInt(), anyInt(), any(Travel.class));
        
        mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).updateTravel(anyInt(), anyInt(), any(Travel.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing RecordNotFoundException() from getUser()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_RecordNotFoundExceptionGetUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
         mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing RecordNotFoundException() from travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_RecordNotFoundExceptionTravelId() throws Exception {
         mockMVC.perform(put("/user/1/travel/404")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing RecordNotFoundException() from UpdateTravel()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_RecordNotFoundExceptionUpdateTravel() throws Exception {
        doThrow(new RecordNotFoundException()).when(databaseMock).updateTravel(anyInt(), anyInt(), any(Travel.class));
        
         mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).updateTravel(anyInt(), anyInt(), any(Travel.class));
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of editTravel method of class TravelController when throwing ForeignKeyNotFoundException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testEditTravel_ForeignKeyNotFoundException() throws Exception {
        doThrow(new ForeignKeyNotFoundException()).when(databaseMock).updateTravel(anyInt(), anyInt(), any(Travel.class));
        
         mockMVC.perform(put("/user/1/travel/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).updateTravel(anyInt(), anyInt(), any(Travel.class));
        verifyNoMoreInteractions(databaseMock);
    }
    

    /**
     * Test of deleteTravel method, of class TravelController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testDeleteTravel() throws Exception{
        
        doNothing().when(databaseMock).deleteTravel(anyInt());
        
        mockMVC.perform(delete("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isNoContent());
        
        verify(databaseMock, times(1)).getUser(1);
        verify(databaseMock, times(1)).deleteTravel(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteTravel method of class TravelController when throwing NumberFormatException() from the userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteTravel_NumberFormatExceptionUserId() throws Exception {
        
        mockMVC.perform(delete("/user/a/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteTravel method of class TravelController when throwing NumberFormatException() from the travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteTravel_NumberFormatExceptionTravelId() throws Exception {
        
        mockMVC.perform(delete("/user/1/travel/a")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteTravel method of class TravelController when throwing DataAccessException() when getting a user
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteTravel_DataAccessExceptionGetUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
         mockMVC.perform(delete("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteTravel method of class TravelController when throwing DataAccessException() when deleting a travel
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteTravel_DataAccessExceptionDeleteTravel() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).deleteTravel(anyInt());
        
        mockMVC.perform(delete("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).deleteTravel(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteTravel method of class TravelController when throwing RecordNotFoundException() from getUser()
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteTravel_RecordNotFoundExceptionGetUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
         mockMVC.perform(delete("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteTravel method of class TravelController when throwing RecordNotFoundException() from DeleteTravel()
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteTravel_RecordNotFoundExceptionDeleteTravel() throws Exception {
        doThrow(new RecordNotFoundException()).when(databaseMock).deleteTravel(anyInt());
        
         mockMVC.perform(delete("/user/1/travel/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).deleteTravel(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
}

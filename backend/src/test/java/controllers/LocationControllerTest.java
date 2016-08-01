package controllers;

import DTO.mappers.LocationMapper;
import DTO.models.LocationDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.exceptions.RecordNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import models.Location;
import models.address.Address;
import models.address.Street;
import models.address.City;
import models.Coordinate;
import models.Transportation;
import models.event.Event;
import models.event.EventType;
import models.services.PhoneNumber;
import models.users.User;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.javatuples.Pair;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import org.springframework.http.MediaType;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LocationControllerTest {
    
    @Mock
    private Database databaseMock;
    @Mock
    private UserAuthenticationInterceptor userAuthenticationInterceptor;
    
    private MockMvc mockMVC;
    
    private Location location;
    
    private LocationDTO dto;
    
    private User user;
    
    
    public LocationControllerTest() {
        
    }
    
    @Before
    public void setUp() throws Exception {
        mockMVC = MockMvcBuilders.standaloneSetup(new LocationController(databaseMock))
                .build();
        when(userAuthenticationInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class))).thenReturn(Boolean.TRUE);
        
        City city = new City("Ghent","9000","BE"); 
        
        Street street = new Street("Plateaustraat",city);
        
        Address address = new Address(street, 22, new Coordinate(50,50));
        
        Map<Integer, EventType> eventTypes = new HashMap<>();
        EventType eventtype = new EventType("Jam",Arrays.asList(Transportation.CAR));
        eventTypes.put(1, eventtype);
        
        location  = new Location(address, "work", 2, false,
                eventTypes, Arrays.asList(new PhoneNumber("0499311921")));
        
        LocationMapper mapper = new LocationMapper();
        
        dto = mapper.convertToDTO(location, "1");
        
        user = new User("wachtwoord1", "email@email.com", false);
        user.addLocation(1, location);
        
        
        
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        when(databaseMock.createLocationEventtype(anyInt(), anyString())).thenReturn(new Pair(1,eventtype));
    }

    /**
     * Test of getLocations method, of class LocationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLocations() throws Exception {
        System.out.println("getLocations");
        
        mockMVC.perform(get("/user/1/point_of_interest")
                .header("Authorization", "1"))
                .andDo(print())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].radius",is(2)))
                .andExpect(jsonPath("$[0].name",is("work")))
                .andExpect(jsonPath("$[0].active",is(true)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].notify.email",is(false)))
                .andExpect(jsonPath("$[0].address.street",is("Plateaustraat")))
                .andExpect(jsonPath("$[0].address.housenumber",is("22")))
                .andExpect(jsonPath("$[0].address.city",is("Ghent")))
                .andExpect(jsonPath("$[0].address.country",is("BE")))
                .andExpect(jsonPath("$[0].address.postal_code",is("9000")))
                .andExpect(jsonPath("$[0].address.coordinates.lat",is(50.0)))
                .andExpect(jsonPath("$[0].address.coordinates.lon",is(50.0)))
                .andExpect(jsonPath("$[0].notify_for_event_types", hasSize(1)))
                .andExpect(jsonPath("$[0].notify_for_event_types[0].type",is("Jam")));
    }

    @Test
    public void testGetLocations_DataAccessException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/user/1/point_of_interest")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test 
    public void testGetLocations_RecordNotFoundException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException(""));
        mockMVC.perform(get("/user/1/point_of_interest")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    @Test 
    public void testGetLocations_NumberFormatException() throws Exception{
        mockMVC.perform(get("/user/a/point_of_interest/")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    /**
     * Test of addLocation method, of class LocationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testAddLocation() throws Exception {
        System.out.println("addLocation");
        
        when(databaseMock.createLocation(anyInt(), any(Location.class))).thenReturn(1);
        
        Pair<Integer,EventType> pair = new Pair<>(1,new EventType("Jam",Arrays.asList(Transportation.CAR)));
        when(databaseMock.createLocationEventtype(1,"Jam")).thenReturn(pair);
        
        mockMVC.perform(post("/user/1/point_of_interest")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization", "1"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                    MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                    Charset.forName("utf8"))))
                .andExpect(jsonPath("$.radius",is(2)))
                .andExpect(jsonPath("$.name",is("work")))
                .andExpect(jsonPath("$.active",is(true)))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.notify.email",is(false)))
                .andExpect(jsonPath("$.address.street",is("Plateaustraat")))
                .andExpect(jsonPath("$.address.housenumber",is("22")))
                .andExpect(jsonPath("$.address.city",is("Ghent")))
                .andExpect(jsonPath("$.address.country",is("BE")))
                .andExpect(jsonPath("$.address.postal_code",is("9000")))
                .andExpect(jsonPath("$.address.coordinates.lat",is(50.0)))
                .andExpect(jsonPath("$.address.coordinates.lon",is(50.0)))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(1)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type",is("Jam")));
    }
    
    @Test 
    public void testAddLocation_DataAccessException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        mockMVC.perform(post("/user/1/point_of_interest")
                         .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                        .andExpect(status().isInternalServerError());
    }

    @Test 
    public void testAddLocation_AlreadyExistsException() throws Exception{
        when(databaseMock.createLocation(anyInt(),any(Location.class))).thenThrow(new AlreadyExistsException());
        mockMVC.perform(post("/user/1/point_of_interest")
                         .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isConflict());
    }
    
    @Test 
    public void testAddLocation_InvalidCountryCodeException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenReturn(user);
        when(databaseMock.createLocation(1, location)).thenReturn(1);
        Pair<Integer,EventType> pair = new Pair<>(1,new EventType("Jam",Arrays.asList(Transportation.CAR)));
        when(databaseMock.createLocationEventtype(1,"Jam")).thenReturn(pair);
        dto.getAddress().setCountry("BELGIUM");
        mockMVC.perform(post("/user/1/point_of_interest")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test 
    public void testAddLocation_RecordNotFoundException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(post("/user/1/point_of_interest")
                         .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    @Test 
    public void testAddLocation_ForeignKeyNotFoundException() throws Exception{
        when(databaseMock.createLocationEventtype(anyInt(),any(String.class))).thenThrow(new ForeignKeyNotFoundException());
        mockMVC.perform(post("/user/1/point_of_interest")
                         .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
               .andExpect(status().isNotFound());
    }
    
    /**
     * Test of getLocation method, of class LocationController.
     */
    @Test
    public void testGetLocation() throws Exception {
        mockMVC.perform(get("/user/1/point_of_interest/1")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
                .andExpect(jsonPath("$.radius",is(2)))
                .andExpect(jsonPath("$.name",is("work")))
                .andExpect(jsonPath("$.active",is(true)))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.notify.email",is(false)))
                .andExpect(jsonPath("$.address.street",is("Plateaustraat")))
                .andExpect(jsonPath("$.address.housenumber",is("22")))
                .andExpect(jsonPath("$.address.city",is("Ghent")))
                .andExpect(jsonPath("$.address.country",is("BE")))
                .andExpect(jsonPath("$.address.postal_code",is("9000")))
                .andExpect(jsonPath("$.address.coordinates.lat",is(50.0)))
                .andExpect(jsonPath("$.address.coordinates.lon",is(50.0)))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(1)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type",is("Jam")));
    }

    @Test 
    public void testGetLocation_DataAccessException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/user/1/point_of_interest/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test 
    public void testGetLocation_NumberFormatException() throws Exception{
        mockMVC.perform(get("/user/1/point_of_interest/a")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test 
    public void testGetLocation_RecordNotFoundException() throws Exception{
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(get("/user/1/point_of_interest/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetLocationEvents() throws Exception{
        Date date = new Date();
        String now = Event.DATE_FORMATTER.format(date);
        Event eventLocation = new Event(new Coordinate(50.0,50.0), false, now, date.getTime(),
                "eventLocation", "address", new EventType("", new ArrayList<>()));
        Set<Pair<String, Event>> set = new HashSet<>();
        set.add(new Pair<>("testLocation", eventLocation));
        when(databaseMock.getRecentEventsOfLocation(1)).thenReturn(set);
        
        mockMVC.perform(get("/user/1/point_of_interest/1/events")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testLocation")))
                .andExpect(jsonPath("$[0].active", is(false)))
                .andExpect(jsonPath("$[0].description", is("eventLocation")));
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).getRecentEventsOfLocation(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    
    /**
     * Test of editLocation method, of class LocationController.
     */
    @Test
    public void testEditLocation() throws Exception {
        
       doNothing().when(databaseMock).updateLocation(anyInt(), anyInt(), any(Location.class));
       
       Pair<Integer,EventType> pair = new Pair<>(1,new EventType("Jam",Arrays.asList(Transportation.CAR)));
        
       when(databaseMock.createLocationEventtype(1,"Jam")).thenReturn(pair);
       when(databaseMock.getEventtype("Jam")).thenReturn(pair);
       
       dto.setName("home");
       
       mockMVC.perform(put("/user/1/point_of_interest/1")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
                    .andExpect(jsonPath("$.radius",is(2)))
                    .andExpect(jsonPath("$.name",is("home")))
                    .andExpect(jsonPath("$.active",is(true)))
                    .andExpect(jsonPath("$.id", is("1")))
                    .andExpect(jsonPath("$.notify.email",is(false)))
                    .andExpect(jsonPath("$.address.street",is("Plateaustraat")))
                    .andExpect(jsonPath("$.address.housenumber",is("22")))
                    .andExpect(jsonPath("$.address.city",is("Ghent")))
                    .andExpect(jsonPath("$.address.country",is("BE")))
                    .andExpect(jsonPath("$.address.postal_code",is("9000")))
                    .andExpect(jsonPath("$.address.coordinates.lat",is(50.0)))
                    .andExpect(jsonPath("$.address.coordinates.lon",is(50.0)))
                    .andExpect(jsonPath("$.notify_for_event_types", hasSize(1)))
                    .andExpect(jsonPath("$.notify_for_event_types[0].type",is("Jam")));
        
    }

    @Test
    public void testEditLocation_DataAccessException() throws Exception { 
        doThrow(new DataAccessException("")).when(databaseMock)
                .updateLocation(anyInt(), anyInt(), any(Location.class));
        dto.setName("home");
        mockMVC.perform(put("/user/1/point_of_interest/1")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testEditLocation_NumberFormatException() throws Exception { 
        mockMVC.perform(put("/user/1/point_of_interest/a")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testEditLocation_InvalidCountryCodeException() throws Exception { 
        doThrow(new DataAccessException("")).when(databaseMock)
                .updateLocation(anyInt(), anyInt(), any(Location.class));
        dto.getAddress().setCountry("blablbabab");
        mockMVC.perform(put("/user/1/point_of_interest/1")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testEditLocation_AlreadyExistsException() throws Exception { 
        doThrow(new AlreadyExistsException()).when(databaseMock)
                .updateLocation(anyInt(), anyInt(), any(Location.class));
        dto.setName("home");
        mockMVC.perform(put("/user/1/point_of_interest/1")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isConflict());
    }
    
    @Test
    public void testEditLocation_RecordNotFoundException() throws Exception { 
        doThrow(new RecordNotFoundException("")).when(databaseMock)
                .updateLocation(anyInt(), anyInt(), any(Location.class));
        dto.setName("home");
        mockMVC.perform(put("/user/1/point_of_interest/1")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testEditLocation_ForeignKeyNotFoundException() throws Exception { 
        doThrow(new ForeignKeyNotFoundException("")).when(databaseMock)
                .updateLocation(anyInt(), anyInt(), any(Location.class));
        dto.setName("home");
        mockMVC.perform(put("/user/1/point_of_interest/1")
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    /**
     * Test of deleteTravel method, of class LocationController.
     */
    @Test
    public void testDeleteLocation() throws Exception {
        doNothing().when(databaseMock).deleteLocation(anyInt());
        
        mockMVC.perform(delete("/user/1/point_of_interest/1")
                    .content(TestUtil.convertObjectToJsonBytes(dto))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8")
                .header("Authorization", "1"))
                    .andExpect(status().isNoContent());
    }
    
    @Test 
    public void testDeleteLocation_DataAccessException() throws Exception{
        doThrow(new DataAccessException("")).when(databaseMock).deleteLocation(anyInt());
        
        mockMVC.perform(delete("/user/1/point_of_interest/1")
                    .content(TestUtil.convertObjectToJsonBytes(dto))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8")
                .header("Authorization", "1"))
                    .andExpect(status().isInternalServerError());
    }
    
    @Test 
    public void testDeleteLocation_NumberFormatException() throws Exception {
        mockMVC.perform(delete("/user/1/point_of_interest/a")
                    .content(TestUtil.convertObjectToJsonBytes(dto))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8")
                .header("Authorization", "1"))
                    .andExpect(status().isInternalServerError());
    }
    
    @Test 
    public void testDeleteLocation_RecordNotFoundException() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(delete("/user/1/point_of_interest/1")
                    .content(TestUtil.convertObjectToJsonBytes(dto))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8")
                .header("Authorization", "1"))
                    .andExpect(status().isInternalServerError());
    }
    
    
}

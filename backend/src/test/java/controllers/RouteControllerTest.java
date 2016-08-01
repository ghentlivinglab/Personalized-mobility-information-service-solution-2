package controllers;

import DTO.models.CoordinateDTO;
import DTO.models.EventTypeDTO;
import DTO.models.NotifyDTO;
import DTO.models.RouteDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Coordinate;
import models.Route;
import models.Transportation;
import models.Travel;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.event.Event;
import models.event.EventType;
import models.repetition.Repetition;
import models.repetition.RepetitionWeek;
import models.services.Email;
import models.services.Service;
import models.users.User;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.javatuples.Pair;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Testclass for testing controllers.RouteController
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteControllerTest {
    
    private MockMvc mockMVC;
    
    @Mock
    private Database databaseMock;
    @Mock
    private UserAuthenticationInterceptor userAuthenticationInterceptor;
    
    private User user;
    private Travel travel;
    private Route firstRoute;
    private Route secondRoute;
    private int nextId = 3;
    private RouteDTO dto;
    
    @Before
    public void setUp() throws Exception{
        mockMVC = MockMvcBuilders.standaloneSetup(new RouteController(databaseMock))
                .build();
        
        when(userAuthenticationInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class))).thenReturn(Boolean.TRUE);
        
        user = new User("firstname", "lastname", "password1", "first.last@email.com", true);
        LocalTime beginDate = LocalTime.parse("10:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endDate = LocalTime.parse("12:15:30", DateTimeFormatter.ISO_LOCAL_TIME);
        Address startPoint = new Address(new Street("teststreet", new City("testcity", "1234", "BE")), 1, new Coordinate(50.0, 60.0));
        Address endPoint = new Address(new Street("teststreet", new City("testcity", "1234", "BE")), 5, new Coordinate(55.0, 65.0));
        RepetitionWeek recurring = new RepetitionWeek();
         
        ArrayList<Repetition> recurlist = new ArrayList<>();
        recurlist.add(recurring);
        
        travel = new Travel("travel1", beginDate, endDate, false, startPoint, endPoint, recurlist, new HashMap<>(), new ArrayList<>());
        
        user.addTravel(1, travel);
        
        Coordinate waypoint1 = new Coordinate(10.0, 20.0);
        Coordinate waypoint2 = new Coordinate(15.0, 25.0);
        ArrayList<Coordinate> waypoints = new ArrayList<>();
        waypoints.add(waypoint1);
        waypoints.add(waypoint2);
        
        Email email = new Email("e@mail.be");
        ArrayList<Service> services = new ArrayList<>();
        services.add(email);
        
        EventType eventtype = new EventType("secondrouteeventtype", new ArrayList<>());
        
        HashMap<Integer, EventType> notifyforeventtypes = new HashMap<>();
        notifyforeventtypes.put(1, eventtype);
        
        firstRoute = new Route(waypoints, new ArrayList<>(), Transportation.TRAIN, new HashMap<>(), true, services);
        secondRoute = new Route(waypoints, new ArrayList<>(), Transportation.CAR, notifyforeventtypes, false, services);
        
        travel.addRoute(1, firstRoute);
        travel.addRoute(2, secondRoute);
        
        EventTypeDTO [] eventtypedtoarray = new EventTypeDTO [1];
        eventtypedtoarray[0] = new EventTypeDTO("dtoeventtype");
        
        CoordinateDTO[] dtowaypoints = new CoordinateDTO[1];
        dtowaypoints[0] = new CoordinateDTO(0.0, 1.0);
        
        dto = new RouteDTO();
        dto.setActive(true);
        dto.setNotify(new NotifyDTO());
        dto.setTransportationType("car");
        dto.setNotifyEventTypes(eventtypedtoarray);
        dto.setUserWaypoints(dtowaypoints);
        dto.setFullWaypoints(new CoordinateDTO[0]);
        
        when(databaseMock.getUser(anyInt())).thenReturn(user);
    }
    
    /**
     * Test of getRoutes method, of class RouteController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testGetRoutes() throws Exception{
        mockMVC.perform(get("/user/1/travel/1/route")
                .header("Authorization", "1"))
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].waypoints", hasSize(2)))
                .andExpect(jsonPath("$[0].waypoints[0].lat", is(10.0)))
                .andExpect(jsonPath("$[0].waypoints[0].lon", is(20.0)))
                .andExpect(jsonPath("$[0].waypoints[1].lat", is(15.0)))
                .andExpect(jsonPath("$[0].waypoints[1].lon", is(25.0)))
                .andExpect(jsonPath("$[0].transportation_type", is("train")))
                .andExpect(jsonPath("$[0].notify_for_event_types", hasSize(0)))
                .andExpect(jsonPath("$[0].notify.email", is(false)))
                .andExpect(jsonPath("$[0].active", is(false)))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].waypoints", hasSize(2)))
                .andExpect(jsonPath("$[1].waypoints[0].lat", is(10.0)))
                .andExpect(jsonPath("$[1].waypoints[0].lon", is(20.0)))
                .andExpect(jsonPath("$[1].waypoints[1].lat", is(15.0)))
                .andExpect(jsonPath("$[1].waypoints[1].lon", is(25.0)))
                .andExpect(jsonPath("$[1].transportation_type", is("car")))
                .andExpect(jsonPath("$[1].notify_for_event_types", hasSize(1)))
                .andExpect(jsonPath("$[1].notify_for_event_types[0].type", is("secondrouteeventtype")))
                .andExpect(jsonPath("$[1].notify.email", is(false)))
                .andExpect(jsonPath("$[1].active", is(true)));
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getRoutes method of class RouteController when throwing DataAccessException()
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetRoutes_DataAccessException() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(get("/user/1/travel/1/route")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getRoutes method of class RouteController when throwing NumberFormatException() from userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetRoutes_NumberFormatExceptionUserId() throws Exception {
        
        mockMVC.perform(get("/user/a/travel/1/route")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
     /**
     * Test of getRoutes method of class RouteController when throwing NumberFormatException() from travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetRoutes_NumberFormatExceptionTravelId() throws Exception {
        
        mockMVC.perform(get("/user/1/travel/a/route")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getRoutes method of class RouteController when throwing RecordNotFoundException() from userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetRoutes_RecordNotFoundExceptionUserId() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
        mockMVC.perform(get("/user/1/travel/1/route")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of getRoutes method of class RouteController when throwing RecordNotFoundException() from travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testGetRoutes_RecordNotFoundExceptionTravelId() throws Exception {
        
        mockMVC.perform(get("/user/1/travel/404/route")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }

    /**
     * Test of addRoute method, of class RouteController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testAddRoute() throws Exception{
        when(databaseMock.createRoute(anyInt(), any(Route.class))).thenReturn(nextId);
        EventType dtoevent = new EventType("dtoeventtype", new ArrayList<>());
        
        when(databaseMock.createRouteEventtype(anyInt(), anyString())).thenReturn(new Pair<>(1, dtoevent));
        
        mockMVC.perform(post("/user/1/travel/1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(Integer.toString(nextId))))
                .andExpect(jsonPath("$.waypoints", hasSize(1)))
                .andExpect(jsonPath("$.waypoints[0].lat", is(0.0)))
                .andExpect(jsonPath("$.waypoints[0].lon", is(1.0)))
                .andExpect(jsonPath("$.transportation_type", is("car")))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(1)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type", is("dtoeventtype")))
                .andExpect(jsonPath("$.notify.email", is(false)))
                .andExpect(jsonPath("$.active", is(true)));
        
        ArgumentCaptor<Route> routeCaptor = ArgumentCaptor.forClass(Route.class);
        ArgumentCaptor<Integer> travelIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> routeIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> eventTypeCaptor = ArgumentCaptor.forClass(String.class);
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).createRoute(travelIdCaptor.capture(), routeCaptor.capture());
        verify(databaseMock, times(1)).createRouteEventtype(routeIdCaptor.capture(), eventTypeCaptor.capture());
        verifyNoMoreInteractions(databaseMock);
        
        assertThat(travelIdCaptor.getValue(), is(1));
        assertThat(routeIdCaptor.getValue(), is(nextId));
        assertThat(eventTypeCaptor.getValue(), is("dtoeventtype"));
        
        Route captValue = routeCaptor.getValue();
        assertThat(captValue.getTransportationType(), is(Transportation.CAR));
        assertThat(captValue.isMuted(), is(false));
    }
    
    @Test
    public void testAddRouteExceptions() throws Exception {
        //travel not found
        mockMVC.perform(post("/user/1/travel/42/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
                
        //internal server error
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        mockMVC.perform(post("/user/1/travel/1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
    }
    
    @Test
    public void testAddRouteRecordNotFoundException() throws Exception {
        //user not found
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(post("/user/1/travel/1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    

    /**
     * Test of getRoute method, of class RouteController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testGetRoute() throws Exception{
        mockMVC.perform(get("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.waypoints", hasSize(2)))
                .andExpect(jsonPath("$.waypoints[0].lat", is(10.0)))
                .andExpect(jsonPath("$.waypoints[0].lon", is(20.0)))
                .andExpect(jsonPath("$.waypoints[1].lat", is(15.0)))
                .andExpect(jsonPath("$.waypoints[1].lon", is(25.0)))
                .andExpect(jsonPath("$.transportation_type", is("train")))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(0)))
                .andExpect(jsonPath("$.notify.email", is(false)))
                .andExpect(jsonPath("$.active", is(false)));
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testGetRouteExceptions() throws Exception {
        //travel not found
        mockMVC.perform(get("/user/1/travel/42/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
                
        //route not found
        mockMVC.perform(get("/user/1/travel/1/route/42")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        //internal server error
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
    }
    
    @Test
    public void testGetRouteRecordNotFoundException() throws Exception {
        //user not found
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(get("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetRouteEvents() throws Exception{
        Date date = new Date();
        String now = Event.DATE_FORMATTER.format(date);
        Event eventRoute = new Event(new Coordinate(50.0,50.0), true, now, date.getTime(),
                "eventRoute", "address", new EventType("", new ArrayList<>()));
        Set<Pair<String, Event>> dbResponse = new HashSet<>();
        dbResponse.add(new Pair<>("routeevent", eventRoute));
        when(databaseMock.getRecentEventsOfRoute(1)).thenReturn(dbResponse);
        
        mockMVC.perform(get("/user/1/travel/1/route/1/events")
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("routeevent")))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].description", is("eventRoute")));
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).getRecentEventsOfRoute(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
    @Test
    public void testGetRouteEventsExceptions() throws Exception {
        //travel not found
        mockMVC.perform(get("/user/1/travel/42/route/1/events")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        when(databaseMock.getRecentEventsOfRoute(42)).thenThrow(new RecordNotFoundException());
        //route not found
        mockMVC.perform(get("/user/1/travel/1/route/42/events")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        //internal server error
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/user/1/travel/1/route/1/events")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
    }
    
    @Test
    public void testGetRouteEventsRecordNotFoundException() throws Exception {
        //user not found
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        mockMVC.perform(get("/user/1/travel/1/route/1/events")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test of editRoute method, of class RouteController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testEditRoute() throws Exception {
        EventType dtoevent = new EventType("dtoeventtype", new ArrayList<>());
        when(databaseMock.getEventtype(anyString())).thenReturn(new Pair<>(1, dtoevent));
        when(databaseMock.createRouteEventtype(anyInt(), anyString())).thenReturn(new Pair<>(1, dtoevent));
        doNothing().when(databaseMock).updateRoute(anyInt(), anyInt(), any(Route.class));
        doNothing().when(databaseMock).deleteRouteEventtype(anyInt(), anyInt());
        
        mockMVC.perform(put("/user/1/travel/1/route/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .header("Authorization", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(Integer.toString(1))))
                .andExpect(jsonPath("$.waypoints", hasSize(1)))
                .andExpect(jsonPath("$.waypoints[0].lat", is(0.0)))
                .andExpect(jsonPath("$.waypoints[0].lon", is(1.0)))
                .andExpect(jsonPath("$.transportation_type", is("car")))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(1)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type", is("dtoeventtype")))
                .andExpect(jsonPath("$.notify.email", is(false)))
                .andExpect(jsonPath("$.active", is(true)));
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).updateRoute(anyInt(), anyInt(), any(Route.class));
        verify(databaseMock, times(1)).getEventtype(anyString());
        verify(databaseMock, times(1)).createRouteEventtype(anyInt(), anyString());
        verifyNoMoreInteractions(databaseMock);
        
    }

    /**
     * Test of deleteRoute method, of class RouteController.
     * @throws java.lang.Exception should never be doing this
     */
    @Test
    public void testDeleteRoute() throws Exception {
        doNothing().when(databaseMock).deleteRoute(anyInt());
        mockMVC.perform(delete("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isNoContent());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).deleteRoute(1);
        verifyNoMoreInteractions(databaseMock);
    }
    
     /**
     * Test of deleteRoute method of class RouteController when throwing DataAccessException() from getuser
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_DataAccessExceptionGetUser() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(delete("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing DataAccessException() from deleteroute
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_DataAccessExceptionDeleteRoute() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).deleteRoute(anyInt());
        
        mockMVC.perform(delete("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verify(databaseMock, times(1)).deleteRoute(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing NumberFormatException() from userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_NumberFormatExceptionUserId() throws Exception {
        
        mockMVC.perform(delete("/user/a/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
     /**
     * Test of deleteRoute method of class RouteController when throwing NumberFormatException() from travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_NumberFormatExceptionTravelId() throws Exception {
        
        mockMVC.perform(delete("/user/1/travel/a/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing NumberFormatException() from routeid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_NumberFormatExceptionRouteId() throws Exception {
        
        mockMVC.perform(delete("/user/1/travel/1/route/a")
                .header("Authorization", "1"))
                .andExpect(status().isInternalServerError());
        
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing RecordNotFoundException() from userid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_RecordNotFoundExceptionUserId() throws Exception {
        when(databaseMock.getUser(anyInt())).thenThrow(new RecordNotFoundException());
        
        mockMVC.perform(delete("/user/1/travel/1/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing RecordNotFoundException() from travelid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_RecordNotFoundExceptionTravelId() throws Exception {
        
        mockMVC.perform(delete("/user/1/travel/404/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing RecordNotFoundException() from routeid
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_RecordNotFoundExceptionRouteId() throws Exception {
        
        mockMVC.perform(delete("/user/1/travel/1/route/404")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }
    
    /**
     * Test of deleteRoute method of class RouteController when throwing RecordNotFoundException() from deleteroute
     * @throws Exception should never be doing this
     */
    @Test
    public void testDeleteRoute_RecordNotFoundExceptionDeleteRoute() throws Exception {
        doThrow(new RecordNotFoundException()).when(databaseMock).deleteRoute(anyInt());
        
        mockMVC.perform(delete("/user/1/travel/404/route/1")
                .header("Authorization", "1"))
                .andExpect(status().isNotFound());
        
        verify(databaseMock, times(1)).getUser(anyInt());
        verifyNoMoreInteractions(databaseMock);
    }

}

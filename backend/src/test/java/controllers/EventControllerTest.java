package controllers;

import DTO.models.CoordinateDTO;
import DTO.models.EventDTO;
import DTO.models.EventTypeDTO;
import DTO.models.JamDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Coordinate;
import models.Transportation;
import models.event.Event;
import models.event.EventType;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(MockitoJUnitRunner.class)
@ImportResource({"classpath:beans.xml"})
public class EventControllerTest {
    
    private MockMvc mockMVC;
    
    private Event event;
    
    private EventDTO eventDTO;
    
    @Mock
    private Database databaseMock;
    
    @Mock 
    private EventAuthenticationInterceptor eventAuthenticationInterceptor;
    
    public EventControllerTest() {
    }
    

    @Before
    public void setUp() throws Exception{
        mockMVC = MockMvcBuilders.standaloneSetup(new EventController(databaseMock))
                .build();
        event = new Event("uuid",new Coordinate(60,60),true,
                100000,100000,"jammy jammy",
                "jozef plateaustraat 22, 9000 Gent",
                new EventType("Jam",Arrays.asList(new Transportation[]{Transportation.CAR})));
        eventDTO = new EventDTO("1", new CoordinateDTO(60,60), true, "1970-01-01T01:01:40.000", 
                "1970-01-01T01:01:40.000", 
                "jammy jammy", "jozef plateaustraat 22, 9000 Gent", new JamDTO []{}
                , new EventTypeDTO("Jam"), new String[]{"Car"});

        when(databaseMock.getEvent("1")).thenReturn(event);
        
        when(eventAuthenticationInterceptor.preHandle(any(HttpServletRequest.class), 
                any(HttpServletResponse.class), any(Object.class))).thenReturn(Boolean.TRUE);

    }
    
    /**
     * Test of getEvents method, of class EventController.
     */
    @Test
    public void testGetEvents() throws Exception {
        Event event1 = new Event("uuid",new Coordinate(50,50),true,
                100000,100000,"description1",
                "jozef plateaustraat 22, 9000 Gent",
                new EventType("Jam",Arrays.asList(new Transportation[]{Transportation.CAR})));
        Event event2 = new Event("uuid",new Coordinate(50,40),true,
                100000,100000,"description2",
                "jozef plateaustraat 20, 9000 Gent",
                new EventType("Jam",Arrays.asList(new Transportation[]{Transportation.CAR,Transportation.BUS}))); 
        
        when(databaseMock.listAllEvents()).thenReturn(Arrays.asList(new Pair("1",event1),new Pair("2",event2)));
        
        mockMVC.perform(get("/event"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].coordinates.lon", is(50.0)))
                .andExpect(jsonPath("$[0].coordinates.lat", is(50.0)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].publication_time", is("1970-01-01T01:01:40.000")))
                .andExpect(jsonPath("$[0].last_edit_time", is("1970-01-01T01:01:40.000")))
                .andExpect(jsonPath("$[0].description", is("description1")))
                .andExpect(jsonPath("$[0].type.type", is("Jam")))
                .andExpect(jsonPath("$[0].formatted_address", is ("jozef plateaustraat 22, 9000 Gent")))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types[0]", is("car")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].coordinates.lon", is(40.0)))
                .andExpect(jsonPath("$[1].coordinates.lat", is(50.0)))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].publication_time", is("1970-01-01T01:01:40.000")))
                .andExpect(jsonPath("$[1].last_edit_time", is("1970-01-01T01:01:40.000")))
                .andExpect(jsonPath("$[1].description", is("description2")))
                .andExpect(jsonPath("$[1].type.type", is("Jam")))
                .andExpect(jsonPath("$[0].formatted_address", is ("jozef plateaustraat 22, 9000 Gent")))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types[0]", is("car")));
    }

    @Test
    public void testGetEvents_DataAccessException() throws Exception {
        when(databaseMock.listAllEvents()).thenThrow(new DataAccessException(""));
        
        mockMVC.perform(get("/event"))
                .andExpect(status().isInternalServerError());
    }    
    
    /**
     * Test of addEvent method, of class EventController.
     */
    @Test
    public void testGetEvent() throws Exception {
        mockMVC.perform(get("/event/1"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
               .andExpect(jsonPath("$.id", is("1")))
               .andExpect(jsonPath("$.coordinates.lon", is(60.0)))
               .andExpect(jsonPath("$.coordinates.lat", is(60.0)))
               .andExpect(jsonPath("$.active", is(true)))
               .andExpect(jsonPath("$.publication_time", is("1970-01-01T01:01:40.000")))
               .andExpect(jsonPath("$.last_edit_time", is("1970-01-01T01:01:40.000")))
               .andExpect(jsonPath("$.type.type", is("Jam")))
               .andExpect(jsonPath("$.formatted_address", is ("jozef plateaustraat 22, 9000 Gent")))
               .andExpect(jsonPath("$.relevant_for_transportation_types[0]", is("car")));
        verify(databaseMock, times(1)).getEvent("1");
    }
    
    @Test
    public void testGetEvent_DataAccessException() throws Exception {
        doThrow(new DataAccessException("")).when(databaseMock).getEvent("1");
        mockMVC.perform(get("/event/1"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testGetEvent_RecordNotFoundException() throws Exception {
        doThrow(new RecordNotFoundException("")).when(databaseMock).getEvent("1");
        mockMVC.perform(get("/event/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test of getEvent method, of class EventController.
     */
    @Test
    public void testAddEvent() throws Exception {
        System.out.println("addEvent");
        when(databaseMock.createEvent(any(Event.class))).thenReturn("1");
        mockMVC.perform(post("/event")
                    .header("Authorization","operator")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id",is("1")));
        verify(databaseMock, times(1)).createEvent(any(Event.class));
    }
    
    @Test 
    public void testAddEvent_DataAccessException() throws Exception {
        when(databaseMock.createEvent(any(Event.class))).thenThrow(new DataAccessException(""));
        mockMVC.perform(post("/event")
                .header("Authorization","operator")
                .content(TestUtil.convertObjectToJsonBytes(eventDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }
    
    /**
     * Test of updateEvent method, of class EventController.
     */
    @Test
    public void testUpdateEvent() throws Exception {
        doNothing().when(databaseMock).updateEvent(anyString(), any(Event.class));
        System.out.println("updateEvent");
        mockMVC.perform(put("/event/1")
                    .header("Authorization","operator")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8"))
                    .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
               .andExpect(jsonPath("$.id", is("1")))
               .andExpect(jsonPath("$.coordinates.lon", is(60.0)))
               .andExpect(jsonPath("$.coordinates.lat", is(60.0)))
               .andExpect(jsonPath("$.active", is(true)))
               .andExpect(jsonPath("$.publication_time", is("1970-01-01T01:01:40.000")))
               //.andExpect(jsonPath("$.last_edit_time", is()))
               .andExpect(jsonPath("$.description", is("jammy jammy")))
               .andExpect(jsonPath("$.type.type", is("Jam")));
    }
    
    @Test
    public void testUpdateEvent_DataAccessException() throws Exception{
        doThrow(new DataAccessException("")).when(databaseMock).updateEvent(anyString(), any(Event.class));
        mockMVC.perform(put("/event/1")
                    .header("Authorization","operator")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8"))
               .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testUpdateEvent_RecordNotFoundException() throws Exception{
        doThrow(new RecordNotFoundException("")).when(databaseMock).updateEvent(anyString(), any(Event.class));
        mockMVC.perform(put("/event/1")
                    .header("Authorization","operator")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .characterEncoding("UTF-8"))
               .andExpect(status().isNotFound());
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DTO.models.EventTypeDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import models.Transportation;
import models.event.EventType;
import org.javatuples.Pair;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author hannedesutter
 */
@RunWith(MockitoJUnitRunner.class)
public class EventTypeControllerTest {
    
    private MockMvc mockMVC;
    @Mock
    private Database databaseMock;
    
    
    public EventTypeControllerTest() {
    }

    @Before
    public void setUp(){
        mockMVC = MockMvcBuilders.standaloneSetup(new EventTypeController(databaseMock))
                .build();
    }
    
    /**
     * Test of getEventTypesByTransport method, of class EventTypeController.
     */
    @Test
    public void testGetEventTypesByTransport() throws DataAccessException, RecordNotFoundException, Exception {
        EventType eventType1 = new EventType("Jam", Arrays.asList(Transportation.CAR));
        EventType eventType2 = new EventType("RoadWork", Arrays.asList(Transportation.CAR));
        when(databaseMock.getEventtypes(Transportation.CAR)).thenReturn(Arrays.asList(new Pair(1,eventType1),new Pair(1,eventType2)));
        mockMVC.perform(get("/eventtype/")
            .param("transportationType","car")
            .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),                        
                                                                        Charset.forName("utf8"))))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type",is("Jam")))
                .andExpect(jsonPath("$[1].type",is("RoadWork")));
    }
    
    @Test 
    public void testGetEventTypesByTransport_DataAccessException() throws Exception{
        when(databaseMock.getEventtypes(Transportation.CAR)).thenThrow(new DataAccessException(""));
        mockMVC.perform(get("/eventtype/")
            .param("transportationType","car")
            .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError());
    }
    
}

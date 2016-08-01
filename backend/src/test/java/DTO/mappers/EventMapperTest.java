/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.CoordinateDTO;
import DTO.models.EventDTO;
import DTO.models.EventTypeDTO;
import DTO.models.JamDTO;
import DTO.models.SourceDTO;
import database_v2.controlLayer.Database;
import database_v2.controlLayer.impl.DatabaseImpl;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import models.Coordinate;
import models.Transportation;
import models.event.Event;
import models.event.Jam;
import models.event.Source;
import org.junit.Test;
import static org.junit.Assert.*;
import models.event.EventType;
import org.javatuples.Pair;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author hannedesutter
 */
public class EventMapperTest {
    
    private static Database database;
    
    public EventMapperTest() throws DataAccessException, RecordNotFoundException {
        database = mock(DatabaseImpl.class);
        when(database.getEventtype("Jam")).thenReturn(new Pair("1",new EventType("Jam",new ArrayList<>())));
    }

    /**
     * Test of convertToDTO method, of class EventMapper.
     */
    @Test
    public void testConvertToDTO_Jam() throws MalformedURLException {
        System.out.println("convertToDTO");
        Event event = new Event("uuid",new Coordinate(60,60), true, 1200, 1200, "leuk event", 
                "jozef plateaustraat 22, 9000 Gent",new EventType("Jam",Arrays.asList(new Transportation[]
                {Transportation.CAR})));
        String id = "1";
        Jam jam = new Jam("1234",1200,Arrays.asList(new Coordinate []{new Coordinate(50,50),
            new Coordinate(60,60)}),10,10);
        event.addJam(jam);
        EventMapper instance = new EventMapper();
        JamDTO jamDTO = new JamDTO(new CoordinateDTO []{new CoordinateDTO(50,50),new CoordinateDTO(60,60)},
                "1970-01-01T01:00:01.200",10,10);
        JamDTO [] jams = new JamDTO[]{jamDTO};
        EventDTO expResult = new EventDTO("1", new CoordinateDTO(60,60), true, "1970-01-01T01:00:01.200",
                "1970-01-01T01:00:01.200", "leuk event", "jozef plateaustraat 22, 9000 Gent", jams , 
                new EventTypeDTO("Jam"), new String []{"car"});
        EventDTO result = instance.convertToDTO(event, id);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testConvertToDTO_Event() throws MalformedURLException {
        System.out.println("convertToDTO");
        Event event = new Event("uuid", new Coordinate(60,60),true,1200,1200,"leuk event",
                "jozef plateaustraat 22, 9000 Gent",new EventType("WATER_HAZARD",new ArrayList<>()));
        String id = "1";
        EventMapper instance = new EventMapper();
        EventDTO expResult = new EventDTO("1", new CoordinateDTO(60,60), true, "1970-01-01T01:00:01.200",
                "1970-01-01T01:00:01.200", "leuk event", "jozef plateaustraat 22, 9000 Gent",new JamDTO[]{} , 
                new EventTypeDTO("WATER_HAZARD"), new String []{"CAR"});
        EventDTO result = instance.convertToDTO(event, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class EventMapper.
     */
    @Test
    public void testConvertFromDTO_Jam() throws Exception {
        System.out.println("convertFromDTO");
        JamDTO jamDTO = new JamDTO(new CoordinateDTO[]{new CoordinateDTO(50,50),new CoordinateDTO(60,60)},
                "1970-01-01T01:00:01.200",10,10);
        JamDTO [] jams = new JamDTO[]{jamDTO};
        EventDTO eventDTO = new EventDTO("1", new CoordinateDTO(60,60), true, "1970-01-01T01:00:01.200",
                "1970-01-01T01:00:01.200", "leuk event", "jozef plateaustraat 22, 9000 Gent", jams,
                new EventTypeDTO("Jam"), new String []{"CAR"});
        EventMapper instance = new EventMapper();
        Event expResult =  new Event(null, new Coordinate(60,60), true, 1200, 1200, "leuk event", 
                "jozef plateaustraat 22, 9000 Gent", new EventType("Jam",Arrays.asList(new Transportation
                        []{Transportation.CAR})));
        Jam jam = new Jam("uuid", 1200, Arrays.asList(new Coordinate[]{new Coordinate(50,50), new Coordinate(60,60)}),
                10, 10);
        expResult.addJam(jam);
        Event result = instance.convertFromDTO(eventDTO);
//        assertEquals(expResult,result);
        assertEquals(expResult.getCoordinates(),result.getCoordinates());
        assertEquals(expResult.getDescription(), result.getDescription());
//       assertEquals(expResult.getLastEditTimeMillis(), result.getLastEditTimeMillis());
        assertEquals(expResult.getPublicationTimeMillis(), result.getPublicationTimeMillis());
        assertEquals(expResult.getTransportTypes(), result.getTransportTypes());
        assertEquals(expResult.getType(), result.getType());
        assertEquals(expResult.isActive(), result.isActive());
        assertEquals(expResult.getUuid(), result.getUuid());
        assertEquals(expResult.getAllJams().get(0).getPublicationString(),result.getAllJams().get(0).getPublicationString());
        assertEquals(expResult.getAllJams().get(0).getSpeed(),result.getAllJams().get(0).getSpeed());
        assertEquals(expResult.getAllJams().get(0).getDelay(),result.getAllJams().get(0).getDelay());
        assertEquals(expResult.getAllJams().get(0).getLineView(),result.getAllJams().get(0).getLineView());
 
    }
    
    @Test
    public void testConvertFromDTO_Event() throws Exception {
        System.out.println("convertFromDTO");
        EventDTO eventDTO = new EventDTO("1", new CoordinateDTO(60,60), 
                true, "1970-01-01T01:00:01.200", "1970-01-01T01:00:01.200", "leuk event",
                "jozef plateaustraat 22, 9000 Gent", new JamDTO[]{} , new EventTypeDTO("Event"), new String []{});
        EventMapper instance = new EventMapper();
        Event expResult =  new Event(null, new Coordinate(60,60), true, 1200, 1200,
                "leuk event", "jozef plateaustraat 22, 9000 Gent", new EventType("Event",new ArrayList<>()));
        Event result = instance.convertFromDTO(eventDTO);
       // assertEquals(expResult,result);
        assertEquals(expResult.getCoordinates(),result.getCoordinates());
        assertEquals(expResult.getDescription(), result.getDescription());
//        assertEquals(expResult.getLastEditTimeMillis(), result.getLastEditTimeMillis());
        assertEquals(expResult.getPublicationTimeMillis(), result.getPublicationTimeMillis());
        assertEquals(expResult.getTransportTypes(), result.getTransportTypes());
        assertEquals(expResult.getType(), result.getType());
        assertEquals(expResult.isActive(), result.isActive());
        assertEquals(expResult.getUuid(), result.getUuid());
        assertEquals(expResult.getAllJams(),result.getAllJams());
    }
}

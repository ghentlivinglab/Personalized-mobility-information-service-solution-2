/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.DAOLayer.impl;

import com.mongodb.client.MongoDatabase;
import database_v2.DAOLayer.EventDAO;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.impl.DataAccessProviderImpl;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.mongo.CoordinateDBModel;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.mongo.JamDBModel;
import java.util.Arrays;
import java.util.List;
import models.Coordinate;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class EventDAOImplTest {

    private final DataAccessProviderImpl dap;

    public EventDAOImplTest() {
        dap = new DataAccessProviderImpl();
    }

    @Before
    public void setUp() {
        // clear database
        MongoDatabase mongoDB = dap.getMongoConnection();
        mongoDB.getCollection("events").deleteMany(new Document());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createEvent method, of class EventDAOImpl.
     */
    @Test
    public void testCreateEvent() throws Exception {
        System.out.println("createEvent");
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();

            EventDBModel ev1 = createTestEvent();
            ed.createEvent(ev1);

            EventDBModel fetched = ed.getEvent(ev1.getId());
            assertEquals(fetched, ev1);

            // PATH 2: an event already exists, should be uodated
            ev1.setDescription("updated");
            ed.createEvent(ev1);
            fetched = ed.getEvent(ev1.getId());
            assertEquals(ev1, fetched);
        }
    }

    /**
     * Test of updateEvent method, of class EventDAOImpl.
     */
    @Test
    public void testUpdateEvent() throws Exception {
        System.out.println("updateEvent");
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();

            EventDBModel ev1 = createTestEvent();
            ed.createEvent(ev1);

            ev1.setUuid("ANDERE_UUID");
            ed.updateEvent(ev1);
            EventDBModel fetched = ed.getEvent(ev1.getId());
            assertEquals(fetched, ev1);
        }
    }

    /**
     * Test of deleteEvent method, of class EventDAOImpl.
     */
    @Test
    public void testDeleteEvent_EventDBModel() throws Exception {
        System.out.println("deleteEvent");
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();

            // first create an event
            EventDBModel ev1 = createTestEvent();
            ed.createEvent(ev1);

            // delete the event
            ed.deleteEvent(ev1);

            // check if deleted
            boolean deleted = false;
            try {
                ed.getEvent(ev1.getId());
            } catch (RecordNotFoundException ex) {
                deleted = true;
            }
            assertTrue(deleted);
        }
    }

    @Test
    public void getEventsTest() throws Exception {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();
            
            EventDBModel ev1 = createTestEvent();
            // insert the event
            ed.createEvent(ev1);
            
            List<EventDBModel> result = ed.getEvents();
            
            assertTrue(result.size() == 1);
            assertEquals(ev1, result.get(0));
        }
    }
    
    @Test
    public void getEventsByGridPointTest() throws Exception {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();
            
            EventDBModel ev1 = createTestEvent();
            ev1.setGridX(1);
            ev1.setGridY(1);
            ev1.setUuid("UUID1");
            ed.createEvent(ev1);
            EventDBModel ev2 = createTestEvent();
            ev2.setGridX(2);
            ev2.setGridY(2);
            ev2.setUuid("UUID2");
            ed.createEvent(ev2);
            
            List<EventDBModel> result = ed.getEvents(1, 1);
            assertTrue(result.size() == 1);
            assertEquals(ev1, result.get(0));
        }
    }
    
    @Test
    public void getEventsByRadiusTest() throws Exception {
        try(DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();
            
            EventDBModel ev1 = createTestEvent();
            Coordinate coord1 = new Coordinate(51.038437, 3.731271);
            ev1.setCoordinates(new CoordinateDBModel(coord1));
            ev1.setUuid("UUID1");
            EventDBModel ev2 = createTestEvent();
            Coordinate coord2 = new Coordinate(51.047576, 3.717513);
            ev2.setCoordinates(new CoordinateDBModel(coord2));
            ev2.setUuid("UUID2");
            ed.createEvent(ev1);
            ed.createEvent(ev2);
            
            Coordinate coord3 = new Coordinate(51.038425, 3.729808);
            List<EventDBModel> result = ed.getEventsInRadius(coord3.getX(), coord3.getY(), 200);
            
            assertTrue(result.size() == 1);
            assertEquals(ev1, result.get(0));
        }
    }

    private EventDBModel createTestEvent() {
        Coordinate coord1 = new Coordinate(40.714224, -73.961452);
        CoordinateDBModel eventCoord = new CoordinateDBModel(coord1);
        Coordinate coord2 = new Coordinate(40.714224, -73.961452);
        CoordinateDBModel startCoord = new CoordinateDBModel(coord2);
        Coordinate coord3 = new Coordinate(40.714224, -73.961452);
        CoordinateDBModel endCoord = new CoordinateDBModel(coord3);
        JamDBModel jam = new JamDBModel("UUID", 1000L, Arrays.asList(startCoord, endCoord), 10, 15);
        return new EventDBModel(eventCoord, true, 0, 0, "desc", "address", Arrays.asList(jam), 0, "UUID", 0, 0);
    }
}

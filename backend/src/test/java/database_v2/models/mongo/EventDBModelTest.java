/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.Coordinate;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class EventDBModelTest {
    
    public EventDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

//    /**
//     * Test of toDocument method, of class EventDBModel.
//     */
//    @Test
//    public void testToDocument() {
//        System.out.println("toDocument");
//        Coordinate coord = new Coordinate(50.0,50.0);
//        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
//        EventDBModel instance = new EventDBModel(coordModel,
//                true, 10000, 10000, "description", "jozef plateaustraat 22",
//                new ArrayList<>(), 1, "1", 0, 0);
//        
//        HashMap<String,Object> map = new HashMap<>();
//        map.put("coordinates", coordModel.toDocument());
//        map.put("active", instance.getActive());
//        map.put("gridX", 0);
//        map.put("gridY", 0);
//        map.put("publicationTimeMillis", 10000);
//        map.put("lastEditTimeMillis", 10000);
//        map.put("description", "description");
//        List<Document> jamDocs = new ArrayList<>();
//        List<JamDBModel> jams = new ArrayList<>();
//        jams.forEach(jam -> {
//            jamDocs.add(jam.toDocument());
//        });
//        map.put("jams", jamDocs);
//        map.put("uuid", 1);
//        map.put("eventTypeId", 1);
//        map.put("formattedAddress", "jozef plateaustraat 22");
//        
//        Document expResult = new Document(map);
//        Document result = instance.toDocument();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of parse method, of class EventDBModel.
//     */
//    @Test
//    public void testParse() {
//        Coordinate coord = new Coordinate(50.0,50.0);
//        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
//        EventDBModel instance = new EventDBModel(coordModel,
//                true, 10000, 10000, "description", "jozef plateaustraat 22",
//                new ArrayList<>(), 1, "uuid", 0, 0);
//        instance.setId("573329db89f2758026c06e5c");
//        HashMap<String,Object> map = new HashMap<>();
//         map.put("coordinates", coordModel.toDocument());
//        map.put("active", instance.getActive());
//        map.put("gridX", 0);
//        map.put("gridY", 0);
//        map.put("publicationTimeMillis", 10000L);
//        map.put("lastEditTimeMillis", 10000L);
//        map.put("description", "description");
//        List<Document> jamDocs = new ArrayList<>();
//        List<JamDBModel> jams = new ArrayList<>();
//        jams.forEach(jam -> {
//            jamDocs.add(jam.toDocument());
//        });
//        map.put("jams", jamDocs);
//        map.put("uuid", "1");
//        map.put("eventTypeId", 1);
//        map.put("formattedAddress", "jozef plateaustraat 22");
//        map.put("_id", new ObjectId());
//        Document doc = new Document(map);
//        EventDBModel expResult = instance;
//        EventDBModel result = EventDBModel.parse(doc);
//        assertEquals(expResult, result);
//    }

    /**
     * Test of getId method, of class EventDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        String expResult = null;
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class EventDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        String id = "2";
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getCoordinates method, of class EventDBModel.
     */
    @Test
    public void testGetCoordinates() {
        System.out.println("getCoordinates");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        CoordinateDBModel expResult = new CoordinateDBModel(coord);
        CoordinateDBModel result = instance.getCoordinates();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCoordinates method, of class EventDBModel.
     */
    @Test
    public void testSetCoordinates() {
        System.out.println("setCoordinates");
        CoordinateDBModel coordinates = new CoordinateDBModel(new Coordinate(60.0,60.0));
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setCoordinates(coordinates);
        assertEquals(coordinates,instance.getCoordinates());
    }

    /**
     * Test of getActive method, of class EventDBModel.
     */
    @Test
    public void testGetActive() {
        System.out.println("getActive");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        Boolean expResult = true;
        Boolean result = instance.getActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActive method, of class EventDBModel.
     */
    @Test
    public void testSetActive() {
        System.out.println("setActive");
        Boolean active = false;
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setActive(active);
        assertEquals(active,instance.getActive());
    }

    /**
     * Test of getPublicationTimeMillis method, of class EventDBModel.
     */
    @Test
    public void testGetPublicationTimeMillis() {
        System.out.println("getPublicationTimeMillis");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        long expResult = 10000;
        long result = instance.getPublicationTimeMillis();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPublicationTimeMillis method, of class EventDBModel.
     */
    @Test
    public void testSetPublicationTimeMillis() {
        System.out.println("setPublicationTimeMillis");
        long publicationTimeMillis = 0L;
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setPublicationTimeMillis(publicationTimeMillis);
        assertEquals(publicationTimeMillis,instance.getPublicationTimeMillis());
    }

    /**
     * Test of getLastEditTimeMillis method, of class EventDBModel.
     */
    @Test
    public void testGetLastEditTimeMillis() {
        System.out.println("getLastEditTimeMillis");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        long expResult = 10000;
        long result = instance.getLastEditTimeMillis();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastEditTimeMillis method, of class EventDBModel.
     */
    @Test
    public void testSetLastEditTimeMillis() {
        System.out.println("setLastEditTimeMillis");
        long lastEditTimeMillis = 0L;
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setLastEditTimeMillis(lastEditTimeMillis);
        assertEquals(lastEditTimeMillis,instance.getLastEditTimeMillis());
    }

    /**
     * Test of getDescription method, of class EventDBModel.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        String expResult = "description";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescription method, of class EventDBModel.
     */
    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
        String description = "beschrijving";
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setDescription(description);
    }

    /**
     * Test of getJams method, of class EventDBModel.
     */
    @Test
    public void testGetJams() {
        System.out.println("getJams");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        List<JamDBModel> expResult = new ArrayList<>();
        List<JamDBModel> result = instance.getJams();
        assertEquals(expResult, result);
    }

    /**
     * Test of setJam method, of class EventDBModel.
     */
    @Test
    public void testSetJam() {
        System.out.println("setJam");
        List<JamDBModel> jams = new ArrayList<>();
       Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setJam(jams);
    }

    /**
     * Test of getEventTypeId method, of class EventDBModel.
     */
    @Test
    public void testGetEventTypeId() {
        System.out.println("getEventTypeId");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        int expResult = 1;
        int result = instance.getEventTypeId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventTypeId method, of class EventDBModel.
     */
    @Test
    public void testSetEventTypeId() {
        System.out.println("setEventTypeId");
        int eventTypeId = 2;
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setEventTypeId(eventTypeId);
        assertEquals(eventTypeId,instance.getEventTypeId());
    }

    /**
     * Test of getGridX method, of class EventDBModel.
     */
    @Test
    public void testGetGridX() {
        System.out.println("getGridX");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        int expResult = 0;
        int result = instance.getGridX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGridY method, of class EventDBModel.
     */
    @Test
    public void testGetGridY() {
        System.out.println("getGridY");
       Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        int expResult = 0;
        int result = instance.getGridY();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUuid method, of class EventDBModel.
     */
    @Test
    public void testGetUuid() {
        System.out.println("getUuid");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        String expResult = "uuid";
        String result = instance.getUuid();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUuid method, of class EventDBModel.
     */
    @Test
    public void testSetUuid() {
        System.out.println("setUuid");
        String uuid = "uuidd";
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        instance.setUuid(uuid);
        assertEquals(uuid,instance.getUuid());
    }

    /**
     * Test of getFormattedAddress method, of class EventDBModel.
     */
    @Test
    public void testGetFormattedAddress() {
        System.out.println("getFormattedAddress");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        String expResult = "jozef plateaustraat 22";
        String result = instance.getFormattedAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class EventDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        int expResult = -1120263814;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class EventDBModel.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        Coordinate coord = new Coordinate(50.0,50.0);
        CoordinateDBModel coordModel = new CoordinateDBModel(coord);
        EventDBModel instance = new EventDBModel(coordModel,
                true, 10000, 10000, "description", "jozef plateaustraat 22",
                new ArrayList<>(), 1, "uuid", 0, 0);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
}

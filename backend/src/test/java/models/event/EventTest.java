/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.event;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Coordinate;
import models.Transportation;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class EventTest {
    
    public EventTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_1() throws MalformedURLException { 
        Event instance =  new Event( null, true, "2016-05-15T17:24:03.881", 1200,
                "description", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_2() throws MalformedURLException { 
        Event instance =  new Event( null, true, "2016-05-15T17:24:03.881", 1200,
                null, "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_3() throws MalformedURLException { 
        Event instance =  new Event( null, true, "20", 1200,
                "description", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_4() throws MalformedURLException { 
        Event instance =  new Event("uuid", null, true, 1200, 1200,
                "description", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_5() throws MalformedURLException { 
        Event instance =  new Event("uuid", new Coordinate(50,50), true, 1200, 1200,
                null, "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
    }
    
    /**
     * Test of getCoordinates method, of class Event.
     */
    @Test
    public void testGetCoordinates() throws MalformedURLException {
        System.out.println("getCoordinates");
        Coordinate end = new Coordinate(50,60);
        Coordinate expResult = end;
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Coordinate result = instance.getCoordinates();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCoordinates method, of class Event.
     */
    @Test
    public void testSetCoordinates() throws MalformedURLException {
        System.out.println("setCoordinates");
        Coordinate end = new Coordinate(50,50);
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Coordinate coordinate = new Coordinate(20,40);
        instance.setCoordinates(coordinate);
        assertEquals(coordinate, instance.getCoordinates());
    }

    /**
     * Test of setCoordinates method, of class Event.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCoordinates_2() throws MalformedURLException {
        System.out.println("setCoordinates");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Coordinate coordinate = null;
        instance.setCoordinates(coordinate);
        assertEquals(coordinate, instance.getCoordinates());
    }
    
    /**
     * Test of isActive method, of class Event.
     */
    @Test
    public void testIsActive() throws MalformedURLException {
        System.out.println("isActive");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Boolean expResult = true;
        Boolean result = instance.isActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActive method, of class Event.
     */
    @Test
    public void testSetActive() throws MalformedURLException {
        System.out.println("setActive");
        Boolean active = false;
        Coordinate end = new Coordinate(50,50);
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.setActive(active);
        assertEquals(active,instance.isActive());
    }

    /**
     * Test of activate method, of class Event.
     */
    @Test
    public void testActivate() throws MalformedURLException {
        System.out.println("activate");
        Coordinate end = new Coordinate(50,50);
        Event instance =  new Event("uuid", new Coordinate(50,60), false, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.activate();
        assertEquals(instance.isActive(),true);
    }

    /**
     * Test of deactivate method, of class Event.
     */
    @Test
    public void testDeactivate() throws MalformedURLException {
        System.out.println("activate");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.deactivate();
        assertEquals(instance.isActive(),false);
    }

    /**
     * Test of getPublicationTime method, of class Event.
     */
    @Test
    public void testGetPublicationTime() throws MalformedURLException {
        System.out.println("getPublicationTime");
        long time = 1200;
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        assertEquals(instance.getPublicationTimeMillis(),time);
    }


    /**
     * Test of getLastEditTime method, of class Event.
     */
    @Test
    public void testGetLastEditTime() throws MalformedURLException {
        System.out.println("activate");
        Coordinate end = new Coordinate(50,50);
        long time = 1200;
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.activate();
        assertEquals(instance.getLastEditTimeMillis(),time);
    }

    /**
     * Test of setLastEditTime method, of class Event.
     */
    @Test
    public void testSetLastEditTime() throws MalformedURLException {
        System.out.println("setLastEditTime");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.setLastEditTimeMillis(1400);
        assertEquals(instance.getLastEditTimeMillis(),1400);
    }

    /**
     * Test of getDescription method, of class Event.
     */
    @Test
    public void testGetDescription() throws MalformedURLException {
        System.out.println("getDescription");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "description", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        String expResult = "description";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescription method, of class Event.
     */
    @Test
    public void testSetDescription() throws MalformedURLException {
        System.out.println("setDescription");
        String description = "aa";
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.setDescription(description);
        assertEquals(instance.getDescription(),description);
    }

    /**
     * Test of getType method, of class Event.
     */
    @Test
    public void testGetType() throws MalformedURLException {
        System.out.println("getType");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        EventType expResult = new EventType("Jam",Arrays.asList(new Transportation[]{Transportation.CAR}));
        EventType result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setType method, of class Event.
     */
    @Test
    public void testSetType() throws MalformedURLException {
        System.out.println("setType");
        EventType type =  new EventType("ROAD HAZARD", new ArrayList<>());
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        EventType expResult = new EventType("ROAD HAZARD", new ArrayList<>());
        instance.setType(type);
        assertEquals(expResult,instance.getType());
    }

    /**
     * Test of equals method, of class Event.
     */
    @Test
    public void testEqualsFalse_1() throws MalformedURLException {
        System.out.println("equals");
        Event instance1 = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Event instance2 = new Event("uui", new Coordinate(50,50), true, 1300, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR, Transportation.BIKE})));
        boolean expResult = false;
        boolean result = instance1.equals(instance2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEqualsFalse_2() throws MalformedURLException {
        System.out.println("equals");
        Event instance1 = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Event instance2 = null;
        boolean expResult = false;
        boolean result = instance1.equals(instance2);
        assertEquals(expResult, result);
    }
    
    public void testEqualsTrue_1() throws MalformedURLException {
        System.out.println("equals");
        Event instance1 = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        boolean expResult = true;
        boolean result = instance1.equals(instance1);
        assertEquals(expResult, result);
    }
    
    
    /**
     * Test of equals method, of class Event.
     */
    @Test
    public void testEqualsTrue_2() throws MalformedURLException {
        System.out.println("equals");
        Object obj = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransportTypes method, of class Event.
     */
    @Test
    public void testGetTransportTypes() throws MalformedURLException {
        System.out.println("getTransportTypes");
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        List<Transportation> expResult = Arrays.asList(new Transportation[]{Transportation.CAR});
        List<Transportation> result = instance.getTransportTypes();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUuid method, of class Event.
     */
    @Test
    public void testGetUuid() throws Exception{
        System.out.println("getUuid");
        Event instance =  new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        String expResult = "uuid";
        String result = instance.getUuid();
        assertEquals(expResult, result);
    }


    /**
     * Test of getPublicationString method, of class Event.
     */
    @Test
    public void testGetPublicationString() {
        System.out.println("getPublicationString");
        Event instance =new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        String expResult = "1970-01-01T01:00:01.200";
        String result = instance.getPublicationString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastEditString method, of class Event.
     */
    @Test
    public void testGetLastEditString() {
        System.out.println("getLastEditString");
        Event instance =new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        String expResult = "1970-01-01T01:00:01.200";
        String result = instance.getLastEditString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFormattedAddress method, of class Event.
     */
    @Test
    public void testGetFormattedAddress() {
        System.out.println("getFormattedAddress");
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        String expResult = "krijtekerkweg 17, 9041 Oostakker";
        String result = instance.getFormattedAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of addJam method, of class Event.
     */
    @Test
    public void testAddJam() {
        System.out.println("addJam");
        Jam jam = new Jam("2016-05-15T17:24:03.881", Arrays.asList(new Coordinate[]{new Coordinate(50,50),new Coordinate(50,60), new Coordinate(60,60)}), 10, 10);
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.addJam(jam);
        assertFalse(instance.getAllJams().isEmpty());
        assertEquals(jam,instance.getAllJams().get(0));
    }

    /**
     * Test of getJamIfPresent method, of class Event.
     */
    @Test
    public void testGetJamIfPresent() {
        System.out.println("getJamIfPresent");
        String uuid = "1234";
        Jam jam = new Jam("1234",1236, Arrays.asList(new Coordinate[]{new Coordinate(50,50),new Coordinate(50,60), new Coordinate(60,60)}), 10, 10);
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.addJam(jam);
        Jam result = instance.getJamIfPresent(uuid);
        assertEquals(jam, result);
    }

    /**
     * Test of getJamIfPresent method, of class Event.
     */
    @Test
    public void testGetJamIfPresent_2() {
        System.out.println("getJamIfPresent");
        String uuid = "12342";
        Jam jam = new Jam("1234",1236, Arrays.asList(new Coordinate[]{new Coordinate(50,50),new Coordinate(50,60), new Coordinate(60,60)}), 10, 10);
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        Jam result = instance.getJamIfPresent(uuid);
        instance.addJam(jam);
        assertEquals(null, result);
    }
    
    /**
     * Test of deleteJam method, of class Event.
     */
    @Test
    public void testDeleteJam() {
        System.out.println("deleteJam");
        Jam jam1 = new Jam("1234",1236, Arrays.asList(new Coordinate[]{new Coordinate(50,50),new Coordinate(50,60), new Coordinate(60,60)}), 10, 10);
        Jam jam2 = new Jam("1235",1236, Arrays.asList(new Coordinate[]{new Coordinate(40,50),new Coordinate(40,60), new Coordinate(40,60)}), 15, 15);
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.addJam(jam1);
        instance.addJam(jam2);
        assertFalse(instance.getAllJams().isEmpty());
        instance.deleteJam("1234");
        assertTrue(instance.getAllJams().size()==1);
        assertEquals(instance.getJamIfPresent("1235"),jam2);
        assertEquals(instance.getJamIfPresent("1234"),null);
    }

    /**
     * Test of getAllJams method, of class Event.
     */
    @Test
    public void testGetAllJams() {
        System.out.println("getAllJams");
        Jam jam1 = new Jam("1234",1236, Arrays.asList(new Coordinate[]{new Coordinate(50,50),new Coordinate(50,60), new Coordinate(60,60)}), 10, 10);
        Jam jam2 = new Jam("1235",1236, Arrays.asList(new Coordinate[]{new Coordinate(40,50),new Coordinate(40,60), new Coordinate(40,60)}), 15, 15);
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.addJam(jam1);
        instance.addJam(jam2);
        List<Jam> expResult = Arrays.asList(new Jam []{jam1,jam2});
        List<Jam> result = instance.getAllJams();
        for(int i=0;i<result.size();i++){
            assertTrue(result.containsAll(expResult));
        }
        
    }

    /**
     * Test of deleteAllJams method, of class Event.
     */
    @Test
    public void testDeleteAllJams() {
        System.out.println("deleteAllJams");
        Jam jam1 = new Jam("1234",1236, Arrays.asList(new Coordinate[]{new Coordinate(50,50),new Coordinate(50,60), new Coordinate(60,60)}), 10, 10);
        Jam jam2 = new Jam("1235",1236, Arrays.asList(new Coordinate[]{new Coordinate(40,50),new Coordinate(40,60), new Coordinate(40,60)}), 15, 15);
        Event instance = new Event("uuid", new Coordinate(50,60), true, 1200, 1200,
                "jam in the street", "krijtekerkweg 17, 9041 Oostakker",
                new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR})));
        instance.addJam(jam1);
        instance.addJam(jam2);
        instance.deleteAllJams();
    }


}

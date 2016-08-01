/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.event.EventType;
import models.services.PhoneNumber;
import models.services.Service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author hannedesutter
 */
public class RouteTest {
    
    public RouteTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test of getWayPoints method, of class Route.
     */
    @Test
    public void testGetUserWayPoints() {
        System.out.println("getWayPoints");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        Route instance = new Route(Arrays.asList(wayPoints), new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        List<Coordinate> expResult = Arrays.asList(wayPoints);
        List<Coordinate> result = instance.getUserWaypoints();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWayPoints method, of class Route.
     */
    @Test
    public void testGetFullWayPoints() {
        System.out.println("getWayPoints");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        Route instance = new Route(Arrays.asList(wayPoints), new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        List<Coordinate> expResult = new ArrayList<>();
        List<Coordinate> result = instance.getFullWaypoints();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getTransportationTypes method, of class Route.
     */
    @Test
    public void testGetTransportationTypes() {
        System.out.println("getTransportationTypes");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        Route instance = new Route(Arrays.asList(wayPoints),new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        Transportation result = instance.getTransportationType();
        assertEquals(Transportation.TRAIN, result);
    }

    /**
     * Test of getNotifyForEventTypes method, of class Route.
     */
    @Test
    public void testGetNotifyForEventTypes() {
        System.out.println("getNotifyForEventTypes");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        HashMap<Integer, EventType> nftt = new HashMap<>();
        Route instance = new Route(Arrays.asList(wayPoints),new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        Map<Integer, EventType> result = instance.getNotifyForEventTypes();
        assertEquals(nftt, result);
    }

    @Test
    public void testGetTransportationType(){
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        HashMap<Integer, EventType> nftt = new HashMap<>();
        Route instance = new Route(Arrays.asList(wayPoints),new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        assertEquals(instance.getTransportationType(),Transportation.TRAIN);
    }

    /**
     * Test of getServices method, of class Route.
     */
    @Test
    public void testGetServices() {
        System.out.println("getServices");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        HashMap<Integer, EventType> nftt = new HashMap<>();
        Route instance = new Route(Arrays.asList(wayPoints),new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, Arrays.asList(new Service[]{new PhoneNumber("0499311921")}));
        List<Service> expResult = Arrays.asList(new Service [] {new PhoneNumber("0499311921")});
        List<Service> result = instance.getServices();
        assertEquals(expResult, result);
    }

    /**
     * Test of addService method, of class Route.
     */
    @Test
    public void testAddService() {
        System.out.println("addService");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        HashMap<Integer, EventType> nftt = new HashMap<>();
        Route instance = new Route(Arrays.asList(wayPoints),new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        Service service = new PhoneNumber("0499311921");
        instance.addService(service);
        assertFalse(instance.getServices().isEmpty());
    }

    /**
     * Test of removeService method, of class Route.
     */
    @Test
    public void testRemoveService() {
        System.out.println("removeService");
        Coordinate [] wayPoints = new Coordinate[2];
        wayPoints[0] = new Coordinate(50,50);
        wayPoints[1] = new Coordinate(50,60);
        Route instance = new Route(Arrays.asList(wayPoints),new ArrayList<>(),Transportation.TRAIN,new HashMap<>(),false, new ArrayList<>());
        Service service = new PhoneNumber("0499311921");
        instance.addService(service);
        assertFalse(instance.getServices().isEmpty());
        instance.removeService(service);
        assertTrue(instance.getServices().isEmpty());
    }
    
    
}
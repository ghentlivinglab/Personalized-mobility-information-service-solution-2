/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Transportation;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class EventTypeTest {
    
    public EventTypeTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test of getType method, of class EventType.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        EventType instance = new EventType("Jam", new ArrayList<>());
        String expResult = "Jam";
        String result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class EventType.
     */
    @Test
    public void testEqualsTrue() {
        System.out.println("equals");
        Object obj = new EventType("Jam",new ArrayList<>());
        EventType instance =new EventType("Jam",new ArrayList<>());
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEqualsFalse_1(){
        System.out.println("equals");
        Object obj = new EventType("Jam",new ArrayList<>());
        EventType instance =new EventType("Ja",new ArrayList<>());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    public void testEqualsFalse_2(){
        System.out.println("equals");
        Object obj = "";
        EventType instance =new EventType("Ja",new ArrayList<>());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    public void testEqualsFalse_3(){
        System.out.println("equals");
        Object obj = null;
        EventType instance =new EventType("Ja",new ArrayList<>());
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransportationTypes method, of class EventType.
     */
    @Test
    public void testGetTransportationTypes() {
        System.out.println("getTransportationTypes");
        EventType instance = new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR}));
        List<Transportation> expResult = Arrays.asList(new Transportation[]{Transportation.CAR});
        List<Transportation> result = instance.getTransportationTypes();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTransportationTypes method, of class EventType.
     */
    @Test
    public void testSetTransportationTypes() {
        System.out.println("setTransportationTypes");
        List<Transportation> transportationTypes = Arrays.asList(new Transportation[]{Transportation.CAR});
        EventType instance = new EventType("Jam",Arrays.asList(new Transportation[]{Transportation.TRAIN}));
        instance.setTransportationTypes(transportationTypes);
        assertEquals(instance.getTransportationTypes(),transportationTypes);
    }

    /**
     * Test of hashCode method, of class EventType.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        EventType instance = new EventType("Jam", Arrays.asList(new Transportation[]{Transportation.CAR}));
        int expResult = 74757;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    
}
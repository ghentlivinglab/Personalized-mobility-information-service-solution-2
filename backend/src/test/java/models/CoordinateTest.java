/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class CoordinateTest {
    
    public CoordinateTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testConstructor(){
        System.out.println("constructor");
        Coordinate instance = new Coordinate(50,50);
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testConstructorException_1(){
        System.out.println("constructor");
        Coordinate instance = new Coordinate(5000,5000);
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testConstructorException_2(){
        System.out.println("constructor");
        Coordinate instance = new Coordinate(50,-200);
    }
    
    /**
     * Test of getLat method, of class Coordinate.
     */
    @Test
    public void testGetLat() {
        System.out.println("getLat");
        Coordinate instance = new Coordinate(50,50);
        double expResult = 50.0;
        double result = instance.getLat();
        assertEquals("The latitude did not match.",expResult, result, 50.0);
    }

    /**
     * Test of getLon method, of class Coordinate.
     */
    @Test
    public void testGetLon() {
        System.out.println("getLon");
        Coordinate instance = new Coordinate(50,50);
        double expResult = 50.0;
        double result = instance.getLon();
        assertEquals("The longitude did not match.",expResult, result, 50.0);
    }

    /**
     * Test of equals method, of class Coordinate.
     */
    @Test
    public void testEqualsTrue() {
        System.out.println("equals");
        Object obj = new Coordinate(50,50);
        Coordinate instance = new Coordinate(50,50);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Coordinate.
     */
    @Test
    public void testEqualsFalse() {
        System.out.println("equals");
        Object obj = new Coordinate(10,50);
        Coordinate instance = new Coordinate(50,50);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of getX method, of class Coordinate.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        Coordinate instance = new Coordinate(50,50);
        double expResult = 3245097.4286561566;
        double result = instance.getX();
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of getY method, of class Coordinate.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        Coordinate instance = new Coordinate(50,50);
        double expResult = -117207.88553436068;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of distance method, of class Coordinate.
     */
    @Test
    public void testDistance() {
        System.out.println("distance");
        Coordinate instance1 = new Coordinate(50,50);
        Coordinate instance2 = new Coordinate(50,50);
        double expResult = 0.0;
        double result = instance1.distance(instance2);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getSquaredCartDistance method, of class Coordinate.
     */
    @Test
    public void testGetSquaredCartDistance() {
        System.out.println("getSquaredCartDistance");
        Coordinate instance1 = new Coordinate(50,50);
        Coordinate instance2 = new Coordinate(60,60);
        double expResult = 1.729277261096098E12;
        double result = instance1.getSquaredCartDistance(instance2);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of equals method, of class Coordinate.
     */
    @Test
    public void testEquals_False() {
        System.out.println("equals");
        Object obj = "SKDKD";
        Coordinate instance = new Coordinate(60,60);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Coordinate.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Coordinate instance = new Coordinate(50,50);
        int expResult = 865861779;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
}

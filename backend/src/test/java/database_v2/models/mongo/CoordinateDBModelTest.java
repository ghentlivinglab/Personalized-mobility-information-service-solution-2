/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.mongo;

import java.util.HashMap;
import models.Coordinate;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class CoordinateDBModelTest {
    
    public CoordinateDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of parse method, of class CoordinateDBModel.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        HashMap <String, Object> map = new HashMap<>();
        Coordinate coordinate = new Coordinate(50.0,50.0);
        map.put("lon", 50.0);
        map.put("lat", 50.0);
        map.put("cartX", coordinate.getX());
        map.put("cartY", coordinate.getY());
        Document doc = new Document(map);
        CoordinateDBModel expResult = new CoordinateDBModel(new Coordinate(50.0,50.0));
        CoordinateDBModel result = CoordinateDBModel.parse(doc);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLat method, of class CoordinateDBModel.
     */
    @Test
    public void testGetLat() {
        System.out.println("getLat");
        CoordinateDBModel instance = new CoordinateDBModel(new Coordinate(50,50));
        double expResult = 50;
        double result = instance.getLat();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLon method, of class CoordinateDBModel.
     */
    @Test
    public void testGetLon() {
        System.out.println("getLon");
        CoordinateDBModel instance = new CoordinateDBModel(new Coordinate(50,50));
        double expResult = 50;
        double result = instance.getLon();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of equals method, of class CoordinateDBModel.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = new CoordinateDBModel(new Coordinate(50,50));
        CoordinateDBModel instance = new CoordinateDBModel(new Coordinate(50,50));
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class CoordinateDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        CoordinateDBModel instance = new CoordinateDBModel(new Coordinate(50,50));
        int expResult = 865861779;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of toDocument method, of class CoordinateDBModel.
     */
    @Test
    public void testToDocument() {
        System.out.println("toDocument");
        CoordinateDBModel instance = new CoordinateDBModel(new Coordinate(50.0,50.0));
        HashMap <String, Object> map = new HashMap<>();
        Coordinate coordinate = new Coordinate(50.0,50.0);
        map.put("lon", 50.0);
        map.put("lat", 50.0);
        map.put("cartX", coordinate.getX());
        map.put("cartY", coordinate.getY());
        Document expResult = new Document(map);
        Document result = instance.toDocument();
        assertEquals(expResult, result);
    }
    
}

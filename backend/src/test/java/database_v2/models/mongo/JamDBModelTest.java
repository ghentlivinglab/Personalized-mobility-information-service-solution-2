/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.mongo;

import java.util.ArrayList;
import java.util.List;
import models.Coordinate;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class JamDBModelTest {
    
    public JamDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of parse method, of class JamDBModel.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        Document doc = new Document();
        doc.append("uuid", "1");
        doc.append("publicationTimeMillis", 100L);
         List<Document> lineDoc = new ArrayList<>();
        new ArrayList<CoordinateDBModel>().forEach(coord -> {
            lineDoc.add(coord.toDocument());
        });
        doc.append("line", lineDoc);
        doc.append("speed", 100);
        doc.append("delay", 100);
        JamDBModel expResult = new JamDBModel("1", 100L, new ArrayList<>(), 100, 100);
        JamDBModel result = JamDBModel.parse(doc);
        assertEquals(expResult, result);
    }

    /**
     * Test of toDocument method, of class JamDBModel.
     */
    @Test
    public void testToDocument() {
        System.out.println("toDocument");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        Document doc = new Document();
        doc.append("uuid", "1");
        doc.append("publicationTimeMillis", 100L);
         List<Document> lineDoc = new ArrayList<>();
        new ArrayList<CoordinateDBModel>().forEach(coord -> {
            lineDoc.add(coord.toDocument());
        });
        doc.append("line", lineDoc);
        doc.append("speed", 100);
        doc.append("delay", 100);
        Document expResult = doc;
        Document result = instance.toDocument();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUuid method, of class JamDBModel.
     */
    @Test
    public void testGetUuid() {
        System.out.println("getUuid");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        String expResult = "1";
        String result = instance.getUuid();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUuid method, of class JamDBModel.
     */
    @Test
    public void testSetUuid() {
        System.out.println("setUuid");
        String uuid = "2";
        JamDBModel instance =new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        instance.setUuid(uuid);
        assertEquals(uuid,instance.getUuid());
    }

    /**
     * Test of getPublicationTimeMillis method, of class JamDBModel.
     */
    @Test
    public void testGetPublicationTimeMillis() {
        System.out.println("getPublicationTimeMillis");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        long expResult = 100L;
        long result = instance.getPublicationTimeMillis();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPublicationTimeMillis method, of class JamDBModel.
     */
    @Test
    public void testSetPublicationTimeMillis() {
        System.out.println("setPublicationTimeMillis");
        long publicationTimeMillis = 1000L;
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        instance.setPublicationTimeMillis(publicationTimeMillis);
        assertEquals(publicationTimeMillis,instance.getPublicationTimeMillis());
    }

    /**
     * Test of getLine method, of class JamDBModel.
     */
    @Test
    public void testGetLine() {
        System.out.println("getLine");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        List<CoordinateDBModel> expResult = new ArrayList<>();
        List<CoordinateDBModel> result = instance.getLine();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLine method, of class JamDBModel.
     */
    @Test
    public void testSetLine() {
        System.out.println("setLine");
        List<CoordinateDBModel> line = new ArrayList<>();
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        instance.setLine(line);
    }

    /**
     * Test of getSpeed method, of class JamDBModel.
     */
    @Test
    public void testGetSpeed() {
        System.out.println("getSpeed");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        int expResult = 100;
        int result = instance.getSpeed();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSpeed method, of class JamDBModel.
     */
    @Test
    public void testSetSpeed() {
        System.out.println("setSpeed");
        int speed = 10;
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        instance.setSpeed(speed);
        assertEquals(speed,instance.getSpeed());
    }

    /**
     * Test of getDelay method, of class JamDBModel.
     */
    @Test
    public void testGetDelay() {
        System.out.println("getDelay");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        int expResult = 100;
        int result = instance.getDelay();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDelay method, of class JamDBModel.
     */
    @Test
    public void testSetDelay() {
        System.out.println("setDelay");
        int delay =10;
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        instance.setDelay(delay);
        assertEquals(delay,instance.getDelay());
    }

    /**
     * Test of hashCode method, of class JamDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        int expResult = -1240805483;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class JamDBModel.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        JamDBModel instance = new JamDBModel("1", 100, new ArrayList<>(), 100, 100);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.mongo;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class SourceDBModelTest {
    
    public SourceDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of parse method, of class SourceDBModel.
     */
    @Test
    public void testParse() {
        Document doc = new Document();
        doc.append("name", "name");
        doc.append("url", "url");
        SourceDBModel expResult = new SourceDBModel("name", "url");
        SourceDBModel result = SourceDBModel.parse(doc);
        assertEquals(expResult, result);
    }

    /**
     * Test of toDocument method, of class SourceDBModel.
     */
    @Test
    public void testToDocument() {
        SourceDBModel instance = new SourceDBModel("name", "url");
        Document expResult = new Document();
        expResult.append("name", "name");
        expResult.append("url", "url");
        Document result = instance.toDocument();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class SourceDBModel.
     */
    @Test
    public void testGetName() {
        SourceDBModel instance = new SourceDBModel("name", "url");
        String expResult = "name";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class SourceDBModel.
     */
    @Test
    public void testSetName() {
        String name = "naam";
        SourceDBModel instance =  new SourceDBModel("name", "url");
        instance.setName(name);
        assertEquals(name,instance.getName());
    }

    /**
     * Test of getUrl method, of class SourceDBModel.
     */
    @Test
    public void testGetUrl() {
        SourceDBModel instance =  new SourceDBModel("name", "url");
        String expResult = "url";
        String result = instance.getUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUrl method, of class SourceDBModel.
     */
    @Test
    public void testSetUrl() {
        String url = "url1";
        SourceDBModel instance =  new SourceDBModel("name", "url");
        instance.setUrl(url);
        assertEquals(url, instance.getUrl());
    }

    /**
     * Test of equals method, of class SourceDBModel.
     */
    @Test
    public void testEquals() {
        Object obj =  null;
        SourceDBModel instance =  new SourceDBModel("name", "url");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class SourceDBModel.
     */
    @Test
    public void testEquals_1() {
        Object obj =  new SourceDBModel("name", "url");
        SourceDBModel instance =  new SourceDBModel("name", "url");
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class SourceDBModel.
     */
    @Test
    public void testEquals_2() {
        SourceDBModel instance =  new SourceDBModel("name", "url");
        Object obj =  instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class SourceDBModel.
     */
    @Test
    public void testEquals_3() {
        SourceDBModel instance =  new SourceDBModel("name", "url");
        Object obj =  "String";
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class SourceDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        SourceDBModel instance =  new SourceDBModel("name", "url");
        int expResult = 871848257;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models;

import java.util.HashMap;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class MongoModelTest {
    
    public MongoModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of toDocument method, of class MongoModel.
     */
    @Test
    public void testToDocument() {
        System.out.println("toDocument");
        MongoModel instance = new MongoModelImpl();
        Document expResult = new Document(new HashMap<>());
        Document result = instance.toDocument();
        assertEquals(expResult, result);
    }

    public class MongoModelImpl implements MongoModel {

        @Override
        public Document toDocument() {
            return new Document(new HashMap<>());
        }
    }
    
}

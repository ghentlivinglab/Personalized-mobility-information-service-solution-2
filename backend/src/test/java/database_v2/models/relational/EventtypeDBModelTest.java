/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class EventtypeDBModelTest {
    
    private static final String tableName = "eventtype";
    
    public EventtypeDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class EventtypeDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        EventtypeDBModel instance = new EventtypeDBModel("jam", null);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new Attribute("type", "type", AttributeType.TEXT, tableName, true));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class EventtypeDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        EventtypeDBModel instance = new EventtypeDBModel();
       List<Attribute> expResult = new ArrayList<>();
        expResult.add(new Attribute("type", "type", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("relevantTransportTypes", "relevant_transportation_types", AttributeType.ARRAY, tableName, false));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class EventtypeDBModel.
     */
    @Test
    public void testGetTableName() {
        EventtypeDBModel instance = new EventtypeDBModel("type",null);
        String expResult = "eventtype";
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class EventtypeDBModel.
     */
    @Test
    public void testSetId() {
        int id = 1;
        EventtypeDBModel instance = new EventtypeDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class EventtypeDBModel.
     */
    @Test
    public void testGetId() {
        EventtypeDBModel instance = new EventtypeDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class EventtypeDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        EventtypeDBModel instance = new EventtypeDBModel();
        String expResult = "eventtypeid";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventtypeId method, of class EventtypeDBModel.
     */
    @Test
    public void testGetEventtypeId() {
        EventtypeDBModel instance = new EventtypeDBModel();
        Integer expResult = null;
        Integer result = instance.getEventtypeId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventtypeIdAttribute method, of class EventtypeDBModel.
     */
    @Test
    public void testGetEventtypeIdAttribute() {
        Attribute expResult = new Attribute("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true);
        Attribute result = EventtypeDBModel.getEventtypeIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventtypeId method, of class EventtypeDBModel.
     */
    @Test
    public void testSetEventtypeId() {
        Integer eventtypeId = 1;
        EventtypeDBModel instance = new EventtypeDBModel();
        instance.setEventtypeId(eventtypeId);
        assertEquals(eventtypeId,instance.getEventtypeId());
    }

    /**
     * Test of getType method, of class EventtypeDBModel.
     */
    @Test
    public void testGetType() {
        EventtypeDBModel instance = new EventtypeDBModel("type",null);
        String expResult = "type";
        String result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTypeAttribute method, of class EventtypeDBModel.
     */
    @Test
    public void testGetTypeAttribute() {
        System.out.println("getTypeAttribute");
        Attribute expResult = new Attribute("type", "type", AttributeType.TEXT, tableName, true);
        Attribute result = EventtypeDBModel.getTypeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setType method, of class EventtypeDBModel.
     */
    @Test
    public void testSetType() {
        String type = "jam";
        EventtypeDBModel instance = new EventtypeDBModel();
        instance.setType(type);
        assertEquals(type, instance.getType());
    }

    /**
     * Test of getRelevantTransportTypes method, of class EventtypeDBModel.
     */
    @Test
    public void testGetRelevantTransportTypes() {
        EventtypeDBModel instance = new EventtypeDBModel();
        Array expResult = null;
        Array result = instance.getRelevantTransportTypes();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRelevantTransportTypesAttribute method, of class EventtypeDBModel.
     */
    @Test
    public void testGetRelevantTransportTypesAttribute() {
        Attribute expResult = new Attribute("relevantTransportTypes", "relevant_transportation_types", AttributeType.ARRAY, tableName, false);
        Attribute result = EventtypeDBModel.getRelevantTransportTypesAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRelevantTransportTypes method, of class EventtypeDBModel.
     */
    @Test
    public void testSetRelevantTransportTypes() {
        Array relevantTransportTypes = null;
        EventtypeDBModel instance = new EventtypeDBModel();
        instance.setRelevantTransportTypes(relevantTransportTypes);
    }

    /**
     * Test of equals method, of class EventtypeDBModel.
     */
    @Test
    public void testEquals() {
        Object obj = null;
        EventtypeDBModel instance = new EventtypeDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class EventtypeDBModel.
     */
    @Test
    public void testEquals_1() {
        Object obj = "String";
        EventtypeDBModel instance = new EventtypeDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class EventtypeDBModel.
     */
    @Test
    public void testEquals_2() {
        Object obj = new EventtypeDBModel("type",null);
        EventtypeDBModel instance = new EventtypeDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class EventtypeDBModel.
     */
    @Test
    public void testEquals_3() {
        EventtypeDBModel instance = new EventtypeDBModel();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class EventtypeDBModel.
     */
    @Test
    public void testHashCode() {
        EventtypeDBModel instance = new EventtypeDBModel();
        int expResult = -1322530657;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

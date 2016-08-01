/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.ForeignKeyAttribute;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class LocationEventDBModelTest {
    
    private static final String tableName = "location_event";
    
    public LocationEventDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class LocationEventDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        LocationEventDBModel instance = new LocationEventDBModel(1, "1", Boolean.FALSE);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class));
        expResult.add(new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class LocationEventDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        System.out.println("getAllAttributeList");
        LocationEventDBModel instance = new LocationEventDBModel();
         List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class));
        expResult.add(new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class LocationEventDBModel.
     */
    @Test
    public void testGetTableName() {
        LocationEventDBModel instance = new LocationEventDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class LocationEventDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 1;
        LocationEventDBModel instance = new LocationEventDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class LocationEventDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        LocationEventDBModel instance = new LocationEventDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class LocationEventDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        LocationEventDBModel instance = new LocationEventDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationEventId method, of class LocationEventDBModel.
     */
    @Test
    public void testGetLocationEventId() {
        LocationEventDBModel instance = new LocationEventDBModel();
        Integer expResult = null;
        Integer result = instance.getLocationEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationEventIdAttribute method, of class LocationEventDBModel.
     */
    @Test
    public void testGetLocationEventIdAttribute() {
        Attribute expResult = new Attribute("locationEventId", "location_eventid", AttributeType.INTEGER, tableName, false);
        Attribute result = LocationEventDBModel.getLocationEventIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocationEventId method, of class LocationEventDBModel.
     */
    @Test
    public void testSetLocationEventId() {
        Integer locationEventId = 1;
        LocationEventDBModel instance = new LocationEventDBModel();
        instance.setLocationEventId(locationEventId);
        assertEquals(locationEventId,instance.getLocationEventId());
    }

    /**
     * Test of getLocationId method, of class LocationEventDBModel.
     */
    @Test
    public void testGetLocationId() {
        LocationEventDBModel instance = new LocationEventDBModel(1,"1",false);
        Integer expResult = 1;
        Integer result = instance.getLocationId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationIdAttribute method, of class LocationEventDBModel.
     */
    @Test
    public void testGetLocationIdAttribute() {
        ForeignKeyAttribute<LocationDBModel> expResult =  new ForeignKeyAttribute<>("locationId",
                "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class);;
        ForeignKeyAttribute<LocationDBModel> result = LocationEventDBModel.getLocationIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocationId method, of class LocationEventDBModel.
     */
    @Test
    public void testSetLocationId() {
        Integer locationId = 2;
        LocationEventDBModel instance = new LocationEventDBModel(1,"1",false);
        instance.setLocationId(locationId);
        assertEquals(locationId,instance.getLocationId());
    }

    /**
     * Test of getEventId method, of class LocationEventDBModel.
     */
    @Test
    public void testGetEventId() {
        LocationEventDBModel instance =new LocationEventDBModel(1,"1",false);
        String expResult = "1";
        String result = instance.getEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventIdAttribute method, of class LocationEventDBModel.
     */
    @Test
    public void testGetEventIdAttribute() {
        Attribute expResult = new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true);
        Attribute result = LocationEventDBModel.getEventIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventId method, of class LocationEventDBModel.
     */
    @Test
    public void testSetEventId() {
        String eventId = "1";
        LocationEventDBModel instance = new LocationEventDBModel();
        instance.setEventId(eventId);
        assertEquals(eventId, instance.getEventId());
    }

    /**
     * Test of getDeleted method, of class LocationEventDBModel.
     */
    @Test
    public void testGetDeleted() {
        LocationEventDBModel instance = new LocationEventDBModel(1,"1",false);
        Boolean expResult = false;
        Boolean result = instance.getDeleted();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDeletedAttribute method, of class LocationEventDBModel.
     */
    @Test
    public void testGetDeletedAttribute() {
        Attribute expResult = new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true);
        Attribute result = LocationEventDBModel.getDeletedAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDeleted method, of class LocationEventDBModel.
     */
    @Test
    public void testSetDeleted() {
        Boolean deleted = true;
        LocationEventDBModel instance = new LocationEventDBModel();
        instance.setDeleted(deleted);
        assertEquals(deleted, instance.getDeleted());
    }

    /**
     * Test of equals method, of class LocationEventDBModel.
     */
    @Test
    public void testEquals() {
        Object obj = null;
        LocationEventDBModel instance = new LocationEventDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class LocationEventDBModel.
     */
    @Test
    public void testEquals_1() {
        Object obj = "";
        LocationEventDBModel instance = new LocationEventDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class LocationEventDBModel.
     */
    @Test
    public void testEquals_2() {
        Object obj = new LocationEventDBModel(Integer.SIZE, tableName, Boolean.FALSE);
        LocationEventDBModel instance = new LocationEventDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class LocationEventDBModel.
     */
    @Test
    public void testEquals_3() {
        LocationEventDBModel instance = new LocationEventDBModel();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    /**
     * Test of hashCode method, of class LocationEventDBModel.
     */
    @Test
    public void testHashCode() {
        LocationEventDBModel instance = new LocationEventDBModel();
        int expResult = -1613088207;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

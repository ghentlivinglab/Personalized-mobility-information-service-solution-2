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
public class LocationEventtypeDBModelTest {
    
    private static final String tableName = "location_eventtype";
    
    public LocationEventtypeDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetTableName() {
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1, 1);
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 1;
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationEventtypeId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetLocationEventtypeId() {
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        Integer expResult = null;
        Integer result = instance.getLocationEventtypeId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationEventtypeIdAttribute method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetLocationEventtypeIdAttribute() {
        Attribute expResult = new Attribute("locationEventtypeId", "location_eventtypeid", AttributeType.INTEGER, tableName, false);
        Attribute result = LocationEventtypeDBModel.getLocationEventtypeIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocationEventtypeId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testSetLocationEventtypeId() {
        Integer locationEventtypeId = 1;
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel();
        instance.setLocationEventtypeId(locationEventtypeId);
        assertEquals(locationEventtypeId,instance.getLocationEventtypeId());
    }

    /**
     * Test of getLocationId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetLocationId() {
        System.out.println("getLocationId");
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        Integer expResult = 1;
        Integer result = instance.getLocationId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationIdAttribute method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetLocationIdAttribute() {
        ForeignKeyAttribute<LocationDBModel> expResult = new ForeignKeyAttribute<>("locationId", "locationid",
                AttributeType.INTEGER, tableName, true, LocationDBModel.class);
        ForeignKeyAttribute<LocationDBModel> result = LocationEventtypeDBModel.getLocationIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocationId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testSetLocationId() {
        Integer locationId = 1;
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel();
        instance.setLocationId(locationId);
        assertEquals(locationId,instance.getLocationId());
    }

    /**
     * Test of getEventtypeId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetEventtypeId() {
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel(1,1);
        Integer expResult = 1;
        Integer result = instance.getEventtypeId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventtypeIdAttribute method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testGetEventtypeIdAttribute() {
        ForeignKeyAttribute<EventtypeDBModel> expResult = new ForeignKeyAttribute<>("eventtypeId", "eventtypeid",
                AttributeType.INTEGER, tableName, true, EventtypeDBModel.class);
        ForeignKeyAttribute<EventtypeDBModel> result = LocationEventtypeDBModel.getEventtypeIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventtypeId method, of class LocationEventtypeDBModel.
     */
    @Test
    public void testSetEventtypeId() {
        Integer eventtypeId = 1;
        LocationEventtypeDBModel instance = new LocationEventtypeDBModel();
        instance.setEventtypeId(eventtypeId);
        assertEquals(eventtypeId,instance.getEventtypeId());
    }
    
}

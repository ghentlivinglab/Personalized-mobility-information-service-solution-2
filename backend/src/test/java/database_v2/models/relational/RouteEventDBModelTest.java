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
public class RouteEventDBModelTest {
    
    private static final String tableName = "route_event";
    
    public RouteEventDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class RouteEventDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        RouteEventDBModel instance = new RouteEventDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new Attribute("routeId", "routeid", AttributeType.INTEGER, tableName, true));
        expResult.add(new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class RouteEventDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        RouteEventDBModel instance = new RouteEventDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new Attribute("routeId", "routeid", AttributeType.INTEGER, tableName, true));
        expResult.add(new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class RouteEventDBModel.
     */
    @Test
    public void testGetTableName() {
        RouteEventDBModel instance = new RouteEventDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class RouteEventDBModel.
     */
    @Test
    public void testSetId() {
        int id = 1;
        RouteEventDBModel instance = new RouteEventDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class RouteEventDBModel.
     */
    @Test
    public void testGetId() {
        RouteEventDBModel instance = new RouteEventDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class RouteEventDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        RouteEventDBModel instance = new RouteEventDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteEventId method, of class RouteEventDBModel.
     */
    @Test
    public void testGetRouteEventId() {
        RouteEventDBModel instance = new RouteEventDBModel();
        Integer expResult = null;
        Integer result = instance.getRouteEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteEventIdAttribute method, of class RouteEventDBModel.
     */
    @Test
    public void testGetRouteEventIdAttribute() {
        Attribute expResult = new Attribute("routeEventId", "route_eventid",
                AttributeType.INTEGER, tableName, false);
        Attribute result = RouteEventDBModel.getRouteEventIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteEventId method, of class RouteEventDBModel.
     */
    @Test
    public void testSetRouteEventId() {
        Integer routeEventId = 1;
        RouteEventDBModel instance = new RouteEventDBModel();
        instance.setRouteEventId(routeEventId);
        assertEquals(routeEventId,instance.getRouteEventId());
    }

    /**
     * Test of getRouteId method, of class RouteEventDBModel.
     */
    @Test
    public void testGetRouteId() {
        RouteEventDBModel instance = new RouteEventDBModel(1,"1", Boolean.FALSE);
        Integer expResult = 1;
        Integer result = instance.getRouteId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteIdAttribute method, of class RouteEventDBModel.
     */
    @Test
    public void testGetRouteIdAttribute() {
        ForeignKeyAttribute<RouteDBModel> expResult = new ForeignKeyAttribute("routeId", "routeid",
                AttributeType.INTEGER, tableName, true, RouteDBModel.class);
        ForeignKeyAttribute<RouteDBModel> result = RouteEventDBModel.getRouteIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteId method, of class RouteEventDBModel.
     */
    @Test
    public void testSetRouteId() {
        Integer routeId = 2;
        RouteEventDBModel instance = new RouteEventDBModel(1,"1",false);
        instance.setRouteId(routeId);
        assertEquals(routeId,instance.getRouteId());
    }

    /**
     * Test of getEventId method, of class RouteEventDBModel.
     */
    @Test
    public void testGetEventId() {
        RouteEventDBModel instance = new RouteEventDBModel(1,"1",false);
        String expResult = "1";
        String result = instance.getEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventIdAttribute method, of class RouteEventDBModel.
     */
    @Test
    public void testGetEventIdAttribute() {
        Attribute expResult = new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true);
        Attribute result = RouteEventDBModel.getEventIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventId method, of class RouteEventDBModel.
     */
    @Test
    public void testSetEventId() {
        String eventId = "1";
        RouteEventDBModel instance = new RouteEventDBModel();
        instance.setEventId(eventId);
        assertEquals(eventId,instance.getEventId());
    }

    /**
     * Test of getDeleted method, of class RouteEventDBModel.
     */
    @Test
    public void testGetDeleted() {
        RouteEventDBModel instance = new RouteEventDBModel(1,"1",false);
        Boolean expResult = false;
        Boolean result = instance.getDeleted();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDeletedAttribute method, of class RouteEventDBModel.
     */
    @Test
    public void testGetDeletedAttribute() {
        Attribute expResult = new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true);
        Attribute result = RouteEventDBModel.getDeletedAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDeleted method, of class RouteEventDBModel.
     */
    @Test
    public void testSetDeleted() {
        Boolean deleted = true;
        RouteEventDBModel instance = new RouteEventDBModel(1,"1",false);
        instance.setDeleted(deleted);
        assertEquals(deleted,instance.getDeleted());
    }
    
}

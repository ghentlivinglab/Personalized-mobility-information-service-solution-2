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
public class RouteEventtypeDBModelTest {
    
    private static final String tableName = "route_eventtype";
    
    public RouteEventtypeDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel(1,1);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel(1,1);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetTableName() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testSetId() {
        int id = 0;
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetId() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteEventtypeId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetRouteEventtypeId() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel(1,1);
        Integer expResult = null;
        Integer result = instance.getRouteEventtypeId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteEventtypeIdAttribute method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetRouteEventtypeIdAttribute() {
        Attribute expResult = new Attribute("routeEventtypeId", "route_eventtypeid", AttributeType.INTEGER, tableName, true);
        Attribute result = RouteEventtypeDBModel.getRouteEventtypeIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteEventtypeId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testSetRouteEventtypeId() {
        Integer routeEventtypeId = 1;
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        instance.setRouteEventtypeId(routeEventtypeId);
        assertEquals(routeEventtypeId,instance.getRouteEventtypeId());
    }

    /**
     * Test of getRouteId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetRouteId() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel(1,1);
        Integer expResult = 1;
        Integer result = instance.getRouteId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteIdAttribute method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetRouteIdAttribute() {
        ForeignKeyAttribute<RouteDBModel> expResult = new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class);
        ForeignKeyAttribute<RouteDBModel> result = RouteEventtypeDBModel.getRouteIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testSetRouteId() {
        Integer routeId = 1;
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        instance.setRouteId(routeId);
        assertEquals(routeId,instance.getRouteId());
    }

    /**
     * Test of getEventtypeId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetEventtypeId() {
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel(1,1);
        Integer expResult = 1;
        Integer result = instance.getEventtypeId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventtypeIdAttribute method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testGetEventtypeIdAttribute() {
        ForeignKeyAttribute<EventtypeDBModel> expResult =  new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class);
        ForeignKeyAttribute<EventtypeDBModel> result = RouteEventtypeDBModel.getEventtypeIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventtypeId method, of class RouteEventtypeDBModel.
     */
    @Test
    public void testSetEventtypeId() {
        Integer eventtypeId = 1;
        RouteEventtypeDBModel instance = new RouteEventtypeDBModel();
        instance.setEventtypeId(eventtypeId);
        assertEquals(eventtypeId,instance.getEventtypeId());
    }
    
}

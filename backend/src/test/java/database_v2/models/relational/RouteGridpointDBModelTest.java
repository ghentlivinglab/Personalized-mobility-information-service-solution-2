/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import static database_v2.models.relational.RouteGridpointDBModel.GRID_X_ATTRIBUTE;
import static database_v2.models.relational.RouteGridpointDBModel.GRID_Y_ATTRIBUTE;
import static database_v2.models.relational.RouteGridpointDBModel.ROUTE_ID_ATTRIBUTE;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class RouteGridpointDBModelTest {
    
     private static final String tableName = "route_gridpoint";
    
    public RouteGridpointDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(ROUTE_ID_ATTRIBUTE);
        expResult.add(GRID_X_ATTRIBUTE);
        expResult.add(GRID_Y_ATTRIBUTE);
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(ROUTE_ID_ATTRIBUTE);
        expResult.add(GRID_X_ATTRIBUTE);
        expResult.add(GRID_Y_ATTRIBUTE);
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetTableName() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class RouteGridpointDBModel.
     */
    @Test
    public void testSetId() {
        int id = 1;
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetId() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteGridpointId method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetRouteGridpointId() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        Integer expResult = null;
        Integer result = instance.getRouteGridpointId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteGridpointId method, of class RouteGridpointDBModel.
     */
    @Test
    public void testSetRouteGridpointId() {
        Integer routeGridpointId = 1;
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        instance.setRouteGridpointId(routeGridpointId);
        assertEquals(routeGridpointId,instance.getRouteGridpointId());
    }

    /**
     * Test of getRouteId method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetRouteId() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel(1, 100, 100);
        Integer expResult = 1;
        Integer result = instance.getRouteId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteId method, of class RouteGridpointDBModel.
     */
    @Test
    public void testSetRouteId() {
        Integer routeId = 1;
        RouteGridpointDBModel instance = new RouteGridpointDBModel(1,100,100);
        instance.setRouteId(routeId);
        assertEquals(routeId,instance.getRouteId());
    }

    /**
     * Test of getGridX method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetGridX() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel(1,100,100);
        Integer expResult = 100;
        Integer result = instance.getGridX();
        assertEquals(expResult, result);
    }

    /**
     * Test of setGridX method, of class RouteGridpointDBModel.
     */
    @Test
    public void testSetGridX() {
        Integer gridX = 10;
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        instance.setGridX(gridX);
        assertEquals(gridX,instance.getGridX());
    }

    /**
     * Test of getGridY method, of class RouteGridpointDBModel.
     */
    @Test
    public void testGetGridY() {
        RouteGridpointDBModel instance = new RouteGridpointDBModel(1,100,100);
        Integer expResult = 100;
        Integer result = instance.getGridY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setGridY method, of class RouteGridpointDBModel.
     */
    @Test
    public void testSetGridY() {
        Integer gridY = 10;
        RouteGridpointDBModel instance = new RouteGridpointDBModel();
        instance.setGridY(gridY);
        assertEquals(gridY,instance.getGridY());
    }
    
}

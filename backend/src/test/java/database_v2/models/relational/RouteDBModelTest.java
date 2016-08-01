/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.ForeignKeyAttribute;
import static database_v2.models.relational.RouteDBModel.fullWaypointsAttribute;
import static database_v2.models.relational.RouteDBModel.userWaypointsAttribute;
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
public class RouteDBModelTest {
    
    private static final String tableName = "route";
    
    public RouteDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class RouteDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("travelId", "travelid", AttributeType.INTEGER, tableName, true, TravelDBModel.class));
        expResult.add(new Attribute("transportationType", "transportation_type", AttributeType.TEXT, tableName, false));
        expResult.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class RouteDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        RouteDBModel instance = new RouteDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("travelId", "travelid", AttributeType.INTEGER, tableName, true, TravelDBModel.class));
        expResult.add(userWaypointsAttribute);
        expResult.add(fullWaypointsAttribute);
        expResult.add(new Attribute("transportationType", "transportation_type", AttributeType.TEXT, tableName, false));
        expResult.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class RouteDBModel.
     */
    @Test
    public void testGetTableName() {
        System.out.println("getTableName");
        RouteDBModel instance = new RouteDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class RouteDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 1;
        RouteDBModel instance = new RouteDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class RouteDBModel.
     */
    @Test
    public void testGetId() {
        RouteDBModel instance = new RouteDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class RouteDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        RouteDBModel instance = new RouteDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteId method, of class RouteDBModel.
     */
    @Test
    public void testGetRouteId() {
        RouteDBModel instance = new RouteDBModel(1);
        instance.setRouteId(1);
        Integer expResult = 1;
        Integer result = instance.getRouteId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteIdAttribute method, of class RouteDBModel.
     */
    @Test
    public void testGetRouteIdAttribute() {
        Attribute expResult = new Attribute("routeId", "routeid", AttributeType.INTEGER, tableName, false);
        Attribute result = RouteDBModel.getRouteIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRouteId method, of class RouteDBModel.
     */
    @Test
    public void testSetRouteId() {
        Integer routeId = 1;
        RouteDBModel instance = new RouteDBModel(1);
        instance.setRouteId(routeId);
        assertEquals(routeId,instance.getRouteId());
    }

    /**
     * Test of getTravelId method, of class RouteDBModel.
     */
    @Test
    public void testGetTravelId() {
        RouteDBModel instance = new RouteDBModel(1);
        Integer expResult = 1;
        Integer result = instance.getTravelId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTravelIdAttribute method, of class RouteDBModel.
     */
    @Test
    public void testGetTravelIdAttribute() {
        ForeignKeyAttribute<TravelDBModel> expResult = new ForeignKeyAttribute<>("travelId", "travelid", 
                AttributeType.INTEGER, tableName, true, TravelDBModel.class);
        ForeignKeyAttribute<TravelDBModel> result = RouteDBModel.getTravelIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTravelId method, of class RouteDBModel.
     */
    @Test
    public void testSetTravelId() {
        Integer travelId = 1;
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        List<Attribute> expResult = new ArrayList<>();
        instance.setTravelId(travelId);
    }

    /**
     * Test of getUserWaypoints method, of class RouteDBModel.
     */
    @Test
    public void testGetUserWaypoints() {
        RouteDBModel instance = new RouteDBModel();
        Array expResult = null;
        Array result = instance.getUserWaypoints();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUserWaypoints method, of class RouteDBModel.
     */
    @Test
    public void testSetUserWaypoints() {
        Array userWaypoints = null;
        RouteDBModel instance = new RouteDBModel();
        instance.setUserWaypoints(userWaypoints);
    }

    /**
     * Test of getFullWaypoints method, of class RouteDBModel.
     */
    @Test
    public void testGetFullWaypoints() {
        RouteDBModel instance = new RouteDBModel();
        Array expResult = null;
        Array result = instance.getFullWaypoints();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFullWaypoints method, of class RouteDBModel.
     */
    @Test
    public void testSetFullWaypoints() {
        Array fullWaypoints = null;
        RouteDBModel instance = new RouteDBModel();
        instance.setFullWaypoints(fullWaypoints);
    }

    /**
     * Test of getTransportationType method, of class RouteDBModel.
     */
    @Test
    public void testGetTransportationType() {
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        String expResult = "car";
        String result = instance.getTransportationType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransportationTypeAttribute method, of class RouteDBModel.
     */
    @Test
    public void testGetTransportationTypeAttribute() {
        Attribute expResult = new Attribute("transportationType", "transportation_type", 
                AttributeType.TEXT, tableName, false);
        Attribute result = RouteDBModel.getTransportationTypeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTransportationType method, of class RouteDBModel.
     */
    @Test
    public void testSetTransportationType() {
        String transportationType = "";
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        instance.setTransportationType(transportationType);
        assertEquals(transportationType,instance.getTransportationType());
    }

    /**
     * Test of getActive method, of class RouteDBModel.
     */
    @Test
    public void testGetActive() {
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of getActiveAttribute method, of class RouteDBModel.
     */
    @Test
    public void testGetActiveAttribute() {
        Attribute expResult = new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false);
        Attribute result = RouteDBModel.getActiveAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActive method, of class RouteDBModel.
     */
    @Test
    public void testSetActive() {
        Boolean active = false;
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        instance.setActive(active);
        assertEquals(active, instance.getActive());
    }

    /**
     * Test of getNotifyEmail method, of class RouteDBModel.
     */
    @Test
    public void testGetNotifyEmail() {
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getNotifyEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNotifyEmailAttribute method, of class RouteDBModel.
     */
    @Test
    public void testGetNotifyEmailAttribute() {
        Attribute expResult = new Attribute("notifyEmail", "notify_email", 
                AttributeType.BOOLEAN, tableName, false);
        Attribute result = RouteDBModel.getNotifyEmailAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNotifyEmail method, of class RouteDBModel.
     */
    @Test
    public void testSetNotifyEmail() {
        Boolean notifyEmail = false;
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        instance.setNotifyEmail(notifyEmail);
        assertEquals(notifyEmail, instance.getNotifyEmail());
    }

    /**
     * Test of getNotifyCell method, of class RouteDBModel.
     */
    @Test
    public void testGetNotifyCell() {
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getNotifyCell();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNotifyCellAttribute method, of class RouteDBModel.
     */
    @Test
    public void testGetNotifyCellAttribute() {
        Attribute expResult = new Attribute("notifyCell", "notify_cell",
                AttributeType.BOOLEAN, tableName, false);
        Attribute result = RouteDBModel.getNotifyCellAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNotifyCell method, of class RouteDBModel.
     */
    @Test
    public void testSetNotifyCell() {
        Boolean notifyCell = false;
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        instance.setNotifyCell(notifyCell);
        assertEquals(notifyCell,instance.getNotifyCell());
    }

    /**
     * Test of equals method, of class RouteDBModel.
     */
    @Test
    public void testEquals() {
        Object obj = null;
        RouteDBModel instance = new RouteDBModel(1, null, null,
                "car", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class RouteDBModel.
     */
    @Test
    public void testHashCode() {
        RouteDBModel instance = new RouteDBModel();
        int expResult = -1953075650;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

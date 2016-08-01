/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.ForeignKeyAttribute;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class TravelDBModelTest {
    
    private static final String tableName = "travel";
    
    public TravelDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class TravelDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("startpoint", "startpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("endpoint", "endpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        expResult.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("beginTime", "begin_time", AttributeType.TIME, tableName, true));
        expResult.add(new Attribute("endTime", "end_time", AttributeType.TIME, tableName, true));
        expResult.add(new Attribute("isArrivalTime", "is_arrival_time", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("mon", "mon", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("tue", "tue", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("wed", "wed", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("thu", "thu", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("fri", "fri", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("sat", "sat", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("sun", "sun", AttributeType.BOOLEAN, tableName, false));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class TravelDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        TravelDBModel instance = new TravelDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("startpoint", "startpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        expResult.add(new ForeignKeyAttribute<>("endpoint", "endpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        expResult.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("beginTime", "begin_time", AttributeType.TIME, tableName, true));
        expResult.add(new Attribute("endTime", "end_time", AttributeType.TIME, tableName, true));
        expResult.add(new Attribute("isArrivalTime", "is_arrival_time", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("mon", "mon", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("tue", "tue", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("wed", "wed", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("thu", "thu", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("fri", "fri", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("sat", "sat", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("sun", "sun", AttributeType.BOOLEAN, tableName, false));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class TravelDBModel.
     */
    @Test
    public void testGetTableName() {
        TravelDBModel instance = new TravelDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class TravelDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 1;
        TravelDBModel instance = new TravelDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class TravelDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        TravelDBModel instance = new TravelDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class TravelDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        TravelDBModel instance = new TravelDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWeekArray method, of class TravelDBModel.
     */
    @Test
    public void testGetWeekArray() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        boolean[] expResult = new boolean[]{true, true, true, true, true, true, true};
        boolean[] result = instance.getWeekArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getTravelId method, of class TravelDBModel.
     */
    @Test
    public void testGetTravelId() {
        TravelDBModel instance = new TravelDBModel();
        Integer expResult = null;
        Integer result = instance.getTravelId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTravelIdAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetTravelIdAttribute() {
        Attribute expResult = new Attribute("travelId", "travelid", AttributeType.INTEGER, tableName, true);
        Attribute result = TravelDBModel.getTravelIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTravelId method, of class TravelDBModel.
     */
    @Test
    public void testSetTravelId() {
        Integer travelId = 1;
        TravelDBModel instance = new TravelDBModel();
        instance.setTravelId(travelId);
        assertEquals(travelId, instance.getTravelId());
    }

    /**
     * Test of getAccountId method, of class TravelDBModel.
     */
    @Test
    public void testGetAccountId() {
        TravelDBModel instance  = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Integer expResult = 1;
        Integer result = instance.getAccountId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAccountIdAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetAccountIdAttribute() {
        ForeignKeyAttribute<AccountDBModel> expResult =new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class);
        ForeignKeyAttribute<AccountDBModel> result = TravelDBModel.getAccountIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAccountId method, of class TravelDBModel.
     */
    @Test
    public void testSetAccountId() {
        Integer accountId = 2;
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        instance.setAccountId(accountId);
        assertEquals(accountId, instance.getAccountId());
    }

    /**
     * Test of getStartpoint method, of class TravelDBModel.
     */
    @Test
    public void testGetStartpoint() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000));
        Integer expResult = 1;
        Integer result = instance.getStartpoint();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartpointAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetStartpointAttribute() {
        ForeignKeyAttribute<AddressDBModel> expResult = new ForeignKeyAttribute<>("startpoint", 
                "startpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class);
        ForeignKeyAttribute<AddressDBModel> result = TravelDBModel.getStartpointAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStartpoint method, of class TravelDBModel.
     */
    @Test
    public void testSetStartpoint() {
        Integer startpoint = 3;
        TravelDBModel instance = new TravelDBModel();
        instance.setStartpoint(startpoint);
        assertEquals(startpoint, instance.getStartpoint());
    }

    /**
     * Test of getEndpoint method, of class TravelDBModel.
     */
    @Test
    public void testGetEndpoint() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Integer expResult = 2;
        Integer result = instance.getEndpoint();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndpointAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetEndpointAttribute() {
        ForeignKeyAttribute<AddressDBModel> expResult = new ForeignKeyAttribute<>("endpoint", "endpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class);
        ForeignKeyAttribute<AddressDBModel> result = TravelDBModel.getEndpointAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEndpoint method, of class TravelDBModel.
     */
    @Test
    public void testSetEndpoint() {
        Integer endpoint = 3;
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        instance.setEndpoint(endpoint);
        assertEquals(endpoint,instance.getEndpoint());
    }

    /**
     * Test of getName method, of class TravelDBModel.
     */
    @Test
    public void testGetName() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        String expResult = "name";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class TravelDBModel.
     */
    @Test
    public void testSetName() {
        String name = "name";
        TravelDBModel instance = new TravelDBModel();
        instance.setName(name);
        assertEquals(name,instance.getName());
    }

    /**
     * Test of getBeginTime method, of class TravelDBModel.
     */
    @Test
    public void testGetBeginTime() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Time expResult = new Time(1000);
        Time result = instance.getBeginTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBeginTimeAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetBeginTimeAttribute() {
        Attribute expResult = new Attribute("beginTime", "begin_time", AttributeType.TIME, tableName, true);
        Attribute result = TravelDBModel.getBeginTimeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setBeginTime method, of class TravelDBModel.
     */
    @Test
    public void testSetBeginTime() {
        Time beginTime = new Time(1000000);
        TravelDBModel instance = new TravelDBModel();
        instance.setBeginTime(beginTime);
        assertEquals(beginTime, instance.getBeginTime());
    }

    /**
     * Test of getEndTime method, of class TravelDBModel.
     */
    @Test
    public void testGetEndTime() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Time expResult = new Time(1000);
        Time result = instance.getEndTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndTimeAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetEndTimeAttribute() {
        Attribute expResult = new Attribute("endTime", "end_time", AttributeType.TIME, tableName, true);
        Attribute result = TravelDBModel.getEndTimeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEndTime method, of class TravelDBModel.
     */
    @Test
    public void testSetEndTime() {
        Time endTime = new Time(1000000);
        TravelDBModel instance = new TravelDBModel();
        instance.setEndTime(endTime);
        assertEquals(endTime,instance.getEndTime());
    }

    /**
     * Test of getIsArrivalTime method, of class TravelDBModel.
     */
    @Test
    public void testGetIsArrivalTime() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                true, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getIsArrivalTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIsArrivalTimeAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetIsArrivalTimeAttribute() {
        Attribute expResult = new Attribute("isArrivalTime", "is_arrival_time", 
                AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getIsArrivalTimeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsArrivalTime method, of class TravelDBModel.
     */
    @Test
    public void testSetIsArrivalTime() {
        Boolean isArrivalTime = false;
        TravelDBModel instance = new TravelDBModel();
        instance.setIsArrivalTime(isArrivalTime);
        assertEquals(instance.getIsArrivalTime(),isArrivalTime);
    }

    /**
     * Test of getMon method, of class TravelDBModel.
     */
    @Test
    public void testGetMon() {
        System.out.println("getMon");
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getMon();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMonAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetMonAttribute() {
        Attribute expResult = new Attribute("mon", "mon", AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getMonAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMon method, of class TravelDBModel.
     */
    @Test
    public void testSetMon() {
        Boolean mon = false;
        TravelDBModel instance = new TravelDBModel();
        instance.setMon(mon);
        assertEquals(instance.getMon(),mon);
    }

    /**
     * Test of getTue method, of class TravelDBModel.
     */
    @Test
    public void testGetTue() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getTue();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTueAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetTueAttribute() {
        Attribute expResult = new Attribute("tue", "tue", AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getTueAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTue method, of class TravelDBModel.
     */
    @Test
    public void testSetTue() {
        Boolean tue = false;
        TravelDBModel instance = new TravelDBModel();
        instance.setTue(tue);
        assertEquals(tue, instance.getTue());
    }

    /**
     * Test of getWed method, of class TravelDBModel.
     */
    @Test
    public void testGetWed() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getWed();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWedAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetWedAttribute() {
        Attribute expResult = new Attribute("wed", "wed", AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getWedAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setWed method, of class TravelDBModel.
     */
    @Test
    public void testSetWed() {
        Boolean wed = true;
        TravelDBModel instance = new TravelDBModel();
        instance.setWed(wed);
        assertEquals(wed, instance.getWed());
    }

    /**
     * Test of getThu method, of class TravelDBModel.
     */
    @Test
    public void testGetThu() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getThu();
        assertEquals(expResult, result);
    }

    /**
     * Test of getThuAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetThuAttribute() {
        Attribute expResult = new Attribute("thu", "thu", AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getThuAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setThu method, of class TravelDBModel.
     */
    @Test
    public void testSetThu() {
        Boolean thu = false;
        TravelDBModel instance = new TravelDBModel();
        instance.setThu(thu);
        assertEquals(thu, instance.getThu());
    }

    /**
     * Test of getFri method, of class TravelDBModel.
     */
    @Test
    public void testGetFri() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getFri();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFriAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetFriAttribute() {
        Attribute expResult = new Attribute("fri", "fri", AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getFriAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFri method, of class TravelDBModel.
     */
    @Test
    public void testSetFri() {
        Boolean fri = true;
        TravelDBModel instance = new TravelDBModel();
        instance.setFri(fri);
        assertEquals(fri,instance.getFri());
    }

    /**
     * Test of getSat method, of class TravelDBModel.
     */
    @Test
    public void testGetSat() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getSat();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSatAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetSatAttribute() {
        Attribute expResult = new Attribute("sat", "sat", AttributeType.BOOLEAN, tableName, false);;
        Attribute result = TravelDBModel.getSatAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSat method, of class TravelDBModel.
     */
    @Test
    public void testSetSat() {
        Boolean sat = true;
        TravelDBModel instance = new TravelDBModel();
        instance.setSat(sat);
        assertEquals(sat,instance.getSat());
    }

    /**
     * Test of getSun method, of class TravelDBModel.
     */
    @Test
    public void testGetSun() {
        TravelDBModel instance = new TravelDBModel(1, 1, 2, "name", new Time(1000),new Time(1000), 
                Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getSun();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSunAttribute method, of class TravelDBModel.
     */
    @Test
    public void testGetSunAttribute() {
        Attribute expResult = new Attribute("sun", "sun", AttributeType.BOOLEAN, tableName, false);
        Attribute result = TravelDBModel.getSunAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSun method, of class TravelDBModel.
     */
    @Test
    public void testSetSun() {
        Boolean sun = false;
        TravelDBModel instance = new TravelDBModel();
        instance.setSun(sun);
        assertEquals(sun,instance.getSun());
    }
    
}

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
public class LocationDBModelTest {
    
    private static final String tableName = "location";
    
    public LocationDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class LocationDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        expResult.add(new ForeignKeyAttribute("addressId", "addressid", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        expResult.add(new Attribute("name", "name", AttributeType.TEXT, tableName, false));
        expResult.add(new Attribute("radius", "radius", AttributeType.INTEGER, tableName, false));
        expResult.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class LocationDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        LocationDBModel instance = new LocationDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        expResult.add(new ForeignKeyAttribute("addressId", "addressid", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        expResult.add(new Attribute("name", "name", AttributeType.TEXT, tableName, false));
        expResult.add(new Attribute("radius", "radius", AttributeType.INTEGER, tableName, false));
        expResult.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        expResult.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class LocationDBModel.
     */
    @Test
    public void testGetTableName() {
        LocationDBModel instance = new LocationDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class LocationDBModel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 1;
        LocationDBModel instance = new LocationDBModel();
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getId method, of class LocationDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        LocationDBModel instance = new LocationDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class LocationDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        LocationDBModel instance = new LocationDBModel();
        String expResult = "locationid";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationId method, of class LocationDBModel.
     */
    @Test
    public void testGetLocationId() {
        LocationDBModel instance = new LocationDBModel();
        Integer expResult = null;
        Integer result = instance.getLocationId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocationIdAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetLocationIdAttribute() {
        Attribute expResult = new Attribute("locationId", "locationid", AttributeType.INTEGER, tableName, false);
        Attribute result = LocationDBModel.getLocationIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocationId method, of class LocationDBModel.
     */
    @Test
    public void testSetLocationId() {
        Integer locationId = 2;
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        instance.setLocationId(locationId);
        assertEquals(locationId, instance.getLocationId());
    }

    /**
     * Test of getAccountId method, of class LocationDBModel.
     */
    @Test
    public void testGetAccountId() {
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        Integer expResult = 1;
        Integer result = instance.getAccountId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAccountIdAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetAccountIdAttribute() {
        ForeignKeyAttribute<AccountDBModel> expResult = new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER,
                tableName, true, AccountDBModel.class);
        ForeignKeyAttribute<AccountDBModel> result = LocationDBModel.getAccountIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAccountId method, of class LocationDBModel.
     */
    @Test
    public void testSetAccountId() {
        System.out.println("setAccountId");
        Integer accountId = 2;
        LocationDBModel instance = new LocationDBModel();
        instance.setAccountId(accountId);
        assertEquals(accountId, instance.getAccountId());
    }

    /**
     * Test of getAddressId method, of class LocationDBModel.
     */
    @Test
    public void testGetAddressId() {
        System.out.println("getAddressId");
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        Integer expResult = 1;
        Integer result = instance.getAddressId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAddressIdAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetAddressIdAttribute() {
        ForeignKeyAttribute<AddressDBModel> expResult = new ForeignKeyAttribute<>("addressId", "addressid", 
                AttributeType.INTEGER, tableName, true, AddressDBModel.class);
        ForeignKeyAttribute<AddressDBModel> result = LocationDBModel.getAddressIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAddressId method, of class LocationDBModel.
     */
    @Test
    public void testSetAddressId() {
        Integer addressId = 2;
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        instance.setAddressId(addressId);
        assertEquals(addressId,instance.getAddressId());
    }

    /**
     * Test of getName method, of class LocationDBModel.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        String expResult = "name";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNameAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetNameAttribute() {
        Attribute expResult = new Attribute("name", "name", AttributeType.TEXT, tableName, false);
        Attribute result = LocationDBModel.getNameAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class LocationDBModel.
     */
    @Test
    public void testSetName() {
        String name = "naam";
        LocationDBModel instance = new LocationDBModel();
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    /**
     * Test of getRadius method, of class LocationDBModel.
     */
    @Test
    public void testGetRadius() {
        System.out.println("getRadius");
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        Integer expResult = 1;
        Integer result = instance.getRadius();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRadiusAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetRadiusAttribute() {
        Attribute expResult = new Attribute("radius", "radius", AttributeType.INTEGER, tableName, false);
        Attribute result = LocationDBModel.getRadiusAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRadius method, of class LocationDBModel.
     */
    @Test
    public void testSetRadius() {
        Integer radius = 1;
        LocationDBModel instance = new LocationDBModel();
        instance.setRadius(radius);
        assertEquals(radius,instance.getRadius());
    }

    /**
     * Test of getActive method, of class LocationDBModel.
     */
    @Test
    public void testGetActive() {
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of getActiveAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetActiveAttribute() {
        Attribute expResult = new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false);
        Attribute result = LocationDBModel.getActiveAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActive method, of class LocationDBModel.
     */
    @Test
    public void testSetActive() {
        Boolean active = false;
        LocationDBModel instance =new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);;
        instance.setActive(active);
        assertEquals(active,instance.getActive());
    }

    /**
     * Test of getNotifyEmail method, of class LocationDBModel.
     */
    @Test
    public void testGetNotifyEmail() {
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getNotifyEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNotifyEmailAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetNotifyEmailAttribute() {
        Attribute expResult = new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false);
        Attribute result = LocationDBModel.getNotifyEmailAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNotifyEmail method, of class LocationDBModel.
     */
    @Test
    public void testSetNotifyEmail() {
        Boolean notifyEmail = true;
        LocationDBModel instance = new LocationDBModel();
        instance.setNotifyEmail(notifyEmail);
        assertEquals(notifyEmail,instance.getNotifyEmail());
    }

    /**
     * Test of getNotifyCell method, of class LocationDBModel.
     */
    @Test
    public void testGetNotifyCell() {
        LocationDBModel instance = new LocationDBModel(1, 1, "name",1, Boolean.TRUE, 
                Boolean.TRUE, Boolean.TRUE);
        Boolean expResult = true;
        Boolean result = instance.getNotifyCell();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNotifyCellAttribute method, of class LocationDBModel.
     */
    @Test
    public void testGetNotifyCellAttribute() {
        Attribute expResult = new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false);
        Attribute result = LocationDBModel.getNotifyCellAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNotifyCell method, of class LocationDBModel.
     */
    @Test
    public void testSetNotifyCell() {
        Boolean notifyCell = true;
        LocationDBModel instance = new LocationDBModel();
        instance.setNotifyCell(notifyCell);
        assertEquals(notifyCell,instance.getNotifyCell());
    }

    /**
     * Test of equals method, of class LocationDBModel.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        LocationDBModel instance = new LocationDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class LocationDBModel.
     */
    @Test
    public void testEquals_1() {
        System.out.println("equals");
        Object obj = "String";
        LocationDBModel instance = new LocationDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class LocationDBModel.
     */
    @Test
    public void testEquals_2() {
        System.out.println("equals");
        Object obj = new LocationDBModel(1, 1);
        LocationDBModel instance = new LocationDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class LocationDBModel.
     */
    @Test
    public void testEquals_3() {
        System.out.println("equals");
        LocationDBModel instance = new LocationDBModel();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class LocationDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        LocationDBModel instance = new LocationDBModel();
        int expResult = 1344044545;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

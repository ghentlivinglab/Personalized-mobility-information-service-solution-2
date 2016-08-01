/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.ForeignKeyAttribute;
import static database_v2.models.relational.AddressDBModel.CARTESIAN_X_ATTRIBUTE;
import static database_v2.models.relational.AddressDBModel.CARTESIAN_Y_ATTRIBUTE;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class AddressDBModelTest {
    
    private static final String tableName = "address";
    
    public AddressDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class AddressDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("streetId", "streetid", AttributeType.INTEGER, tableName, true, StreetDBModel.class));
        expResult.add(new Attribute("housenumber", "housenumber", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("latitude", "latitude", AttributeType.DOUBLE, tableName, false));
        expResult.add(new Attribute("longitude", "longitude", AttributeType.DOUBLE, tableName, false));
        expResult.add(CARTESIAN_X_ATTRIBUTE);
        expResult.add(CARTESIAN_Y_ATTRIBUTE);
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class AddressDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("streetId", "streetid", AttributeType.INTEGER, tableName, true, StreetDBModel.class));
        expResult.add(new Attribute("housenumber", "housenumber", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("latitude", "latitude", AttributeType.DOUBLE, tableName, false));
        expResult.add(new Attribute("longitude", "longitude", AttributeType.DOUBLE, tableName, false));
        expResult.add(CARTESIAN_X_ATTRIBUTE);
        expResult.add(CARTESIAN_Y_ATTRIBUTE);
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class AddressDBModel.
     */
    @Test
    public void testGetTableName() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class AddressDBModel.
     */
    @Test
    public void testSetId() {
        int id = 1;
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class AddressDBModel.
     */
    @Test
    public void testGetId() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class AddressDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        System.out.println("getIdColumnName");
        AddressDBModel instance = new AddressDBModel(1,"22");
        String expResult = "addressid";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAddressId method, of class AddressDBModel.
     */
    @Test
    public void testGetAddressId() {
        AddressDBModel instance = new AddressDBModel();
        instance.setAddressId(1);
        Integer expResult = 1;
        Integer result = instance.getAddressId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAddressIdAttribute method, of class AddressDBModel.
     */
    @Test
    public void testGetAddressIdAttribute() {
        Attribute expResult = new Attribute("addressId", "addressid", AttributeType.INTEGER, tableName, false);
        Attribute result = AddressDBModel.getAddressIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAddressId method, of class AddressDBModel.
     */
    @Test
    public void testSetAddressId() {
        Integer addressId = 1;
        AddressDBModel instance = new AddressDBModel();
        instance.setAddressId(addressId);
        assertEquals(addressId,instance.getAddressId());
    }

    /**
     * Test of getStreetId method, of class AddressDBModel.
     */
    @Test
    public void testGetStreetId() {
        AddressDBModel instance = new AddressDBModel(1,"22");
        Integer expResult = 1;
        Integer result = instance.getStreetId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStreetIdAttribute method, of class AddressDBModel.
     */
    @Test
    public void testGetStreetIdAttribute() {
        ForeignKeyAttribute<StreetDBModel> expResult = new ForeignKeyAttribute("streetId", "streetid", 
                AttributeType.INTEGER, tableName, true, StreetDBModel.class);
        ForeignKeyAttribute<StreetDBModel> result = AddressDBModel.getStreetIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStreetId method, of class AddressDBModel.
     */
    @Test
    public void testSetStreetId() {
        System.out.println("setStreetId");
        Integer streetId = 2;
        AddressDBModel instance = new AddressDBModel(1,"22");
        instance.setStreetId(streetId);
        assertEquals(streetId, instance.getStreetId());
    }

    /**
     * Test of getHousenumber method, of class AddressDBModel.
     */
    @Test
    public void testGetHousenumber() {
        System.out.println("getHousenumber");
        AddressDBModel instance = new AddressDBModel(1,"22");
        String expResult = "22";
        String result = instance.getHousenumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHousenumberAttribute method, of class AddressDBModel.
     */
    @Test
    public void testGetHousenumberAttribute() {
        Attribute expResult = new Attribute("housenumber", "housenumber", AttributeType.TEXT, tableName, true);
        Attribute result = AddressDBModel.getHousenumberAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHousenumber method, of class AddressDBModel.
     */
    @Test
    public void testSetHousenumber() {
        System.out.println("setHousenumber");
        String housenumber = "2";
        AddressDBModel instance = new AddressDBModel(1,"22");
        instance.setHousenumber(housenumber);
        assertEquals(housenumber,instance.getHousenumber());
    }

    /**
     * Test of getLatitude method, of class AddressDBModel.
     */
    @Test
    public void testGetLatitude() {
        System.out.println("getLatitude");
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        Double expResult = 50.0;
        Double result = instance.getLatitude();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLatitudeAttribute method, of class AddressDBModel.
     */
    @Test
    public void testGetLatitudeAttribute() {
        Attribute expResult = new Attribute("latitude", "latitude", AttributeType.DOUBLE, tableName, false);
        Attribute result = AddressDBModel.getLatitudeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLatitude method, of class AddressDBModel.
     */
    @Test
    public void testSetLatitude() {
        Double latitude = 60.0;
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        instance.setLatitude(latitude);
        assertEquals(latitude,instance.getLatitude());
    }

    /**
     * Test of getLongitude method, of class AddressDBModel.
     */
    @Test
    public void testGetLongitude() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        Double expResult = 50.0;
        Double result = instance.getLongitude();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLongitudeAttribute method, of class AddressDBModel.
     */
    @Test
    public void testGetLongitudeAttribute() {
        Attribute expResult = new Attribute("longitude", "longitude", AttributeType.DOUBLE, tableName, false);
        Attribute result = AddressDBModel.getLongitudeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLongitude method, of class AddressDBModel.
     */
    @Test
    public void testSetLongitude() {
        Double longitude = 3.0;
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        instance.setLongitude(longitude);
        assertEquals(longitude,instance.getLongitude());
    }

    /**
     * Test of getCartesianX method, of class AddressDBModel.
     */
    @Test
    public void testGetCartesianX() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        Double expResult = 123.0;
        Double result = instance.getCartesianX();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCartesianX method, of class AddressDBModel.
     */
    @Test
    public void testSetCartesianX() {
        Double cartesianX = 1234.0;
        AddressDBModel instance = new AddressDBModel();
        instance.setCartesianX(cartesianX);
        assertEquals(cartesianX, instance.getCartesianX());
    }

    /**
     * Test of getCartesianY method, of class AddressDBModel.
     */
    @Test
    public void testGetCartesianY() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        Double expResult = 234.0;
        Double result = instance.getCartesianY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCartesianY method, of class AddressDBModel.
     */
    @Test
    public void testSetCartesianY() {
        Double cartesianY = 1234.0;
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        instance.setCartesianY(cartesianY);
        assertEquals(cartesianY, instance.getCartesianY());
    }

    /**
     * Test of equals method, of class AddressDBModel.
     */
    @Test
    public void testEquals() {
        Object obj = null;
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class AddressDBModel.
     */
    @Test
    public void testEquals_1() {
        Object obj = new AddressDBModel(1,"22");
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class AddressDBModel.
     */
    @Test
    public void testEquals_3() {
        Object obj = "String";
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class AddressDBModel.
     */
    @Test
    public void testEquals_2() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class AddressDBModel.
     */
    @Test
    public void testHashCode() {
        AddressDBModel instance = new AddressDBModel(1, "22", 50.0, 50.0, 123.0, 234.0);
        int expResult = -1103400408;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

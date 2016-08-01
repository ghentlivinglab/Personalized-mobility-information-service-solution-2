/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class CityDBModelTest {
    
    private static final String tableName = "city";
    
    public CityDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class CityDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        CityDBModel instance = new CityDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new Attribute("city", "city", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("postalCode", "postal_code", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("country", "country", AttributeType.TEXT, tableName, true));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class CityDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        CityDBModel instance = new CityDBModel();
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new Attribute("city", "city", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("postalCode", "postal_code", AttributeType.TEXT, tableName, true));
        expResult.add(new Attribute("country", "country", AttributeType.TEXT, tableName, true));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class CityDBModel.
     */
    @Test
    public void testGetTableName() {
        CityDBModel instance = new CityDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class CityDBModel.
     */
    @Test
    public void testSetId() {
        int id = 1;
        CityDBModel instance = new CityDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class CityDBModel.
     */
    @Test
    public void testGetId() {
        CityDBModel instance = new CityDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class CityDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        CityDBModel instance = new CityDBModel();
        String expResult = "cityid";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCityId method, of class CityDBModel.
     */
    @Test
    public void testGetCityId() {
        CityDBModel instance = new CityDBModel();
        Integer expResult = null;
        Integer result = instance.getCityId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCityIdAttribute method, of class CityDBModel.
     */
    @Test
    public void testGetCityIdAttribute() {
        Attribute expResult = new Attribute("cityId", "cityid", AttributeType.INTEGER, tableName, false);
        Attribute result = CityDBModel.getCityIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCityId method, of class CityDBModel.
     */
    @Test
    public void testSetCityId() {
        Integer cityId = 1;
        CityDBModel instance = new CityDBModel();
        instance.setCityId(cityId);
        assertEquals(cityId,instance.getCityId());
    }

    /**
     * Test of getCity method, of class CityDBModel.
     */
    @Test
    public void testGetCity() {
        CityDBModel instance = new CityDBModel("city", "1010", "BE");
        String expResult = "city";
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCityAttribute method, of class CityDBModel.
     */
    @Test
    public void testGetCityAttribute() {
        Attribute expResult = new Attribute("city", "city", AttributeType.TEXT, tableName, true);
        Attribute result = CityDBModel.getCityAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCity method, of class CityDBModel.
     */
    @Test
    public void testSetCity() {
        String city = "gent";
        CityDBModel instance = new CityDBModel("city", "1010", "BE");
        instance.setCity(city);
        assertEquals(city, instance.getCity());
    }

    /**
     * Test of getPostalCode method, of class CityDBModel.
     */
    @Test
    public void testGetPostalCode() {
        CityDBModel instance = new CityDBModel("city", "1010", "BE");
        String expResult = "1010";
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPostalCodeAttribute method, of class CityDBModel.
     */
    @Test
    public void testGetPostalCodeAttribute() {
        Attribute expResult = new Attribute("postalCode", "postal_code", AttributeType.TEXT, tableName, true);
        Attribute result = CityDBModel.getPostalCodeAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPostalCode method, of class CityDBModel.
     */
    @Test
    public void testSetPostalCode() {
        String postalCode = "9000";
        CityDBModel instance = new CityDBModel();
        instance.setPostalCode(postalCode);
        assertEquals(postalCode,instance.getPostalCode());
    }

    /**
     * Test of getCountry method, of class CityDBModel.
     */
    @Test
    public void testGetCountry() {
        System.out.println("getCountry");
        CityDBModel instance = new CityDBModel("city", "1010", "BE");
        String expResult = "BE";
        String result = instance.getCountry();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCountryAttribute method, of class CityDBModel.
     */
    @Test
    public void testGetCountryAttribute() {
        Attribute expResult = new Attribute("country", "country", AttributeType.TEXT, tableName, true);
        Attribute result = CityDBModel.getCountryAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCountry method, of class CityDBModel.
     */
    @Test
    public void testSetCountry() {
        String country = "NL";
        CityDBModel instance = new CityDBModel();
        instance.setCountry(country);
        assertEquals(country, instance.getCountry());
    }

    /**
     * Test of equals method, of class CityDBModel.
     */
    @Test
    public void testEquals() {
        Object obj = null;
        CityDBModel instance = new CityDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class CityDBModel.
     */
    @Test
    public void testEquals_1() {
        Object obj = "String";
        CityDBModel instance = new CityDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class CityDBModel.
     */
    @Test
    public void testEquals_2() {
        CityDBModel instance = new CityDBModel();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class CityDBModel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        CityDBModel instance = new CityDBModel();
        int expResult = 1254360265;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

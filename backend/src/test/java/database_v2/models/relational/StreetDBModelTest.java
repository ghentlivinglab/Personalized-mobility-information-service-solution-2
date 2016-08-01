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
public class StreetDBModelTest {
    
    private static final String tableName = "street";
    
    public StreetDBModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getActiveAttributeList method, of class StreetDBModel.
     */
    @Test
    public void testGetActiveAttributeList() {
        StreetDBModel instance = new StreetDBModel(1, "name");
        List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("cityId", "cityid", AttributeType.INTEGER, tableName, true, CityDBModel.class));
        expResult.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        List<Attribute> result = instance.getActiveAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAttributeList method, of class StreetDBModel.
     */
    @Test
    public void testGetAllAttributeList() {
        StreetDBModel instance = new StreetDBModel(1,"name");
         List<Attribute> expResult = new ArrayList<>();
        expResult.add(new ForeignKeyAttribute<>("cityId", "cityid", AttributeType.INTEGER, tableName, true, CityDBModel.class));
        expResult.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        List<Attribute> result = instance.getAllAttributeList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class StreetDBModel.
     */
    @Test
    public void testGetTableName() {
        System.out.println("getTableName");
        StreetDBModel instance = new StreetDBModel();
        String expResult = tableName;
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class StreetDBModel.
     */
    @Test
    public void testSetId() {
        int id = 1;
        StreetDBModel instance = new StreetDBModel();
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getId method, of class StreetDBModel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        StreetDBModel instance = new StreetDBModel();
        instance.setId(1);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdColumnName method, of class StreetDBModel.
     */
    @Test
    public void testGetIdColumnName() {
        StreetDBModel instance = new StreetDBModel();
        String expResult = tableName+"id";
        String result = instance.getIdColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStreetId method, of class StreetDBModel.
     */
    @Test
    public void testGetStreetId() {
        System.out.println("getStreetId");
        StreetDBModel instance = new StreetDBModel();
        Integer expResult = null;
        Integer result = instance.getStreetId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStreetIdAttribute method, of class StreetDBModel.
     */
    @Test
    public void testGetStreetIdAttribute() {
        System.out.println("getStreetIdAttribute");
        Attribute expResult = new Attribute("streetId", "streetid", AttributeType.INTEGER, tableName, true);
        Attribute result = StreetDBModel.getStreetIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStreetId method, of class StreetDBModel.
     */
    @Test
    public void testSetStreetId() {
        System.out.println("setStreetId");
        Integer streetId = 1;
        StreetDBModel instance = new StreetDBModel();
        instance.setStreetId(streetId);
        assertEquals(streetId,instance.getStreetId());
    }

    /**
     * Test of getCityId method, of class StreetDBModel.
     */
    @Test
    public void testGetCityId() {
        StreetDBModel instance = new StreetDBModel(1,"name");
        Integer expResult = 1;
        Integer result = instance.getCityId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCityIdAttribute method, of class StreetDBModel.
     */
    @Test
    public void testGetCityIdAttribute() {
        ForeignKeyAttribute<CityDBModel> expResult = new ForeignKeyAttribute<>("cityId", "cityid", AttributeType.INTEGER,
                tableName, true, CityDBModel.class);
        ForeignKeyAttribute<CityDBModel> result = StreetDBModel.getCityIdAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCityId method, of class StreetDBModel.
     */
    @Test
    public void testSetCityId() {
        Integer cityId = 1;
        StreetDBModel instance = new StreetDBModel();
        instance.setCityId(cityId);
        assertEquals(cityId,instance.getCityId());
    }

    /**
     * Test of getName method, of class StreetDBModel.
     */
    @Test
    public void testGetName() {
        StreetDBModel instance = new StreetDBModel(1,"name");
        String expResult = "name";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNameAttribute method, of class StreetDBModel.
     */
    @Test
    public void testGetNameAttribute() {
        Attribute expResult = new Attribute("name", "name", AttributeType.TEXT, tableName, true);
        Attribute result = StreetDBModel.getNameAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class StreetDBModel.
     */
    @Test
    public void testSetName() {
        String name = "naam";
        StreetDBModel instance = new StreetDBModel(1,"name");
        instance.setName(name);
        assertEquals(name,instance.getName());
    }

    /**
     * Test of equals method, of class StreetDBModel.
     */
    @Test
    public void testEquals() {
        Object obj = null;
        StreetDBModel instance = new StreetDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class StreetDBModel.
     */
    @Test
    public void testEquals_1() {
        Object obj = "String";
        StreetDBModel instance = new StreetDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class StreetDBModel.
     */
    @Test
    public void testEquals_2() {
        Object obj = new StreetDBModel(1,"");
        StreetDBModel instance = new StreetDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class StreetDBModel.
     */
    @Test
    public void testEquals_3() {
        Object obj = new StreetDBModel(1,"");
        StreetDBModel instance = new StreetDBModel();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class StreetDBModel.
     */
    @Test
    public void testEquals_4() {
        StreetDBModel instance = new StreetDBModel();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hashCode method, of class StreetDBModel.
     */
    @Test
    public void testHashCode() {
        StreetDBModel instance = new StreetDBModel();
        int expResult = -1713993427;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}

package database_v2.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class AttributeTest {
    
    public AttributeTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getAttribute method, of class Attribute.
     */
    @Test
    public void testGetAttribute() {
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        String expResult = "attribute";
        String result = instance.getAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAttribute method, of class Attribute.
     */
    @Test
    public void testSetAttribute() {
        String attribute = "attribute1";
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        instance.setAttribute(attribute);
        assertEquals(attribute, instance.getAttribute());
    }

    /**
     * Test of getColumnName method, of class Attribute.
     */
    @Test
    public void testGetColumnName() {
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        String expResult = "columnName";
        String result = instance.getColumnName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setColumnName method, of class Attribute.
     */
    @Test
    public void testSetColumnName() {
        String columnName = "kolomNaam";
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        instance.setColumnName(columnName);
        assertEquals(columnName,instance.getColumnName());
    }

    /**
     * Test of isRequired method, of class Attribute.
     */
    @Test
    public void testIsRequired() {
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = true;
        boolean result = instance.isRequired();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRequired method, of class Attribute.
     */
    @Test
    public void testSetRequired() {
        boolean required = false;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        instance.setRequired(required);
        assertEquals(required, instance.isRequired());
    }

    /**
     * Test of getAttributeType method, of class Attribute.
     */
    @Test
    public void testGetAttributeType() {
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        AttributeType expResult = AttributeType.INTEGER;
        AttributeType result = instance.getAttributeType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAttributeType method, of class Attribute.
     */
    @Test
    public void testSetAttributeType() {
        AttributeType attributeType = AttributeType.TEXT;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);;
        instance.setAttributeType(attributeType);
        assertEquals(attributeType,instance.getAttributeType());
    }

    /**
     * Test of getSetterName method, of class Attribute.
     */
    @Test
    public void testGetSetterName() {
        System.out.println("getSetterName");
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        String expResult = "setAttribute";
        String result = instance.getSetterName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGetterName method, of class Attribute.
     */
    @Test
    public void testGetGetterName() {
        System.out.println("getGetterName");
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        String expResult = "getAttribute";
        String result = instance.getGetterName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class Attribute.
     */
    @Test
    public void testGetTableName() {
        System.out.println("getTableName");
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        String expResult = "tableName";
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTableName method, of class Attribute.
     */
    @Test
    public void testSetTableName() {
        String tableName = "tabelNaam";
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        instance.setTableName(tableName);
        assertEquals(tableName,instance.getTableName());
    }
    
    @Test
    public void testEquals(){
        Object obj = null;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_1(){
        Object obj = "String";
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_2(){
        Object obj = "";
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_3(){
        Object obj =  new Attribute("attribu","columnName", AttributeType.INTEGER, "tableName", true);;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_4(){
        Object obj =  new Attribute("attribute","columnNa", AttributeType.INTEGER, "tableName", true);;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_5(){
        Object obj =  new Attribute("attribute","columnName", AttributeType.ARRAY, "tableName", true);;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_6(){
        Object obj =  new Attribute("attribute","columnName", AttributeType.INTEGER, "tableNam", true);;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_7(){
        Object obj =  new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", false);;
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = false; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_8(){
        Object obj =  new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        boolean expResult = true; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
    
    @Test
    public void testEquals_9(){
        Attribute instance = new Attribute("attribute","columnName", AttributeType.INTEGER, "tableName", true);
        Object obj = instance;
        boolean expResult = true; 
        boolean result = instance.equals(obj);
        assertEquals(expResult,result);
    }
}

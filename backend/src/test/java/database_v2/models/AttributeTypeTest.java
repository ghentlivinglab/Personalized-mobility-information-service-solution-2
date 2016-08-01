/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class AttributeTypeTest {
    
    public AttributeTypeTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of values method, of class AttributeType.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        AttributeType[] expResult = new AttributeType[]{AttributeType.TEXT,AttributeType.INTEGER,
            AttributeType.BOOLEAN,AttributeType.TIME,AttributeType.DATE,AttributeType.ARRAY,
            AttributeType.DOUBLE,AttributeType.BYTE};
        AttributeType[] result = AttributeType.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class AttributeType.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "DOUBLE";
        AttributeType expResult = AttributeType.DOUBLE;
        AttributeType result = AttributeType.valueOf(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class AttributeType.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        AttributeType instance = AttributeType.INTEGER;
        Class expResult = Integer.class;
        Class result = instance.getType();
        assertEquals(expResult, result);
    }
    
}

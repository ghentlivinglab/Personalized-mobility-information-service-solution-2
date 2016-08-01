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
public class ForeignKeyAttributeTest {
    
    public ForeignKeyAttributeTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getReferencedTable method, of class ForeignKeyAttribute.
     */
    @Test
    public void testGetReferencedTable() {
        System.out.println("getReferencedTable");
        ForeignKeyAttribute instance = new ForeignKeyAttribute("attribute", "columnName", AttributeType.INTEGER, "tableName", true, Integer.class);
        Class expResult = Integer.class;
        Class result = instance.getReferencedTable();
        assertEquals(expResult, result);
    }
    
}

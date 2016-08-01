/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models;

import database_v2.models.relational.AddressDBModel;
import database_v2.models.relational.CityDBModel;
import database_v2.models.relational.StreetDBModel;
import models.address.City;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author hannedesutter
 */
@RunWith(MockitoJUnitRunner.class)
public class TableJoinTest {
    
    public TableJoinTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testConstructor_1() throws InstantiationException, IllegalAccessException{
        //when(CityDBModel.class.newInstance()).thenThrow(new InstantiationException());
        ForeignKeyAttribute foreignKey = new ForeignKeyAttribute("city", "city", AttributeType.INTEGER, "tableName", true, City.class);
        TableJoin instance = new TableJoin(CityDBModel.class, StreetDBModel.class, foreignKey);
    }
    
    @Test
    public void testConstructor_2() throws InstantiationException, IllegalAccessException{
        ForeignKeyAttribute foreignKey = new ForeignKeyAttribute("city", "city", AttributeType.INTEGER, "tableName", true, City.class);
        TableJoin join = new TableJoin(StreetDBModel.class, AddressDBModel.class, foreignKey);
        TableJoin instance = new TableJoin(join, StreetDBModel.class, foreignKey);
    }
    
    /**
     * Test of getJoinStatementPart method, of class TableJoin.
     */
    @Test
    public void testGetJoinStatementPart() {
        System.out.println("getJoinStatementPart");
        ForeignKeyAttribute foreignKey = new ForeignKeyAttribute("city", "city", AttributeType.INTEGER, "tableName", true, CityDBModel.class);
        TableJoin instance = new TableJoin(CityDBModel.class, StreetDBModel.class, foreignKey);
        String expResult = "city JOIN street ON city.cityid=tableName.city";
        String result = instance.getJoinStatementPart();
        assertEquals(expResult, result);
    }
    
}

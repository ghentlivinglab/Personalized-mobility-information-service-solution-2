/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.CoordinateDTO;
import models.Coordinate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class CoordinateMapperTest {
    
    public CoordinateMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class CoordinateMapper.
     */
    @Test
    public void testConvertToDTO() {
        System.out.println("convertToDTO");
        Coordinate coordinate = new Coordinate(50,50);
        CoordinateMapper instance = new CoordinateMapper();
        CoordinateDTO expResult = new CoordinateDTO(50,50);
        CoordinateDTO result = instance.convertToDTO(coordinate);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class CoordinateMapper.
     */
    @Test
    public void testConvertFromDTO() {
        System.out.println("convertFromDTO");
        CoordinateDTO coordinatedto = new CoordinateDTO(50,50);
        CoordinateMapper instance = new CoordinateMapper();
        Coordinate expResult = new Coordinate(50,50);
        Coordinate result = instance.convertFromDTO(coordinatedto);
        assertEquals(expResult, result);
    }
    
}

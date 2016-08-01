/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.models;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class TravelDumpDTOTest {
    
    public TravelDumpDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getTravel method, of class TravelDumpDTO.
     */
    @Test
    public void testGetTravel() {
        System.out.println("getTravel");
        TravelDumpDTO instance = new TravelDumpDTO();
        TravelDTO expResult = null;
        TravelDTO result = instance.getTravel();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTravel method, of class TravelDumpDTO.
     */
    @Test
    public void testSetTravel() {
        System.out.println("setTravel");
        TravelDTO travel = new TravelDTO();
        TravelDumpDTO instance = new TravelDumpDTO();
        instance.setTravel(travel);
        assertEquals(travel, instance.getTravel());
    }

    /**
     * Test of getRoutes method, of class TravelDumpDTO.
     */
    @Test
    public void testGetRoutes() {
        System.out.println("getRoutes");
        TravelDumpDTO instance = new TravelDumpDTO();
        List<RouteDTO> expResult = null;
        List<RouteDTO> result = instance.getRoutes();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRoutes method, of class TravelDumpDTO.
     */
    @Test
    public void testSetRoutes() {
        System.out.println("setRoutes");
        List<RouteDTO> routes = new ArrayList<>();
        TravelDumpDTO instance = new TravelDumpDTO();
        instance.setRoutes(routes);
        assertEquals(routes, instance.getRoutes());
    }
    
}

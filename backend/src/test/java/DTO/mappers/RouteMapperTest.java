///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package DTO.mappers;
//
//import DTO.models.CoordinateDTO;
//import DTO.models.EventTypeDTO;
//import DTO.models.NotifyDTO;
//import DTO.models.RouteDTO;
//import database_v2.controlLayer.Database;
//import database_v2.controlLayer.impl.DatabaseImpl;
//import database_v2.exceptions.DataAccessException;
//import database_v2.exceptions.RecordNotFoundException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import models.MuteNotification;
//import models.Route;
//import models.Transportation;
//import models.event.EventType;
//import org.javatuples.Pair;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// *
// * @author hannedesutter
// */
//public class RouteMapperTest {
//    
//    
//    public RouteMapperTest() throws DataAccessException, RecordNotFoundException {
//    }
//
//    /**
//     * Test of convertToDTO method, of class RouteMapper.
//     */
//    @Test
//    public void testConvertToDTO_Route_int() {
//        System.out.println("convertToDTO");
//        Route route = new Route(new ArrayList<>(), Transportation.TRAIN, new HashMap<>(), new MuteNotification(false), new ArrayList<>());
//        route.addEventType(1,new EventType("Jam",new ArrayList<>()));
//        int routeId = 1;
//        RouteMapper instance = new RouteMapper();
//        RouteDTO expResult = new RouteDTO("1", new CoordinateDTO[]{}, "train", new EventTypeDTO[]{new EventTypeDTO("Jam")}, new NotifyDTO(), true);
//        RouteDTO result = instance.convertToDTO(route, routeId);
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of convertToDTO method, of class RouteMapper.
//     */
//    @Test
//    public void testConvertToDTO_Route_String() {
//        System.out.println("convertToDTO");
//        System.out.println("convertToDTO");
//        Route route = new Route(new ArrayList<>(), Transportation.TRAIN, new HashMap<>(), new MuteNotification(false), new ArrayList<>());
//        route.addEventType(1,new EventType("Jam",new ArrayList<>()));
//        String routeId = "1";
//        RouteMapper instance = new RouteMapper();
//        RouteDTO expResult = new RouteDTO("1", new CoordinateDTO[]{}, "train", new EventTypeDTO[]{new EventTypeDTO("Jam")}, new NotifyDTO(), true);
//        RouteDTO result = instance.convertToDTO(route, routeId);
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of convertFromDTO method, of class RouteMapper.
//     */
//    @Test
//    public void testConvertFromDTO() throws Exception {
//        System.out.println("convertFromDTO");
//        RouteDTO routedto = new RouteDTO("1", new CoordinateDTO[]{}, "train", new EventTypeDTO[]{new EventTypeDTO("Jam")}, new NotifyDTO(), true);
//        RouteMapper instance = new RouteMapper();
//        Route expResult= new Route(new ArrayList<>(), Transportation.TRAIN, new HashMap<>(), new MuteNotification(false), new ArrayList<>());
//        expResult.addEventType(1,new EventType("Jam",new ArrayList<>()));
//        Route result = instance.convertFromDTO(routedto);
//        assertEquals("The services aren't the same",expResult.getServices(), result.getServices());
//        assertEquals("The transTyp isn't the same",expResult.getTransportationType(), result.getTransportationType());
//        assertEquals("The waypoints aren't the same",expResult.getWayPoints(), result.getWayPoints());
//    }
//    
//}

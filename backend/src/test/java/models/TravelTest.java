/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.time.LocalTime;
import models.exceptions.InvalidPhoneNumberException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Route;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.event.EventType;
import models.exceptions.InvalidCountryCodeException;
import models.repetition.Repetition;
import models.repetition.RepetitionWeek;
import models.services.PhoneNumber;
import models.services.Service;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class TravelTest {
    
    public TravelTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_1(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel(null,LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_2(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",null,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_3(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,null,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_4(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,null,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_5(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,null,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_6(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,null,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_7(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,null,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_8(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,null,new HashMap<>(),new ArrayList<>());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_Exception_9(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),null,new ArrayList<>());
    }
    
    
    /**
     * Test of getName method, of class Travel.
     */
    @Test
    public void testGetName() throws Exception{
        System.out.println("getName");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        String expResult = "name";
        String result = travel.getName();
        assertEquals("The names did not match",expResult, result);
    }

    /**
     * Test of setName method, of class Travel.
     */
    @Test
    public void testSetName() throws Exception{
        System.out.println("setName");
        String name = "name1";
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.setName(name);
        assertEquals("The new name did not match.", name,travel.getName());
    }

    /**
     * Test of getRoutes method, of class Travel.
     */
    @Test
    public void testGetRoutes() throws Exception{
        System.out.println("getRoutes");
        Map<Integer,Route> newmap= new HashMap<>();
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        assertEquals(newmap,travel.getRoutes());
    }

    /**
     * Test of addRoute method, of class Travel.
     */
    @Test
    public void testAddRoute() throws InvalidCountryCodeException {
        System.out.println("addRoute");
        Integer id = 1;
        Route route = new Route(new ArrayList<>(), new ArrayList<>(), Transportation.BIKE, new HashMap<>(), false, new ArrayList<>());
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.addRoute(id, route);
        assertEquals("The route was not added.",1,travel.getRoutes().size());
    }

    /**
     * Test of removeRoute method, of class Travel.
     */
    @Test
    public void testRemoveRoute() throws Exception{
        System.out.println("removeRoute");
        Integer id = 1;
        Route route = new Route(new ArrayList<>(), new ArrayList<>(),Transportation.BIKE,new HashMap<>(),false,new ArrayList<>());        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.addRoute(id, route);
        travel.removeRoute(id);
        assertTrue("The route was not deleted.", travel.getRoutes().isEmpty());
    }

    /**
     * Test of getBeginDate method, of class Travel.
     */
    @Test
    public void testGetBeginDate() throws Exception{
        System.out.println("getBeginDate");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        LocalTime expResult = LocalTime.MIDNIGHT;
        LocalTime result = travel.getBeginDate();
        assertEquals("The dates were not the same.",expResult, result);
    }

    /**
     * Test of setBeginDate method, of class Travel.
     */
    @Test
    public void testSetBeginDate() throws InvalidCountryCodeException {
        System.out.println("setBeginDate");
        LocalTime time = LocalTime.NOON;
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.setBeginDate(time);
        assertEquals("The dates did not match.", time,travel.getBeginDate());
    }

    /**
     * Test of getEndDate method, of class Travel.
     */
    @Test
    public void testGetEndDate() throws Exception{
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        LocalTime expResult = LocalTime.NOON;
        LocalTime result = travel.getEndDate();
        assertEquals("The dates were not the same.",expResult, result);
    }

    /**
     * Test of setEndDate method, of class Travel.
     */
    @Test
    public void testSetEndDate() throws Exception {
        System.out.println("setEndDate");
        LocalTime end = LocalTime.MIDNIGHT;
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.setEndDate(end);
        assertEquals("The dates did not match.", end,travel.getEndDate());
    }

    /**
     * Test of isArrivalTime method, of class Travel.
     */
    @Test
    public void testIsArrivalTime() throws Exception {
        System.out.println("isArrivalTime");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Boolean expResult = false;
        Boolean result = travel.isArrivalTime();
        assertEquals("The value is wrong.",expResult, result);
    }

    /**
     * Test of setArrivalTime method, of class Travel.
     */
    @Test
    public void testSetArrivalTime() throws InvalidCountryCodeException {
        System.out.println("setArrivalTime");
        Boolean arrivalTime = true;
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.setArrivalTime(arrivalTime);
        assertTrue("The value is wrong, it should be true.", travel.isArrivalTime());
    }

    /**
     * Test of getRecurring method, of class Travel.
     */
    @Test
    public void testGetRecurring() throws InvalidCountryCodeException {
        System.out.println("getRecurring");
        List<Repetition> expResult = new ArrayList<>();
        RepetitionWeek rw = new RepetitionWeek();
        rw.setMon(Boolean.TRUE);
        expResult.add(rw);
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,expResult,new HashMap<>(),new ArrayList<>());
        List<Repetition> result = travel.getRecurring();
        for(int i=0;i<result.size();i++){
            assertEquals("The lists are different", expResult.get(i),result.get(i));
        }
    }

    /**
     * Test of addRecurring method, of class Travel.
     */
    @Test
    public void testAddRecurring() throws InvalidCountryCodeException {
        System.out.println("addRecurring");
        RepetitionWeek rw = new RepetitionWeek();
        rw.setMon(Boolean.TRUE);
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.addRecurring(rw);
        assertEquals("The Repetition was not added.",1,travel.getRecurring().size());
    }

    /**
     * Test of removeRecurring method, of class Travel.
     */
    @Test
    public void testRemoveRecurring() throws InvalidCountryCodeException {
        System.out.println("removeRecurring");
        RepetitionWeek rw = new RepetitionWeek();
        rw.setMon(Boolean.TRUE);
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.addRecurring(rw);
        travel.removeRecurring(rw);
        assertTrue("The Repetition was not added.",travel.getRecurring().isEmpty());
    }
    
    /**
     * Test of getStartPoint method, of class Travel.
     */
    @Test
    public void testGetStartPoint() throws InvalidCountryCodeException {
        System.out.println("getStartPoint");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Address result = travel.getStartPoint();
        assertEquals("The coordinates did not match",address1,result);
    }

    /**
     * Test of getEndPoint method, of class Travel.
     */
    @Test
    public void testGetEndPoint() throws InvalidCountryCodeException {
        System.out.println("getEndPoint");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Address result = travel.getEndPoint();
        assertEquals("The coordinates did not match",address2,result);
    }

    /**
     * Test of setStartPoint method, of class Travel.
     */
    @Test
    public void testSetStartPoint() throws InvalidCountryCodeException {
        System.out.println("setStartPoint");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Address address3 = new Address(new Street("name",new City("Ghent","9000","BE")),120,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Address result = address3;
        travel.setStartPoint(address3);
        assertEquals("The startpoints did not match.", result,travel.getStartPoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStartPoint_Exception() throws InvalidCountryCodeException {
        System.out.println("setStartPoint");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.setStartPoint(address2);
    }
    
    /**
     * Test of setEndPoint method, of class Travel.
     */
    @Test
    public void testSetEndPoint() throws InvalidCountryCodeException {
        System.out.println("setEndPoint");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Address address3 = new Address(new Street("name",new City("Ghent","9000","BE")),120,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Address result = address3;
        travel.setEndPoint(address3);
        assertEquals("The startpoints did not match.", result,travel.getEndPoint());
    }

    /**
     * Test of setEndPoint method, of class Travel.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEndPoint_Exception() throws InvalidCountryCodeException {
        System.out.println("setEndPoint");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.setEndPoint(address1);
    }
    
    /**
     * Test of getServices method, of class Travel.
     */
    @Test
    public void testGetServices() throws Exception{
        System.out.println("getServices");
        List<Service> expResult = new ArrayList<>();
        expResult.add(new PhoneNumber("0499311921"));
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        assertEquals(travel.getServices(),new ArrayList<Service>());
    }

    /**
     * Test of addService method, of class Travel.
     */
    @Test
    public void testAddService() throws Exception{
        System.out.println("addService");
        Service service = new PhoneNumber("0499311921");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.addService(service);
        assertEquals("The service was added.",1,travel.getServices().size());
    }

    /**
     * Test of removeService method, of class Travel.
     */
    @Test
    public void testRemoveService() throws InvalidPhoneNumberException, InvalidCountryCodeException {
        System.out.println("removeService");
        Service service = new PhoneNumber("0499311921");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel.addService(service);
        travel.removeService(service);
        assertTrue("The service was not removed",travel.getServices().isEmpty());
    }

    /**
     * Test of equals method, of class Travel.
     */
    @Test
    public void testEqualsTrue() throws InvalidCountryCodeException {
        System.out.println("equals");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Travel travel2 = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        boolean expResult = true;
        boolean result = travel.equals(travel2);
        assertEquals("The travels were different.",expResult, result);
    }

    /**
     * Test of equals method, of class Travel.
     */
    @Test
    public void testEqualsFalse() throws InvalidCountryCodeException {
        System.out.println("equals");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Travel travel2 = new Travel("name",LocalTime.MIDNIGHT,LocalTime.NOON,true,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        boolean expResult = false;
        boolean result = travel.equals(travel2);
        assertEquals("The travels were not different.",expResult, result);
    }

    /**
     * Test of today method, of class Travel.
     */
    @Test
    public void testToday() {
        System.out.println("today");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel instance = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address2,address1,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        boolean expResult = false;
        boolean result = instance.today();
        assertEquals(expResult, result);
    }

    /**
     * Test of updateData method, of class Travel.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel1 = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address2,address1,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Travel travel2 = new Travel("name-2",LocalTime.MIDNIGHT,LocalTime.NOON,true,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        travel2.updateData(travel1);
        assertEquals(travel2.getName(),travel1.getName());
        assertEquals(travel2.getBeginDate(),travel1.getBeginDate());
        assertEquals(travel2.getEndDate(),travel1.getEndDate());
        assertEquals(travel2.getEndPoint(),travel1.getEndPoint());
        assertEquals(travel2.getRecurring(),travel1.getRecurring());
        assertEquals(travel2.isArrivalTime(),travel1.isArrivalTime());
        assertEquals(travel2.getStartPoint(),travel1.getStartPoint());
    }

    /**
     * Test of equals method, of class Travel.
     */
    @Test
    public void testEquals_True() {
        System.out.println("equals");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel instance = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Object obj = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Travel.
     */
    @Test
    public void testEquals_False_1() {
        System.out.println("equals");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel instance = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        Object obj = new City("Ghent","9000","BE");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class Travel.
     */
    @Test
    public void testEquals_False_2() {
        System.out.println("equals");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel instance = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        boolean expResult = true;
        boolean result = instance.equals(instance);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetRecurring(){
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel instance = new Travel("name-1",LocalTime.MIDNIGHT,LocalTime.NOON,false,address1,address2,new ArrayList<>(),new HashMap<>(),new ArrayList<>());
        instance.setRecurring(Arrays.asList(new Repetition [] { new RepetitionWeek(new boolean [] {true, true, true, true, true, false, false})}));
    }
    
    
}
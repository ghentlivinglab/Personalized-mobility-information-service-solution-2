/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.AddressDTO;
import DTO.models.CoordinateDTO;
import DTO.models.TravelDTO;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import models.Coordinate;
import models.Travel;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.exceptions.InvalidCountryCodeException;
import models.repetition.RepetitionWeek;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class TravelMapperTest {
    
    public TravelMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class TravelMapper.
     */
    @Test
    public void testConvertToDTO_Travel_String() throws InvalidCountryCodeException {
        System.out.println("convertToDTO");
         Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name", LocalTime.MIN, LocalTime.MIN, true, address1, address2, new ArrayList<>(),new HashMap<>(), new ArrayList<>());
        travel.addRecurring(new RepetitionWeek(new boolean[]{true,true,true,true,true,true,true}));
        String id = "1";
        TravelMapper instance = new TravelMapper();
        AddressDTO addressDTO1 = new AddressDTO("name", "1", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        AddressDTO addressDTO2 = new AddressDTO("name", "100", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        TravelDTO expResult = new TravelDTO("1", "name", new String[]{"00:00:00","00:00:00"}, true, new boolean[]{true,true,true,true,true,true,true}, addressDTO1, addressDTO2);
        TravelDTO result = instance.convertToDTO(travel, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertToDTO method, of class TravelMapper.
     */
    @Test
    public void testConvertToDTO_Travel_int() throws InvalidCountryCodeException {
        System.out.println("convertToDTO");
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel travel = new Travel("name", LocalTime.MIN, LocalTime.MIN, true, address1, address2, new ArrayList<>(),new HashMap<>(), new ArrayList<>());
        travel.addRecurring(new RepetitionWeek(new boolean[]{true,true,true,true,true,true,true}));
        int id = 1;
        TravelMapper instance = new TravelMapper();
        AddressDTO addressDTO1 = new AddressDTO("name", "1", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        AddressDTO addressDTO2 = new AddressDTO("name", "100", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        TravelDTO expResult = new TravelDTO("1", "name", new String[]{"00:00:00","00:00:00"}, true, new boolean[]{true,true,true,true,true,true,true}, addressDTO1, addressDTO2);
        TravelDTO result = instance.convertToDTO(travel, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class TravelMapper.
     */
    @Test
    public void testConvertFromDTO() throws Exception {
        System.out.println("convertFromDTO");
        AddressDTO addressDTO1 = new AddressDTO("name", "1", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        AddressDTO addressDTO2 = new AddressDTO("name", "100", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        TravelDTO traveldto = new TravelDTO("1", "name", new String[]{"00:00","00:00"}, true, new boolean[]{true,true,true,true,true,true,true}, addressDTO1, addressDTO2);
        TravelMapper instance = new TravelMapper();
        Address address1 = new Address(new Street("name",new City("Ghent","9000","BE")),1,new Coordinate(50,50));
        Address address2 = new Address(new Street("name",new City("Ghent","9000","BE")),100,new Coordinate(50,50));
        Travel expResult = new Travel("name", LocalTime.MIN, LocalTime.MIN, true, address1, address2, new ArrayList<>(),new HashMap<>(), new ArrayList<>());
        Travel result = instance.convertFromDTO(traveldto);
        
        //compare startpoint
        assertEquals(expResult.getStartPoint().getHouseNumber(), result.getStartPoint().getHouseNumber());
        assertEquals(expResult.getStartPoint().getStreet().getName(), result.getStartPoint().getStreet().getName());
        assertEquals(expResult.getStartPoint().getStreet().getCity().getCityName(), result.getStartPoint().getStreet().getCity().getCityName());
        assertEquals(expResult.getStartPoint().getStreet().getCity().getCountry(), result.getStartPoint().getStreet().getCity().getCountry());
        assertEquals(expResult.getStartPoint().getStreet().getCity().getPostalCode(), result.getStartPoint().getStreet().getCity().getPostalCode());
        assertEquals(expResult.getStartPoint().getBox(), result.getStartPoint().getBox());
        assertEquals(expResult.getStartPoint().getCoordinates(),result.getStartPoint().getCoordinates());
        
        //compare endpoint
        assertEquals(expResult.getEndPoint().getHouseNumber(), result.getEndPoint().getHouseNumber());
        assertEquals(expResult.getEndPoint().getStreet().getName(), result.getEndPoint().getStreet().getName());
        assertEquals(expResult.getEndPoint().getStreet().getCity().getCityName(), result.getEndPoint().getStreet().getCity().getCityName());
        assertEquals(expResult.getEndPoint().getStreet().getCity().getCountry(), result.getEndPoint().getStreet().getCity().getCountry());
        assertEquals(expResult.getEndPoint().getStreet().getCity().getPostalCode(), result.getEndPoint().getStreet().getCity().getPostalCode());
        assertEquals(expResult.getEndPoint().getBox(), result.getEndPoint().getBox());
        assertEquals(expResult.getEndPoint().getCoordinates(),result.getEndPoint().getCoordinates());
        
        assertEquals(expResult.getBeginDate(), result.getBeginDate());
        assertEquals(expResult.getEndDate(), result.getEndDate());
        assertEquals(expResult.getName(), result.getName());
        //assertEquals(expResult.getRecurring(), result.getRecurring());
        assertEquals(expResult.getRoutes(), result.getRoutes());
        assertEquals(expResult.getServices(), result.getServices());
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.AddressDTO;
import DTO.models.CoordinateDTO;
import models.Coordinate;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.exceptions.InvalidCountryCodeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class AddressMapperTest {
    
    public AddressMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class AddressMapper.
     */
    @Test
    public void testConvertToDTO() throws InvalidCountryCodeException {
        System.out.println("convertToDTO");
        Address address = new Address(new Street("sportstraat", new City("Ghent","9000","BE")), 10, new Coordinate(50,50));
        AddressMapper instance = new AddressMapper();
        AddressDTO expResult = new AddressDTO("sportstraat", "10", "Ghent", "BE", "9000", new CoordinateDTO(50, 50));
        AddressDTO result = instance.convertToDTO(address);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class AddressMapper.
     */
    @Test
    public void testConvertFromDTO() throws Exception {
        System.out.println("convertFromDTO");
        AddressDTO addressdto = new AddressDTO("sportstraat", "10", "Ghent", "BE", "9000", new CoordinateDTO(50, 50));
        AddressMapper instance = new AddressMapper();
        Address expResult = new Address(new Street("sportstraat", new City("Ghent","9000","BE")), 10, new Coordinate(50,50));
        Address result = instance.convertFromDTO(addressdto);
        assertEquals(expResult.getHouseNumber(), result.getHouseNumber());
        assertEquals(expResult.getStreet().getName(), result.getStreet().getName());
        assertEquals(expResult.getStreet().getCity().getCityName(), result.getStreet().getCity().getCityName());
        assertEquals(expResult.getStreet().getCity().getCountry(), result.getStreet().getCity().getCountry());
        assertEquals(expResult.getStreet().getCity().getPostalCode(), result.getStreet().getCity().getPostalCode());
        assertEquals(expResult.getBox(), result.getBox());
        assertEquals(expResult.getCoordinates(),result.getCoordinates());
    }
    
    /**
     * Test of convertFromDTO method, of class AddressMapper.
     */
    @Test(expected = InvalidCountryCodeException.class)
    public void testConvertFromDTO_Exception() throws Exception {
        System.out.println("convertFromDTO");
        AddressDTO addressdto = new AddressDTO("sportstraat", "10", "Ghent", "BEE", "9000", new CoordinateDTO(50, 50));
        AddressMapper instance = new AddressMapper();
        Address expResult = new Address(new Street("sportstraat", new City("Ghent","9000","BE")), 10, new Coordinate(50,50));
        Address result = instance.convertFromDTO(addressdto);
        assertEquals(expResult.getHouseNumber(), result.getHouseNumber());
        assertEquals(expResult.getStreet().getName(), result.getStreet().getName());
        assertEquals(expResult.getStreet().getCity().getCityName(), result.getStreet().getCity().getCityName());
        assertEquals(expResult.getStreet().getCity().getCountry(), result.getStreet().getCity().getCountry());
        assertEquals(expResult.getStreet().getCity().getPostalCode(), result.getStreet().getCity().getPostalCode());
        assertEquals(expResult.getBox(), result.getBox());
        assertEquals(expResult.getCoordinates(),result.getCoordinates());
    }
}

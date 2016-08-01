/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.AddressDTO;
import DTO.models.CoordinateDTO;
import DTO.models.EventTypeDTO;
import static org.mockito.Mockito.*;
import DTO.models.LocationDTO;
import DTO.models.NotifyDTO;
import database_v2.controlLayer.Database;
import database_v2.controlLayer.impl.DatabaseImpl;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.mail.internet.AddressException;
import models.Coordinate;
import models.Location;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.event.EventType;
import models.exceptions.InvalidCountryCodeException;
import models.services.Email;
import models.services.PhoneNumber;
import org.javatuples.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class LocationMapperTest {
    
    public LocationMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class LocationMapper.
     */
    @Test
    public void testConvertToDTO_Location_String() throws InvalidCountryCodeException, AddressException {
        System.out.println("convertToDTO");
        Street street = new Street("Krijtekerkweg",new City("Ghent","9000","BE"));
        Address address = new Address(street,1, new Coordinate(50,50));
        Location location = new Location(address , "home", 2, false, new HashMap<>(), new ArrayList<>());
        location.addEventType(1, new EventType("Jam",new ArrayList<>()));
        location.addService(new Email("email@email.com"));
        String locationId = "1";
        LocationMapper instance = new LocationMapper();
        AddressDTO addressDTO = new AddressDTO("Krijtekerkweg", "1", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        LocationDTO expResult = new LocationDTO("1", addressDTO, "home", 2, true,new EventTypeDTO[]{new EventTypeDTO("Jam")}, new NotifyDTO(false));
        LocationDTO result = instance.convertToDTO(location, locationId);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertToDTO method, of class LocationMapper.
     */
    @Test
    public void testConvertToDTO_Location_int() throws InvalidCountryCodeException {
        System.out.println("convertToDTO");
        Street street = new Street("Krijtekerkweg",new City("Ghent","9000","BE"));
        Address address = new Address(street,1, new Coordinate(50,50));
        Location location = new Location(address , "home", 2, false, new HashMap<>(), new ArrayList<>());
        location.addEventType(1, new EventType("Jam",new ArrayList<>()));
        int locationId = 1;
        LocationMapper instance = new LocationMapper();
        AddressDTO addressDTO = new AddressDTO("Krijtekerkweg", "1", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        LocationDTO expResult = new LocationDTO("1", addressDTO, "home", 2, true,new EventTypeDTO[]{new EventTypeDTO("Jam")}, new NotifyDTO());
        LocationDTO result = instance.convertToDTO(location, locationId);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class LocationMapper.
     */
    @Test
    public void testConvertFromDTO() throws Exception {
        System.out.println("convertFromDTO");
        AddressDTO addressDTO = new AddressDTO("Krijtekerkweg", "1", "Ghent", "BE", "9000", new CoordinateDTO(50,50));
        LocationDTO locationdto = new LocationDTO("1", addressDTO, "home", 2, true,new EventTypeDTO[]{new EventTypeDTO("Jam")}, new NotifyDTO());
        LocationMapper instance = new LocationMapper();
        Street street = new Street("Krijtekerkweg",new City("Ghent","9000","BE"));
        Address address = new Address(street,1, new Coordinate(50,50));
        Location location = new Location(address , "home", 2, false, new HashMap<>(), new ArrayList<>());
        location.addEventType(1, new EventType("Jam",new ArrayList<>()));
        Location result = instance.convertFromDTO(locationdto);
        Location expResult = location;
        //assertEquals(expResult, result);
        
    }
    
}

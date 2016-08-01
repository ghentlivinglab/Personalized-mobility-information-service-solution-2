package database_v2.mappers;

import database_v2.exceptions.AlreadyExistsException;
import database_v2.models.relational.AddressDBModel;
import database_v2.models.relational.StreetDBModel;
import java.util.Arrays;
import models.Coordinate;
import models.address.Address;
import models.address.City;
import models.address.Street;
import org.javatuples.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

/**
 *
 */
public class AddressMapperTest extends AbstractDBMapperTest {

    @Mock
    private final CityMapper cityMapper;
    
    private final AddressMapper addressMapper;
    
    public AddressMapperTest() throws Exception {
        super();
        cityMapper = mock(CityMapper.class);
        
        addressMapper = new AddressMapper(cityMapper);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAddress method, of class AddressMapper.
     */
    @Test
    public void testGetAddress() throws Exception {
        System.out.println("getAddress");

        int streetId = 1;
        int addressId = 1;
        int cityId = 1;
        
        // create a coordinate for the address
        Coordinate coord = new Coordinate(51.050559, 3.725171);
        // start with mocking the responses of CRUDdao
        AddressDBModel dbAddress = new AddressDBModel(
                streetId, "1", coord.getLat(), coord.getLon(), coord.getX(), coord.getY());
        dbAddress.setId(addressId);
        when(cd.read(addressId, AddressDBModel.class)).thenReturn(dbAddress);
        StreetDBModel dbStreet = new StreetDBModel(cityId, "teststraat");
        dbStreet.setId(streetId);
        when(cd.read(streetId, StreetDBModel.class)).thenReturn(dbStreet);

        City city = new City("teststad", "1234", "BE");
        // mock the call to the CityMapper
        when(cityMapper.getCity(cityId, dac, cache)).thenReturn(city);

        // and check if we get a valid response
        Address result = addressMapper.getAddress(addressId, dac, cache);
        assertEquals(1, result.getHouseNumber());
        assertNull(result.getBox());
        assertEquals(dbStreet.getName(), result.getStreet().getName());
        assertEquals(city, result.getStreet().getCity());
    }

    /**
     * Test of validateAddress method, of class AddressMapper.
     */
    @Test
    public void testValidateAddress() throws Exception {
        System.out.println("validateAddress");

        int addressId = 1;
        int streetId = 1;
        int cityId = 1;
        City toTestCity = new City("invalidCity", "1234", "BE");
        Street toTestStreet = new Street("teststraat", toTestCity);
        Coordinate coord = new Coordinate(90.0, 90.0);
        Address toTestAddress = new Address(toTestStreet, 12, "B", coord);

        // first mock the responce of CityMapper
        City validCity = new City("validcity", "1234", "BE");
        when(cityMapper.validateCity(toTestCity, dac, cache)).thenReturn(new Pair<>(cityId, validCity));

        // we suppose the street already exists in the db
        doThrow(new AlreadyExistsException()).when(cd).create(any(StreetDBModel.class));

        // mock the response of the found street
        StreetDBModel validStreet = new StreetDBModel(cityId, "teststraat");
        validStreet.setId(streetId);
        when(cd.simpleSearch(eq(StreetDBModel.class), anyList())).thenReturn(Arrays.asList(validStreet));

        // we suppose the address already exists in the db
        doThrow(new AlreadyExistsException()).when(cd).create(any(AddressDBModel.class));

        // mock the resonse of the found address
        
        AddressDBModel validAddress = new AddressDBModel(
                streetId, "12;B", coord.getLat(), coord.getLon(), coord.getX(), coord.getY());
        validAddress.setId(addressId);
        when(cd.simpleSearch(eq(AddressDBModel.class), anyList())).thenReturn(Arrays.asList(validAddress));

        Pair<Integer, Address> result = addressMapper.validateAddress(toTestAddress, dac, cache);

        // and check if everything is all right
        assertEquals(12, result.getValue1().getHouseNumber());
        assertEquals("B", result.getValue1().getBox());
        assertEquals(validStreet.getName(), result.getValue1().getStreet().getName());
        assertEquals(validCity.getCityName(), result.getValue1().getStreet().getCity().getCityName());
        assertEquals(validCity.getPostalCode(), result.getValue1().getStreet().getCity().getPostalCode());
        assertEquals(validCity.getCountry(), result.getValue1().getStreet().getCity().getCountry());
    }
    
    @Test
    public void testDefaultConstructor() {
        AddressMapper instance = new AddressMapper();
        assertNotNull(instance);
    }

}

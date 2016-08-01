package database_v2.mappers;

import database_v2.models.TableJoin;
import database_v2.models.mongo.CoordinateDBModel;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.relational.LocationDBModel;
import database_v2.models.relational.LocationEventDBModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Coordinate;
import models.Location;
import models.Transportation;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.event.Event;
import models.event.EventType;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
/**
 *
 */
public class LocationMapperTest extends AbstractDBMapperTest {
    
    @Mock
    private final AddressMapper addressMapper;
    @Mock
    private final EventtypeMapper eventtypeMapper;
    @Mock
    private final EventMapper eventMapper;
    
    private final LocationMapper locationMapper;
    
    public LocationMapperTest() throws Exception {
        super();
        addressMapper = mock(AddressMapper.class);
        eventtypeMapper = mock(EventtypeMapper.class);
        eventMapper = mock(EventMapper.class);
        
        locationMapper = new LocationMapper(addressMapper, eventtypeMapper, eventMapper);
    }
    
    @Test
    public void testGetUserLocations() throws Exception {
        int accoundId = 1;
        int addressId = 2;
        int locationId = 3;
        int eventTypeId = 4;
        String eventId = "5";
        // here we mock the result list of the query for all the locations of a user
        List<LocationDBModel> queryResult = new ArrayList<>();
        LocationDBModel dbLoc = new LocationDBModel(
                accoundId,
                addressId,
                "location", // name
                1000, // radius
                Boolean.TRUE, // active
                Boolean.TRUE, // notify email
                Boolean.TRUE // notify cell
        );
        dbLoc.setId(locationId);
        queryResult.add(dbLoc);
        
        // the database will return this list of locations
        when(cd.complexSearch(any(TableJoin.class), anyList(), eq(LocationDBModel.class)))
                .thenReturn(queryResult);
        
        // then the mapper will convert this list to domain models. This will result in a call to
        // addressMapper and a call to EventtypeMapper. We have to mock both
        Coordinate addressCoord = new Coordinate(51.053447, 3.680833);
        City city = new City("testcity", "1234", "BE");
        Street street = new Street("teststreet", city);
        Address locationAddress = new Address(street, 112, addressCoord);
        
        Map<Integer, EventType> relevantEventTypes = new HashMap<>();
        EventType et = new EventType("TEST", Arrays.asList(Transportation.CAR));
        relevantEventTypes.put(eventTypeId, et);
        
        when(addressMapper.getAddress(addressId, dac, cache))
                .thenReturn(locationAddress);
        when(eventtypeMapper.getLocationEventtypes(locationId, dac, cache))
                .thenReturn(relevantEventTypes);
        
        // the mapper also gets the relevant events for a location, so we have to mock these to.
        List<LocationEventDBModel> foundLinks = new ArrayList<>();
        foundLinks.add(new LocationEventDBModel(locationId, eventId, Boolean.FALSE));
        when(cd.simpleSearch(eq(LocationEventDBModel.class), anyList()))
                .thenReturn(foundLinks);
        
        // mock response of EventDAO
        Coordinate eventCoord = new Coordinate(51.050559, 3.725171);
        CoordinateDBModel eventDbCoord = new CoordinateDBModel(eventCoord);
        EventDBModel dbEvent = new EventDBModel(
                eventDbCoord,
                true, // active
                1451606400000L, // Fri, 01 Jan 2016 00:00:00 GMT in epoch (publication)
                1451606400000L, // last edit
                "description",
                "address",
                new ArrayList<>(), // no jams
                eventTypeId,
                "UUID_MOCK",
                1, // mock for grid X value
                1 // mock for grid Y value
        );
        dbEvent.setId(eventId);
        when(ed.getEvent(eventId)).thenReturn(dbEvent);
        
        // mock the answer of the EventMapper
        Event event = new Event(
                dbEvent.getUuid(), //  uuid
                eventCoord, // coordinate
                dbEvent.getActive(), // is active?
                dbEvent.getPublicationTimeMillis(), // publication time
                dbEvent.getLastEditTimeMillis(),
                dbEvent.getDescription(),
                "address",
                et
        );
        when(eventMapper.getAppModel(any(), eq(dac), eq(cache))).thenReturn(event);
        
        // now we make the actual call
        Map<Integer,Location> result = locationMapper.getUserLocations(accoundId, dac, cache);
        
        // do the all the checks
        assertTrue(result.size() == 1);
        assertTrue(result.containsKey(locationId));
        Location locResult = result.get(locationId);
        checkCities(city, locResult.getAddress().getStreet().getCity());
        assertEquals(street.getName(), locResult.getAddress().getStreet().getName());
        assertEquals(locationAddress.getHouseNumber(), locResult.getAddress().getHouseNumber());
        assertEquals(dbLoc.getName(), locResult.getName());
        assertEquals((int)dbLoc.getRadius(), (int) locResult.getRadius());
        assertEquals((boolean) dbLoc.getActive(), !((boolean) locResult.isMuted()));
        assertEquals(relevantEventTypes, locResult.getNotifyForEventTypes());
    }
    
    @Test
    public void testDefaultConstructor() {
        LocationMapper instance = new LocationMapper();
        assertNotNull(instance);
    }
}

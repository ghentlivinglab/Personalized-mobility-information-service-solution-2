package database_v2.mappers;

import database_v2.models.TableJoin;
import database_v2.models.relational.TravelDBModel;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Coordinate;
import models.Travel;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.repetition.RepetitionWeek;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 */
public class TravelMapperTest extends AbstractDBMapperTest {

    @Mock
    private final AddressMapper addressMapper;
    @Mock
    private final RouteMapper routeMapper;

    private final TravelMapper travelMapper;

    public TravelMapperTest() throws Exception {
        super();
        addressMapper = mock(AddressMapper.class);
        routeMapper = mock(RouteMapper.class);
        travelMapper = new TravelMapper(addressMapper, routeMapper);
    }

    @Test
    public void testGetUserTravels() throws Exception {
        int accountId = 1;
        int startAddressId = 2;
        int endAddressId = 3;
        int travelId = 4;
        // mock the response of the database
        List<TravelDBModel> dbTravels = new ArrayList<>();
        TravelDBModel dbTravel = new TravelDBModel(
                accountId,
                startAddressId,
                endAddressId,
                "testTravel",
                new Time(1451606400000L), // Fri, 01 Jan 2016 00:00:00 GMT
                new Time(1451610000000L), // Fri, 01 Jan 2016 01:00:00 GMT
                true, // is arrival time
                true, true, true, true, true, true, true
        );
        dbTravel.setId(travelId);
        dbTravels.add(dbTravel);
        when(cd.complexSearch(any(TableJoin.class), anyList(), eq(TravelDBModel.class)))
                .thenReturn(dbTravels);

        // mock the answers of addressMapper
        Coordinate startCoord = new Coordinate(51.039147, 3.724520);
        City startCity = new City("startCity", "1234", "BE");
        Street startStreet = new Street("startStreet", startCity);
        Address startAddress = new Address(startStreet, 1, startCoord);
        when(addressMapper.getAddress(eq(startAddressId), any(), any()))
                .thenReturn(startAddress);

        Coordinate endCoord = new Coordinate(51.064118, 3.723863);
        City endCity = new City("endCity", "4321", "BE");
        Street endStreet = new Street("endStreet", endCity);
        Address endAddress = new Address(endStreet, 1, endCoord);
        when(addressMapper.getAddress(eq(endAddressId), any(), any()))
                .thenReturn(endAddress);

        // easy solution: this travel has no routes
        when(routeMapper.getTravelRoutes(eq(travelId), any(), any()))
                .thenReturn(new HashMap<>());

        Map<Integer, Travel> result = travelMapper.getUserTravels(accountId, dac, cache);

        // do all the checks
        assertTrue(result.size() == 1);
        assertTrue(result.containsKey(travelId));
        Travel travelRes = result.get(travelId);
        assertEquals(dbTravel.getName(), travelRes.getName());
        assertEquals(travelRes.getBeginDate().toString(), "01:00");
        assertEquals(travelRes.getEndDate().toString(), "02:00");
        for (boolean day : ((RepetitionWeek) travelRes.getRecurring().get(0)).getAllWeek()) {
            assertTrue(day);
        }
        assertEquals(travelRes.getStartPoint(), startAddress);
        assertEquals(travelRes.getEndPoint(), endAddress);
    }

    @Test
    public void toDBModelTest() throws Exception {
        int accountId = 1;
        int startId = 2;
        int endId = 3;
        
        Coordinate startCoord = new Coordinate(51.039147, 3.724520);
        City startCity = new City("startCity", "1234", "BE");
        Street startStreet = new Street("startStreet", startCity);
        Address startAddress = new Address(startStreet, 1, startCoord);

        Coordinate endCoord = new Coordinate(51.064118, 3.723863);
        City endCity = new City("endCity", "4321", "BE");
        Street endStreet = new Street("endStreet", endCity);
        Address endAddress = new Address(endStreet, 1, endCoord);

        Travel inputTrav = new Travel(
                "testtravel",
                LocalTime.NOON, // start
                LocalTime.NOON, // end
                Boolean.TRUE, // is arrival
                startAddress,
                endAddress,
                Arrays.asList(new RepetitionWeek()),
                new HashMap<>(),
                new ArrayList<>()
        );
        TravelDBModel result = travelMapper.toDBModel(accountId, startId, endId, inputTrav);
        
        // do all checks
        assertEquals(inputTrav.getName(), result.getName());
        assertEquals(startId, (int) result.getStartpoint());
        assertEquals(endId, (int) result.getEndpoint());
        assertEquals("12:00:00", result.getBeginTime().toString());
        assertEquals("12:00:00", result.getEndTime().toString());
    }
    
    @Test
    public void testDefaultConstructor() {
        TravelMapper tm = new TravelMapper();
        assertNotNull(tm);
    }
}

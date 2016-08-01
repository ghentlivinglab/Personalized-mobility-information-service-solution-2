package database_v2.mappers;

import database_v2.models.CRUDModel;
import database_v2.models.TableJoin;
import database_v2.models.relational.EventtypeDBModel;
import database_v2.models.relational.LocationEventtypeDBModel;
import database_v2.models.relational.RouteEventtypeDBModel;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import models.Transportation;
import models.event.EventType;
import org.javatuples.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class EventtypeMapperTest extends AbstractDBMapperTest {

    private final EventtypeMapper eventtypeMapper;

    public EventtypeMapperTest() throws Exception {
        super();
        eventtypeMapper = new EventtypeMapper();
    }

    @Test
    public void getRouteEventtypesTest() throws Exception {
        int routeId = 1;
        int eventtypeId = 2;
        // fist we mock the answer of the database
        List<RouteEventtypeDBModel> dbLinks = new ArrayList<>();
        dbLinks.add(new RouteEventtypeDBModel(routeId, eventtypeId));
        when(cd.complexSearch(any(), anyList(), eq(RouteEventtypeDBModel.class)))
                .thenReturn(dbLinks);

        // then the function will call getEventType
        // in this test, the eventType will be cached
        EventType cachedEt = new EventType("cached_et", Arrays.asList(Transportation.CAR));
        when(cache.getEventTypeIfPresent(eq(eventtypeId))).thenReturn(cachedEt);

        Map<Integer, EventType> result = eventtypeMapper.getRouteEventtypes(routeId, dac, cache);

        // do all checks
        assertTrue(result.size() == 1);
        assertTrue(result.containsKey(eventtypeId));
        EventType eventType = result.get(eventtypeId);
        assertEquals("cached_et", eventType.getType());
        assertEquals(Arrays.asList(Transportation.CAR), eventType.getTransportationTypes());
    }

    @Test
    public void getLocationEventtypesTest() throws Exception {
        int locationId = 1;
        int eventtypeId = 2;

        List<LocationEventtypeDBModel> dbLinks = new ArrayList<>();
        dbLinks.add(new LocationEventtypeDBModel(locationId, eventtypeId));
        when(cd.complexSearch(any(TableJoin.class), anyList(), eq(LocationEventtypeDBModel.class)))
                .thenReturn(dbLinks);

        // the function will then call getEventType. This time, there will be no EventType cached
        when(cache.getEventTypeIfPresent(eventtypeId)).thenReturn(null);

        // the method will then go to the databse to get the eventType
        Array array = mock(Array.class);
        when(array.getArray()).thenReturn(new String[]{"car", "bike"});
        EventtypeDBModel dbEt = new EventtypeDBModel("TEST", array);
        dbEt.setId(eventtypeId);
        when(cd.read(eq(eventtypeId), eq(EventtypeDBModel.class))).thenReturn(dbEt);

        // then the method will call dbToApplication to convert the newly acquired db model. We
        // only have to mock the answer of the array (done above)
        // do the command
        Map<Integer, EventType> result = eventtypeMapper.getLocationEventtypes(locationId, dac, cache);

        // do all checks
        assertTrue(result.size() == 1);
        assertTrue(result.containsKey(eventtypeId));
        assertEquals("TEST", result.get(eventtypeId).getType());
        assertEquals(Arrays.asList(Transportation.CAR, Transportation.BIKE),
                result.get(eventtypeId).getTransportationTypes());
        // check if the new eventType is added to the cache
        verify(cache, times(1)).addEventType(eq(eventtypeId), eq(result.get(eventtypeId)));

    }

    @Test
    public void getEventTypeTest_string() throws Exception {
        int eventTypeId = 1;

        // PATH 1: event type was cached
        EventType cachedEt = mock(EventType.class);
        when(cache.getEventTypeIfPresent(eq("TEST"))).thenReturn(cachedEt);
        when(cache.getEventTypeIdIfPresent(eq(cachedEt))).thenReturn(eventTypeId);

        Pair<Integer, EventType> result = eventtypeMapper.getEventType("TEST", dac, cache);
        assertEquals((int) result.getValue0(), eventTypeId);
        assertEquals(result.getValue1(), cachedEt);
        
        // PATH 2: event type was not cached and no event type was found in db
        when(cd.simpleSearch(eq(EventtypeDBModel.class), anyList())).thenReturn(new ArrayList<>());
        Connection conn = mock(Connection.class);
        Array array = mock(Array.class);
        when(array.getArray()).thenReturn(new String[]{"car", "bike"});
        when(dac.getSQLConnection()).thenReturn(conn);
        when(conn.createArrayOf(eq("text"), any())).thenReturn(array);
        doAnswer(invocation -> {
            CRUDModel in = (CRUDModel) invocation.getArguments()[0];
            in.setId(eventTypeId);
            return null;
        }).when(cd).create(any());
        
        result = eventtypeMapper.getEventType("TEST2", dac, cache);
        assertEquals(eventTypeId, (int)result.getValue0());
        assertEquals("TEST2", result.getValue1().getType());
        assertEquals(Arrays.asList(Transportation.CAR, Transportation.BIKE),
                result.getValue1().getTransportationTypes());
        
    }
    
}

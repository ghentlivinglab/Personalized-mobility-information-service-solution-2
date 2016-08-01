package database_v2.mappers;

import database_v2.models.TableJoin;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.relational.RouteDBModel;
import database_v2.models.relational.RouteEventDBModel;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Coordinate;
import models.Route;
import models.Transportation;
import models.event.Event;
import models.event.EventType;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.Mock;

/**
 *
 */
public class RouteMapperTest extends AbstractDBMapperTest {

    @Mock
    private final EventtypeMapper eventtypeMapper;
    @Mock
    private final EventMapper eventMapper;

    private final RouteMapper routeMapper;

    public RouteMapperTest() throws Exception {
        super();
        eventtypeMapper = mock(EventtypeMapper.class);
        eventMapper = mock(EventMapper.class);

        routeMapper = new RouteMapper(eventtypeMapper, eventMapper);
    }

    @Test
    public void getTravelRoutesTest() throws Exception {
        int travelId = 1;
        int routeId = 2;
        String eventId = "eventId";
        Array array = mock(Array.class);
        BigDecimal[][] arrayAnswer = new BigDecimal[][]{{new BigDecimal(1), new BigDecimal(1)}};
        when(array.getArray()).thenReturn(arrayAnswer);
        
        // fist mock the response of the db when looking for routes of travel
        List<RouteDBModel> dbRoutes = new ArrayList<>();
        RouteDBModel dbRoute = new RouteDBModel(
                travelId,
                array, // sql array user waypoints
                array, // sql array full waypoints
                "car",
                Boolean.TRUE, // active
                Boolean.TRUE, // email
                Boolean.TRUE // cell
        );
        dbRoute.setId(routeId);
        dbRoutes.add(dbRoute);
        when(cd.complexSearch(any(TableJoin.class), anyList(), eq(RouteDBModel.class)))
                .thenReturn(dbRoutes);

        // the function will then call toDomainModel. Here we have to mock the eventtypeMapper
        when(eventtypeMapper.getRouteEventtypes(eq(routeId), any(), any()))
                .thenReturn(new HashMap<>());
        
        // the function will then call fillInRelevantEvents, here we have to mock simpleSearch first
        List<RouteEventDBModel> foundLinks = new ArrayList<>();
        foundLinks.add(new RouteEventDBModel(routeId, eventId, Boolean.FALSE));
        when(cd.simpleSearch(eq(RouteEventDBModel.class), anyList()))
                .thenReturn(foundLinks);
        
        // dummy dbEvent
        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        when(ed.getEvent(eq(eventId))).thenReturn(dbEvent);
        
        // mock the answer of the EventMapper
        EventType et = new EventType("TEST", Arrays.asList(Transportation.CAR));
        Coordinate eventCoord = new Coordinate(51.050559, 3.725171);
        Event event = new Event(
                "UUID", //  uuid
                eventCoord, // coordinate
                true, // is active?
                1000L, // publication time
                1000L,
                "desc",
                "address",
                et
        );
        when(eventMapper.getAppModel(eq(dbEvent), any(), any())).thenReturn(event);
        
        Map<Integer, Route> result = routeMapper.getTravelRoutes(travelId, dac, cache);
        
        // do all the checks
        assertTrue(result.size() == 1);
        assertTrue(result.containsKey(routeId));
        Route route = result.get(routeId);
        assertEquals(new Coordinate(1.0, 1.0), route.getUserWaypoints().get(0));
        assertEquals(new Coordinate(1.0, 1.0), route.getFullWaypoints().get(0));
        assertEquals(Transportation.CAR, route.getTransportationType());
    }
    
    @Test
    public void toDBModelTest() throws Exception {
        int travelId = 1;
        Connection conn = mock(Connection.class);
        Array array = mock(Array.class);
        when(conn.createArrayOf(eq("numeric"), any(Double[][].class))).thenReturn(array);
        
        Route input = new Route(
                Arrays.asList(new Coordinate(50.0, 3.5)), // user waypoints
                Arrays.asList(new Coordinate(50.0, 3.5)),
                Transportation.CAR,
                new HashMap<>(),
                true,
                new ArrayList<>()
        );
        
        RouteDBModel result = routeMapper.toDBModel(travelId, input, conn);
        
        // do all the checks
        assertEquals(travelId, (int) result.getTravelId());
        assertEquals(array, result.getUserWaypoints());
        assertEquals(array, result.getFullWaypoints());
        assertEquals("car", result.getTransportationType());
    }
    
    @Test
    public void testDefaultConstructor() {
        RouteMapper instance = new RouteMapper();
        assertNotNull(instance);
    }

}

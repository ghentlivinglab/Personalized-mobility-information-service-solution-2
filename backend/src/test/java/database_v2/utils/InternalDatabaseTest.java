package database_v2.utils;

import database_v2.DAOLayer.CRUDdao;
import database_v2.DAOLayer.EventDAO;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DataAccessProvider;
import database_v2.controlLayer.DatabaseCache;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.mappers.EventMapper;
import database_v2.mappers.LocationMapper;
import database_v2.mappers.RouteMapper;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.relational.AccountDBModel;
import database_v2.models.relational.LocationDBModel;
import database_v2.models.relational.LocationEventDBModel;
import database_v2.models.relational.RouteDBModel;
import database_v2.models.relational.RouteEventDBModel;
import database_v2.models.relational.TravelDBModel;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Coordinate;
import models.Location;
import models.Route;
import models.event.Event;
import models.event.EventType;
import models.users.Password;
import models.users.User;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class InternalDatabaseTest {

    @Mock
    private final DataAccessProvider dap;
    @Mock
    private final DatabaseCache cache;
    @Mock
    private final DataAccessContext dac;
    @Mock
    private final CRUDdao cd;
    @Mock
    private final EventDAO ed;

    @Mock
    private final LocationMapper locationMapper;
    @Mock
    private final RouteMapper routeMapper;
    @Mock
    private final EventMapper eventMapper;

    private final InternalDatabase db;

    public InternalDatabaseTest() {
        this.dap = mock(DataAccessProvider.class);
        this.cache = mock(DatabaseCache.class);
        this.dac = mock(DataAccessContext.class);
        this.cd = mock(CRUDdao.class);
        this.ed = mock(EventDAO.class);

        this.eventMapper = mock(EventMapper.class);
        this.routeMapper = mock(RouteMapper.class);
        this.locationMapper = mock(LocationMapper.class);

        db = new InternalDatabase(dap, cache, locationMapper, routeMapper, eventMapper);
    }

    @Before
    public void setUp() throws Exception {
        when(dap.getDataAccessContext()).thenReturn(dac);
        when(dac.getCRUDdao()).thenReturn(cd);
        when(dac.getEventDAO()).thenReturn(ed);
    }

    @Test
    public void testGetRouteSelection() throws Exception {
        // PATH 1
        int routeId = 1;
        Pair<Integer, Integer> gridPoint = new Pair<>(1, 2);
        Event event = mock(Event.class);
        when(event.getPublicationTimeMillis()).thenReturn(0L);
        when(event.getLastEditTimeMillis()).thenReturn(1000L);

        List<RouteDBModel> foundRoutes = new ArrayList<>();
        RouteDBModel dbRoute = mock(RouteDBModel.class);
        when(dbRoute.getId()).thenReturn(routeId);
        foundRoutes.add(dbRoute);
        when(cd.complexSearch(any(), anyList(), eq(RouteDBModel.class))).thenReturn(foundRoutes);

        Route route = mock(Route.class);
        when(routeMapper.toDomainModel(dbRoute, dac, cache)).thenReturn(route);

        List<Pair<Integer, Route>> result = db.getRouteSelection(gridPoint, event);

        // do all the checks
        assertTrue(result.size() == 1);
        assertEquals(routeId, (int) result.get(0).getValue0());
        assertEquals(route, result.get(0).getValue1());

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getRouteSelection(gridPoint, event);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);

    }

    @Test
    public void testCoupleEventToRoute() throws Exception {
        // PATH 1
        int routeId = 1;
        String eventId = "eventid";
        db.coupleEventToRoute(routeId, eventId);
        verify(cd, times(1)).create(any(RouteEventDBModel.class));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.coupleEventToRoute(routeId, eventId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testDeleteOldEventsOfRoute() throws Exception {
        // PATH 1
        int routeId = 1;
        db.deleteOldEventsOfRoute(routeId);
        verify(cd, times(1)).delete(eq(RouteEventDBModel.class), anyList());

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteOldEventsOfRoute(routeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetLocationSelection() throws Exception {
        // PATH 1
        int locationId = 1;
        Coordinate coord = mock(Coordinate.class);
        List<LocationDBModel> dbLocations = new ArrayList<>();
        LocationDBModel dbLoc = mock(LocationDBModel.class);
        when(dbLoc.getId()).thenReturn(locationId);
        dbLocations.add(dbLoc);
        when(cd.complexSearch(any(), anyList(), eq(LocationDBModel.class))).thenReturn(dbLocations);

        Location loc = mock(Location.class);
        when(locationMapper.toDomainModel(dbLoc, dac, cache)).thenReturn(loc);

        List<Pair<Integer, Location>> result = db.getLocationSelection(coord);

        // do all the checks
        assertTrue(result.size() == 1);
        assertEquals(locationId, (int) result.get(0).getValue0());
        assertEquals(loc, result.get(0).getValue1());

        // PATH 2
        when(locationMapper.toDomainModel(dbLoc, dac, cache)).thenThrow(new RecordNotFoundException());
        db.getLocationSelection(coord);

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getLocationSelection(coord);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCoupleEventToLocation() throws Exception {
        // PATH 1
        int locationId = 1;
        String eventId = "eventid";
        db.coupleEventToLocation(locationId, eventId);
        verify(cd, times(1)).create(any(LocationEventDBModel.class));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.coupleEventToLocation(locationId, eventId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testDeleteOldEventsOfLocation() throws Exception {
        // PATH 1
        int locationId = 1;
        db.deleteOldEventsOfLocation(locationId);
        verify(cd, times(1)).delete(eq(LocationEventDBModel.class), anyList());

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteOldEventsOfLocation(locationId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetTimeInfoOfRoute() throws Exception {
        // PATH 1
        int routeId = 2;
        int travelId = 1;

        List<TravelDBModel> dbTravels = new ArrayList<>();
        boolean[] daysArray = new boolean[7];
        Time begin = mock(Time.class);
        Time end = mock(Time.class);
        TravelDBModel dbTravel = mock(TravelDBModel.class);
        when(dbTravel.getDaysArray()).thenReturn(daysArray);
        when(dbTravel.getBeginTime()).thenReturn(begin);
        when(dbTravel.getEndTime()).thenReturn(end);
        dbTravels.add(dbTravel);
        when(cd.complexSearch(any(), anyList(), eq(TravelDBModel.class))).thenReturn(dbTravels);

        Triplet<boolean[], Date, Date> result = db.getTimeInfoOfRoute(routeId);

        // do all checks
        assertEquals(daysArray, result.getValue0());
        assertEquals(begin, result.getValue1());
        assertEquals(end, result.getValue2());

        // PATH 2
        when(cd.complexSearch(any(), anyList(), eq(TravelDBModel.class)))
                .thenReturn(new ArrayList<>());
        boolean thrown = false;
        try {
            db.getTimeInfoOfRoute(routeId);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);

        // GENERAL: SQL exception
        when(cd.complexSearch(any(), anyList(), eq(TravelDBModel.class))).thenReturn(dbTravels);
        doThrow(new SQLException()).when(dac).close();
        thrown = false;
        try {
            db.getTimeInfoOfRoute(routeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetUserOfRoute() throws Exception {
        // PATH 1
        int routeId = 1;
        int userId = 2;

        List<AccountDBModel> foundAccs = new ArrayList<>();
        AccountDBModel dbAcc = mock(AccountDBModel.class);
        Password pass = new Password("Default123");
        when(dbAcc.getId()).thenReturn(userId);
        when(dbAcc.getPassword()).thenReturn(pass.getStringPassword());
        when(dbAcc.getSalt()).thenReturn(pass.getSalt());
        when(dbAcc.getFirstname()).thenReturn("firstname");
        when(dbAcc.getLastname()).thenReturn("lastname");
        when(dbAcc.getEmail()).thenReturn("test@test.com");
        when(dbAcc.getEmailValidated()).thenReturn(Boolean.TRUE);
        when(dbAcc.getMuteNotifications()).thenReturn(Boolean.FALSE);
        when(dbAcc.getIsOperator()).thenReturn(Boolean.FALSE);
        when(dbAcc.getIsAdmin()).thenReturn(Boolean.FALSE);
        foundAccs.add(dbAcc);
        when(cd.complexSearch(any(), anyList(), eq(AccountDBModel.class))).thenReturn(foundAccs);

        Pair<Integer, User> result = db.getUserOfRoute(routeId);

        assertEquals(userId, (int) result.getValue0());
        assertEquals("firstname", result.getValue1().getFirstName());
        assertEquals("lastname", result.getValue1().getLastName());
        assertEquals("test@test.com", result.getValue1().getEmailAsString());

        // PATH 2
        when(cd.complexSearch(any(), anyList(), eq(AccountDBModel.class)))
                .thenReturn(new ArrayList<>());
        boolean thrown = false;
        try {
            db.getUserOfRoute(routeId);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);

        // GENERAL: SQL exception
        when(cd.complexSearch(any(), anyList(), eq(AccountDBModel.class)))
                .thenReturn(foundAccs);
        doThrow(new SQLException()).when(dac).close();
        thrown = false;
        try {
            db.getUserOfRoute(routeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);

    }

    @Test
    public void testGetUserOfLocation() throws Exception {
        // PATH 1
        int locationId = 1;
        int userId = 2;

        List<AccountDBModel> foundAccs = new ArrayList<>();
        AccountDBModel dbAcc = mock(AccountDBModel.class);
        Password pass = new Password("Default123");
        when(dbAcc.getId()).thenReturn(userId);
        when(dbAcc.getPassword()).thenReturn(pass.getStringPassword());
        when(dbAcc.getSalt()).thenReturn(pass.getSalt());
        when(dbAcc.getFirstname()).thenReturn("firstname");
        when(dbAcc.getLastname()).thenReturn("lastname");
        when(dbAcc.getEmail()).thenReturn("test@test.com");
        when(dbAcc.getEmailValidated()).thenReturn(Boolean.TRUE);
        when(dbAcc.getMuteNotifications()).thenReturn(Boolean.FALSE);
        when(dbAcc.getIsOperator()).thenReturn(Boolean.FALSE);
        when(dbAcc.getIsAdmin()).thenReturn(Boolean.FALSE);
        foundAccs.add(dbAcc);
        when(cd.complexSearch(any(), anyList(), eq(AccountDBModel.class))).thenReturn(foundAccs);

        Pair<Integer, User> result = db.getUserOfLocation(locationId);

        assertEquals(userId, (int) result.getValue0());
        assertEquals("firstname", result.getValue1().getFirstName());
        assertEquals("lastname", result.getValue1().getLastName());
        assertEquals("test@test.com", result.getValue1().getEmailAsString());

        // PATH 2
        when(cd.complexSearch(any(), anyList(), eq(AccountDBModel.class)))
                .thenReturn(new ArrayList<>());
        boolean thrown = false;
        try {
            db.getUserOfLocation(locationId);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);

        // GENERAL: SQL exception
        when(cd.complexSearch(any(), anyList(), eq(AccountDBModel.class)))
                .thenReturn(foundAccs);
        doThrow(new SQLException()).when(dac).close();
        thrown = false;
        try {
            db.getUserOfLocation(locationId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetEventTypeId() throws Exception {
        // PATH 1
        EventType input = mock(EventType.class);
        int id = 1;
        when(cache.getEventTypeIdIfPresent(input)).thenReturn(id);
        assertEquals(id, db.getEventTypeId(input));

        // PATH 2
        id = 0;
        when(cache.getEventTypeIdIfPresent(input)).thenReturn(id);
        boolean thrown = false;
        try {
            db.getEventTypeId(input);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    
    @Test
    public void testGetEventId() throws Exception {
        // PATH 1
        Event input = mock(Event.class);
        String eventId = "eventid";
        when(cache.getEventIdIfPresent(input)).thenReturn(eventId);
        assertEquals(eventId, db.getEventId(input));
        
        // PATH 2
        eventId = null;
        when(cache.getEventIdIfPresent(input)).thenReturn(eventId);
        boolean thrown = false;
        try {
            db.getEventId(input);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    
    @Test
    public void testGetEventsByGridpoint() throws Exception {
        // PATH 1
        String eventId = "eventid";
        List<EventDBModel> foundEvents = new ArrayList<>();
        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        foundEvents.add(dbEvent);
        when(ed.getEvents(anyInt(), anyInt())).thenReturn(foundEvents);
        
        when(cache.getEventIfPresent(eq(eventId))).thenReturn(null);
        Event event = mock(Event.class);
        when(eventMapper.getAppModel(dbEvent, dac, cache)).thenReturn(event);
        
        List<Pair<String, Event>> result = db.getEventsByGridpoint(new Pair<>(1,1));
        
        verify(cache, times(1)).addEvent(eventId, event);
        
        // do all checks
        assertTrue(result.size() == 1);
        assertEquals(eventId, result.get(0).getValue0());
        assertEquals(event, result.get(0).getValue1());
        
        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getEventsByGridpoint(new Pair<>(1,1));
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    
    @Test
    public void testGetEventsByRadius() throws Exception {
        // PATH 1
        String eventId = "eventid";
        List<EventDBModel> foundEvents = new ArrayList<>();
        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        foundEvents.add(dbEvent);
        when(ed.getEventsInRadius(anyDouble(), anyDouble(), anyInt())).thenReturn(foundEvents);
        
        when(cache.getEventIfPresent(eq(eventId))).thenReturn(null);
        Event event = mock(Event.class);
        when(eventMapper.getAppModel(dbEvent, dac, cache)).thenReturn(event);
        
        List<Pair<String, Event>> result = db.getEventsByRadius(1.0, 1.0, 10);
        
        verify(cache, times(1)).addEvent(eventId, event);
        
        // do all checks
        assertTrue(result.size() == 1);
        assertEquals(eventId, result.get(0).getValue0());
        assertEquals(event, result.get(0).getValue1());
        
        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getEventsByRadius(1.0, 1.0, 1);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    
    @Test
    public void constructorTest() throws Exception {
        InternalDatabase inst = new InternalDatabase(dap, cache);
        assertNotNull(inst);
    }

}

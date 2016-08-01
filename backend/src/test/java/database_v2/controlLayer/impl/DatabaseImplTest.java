package database_v2.controlLayer.impl;

import backend.AppProperties;
import database_v2.DAOLayer.*;
import database_v2.controlLayer.*;
import database_v2.exceptions.*;
import database_v2.mappers.*;
import database_v2.models.CRUDModel;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.relational.*;
import database_v2.utils.Rasterizer;
import datacoupler.DataCoupler;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import models.Location;
import models.Route;
import models.Transportation;
import models.Travel;
import models.address.Address;
import models.event.Event;
import models.event.EventType;
import models.users.Password;
import models.users.User;
import org.javatuples.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class DatabaseImplTest {

    // fields of databaseImpl
    private final DataAccessProvider dap;
    private final DatabaseCache cache;
    private final Rasterizer rasterizer;
    private final DataCoupler coupler;

    // standard responses
    private final DataAccessContext dac;
    private final CRUDdao cd;
    private final EventDAO ed;

    // mappers
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final AddressMapper addressMapper;
    private final TravelMapper travelMapper;
    private final RouteMapper routeMapper;
    private final EventtypeMapper eventtypeMapper;
    private final EventMapper eventMapper;

    // test subject
    private final DatabaseImpl db;

    public DatabaseImplTest() {
        dap = mock(DataAccessProviderImpl.class);
        cache = mock(DatabaseCacheImpl.class);
        rasterizer = mock(Rasterizer.class);
        coupler = mock(DataCoupler.class);

        dac = mock(DataAccessContext.class);
        cd = mock(CRUDdao.class);
        ed = mock(EventDAO.class);

        userMapper = mock(UserMapper.class);
        locationMapper = mock(LocationMapper.class);
        addressMapper = mock(AddressMapper.class);
        travelMapper = mock(TravelMapper.class);
        routeMapper = mock(RouteMapper.class);
        eventMapper = mock(EventMapper.class);
        eventtypeMapper = mock(EventtypeMapper.class);

        db = new DatabaseImpl(dap, cache, rasterizer, coupler, userMapper, locationMapper,
                addressMapper, travelMapper, routeMapper, eventtypeMapper, eventMapper);
    }

    @Before
    public void setUp() throws Exception {
        when(dap.getDataAccessContext()).thenReturn(dac);
        when(dac.getCRUDdao()).thenReturn(cd);
        when(dac.getEventDAO()).thenReturn(ed);
    }

    @Test
    public void testGetUser() throws Exception {
        // PATH 1: user exists
        int userId = 1;
        User response = mock(User.class);
        when(userMapper.getUser(eq(userId), any(), any())).thenReturn(response);
        User result = db.getUser(userId);
        assertEquals(response, result);

        // GENERAL: SQL exception
        userId = 99;
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getUser(userId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        int userId = 1;
        User user = mock(User.class);
        Pair<Integer, User> response = new Pair<>(userId, user);
        when(userMapper.getUser(anyString(), any(), any())).thenReturn(response);
        Pair<Integer, User> result = db.getUserByEmail("test");
        assertEquals(response, result);

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getUserByEmail("test2");
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateUser() throws Exception {
        // PATH 1
        int userId = 1;
        // mock the input
        User input = mock(User.class);
        // mock the db model
        AccountDBModel dbModel = mock(AccountDBModel.class);
        when(dbModel.getId()).thenReturn(userId);
        // give back this mocked db model
        when(userMapper.toDBModel(eq(input))).thenReturn(dbModel);

        assertEquals(userId, db.createUser(input));

        verify(cd, times(1)).create(dbModel);
        verify(cache, times(1)).addUser(eq(userId), eq(input));

        // PATH 2
        doThrow(new ForeignKeyNotFoundException()).when(cd).create(eq(dbModel));
        boolean thrown = false;
        try {
            db.createUser(input);
        } catch (RuntimeException ex) {
            thrown = true;
        }
        assertTrue(thrown);

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        thrown = false;
        try {
            db.getUserByEmail("test2");
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdateUser() throws Exception {
        // PATH 1
        int userId = 1;
        // mock the input
        User input = mock(User.class);
        // mock the db model
        AccountDBModel dbModel = mock(AccountDBModel.class);
        when(dbModel.getId()).thenReturn(userId);
        // give back this mocked db model
        when(userMapper.toDBModel(eq(input))).thenReturn(dbModel);

        db.updateUser(userId, input);

        verify(dbModel, times(1)).setId(eq(userId));
    }

    @Test
    public void testUpdateUserEmail() throws Exception {
        // PATH 1
        int userId = 1;
        // mock the input
        User input = mock(User.class);
        // mock the db model
        AccountDBModel dbModel = mock(AccountDBModel.class);
        when(dbModel.getId()).thenReturn(userId);
        // give back this mocked db model
        when(userMapper.toDBModel(eq(input))).thenReturn(dbModel);

        db.updateUserEmail(userId, input, "oldmail");

        verify(dbModel, times(1)).setId(eq(userId));
        verify(cache, times(1)).emailUserChanged("oldmail");
    }

    @Test
    public void testDeleteUser() throws Exception {
        // PATH 1
        int userId = 1;

        db.deleteUser(userId);
        verify(cd, times(1)).delete(eq(userId), eq(AccountDBModel.class));
        verify(cache, times(1)).deleteUser(userId);

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteUser(99);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testListAllUsers() throws Exception {
        // PATH 1
        int userId = 1;
        List<AccountDBModel> allDBaccounts = new ArrayList<>();
        Password pass = new Password("Default123");
        AccountDBModel dbAcc = new AccountDBModel(
                "email@t.c",
                "testfn",
                "testln",
                pass.getStringPassword(),
                false, // mute notifications
                true, //  email valid
                "REFRESH",
                pass.getSalt(),
                false, // operator
                false, // admin
                "pushToken"
        );
        dbAcc.setId(userId);
        allDBaccounts.add(dbAcc);

        when(cd.listAll(eq(AccountDBModel.class))).thenReturn(allDBaccounts);
        List<Pair<Integer, User>> result = db.listAllUsers();

        assertTrue(result.size() == 1);
        Pair<Integer, User> user = result.get(0);
        assertEquals(userId, (int) user.getValue0());

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.listAllUsers();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testListAllAdmins() throws Exception {
        int userId = 1;
        List<AccountDBModel> allAdmins = new ArrayList<>();
        Password pass = new Password("Default123");
        AccountDBModel dbAcc = new AccountDBModel(
                "email@t.c",
                "testfn",
                "testln",
                pass.getStringPassword(),
                false, // mute notifications
                true, //  email valid
                "REFRESH",
                pass.getSalt(),
                false, // operator
                true, // admin
                "pushToken"
        );
        dbAcc.setId(userId);
        allAdmins.add(dbAcc);
        when(cd.simpleSearch(eq(AccountDBModel.class), anyList())).thenReturn(allAdmins);

        List<Pair<Integer, User>> result = db.listAllAdmins();

        assertTrue(result.size() == 1);
        Pair<Integer, User> user = result.get(0);
        assertEquals(userId, (int) user.getValue0());

        result = db.listAllOperators();

        assertTrue(result.size() == 1);
        user = result.get(0);
        assertEquals(userId, (int) user.getValue0());

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.listAllUsers();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateTravel() throws Exception {
        // PATH 1
        int userId = 1;
        int travelId = 1;
        int beginId = 2;
        int endId = 3;
        Travel input = mock(Travel.class);
        Address begin = mock(Address.class);
        Address end = mock(Address.class);
        when(input.getStartPoint()).thenReturn(begin);
        when(input.getEndPoint()).thenReturn(end);

        when(addressMapper.validateAddress(eq(begin), any(), any()))
                .thenReturn(new Pair<>(beginId, begin));
        when(addressMapper.validateAddress(eq(end), any(), any()))
                .thenReturn(new Pair<>(endId, end));

        TravelDBModel dbModel = mock(TravelDBModel.class);
        when(dbModel.getId()).thenReturn(travelId);
        when(travelMapper.toDBModel(eq(userId), eq(beginId), eq(endId), eq(input)))
                .thenReturn(dbModel);

        assertEquals(travelId, db.createTravel(userId, input));

        verify(input, times(1)).setStartPoint(eq(begin));
        verify(input, times(1)).setEndPoint(eq(end));
        verify(cd, times(1)).create(eq(dbModel));

        // PATH 2
        doThrow(new AlreadyExistsException()).when(cd).create(eq(dbModel));
        boolean thrown = false;
        try {
            db.createTravel(userId, input);
        } catch (RuntimeException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdateTravel() throws Exception {
        // PATH 1
        int userId = 1;
        int travelId = 1;
        int beginId = 2;
        int endId = 3;
        Travel input = mock(Travel.class);
        Address begin = mock(Address.class);
        Address end = mock(Address.class);
        when(input.getStartPoint()).thenReturn(begin);
        when(input.getEndPoint()).thenReturn(end);

        when(addressMapper.validateAddress(eq(begin), any(), any()))
                .thenReturn(new Pair<>(beginId, begin));
        when(addressMapper.validateAddress(eq(end), any(), any()))
                .thenReturn(new Pair<>(endId, end));

        TravelDBModel dbModel = mock(TravelDBModel.class);
        when(dbModel.getId()).thenReturn(travelId);
        when(travelMapper.toDBModel(eq(userId), eq(beginId), eq(endId), eq(input)))
                .thenReturn(dbModel);

        db.updateTravel(userId, travelId, input);

        verify(dbModel, times(1)).setId(eq(travelId));
        verify(cd, times(1)).update(eq(dbModel));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.updateTravel(userId, travelId, input);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);

    }

    @Test
    public void testDeleteTravel() throws Exception {
        // PATH 1
        int travelId = 1;
        db.deleteTravel(travelId);
        verify(cd, times(1)).delete(eq(travelId), eq(TravelDBModel.class));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteTravel(travelId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testListAllTravels() throws Exception {
        // PATH 1
        int travelId = 1;
        List<TravelDBModel> foundTravels = new ArrayList<>();
        TravelDBModel dbModel = mock(TravelDBModel.class);
        when(dbModel.getId()).thenReturn(travelId);
        foundTravels.add(dbModel);

        when(cd.listAll(eq(TravelDBModel.class))).thenReturn(foundTravels);

        Travel travel = mock(Travel.class);
        when(travelMapper.toDomainModel(eq(dbModel), eq(true), any(), any()))
                .thenReturn(travel);

        List<Pair<Integer, Travel>> result = db.listAlltravels();
        assertTrue(result.size() == 1);
        assertEquals(new Pair<Integer, Travel>(travelId, travel), result.get(0));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.listAlltravels();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateLocation() throws Exception {
        // PATH 1
        int userId = 3;
        int locationId = 1;
        int addressId = 2;
        Address address = mock(Address.class);
        Pair<Integer, Address> addressPair = new Pair<>(addressId, address);

        Location input = mock(Location.class);
        when(input.getAddress()).thenReturn(address);
        when(input.getName()).thenReturn("location");
        when(input.getRadius()).thenReturn(1000);
        when(input.isMuted()).thenReturn(Boolean.FALSE);

        when(addressMapper.validateAddress(eq(address), any(), any()))
                .thenReturn(addressPair);

        doAnswer(invocation -> {
            CRUDModel model = (CRUDModel) invocation.getArguments()[0];
            model.setId(locationId);
            return null;
        }).when(cd).create(any(TravelDBModel.class));

        assertEquals(locationId, db.createLocation(userId, input));

        verify(input, times(1)).setAddress(eq(address));
        verify(cd, times(1)).create(any(TravelDBModel.class));
        verify(coupler, times(1)).coupleLocation(eq(locationId), eq(input));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.createLocation(userId, input);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdateLocation() throws Exception {
        // PATH 1
        int userId = 3;
        int locationId = 1;
        int addressId = 2;
        Address address = mock(Address.class);
        Pair<Integer, Address> addressPair = new Pair<>(addressId, address);

        Location input = mock(Location.class);
        when(input.getAddress()).thenReturn(address);
        when(input.getName()).thenReturn("location");
        when(input.getRadius()).thenReturn(1000);
        when(input.isMuted()).thenReturn(Boolean.TRUE);

        when(addressMapper.validateAddress(eq(address), any(), any()))
                .thenReturn(addressPair);

        db.updateLocation(userId, locationId, input);

        verify(input, times(1)).setAddress(eq(address));
        verify(cd, times(1)).update(any(LocationDBModel.class));
        verify(coupler, times(1)).coupleLocation(eq(locationId), eq(input));
    }

    @Test
    public void testDeleteLocation() throws Exception {
        // PATH 1
        int locationId = 1;
        db.deleteLocation(locationId);
        verify(cd, times(1)).delete(eq(locationId), eq(LocationDBModel.class));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteLocation(locationId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testListAllLocations() throws Exception {
        int locationId = 1;
        List<LocationDBModel> allLocations = new ArrayList<>();
        LocationDBModel dbLoc = mock(LocationDBModel.class);
        when(dbLoc.getId()).thenReturn(locationId);
        allLocations.add(dbLoc);
        when(cd.listAll(eq(LocationDBModel.class))).thenReturn(allLocations);

        Location location = mock(Location.class);
        when(locationMapper.toDomainModel(eq(dbLoc), any(), any())).thenReturn(location);

        List<Pair<Integer, Location>> result = db.listAllLocations();

        assertTrue(result.size() == 1);
        assertEquals(new Pair<Integer, Location>(locationId, location), result.get(0));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.listAllLocations();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateRoute() throws Exception {
        // PATH 1
        int travelId = 1;
        int routeId = 2;
        Route input = mock(Route.class);
        RouteDBModel dbModel = mock(RouteDBModel.class);
        when(dbModel.getId()).thenReturn(routeId);
        when(routeMapper.toDBModel(eq(travelId), eq(input), any())).thenReturn(dbModel);

        assertEquals(routeId, db.createRoute(travelId, input));

        verify(cd, times(1)).create(eq(dbModel));
        verify(rasterizer, times(1)).processRoute(eq(routeId), eq(input), any());

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.createRoute(travelId, input);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdateRoute() throws Exception {
        // PATH 1
        int travelId = 1;
        int routeId = 2;
        Route input = mock(Route.class);
        RouteDBModel dbModel = mock(RouteDBModel.class);
        when(dbModel.getId()).thenReturn(routeId);
        when(routeMapper.toDBModel(eq(travelId), eq(input), any())).thenReturn(dbModel);

        db.updateRoute(travelId, routeId, input);

        verify(dbModel, times(1)).setId(eq(routeId));
        verify(cd, times(1)).update(eq(dbModel));
        verify(rasterizer, times(1)).processRoute(eq(routeId), eq(input), any());

        // PATH 2
        doThrow(new AlreadyExistsException()).when(cd).update(eq(dbModel));
        boolean thrown = false;
        try {
            db.updateRoute(travelId, routeId, input);
        } catch (RuntimeException ex) {
            thrown = true;
        }
        assertTrue(thrown);

    }

    @Test
    public void testDeleteRoute() throws Exception {
        // PATH 1
        int routeId = 1;
        db.deleteRoute(routeId);
        verify(cd, times(1)).delete(eq(routeId), eq(RouteDBModel.class));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteRoute(routeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testListAllRoutes() throws Exception {
        // PATH 1
        int routeId = 1;
        List<RouteDBModel> allRoutes = new ArrayList<>();
        RouteDBModel dbModel = mock(RouteDBModel.class);
        when(dbModel.getId()).thenReturn(routeId);
        allRoutes.add(dbModel);
        when(cd.listAll(eq(RouteDBModel.class))).thenReturn(allRoutes);

        Route route = mock(Route.class);
        when(routeMapper.toDomainModel(eq(dbModel), any(), any())).thenReturn(route);

        List<Pair<Integer, Route>> result = db.listAllRoutes();

        assertTrue(result.size() == 1);
        assertEquals(new Pair<>(routeId, route), result.get(0));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.listAllRoutes();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateRouteEventtype() throws Exception {
        int routeId = 1;
        int eventTypeId = 2;
        String eventType = "TEST";
        Pair<Integer, EventType> validated = new Pair<>(eventTypeId, mock(EventType.class));
        when(eventtypeMapper.getEventType(eq(eventType), any(), any())).thenReturn(validated);

        assertEquals(validated, db.createRouteEventtype(routeId, eventType));

        verify(cd, times(1)).create(any(RouteEventtypeDBModel.class));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.createRouteEventtype(routeId, eventType);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateLocationEventtype() throws Exception {
        int locationId = 1;
        int eventTypeId = 2;
        String eventType = "TEST";
        Pair<Integer, EventType> validated = new Pair<>(eventTypeId, mock(EventType.class));
        when(eventtypeMapper.getEventType(eq(eventType), any(), any())).thenReturn(validated);

        assertEquals(validated, db.createLocationEventtype(locationId, eventType));

        verify(cd, times(1)).create(any(LocationEventtypeDBModel.class));
    }

    @Test
    public void testDeleteRouteEventtype() throws Exception {
        // PATH 1
        int routeId = 1;
        int eventTypeId = 2;
        List<RouteEventtypeDBModel> foundLinks = new ArrayList<>();
        when(cd.simpleSearch(eq(RouteEventtypeDBModel.class), anyList())).thenReturn(foundLinks);

        boolean thrown = false;
        try {
            db.deleteRouteEventtype(routeId, eventTypeId);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);

        // PATH 2
        foundLinks = new ArrayList<>();
        foundLinks.add(mock(RouteEventtypeDBModel.class));
        when(cd.simpleSearch(eq(RouteEventtypeDBModel.class), anyList())).thenReturn(foundLinks);

        db.deleteRouteEventtype(routeId, eventTypeId);
        verify(cd, times(1)).delete(eq(foundLinks.get(0)));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        thrown = false;
        try {
            db.deleteRouteEventtype(routeId, eventTypeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testDeleteLocationEventtype() throws Exception {
        // PATH 1
        int locationId = 1;
        int eventTypeId = 2;
        List<LocationEventtypeDBModel> foundLinks = new ArrayList<>();
        when(cd.simpleSearch(eq(LocationEventtypeDBModel.class), anyList())).thenReturn(foundLinks);

        boolean thrown = false;
        try {
            db.deleteLocationEventtype(locationId, eventTypeId);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);

        // PATH 2
        foundLinks = new ArrayList<>();
        foundLinks.add(mock(LocationEventtypeDBModel.class));
        when(cd.simpleSearch(eq(LocationEventtypeDBModel.class), anyList())).thenReturn(foundLinks);

        db.deleteLocationEventtype(locationId, eventTypeId);
        verify(cd, times(1)).delete(eq(foundLinks.get(0)));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        thrown = false;
        try {
            db.deleteLocationEventtype(locationId, eventTypeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetEventType() throws Exception {
        // PATH 1
        String eventType = "TEST";
        Pair<Integer, EventType> response = new Pair<>(1, mock(EventType.class));
        when(eventtypeMapper.getEventType(eq(eventType), any(), any())).thenReturn(response);

        assertEquals(response, db.getEventtype(eventType));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getEventtype(eventType);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetEventtypes() throws Exception {
        // PATH 1
        int eventTypeId = 1;
        String eventType = "TEST";
        List<EventtypeDBModel> allDbTypes = new ArrayList<>();
        EventtypeDBModel dbModel = mock(EventtypeDBModel.class);
        when(dbModel.getType()).thenReturn(eventType);
        allDbTypes.add(dbModel);

        when(cd.listAll(EventtypeDBModel.class)).thenReturn(allDbTypes);

        EventType et = mock(EventType.class);
        when(eventtypeMapper.getEventType(eq(eventType), any(), any()))
                .thenReturn(new Pair<>(eventTypeId, et));

        List<Pair<Integer, EventType>> result = db.getEventtypes();

        assertTrue(result.size() == 1);
        assertEquals(new Pair<>(eventTypeId, et), result.get(0));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getEventtypes();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void getEventtypesByTransport() throws Exception {
        // PATH 1
        int eventTypeId = 1;
        String eventType = "TEST";
        List<EventtypeDBModel> allDbTypes = new ArrayList<>();
        EventtypeDBModel dbModel = mock(EventtypeDBModel.class);
        when(dbModel.getType()).thenReturn(eventType);
        allDbTypes.add(dbModel);

        Connection conn = mock(Connection.class);
        Array array = mock(Array.class);
        when(conn.createArrayOf(eq("text"), any())).thenReturn(array);
        when(dac.getSQLConnection()).thenReturn(conn);

        when(cd.simpleSearch(eq(EventtypeDBModel.class), anyList())).thenReturn(allDbTypes);

        EventType et = mock(EventType.class);
        when(eventtypeMapper.getEventType(eq(eventType), any(), any()))
                .thenReturn(new Pair<>(eventTypeId, et));

        List<Pair<Integer, EventType>> result = db.getEventtypes(Transportation.CAR);

        assertTrue(result.size() == 1);
        assertEquals(new Pair<>(eventTypeId, et), result.get(0));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getEventtypes(Transportation.CAR);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetEvent() throws Exception {
        // PATH 1
        String eventId = "eventid";
        Event cached = mock(Event.class);
        when(cache.getEventIfPresent(eventId)).thenReturn(cached);

        Event result = db.getEvent(eventId);
        assertEquals(cached, result);

        // PATH 2
        when(cache.getEventIfPresent(eventId)).thenReturn(null);

        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        when(ed.getEvent(eventId)).thenReturn(dbEvent);

        Event event = mock(Event.class);
        when(eventMapper.getAppModel(eq(dbEvent), any(), any())).thenReturn(event);

        assertEquals(event, db.getEvent(eventId));

        verify(cache, times(1)).addEvent(eventId, event);

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getEvent(eventId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCreateEvent() throws Exception {
        // PATH 1
        String eventId = "eventid";
        String eventType = "eventType";
        int eventTypeId = 1;

        EventType et = mock(EventType.class);
        when(et.getType()).thenReturn(eventType);

        Event input = mock(Event.class);
        when(input.getType()).thenReturn(et);

        Pair<Integer, EventType> validatedType = new Pair<>(eventTypeId, et);
        when(eventtypeMapper.getEventType(eq(eventType), any(), any())).thenReturn(validatedType);

        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        when(dbEvent.getLastProcessedTimeMillis()).thenReturn(0L);
        when(eventMapper.toDBModel(eq(eventTypeId), eq(input), any())).thenReturn(dbEvent);

        when(ed.createEvent(dbEvent)).thenReturn(Boolean.TRUE);

        assertEquals(eventId, db.createEvent(input));

        verify(input, times(1)).setType(et);
        verify(ed, times(1)).updateEvent(dbEvent);
        verify(cache, times(1)).addEvent(eventId, input);
        verify(coupler, times(1)).coupleEvent(input);

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.createEvent(input);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdateEvent() throws Exception {
        // PATH 1
        String eventId = "eventid";
        String eventType = "eventType";
        int eventTypeId = 1;

        EventType et = mock(EventType.class);
        when(et.getType()).thenReturn(eventType);

        Event input = mock(Event.class);
        when(input.getType()).thenReturn(et);

        Pair<Integer, EventType> validatedType = new Pair<>(eventTypeId, et);
        when(eventtypeMapper.getEventType(eq(eventType), any(), any())).thenReturn(validatedType);

        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        when(eventMapper.toDBModel(eq(eventTypeId), eq(input), any())).thenReturn(dbEvent);

        db.updateEvent(eventId, input);

        verify(input, times(1)).setType(et);
        verify(dbEvent, times(1)).setId(eventId);
        verify(ed, times(1)).updateEvent(dbEvent);
        verify(cache, times(1)).addEvent(eventId, input);
        verify(coupler, times(1)).coupleEvent(input);
    }

    @Test
    public void testDeleteEvent() throws Exception {
        // PATH 1
        String eventId = "eventId";
        db.deleteEvent(eventId);
        
        verify(cd, times(1)).delete(eq(RouteEventDBModel.class), anyList());
        verify(cd, times(1)).delete(eq(LocationEventDBModel.class), anyList());
        verify(ed, times(1)).deleteEvent(eventId);
        verify(cache, times(1)).deleteEvent(eventId);
        
        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.deleteEvent(eventId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testListAllEvents() throws Exception {
        // PATH 1
        String eventId = "eventId";
        List<EventDBModel> dbEvents = new ArrayList<>();
        EventDBModel dbEvent = mock(EventDBModel.class);
        when(dbEvent.getId()).thenReturn(eventId);
        dbEvents.add(dbEvent);
        when(ed.getEvents()).thenReturn(dbEvents);

        Event event = mock(Event.class);
        when(eventMapper.getAppModel(dbEvent, dac, cache)).thenReturn(event);

        List<Pair<String, Event>> result = db.listAllEvents();

        assertTrue(result.size() == 1);
        assertEquals(new Pair<>(eventId, event), result.get(0));

        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.listAllEvents();
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetRecentEvents() throws Exception {
        // PATH 1
        String eventId1 = "eventId1";
        String eventId2 = "eventId2";

        Map<String, Event> events = new HashMap<>();

        Event ev1 = mock(Event.class);
        when(ev1.getLastEditTimeMillis()).thenReturn(System.currentTimeMillis());
        events.put(eventId1, ev1);

        Event ev2 = mock(Event.class);
        int minuteRecent = Integer.parseInt(AppProperties.instance()
                .getProp(AppProperties.PROP_KEY.EVENT_RECENT_TRES));
        long belowTreshold = System.currentTimeMillis() - 2 * 60 * 1000 * ((long) minuteRecent);
        when(ev2.getLastEditTimeMillis()).thenReturn(belowTreshold);
        events.put(eventId2, ev2);

        when(cache.getAllEvents()).thenReturn(events);

        List<Pair<String, Event>> result = db.getRecentEvents();

        assertTrue(result.size() == 1);
        assertEquals(new Pair<>(eventId1, ev1), result.get(0));
    }
    
    @Test
    public void testGetRecentEventsOfLocation() throws Exception {
        // PATH 1
        String eventId = "eventid";
        int locationId = 1;
        List<LocationEventDBModel> dbLinks = new ArrayList<>();
        LocationEventDBModel dbLink = mock(LocationEventDBModel.class);
        when(dbLink.getEventId()).thenReturn(eventId);
        dbLinks.add(dbLink);
        when(cd.complexSearch(any(), anyList(), eq(LocationEventDBModel.class))).thenReturn(dbLinks);
        
        Event event = mock(Event.class);
        when(event.getLastEditTimeMillis()).thenReturn(System.currentTimeMillis());
        when(cache.getEventIfPresent(eventId)).thenReturn(event);
        
        Set<Pair<String, Event>> res = db.getRecentEventsOfLocation(locationId);
        Set<Pair<String, Event>> exp = new HashSet<>();
        exp.add(new Pair<>(eventId, event));
        assertEquals(exp, res);
        
        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getRecentEventsOfLocation(locationId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    
    @Test
    public void testGetRecentEventsOfRoute() throws Exception {
        // PATH 1
        int routeId = 1;
        String eventId = "eventid";
        List<RouteEventDBModel> dbLinks = new ArrayList<>();
        RouteEventDBModel dbLink = mock(RouteEventDBModel.class);
        when(dbLink.getEventId()).thenReturn(eventId);
        dbLinks.add(dbLink);
        when(cd.complexSearch(any(), anyList(), eq(RouteEventDBModel.class))).thenReturn(dbLinks);
        
        when(cache.getEventIfPresent(eventId)).thenReturn(null);
        EventDBModel dbModel = mock(EventDBModel.class);
        when(dbModel.getId()).thenReturn(eventId);
        when(ed.getEvent(eventId)).thenReturn(dbModel);
        Event ev = mock(Event.class);
        when(ev.getLastEditTimeMillis()).thenReturn(System.currentTimeMillis());
        when(eventMapper.getAppModel(dbModel, dac, cache)).thenReturn(ev);
        
        Set<Pair<String, Event>> res = db.getRecentEventsOfRoute(routeId);
                
        verify(cache, times(1)).addEvent(eventId, ev);
        
        Set<Pair<String, Event>> exp = new HashSet<>();
        exp.add(new Pair<>(eventId, ev));
        
        assertEquals(exp, res);
        
        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getRecentEventsOfRoute(routeId);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    
    @Test
    public void testGetRecentEventsOfUser() throws Exception {
        when(cd.complexSearch(any(), anyList(), eq(RouteEventDBModel.class)))
                .thenReturn(new ArrayList<>());
        when(cd.complexSearch(any(), anyList(), eq(LocationEventDBModel.class)))
                .thenReturn(new ArrayList<>());
        
        db.getRecentEventsOfUser(1);
        
        // GENERAL: SQL exception
        doThrow(new SQLException()).when(dac).close();
        boolean thrown = false;
        try {
            db.getRecentEventsOfUser(1);
        } catch (DataAccessException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void constructorTest() throws Exception {
        Connection conn = mock(Connection.class);
        when(dac.getSQLConnection()).thenReturn(conn);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(conn.prepareStatement(any())).thenReturn(ps);
        
        when(ed.getEvents()).thenReturn(new ArrayList<>());
        
        Database inst = new DatabaseImpl(dap, cache, rasterizer, coupler);
        assertNotNull(inst);
    }
}

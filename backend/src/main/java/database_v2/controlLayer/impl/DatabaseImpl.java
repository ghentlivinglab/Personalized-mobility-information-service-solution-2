package database_v2.controlLayer.impl;

import backend.AppProperties;
import backend.AppProperties.PROP_KEY;
import database_v2.searchTerms.ArraySearchTerm;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.DAOLayer.CRUDdao;
import database_v2.DAOLayer.EventDAO;
import database_v2.controlLayer.*;
import database_v2.exceptions.*;
import database_v2.mappers.*;
import database_v2.models.*;
import database_v2.models.relational.EventtypeDBModel;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.relational.*;
import database_v2.searchTerms.SearchTerm;
import database_v2.utils.*;
import datacoupler.DataCoupler;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.mail.internet.AddressException;
import models.Location;
import models.Route;
import models.Transportation;
import models.Travel;
import models.address.Address;
import models.event.Event;
import models.event.EventType;
import models.exceptions.InvalidPasswordException;
import models.users.Password;
import models.users.User;
import org.apache.log4j.Logger;
import org.javatuples.Pair;

/**
 * A full implementation of the Database interface.
 */
public class DatabaseImpl implements Database {

    private final DataAccessProvider dap;
    private final DatabaseCache cache;
    private final Rasterizer rasterizer;
    private final DataCoupler coupler;

    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final AddressMapper addressMapper;
    private final TravelMapper travelMapper;
    private final RouteMapper routeMapper;
    private final EventtypeMapper eventtypeMapper;
    private final EventMapper eventMapper;

    /**
     * Create a new instance. This class is meant to be a singleton, so the creation and dependency
     * injection to other classes that need the database is done by Spring.
     *
     * @param dap An abstraction of the database.
     * @param cache Access to all cached data.
     * @param rasterizer Preprocessing module for routes
     * @param coupler Module that handles event coupling logics.
     */
    public DatabaseImpl(DataAccessProvider dap, DatabaseCache cache, Rasterizer rasterizer,
            DataCoupler coupler) {
        this.rasterizer = rasterizer;
        this.dap = dap;
        this.cache = cache;
        this.coupler = coupler;

        this.userMapper = new UserMapper();
        this.locationMapper = new LocationMapper();
        this.addressMapper = new AddressMapper();
        this.travelMapper = new TravelMapper();
        this.routeMapper = new RouteMapper();
        this.eventtypeMapper = new EventtypeMapper();
        this.eventMapper = new EventMapper();

        // start cleaning up accounts each hour to get rid of invalidate emails
        AccountCleanup accountCleanup = new AccountCleanup(dap);
        accountCleanup.start();

        // heat up the event cache with all recent events
        loadRecentEventsOnBoot();
    }

    /**
     * ONLY USED FOR TESTING REASONS.
     *
     * @param dap
     * @param cache
     * @param rasterizer
     * @param coupler
     * @param userMapper
     * @param locationMapper
     * @param addressMapper
     * @param travelMapper
     * @param routeMapper
     * @param eventtypeMapper
     * @param eventMapper
     */
    public DatabaseImpl(DataAccessProvider dap, DatabaseCache cache, Rasterizer rasterizer,
            DataCoupler coupler, UserMapper userMapper, LocationMapper locationMapper,
            AddressMapper addressMapper, TravelMapper travelMapper, RouteMapper routeMapper,
            EventtypeMapper eventtypeMapper, EventMapper eventMapper) {
        this.dap = dap;
        this.cache = cache;
        this.rasterizer = rasterizer;
        this.coupler = coupler;

        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this.addressMapper = addressMapper;
        this.travelMapper = travelMapper;
        this.routeMapper = routeMapper;
        this.eventtypeMapper = eventtypeMapper;
        this.eventMapper = eventMapper;

    }

    @Override
    public User getUser(int userId)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            return userMapper.getUser(userId, dac, cache);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Pair<Integer, User> getUserByEmail(String email)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            return userMapper.getUser(email, dac, cache);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public int createUser(User user)
            throws DataAccessException, AlreadyExistsException {
        try {
            return insertUser(false, 0, user);
        } catch (RecordNotFoundException ex) {
            // Program error; only meant for update
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateUser(int userId, User user)
            throws DataAccessException, AlreadyExistsException, RecordNotFoundException {
        insertUser(true, userId, user);
    }

    @Override
    public void updateUserEmail(int userId, User user, String oldEmail)
            throws DataAccessException, AlreadyExistsException, RecordNotFoundException {
        insertUser(true, userId, user);
        cache.emailUserChanged(oldEmail);
    }

    private int insertUser(boolean update, int userId, User user)
            throws DataAccessException, AlreadyExistsException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // first we convert the application model to a database model
            AccountDBModel toAdd = userMapper.toDBModel(user);
            if (update) {
                toAdd.setId(userId);
            }
            // this model we write to the database. If the email is already in use
            // then an AlreadyExistsException will be thrown
            try {
                if (update) {
                    cd.update(toAdd);

                } else {
                    cd.create(toAdd);
                    // and finally we add the user to the cache when created
                    cache.addUser(toAdd.getId(), user);
                }
            } catch (ForeignKeyNotFoundException ex) {
                // no foreign keys should be present in user; program error if reached
                throw new RuntimeException(ex);
            }

            // return the assigned id
            return toAdd.getId();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteUser(int userId) throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // delete the user from both the cache and the database itself.
            cd.delete(userId, AccountDBModel.class);
            cache.deleteUser(userId);

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, User>> listAllUsers() throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<Pair<Integer, User>> out = new ArrayList<>();

            List<AccountDBModel> allDBaccounts = cd.listAll(AccountDBModel.class);
            return toUserList(allDBaccounts);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, User>> listAllAdmins() throws DataAccessException {
        return listUserByRole(AccountDBModel.IS_ADMIN_ATTRIBUTE);
    }

    @Override
    public List<Pair<Integer, User>> listAllOperators() throws DataAccessException {
        return listUserByRole(AccountDBModel.IS_OPERATOR_ATTRIBUTE);
    }

    private List<Pair<Integer, User>> listUserByRole(Attribute roleAttribute)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(roleAttribute, true)
            );
            List<AccountDBModel> foundAdmins = cd.simpleSearch(AccountDBModel.class, search);
            return toUserList(foundAdmins);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private List<Pair<Integer, User>> toUserList(List<AccountDBModel> allDBaccounts)
            throws DataAccessException {
        List<Pair<Integer, User>> out = new ArrayList<>();
        for (AccountDBModel dbAccount : allDBaccounts) {
            try {
                Password pass = new Password(dbAccount.getPassword(), dbAccount.getSalt());
                // here we actually create invalid User models because we leave
                // all the dependencies out of the user (location, travels and services).
                out.add(new Pair<>(
                        dbAccount.getAccountId(),
                        new User(
                                dbAccount.getFirstname(),
                                dbAccount.getLastname(),
                                pass,
                                dbAccount.getEmail(),
                                dbAccount.getEmailValidated(),
                                null, // locations
                                null, // travels
                                dbAccount.getMuteNotifications(),
                                dbAccount.getRefreshToken(),
                                dbAccount.getIsOperator(),
                                dbAccount.getIsAdmin(),
                                dbAccount.getPushToken()
                        )));
            } catch (InvalidPasswordException | AddressException ex) {
                throw new DataAccessException(ex);
            }
        }
        return out;
    }

    @Override
    public int createTravel(int userId, Travel travel)
            throws DataAccessException, ForeignKeyNotFoundException {
        try {
            return insertTravel(false, userId, 0, travel);
        } catch (RecordNotFoundException ex) {
            // this exeption should only be thrown with update actions
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateTravel(int userId, int travelId, Travel travel)
            throws DataAccessException, ForeignKeyNotFoundException, RecordNotFoundException {
        insertTravel(true, userId, travelId, travel);
    }

    private int insertTravel(boolean update, int userId, int travelId, Travel travel)
            throws DataAccessException, ForeignKeyNotFoundException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // a travel contains two addresses. We must fist handle these dependencies
            Pair<Integer, Address> beginAddress
                    = addressMapper.validateAddress(travel.getStartPoint(), dac, cache);
            Pair<Integer, Address> endAddress
                    = addressMapper.validateAddress(travel.getEndPoint(), dac, cache);
            travel.setStartPoint(beginAddress.getValue1());
            travel.setEndPoint(endAddress.getValue1());

            // then we convert the application model to a database model
            TravelDBModel toAdd = travelMapper.toDBModel(userId, beginAddress.getValue0(), endAddress.getValue0(), travel);
            if (update) {
                toAdd.setId(travelId);
            }

            try {
                // we can now write this db model to the database
                if (update) {
                    cd.update(toAdd);
                } else {
                    cd.create(toAdd);
                }
            } catch (AlreadyExistsException ex) {
                // travels don't have any unique constraints so reaching this statement
                // must be a bug
                throw new RuntimeException(ex);
            }

            return toAdd.getId();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteTravel(int travelId) throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            cd.delete(travelId, TravelDBModel.class);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, Travel>> listAlltravels() throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<TravelDBModel> foundTravels = cd.listAll(TravelDBModel.class);
            List<Pair<Integer, Travel>> out = new ArrayList<>();
            for (TravelDBModel dbTravel : foundTravels) {
                out.add(new Pair<>(
                        dbTravel.getId(),
                        travelMapper.toDomainModel(dbTravel, true, dac, cache)
                ));
            }
            return out;
        } catch (SQLException | RecordNotFoundException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public int createLocation(int userId, Location location)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException {
        try {
            return insertLocation(false, userId, 0, location);
        } catch (RecordNotFoundException ex) {
            // only used for update, if this is reached, a programming error has occured
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateLocation(int userId, int locationId, Location location)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException, RecordNotFoundException {
        insertLocation(true, userId, locationId, location);
    }

    private int insertLocation(boolean update, int userId, int locationId, Location location)
            throws DataAccessException, RecordNotFoundException, ForeignKeyNotFoundException, AlreadyExistsException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // A location contains an address. We need to handle this dependency first
            Pair<Integer, Address> address = addressMapper.validateAddress(location.getAddress(), dac, cache);
            location.setAddress(address.getValue1());

            LocationDBModel toAdd = new LocationDBModel(
                    userId,
                    address.getValue0(),
                    location.getName(),
                    location.getRadius(),
                    !location.isMuted(),
                    Boolean.FALSE, // TODO replace this placeholder
                    Boolean.FALSE // TODO replace this placeholder
            );
            if (update) {
                toAdd.setId(locationId);
            }

            // add the model to the database, this may cause a AlreadyExistsException
            // if the location already exists
            if (update) {
                cd.update(toAdd);
            } else {
                cd.create(toAdd);
            }

            // let the coupler process the new location. This will be done on
            // another thread
            coupler.coupleLocation(toAdd.getId(), location);

            // and finally return the id of the newly created location
            return toAdd.getId();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteLocation(int locationId) throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            cd.delete(locationId, LocationDBModel.class);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, Location>> listAllLocations() throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<LocationDBModel> allLocations = cd.listAll(LocationDBModel.class);
            List<Pair<Integer, Location>> out = new ArrayList<>();
            for (LocationDBModel dbLocation : allLocations) {
                out.add(new Pair<>(
                        dbLocation.getId(),
                        locationMapper.toDomainModel(dbLocation, dac, cache)
                ));
            }
            return out;
        } catch (SQLException | RecordNotFoundException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public int createRoute(int travelId, Route route)
            throws DataAccessException, ForeignKeyNotFoundException {
        try {
            return insertRoute(false, travelId, 0, route);
        } catch (RecordNotFoundException ex) {
            // only meant for update
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void updateRoute(int travelId, int routeId, Route route)
            throws DataAccessException, ForeignKeyNotFoundException, RecordNotFoundException {

        insertRoute(true, travelId, routeId, route);
    }

    private int insertRoute(boolean update, int travelId, int routeId, Route route)
            throws DataAccessException, ForeignKeyNotFoundException, RecordNotFoundException {

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            // We first convert the application model to a database model
            RouteDBModel dbRoute = routeMapper.toDBModel(travelId, route, dac.getSQLConnection());
            if (update) {
                dbRoute.setId(routeId);
            }

            try {
                // this will throw a ForeignKeyNotFoundException when the travelId is
                // not associated with a valid travel
                if (update) {
                    cd.update(dbRoute);
                } else {
                    cd.create(dbRoute);
                }
            } catch (AlreadyExistsException ex) {
                // routes don't have any unique constraints, should not reach this
                throw new RuntimeException(ex);
            }

            // we hand the new route to the rasterizer preprocessor. This module
            // will do some heavy calculations on a separate thread an will update
            // the database itself when ready
            rasterizer.processRoute(dbRoute.getId(), route, coupler);

            return dbRoute.getId();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteRoute(int routeId) throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            cd.delete(routeId, RouteDBModel.class);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, Route>> listAllRoutes() throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<RouteDBModel> allRoutes = cd.listAll(RouteDBModel.class);
            List<Pair<Integer, Route>> out = new ArrayList<>();
            for (RouteDBModel dbRoute : allRoutes) {
                out.add(new Pair<>(
                        dbRoute.getId(),
                        routeMapper.toDomainModel(dbRoute, dac, cache)
                ));
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Pair<Integer, EventType> createRouteEventtype(int routeId, String eventType)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException {
        return insertEventtype(true, routeId, eventType);
    }

    @Override
    public Pair<Integer, EventType> createLocationEventtype(int locationId, String eventType)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException {
        return insertEventtype(false, locationId, eventType);
    }

    private Pair<Integer, EventType> insertEventtype(boolean route, int foreignKey, String eventType)
            throws DataAccessException, ForeignKeyNotFoundException, AlreadyExistsException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // first we validate is the eventType is consistent with the database
            Pair<Integer, EventType> validated = eventtypeMapper.getEventType(eventType, dac, cache);
            int eventTypeId = validated.getValue0();

            CRUDModel dbLink;
            if (route) {
                // then we add a link between the route and the validated EventType
                dbLink = new RouteEventtypeDBModel(foreignKey, eventTypeId);
            } else {
                // then we add a link between the location and the validated EventType
                dbLink = new LocationEventtypeDBModel(foreignKey, eventTypeId);
            }

            // and add this link to the database
            cd.create(dbLink);

            // at last we simply return the id of the EventType
            return validated;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteRouteEventtype(int routeId, int eventTypeId)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteEventtypeDBModel.getRouteIdAttribute(), routeId),
                    new SimpleSearchTerm(RouteEventtypeDBModel.getEventtypeIdAttribute(), eventTypeId)
            );
            List<RouteEventtypeDBModel> foundLinks = cd.simpleSearch(RouteEventtypeDBModel.class, search);
            if (foundLinks.isEmpty()) {
                throw new RecordNotFoundException("no link found between this route and eventType");
            } else {
                cd.delete(foundLinks.get(0));
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteLocationEventtype(int locationId, int eventtypeId)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(LocationEventtypeDBModel.getLocationIdAttribute(), locationId),
                    new SimpleSearchTerm(LocationEventtypeDBModel.getEventtypeIdAttribute(), eventtypeId)
            );
            List<LocationEventtypeDBModel> foundLinks = cd.simpleSearch(LocationEventtypeDBModel.class, search);
            if (foundLinks.isEmpty()) {
                throw new RecordNotFoundException("no link found between this location and eventType");
            } else {
                cd.delete(foundLinks.get(0));
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Pair<Integer, EventType> getEventtype(String eventType)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            // wrapper method for EventtypeMapper
            return eventtypeMapper.getEventType(eventType, dac, cache);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, EventType>> getEventtypes() throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            // get the data from the database.
            List<EventtypeDBModel> allDbTypes = cd.listAll(EventtypeDBModel.class);

            // and convert it to application models
            List<Pair<Integer, EventType>> out = new ArrayList<>(allDbTypes.size());
            for (EventtypeDBModel dbModel : allDbTypes) {
                out.add(eventtypeMapper.getEventType(dbModel.getType(), dac, cache));
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<Integer, EventType>> getEventtypes(Transportation transport)
            throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // fist we convert the given transport to an SQL array so we can hand
            // it to the DAOLayer, which only knows SQL supported types.
            Array transportArray = dac.getSQLConnection().createArrayOf(
                    "text",
                    new String[]{transport.toString()}
            );

            List<SearchTerm> search = Arrays.asList(
                    new ArraySearchTerm(EventtypeDBModel.getRelevantTransportTypesAttribute(), transportArray)
            );

            // get the eventTypes from db that match this transport
            List<EventtypeDBModel> allDbTypes = cd.simpleSearch(EventtypeDBModel.class, search);

            // and convert it to application models
            List<Pair<Integer, EventType>> out = new ArrayList<>(allDbTypes.size());
            for (EventtypeDBModel dbModel : allDbTypes) {
                out.add(eventtypeMapper.getEventType(dbModel.getType(), dac, cache));
            }
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Event getEvent(String eventId) throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            return getEvent(eventId, dac);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private Event getEvent(String eventId, DataAccessContext dac)
            throws DataAccessException, RecordNotFoundException {
        Event cached = cache.getEventIfPresent(eventId);
        if (cached != null) {
            return cached;
        }
        EventDAO ed = dac.getEventDAO();
        EventDBModel dbEvent = ed.getEvent(eventId);
        Event out = eventMapper.getAppModel(dbEvent, dac, cache);
        cache.addEvent(dbEvent.getId(), out);
        return out;
    }

    @Override
    public String createEvent(Event event) throws DataAccessException {
        try {
            return insertEvent(false, null, event);
        } catch (RecordNotFoundException ex) {
            // only meant for updates
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void updateEvent(String eventId, Event event)
            throws DataAccessException, RecordNotFoundException {
        insertEvent(true, eventId, event);
    }

    private String insertEvent(boolean update, String eventId, Event event)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            // fist things first: an event has a dependency on EventType. This
            // object is considered dirty until we check if it matches the data
            // as it is stored in the cache/db
            Pair<Integer, EventType> validated
                    = eventtypeMapper.getEventType(event.getType().getType(), dac, cache);
            int eventTypeId = validated.getValue0();

            // put the valid EventType back in the event
            event.setType(validated.getValue1());

            // once we have a valid eventType with id, we can add the event to
            // MongoDB with that id as "foreign key"
            EventDAO ed = dac.getEventDAO();
            EventDBModel dbEvent = eventMapper.toDBModel(eventTypeId, event, rasterizer);
            if (update) {
                dbEvent.setId(eventId);
            }

            boolean needsCoupling = true;
            // now we can add this data to the database
            if (update) {
                // update the event data in mongoDB
                ed.updateEvent(dbEvent);
                // remove all links to the old event in postgres. This must be done because the event
                // may have gotten a new location. For an update the coupling will be redone anyway.
                CRUDdao cd = dac.getCRUDdao();
                deleteLinksToEvent(eventId, cd);
            } else {
                boolean alreadyExists = ed.createEvent(dbEvent);
                if (alreadyExists) {
                    // we now calculate if the event coupling needs an update. We do this as follows:
                    // we check the last time that we coupled the event. If we are almost at the
                    // limit of the predicted duration of the event, we reevaluate the coupling
                    long lastCoupledTime = dbEvent.getLastProcessedTimeMillis();
                    long durationPredict = AppProperties.instance()
                            .getPropAsLong(PROP_KEY.EVENT_DURATION) * 60 * 1000;
                    long predictedEnd = lastCoupledTime + durationPredict;
                    long pollingTime = AppProperties.instance()
                            .getPropAsLong(PROP_KEY.WAZE_POLLING_FEQ) * 1000;
                    if (System.currentTimeMillis() >= predictedEnd - pollingTime) {
                        needsCoupling = true;
                        dbEvent.setLastProcessedTimeMillis(System.currentTimeMillis());
                        ed.updateEvent(dbEvent);
                    } else {
                        needsCoupling = false;
                    }
                }
            }

            // we need to handle the event object with some care. Each event that already exits anywhere
            // in the system must be represented by the same object.
            Event cached = cache.getEventIfPresent(dbEvent.getId());
            if (cached != null) {
                // the event is already somewhere in the system, so we update its value. Then we will
                // couple the cached object. The argument event is thrown away
                cached.updateEvent(event);
                event = cached;
            } else {
                cache.addEvent(dbEvent.getId(), event);
            }

            // now we let the coupler handle the event. Needs to happen AFTER it's added to the 
            // cache. If it is an update (done from contoller), then the coordinates may have changed
            // so we couple again. When it is a create, and the event already existed, then we can
            // skip the coupling
            if (update || needsCoupling) {
                coupler.coupleEvent(event);
            }
            // at last we return the newly assigned id.
            return dbEvent.getId();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteEvent(String eventId) throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            EventDAO ed = dac.getEventDAO();

            // fist we delete all records in the relational database that have pointers to the
            // event to be deleted
            deleteLinksToEvent(eventId, cd);
            // only then we delete the event itself
            ed.deleteEvent(eventId);
            // delete the event from the cache if it was present there
            cache.deleteEvent(eventId);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Remove all links in the relational database between routes/locations and an event. This
     * function can be used before deleting or updating an event.
     *
     * @param eventId The event to which all the pointers in the relational database must be
     * deleted.
     * @param cd DAO for access to relational database.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    private void deleteLinksToEvent(String eventId, CRUDdao cd)
            throws DataAccessException {
        // for route - event links
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(RouteEventDBModel.getEventIdAttribute(), eventId)
        );
        cd.delete(RouteEventDBModel.class, search);
        // for location - event links
        search = Arrays.asList(
                new SimpleSearchTerm(LocationEventDBModel.getEventIdAttribute(), eventId)
        );
        cd.delete(LocationEventDBModel.class, search);
    }

    @Override
    public List<Pair<String, Event>> listAllEvents() throws DataAccessException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            EventDAO ed = dac.getEventDAO();

            // first we get the db models from the database.
            List<EventDBModel> dbEvents = ed.getEvents();

            List<Pair<String, Event>> events = new ArrayList<>(dbEvents.size());
            for (EventDBModel dbEvent : dbEvents) {
                events.add(new Pair<>(
                        dbEvent.getId(),
                        eventMapper.getAppModel(dbEvent, dac, cache)
                ));
            }

            return events;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Pair<String, Event>> getRecentEvents() {
        int minuteRecent = Integer.parseInt(AppProperties.instance().getProp(PROP_KEY.EVENT_RECENT_TRES));
        long treshold = System.currentTimeMillis() - 60 * 1000 * ((long) minuteRecent);
        List<Pair<String, Event>> out = new ArrayList<>();
        cache.getAllEvents().forEach((k, v) -> {
            if (v.getLastEditTimeMillis() >= treshold) {
                out.add(new Pair<>(k, v));
            }
        });
        return out;
    }

    @Override
    public Set<Pair<String, Event>> getRecentEventsOfLocation(int locationId)
            throws DataAccessException, RecordNotFoundException {
        int minuteRecent = Integer.parseInt(AppProperties.instance()
                .getProp(PROP_KEY.EVENT_RECENT_TRES));
        long threshold = System.currentTimeMillis() - 60 * 1000 * ((long) minuteRecent);
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(LocationEventDBModel.getLocationIdAttribute(), locationId)
            );
            TableJoin join = new TableJoin(
                    LocationDBModel.class, LocationEventDBModel.class,
                    LocationEventDBModel.getLocationIdAttribute());

            List<LocationEventDBModel> dbLinks
                    = cd.complexSearch(join, search, LocationEventDBModel.class);

            return dbLocationEventsToSet(dbLinks, threshold, dac);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private Set<Pair<String, Event>> dbLocationEventsToSet(List<LocationEventDBModel> dbLinks,
            long threshold, DataAccessContext dac)
            throws DataAccessException, RecordNotFoundException {
        Set<Pair<String, Event>> out = new HashSet<>();

        for (LocationEventDBModel dbLink : dbLinks) {
            String evId = dbLink.getEventId();
            Event ev = getEvent(evId, dac);
            if (ev.getLastEditTimeMillis() >= threshold) {
                out.add(new Pair<>(evId, ev));
            }
        }
        return out;
    }

    @Override
    public Set<Pair<String, Event>> getRecentEventsOfRoute(int routeId)
            throws DataAccessException, RecordNotFoundException {
        int minuteRecent = Integer.parseInt(AppProperties.instance()
                .getProp(PROP_KEY.EVENT_RECENT_TRES));
        long threshold = System.currentTimeMillis() - 60 * 1000 * ((long) minuteRecent);
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteEventDBModel.getRouteIdAttribute(), routeId)
            );

            TableJoin join = new TableJoin(
                    RouteDBModel.class, RouteEventDBModel.class,
                    RouteEventDBModel.getRouteIdAttribute()
            );

            List<RouteEventDBModel> dbLinks
                    = cd.complexSearch(join, search, RouteEventDBModel.class);
            return dbRouteEventsToSet(dbLinks, threshold, dac);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private Set<Pair<String, Event>> dbRouteEventsToSet(List<RouteEventDBModel> dbLinks,
            long threshold, DataAccessContext dac)
            throws DataAccessException, RecordNotFoundException {
        Set<Pair<String, Event>> out = new HashSet<>();
        for (RouteEventDBModel dbLink : dbLinks) {
            String evId = dbLink.getEventId();
            Event ev = getEvent(evId, dac);
            if (ev.getLastEditTimeMillis() >= threshold) {
                out.add(new Pair<>(evId, ev));
            }
        }
        return out;
    }

    @Override
    public Set<Pair<String, Event>> getRecentEventsOfUser(int userId)
            throws DataAccessException, RecordNotFoundException {
        int minuteRecent = Integer.parseInt(AppProperties.instance()
                .getProp(PROP_KEY.EVENT_RECENT_TRES));
        long threshold = System.currentTimeMillis() - 60 * 1000 * ((long) minuteRecent);
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // PART 1: getting the events from all the routes of a user
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(TravelDBModel.getAccountIdAttribute(), userId)
            );

            TableJoin join = new TableJoin(
                    RouteEventDBModel.class, RouteDBModel.class,
                    RouteEventDBModel.getRouteIdAttribute()
            );
            join = new TableJoin(join, TravelDBModel.class, RouteDBModel.getTravelIdAttribute());

            List<RouteEventDBModel> dbRouteEvents
                    = cd.complexSearch(join, search, RouteEventDBModel.class);

            Set<Pair<String, Event>> out = dbRouteEventsToSet(dbRouteEvents, threshold, dac);

            // PART 2: getting the events of all the locations of the user
            search = Arrays.asList(
                    new SimpleSearchTerm(LocationDBModel.getAccountIdAttribute(), userId)
            );

            join = new TableJoin(LocationEventDBModel.class, LocationDBModel.class,
                    LocationEventDBModel.getLocationIdAttribute());

            List<LocationEventDBModel> dbLocationEvents
                    = cd.complexSearch(join, search, LocationEventDBModel.class);

            out.addAll(dbLocationEventsToSet(dbLocationEvents, threshold, dac));
            return out;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private void loadRecentEventsOnBoot() {
        int minuteRecent = Integer.parseInt(AppProperties.instance().getProp(PROP_KEY.EVENT_RECENT_TRES));
        long treshold = System.currentTimeMillis() - 60 * 1000 * ((long) minuteRecent);
        try {
            listAllEvents().forEach(pair -> {
                if (pair.getValue1().getLastEditTimeMillis() >= treshold) {
                    cache.addEvent(pair.getValue0(), pair.getValue1());
                }
            });
        } catch (DataAccessException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
    }

}

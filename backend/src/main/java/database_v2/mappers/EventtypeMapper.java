package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.EventtypeDBModel;
import database_v2.models.TableJoin;
import database_v2.models.relational.LocationDBModel;
import database_v2.models.relational.LocationEventtypeDBModel;
import database_v2.models.relational.RouteDBModel;
import database_v2.models.relational.RouteEventtypeDBModel;
import database_v2.searchTerms.SearchTerm;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.event.EventType;
import models.Transportation;
import org.apache.log4j.Logger;
import org.javatuples.Pair;

/**
 * This class offers a set of static functions to do the mapping between db and application models
 * for EventTypes.
 */
public class EventtypeMapper {

    /**
     * Get all the EventTypes application models from the database that are relevant for the given
     * route.
     *
     * @param routeId The route of which you want the relevant EventTypes
     * @param dac An abstraction of a connection
     * @param cache Access to all cached data.
     * @return A Map which maps id's on their EventType application model
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Map<Integer, EventType> getRouteEventtypes(int routeId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {
        CRUDdao cd = dac.getCRUDdao();
        TableJoin join = new TableJoin(RouteDBModel.class, RouteEventtypeDBModel.class, RouteEventtypeDBModel.getRouteIdAttribute());
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(RouteDBModel.getRouteIdAttribute(), routeId)
        );
        List<RouteEventtypeDBModel> dbLinks = cd.complexSearch(join, search, RouteEventtypeDBModel.class);

        Map<Integer, EventType> out = new HashMap<>();
        for (RouteEventtypeDBModel dbLink : dbLinks) {
            try {
                out.put(dbLink.getEventtypeId(), getEventType(dbLink.getEventtypeId(), dac, cache));
            } catch (RecordNotFoundException ex) {
                // route contains an invalid EventType. This means a programming
                // error or corrupted database data.
                throw new RuntimeException(ex);
            }
        }
        return out;
    }

    /**
     * Get all eventTypes that are listed in the database to be relevant for a given location. This
     * will return valid and correctly filled in EventType models.
     *
     * @param locationId The id of the location for which you want relevant event types
     * @param dac An abstraction of a connection
     * @param cache Access to cached objects
     * @return Map of event type id's mapped on their event types.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Map<Integer, EventType> getLocationEventtypes(int locationId, DataAccessContext dac,
            DatabaseCache cache)
            throws DataAccessException {
        CRUDdao cd = dac.getCRUDdao();
        TableJoin join = new TableJoin(
                LocationDBModel.class,
                LocationEventtypeDBModel.class,
                LocationEventtypeDBModel.getLocationIdAttribute()
        );
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(LocationDBModel.getLocationIdAttribute(), locationId)
        );
        List<LocationEventtypeDBModel> dbLinks = cd.complexSearch(join, search, LocationEventtypeDBModel.class);

        Map<Integer, EventType> out = new HashMap<>();
        for (LocationEventtypeDBModel dbLink : dbLinks) {
            try {
                out.put(dbLink.getEventtypeId(), getEventType(dbLink.getEventtypeId(), dac, cache));
            } catch (RecordNotFoundException ex) {
                // route contains an invalid EventType. This means a programming
                // error or corrupted database data.
                throw new RuntimeException(ex);
            }
        }
        return out;
    }

    /**
     * Get a specific EventType application model by id. The cache is first consulted before
     * accessing the database itself. The cache will be updated for with newly fetched EventTypes.
     *
     * @param eventtypeId The id of the EventType to be fetched
     * @param dac An abstraction of a connection
     * @param cache Access to all cached data.
     * @return An EventType application model
     * @throws DataAccessException Something went wrong with the underlying database
     * @throws RecordNotFoundException No EventType with this id was found.
     */
    public EventType getEventType(int eventtypeId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        EventType cached = cache.getEventTypeIfPresent(eventtypeId);
        if (cached != null) {
            return cached;
        }

        CRUDdao cd = dac.getCRUDdao();
        EventtypeDBModel dbEventtype = cd.read(eventtypeId, EventtypeDBModel.class);

        EventType out = dbToApplication(dbEventtype);
        cache.addEventType(dbEventtype.getId(), out);
        return out;
    }

    /**
     * Use this function to get an EventType application model based on its name. This will return
     * an EventType that is completely consistent with the database. If the EventType did not
     * already exist in the database, then a new record is added. This will default to all possible
     * transportation types. The cache is always used where possible and updated if necessary.
     *
     * @param eventType The name of the eventType to be fetched
     * @param dac An abstraction of a connection
     * @param cache Access to all cached data
     * @return An EventType application model completely consistent with the database paired with
     * its id.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Pair<Integer, EventType> getEventType(String eventType, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {
        EventType cached = cache.getEventTypeIfPresent(eventType);
        // there was still a cached eventType left
        if (cached != null) {
            return new Pair<>(cache.getEventTypeIdIfPresent(cached), cached);
        }

        // No cached version was found so we need to look in the database
        CRUDdao cd = dac.getCRUDdao();
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(EventtypeDBModel.getTypeAttribute(), eventType)
        );
        List<EventtypeDBModel> foundTypes = cd.simpleSearch(EventtypeDBModel.class, search);

        EventtypeDBModel dbType;
        if (foundTypes.isEmpty()) {
            // in the unlikely event that the EventType is not found in the database,
            // a new record will be added which is defaulted to be relevant for
            // all transportationtypes.

            // some low level conversion for the transport types
            Transportation[] allTransports = Transportation.values();
            String[] transportStrings = new String[allTransports.length];
            for (int i = 0; i < allTransports.length; i++) {
                transportStrings[i] = allTransports[i].toString();
            }
            try {
                dbType = new EventtypeDBModel(
                        eventType,
                        dac.getSQLConnection().createArrayOf("text", transportStrings)
                );
                cd.create(dbType);
            } catch (AlreadyExistsException | ForeignKeyNotFoundException ex) {
                // should not reach this, program error
                Logger.getLogger(getClass())
                        .error("logical error while creating non existing EventType", ex);
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } else {
            dbType = foundTypes.get(0);
        }

        // now that we have a valid db model, we need to convert it to a database model
        EventType out = dbToApplication(dbType);
        cache.addEventType(dbType.getId(), out);
        return new Pair<>(dbType.getId(), out);
    }

    /**
     * Private helper method that parses a EventType db model to an EventType application model.
     *
     * @param dbModel the db model to be parsed
     * @return The resulting application model
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    private EventType dbToApplication(EventtypeDBModel dbModel)
            throws DataAccessException {
        Array transportArray = dbModel.getRelevantTransportTypes();

        List<Transportation> transports = new ArrayList<>();
        // fetch and parse transporttypes from db
        try {
            if (transportArray != null) {
                String[] strings = (String[]) transportArray.getArray();
                for (String ts : strings) {
                    transports.add(
                            Transportation.fromString(ts)
                    );
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }

        return new EventType(dbModel.getType(), transports);
    }

}

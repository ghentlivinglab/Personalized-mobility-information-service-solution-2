package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.TableJoin;
import database_v2.models.relational.AccountDBModel;
import database_v2.models.relational.LocationDBModel;
import database_v2.searchTerms.SearchTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.address.Address;
import models.Location;
import models.event.EventType;

/**
 * This class offers a set of static functions to do the mapping between db and application Location
 * models.
 */
public class LocationMapper {

    private final AddressMapper addressMapper;
    private final EventtypeMapper eventtypeMapper;
    private final EventMapper eventMapper;

    /**
     * create a new LocationMapper
     */
    public LocationMapper() {
        addressMapper = new AddressMapper();
        eventtypeMapper = new EventtypeMapper();
        eventMapper = new EventMapper();
    }

    /**
     * Only for testing reasons, do not use in production code
     *
     * @param addressMapper mapper for addresses
     * @param eventtypeMapper mapper for event types
     * @param eventMapper mapper for events
     */
    public LocationMapper(AddressMapper addressMapper, EventtypeMapper eventtypeMapper,
            EventMapper eventMapper) {
        this.addressMapper = addressMapper;
        this.eventtypeMapper = eventtypeMapper;
        this.eventMapper = eventMapper;
    }

    /**
     * Get all the locations of a user as they are stored in the database. This results in a Map
     * that maps id's to location application models.
     *
     * @param userId The id of the user of which the locations are fetched.
     * @param dac An abstraction of a connection
     * @param cache Access to all cached data
     * @return Map that maps id's to location application models
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No user was found with this id.
     */
    public Map<Integer, Location> getUserLocations(int userId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        CRUDdao cd = dac.getCRUDdao();

        // first we get all stored locations in the database
        TableJoin join = new TableJoin(AccountDBModel.class, LocationDBModel.class, LocationDBModel.getAccountIdAttribute());
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(AccountDBModel.getAccountIdAttribute(), userId)
        );
        List<LocationDBModel> locations = cd.complexSearch(join, search, LocationDBModel.class);

        Map<Integer, Location> out = new HashMap<>();
        for (LocationDBModel dbLocation : locations) {
            Location loc = toDomainModel(dbLocation, dac, cache);
            out.put(dbLocation.getId(), loc);
        }
        return out;
    }

    /**
     * Convert a location database model to an application domain model. Will handle the address and
     * relevant event types of the location.
     *
     * @param dbLocation id of the location
     * @param dac Abstraction of a connection to the database.
     * @param cache Access to cached objects
     * @return Application domain model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No address or eventType was found with the id included in the
     * location.
     */
    public Location toDomainModel(LocationDBModel dbLocation, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        Address address = addressMapper.getAddress(dbLocation.getAddressId(), dac, cache);
        Map<Integer, EventType> relevantEventtypes
                = eventtypeMapper.getLocationEventtypes(dbLocation.getId(), dac, cache);
        return new Location(
                address,
                dbLocation.getName(),
                dbLocation.getRadius(),
                !dbLocation.getActive(),
                relevantEventtypes,
                new ArrayList<>() // TODO placeholder for services
        );
    }

}

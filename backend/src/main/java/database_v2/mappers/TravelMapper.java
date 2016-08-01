package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.TableJoin;
import database_v2.models.relational.AccountDBModel;
import database_v2.models.relational.TravelDBModel;
import database_v2.searchTerms.SearchTerm;
import java.sql.Time;
import java.util.*;
import models.address.Address;
import models.Route;
import models.Travel;
import models.repetition.RepetitionWeek;

/**
 * This class provides some static functions to do the mapping between the application and database
 * model from a Travel.
 */
public class TravelMapper {

    private final AddressMapper addressMapper;
    private final RouteMapper routeMapper;

    /**
     * create a new travelMapper
     */
    public TravelMapper() {
        addressMapper = new AddressMapper();
        routeMapper = new RouteMapper();
    }

    /**
     * only for testing reasons.
     *
     * @param addressMapper a mapper for addresses
     * @param routeMapper a mapper for routes.
     */
    public TravelMapper(AddressMapper addressMapper, RouteMapper routeMapper) {
        this.addressMapper = addressMapper;
        this.routeMapper = routeMapper;
    }

    /**
     * Get all the travels from a user as they are stored in the database. This results in a Map
     * that maps id's to their Travel application model.
     *
     * @param userId The id of the user of whom the travels are fetched.
     * @param dac An abstraction of a connection
     * @param cache Access to all cached data
     * @return Map that maps id's to their Travel application model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No user with that id was found.
     */
    public Map<Integer, Travel> getUserTravels(int userId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        CRUDdao cd = dac.getCRUDdao();
        TableJoin join = new TableJoin(AccountDBModel.class, TravelDBModel.class, TravelDBModel.getAccountIdAttribute());
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(AccountDBModel.getAccountIdAttribute(), userId)
        );

        List<TravelDBModel> dbTravels = cd.complexSearch(join, search, TravelDBModel.class);

        Map<Integer, Travel> out = new HashMap<>();
        for (TravelDBModel dbTravel : dbTravels) {
            out.put(
                    dbTravel.getId(),
                    toDomainModel(dbTravel, true, dac, cache)
            );
        }

        return out;
    }

    /**
     * Convert a travel db model to a domain model. Will handle all dependencies of the travel.
     *
     * @param dbTravel The db model to be converted
     * @param fillInRoutes indicates is the route dependencies must be handled or not
     * @param dac Abstraction of a connection to the database.
     * @param cache Access to cached items
     * @return Converted Travel domain model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException A dependency id was not found of the travel.
     */
    public Travel toDomainModel(TravelDBModel dbTravel, boolean fillInRoutes, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        Address beginpoint = addressMapper.getAddress(dbTravel.getStartpoint(), dac, cache);
        Address endpoint = addressMapper.getAddress(dbTravel.getEndpoint(), dac, cache);

        Map<Integer, Route> routes;
        if (fillInRoutes) {
            routes = routeMapper.getTravelRoutes(dbTravel.getId(), dac, cache);
        } else {
            routes = new HashMap<>();
        }

        RepetitionWeek rep = new RepetitionWeek();
        if (dbTravel.getMon()) {
            rep.setMon(true);
        }
        if (dbTravel.getTue()) {
            rep.setTue(true);
        }
        if (dbTravel.getWed()) {
            rep.setWed(true);
        }
        if (dbTravel.getThu()) {
            rep.setThu(true);
        }
        if (dbTravel.getFri()) {
            rep.setFri(true);
        }
        if (dbTravel.getSat()) {
            rep.setSat(true);
        }
        if (dbTravel.getSun()) {
            rep.setSun(true);
        }

        Travel travel = new Travel(
                dbTravel.getName(),
                dbTravel.getBeginTime().toLocalTime(),
                dbTravel.getEndTime().toLocalTime(),
                dbTravel.getIsArrivalTime(),
                beginpoint,
                endpoint,
                Arrays.asList(rep),
                routes,
                new ArrayList<>() // PLACEHOLDER services
        );
        return travel;
    }

    /**
     * Convert an application model of a travel to a db model.
     *
     * @param userId The id of the user to which this travel belongs
     * @param startId The id of the begin address
     * @param endId the id of the end address
     * @param travel The travel to be converted
     * @return The converted travel db model.
     */
    public TravelDBModel toDBModel(int userId, int startId, int endId, Travel travel) {
        boolean[] allWeek = ((RepetitionWeek) travel.getRecurring().get(0)).getAllWeek();
        TravelDBModel out = new TravelDBModel(
                userId,
                startId,
                endId,
                travel.getName(),
                Time.valueOf(travel.getBeginDate()),
                Time.valueOf(travel.getEndDate()),
                travel.isArrivalTime(),
                allWeek[0], // mon
                allWeek[1], // tue
                allWeek[2], // wed
                allWeek[3], // thu
                allWeek[4], // fri
                allWeek[5], // sat
                allWeek[6] // sun
        );

        return out;
    }
}

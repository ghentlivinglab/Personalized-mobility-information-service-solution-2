package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.CityDBModel;
import database_v2.searchTerms.SearchTerm;
import java.util.Arrays;
import java.util.List;
import models.address.City;
import models.exceptions.InvalidCountryCodeException;
import org.javatuples.Pair;

/**
 * This class offers some static functions to do the mapping between a application and db model of a
 * City.
 */
public class CityMapper {

    /**
     * Fetch a city application model based upon its id. This will first check the cache before
     * consulting the database itself. The cache will be updated on cache misses
     *
     * @param cityId The id of the city to be fetched
     * @param dac A database connection in the form of a DataAccessContext
     * @param cache The cache that must be consulted first and that will be updated on cache misses
     * @return The city application model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No city was found with this id.
     */
    public City getCity(int cityId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        City cached = cache.getCityIfPresent(cityId);
        if (cached != null) {
            return cached;
        }

        CRUDdao cd = dac.getCRUDdao();
        CityDBModel dbCity = cd.read(cityId, CityDBModel.class);
        City out;
        try {
            out = new City(
                    dbCity.getCity(),
                    dbCity.getPostalCode(),
                    dbCity.getCountry()
            );
        } catch (InvalidCountryCodeException ex) {
            throw new DataAccessException(ex);
        }
        // add the fetched city to the cache before proceeding
        cache.addCity(dbCity.getId(), out);
        return out;
    }

    /**
     * Use this function to get a valid city application model that is consistent with the
     * underlying database structure. Lookup is done based upon the combination of postal code and
     * country code. If the database contains this combination, a city application model is returned
     * with the data as it stored in the database. If no such combination was found, then the
     * database will be completed with the data as it is stored in the parameter city application
     * model.
     * <p>
     * This function will update the cache.
     *
     * @param toValidate The city application model that will be validated
     * @param dac An open connection to the database
     * @param cache This cache will be consulted before going to the database.
     * @return A valid City application model
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Pair<Integer, City> validateCity(City toValidate, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {
        CRUDdao cd = dac.getCRUDdao();

        // STEP 1: we look for the city; for this we use the cache first
        City cached = cache.getCityIfPresent(
                toValidate.getPostalCode(),
                toValidate.getCountry()
        );

        // cache hit occured
        if (cached != null) {
            return new Pair<>(cache.getCityIdIfPresent(cached), cached);
        }

        // create a search operation on postal code and country code
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(CityDBModel.getPostalCodeAttribute(), toValidate.getPostalCode()),
                new SimpleSearchTerm(CityDBModel.getCountryAttribute(), toValidate.getCountry())
        );

        // look for a city matching these searchterms
        List<CityDBModel> foundCity = cd.simpleSearch(CityDBModel.class, search);

        CityDBModel validDBCity;
        // in the unlikely case that no city was found we will add a new city
        // containing the given data.
        if (foundCity.size() < 1) {
            validDBCity = new CityDBModel(
                    toValidate.getCityName(),
                    toValidate.getPostalCode(),
                    toValidate.getCountry()
            );
            try {
                cd.create(validDBCity);
            } catch (AlreadyExistsException | ForeignKeyNotFoundException ex) {
                // this indicates programming error
                throw new RuntimeException(ex);
            }
        } else {
            validDBCity = foundCity.get(0);
        }

        // convert the dbModel to a application model
        City out;
        try {
            out = new City(validDBCity.getCity(), validDBCity.getPostalCode(), validDBCity.getCountry());
        } catch (InvalidCountryCodeException ex) {
            throw new DataAccessException(ex);
        }

        // add the city to the cache before returning it.
        cache.addCity(validDBCity.getId(), out);
        return new Pair<>(validDBCity.getId(), out);
    }
}

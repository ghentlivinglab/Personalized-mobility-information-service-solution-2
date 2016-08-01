package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.AddressDBModel;
import database_v2.models.relational.StreetDBModel;
import database_v2.searchTerms.SearchTerm;
import java.util.Arrays;
import java.util.List;
import models.address.Address;
import models.address.City;
import models.Coordinate;
import models.address.Street;
import org.javatuples.Pair;

/**
 * This class offers some static functions to do the mapping between an application and db model of
 * an Address.
 */
public class AddressMapper {

    private final CityMapper cityMapper;

    /**
     * Create a new AddressMapper.
     */
    public AddressMapper() {
        cityMapper = new CityMapper();
    }

    /**
     * ONLY FOR TESTING REASONS, DO NOT USE IN PRODUCTION CODE
     *
     * @param cityMapper mapper for cities
     */
    public AddressMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    /**
     * Get an application model of an address with all the data filled in from the database. All
     * dependencies are handle (for streets and cities).
     *
     * @param addressId The id of the address to be fetched from the database
     * @param dac An abstraction of an open connection
     * @param cache All cached data
     * @return An filled in application model, consistent with the database.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No address was found with this id
     */
    public Address getAddress(int addressId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        CRUDdao cd = dac.getCRUDdao();

        // we begin with reading all the plain data from the database
        AddressDBModel dbAddress = cd.read(addressId, AddressDBModel.class);
        StreetDBModel dbStreet = cd.read(dbAddress.getStreetId(), StreetDBModel.class);

        // for city we use the CityMapper, which will look in the cache first
        City city = cityMapper.getCity(dbStreet.getCityId(), dac, cache);
        Street outStreet = new Street(dbStreet.getName(), city);
        Coordinate coord = new Coordinate(dbAddress.getLatitude(), dbAddress.getLongitude());

        // at the moment, there is no extra column to store house number and box
        // seperately. So I just put one string with ; as delimiter in house number
        String[] parsedHouseNumber = dbAddress.getHousenumber().split(";");
        int houseNumber = Integer.parseInt(parsedHouseNumber[0]);
        // when no box name is specified, there will be only one element in parsedHouseNumber
        String box = parsedHouseNumber.length > 1 ? parsedHouseNumber[1] : null;
        Address out = new Address(outStreet, houseNumber, box, coord);
        return out;
    }

    /**
     * Use this function to get a valid address application model that is consistent with the
     * underlying database structure. This function will check if the database already contains the
     * address, the street and the city. All missing data will be stored
     *
     * @param address The address to be validated
     * @param dac The connection to the database
     * @param cache This cache will be used before access the database itself
     * @return A pair containing the id and the corresponding valid application address model
     * @throws DataAccessException
     */
    public Pair<Integer, Address> validateAddress(Address address, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {

        CRUDdao cd = dac.getCRUDdao();

        // An address has 3 components to check: the city, the street and the address itself
        // PART 1: check the city
        Pair<Integer, City> validCityPair = cityMapper.validateCity(address.getStreet().getCity(), dac, cache);
        int validCityId = validCityPair.getValue0();
        City validCity = validCityPair.getValue1();

        // PART 2: check the street
        // we will attempt to add a new street. If this triggers an AlreadyExistsException
        // we will fetch the data from the database.
        StreetDBModel validDBStreet = new StreetDBModel(validCityId, address.getStreet().getName());
        try {
            cd.create(validDBStreet);
        } catch (AlreadyExistsException ex) {
            // now we look for the existing street
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(StreetDBModel.getCityIdAttribute(), validCityId),
                    new SimpleSearchTerm(StreetDBModel.getNameAttribute(), address.getStreet().getName())
            );
            List<StreetDBModel> foundStreets = cd.simpleSearch(StreetDBModel.class, search);
            if (foundStreets.isEmpty()) {
                throw new RuntimeException("this list should not be empty after an AlreadyExistsException");
            }
            validDBStreet = foundStreets.get(0);
        } catch (ForeignKeyNotFoundException ex) {
            // program error
            throw new RuntimeException(ex);
        }
        Street validStreet = new Street(validDBStreet.getName(), validCity);

        // PART 3: check the address
        // We accomplish this on the exact same way as we did in part 2: we try to add
        // the new address and is it already exists, we fetch the data from the database.
        String houseNumber = "" + address.getHouseNumber();
        if (address.getBox() != null) {
            houseNumber += ";" + address.getBox();
        }
        AddressDBModel validDBAddress = new AddressDBModel(
                validDBStreet.getId(),
                houseNumber,
                address.getCoordinates().getLat(),
                address.getCoordinates().getLon(),
                address.getCoordinates().getX(),
                address.getCoordinates().getY()
        );
        try {
            cd.create(validDBAddress);
        } catch (AlreadyExistsException ex) {
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(AddressDBModel.getStreetIdAttribute(), validDBStreet.getId()),
                    new SimpleSearchTerm(AddressDBModel.getHousenumberAttribute(), houseNumber)
            );
            List<AddressDBModel> foundAddresses = cd.simpleSearch(AddressDBModel.class, search);
            if (foundAddresses.isEmpty()) {
                throw new RuntimeException("this list should not be empty after an AlreadyExistsException");
            }
            validDBAddress = foundAddresses.get(0);
        } catch (ForeignKeyNotFoundException ex) {
            // program error
            throw new RuntimeException(ex);
        }

        // bring it all together and return the valid Address application model
        String[] parsedHouseNumber = validDBAddress.getHousenumber().split(";");
        int number = Integer.parseInt(parsedHouseNumber[0]);
        String box = parsedHouseNumber.length > 1 ? parsedHouseNumber[1] : null;
        Address validAddress = new Address(
                validStreet,
                number,
                box,
                new Coordinate(validDBAddress.getLatitude(), validDBAddress.getLongitude())
        );

        return new Pair<>(validDBAddress.getId(), validAddress);
    }

}

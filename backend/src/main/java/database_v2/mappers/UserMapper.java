package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DatabaseCache;
import database_v2.controlLayer.DataAccessContext;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.AccountDBModel;
import database_v2.searchTerms.SearchTerm;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.mail.internet.AddressException;
import models.Location;
import models.Travel;
import models.exceptions.InvalidPasswordException;
import models.users.Password;
import models.users.User;
import org.javatuples.Pair;

/**
 * This class provides the userCache the means to fetch a user from the database when a cache miss
 * occurs.
 */
public class UserMapper {

    private final LocationMapper locationMapper;
    private final TravelMapper travelMapper;

    /**
     * create a new mapper
     */
    public UserMapper() {
        locationMapper = new LocationMapper();
        travelMapper = new TravelMapper();
    }

    /**
     * only for testing reasons
     *
     * @param locationMapper mapper for locations
     * @param travelMapper mapper for travels
     */
    public UserMapper(LocationMapper locationMapper, TravelMapper travelMapper) {
        this.locationMapper = locationMapper;
        this.travelMapper = travelMapper;
    }

    /**
     * Get a user and all it's dependencies from the database.
     *
     * @param userId The id of the user to be fetched.
     * @param dac Abstraction of a connection to the database.
     * @param cache Access to all cached items
     * @return A correctly filled in user domain model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException The user or one of its dependencies was not found.
     */
    public User getUser(int userId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        User cached = cache.getUserIfPresent(userId);
        if (cached != null) {
            return cached;
        }

        CRUDdao cd = dac.getCRUDdao();
        // first we get the account object itself
        AccountDBModel acc = cd.read(userId, AccountDBModel.class);

        // convert the db model to a domain model and fill in all dependencies
        User out = toDomainModel(acc, dac, cache);

        // finally we add the new user to the database.
        cache.addUser(acc.getId(), out);
        return out;
    }

    /**
     * Get a user and all it's dependencies from the database.
     *
     * @param email The email of the user to be fetched.
     * @param dac Abstraction of a connection to the database.
     * @param cache Access to all cached items
     * @return A correctly filled in user domain model
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException The user or one of its dependencies was not found.
     */
    public Pair<Integer, User> getUser(String email, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {
        Pair<Integer, User> cached = cache.getUserIfPresent(email);
        if (cached != null) {
            return cached;
        }

        CRUDdao cd = dac.getCRUDdao();

        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(AccountDBModel.getEmailAttribute(), email)
        );

        List<AccountDBModel> foundAccounts = cd.simpleSearch(AccountDBModel.class, search);

        if (foundAccounts.isEmpty()) {
            throw new RecordNotFoundException("no users found with that email");
        }
        AccountDBModel acc = foundAccounts.get(0);

        // convert the db model to a filled in application model.
        User out = toDomainModel(acc, dac, cache);

        // add it the user to the cache
        cache.addUser(acc.getId(), out);
        return new Pair<>(acc.getId(), out);
    }

    /**
     * Convert a db account model to an application model. All dependencies (like travels and
     * locations) are handled and correctly filed in.
     *
     * @param acc The account db model to be converted.
     * @param dac An abstraction of an open connection to the database.
     * @param cache Access to all cached data.
     * @return The converted user domain model, with everything filled in.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException Some primary key was pointing to an non existing record.
     */
    private User toDomainModel(AccountDBModel acc, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException, RecordNotFoundException {

        int userId = acc.getId();
        // we get the locations from this user:
        Map<Integer, Location> locations = locationMapper.getUserLocations(userId, dac, cache);

        // then we get the travels of the user
        Map<Integer, Travel> travels = travelMapper.getUserTravels(userId, dac, cache);

        User out;
        try {
            out = new User(
                    acc.getFirstname(),
                    acc.getLastname(),
                    new Password(acc.getPassword(), acc.getSalt()),
                    acc.getEmail(),
                    acc.getEmailValidated(),
                    locations,
                    travels,
                    acc.getMuteNotifications(),
                    acc.getRefreshToken(),
                    acc.getIsOperator(),
                    acc.getIsAdmin(),
                    acc.getPushToken()
            );
            // validate the email of the user
            if (acc.getEmailValidated()) {
                out.getEmail().validate();
            }
            return out;
        } catch (InvalidPasswordException | AddressException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Convert a user application model to a db model.
     *
     * @param user The user application model to be converted.
     * @return The converted user db model
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public AccountDBModel toDBModel(User user)
            throws DataAccessException {

        AccountDBModel acc = new AccountDBModel(
                user.getEmailAsString(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword().getStringPassword(),
                user.isMuted(),
                user.getEmail().isValidated(),
                user.getRefreshToken(),
                user.getPassword().getSalt(),
                user.isOperator(),
                user.isAdmin(),
                user.getPushToken()
        );
        return acc;
    }

}

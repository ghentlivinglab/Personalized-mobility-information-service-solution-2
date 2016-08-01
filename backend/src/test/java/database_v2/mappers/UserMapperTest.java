package database_v2.mappers;

import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.AccountDBModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.services.Service;
import models.users.Password;
import models.users.User;
import org.javatuples.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

/**
 *
 */
public class UserMapperTest extends AbstractDBMapperTest {

    @Mock
    private final LocationMapper locationMapper;
    @Mock
    private final TravelMapper travelMapper;

    private final UserMapper userMapper;

    public UserMapperTest() throws Exception {
        super();
        locationMapper = mock(LocationMapper.class);
        travelMapper = mock(TravelMapper.class);
        userMapper = new UserMapper(locationMapper, travelMapper);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class UserMapper.
     */
    @Test
    public void testGetUserById() throws Exception {
        System.out.println("load");
        int userId = 1;

        AccountDBModel toLoad = getTestDbAccount();
        toLoad.setId(userId);

        // mock the account you get from the database
        when(cd.read(userId, AccountDBModel.class)).thenReturn(toLoad);

        // mock the LocationMapper of an UserMapper
        when(locationMapper.getUserLocations(userId, dac, cache)).thenReturn(new HashMap<>());

        // mock the travelMapper of the UserMapper
        when(travelMapper.getUserTravels(userId, dac, cache)).thenReturn(new HashMap<>());

        // test a user with everything filled in
        User user = userMapper.getUser(userId, dac, cache);
        assertEquals(user.getEmail().toString(), toLoad.getEmail());
        assertEquals(user.getFirstName(), toLoad.getFirstname());
        assertEquals(user.getLastName(), toLoad.getLastname());
        assertEquals(user.getPassword().getStringPassword(), toLoad.getPassword());
        assertEquals(user.isMuted(), toLoad.getMuteNotifications());

        // test a user with bare bones info
        Password pass = new Password("default321");
        toLoad = new AccountDBModel("bare@bones.com", pass.getStringPassword(), pass.getSalt());
        toLoad.setId(2);
        when(cd.read(2, AccountDBModel.class)).thenReturn(toLoad);
        user = userMapper.getUser(2, dac, cache);
        assertEquals(user.getEmail().toString(), toLoad.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertEquals(user.getPassword().getStringPassword(), toLoad.getPassword());
        assertEquals(user.isMuted(), toLoad.getMuteNotifications());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        // PATH 1: user was cached
        AccountDBModel toLoad = getTestDbAccount();
        toLoad.setAccountId(1);

        // the user that was cached
        Password pass = new Password("Default123");
        User cachedUser = new User("cached", null, "Default123", "cached@test.com", false);
        when(cache.getUserIfPresent(toLoad.getEmail())).thenReturn(new Pair<>(1, cachedUser));

        // execute the method
        Pair<Integer, User> gotUser = userMapper.getUser(toLoad.getEmail(), dac, cache);
        assertEquals(1, (int) gotUser.getValue0());
        assertEquals(cachedUser, gotUser.getValue1());

        // PATH 2: the user is not cached
        // mock the list of one bd account with the given email
        List<AccountDBModel> queryResult = new ArrayList<>();
        AccountDBModel nonCached = new AccountDBModel("not_cached@t.c", pass.getStringPassword(), pass.getSalt());
        queryResult.add(nonCached);
        nonCached.setAccountId(2);
        when(cd.simpleSearch(eq(AccountDBModel.class), anyList())).thenReturn(queryResult);

        gotUser = userMapper.getUser("not_cached@t.c", dac, cache);

        // make sure the code adds the new user to the cache
        verify(cache, times(1)).addUser(eq(2), any(User.class));

        // do some checks
        assertEquals(nonCached.getAccountId(), gotUser.getValue0());
        assertEquals(nonCached.getEmail(), gotUser.getValue1().getEmailAsString());
        assertTrue(gotUser.getValue1().getPassword().checkSamePassword("Default123"));
        assertNull(gotUser.getValue1().getFirstName());
        assertNull(gotUser.getValue1().getLastName());
        assertFalse(gotUser.getValue1().getEmail().isValidated());

        // PATH 3: check non existing email
        List<AccountDBModel> emptyQueryResult = new ArrayList<>();
        when(cd.simpleSearch(eq(AccountDBModel.class), anyList())).thenReturn(emptyQueryResult);
        boolean thrown = false;
        try {
            userMapper.getUser("non_existing@t.c", dac, cache);
        } catch (RecordNotFoundException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    /**
     * Test of toDBModel method, of class UserMapper.
     */
    @Test
    public void testToDBModel() throws Exception {
        System.out.println("toDBModel");

        Map<String, Service> services = new HashMap<>();

        // first we test a fully filled in user
        User user = new User("test_vn", "test_ln", "Default123", "t@t.c", false);

        AccountDBModel dbModel = userMapper.toDBModel(user);
        assertEquals(user.getFirstName(), dbModel.getFirstname());
        assertEquals(user.getLastName(), dbModel.getLastname());
        assertEquals(user.getEmail().toString(), dbModel.getEmail());
        assertEquals(user.getPassword().getStringPassword(), dbModel.getPassword());
    }
    
    @Test
    public void testDefaultConstructor() {
        UserMapper instance = new UserMapper();
        assertNotNull(instance);
    }

}

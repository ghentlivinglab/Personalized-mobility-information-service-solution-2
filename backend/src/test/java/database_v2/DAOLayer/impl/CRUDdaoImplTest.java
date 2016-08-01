package database_v2.DAOLayer.impl;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DataAccessProvider;
import database_v2.controlLayer.impl.DataAccessProviderImpl;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.AccountDBModel;
import database_v2.models.relational.AddressDBModel;
import database_v2.models.relational.CityDBModel;
import database_v2.models.relational.EventtypeDBModel;
import database_v2.models.relational.LocationDBModel;
import database_v2.models.relational.RouteDBModel;
import database_v2.models.relational.RouteEventtypeDBModel;
import database_v2.models.relational.StreetDBModel;
import database_v2.models.relational.TravelDBModel;
import database_v2.searchTerms.SearchTerm;
import database_v2.searchTerms.SimpleSearchTerm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Coordinate;
import models.users.Password;
import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import database_v2.models.TableJoin;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.LoggerFactory;

/**
 */
public class CRUDdaoImplTest {

    private final DataAccessProvider dap;
    private final IDatabaseTester tester;
    private final IDatabaseConnection testConnection;
    private Connection conn;

    public CRUDdaoImplTest() throws Exception {
        // create a new DataAccessProvider
        dap = new DataAccessProviderImpl();

        // testConnection is a specific dbUnit wrapper for a connection. We create a single
        // tester object and testConnection for the entire test suite.
        tester = new DataSourceDatabaseTester(dap.getDataSource(), "public");
        testConnection = tester.getConnection();
        testConnection.getConfig().setProperty("http://www.dbunit.org/properties/datatypeFactory", new PostgresqlDataTypeFactory());

        // only for development (to create new xml dump, when there are big changes
        // in the database. Can also be used to update the schema of the testdatabase.
        // generateNewXMLDump();
        // temper dbUnits logger :p
        Logger logger = (Logger) LoggerFactory.getLogger("org.dbunit");
        logger.setLevel(Level.ERROR);
    }

    @Before
    public void setUp() throws Exception {
        // set up a new connection to the database. Must be closed after each
        // test in teardown
        conn = dap.getDataSource().getConnection();

        // load the testdata back in the database
        IDataSet dataset = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/testdata.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(testConnection, dataset);

        // and set the sequence numbering correct
        resetSequence();
    }

    @After
    public void tearDown() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    private void executeSqlScript(Connection conn, File script) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(script))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            Statement stm = conn.createStatement();
            stm.execute(sb.toString());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This function gets all sequences from the database and updates there value according to the
     * max id value of its column. This is needed because the data dumps use hardcoded id's and then
     * the auto increment in postgresql is not used.
     */
    private void resetSequence() {
        try {
            // Select all sequence names ...
            Statement seqStmt = conn.createStatement();
            ResultSet rs = seqStmt.executeQuery("SELECT c.relname FROM pg_class c WHERE c.relkind = 'S';");

            // ... and update the sequence to match max(id)+1.
            while (rs.next()) {
                String sequence = rs.getString("relname");
                String table = sequence.substring(0, sequence.length() - 6);
                Statement updStmt = conn.createStatement();
                String test = "SELECT SETVAL('" + sequence + "', (SELECT MAX(" + table + "id) FROM " + table + "));";
                updStmt.executeQuery(test);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void checkDatabaseState(String expectedDataLocation) throws Exception {
        // Fetch database data
        IDataSet databaseDataSet = testConnection.createDataSet();

        // Load expected data from an XML dataset
        File expectedDataFile = new File(getClass().getResource(expectedDataLocation).toURI());
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(expectedDataFile);

        // Assert actual database table match expected table
        Assertion.assertEqualsIgnoreCols(expectedDataSet, databaseDataSet, "account",
                new String[]{"salt", "created_at", "password"});
    }

    /**
     * This function is purely for development reasons. Don't use it in the final test suite. I use
     * this to create a new flat XML dump, based upon an updated DDL script en SQL test dump.
     *
     * @throws Exception
     */
    private void generateNewXMLDump() throws Exception {
        try (Connection conn = dap.getDataSource().getConnection()) {
            // completely clears database and creates all needed structures (table, 
            // sequences, ...)
            File ddlScript = new File(getClass().getResource("/create_script.sql").toURI());
            executeSqlScript(conn, ddlScript);

            // insert the fixed testdata to be used before each test
            File testDataScript = new File(getClass().getResource("/testdata.sql").toURI());
            executeSqlScript(conn, testDataScript);
        }

        // this filter automatically determines the sequence in which you need 
        // to delete the tables so there are no foreign key conflicts
        ITableFilter filter = new DatabaseSequenceFilter(testConnection);
        IDataSet fullDataSet = new FilteredDataSet(filter, testConnection.createDataSet());
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
    }

    /**
     * Test of create method, of class CRUDdaoImpl.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // create an account
            Password pass = new Password("Defaul123");
            AccountDBModel acc = new AccountDBModel(
                    "test@test.com", // email
                    "test_voornaam", // first name
                    "test_achternaam", // last name
                    "Default123", // dummy pass
                    Boolean.FALSE, // mute
                    Boolean.TRUE, // email valid
                    "REFRESH", // refresh token
                    pass.getSalt(), // salt
                    Boolean.FALSE, // is operator
                    Boolean.FALSE, // is admin
                    "pushToken"
            );
            cd.create(acc);

            // create a poi for this account:
            CityDBModel city1 = new CityDBModel("poistad", "1234", "BE");
            cd.create(city1);
            StreetDBModel street1 = new StreetDBModel(city1.getCityId(), "poistraat");
            cd.create(street1);
            Coordinate coord = new Coordinate(90.0, 90.0);
            AddressDBModel address1
                    = new AddressDBModel(street1.getId(), "1", 90.0, 90.0, coord.getX(), coord.getY());
            cd.create(address1);
            LocationDBModel poi = new LocationDBModel(acc.getId(), address1.getId(), "poi", 2000, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
            cd.create(poi);

            // create a new route for this account
            CityDBModel city2 = new CityDBModel("beginstad", "2345", "BE");
            cd.create(city2);
            CityDBModel city3 = new CityDBModel("eindstad", "3456", "BE");
            cd.create(city3);
            StreetDBModel street2 = new StreetDBModel(city2.getId(), "beginstraat");
            cd.create(street2);
            StreetDBModel street3 = new StreetDBModel(city3.getId(), "eindstraat");
            cd.create(street3);
            AddressDBModel address2
                    = new AddressDBModel(street2.getId(), "1", 90.0, 90.0, coord.getX(), coord.getY());
            cd.create(address2);
            AddressDBModel address3 =
                    new AddressDBModel(street3.getId(), "1", 90.0, 90.0, coord.getX(), coord.getY());
            cd.create(address3);
            TravelDBModel travel = new TravelDBModel(acc.getId(), address2.getId(), address3.getId(), "testtravel",
                    Time.valueOf(LocalTime.parse("08:00")),
                    Time.valueOf(LocalTime.parse("08:30")),
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                    Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
            cd.create(travel);
            EventtypeDBModel evType = new EventtypeDBModel("TEST", conn.createArrayOf("text", new String[]{"car"}));
            cd.create(evType);
            RouteDBModel route = new RouteDBModel(
                    travel.getId(),
                    conn.createArrayOf("float8", new double[][]{{90.0, 90.0}}),
                    conn.createArrayOf("float8", new double[][]{{90.0, 90.0}}),
                    "car",
                    Boolean.TRUE, Boolean.FALSE, Boolean.FALSE
            );
            cd.create(route);
            RouteEventtypeDBModel route_ev = new RouteEventtypeDBModel(route.getId(), evType.getId());
            cd.create(route_ev);

            // PART 2: AlreadyExistsException
            AccountDBModel existingUser =
                    new AccountDBModel("test@test.com", pass.getStringPassword(), pass.getSalt());
            boolean thrown = false;
            try {
                cd.create(existingUser);
            } catch (AlreadyExistsException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        }

        checkDatabaseState("/expected_create.xml");

    }

    /**
     * Test of read method, of class CRUDdaoImpl.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            AccountDBModel acc = cd.read(1, AccountDBModel.class);
            Password pass = new Password("Defaul123");
            AccountDBModel expAcc = new AccountDBModel(
                    "phenderson0@biglobe.ne.jp",
                    "Peter",
                    "Henderson",
                    "Default123", // dummy pass
                    Boolean.FALSE, // mute
                    Boolean.FALSE, // email valid
                    "REFRESH", // refresh token
                    pass.getSalt(), // salt
                    Boolean.FALSE, // is operator
                    Boolean.FALSE, // is admin
                    "pushToken"
            );
            expAcc.setId(1);
            expAcc.setPassword(acc.getPassword());
            expAcc.setSalt(acc.getSalt());
            assertEquals(expAcc, acc);

            CityDBModel city = cd.read(1, CityDBModel.class);
            CityDBModel expCity = new CityDBModel("Gent", "9000", "BE");
            expCity.setId(1);
            assertEquals(city, expCity);

            StreetDBModel street = cd.read(1, StreetDBModel.class);
            StreetDBModel expStreet = new StreetDBModel(1, "Jozef Plateaustraat");
            expStreet.setId(1);
            assertEquals(expStreet, street);

            AddressDBModel address = cd.read(1, AddressDBModel.class);
            Coordinate coord = new Coordinate(51.04615400, 3.72236000);
            AddressDBModel expAddress =
                    new AddressDBModel(1, "1", 51.04615400, 3.72236000, coord.getX(), coord.getY());
            expAddress.setId(1);
            assertEquals(expAddress, address);

            // PART 2: fetching non existing id
            boolean thrown = false;
            try {
                AccountDBModel nonExisting = cd.read(99, AccountDBModel.class);
            } catch (RecordNotFoundException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        }

        // the database should be exactly the same because we only used read operations
        checkDatabaseState("/testdata.xml");
    }

    /**
     * Test of update method, of class CRUDdaoImpl.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            
            Password pass = new Password("Defaul123");
            AccountDBModel acc = new AccountDBModel(
                    "test@test.com",
                    "testvoornaam",
                    "testachternaam",
                    "Default123", // dummy pass
                    Boolean.FALSE, // mute
                    Boolean.FALSE, // email valid
                    "REFRESH", // refresh token
                    pass.getSalt(), // salt
                    Boolean.FALSE, // is operator
                    Boolean.FALSE, // is admin
                    "pushToken"
            );
            acc.setId(1);
            cd.update(acc);

            CityDBModel city = new CityDBModel("test", "1234", "BE");
            city.setCityId(1);
            cd.update(city);

            StreetDBModel street = new StreetDBModel(1, "teststraat");
            street.setId(1);
            cd.update(street);

            Coordinate coord = new Coordinate(25.25, 26.26);
            AddressDBModel address =
                    new AddressDBModel(1, "1T", 25.25, 26.26, coord.getX(), coord.getY());
            address.setId(1);
            cd.update(address);

            // TODO additional tests
            // PART 2: AlreadyExistsException
            AccountDBModel acc2 =
                    new AccountDBModel("mpeterson1@cyberchimps.com", "Default123", pass.getSalt());
            acc2.setId(1);
            boolean thrown = false;
            try {
                cd.update(acc2);
            } catch (AlreadyExistsException ex) {
                thrown = true;
            }
            assertTrue(thrown);

            // PART 3: RecordNotFoundException
            AccountDBModel acc3 =
                    new AccountDBModel("nonexisting@non.exist", "Default123", pass.getSalt());
            acc3.setId(99);
            thrown = false;
            try {
                cd.update(acc3);
            } catch (RecordNotFoundException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        }
        checkDatabaseState("/expected_update.xml");

    }

    /**
     * Test of delete method, of class CRUDdaoImpl.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // delete user, all depending data should also be gone.
            cd.delete(1, AccountDBModel.class);

            // PART 2: deletion of non existing id
            boolean thrown = false;
            try {
                cd.delete(99, AccountDBModel.class);
            } catch (RecordNotFoundException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        }
        checkDatabaseState("/expected_delete.xml");
    }

    /**
     * Test of listAll method, of class CRUDdaoImpl.
     */
    @Test
    public void testListAll() throws Exception {
        System.out.println("listAll");

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<AccountDBModel> allAcc = cd.listAll(AccountDBModel.class);
            List<AccountDBModel> expAcc = new ArrayList<>();
            Password pass = new Password("Defaul123");
            expAcc.add(new AccountDBModel(
                    "phenderson0@biglobe.ne.jp",
                    "Peter",
                    "Henderson",
                    "Default123", // dummy pass
                    Boolean.FALSE, // mute
                    Boolean.FALSE, // email valid
                    "REFRESH", // refresh token
                    pass.getSalt(), // salt
                    Boolean.FALSE, // is operator
                    Boolean.FALSE, // is admin
                    "pushToken"
            ));
            expAcc.add(new AccountDBModel(
                    "mpeterson1@cyberchimps.com",
                    "Melissa",
                    "Peterson",
                    "Default123", // dummy pass
                    Boolean.TRUE, // mute
                    Boolean.TRUE, // email valid
                    "REFRESH", // refresh token
                    pass.getSalt(), // salt
                    Boolean.FALSE, // is operator
                    Boolean.FALSE, // is admin
                    "pushToken"
            ));
            assertTrue(allAcc.size() == expAcc.size());
            for (int i = 0; i < allAcc.size(); i++) {
                expAcc.get(i).setId(i + 1);
                expAcc.get(i).setSalt(allAcc.get(i).getSalt());
                assertEquals(expAcc.get(i), allAcc.get(i));
            }
        }

    }

    /**
     * Test of simpleSearch method, of class CRUDdaoImpl.
     */
    @Test
    public void testSimpleSearch() throws Exception {
        System.out.println("simpleSearch");

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            // get all users with muted notifications
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(AccountDBModel.getMuteNotificationsAttribute(), Boolean.TRUE)
            );
            List<AccountDBModel> acc = cd.simpleSearch(AccountDBModel.class, search);

            List<AccountDBModel> expAcc = new ArrayList<>();
            Password pass = new Password("default123");
            AccountDBModel exp = new AccountDBModel(
                    "mpeterson1@cyberchimps.com",
                    "Melissa",
                    "Peterson",
                    "Default123", // dummy pass
                    Boolean.TRUE, // mute
                    Boolean.TRUE, // email valid
                    "REFRESH", // refresh token
                    pass.getSalt(), // salt
                    Boolean.FALSE, // is operator
                    Boolean.FALSE, // is admin
                    "pushToken"
            );
            exp.setId(2);
            exp.setSalt(acc.get(0).getSalt());
            expAcc.add(exp);

            assertEquals(expAcc, acc);
        }
    }

    /**
     * Test of complexSearch method, of class CRUDdaoImpl.
     */
    @Test
    public void testComplexSearch() throws Exception {
        System.out.println("complexSearch");

        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            // seach all start cities of all routes of user 1;
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(AccountDBModel.getAccountIdAttribute(), 1)
            );

            TableJoin join = new TableJoin(AccountDBModel.class, TravelDBModel.class, TravelDBModel.getAccountIdAttribute());
            join = new TableJoin(join, AddressDBModel.class, TravelDBModel.getStartpointAttribute());
            join = new TableJoin(join, StreetDBModel.class, AddressDBModel.getStreetIdAttribute());
            join = new TableJoin(join, CityDBModel.class, StreetDBModel.getCityIdAttribute());

            Set<CityDBModel> cities = new HashSet<>(cd.complexSearch(join, search, CityDBModel.class));
            Set<CityDBModel> expCities = new HashSet<>();
            CityDBModel expCity = new CityDBModel("Gent", "9000", "BE");
            expCity.setId(1);
            expCities.add(expCity);
            expCity = new CityDBModel("Antwerpen", "2000", "BE");
            expCity.setId(3);
            expCities.add(expCity);

            assertEquals(expCities, cities);
        }

    }
    
    @Test
    public void testComplexDelete() throws Exception {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();

            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(AccountDBModel.getEmailAttribute(), "phenderson0@biglobe.ne.jp")
            );
            // delete user, all depending data should also be gone.
            cd.delete(AccountDBModel.class, search);

        }
        checkDatabaseState("/expected_delete.xml");
    }
}

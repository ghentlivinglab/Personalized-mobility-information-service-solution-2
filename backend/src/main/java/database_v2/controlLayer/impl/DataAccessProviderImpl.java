package database_v2.controlLayer.impl;

import backend.AppProperties;
import database_v2.exceptions.DataAccessException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DataAccessProvider;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.bson.Document;
import org.postgresql.ds.PGPoolingDataSource;

/**
 * A full implementation of the DataAccessProvider interface.
 */
public class DataAccessProviderImpl implements DataAccessProvider {
    
    private MongoClient mongoClient;
    private String mongoDBName;
    private DataSource sqlSource;

    /**
     * Create a new instance. This will user the passed PropertiesFile for all configuration.
     *
     */
    public DataAccessProviderImpl() {
        initDriver();
        initMongo();
    }
    
    @Override
    public DataAccessContext getDataAccessContext() {
        return new DataAccessContextImpl(this);
    }
    
    @Override
    public DataSource getDataSource() {
        return sqlSource;
    }

    /**
     * Return the wrapped sql connection to the underlying relational database.
     *
     * @return An the open, wrapped sql connection.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Connection getSQLConnection() throws DataAccessException {
        try {
            return getConnection();
        } catch (SQLException ex) {
            throw new DataAccessException("Could not create data access context", ex);
        }
    }

    /**
     * Get the wrapped connection to the NoSQL MongoDB.
     *
     * @return the wrapped connection to the NoSQL MongoDB
     */
    public MongoDatabase getMongoConnection() {
        return mongoClient.getDatabase(mongoDBName);
    }
    
    private void initDriver() {
        PGPoolingDataSource postgresSource = new PGPoolingDataSource();
        postgresSource.setServerName(
                AppProperties.instance().getProp(AppProperties.PROP_KEY.PGSQL_HOST));
        postgresSource.setPortNumber(Integer.parseInt(
                AppProperties.instance().getProp(AppProperties.PROP_KEY.PGSQL_POST)));
        postgresSource.setDatabaseName(
                AppProperties.instance().getProp(AppProperties.PROP_KEY.PGSQL_DB_NAME));
        postgresSource.setUser(
                AppProperties.instance().getProp(AppProperties.PROP_KEY.PGSQL_USER));
        postgresSource.setPassword(
                AppProperties.instance().getProp(AppProperties.PROP_KEY.PGSQL_PASS));
        sqlSource = postgresSource;
    }
    
    private void initMongo() {
        String host = AppProperties.instance().getProp(AppProperties.PROP_KEY.MONGO_HOST);
        int port = Integer.parseInt(
                AppProperties.instance().getProp(AppProperties.PROP_KEY.MONGO_PORT));
        mongoDBName = AppProperties.instance().getProp(AppProperties.PROP_KEY.MONGO_DB_NAME);
        mongoClient = new MongoClient(host, port);

        // do some first setup for unique events from waze
        MongoDatabase db = getMongoConnection();
        db.getCollection("events").createIndex(
                new Document("uuid", 1),
                new IndexOptions().unique(true).sparse(true)
        );
        
    }
    
    private Connection getConnection() throws SQLException {
        return sqlSource.getConnection();
    }
}

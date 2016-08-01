package database_v2.controlLayer.impl;

import database_v2.DAOLayer.impl.CRUDdaoImpl;
import database_v2.DAOLayer.CRUDdao;
import database_v2.exceptions.DataAccessException;
import com.mongodb.client.MongoDatabase;
import database_v2.DAOLayer.EventDAO;
import database_v2.DAOLayer.impl.EventDAOImpl;
import database_v2.controlLayer.DataAccessContext;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This implementation combines a connection to the postgreSQL database and the
 * MongoDB database.
 *
 */
public class DataAccessContextImpl implements DataAccessContext {

    private Connection connection = null;
    private MongoDatabase mongoDB = null;
    private final DataAccessProviderImpl dap;

    /**
     * Create new DataAccessContext. The dap that creates this object will pass
     * a self reference. This gives this connection the ability to create other
     * connections.
     * <p>
     * WARNING: try to avoid creating to many connections from within another
     * connection.
     *
     * @param dap A reference to the DataAccessProviderImpl that has created
     * this DataAccessContext.
     */
    public DataAccessContextImpl(DataAccessProviderImpl dap) {
        this.dap = dap;
    }

    private Connection initSQLConnection() throws DataAccessException {
        if (connection == null) {
            connection = dap.getSQLConnection();
        }
        return connection;
    }

    private MongoDatabase initMongoConnection() {
        if (mongoDB == null) {
            mongoDB = dap.getMongoConnection();
        }
        return mongoDB;
    }

    private CRUDdao crudDAO = null;

    @Override
    public CRUDdao getCRUDdao() throws DataAccessException {
        if (crudDAO == null) {
            crudDAO = new CRUDdaoImpl(initSQLConnection(), dap);
        }
        return crudDAO;
    }

    private EventDAO eventDAO = null;

    @Override
    public EventDAO getEventDAO() {
        if (eventDAO == null) {
            eventDAO = new EventDAOImpl(initMongoConnection(), dap);
        }
        return eventDAO;
    }

    @Override
    public Connection getSQLConnection() throws DataAccessException {
        return initSQLConnection();
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}

package database_v2.controlLayer;

import database_v2.DAOLayer.CRUDdao;
import database_v2.DAOLayer.EventDAO;
import database_v2.exceptions.DataAccessException;
import database_v2.models.CRUDModel;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The DataAccessContext is an abstraction of an open connection to a database.
 * Each logical batch of communication to the database should go over one
 * DataAccessContext object, after which the connection must be closed.
 */
public interface DataAccessContext extends AutoCloseable {

    /**
     * Returns an implementation of a CRUDdao. With this dao, every object that
     * implements the CRUDModel interface can be written to the relational
     * database.
     *
     * @see CRUDdao
     * @see CRUDModel
     * @return An instance of a CRUDdao.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     */
    public CRUDdao getCRUDdao() throws DataAccessException;

    /**
     * Returns an implementation of a eventDAO. With this dao all database
     * actions for an event are possible.
     *
     * @return An EventDAO for event related database operations.
     */
    public EventDAO getEventDAO();

    /**
     * Return the sqlConnection for which this class is a wrapper. This
     * connection can be used to accomplish other specific database tasks that
     * are not covered by the provided DAO's
     *
     * @return The wrapped Connection to the database.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     */
    public Connection getSQLConnection() throws DataAccessException;

    @Override
    public void close() throws SQLException;
}

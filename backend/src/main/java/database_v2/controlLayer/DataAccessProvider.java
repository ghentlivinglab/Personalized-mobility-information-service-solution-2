package database_v2.controlLayer;

import javax.sql.DataSource;

/**
 * This class is an abstraction of the database component of the program. All
 * actions that need the relational database or the NoSQL database require an
 * instance of this class. Ideally this class should be a Spring bean, and all
 * classes that need it should receive an instance via dependency injection.
 * <p>
 * This class should normally not be required outside the database component.
 * All other parts of the program should instead use a Database object because
 * there will be extra caching function there.
 */
public interface DataAccessProvider {

    /**
     * Get an abstraction of an open connection to a database. This can be used
     * both for relational database connections of NoSQL.
     * <p>
     * All wrapped connection are lazy created so no overhead takes place when
     * calling this function without using the DataAccessContext itself.
     *
     * @return A DataAccessContext object with open connections wrapped inside.
     */
    public DataAccessContext getDataAccessContext();

    /**
     * Get the sql DataSource wrapped by this class.
     *
     * @return The wrapped DataSource object.
     */
    public DataSource getDataSource();

}

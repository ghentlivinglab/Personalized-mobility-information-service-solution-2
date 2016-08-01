package database_v2.DAOLayer.impl;

import com.mongodb.client.MongoDatabase;
import database_v2.controlLayer.DataAccessProvider;

/**
 * Abstract class to setup everything needed to communicate with the MongoDB
 * database.
 */
public abstract class AbstractMongoDAO {

    /**
     * A Java binder class to perform tasks with a MongoDB database. The
     * connection should be made earlier so this object is ready to use.
     */
    protected MongoDatabase db;

    /**
     * A reference to the DataAccessProvider, an abstraction of the entire
     * database component.
     */
    protected DataAccessProvider dap;

    /**
     * set the MongoDatabase and DataAccessProvider of this abstract class.
     *
     * @param db The used database.
     * @param dap The interface to communicate with the database.
     */
    public AbstractMongoDAO(MongoDatabase db, DataAccessProvider dap) {
        this.db = db;
        this.dap = dap;
    }

    /**
     * Get the wrapped MongoDatabase.
     *
     * @return the wrapped MongoDatabase
     */
    public MongoDatabase getDb() {
        return db;
    }

    /**
     * Set the MongoDatabase to be wrapped.
     *
     * @param db MongoDatabase to be wrapped.
     */
    public void setDb(MongoDatabase db) {
        this.db = db;
    }

}

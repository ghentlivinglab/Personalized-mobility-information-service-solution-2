package database_v2.models;

import org.bson.Document;

/**
 * All models that need to be written to MongoDB need to implement this
 * interface.
 */
public interface MongoModel {

    /**
     * Convert this object and all his dependencies that need to be stored to a
     * Document. This form of serialization allows us to write the object
     * directly away.
     *
     * @return Document form of the object.
     */
    public Document toDocument();

}

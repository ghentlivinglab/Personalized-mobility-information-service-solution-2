package database_v2.DAOLayer;

import database_v2.models.TableJoin;
import database_v2.models.CRUDModel;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.searchTerms.SearchTerm;
import java.util.List;

/**
 * This DAO allows basic database operations (Create, Read, Update and Delete)
 * for all classes that implement CRUDModel. When a model follows the rules of
 * this interface and the database has appropriate tables, then this dao can be
 * used to perform these tasks.
 *
 * @see CRUDModel
 */
public interface CRUDdao {

    /**
     * Adds the model to the database. The id of the model object is filled in.
     *
     * @param model This CRUDModel object will be written to the database.
     * @throws DataAccessException General database error
     * @throws AlreadyExistsException If the model violates the unique
     * constraints of the table an AlreadyExistsException is thrown.
     * @throws ForeignKeyNotFoundException If the model contains a foreign key
     * that points to a non existing record
     */
    void create(CRUDModel model)
            throws DataAccessException, AlreadyExistsException, ForeignKeyNotFoundException;

    /**
     * Get the record with given id from the table represented by the given
     * CRUDModel.
     *
     * @param <T> The type of CRUDModel that you want to get.
     * @param id The id of the object to be fetched.
     * @param type The class object of the CRUDModel you want to fetch.
     * @return An object of the type requested type that belongs to the given
     * id.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     * @throws RecordNotFoundException When there is no record with this id
     */
    <T extends CRUDModel> T read(int id, Class<T> type)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Update the record belonging to the CRUDModel object with the new data.
     *
     * @param model
     * @throws DataAccessException
     * @throws AlreadyExistsException This exception is thrown when the updated
     * data violates a unique constraint from the table.
     * @throws RecordNotFoundException This exception is thrown when the id of
     * the model is not found in the database.
     * @throws ForeignKeyNotFoundException If the model contains a foreign key
     * that points to a non existing record
     */
    void update(CRUDModel model)
            throws DataAccessException, AlreadyExistsException, RecordNotFoundException, ForeignKeyNotFoundException;

    /**
     * Deletes the record belonging to this model from the database. The id of
     * the model is set to null to indicate the erasure.
     *
     * @param model The model that represents the record to be deleted.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     * @throws RecordNotFoundException Is thrown when the id of the model is not
     * found in the database.
     */
    void delete(CRUDModel model) throws DataAccessException, RecordNotFoundException;

    /**
     * Delete the record with given id from the table modeled by the type class.
     *
     * @param <T> The type of CRUDModel representing the table from where you
     * want to delete.
     * @param id The id of the record to be deleted.
     * @param type The class object of the CRUDModel that represents the table
     * from where you want to delete.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     * @throws RecordNotFoundException When there is no record in the given
     * table with this id.
     */
    <T extends CRUDModel> void delete(int id, Class<T> type)
            throws DataAccessException, RecordNotFoundException;

    /**
     * Delete all records from a table that match a certain query. The type
     * tells from which table you want to delete and the query list of
     * SearchTerms describes the query. All attributes from the query must be
     * from the same table then the one you are deleting from.
     *
     * @param <T> The type of CRUDModel, describing the table from which you
     * want to delete.
     * @param type The type of CRUDModel, describing the table from which you
     * want to delete.
     * @param query A list of search terms, all records matching these will be
     * deleted.
     * @return The number of deleted records.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     */
    <T extends CRUDModel> int delete(Class<T> type, List<SearchTerm> query)
            throws DataAccessException;

    /**
     * Return all records from the given table.
     *
     * @param <T> The type of CRUDModel representing the table you want to
     * fetch.
     * @param type The class object of the CRUDModel that represents the table
     * from where you want to delete.
     * @return An object of the requested CRUDModel containing the data from the
     * record.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     */
    <T extends CRUDModel> List<T> listAll(Class<T> type) throws DataAccessException;

    /**
     * Perform a search on a specific table. Pass a list of the search terms on
     * which you want to query. All the passed search terms must have attributes
     * part of the table you are querying or this function will fail.
     *
     * @param <T> The type of CRUDModel representing the table you want to
     * fetch.
     * @param type The class object of the CRUDModel that represents the table
     * you want to query.
     * @param searchTerms This list contains all search terms you want to apply.
     * @return A list of CRUDModel objects that match the given search terms.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     */
    <T extends CRUDModel> List<T> simpleSearch(Class<T> type, List<SearchTerm> searchTerms)
            throws DataAccessException;

    /**
     * The complexSearch is meant to be used to query joined tables. First you
     * need to describe the join operation with a TableJoin. The seachTerms may
     * apply on any column present in the joined tables. The returnType
     * indicates in which object you are interested (usually this is the last
     * joined table, but this is not required.)
     *
     * <p>
     *
     * For example: if you have tables A, B and C, with foreign keys from C to B
     * and from B to A. Then you can use this function to join A, B and C, apply
     * some restrictions on the records and return all B objects that match this
     * query.
     *
     * @see TableJoin
     * @param <T> The type of the CRUDModel in which you are interested.
     * (usually the last joined table, but this is not required.)
     * @param tableJoin This table join describes how the tables must be joined.
     * @param searchTerms A list of search terms to narrow the search. All these
     * search terms must include Attributes pointing to tables that are present
     * in the TableJoin.
     * @param returnType The class object of the CRUDModel that represents the
     * table in which you are interested.
     * @return A list of CRUDModel objects matching the search.
     * @throws DataAccessException Something went wrong with the underlying
     * database.
     */
    <T extends CRUDModel> List<T> complexSearch(
            TableJoin tableJoin,
            List<SearchTerm> searchTerms,
            Class<T> returnType) throws DataAccessException;

}

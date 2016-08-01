package database_v2.DAOLayer;

import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.mongo.EventDBModel;
import java.util.List;

/**
 * All communication with the persistent storage layer for events must go through this interface.
 */
public interface EventDAO {

    /**
     * Create an new event in the persistent storage layer. The assigned id is afterwards stored in
     * the passed event. This method will look at the UUID of the event to determine if it already
     * exists in the db. If this is the case, then an update will be done instead.
     *
     * @param event The event-object we want to add to the database.
     * @return Returns whether the event already existed or not. True is an event with given UUID
     * was found, false otherwise.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    boolean createEvent(EventDBModel event) throws DataAccessException;

    /**
     * Updates a previously stored event. The persistent storage layer uses the event id for lookup,
     * so this field must be correctly filled in in the event parameter.
     *
     * @param event The updated event-object.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No event was found matching this id.
     */
    void updateEvent(EventDBModel event) throws DataAccessException, RecordNotFoundException;

    /**
     * Deletes the given event from the persistent storage layer. The lookup is done based upon the
     * event id, so this value must be correctly filled in.
     *
     * @param event The event-object we wish to remove.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No event was found matching the given id.
     */
    void deleteEvent(EventDBModel event) throws DataAccessException, RecordNotFoundException;

    /**
     * Deletes the given event from the persistent storage layer. The lookup is done based upon the
     * event id.
     *
     * @param eventid The id of the event you wish to delete.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No event was found matching the given id.
     */
    void deleteEvent(String eventid) throws DataAccessException, RecordNotFoundException;

    /**
     * Returns the event as it is stored in the persistent storage layer for a given event id.
     *
     * @param id The id of the event we want to retrieve.
     * @return The event with the corresponding id.
     * @throws DataAccessException Something went wrong with the underlying database.
     * @throws RecordNotFoundException No event was found matching the given id.
     */
    EventDBModel getEvent(String id) throws DataAccessException, RecordNotFoundException;

    /**
     * Returns list of all the events currently stored in the persistent storage layer.
     *
     * @return List of all events.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    List<EventDBModel> getEvents() throws DataAccessException;

    /**
     * Get all events that lie within a certain grid.
     *
     * @param gridX The x coordinate of the grid
     * @param gridY The y coordinate of the grid
     * @return List of all events in that grid
     */
    List<EventDBModel> getEvents(int gridX, int gridY);

    /**
     * Get all events that lie within the a radius around a given point (in Cartesian coordinates).
     *
     * @param cartX Cartesian x value of the center
     * @param cartY Cartesian y value of the center
     * @param radius The radius around the center
     * @return All events that lie in the radius around the center.
     */
    List<EventDBModel> getEventsInRadius(double cartX, double cartY, int radius);

    /**
     * Delete all events from the database that have an edit time (in epoch time) lower then the
     * given threshold.
     *
     * @param threshold all events with edit time (in epoch) lower then this value will be deleted.
     * @return The number of deleted events.
     */
    long deleteOldEvents(long threshold);
}

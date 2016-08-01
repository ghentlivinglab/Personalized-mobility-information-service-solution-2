package database_v2.DAOLayer.impl;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import database_v2.DAOLayer.EventDAO;
import database_v2.controlLayer.DataAccessProvider;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.mongo.EventDBModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.javatuples.Pair;

/**
 * This class communicates with the database to handle everything regarding events.
 */
public class EventDAOImpl extends AbstractMongoDAO implements EventDAO {

    private final String collectionName = "events";
    private final String idColumnName = "_id";

    /**
     * Create a new instance of EventDAOImpl
     *
     * @param db The database we need to communicate with.
     * @param dap The interface to communicate with the PostgreSQL database.
     */
    public EventDAOImpl(MongoDatabase db, DataAccessProvider dap) {
        super(db, dap);
    }

    @Override
    public boolean createEvent(EventDBModel event) {
        Document doc = event.toDocument();
        try {
            // try to insert the event as if it is a new one
            db.getCollection(collectionName).insertOne(doc);
            // no exception was thrown, so the event did not already exist
            ObjectId eventId = doc.getObjectId(idColumnName);
            event.setId(eventId.toHexString());
            // return true if event already existed, so false
            return false;
        } catch (MongoWriteException ex) {
            Logger.getLogger(getClass()).debug("Inserting existing event.");
            // the event was previously created by Waze, so we just do an
            // update on the information, after getting the id MongoDB has originally
            // given. We also get the last process time, so this field remains untouched for
            // each repetion in Waze data
            try {
                Pair<String, Long> info = findInfoByUUID(event.getUuid());
                event.setId(info.getValue0());
                event.setLastProcessedTimeMillis(info.getValue1());
                updateEvent(event);
                // the event already existed, so return true.
                return true;
            } catch (RecordNotFoundException e) {
                // first Mongo throws an unique exception, and now it tells
                // us there is not an event. So something must be off here.
                Logger.getLogger(getClass()).error(e);
                return false;
            }
        }
    }

    private Pair<String, Long> findInfoByUUID(String uuid)
            throws RecordNotFoundException {
        FindIterable<Document> found = db.getCollection(collectionName).find(
                new Document("uuid", uuid)
        );

        MongoCursor<Document> it = found.iterator();
        if (!it.hasNext()) {
            throw new RecordNotFoundException();
        }
        Document eventDoc = it.next();
        return new Pair<>(
                eventDoc.get(idColumnName, ObjectId.class).toHexString(),
                eventDoc.getLong("lastProcessedTimeMillis")
        );
    }
    
    @Override
    public void updateEvent(EventDBModel event) throws RecordNotFoundException {
        Document newDoc = event.toDocument();
        UpdateResult result = db.getCollection(collectionName).replaceOne(
                new Document("_id", new ObjectId(event.getId())),
                newDoc
        );

        if (result.getMatchedCount() < 1) {
            throw new RecordNotFoundException("No event found with id " + event.getId());
        }

    }

    @Override
    public void deleteEvent(EventDBModel event) throws RecordNotFoundException {
        deleteEvent(event.getId());
    }

    @Override
    public void deleteEvent(String eventid) throws RecordNotFoundException {
        DeleteResult result = db.getCollection(collectionName).deleteOne(
                new Document("_id", new ObjectId(eventid))
        );

        if (result.getDeletedCount() < 1) {
            throw new RecordNotFoundException("No event found with id " + eventid);
        }

    }

    @Override
    public EventDBModel getEvent(String id) throws RecordNotFoundException {
        FindIterable<Document> found = db.getCollection(collectionName).find(
                new Document("_id", new ObjectId(id))
        );

        MongoCursor<Document> it = found.iterator();
        if (!it.hasNext()) {
            throw new RecordNotFoundException();
        }
        Document eventDoc = it.next();

        return EventDBModel.parse(eventDoc);
    }

    @Override
    public List<EventDBModel> getEvents() throws DataAccessException {
        FindIterable<Document> allEventDocs = db.getCollection(collectionName).find();
        return toDbEventList(allEventDocs);
    }

    @Override
    public List<EventDBModel> getEvents(int gridX, int gridY) {
        FindIterable<Document> foundDocs = db.getCollection(collectionName)
                .find(and(eq("gridX", gridX), eq("gridY", gridY)));
        return toDbEventList(foundDocs);
    }

    @Override
    public List<EventDBModel> getEventsInRadius(double cartX, double cartY, int radius) {
        String query = radius + " * " + radius + " >= "
                + "((this.coordinates.cartX) - (" + cartX + ")) * "
                + "((this.coordinates.cartX) - (" + cartX + ")) + "
                + "((this.coordinates.cartY) - (" + cartY + ")) * "
                + "((this.coordinates.cartY) - (" + cartY + "))";
        FindIterable<Document> foundDocs = db.getCollection(collectionName)
                .find(where(query));
        return toDbEventList(foundDocs);
    }

    private List<EventDBModel> toDbEventList(FindIterable<Document> found) {
        List<EventDBModel> out = new ArrayList<>();
        MongoCursor<Document> it = found.iterator();

        while (it.hasNext()) {
            out.add(EventDBModel.parse((it.next())));
        }

        return out;
    }

    @Override
    public long deleteOldEvents(long threshold) {
        DeleteResult result = db.getCollection(collectionName)
                .deleteMany(lte("lastEditTimeMillis", threshold));
        return result.getDeletedCount();
    }
    
}

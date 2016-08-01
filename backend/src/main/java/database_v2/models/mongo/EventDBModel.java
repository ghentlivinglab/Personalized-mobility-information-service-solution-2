package database_v2.models.mongo;

import database_v2.models.MongoModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * DB model to represent an event in MongoDB.
 */
public class EventDBModel implements MongoModel {

    private String id;
    private String uuid;
    private CoordinateDBModel coordinates;
    private boolean active;
    private long publicationTimeMillis;
    private long lastEditTimeMillis;
    private long lastProcessedTimeMillis;
    private String description;
    private String formattedAddress;
    private List<JamDBModel> jams;
    private int eventTypeId;
    private int gridX, gridY;

    /**
     * Create a new instance.
     *
     * @param coordinates The coordinate db model of the event
     * @param active Indicate if the event is still active.
     * @param publicationTimeMillis The time of publication of the event
     * @param lastEditTimeMillis The time of the last edit
     * @param description The description of the event.
     * @param formattedAddress
     * @param jams
     * @param eventTypeId The id of the eventType of this event.
     * @param uuid The id given by waze itself
     * @param gridX
     * @param gridY
     */
    public EventDBModel(CoordinateDBModel coordinates, boolean active, long publicationTimeMillis,
            long lastEditTimeMillis, String description, String formattedAddress, List<JamDBModel> jams,
            int eventTypeId, String uuid, int gridX, int gridY) {
        this.coordinates = coordinates;
        this.active = active;
        this.publicationTimeMillis = publicationTimeMillis;
        this.lastEditTimeMillis = lastEditTimeMillis;
        this.description = description;
        this.jams = jams;
        this.eventTypeId = eventTypeId;
        this.uuid = uuid;
        this.gridX = gridX;
        this.gridY = gridY;
        this.formattedAddress = formattedAddress;
        
        // special field
        this.lastProcessedTimeMillis = System.currentTimeMillis();
    }

    @Override
    public Document toDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("coordinates", coordinates.toDocument());
        map.put("active", active);
        map.put("gridX", gridX);
        map.put("gridY", gridY);
        map.put("publicationTimeMillis", publicationTimeMillis);
        map.put("lastEditTimeMillis", lastEditTimeMillis);
        map.put("lastProcessedTimeMillis", lastProcessedTimeMillis);
        map.put("description", description);
        List<Document> jamDocs = new ArrayList<>();
        jams.forEach(jam -> {
            jamDocs.add(jam.toDocument());
        });
        map.put("jams", jamDocs);
        if (uuid != null) {
            map.put("uuid", uuid);
        }
        map.put("eventTypeId", eventTypeId);
        if (formattedAddress != null) {
            map.put("formattedAddress", formattedAddress);
        }
        return new Document(map);
    }

    /**
     * parse a document to an Event db model. Only works if the document is really a representation
     * of an event.
     *
     * @param doc The document to be parsed.
     * @return An event db model
     */
    public static EventDBModel parse(Document doc) {
        List<Document> jamDocs = (List<Document>) doc.get("jams");
        List<JamDBModel> dbJams = new ArrayList<>();
        jamDocs.forEach(jamDoc -> {
            dbJams.add(JamDBModel.parse(jamDoc));
        });

        EventDBModel out = new EventDBModel(
                CoordinateDBModel.parse(doc.get("coordinates", Document.class)),
                (boolean) doc.get("active"),
                (long) doc.get("publicationTimeMillis"),
                (long) doc.get("lastEditTimeMillis"),
                (String) doc.get("description"),
                (String) doc.get("formattedAddress"),
                dbJams,
                (int) doc.get("eventTypeId"),
                (String) doc.get("uuid"),
                (int) doc.get("gridX"),
                (int) doc.get("gridY")
        );
        out.setId(doc.get("_id", ObjectId.class).toHexString());
        out.setLastProcessedTimeMillis(doc.getLong("lastProcessedTimeMillis"));
        return out;
    }

    /**
     * get the id of the event.
     *
     * @return id of the event
     */
    public String getId() {
        return id;
    }

    /**
     * set the id of the event.
     *
     * @param id of the event
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get the coordinates of the event
     *
     * @return coordinates db model
     */
    public CoordinateDBModel getCoordinates() {
        return coordinates;
    }

    /**
     * set the coordinates of the event
     *
     * @param coordinates db model
     */
    public void setCoordinates(CoordinateDBModel coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Get if the event is active.
     *
     * @return True is active, false otherwise.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Set if the event is active.
     *
     * @param active True is active, false otherwise.
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Get the publication time of the event
     *
     * @return publication time of the event
     */
    public long getPublicationTimeMillis() {
        return publicationTimeMillis;
    }

    /**
     * Set the publication time of the event
     *
     * @param publicationTimeMillis publication time of the event
     */
    public void setPublicationTimeMillis(long publicationTimeMillis) {
        this.publicationTimeMillis = publicationTimeMillis;
    }

    /**
     * Get the time of the last edit.
     *
     * @return time of the last edit.
     */
    public long getLastEditTimeMillis() {
        return lastEditTimeMillis;
    }

    /**
     * Set the time of the last edit.
     *
     * @param lastEditTimeMillis time of the last edit.
     */
    public void setLastEditTimeMillis(long lastEditTimeMillis) {
        this.lastEditTimeMillis = lastEditTimeMillis;
    }

    /**
     * get the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the jam of this event, if present.
     *
     * @return jam of the event.
     */
    public List<JamDBModel> getJams() {
        return jams;
    }

    /**
     * Set the jam of this event
     *
     * @param jams The new jam
     */
    public void setJam(List<JamDBModel> jams) {
        this.jams = jams;
    }

    /**
     * Get the id of the eventType
     *
     * @return id of the eventtype
     */
    public int getEventTypeId() {
        return eventTypeId;
    }

    /**
     * set the id of the eventtype
     *
     * @param eventTypeId id of the eventtype
     */
    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getLastProcessedTimeMillis() {
        return lastProcessedTimeMillis;
    }

    public void setLastProcessedTimeMillis(long lastProcessedTimeMillis) {
        this.lastProcessedTimeMillis = lastProcessedTimeMillis;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 41)
                .append(id)
                .append(coordinates)
                .append(active)
                .append(publicationTimeMillis)
                .append(lastEditTimeMillis)
                .append(description)
                .append(jams)
                .append(eventTypeId)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        EventDBModel rhs = (EventDBModel) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(coordinates, rhs.coordinates)
                .append(active, rhs.active)
                .append(publicationTimeMillis, rhs.publicationTimeMillis)
                .append(lastEditTimeMillis, rhs.lastEditTimeMillis)
                .append(description, rhs.description)
                .append(jams, rhs.jams)
                .append(eventTypeId, rhs.eventTypeId)
                .isEquals();

    }

}

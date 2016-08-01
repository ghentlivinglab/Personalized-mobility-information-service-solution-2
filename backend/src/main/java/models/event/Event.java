package models.event;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import models.Coordinate;
import models.Transportation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model to represent an event.
 */
public class Event {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    protected Coordinate coordinate;
    protected boolean active;
    protected long publicationTimeMillis;
    protected long lastEditTimeMillis;
    protected String description;
    protected String formattedAddress;
    protected EventType type;
    protected String uuid;
    protected Map<String, Jam> jams;

    /**
     * The constructor for events made by Waze. These need the UUID's given by Waze to recognize
     * recurring events.
     *
     * @param coordinates
     * @param active
     * @param publicationTimeMillis
     * @param lastEditTimeMillis
     * @param description
     * @param formattedAddress
     * @param eventType
     * @param uuid
     */
    public Event(String uuid, Coordinate coordinates, boolean active, long publicationTimeMillis,
            long lastEditTimeMillis, String description, String formattedAddress, EventType eventType) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates of event can't be null");
        } else {
            this.coordinate = coordinates;
        }
        this.active = active;
        this.publicationTimeMillis = publicationTimeMillis;
        this.lastEditTimeMillis = lastEditTimeMillis;
        if (description == null) {
            throw new IllegalArgumentException("Discription of event can't be null");
        }
        this.description = description;
        this.formattedAddress = formattedAddress;
        this.type = eventType;
        this.uuid = uuid;
        jams = new ConcurrentHashMap<>();
    }

    /**
     * Constructor for events made with the front end, no UUID is available from Waze.
     *
     * @param coordinates
     * @param active
     * @param publicationTimeString (format "2016-05-15T17:24:03.881")
     * @param lastEditTimeMillis
     * @param description
     * @param formattedAddress
     * @param eventType
     */
    public Event(Coordinate coordinates, boolean active, String publicationTimeString,
            long lastEditTimeMillis, String description, String formattedAddress, EventType eventType) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates of event can't be null");
        } else {
            this.coordinate = coordinates;
        }
        this.active = active;
        try {
            this.publicationTimeMillis = DATE_FORMATTER.parse(publicationTimeString).getTime();
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
        this.lastEditTimeMillis = lastEditTimeMillis;
        if (description == null) {
            throw new IllegalArgumentException("Discription of event can't be null");
        }
        this.description = description;
        this.formattedAddress = formattedAddress;
        this.type = eventType;
        this.uuid = null;
        jams = new ConcurrentHashMap<>();
    }

    public void updateEvent(Event newEvent) {
        this.coordinate = newEvent.coordinate;
        this.active = newEvent.active;
        this.publicationTimeMillis = newEvent.publicationTimeMillis;
        this.lastEditTimeMillis = newEvent.lastEditTimeMillis;
        this.description = newEvent.description;
        this.formattedAddress = newEvent.formattedAddress;
        this.type = newEvent.type;
        this.uuid = newEvent.uuid;
        this.jams = newEvent.jams;
    }

    /**
     * Returns the coordinates of the Event.
     *
     * @return
     */
    public Coordinate getCoordinates() {
        return coordinate;
    }

    /**
     * Setter for the coordinates of the Event.
     *
     * @param coordinates
     */
    public void setCoordinates(Coordinate coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates of event can't be null");
        } else {
            this.coordinate = coordinates;
        }
    }

    /**
     * Returns whether or not the Event is still Active;
     *
     * @return
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * This method can set the object active of the event.
     *
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * This method activates the Event.
     */
    public void activate() {
        setActive(true);
    }

    /**
     * This method deactivates the Event.
     */
    public void deactivate() {
        setActive(false);
    }

    /**
     * This is the getter for the Publication Time.
     *
     * @return
     */
    public long getPublicationTimeMillis() {
        return publicationTimeMillis;
    }

    public String getPublicationString() {
        return DATE_FORMATTER.format(new Date(publicationTimeMillis));
    }

    /**
     * This is the getter for the publication time.
     *
     * @return
     */
    public long getLastEditTimeMillis() {
        return lastEditTimeMillis;
    }

    public String getLastEditString() {
        return DATE_FORMATTER.format(new Date(lastEditTimeMillis));
    }

    public Time getEditTime() {
        return new Time(lastEditTimeMillis);
    }

    /**
     * This is the setter for the publication time.
     *
     * @param lastEditTimeMillis
     */
    public void setLastEditTimeMillis(long lastEditTimeMillis) {
        this.lastEditTimeMillis = lastEditTimeMillis;
    }

    /**
     * This is the getter for the description.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * This is the setter for the description.
     *
     * @param description
     */
    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("The description can't be null.");
        }
        this.description = description;
        this.lastEditTimeMillis = System.currentTimeMillis();
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * This is the getter of the Event Type.
     *
     * @return
     */
    public EventType getType() {
        return type;
    }

    /**
     * This is the setter for the Event Type.
     *
     * @param type the new type for the event.
     */
    public void setType(EventType type) {
        this.type = type;
    }

    public List<Transportation> getTransportTypes() {
        return Collections.unmodifiableList(type.getTransportationTypes());
    }

    public String getUuid() {
        return uuid;
    }

    public void addJam(Jam jam) {
        jams.put(jam.getUuid(), jam);
    }

    public Jam getJamIfPresent(String uuid) {
        return jams.get(uuid);
    }

    public void deleteJam(String uuid) {
        jams.remove(uuid);
    }

    public List<Jam> getAllJams() {
        List<Jam> out = new ArrayList<>();
        for (Entry<String, Jam> entry : jams.entrySet()) {
            out.add(entry.getValue());
        }
        return Collections.unmodifiableList(out);
    }

    public void deleteAllJams() {
        jams.clear();
    }

    /**
     * This is the override of the hashcode function.
     *
     * @return the hash value;
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 41).
                append(coordinate).
                append(active).
                append(publicationTimeMillis).
                append(lastEditTimeMillis).
                append(description).
                append(type).
                toHashCode();
    }

    /**
     * This is the override of the equals() function for the Event.
     *
     * @param obj The Object that needs to be compared to the Event.
     * @return boolean indicating whether or not the events are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Event)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Event rhs = (Event) obj;
        return new EqualsBuilder().
                append(coordinate, rhs.coordinate).
                append(active, rhs.active).
                append(publicationTimeMillis, rhs.publicationTimeMillis).
                append(lastEditTimeMillis, rhs.lastEditTimeMillis).
                append(description, rhs.description).
                append(type, rhs.type).
                append(uuid, rhs.uuid).
                isEquals();

    }
}

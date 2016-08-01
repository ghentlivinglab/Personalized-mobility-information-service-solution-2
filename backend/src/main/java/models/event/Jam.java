package models.event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import models.Coordinate;
import java.util.UUID;

/**
 *
 */
public class Jam {

    private final String uuid;
    private final long publicationTimeMillis;
    private List<Coordinate> line;
    private int speed;
    private int delay;

    /**
     * Constructor for database
     *
     * @param uuid
     * @param publicationTimeMillis
     * @param line
     * @param speed
     * @param delay
     */
    public Jam(String uuid, long publicationTimeMillis, List<Coordinate> line, int speed, int delay) {
        this.uuid = uuid;
        this.publicationTimeMillis = publicationTimeMillis;
        this.line = new ArrayList<>(line);
        this.speed = speed;
        this.delay = delay;
    }

    /**
     * Constructor for DTO
     *
     * @param publicationTimeString
     * @param line
     * @param speed
     * @param delay
     */
    public Jam(String publicationTimeString, List<Coordinate> line, int speed, int delay) {
        try {
            this.publicationTimeMillis = Event.DATE_FORMATTER.parse(publicationTimeString).getTime();
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
        this.line = new ArrayList<>(line);
        this.speed = speed;
        this.delay = delay;
        // generate a uuid for this jam.
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }


    public long getPublicationTimeMillis() {
        return publicationTimeMillis;
    }

    public String getPublicationString() {
        return Event.DATE_FORMATTER.format(new Date(publicationTimeMillis));
    }

    public List<Coordinate> getLineView() {
        return Collections.unmodifiableList(line);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}

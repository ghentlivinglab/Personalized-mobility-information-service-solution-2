package hello;


public class Event {

    private String id;
    private Coordinate coordinates;
    private boolean active;
    private String publicationTime;
    private String lastEditTime;
    private String description;
    private Jam jam;
    private String[] source;
    private EventType type;

    public Event(String id, Coordinate coordinates, boolean active, String publicationTime, String lastEditTime, String description, Jam jam, String[] source, EventType type) {
        this.id = id;
        this.coordinates = coordinates;
        this.active = active;
        this.publicationTime = publicationTime;
        this.lastEditTime = lastEditTime;
        this.description = description;
        this.jam = jam;
        this.source = source;
        this.type = type;
    }

    public Event(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Jam getJam() {
        return jam;
    }

    public void setJam(Jam jam) {
        this.jam = jam;
    }

    public String[] getSource() {
        return source;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}

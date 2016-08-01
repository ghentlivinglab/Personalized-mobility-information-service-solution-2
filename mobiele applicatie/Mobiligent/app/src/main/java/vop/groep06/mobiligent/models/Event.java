package vop.groep06.mobiligent.models;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event {
    private String description;
    private Coordinate coordinate;
    private ArrayList<Jam> jams;
    private String address;
    private Date publicationTime;
    private Date lastEditTime;
    private String type;
    private ArrayList<String> transportationTypes;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Event() {
    }

    public Event(String description, Coordinate coordinate, ArrayList<Jam> jams, String address, Date publicationTime, Date lastEditTime, String type, ArrayList<String> transportationTypes) {
        this.description = description;
        this.coordinate = coordinate;
        this.jams = jams;
        this.address = address;
        this.publicationTime = publicationTime;
        this.lastEditTime = lastEditTime;
        this.type = type;
        this.transportationTypes = transportationTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public ArrayList<Jam> getJams() {
        return jams;
    }

    public void setJams(ArrayList<Jam> jams) {
        this.jams = jams;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(Date publicationTime) {
        this.publicationTime = publicationTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getTransportationTypes() {
        return transportationTypes;
    }

    public void setTransportationTypes(ArrayList<String> transportationTypes) {
        this.transportationTypes = transportationTypes;
    }

    public String getPublicationTimeAsString () {
        return DATE_FORMATTER.format(publicationTime);
    }

    public String getLastEditTimeAsString () {
        return DATE_FORMATTER.format(lastEditTime);
    }
}

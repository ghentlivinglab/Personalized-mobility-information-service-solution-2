package hello;


import java.util.List;

public class Route {

    private String id;
    private Address startPoint;
    private Address endPoint;
    private List<Coordinate> waypoints;
    private List<Transportation> transportationTypes;
    private List<EventType> notifyForEventTypes;

    public Route(String id, Address startPoint, Address endPoint, List<Coordinate> waypoints, List<Transportation> transportationTypes, List<EventType> notifyForEventTypes) {
        this.id = id;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.waypoints = waypoints;
        this.transportationTypes = transportationTypes;
        this.notifyForEventTypes = notifyForEventTypes;
    }

    public Route(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Address getStartpoint() {
        return startPoint;
    }

    public void setStartpoint(Address startPoint) {
        this.startPoint = startPoint;
    }

    public Address getEndpoint() {
        return endPoint;
    }

    public void setEndpoint(Address endPoint) {
        this.endPoint = endPoint;
    }

    public List<Coordinate> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Coordinate> waypoints) {
        this.waypoints = waypoints;
    }

    public List<Transportation> getTransportation_types() {
        return transportationTypes;
    }

    public void setTransportation_types(List<Transportation> transportationTypes) {
        this.transportationTypes = transportationTypes;
    }

    public List<EventType> getNotify_for_event_types() {
        return notifyForEventTypes;
    }

    public void setNotify_for_event_types(List<EventType> notifyForEventTypes) {
        this.notifyForEventTypes = notifyForEventTypes;
    }
}

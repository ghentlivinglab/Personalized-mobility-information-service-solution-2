package vop.groep06.mobiligent.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable{
    private String name;
    private String travelId;
    private Address start;
    private Address end;
    private String routeId;
    private ArrayList<Coordinate> waypoints;
    private ArrayList<Coordinate> fullWaypoints;
    private String transportationType;
    private String url;

    public Route() {
    }

    public Route(String travelId, String name, Address start, Address end) {
        this.name = name;
        this.travelId = travelId;
        this.start = start;
        this.end = end;
    }

    public Route(String travelId, String name, Address start, Address end, String routeId, ArrayList<Coordinate> waypoints, ArrayList<Coordinate> fullWaypoints, String transportationType, String url) {
        this.name = name;
        this.travelId = travelId;
        this.start = start;
        this.end = end;
        this.routeId = routeId;
        this.waypoints = waypoints;
        this.fullWaypoints = fullWaypoints;
        this.transportationType = transportationType;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public Address getStart() {
        return start;
    }

    public void setStart(Address start) {
        this.start = start;
    }

    public Address getEnd() {
        return end;
    }

    public void setEnd(Address end) {
        this.end = end;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public ArrayList<Coordinate> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Coordinate> waypoints) {
        this.waypoints = waypoints;
    }

    public ArrayList<Coordinate> getFullWaypoints() {
        return fullWaypoints;
    }

    public void setFullWaypoints(ArrayList<Coordinate> fullWaypoints) {
        this.fullWaypoints = fullWaypoints;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubText() {
        return start.getStreet()+", "+start.getCity()+" -> "+end.getStreet()+", "+end.getCity();
    }

    public String getUrlRoutePart() {
        return "/"+travelId+"/route";
    }

    public String getEventsUrl() {
        return url+"/events";
    }
}

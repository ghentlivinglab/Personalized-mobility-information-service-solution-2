package vop.groep06.mobiligent.models;

import java.io.Serializable;

public class Location implements Serializable{
    private String id;
    private Address address;
    private String name;
    private int radius;
    //private boolean active;
    private String url;

    public Location() {
    }

    public Location(String id, Address address, String name, int radius, String url) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.radius = radius;
//        this.active = active;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventsUrl() {
        return url+"/events";
    }
}

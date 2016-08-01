package vop.groep06.mobiligent.models;


import java.io.Serializable;

public class Address implements Serializable{

    private String street;
    private String housenumber;
    private String city;
    private String country;
    private String postalCode;
    private Coordinate coordinate;

    public Address() {

    }

    public Address(String street, String housenumber, String city, String country, String postalCode, Coordinate coordinate) {
        this.street = street;
        this.housenumber = housenumber;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.coordinate = coordinate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return street+" "+housenumber+", "+postalCode+" "+city+", "+country;
    }
}

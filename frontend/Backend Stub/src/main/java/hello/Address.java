package hello;

public class Address {

    private String street;
    private String houseNumber;
    private String city;
    private String country;
    private String postalCode;
    private Coordinate coordinates;

    public Address(String street, String houseNumber, String city, String country, String postalCode, Coordinate coordinates) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.coordinates = coordinates;
    }

    public Address(){};

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenumber() {
        return houseNumber;
    }

    public void setHousenumber(String houseNumber) {
        this.houseNumber = houseNumber;
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

    public String getPostal_code() {
        return postalCode;
    }

    public void setPostal_code(String postalCode) {
        this.postalCode = postalCode;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }
}

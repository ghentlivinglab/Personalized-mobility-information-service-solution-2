package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class AddressDTO {
    private String street;
    private String houseNumber;
    private String city;
    private String country;
    private String postalCode;
    private CoordinateDTO coordinates;

    public AddressDTO() {
        this.houseNumber = "";
    }

    public AddressDTO(String street, String houseNumber, String city, String country, String postalCode, CoordinateDTO coordinates) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.coordinates = coordinates;
    }

    @JsonProperty("street")
    public String getStreet() {
        return street;
    }

    @JsonProperty("housenumber")
    public String getHouseNumber() {
        return houseNumber;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("postal_code")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("coordinates")
    public CoordinateDTO getCoordinates() {
        return coordinates;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        if (this.houseNumber != null) {
            this.houseNumber = houseNumber;
        }
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCoordinates(CoordinateDTO coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        AddressDTO rhs = (AddressDTO) obj;
        return new EqualsBuilder()
                .append(street, rhs.street)
                .append(houseNumber, rhs.houseNumber)
                .append(city, rhs.city)
                .append(postalCode, rhs.postalCode)
                .append(country, rhs.country)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.street);
        hash = 53 * hash + Objects.hashCode(this.houseNumber);
        hash = 53 * hash + Objects.hashCode(this.city);
        hash = 53 * hash + Objects.hashCode(this.country);
        hash = 53 * hash + Objects.hashCode(this.postalCode);
        return hash;
    }
    
    
    
}

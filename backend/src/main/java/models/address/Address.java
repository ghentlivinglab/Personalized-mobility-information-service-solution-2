package models.address;

import models.Coordinate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model to represent an address. This model is used as a value, so the equals
 * must be overwritten. This means no cache system is required for this model.
 */
public class Address {

    private final Street street;
    private final int houseNumber;
    private final String box;
    private final Coordinate coordinates;

    /**
     * Create a new address.
     * @param street the street of the address (not null)
     * @param houseNumber the house number (not null)
     * @param box the box of the address, if it exists.
     * @param coordinates the coordinates (not null)
     */
    public Address(Street street, int houseNumber, String box, Coordinate coordinates) {
        if (street == null) {
            throw new IllegalArgumentException("street can't be null");
        }
        this.street = street;
        this.houseNumber = houseNumber;
        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates can't be null");
        } else {
            this.coordinates = coordinates;
        }
        this.box=box;
    }

    /**
     * Create a new address.
     * This box is initialized as an empty string.
     * @param street the street of the address (not null)
     * @param houseNumber the house number (not null)
     * @param coordinates the coordinates (not null)
     */
    public Address(Street street, int houseNumber, Coordinate coordinates) {
        if (street == null) {
            throw new IllegalArgumentException("street can't be null");
        }
        this.street = street;
        this.houseNumber = houseNumber;
        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates can't be null");
        } else {
            this.coordinates = coordinates;
        }
        this.box="";
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1311, 4757)
                .append(street)
                .append(houseNumber)
                .append(coordinates)
                .append(box)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Address rhs = (Address) obj;
        return new EqualsBuilder()
                .append(street, rhs.street)
                .append(houseNumber, rhs.houseNumber)
                .append(coordinates, rhs.coordinates)
                .append(box,rhs.box)
                .isEquals();
    }

    /**
     * Get the street of this address.
     * @return the street
     */
    public Street getStreet() {
        return street;
    }

    /**
     * Get the house number of this address
     * @return the house number
     */
    public int getHouseNumber() {
        return houseNumber;
    }

    /**
     * Get the coordinates of this address
     * @return the coordinates
     */
    public Coordinate getCoordinates() {
        return coordinates;
    }

    /**
     * The getter of the box of the address
     * @return the box
     */
    public String getBox() {
        return box;
    }
    
    
    @Override
    public String toString(){
        return street.getName()+" "+houseNumber+", "+street.getCity().getPostalCode()+" "+street.getCity().getCityName()+", "+street.getCity().getCountry();
    }
}

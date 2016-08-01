package models.address;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This model is used to represent a street. It is used as a value so the equals
 * method must be overwritten
 */
public class Street {
    private String name;
    private City city;

    /**
     * Create new street
     * @param name the name of the street (not null) 
     * @param city the city of the street (not null)
     */
    public Street(String name, City city) {
        if(name == null) {
            throw new IllegalArgumentException("name of street can't be null.");
        }
        this.name = name;
        if(city == null) {
            throw new IllegalArgumentException("city of street can't be null");
        }
        this.city = city;
    }

    /**
     * Get the name of the street
     * @return name of the street
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the city
     * @return name of the city
     */
    public City getCity() {
        return city;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Street)) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        
        Street other = (Street) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .append(city.getCityName(), other.city.getCityName())
                .append(city.getCountry(), other.city.getCountry())
                .append(city.getPostalCode(), other.city.getPostalCode())
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(533, 4285)
                .append(name)
                .append(city)
                .toHashCode();
    }
    
}

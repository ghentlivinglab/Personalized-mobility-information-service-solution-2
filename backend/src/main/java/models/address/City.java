package models.address;

import models.exceptions.InvalidCountryCodeException;

/**
 * Model to represent a city. Each city is represented by a unique object in the domain
 * layer. A city model can only be equal to another if their objects are the same. This
 * requires a caching system and the equals method should not be overwritten.
 * 
 * Refer to the cache to get the ID of this object.
 */
public class City {
    private final String cityName;
    private final String postalCode;
    private final String country;
    
    /**
     * Create a new model for a city; don't use this constructor directly but obtain
     * a object through the cache layer. Each city should only be represented by a
     * single object in the domain layer at all times. This requires a cache system.
     * @param cityName The name of the city
     * @param postalCode the postal code of the city
     * @param country the country code of the country of the city. This code is 2 characters long.
     * @throws models.exceptions.InvalidCountryCodeException this Exception will be thrown if the country code is invalid.
     */
    public City(String cityName, String postalCode, String country) throws InvalidCountryCodeException {
        if(cityName == null) {
            throw new IllegalArgumentException("cityname can't be null");
        } else {
            this.cityName = cityName;
        }
        if(postalCode == null) {
            throw new IllegalArgumentException("postal code can't be null");
        }
        this.postalCode = postalCode;
        if(country == null){
            throw new IllegalArgumentException("Country code can't be null");
        }
        City.checkCountry(country);
        this.country = country;
    }

    /**
     * Get the name of the city.
     * @return the name of the city.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Get the postal code of the city.
     * @return postal code of the city.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Get the county where this city lays in.
     * @return the country of the city.
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * This method checks whether or not the String is a 2 letter country code.
     * @param country the string that needs to be checked.
     * @return boolean indication whether or not the coun
     */
    public static boolean checkCountry(String country) throws InvalidCountryCodeException{
       if(country.length()!=2) {
           throw new InvalidCountryCodeException("The country code must be 2 characters long.");
       }
       return true;
    }

    
}

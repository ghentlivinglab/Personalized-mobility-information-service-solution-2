package DTO.mappers;

import DTO.models.AddressDTO;
import models.Coordinate;
import models.address.Address;
import models.address.City;
import models.address.Street;
import models.exceptions.InvalidCountryCodeException;

/**
 * Class to convert an address object from/into an addressDTO (json) object
 */
public class AddressMapper {
    
    /**
     * Method to convert Address into AddressDTO
     * @param address the address (model) object
     * @return an AddressDTO object
     */
    public AddressDTO convertToDTO (Address address) {
        Street street = address.getStreet();
        City city = street.getCity();
        CoordinateMapper coomap = new CoordinateMapper();
        String housenumber = "";
        if (address.getHouseNumber() != 0) {
            housenumber = Integer.toString(address.getHouseNumber());
        }
        
        return new AddressDTO(
                street.getName(),
                housenumber, 
                city.getCityName(), 
                city.getCountry(), 
                city.getPostalCode(), 
                coomap.convertToDTO(address.getCoordinates()));
    }
    
    /**
     * Method to convert AddressDTO into Address
     * @param addressdto an AddressDTO object
     * @return an Address object (model)
     * @throws InvalidCountryCodeException if the given country is not valid (e.g.: 'BE' is valid)
     */
    public Address convertFromDTO (AddressDTO addressdto) throws InvalidCountryCodeException {
        CoordinateMapper coormapper = new CoordinateMapper();
        Coordinate coordinates = coormapper.convertFromDTO(addressdto.getCoordinates());
        int housenumber = 0;
        if (!addressdto.getHouseNumber().equals("")) {
            housenumber = Integer.parseInt(addressdto.getHouseNumber());
        }
        return new Address(
                new Street(addressdto.getStreet(), 
                        new City(addressdto.getCity(), 
                                addressdto.getPostalCode(), 
                                addressdto.getCountry())), 
                housenumber, 
                coordinates);
    }
}

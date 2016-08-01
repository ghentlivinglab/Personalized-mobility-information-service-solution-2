package DTO.mappers;

import DTO.models.AddressDTO;
import DTO.models.EventTypeDTO;
import DTO.models.LocationDTO;
import DTO.models.NotifyDTO;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import models.Location;
import models.address.Address;
import models.event.EventType;
import models.exceptions.InvalidCountryCodeException;
import models.services.Email;
import models.services.PhoneNumber;
import models.services.Service;

/**
 * Class to convert a location to locationdto and vice versa
 */
public class LocationMapper {
    
    /**
     * Convert Location to LocationDTO object
     * @param location Location object
     * @param locationId String of locationid
     * @return LocationDTO representation
     */
    public LocationDTO convertToDTO (Location location, String locationId) {
        //convert address
        AddressMapper addressmapper = new AddressMapper();
        AddressDTO address = addressmapper.convertToDTO(location.getAddress());
        
        //converting eventtypes
        EventTypeMapper eventtypemapper = new EventTypeMapper();
        int size = location.getNotifyForEventTypes().size();
        EventTypeDTO [] eventtypes = new EventTypeDTO[size];
        int i = 0;
        for (EventType eventtype : location.getNotifyForEventTypes().values()) {
            eventtypes[i] = eventtypemapper.convertToDTO(eventtype);
            i++;
        }
        
        NotifyDTO notify = new NotifyDTO();
        for (Service service : location.getServices()) {
            if(service.getClass() == Email.class) {
                Email email = (Email) service;
                notify.setEmail(email.isValidated());
            }
        }
        
        return new LocationDTO(
                locationId, 
                address, 
                location.getName(), 
                location.getRadius(), 
                !location.isMuted(), //if it's muted, it isn't active
                eventtypes, 
                notify);
    }
    
    /**
     * Convert Location to LocationDTO object
     * @param location Location object
     * @param locationId Integer of locationid
     * @return LocationDTO representation
     */
    public LocationDTO convertToDTO (Location location, int locationId) {
        return convertToDTO(location, Integer.toString(locationId));
    }
    
    /**
     * Convert LocationDTO to Location object
     * @param locationdto LocationDTO object
     * @return Location representation
     * @throws DataAccessException
     * @throws RecordNotFoundException
     * @throws InvalidCountryCodeException
     */
    public Location convertFromDTO (LocationDTO locationdto)
            throws DataAccessException, RecordNotFoundException, InvalidCountryCodeException {
        AddressMapper addressmapper = new AddressMapper();
        Address address = addressmapper.convertFromDTO(locationdto.getAddress());
        
        ArrayList<Service> services = new ArrayList<>();
        if(locationdto.getNotify().isEmail()) {
            //TODO - NO WAY ATM
        }
        // We need the database to convert eventType names to actual models
        // This step is handled in the controller itself
        Map<Integer, EventType> eventTypes = new HashMap<>();
//        for(EventTypeDTO eventTypeDTO: locationdto.getNotifyEventTypes()) {
//            Pair<Integer, EventType> et = database.getEventtype(eventTypeDTO.getType());
//            eventTypes.put(et.getValue0(), et.getValue1());
//        }
        return new Location(
                address, 
                locationdto.getName(), 
                locationdto.getRadius(), 
                !locationdto.isActive(),
                eventTypes,
                services //TODO
        );
    }
}

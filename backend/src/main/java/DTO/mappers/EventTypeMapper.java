package DTO.mappers;

import DTO.models.EventTypeDTO;
import models.event.EventType;

/**
 * Class to convert an eventtype object from/into an eventtypeDTO (json) object
 */
public class EventTypeMapper {
    
    /**
     * Method to convert EventType into EventTypeDTO
     * @param eventtype EventType to convert
     * @return EventTypeDTO object
     */
    public EventTypeDTO convertToDTO (EventType eventtype) {
        
        return new EventTypeDTO(eventtype.getType());
    }
    
    /**
     * Method to convert EventTypeDTO into EventType
     * @param eventtypedto EventTypeDTO object to convert
     * @return EventType object
     */
    public EventType convertFromDTO (EventTypeDTO eventtypedto) {
        return new EventType(eventtypedto.getType(), null);
    }
}

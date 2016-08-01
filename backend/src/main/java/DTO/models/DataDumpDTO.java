package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class DataDumpDTO {
    
    private List <UserDTO> users;
    private List <TravelDumpDTO> travels;
    private List <LocationDTO> locations;
    private List <EventDTO> events;
    private List <EventTypeDTO> eventTypes;
    
    public DataDumpDTO () {
        
    }

    public DataDumpDTO(List<UserDTO> users, List<TravelDumpDTO> travels, List<LocationDTO> locations, List<EventDTO> events, List<EventTypeDTO> eventTypes) {
        this.users = users;
        this.travels = travels;
        this.locations = locations;
        this.events = events;
        this.eventTypes = eventTypes;
    }

    @JsonProperty("users")
    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    @JsonProperty("travels")
    public List<TravelDumpDTO> getTravels() {
        return travels;
    }

    public void setTravels(List<TravelDumpDTO> travels) {
        this.travels = travels;
    }

    @JsonProperty("point_of_interests")
    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }

    @JsonProperty("events")
    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    @JsonProperty("eventtypes")
    public List<EventTypeDTO> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventTypeDTO> eventTypes) {
        this.eventTypes = eventTypes;
    }
    
    
}

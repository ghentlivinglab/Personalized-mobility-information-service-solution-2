package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class LocationDTO {
    
    private String id;
    private AddressDTO address;
    private String name;
    //private boolean homeAddress;
    private int radius;
    private boolean active;
    private EventTypeDTO [] notifyEventTypes;
    private NotifyDTO notify;

    public LocationDTO() {
        this.id = "";
        this.notifyEventTypes = new EventTypeDTO[0];
        this.notify = new NotifyDTO();
    }

    public LocationDTO(String id, AddressDTO address, String name, int radius, boolean active, EventTypeDTO[] notifyEventTypes, NotifyDTO notify) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.radius = radius;
        this.active = active;
        this.notifyEventTypes = notifyEventTypes;
        this.notify = notify;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("notify")
    public NotifyDTO getNotify() {
        return notify;
    }

    @JsonProperty("address")
    public AddressDTO getAddress() {
        return address;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

//    @JsonProperty("is_home_address")
//    public boolean isHomeAddress() {
//        return homeAddress;
//    }

    @JsonProperty("radius")
    public int getRadius() {
        return radius;
    }

    @JsonProperty("active")
    public boolean isActive() {
        return active;
    }

    @JsonProperty("notify_for_event_types")
    public EventTypeDTO[] getNotifyEventTypes() {
        return notifyEventTypes;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setHomeAddress(boolean homeAddress) {
//        this.homeAddress = homeAddress;
//    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setNotifyEventTypes(EventTypeDTO[] notifyEventTypes) {
        this.notifyEventTypes = notifyEventTypes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNotify(NotifyDTO notify) {
        this.notify = notify;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        LocationDTO rhs = (LocationDTO) obj;
        return new EqualsBuilder()
                .append(id,rhs.id)
                .append(notify, rhs.notify)
                .append(active, rhs.active)
                .append(address, rhs.address)
                .append(name, rhs.name)
                .append(notifyEventTypes, rhs.notifyEventTypes)
                .append(radius, rhs.radius)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.address);
        hash = 19 * hash + Objects.hashCode(this.name);
        hash = 19 * hash + this.radius;
        hash = 19 * hash + (this.active ? 1 : 0);
        hash = 19 * hash + Arrays.deepHashCode(this.notifyEventTypes);
        hash = 19 * hash + Objects.hashCode(this.notify);
        return hash;
    }

 
    
}

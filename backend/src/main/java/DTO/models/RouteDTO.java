package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class RouteDTO {

    private String id;
    private CoordinateDTO[] userWaypoints, fullWaypoints;
    private String transportationType;
    private EventTypeDTO[] notifyEventTypes;
    private NotifyDTO notify;
    private boolean active;

    public RouteDTO() {
        this.id = "";
        this.notifyEventTypes = new EventTypeDTO[0];
        this.notify = new NotifyDTO();
    }

    public RouteDTO(String id, CoordinateDTO[] userWaypoints, CoordinateDTO[] fullWaypoints, String transportationType,
            EventTypeDTO[] notifyEventTypes, NotifyDTO notify, boolean active) {
        this.id = id;
        this.userWaypoints = userWaypoints;
        this.fullWaypoints = fullWaypoints;
        this.transportationType = transportationType;
        this.notifyEventTypes = notifyEventTypes;
        this.notify = notify;
        this.active = active;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("notify")
    public NotifyDTO getNotify() {
        return notify;
    }

    @JsonProperty("active")
    public boolean isMuted() {
        return active;
    }

    @JsonProperty("waypoints")
    public CoordinateDTO[] getUserWaypoints() {
        return userWaypoints;
    }

    @JsonProperty("full_waypoints")
    public CoordinateDTO[] getFullWaypoints() {
        return fullWaypoints;
    }

    @JsonProperty("transportation_type")
    public String getTransportationType() {
        return transportationType;
    }

    @JsonProperty("notify_for_event_types")
    public EventTypeDTO[] getNotifyEventTypes() {
        return notifyEventTypes;
    }

    public void setUserWaypoints(CoordinateDTO[] userWaypoints) {
        this.userWaypoints = userWaypoints;
    }

    public void setFullWaypoints(CoordinateDTO[] fullWaypoints) {
        this.fullWaypoints = fullWaypoints;
    }
    
    public void setNotifyEventTypes(EventTypeDTO[] notifyEventTypes) {
        this.notifyEventTypes = notifyEventTypes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public void setNotify(NotifyDTO notify) {
        this.notify = notify;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        RouteDTO rhs = (RouteDTO) obj;
        return new EqualsBuilder()
                .append(this.active, rhs.active)
                .append(this.id, rhs.id)
                .append(this.notify, rhs.notify)
                .append(this.notifyEventTypes, rhs.notifyEventTypes)
                .append(this.transportationType, rhs.transportationType)
                .append(this.userWaypoints, rhs.userWaypoints)
                .append(this.fullWaypoints, rhs.fullWaypoints)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Arrays.deepHashCode(this.userWaypoints);
        hash = 83 * hash + Arrays.deepHashCode(this.fullWaypoints);
        hash = 83 * hash + Objects.hashCode(this.transportationType);
        hash = 83 * hash + Arrays.deepHashCode(this.notifyEventTypes);
        hash = 83 * hash + Objects.hashCode(this.notify);
        hash = 83 * hash + (this.active ? 1 : 0);
        return hash;
    }

}

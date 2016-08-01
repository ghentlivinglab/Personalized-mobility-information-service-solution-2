package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Objects;

public class TravelDTO {
    private String id;
    private String name;
    private String [] timeInterval;
    private boolean arrivalTime;
    private boolean [] recurring;
    private AddressDTO startpoint;
    private AddressDTO endpoint;

    public TravelDTO() {
        this.id = "";
        this.timeInterval = new String [2];
        timeInterval[0] = "";
        timeInterval[1] = "";
        this.recurring = new boolean [7];
        for (int i = 0; i < 7; i++) {
            recurring[i] = false;
        }
        
    }

    public TravelDTO(String id, String name, String[] timeInterval, boolean arrivalTime, boolean[] recurring, AddressDTO startpoint, AddressDTO endpoint) {
        this.id = id;
        this.name = name;
        this.timeInterval = timeInterval;
        this.arrivalTime = arrivalTime;
        this.recurring = recurring;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("time_interval")
    public String[] getTimeInterval() {
        return timeInterval;
    }

    @JsonProperty("is_arrival_time")
    public boolean isArrivalTime() {
        return arrivalTime;
    }

    @JsonProperty("recurring")
    public boolean[] getRecurring() {
        return recurring;
    }

    @JsonProperty("startpoint")
    public AddressDTO getStartpoint() {
        return startpoint;
    }

    @JsonProperty("endpoint")
    public AddressDTO getEndpoint() {
        return endpoint;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeInterval(String[] timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setArrivalTime(boolean arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setRecurring(boolean[] recurring) {
        this.recurring = recurring;
    }

    public void setStartpoint(AddressDTO startpoint) {
        this.startpoint = startpoint;
    }

    public void setEndpoint(AddressDTO endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Arrays.deepHashCode(this.timeInterval);
        hash = 71 * hash + (this.arrivalTime ? 1 : 0);
        hash = 71 * hash + Arrays.hashCode(this.recurring);
        hash = 71 * hash + Objects.hashCode(this.startpoint);
        hash = 71 * hash + Objects.hashCode(this.endpoint);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TravelDTO other = (TravelDTO) obj;
        if (this.arrivalTime != other.arrivalTime) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Arrays.deepEquals(this.timeInterval, other.timeInterval)) {
            return false;
        }
        if (!Arrays.equals(this.recurring, other.recurring)) {
            return false;
        }
        if (!Objects.equals(this.startpoint, other.startpoint)) {
            return false;
        }
        if (!Objects.equals(this.endpoint, other.endpoint)) {
            return false;
        }
        return true;
    }

    
    
    
}

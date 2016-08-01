/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.event;

import java.util.List;
import models.Transportation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author hannedesutter
 */
public class EventType {
    
    private final String type;
    private List<Transportation> transportationTypes;

    public EventType(String type, List<Transportation> transportationTypes) {
        this.type = type;
        this.transportationTypes = transportationTypes;
    }
    
    /**
     * Returns the main type of the eventtype.
     * @return String type
     */
    public String getType() {
        return type;
    }
    
    /**
     * The getter of the transportationTypes.
     * @return 
     */
    public List<Transportation> getTransportationTypes(){
        return transportationTypes;
    }
    
    /**
     * Setter for the transportationTypes
     * @param transportationTypes the new relevant transportation types
     */
    public void setTransportationTypes(List<Transportation> transportationTypes) {
        this.transportationTypes = transportationTypes;
    } 

    /**
     * The override method for the hashcode function for EventType.
     * @return 
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(type).
                toHashCode();
    }

    /**
     * The override of the equals method for Event types.
     * @param obj
     * @return 
     */
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
        EventType rhs = (EventType) obj;
        return new EqualsBuilder().
                append(type,rhs.type).
                isEquals();
    }

    
}

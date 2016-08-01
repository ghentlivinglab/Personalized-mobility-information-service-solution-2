package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;


public class EventTypeDTO {
    private String type;

    public EventTypeDTO() {
        this.type = "";
    }

    public EventTypeDTO(String type) {
        this.type = type;
    }


    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        EventTypeDTO rhs = (EventTypeDTO) obj;
        return new EqualsBuilder().
                append(type,rhs.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.type);
        return hash;
    }

    
}

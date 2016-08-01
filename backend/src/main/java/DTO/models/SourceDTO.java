package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
*
*/
public class SourceDTO {
    private String name;
    private String iconURL;

    public SourceDTO() {
        this.name = "";
        this.iconURL = "";
    }

    public SourceDTO(String name, String iconURL) {
        this.name = name;
        this.iconURL = iconURL;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("icon_url")
    public String getIconURL() {
        return iconURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) { 
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        SourceDTO rhs = (SourceDTO) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(iconURL, rhs.iconURL)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.iconURL);
        return hash;
    }

    
}

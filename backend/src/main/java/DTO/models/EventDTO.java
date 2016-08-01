package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class EventDTO {

    private String id;
    private CoordinateDTO coordinates;
    private boolean active;
    private String publicationTime;
    private String lastEditTime;
    private String description;
    private String formattedAddress;
    private JamDTO[] jams;
    private EventTypeDTO type;
    private String[] relevantTransportationTypes;

    public EventDTO() {
        this.id = "";
        this.publicationTime = "";
        this.lastEditTime = "";
        this.jams = new JamDTO[0];
        this.type = new EventTypeDTO();
        this.relevantTransportationTypes = new String[0];
    }

    public EventDTO(String id, CoordinateDTO coordinates, boolean active, String publicationTime,
            String lastEditTime, String description, String formattedAddress, JamDTO[] jams, EventTypeDTO type,
            String[] relevantTransportationTypes) {
        this.id = id;
        this.coordinates = coordinates;
        this.active = active;
        this.publicationTime = publicationTime;
        this.lastEditTime = lastEditTime;
        this.description = description;
        this.formattedAddress = formattedAddress;
        this.jams = jams;
        this.type = type;
        this.relevantTransportationTypes = relevantTransportationTypes;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("coordinates")
    public CoordinateDTO getCoordinates() {
        return coordinates;
    }

    @JsonProperty("active")
    public boolean isActive() {
        return active;
    }

    @JsonProperty("publication_time")
    public String getPublicationTime() {
        return publicationTime;
    }

    @JsonProperty("last_edit_time")
    public String getLastEditTime() {
        return lastEditTime;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("formatted_address")
    public String getFormattedAddress() {
        return formattedAddress;
    }

    @JsonProperty("jams")
    public JamDTO[] getJams() {
        return jams;
    }

    @JsonProperty("type")
    public EventTypeDTO getType() {
        return type;
    }

    @JsonProperty("relevant_for_transportation_types")
    public String[] getRelevantTransportationTypes() {
        return relevantTransportationTypes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCoordinates(CoordinateDTO coordinates) {
        this.coordinates = coordinates;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public void setJams(JamDTO[] jams) {
        this.jams = jams;
    }

    public void setType(EventTypeDTO type) {
        this.type = type;
    }

    public void setRelevantTransportationTypes(String[] relevantTransportationTypes) {
        this.relevantTransportationTypes = relevantTransportationTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        EventDTO rhs = (EventDTO) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(type, rhs.type)
                .append(description, rhs.description)
                .append(lastEditTime, rhs.lastEditTime)
                .append(publicationTime, rhs.publicationTime)
                .append(active, rhs.active)
                .append(coordinates, rhs.coordinates)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.coordinates);
        hash = 97 * hash + (this.active ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.publicationTime);
        hash = 97 * hash + Objects.hashCode(this.lastEditTime);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Arrays.deepHashCode(this.jams);
        hash = 97 * hash + Objects.hashCode(this.type);
        return hash;
    }

}

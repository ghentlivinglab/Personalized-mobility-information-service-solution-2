package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.CRUDModel;
import database_v2.models.ForeignKeyAttribute;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public class LocationEventDBModel implements CRUDModel {

    private Integer locationEventId;
    private Integer locationId;
    private String eventId;
    private Boolean deleted;

    private static final String tableName = "location_event";
    private final List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        out.add(new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class));
        out.add(new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true));
        return out;
    }

    public LocationEventDBModel() {
    }

    public LocationEventDBModel(Integer locationId, String eventId, Boolean deleted) {
        this.locationId = locationId;
        this.eventId = eventId;
        this.deleted = deleted;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        // all attributes are required
        return allAttributes;
    }

    @Override
    public List<Attribute> getAllAttributeList() {
        return allAttributes;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void setId(int id) {
        setLocationEventId(id);
    }

    @Override
    public int getId() {
        return getLocationEventId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    public Integer getLocationEventId() {
        return locationEventId;
    }

    public static Attribute getLocationEventIdAttribute() {
        return new Attribute("locationEventId", "location_eventid", AttributeType.INTEGER, tableName, false);
    }

    public void setLocationEventId(Integer locationEventId) {
        this.locationEventId = locationEventId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public static ForeignKeyAttribute<LocationDBModel> getLocationIdAttribute() {
        return new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class);
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getEventId() {
        return eventId;
    }

    public static Attribute getEventIdAttribute() {
        return new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true);
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public static Attribute getDeletedAttribute() {
        return new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true);
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocationEventDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        LocationEventDBModel oth = (LocationEventDBModel) obj;
        return new EqualsBuilder()
                .append(locationEventId, oth.locationEventId)
                .append(locationId, oth.locationId)
                .append(eventId, oth.eventId)
                .append(deleted, oth.deleted)
                .isEquals();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(4129, 83)
                .append(locationEventId)
                .append(locationId)
                .append(eventId)
                .append(deleted)
                .toHashCode();
    }

}

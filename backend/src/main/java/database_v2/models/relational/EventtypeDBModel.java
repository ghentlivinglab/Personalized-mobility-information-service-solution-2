package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.CRUDModel;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for the eventtype table in the relational database.
 * <p>
 * Schema:
 * <pre>
 * +-------------------------------+----------+
 * |          Columnname           | datatype |
 * +-------------------------------+----------+
 * | eventtypeid                   | serial   |
 * | type                          | text     |
 * | relevant_transportation_types | text[]   |
 * +-------------------------------+----------+
 * </pre>
 *
 */
public class EventtypeDBModel implements CRUDModel {

    private Integer eventtypeId;
    private String type;
    private Array relevantTransportTypes;
    private static final String tableName = "eventtype";
    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new Attribute("type", "type", AttributeType.TEXT, tableName, true));

        // non required
        out.add(new Attribute("relevantTransportTypes", "relevant_transportation_types", AttributeType.ARRAY, tableName, false));
        return out;
    }

    /**
     * Default empty constructor. Needed by CRUDdao for reflection.
     */
    public EventtypeDBModel() {
    }

    /**
     * Create a new instance.
     *
     * @param type The name of the type.
     * @param relevantTransportTypes Array of all relevant transport types.
     */
    public EventtypeDBModel(String type, Array relevantTransportTypes) {
        this.type = type;
        this.relevantTransportTypes = relevantTransportTypes;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new Attribute("type", "type", AttributeType.TEXT, tableName, true));

        // non required
        if (relevantTransportTypes != null) {
            out.add(new Attribute("relevantTransportTypes", "relevant_transportation_types", AttributeType.ARRAY, tableName, false));
        }
        return out;
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
        setEventtypeId(id);
    }

    @Override
    public int getId() {
        return getEventtypeId();
    }

    @Override
    public String getIdColumnName() {
        return "eventtypeid";
    }

    /**
     * Return id of this eventtype.
     * @return d of this eventtype.
     */
    public Integer getEventtypeId() {
        return eventtypeId;
    }

    /**
     * Return attribute of the id of this eventtype.
     * @return attribute of the id
     */
    public static Attribute getEventtypeIdAttribute() {
        return new Attribute("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true);
    }

    /**
     * Set the id of the eventtype
     * @param eventtypeId new id of the eventtype
     */
    public void setEventtypeId(Integer eventtypeId) {
        this.eventtypeId = eventtypeId;
    }

    /**
     * Get the name of the eventtype
     * @return name of the eventtype
     */
    public String getType() {
        return type;
    }

    /**
     * Get the attribute of the name of the eventtype
     * @return attribute of the name
     */
    public static Attribute getTypeAttribute() {
        return new Attribute("type", "type", AttributeType.TEXT, tableName, true);
    }

    /**
     * Set the name of the eventtype
     * @param type name of the eventtype
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get an array of all relevant transport types.
     * @return array of all relevant transport types.
     */
    public Array getRelevantTransportTypes() {
        return relevantTransportTypes;
    }

    /**
     * Get the attribute of the array of all relevant transport types.
     * @return attribute of the array of all relevant transport types.
     */
    public static Attribute getRelevantTransportTypesAttribute() {
        return new Attribute("relevantTransportTypes", "relevant_transportation_types", AttributeType.ARRAY, tableName, false);
    }

    /**
     * set an array of all relevant transport types.
     * @param relevantTransportTypes array of all relevant transport types.
     */
    public void setRelevantTransportTypes(Array relevantTransportTypes) {
        this.relevantTransportTypes = relevantTransportTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventtypeDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        EventtypeDBModel oth = (EventtypeDBModel) obj;
        return new EqualsBuilder()
                .append(eventtypeId, oth.eventtypeId)
                .append(type, oth.type)
                .append(relevantTransportTypes, oth.relevantTransportTypes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(2625, 6047)
                .append(eventtypeId)
                .append(type)
                .append(relevantTransportTypes)
                .toHashCode();
    }

}

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
 * Model for the street table in the relational database.
 * <p>
 * Schema:
 * <pre>
 * +------------+----------+----------------+
 * | Columnname | datatype |  foreign key   |
 * +------------+----------+----------------+
 * | streetid   | serial   |                |
 * | cityid     | integer  | => city.cityid |
 * | name       | text     |                |
 * +------------+----------+----------------+
 * </pre>
 */
public class StreetDBModel implements CRUDModel {

    private Integer streetId;
    private Integer cityId;
    private String name;
    private static final String tableName = "street";
    
    private static List<Attribute> allAttributes = fillAttributes();
    
    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        out.add(new ForeignKeyAttribute<>("cityId", "cityid", AttributeType.INTEGER, tableName, true, CityDBModel.class));
        out.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        return out;
    }
    

    /**
     * Default empty constructor. Only used for reflection in CRUDdao
     */
    public StreetDBModel() {
    }

    /**
     * Create new StreetDBModel
     * @param cityId ID of city
     * @param name Streetname
     */
    public StreetDBModel(Integer cityId, String name) {
        this.cityId = cityId;
        this.name = name;
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
        setStreetId(id);
    }

    @Override
    public int getId() {
        return getStreetId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    /**
     * get primary key id from this street
     * @return primary key id
     */
    public Integer getStreetId() {
        return streetId;
    }
    
    /**
     * Get the attribute of the StreetId (this is the primary key).
     * @return the Attribute of the streetId (primary key)
     */
    public static Attribute getStreetIdAttribute() {
        return new Attribute("streetId", "streetid", AttributeType.INTEGER, tableName, true);
    }

    /**
     * set primary key id from this street
     * @param streetId primary key id of the street
     */
    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    /**
     * get the primary key id from the city where the street lies in
     * @return primary key id from the city
     */
    public Integer getCityId() {
        return cityId;
    }
    
    /**
     * Get the ForeignKeyAttribute of the cityId
     * @return ForeignKeyAttribute of the cityId
     */
    public static ForeignKeyAttribute<CityDBModel> getCityIdAttribute() {
        return new ForeignKeyAttribute<>("cityId", "cityid", AttributeType.INTEGER, tableName, true, CityDBModel.class);
    }

    /**
     * set the primary key id from the city where the street lies in
     * @param cityId primary key id from the city
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * get the name of the street
     * @return name of the street
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the attribute of the name
     * @return attribute of the name
     */
    public static Attribute getNameAttribute() {
        return new Attribute("name", "name", AttributeType.TEXT, tableName, true);
    }

    /**
     * set the name of the street
     * @param name name of the street
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof StreetDBModel)) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        StreetDBModel oth = (StreetDBModel) obj;
        return new EqualsBuilder()
                .append(streetId, oth.streetId)
                .append(cityId, oth.cityId)
                .append(name, oth.name)
                .isEquals();
    }
    
   @Override 
   public int hashCode() {
       return new HashCodeBuilder(2957, 1377)
               .append(streetId)
               .append(cityId)
               .append(name)
               .toHashCode();
   }
}

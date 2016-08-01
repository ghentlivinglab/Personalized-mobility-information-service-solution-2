/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * This class models the address table from the relational database.
 * <p>
 * Schema:
 * <pre>
 * +-------------+----------+--------------------+
 * | Columnname  | datatype |  foreign key       |
 * +-------------+----------+--------------------+
 * | addressid   | serial   |                    |
 * | streetid    | integer  | => street.streetid |
 * | housenumber | text     |                    |
 * | latitude    | numeric  |                    |
 * | longitude   | numeric  |                    |
 * | cartesianX  | numeric  |                    |
 * | cartesianY  | numeric  |                    |
 * +-------------+----------+--------------------+
 * </pre>
 *
 */
public class AddressDBModel implements CRUDModel {

    private static final String tableName = "address";

    private Integer addressId;
    private Integer streetId;
    private String housenumber;
    private Double latitude;
    private Double longitude;
    private  Double cartesianX, cartesianY;

    public static final Attribute CARTESIAN_X_ATTRIBUTE
            = new Attribute("cartesianX", "cartesianX", AttributeType.DOUBLE, tableName, false);
    public static final Attribute CARTESIAN_Y_ATTRIBUTE
            = new Attribute("cartesianY", "cartesianY", AttributeType.DOUBLE, tableName, false);

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();

        // required
        out.add(new ForeignKeyAttribute<>("streetId", "streetid", AttributeType.INTEGER, tableName, true, StreetDBModel.class));
        out.add(new Attribute("housenumber", "housenumber", AttributeType.TEXT, tableName, true));

        // not required
        out.add(new Attribute("latitude", "latitude", AttributeType.DOUBLE, tableName, false));
        out.add(new Attribute("longitude", "longitude", AttributeType.DOUBLE, tableName, false));
        out.add(CARTESIAN_X_ATTRIBUTE);
        out.add(CARTESIAN_Y_ATTRIBUTE);
        return out;
    }

    /**
     * Default empty constructor, only used for reflection in CRUDdao.
     */
    public AddressDBModel() {
    }

    /**
     * Creates new AddressDBModel
     *
     * @param streetId The foreign key to the street of this address
     * @param housenumber The house number
     * @param latitude The latitude
     * @param longitude the Longitude
     * @param cartesianX the Cartesian coordinate (x-axis)
     * @param cartesianY the Cartesian coordinate (y-axis)
     */
    public AddressDBModel(Integer streetId, String housenumber, Double latitude, Double longitude,
            Double cartesianX, Double cartesianY) {
        this.streetId = streetId;
        this.housenumber = housenumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cartesianX = cartesianX;
        this.cartesianY = cartesianY;
    }

    /**
     * Creates new AddressDBModel, only required attributes are needed with this
     * constructor.
     *
     * @param streetId the foreign key to the street of this address.
     * @param housenumber The housenumber of this address.
     */
    public AddressDBModel(Integer streetId, String housenumber) {
        this.streetId = streetId;
        this.housenumber = housenumber;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();

        // required
        out.add(new ForeignKeyAttribute<>("streetId", "streetid", AttributeType.INTEGER, tableName, true, StreetDBModel.class));
        out.add(new Attribute("housenumber", "housenumber", AttributeType.TEXT, tableName, true));

        // not required
        if (latitude != null) {
            out.add(new Attribute("latitude", "latitude", AttributeType.DOUBLE, tableName, false));
        }
        if (longitude != null) {
            out.add(new Attribute("longitude", "longitude", AttributeType.DOUBLE, tableName, false));
        }
        if (cartesianX != null) {
            out.add(CARTESIAN_X_ATTRIBUTE);
        }
        if (cartesianY != null) {
            out.add(CARTESIAN_Y_ATTRIBUTE);
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
        setAddressId(id);
    }

    @Override
    public int getId() {
        return getAddressId();
    }

    @Override
    public String getIdColumnName() {
        return "addressid";
    }

    /**
     * get the addressId (primary key)
     *
     * @return the addressId (primary key)
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * Get the attribute of the addressId (primary key)
     *
     * @return Attribute of the addressId (primary key)
     */
    public static Attribute getAddressIdAttribute() {
        return new Attribute("addressId", "addressid", AttributeType.INTEGER, tableName, false);
    }

    /**
     * set the addressId (primary key)
     *
     * @param addressId
     */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    /**
     * Get the foreign key to the street of this address.
     *
     * @return The foreign key to the street
     */
    public Integer getStreetId() {
        return streetId;
    }

    /**
     * Get the ForeignKeyAttribute of the street.
     *
     * @return the foreignKeyAttribute of the street
     */
    public static ForeignKeyAttribute<StreetDBModel> getStreetIdAttribute() {
        return new ForeignKeyAttribute("streetId", "streetid", AttributeType.INTEGER, tableName, true, StreetDBModel.class);
    }

    /**
     * Set the foreign key to the street.
     *
     * @param streetId
     */
    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    /**
     * Get the housenumber of this address
     *
     * @return the housenumber
     */
    public String getHousenumber() {
        return housenumber;
    }

    /**
     * Get the attribute of the housenumber of this address
     *
     * @return the attribute of the housenumber
     */
    public static Attribute getHousenumberAttribute() {
        return new Attribute("housenumber", "housenumber", AttributeType.TEXT, tableName, true);
    }

    /**
     * set the housenumber of this address.
     *
     * @param housenumber the new housenumber of the address
     */
    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    /**
     * get the latitude from this address.
     *
     * @return latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * get the attribute of the latitude
     *
     * @return attribute of the latitude
     */
    public static Attribute getLatitudeAttribute() {
        return new Attribute("latitude", "latitude", AttributeType.DOUBLE, tableName, false);
    }

    /**
     * set the latitude of this address
     *
     * @param latitude the new latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * get the longitude of this address
     *
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * get the Attribute of the longitude
     *
     * @return Attribute of the longitude
     */
    public static Attribute getLongitudeAttribute() {
        return new Attribute("longitude", "longitude", AttributeType.DOUBLE, tableName, false);
    }

    /**
     * set the longitude of this address
     *
     * @param longitude longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getCartesianX() {
        return cartesianX;
    }

    public void setCartesianX(Double cartesianX) {
        this.cartesianX = cartesianX;
    }

    public Double getCartesianY() {
        return cartesianY;
    }

    public void setCartesianY(Double cartesianY) {
        this.cartesianY = cartesianY;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AddressDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        AddressDBModel oth = (AddressDBModel) obj;
        return new EqualsBuilder()
                .append(addressId, oth.addressId)
                .append(streetId, oth.streetId)
                .append(housenumber, oth.housenumber)
                .append(latitude, oth.latitude)
                .append(longitude, oth.longitude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3703, 271)
                .append(addressId)
                .append(streetId)
                .append(housenumber)
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }

}

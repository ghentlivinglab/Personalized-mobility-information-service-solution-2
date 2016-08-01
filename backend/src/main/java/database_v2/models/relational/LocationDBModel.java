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
 * This class models the location table from the relational database.
 * <p>
 * Schema:
 * <pre>
 * +-------------------+----------+----------------------+
 * |    Columnname     | datatype |     foreign key      |
 * +-------------------+----------+----------------------+
 * | account_addressid | serial   |                      |
 * | accountid         | integer  | => account.accountid |
 * | addressid         | integer  | => address.addressid |
 * | name              | text     |                      |
 * | radius            | integer  |                      |
 * | active            | boolean  |                      |
 * | notify_email      | boolean  |                      |
 * | notify_cell       | boolean  |                      |
 * +-------------------+----------+----------------------+
 * </pre>
 *
 */
public class LocationDBModel implements CRUDModel {

    private Integer locationId;
    private Integer accountId;
    private Integer addressId;
    private String name;
    private Integer radius;
    private Boolean active;
    private Boolean notifyEmail;
    private Boolean notifyCell;
    private static final String tableName = "location";

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        out.add(new ForeignKeyAttribute("addressId", "addressid", AttributeType.INTEGER, tableName, true, AddressDBModel.class));

        // not required
        out.add(new Attribute("name", "name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("radius", "radius", AttributeType.INTEGER, tableName, false));
        out.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
        return out;
    }

    public LocationDBModel() {
        // fill in default values
        this.notifyEmail = false;
        this.notifyCell = false;
    }

    public LocationDBModel(Integer accountId, Integer addressId, String name,
            Integer radius, Boolean active, Boolean notifyEmail, Boolean notifyCell) {
        this.accountId = accountId;
        this.addressId = addressId;
        this.name = name;
        this.radius = radius;
        this.active = active;
        this.notifyEmail = notifyEmail;
        this.notifyCell = notifyCell;
    }

    public LocationDBModel(Integer accountId, Integer addressId) {
        this.accountId = accountId;
        this.addressId = addressId;

        // fill in default values
        this.notifyEmail = false;
        this.notifyCell = false;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        out.add(new ForeignKeyAttribute("addressId", "addressid", AttributeType.INTEGER, tableName, true, AddressDBModel.class));

        // not required
        if (name != null) {
            out.add(new Attribute("name", "name", AttributeType.TEXT, tableName, false));
        }
        if (radius != null) {
            out.add(new Attribute("radius", "radius", AttributeType.INTEGER, tableName, false));
        }
        if (active != null) {
            out.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        }
        if (notifyEmail != null) {
            out.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        }
        if (notifyCell != null) {
            out.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
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
        setLocationId(id);
    }

    @Override
    public int getId() {
        return getLocationId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    public Integer getLocationId() {
        return locationId;
    }

    public static Attribute getLocationIdAttribute() {
        return new Attribute("locationId", "locationid", AttributeType.INTEGER, tableName, false);
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public static ForeignKeyAttribute<AccountDBModel> getAccountIdAttribute() {
        return new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class);
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public static ForeignKeyAttribute<AddressDBModel> getAddressIdAttribute() {
        return new ForeignKeyAttribute<>("addressId", "addressid", AttributeType.INTEGER, tableName, true, AddressDBModel.class);
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public static Attribute getNameAttribute() {
        return new Attribute("name", "name", AttributeType.TEXT, tableName, false);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRadius() {
        return radius;
    }

    public static Attribute getRadiusAttribute() {
        return new Attribute("radius", "radius", AttributeType.INTEGER, tableName, false);
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Boolean getActive() {
        return active;
    }

    public static Attribute getActiveAttribute() {
        return new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false);
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getNotifyEmail() {
        return notifyEmail;
    }

    public static Attribute getNotifyEmailAttribute() {
        return new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false);
    }

    public void setNotifyEmail(Boolean notifyEmail) {
        this.notifyEmail = notifyEmail;
    }

    public Boolean getNotifyCell() {
        return notifyCell;
    }

    public static Attribute getNotifyCellAttribute() {
        return new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false);
    }

    public void setNotifyCell(Boolean notifyCell) {
        this.notifyCell = notifyCell;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocationDBModel)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        LocationDBModel oth = (LocationDBModel) obj;
        return new EqualsBuilder()
                .append(locationId, oth.locationId)
                .append(accountId, oth.accountId)
                .append(addressId, oth.addressId)
                .append(name, oth.name)
                .append(radius, oth.radius)
                .append(active, oth.active)
                .append(notifyEmail, oth.notifyEmail)
                .append(notifyCell, oth.notifyCell)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(91, 8493)
                .append(locationId)
                .append(accountId)
                .append(addressId)
                .append(name)
                .append(radius)
                .append(active)
                .append(notifyEmail)
                .append(notifyCell)
                .toHashCode();
    }

}

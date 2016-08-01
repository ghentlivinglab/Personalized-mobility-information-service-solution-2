/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.CRUDModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class models the city table from the relational database.
 * <p>
 * Schema:
 * <pre>
 * +-------------+----------+-------------+
 * | Columnname  | datatype | foreign key |
 * +-------------+----------+-------------+
 * | cityid      | serial   |             |
 * | city        | text     |             |
 * | postal_code | text     |             |
 * | country     | text     |             |
 * +-------------+----------+-------------+
 * </pre>
 */
public class CityDBModel implements CRUDModel {

    private Integer cityId;
    private String city;
    private String postalCode;
    private String country;
    private static final String tableName = "city";

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new Attribute("city", "city", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("postalCode", "postal_code", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("country", "country", AttributeType.TEXT, tableName, true));
        return out;
    }

    public CityDBModel() {
    }

    public CityDBModel(String city, String postalCode, String country) {
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
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
        setCityId(id);
    }

    @Override
    public int getId() {
        return getCityId();
    }

    @Override
    public String getIdColumnName() {
        return "cityid";
    }

    public Integer getCityId() {
        return cityId;
    }
    
    public static Attribute getCityIdAttribute() {
        return new Attribute("cityId", "cityid", AttributeType.INTEGER, tableName, false);
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }
    
    public static Attribute getCityAttribute() {
        return new Attribute("city", "city", AttributeType.TEXT, tableName, true);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }
    
    public static Attribute getPostalCodeAttribute() {
        return new Attribute("postalCode", "postal_code", AttributeType.TEXT, tableName, true);
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }
    
    public static Attribute getCountryAttribute() {
        return new Attribute("country", "country", AttributeType.TEXT, tableName, true);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CityDBModel))  {
            return false;
        }
        if(obj == this) {
            return true;
        }
        CityDBModel oth = (CityDBModel) obj;
        return new EqualsBuilder()
                .append(cityId, oth.cityId)
                .append(city, oth.city)
                .append(postalCode, oth.postalCode)
                .append(country, oth.country)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(457, 705)
                .append(cityId)
                .append(city)
                .append(postalCode)
                .append(country)
                .toHashCode();
    }
    
}

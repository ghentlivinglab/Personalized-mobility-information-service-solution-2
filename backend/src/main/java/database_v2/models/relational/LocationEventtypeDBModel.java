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

/**
 *
 */
public class LocationEventtypeDBModel implements CRUDModel {
    private Integer locationEventtypeId;
    private Integer locationId;
    private Integer eventtypeId;
    private static final String tableName = "location_eventtype";
    
    private static List<Attribute> allAttributes = fillAttributes();
    
    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        out.add(new ForeignKeyAttribute<>("locationId", "locationid", AttributeType.INTEGER, tableName, true, LocationDBModel.class));
        out.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));
        return out;
    }

    public LocationEventtypeDBModel() {}
    
    public LocationEventtypeDBModel(Integer locationId, Integer eventtypeId) {
        this.locationId = locationId;
        this.eventtypeId = eventtypeId;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        // all attributes are required, so...
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
        setLocationEventtypeId(id);
    }

    @Override
    public int getId() {
        return getLocationEventtypeId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    public Integer getLocationEventtypeId() {
        return locationEventtypeId;
    }
    
    public static Attribute getLocationEventtypeIdAttribute() {
        return new Attribute("locationEventtypeId", "location_eventtypeid", AttributeType.INTEGER, tableName, false);
    }

    public void setLocationEventtypeId(Integer locationEventtypeId) {
        this.locationEventtypeId = locationEventtypeId;
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

    public Integer getEventtypeId() {
        return eventtypeId;
    }
    
    public static ForeignKeyAttribute<EventtypeDBModel> getEventtypeIdAttribute() {
        return new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class);
    }

    public void setEventtypeId(Integer eventtypeId) {
        this.eventtypeId = eventtypeId;
    }
    
}

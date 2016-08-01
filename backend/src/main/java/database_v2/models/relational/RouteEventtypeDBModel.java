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
 * This class represents the route_eventtype table from the relational database.
 * <pre>
 * +-------------------+----------+--------------------------+
 * |    Columnname     | datatype |       foreign key        |
 * +-------------------+----------+--------------------------+
 * | route_eventtypeid | serial   |                          |
 * | routeid           | integer  | => route.routeid         |
 * | eventtypeid       | integer  | => eventtype.eventtypeid |
 * +-------------------+----------+--------------------------+
 * </pre>
 *
 */
public class RouteEventtypeDBModel implements CRUDModel {

    private Integer routeEventtypeId;
    private Integer routeId;
    private Integer eventtypeId;
    private static final String tableName = "route_eventtype";

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        out.add(new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class));
        out.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));

        return out;
    }

    public RouteEventtypeDBModel() {
    }

    public RouteEventtypeDBModel(Integer routeId, Integer eventtypeId) {
        this.routeId = routeId;
        this.eventtypeId = eventtypeId;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();
        out.add(new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class));
        out.add(new ForeignKeyAttribute<>("eventtypeId", "eventtypeid", AttributeType.INTEGER, tableName, true, EventtypeDBModel.class));

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
        setRouteEventtypeId(id);
    }

    @Override
    public int getId() {
        return getRouteEventtypeId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    public Integer getRouteEventtypeId() {
        return routeEventtypeId;
    }

    public static Attribute getRouteEventtypeIdAttribute() {
        return new Attribute("routeEventtypeId", "route_eventtypeid", AttributeType.INTEGER, tableName, true);
    }

    public void setRouteEventtypeId(Integer routeEventtypeId) {
        this.routeEventtypeId = routeEventtypeId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public static ForeignKeyAttribute<RouteDBModel> getRouteIdAttribute() {
        return new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class);
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
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

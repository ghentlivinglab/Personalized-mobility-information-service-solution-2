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
public class RouteEventDBModel implements CRUDModel {

    private Integer routeEventId;
    private Integer routeId;
    private String eventId;
    private Boolean deleted;

    private static final String tableName = "route_event";
    private static final List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        out.add(new Attribute("routeId", "routeid", AttributeType.INTEGER, tableName, true));
        out.add(new Attribute("eventId", "eventid", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("deleted", "deleted", AttributeType.BOOLEAN, tableName, true));
        return out;
    }

    public RouteEventDBModel() {
    }

    public RouteEventDBModel(Integer routeId, String eventId, Boolean deleted) {
        this.routeId = routeId;
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
        setRouteEventId(id);
    }

    @Override
    public int getId() {
        return getRouteEventId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    public Integer getRouteEventId() {
        return routeEventId;
    }

    public static Attribute getRouteEventIdAttribute() {
        return new Attribute("routeEventId", "route_eventid", AttributeType.INTEGER, tableName, false);
    }

    public void setRouteEventId(Integer routeEventId) {
        this.routeEventId = routeEventId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public static ForeignKeyAttribute<RouteDBModel> getRouteIdAttribute() {
        return new ForeignKeyAttribute("routeId", "routeid", AttributeType.INTEGER, tableName, true, RouteDBModel.class);
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
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

}

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
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for the route table in the relational database.
 * <pre>
 * +----------------------+-------------+--------------------+
 * |      Columnname      |  datatype   |    foreign key     |
 * +----------------------+-------------+--------------------+
 * | routeid              | serial      |                    |
 * | travelid             | integer     | => travel.travelid |
 * | user_waypoints       | numeric[][] |                    |
 * | full_waypoints       | numeric[][] |                    |
 * | transportation_types | text        |                    |
 * | active               | boolean     |                    |
 * | notify_email         | boolean     |                    |
 * | notify_cell          | boolean     |                    |
 * +----------------------+-------------+--------------------+
 * </pre>
 *
 */
public class RouteDBModel implements CRUDModel {

    private static final String tableName = "route";

    private Integer routeId;
    private Integer travelId;
    private Array userWaypoints;
    private Array fullWaypoints;
    private String transportationType;
    private Boolean active;
    private Boolean notifyEmail;
    private Boolean notifyCell;

    public static final Attribute userWaypointsAttribute
            = new Attribute("userWaypoints", "user_waypoints", AttributeType.ARRAY, tableName, false);
    public static final Attribute fullWaypointsAttribute
            = new Attribute("fullWaypoints", "full_waypoints", AttributeType.ARRAY, tableName, false);

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new ForeignKeyAttribute<>("travelId", "travelid", AttributeType.INTEGER, tableName, true, TravelDBModel.class));

        // not required
        out.add(userWaypointsAttribute);
        out.add(fullWaypointsAttribute);
        out.add(new Attribute("transportationType", "transportation_type", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("active", "active", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("notifyEmail", "notify_email", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("notifyCell", "notify_cell", AttributeType.BOOLEAN, tableName, false));
        return out;
    }

    public RouteDBModel() {
        // fill in default values
        this.active = true;
        this.notifyEmail = false;
        this.notifyCell = false;
    }

    public RouteDBModel(Integer travelId, Array userWaypoints, Array fullwaypoints,
            String transportationType, Boolean active, Boolean notifyEmail, Boolean notifyCell) {
        this.travelId = travelId;
        this.userWaypoints = userWaypoints;
        this.fullWaypoints = fullwaypoints;
        this.transportationType = transportationType;
        this.active = active;
        this.notifyEmail = notifyEmail;
        this.notifyCell = notifyCell;
    }

    public RouteDBModel(Integer travelId) {
        this.travelId = travelId;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new ForeignKeyAttribute<>("travelId", "travelid", AttributeType.INTEGER, tableName, true, TravelDBModel.class));

        // not required
        if (userWaypoints != null) {
            out.add(userWaypointsAttribute);
        }
        if(fullWaypoints != null) {
            out.add(fullWaypointsAttribute);
        }
        if (transportationType != null) {
            out.add(new Attribute("transportationType", "transportation_type", AttributeType.TEXT, tableName, false));
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
        setRouteId(id);
    }

    @Override
    public int getId() {
        return getRouteId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }

    public Integer getRouteId() {
        return routeId;
    }

    public static Attribute getRouteIdAttribute() {
        return new Attribute("routeId", "routeid", AttributeType.INTEGER, tableName, false);
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getTravelId() {
        return travelId;
    }

    public static ForeignKeyAttribute<TravelDBModel> getTravelIdAttribute() {
        return new ForeignKeyAttribute<>("travelId", "travelid", AttributeType.INTEGER, tableName, true, TravelDBModel.class);
    }

    public void setTravelId(Integer travelId) {
        this.travelId = travelId;
    }

    public Array getUserWaypoints() {
        return userWaypoints;
    }

    public void setUserWaypoints(Array userWaypoints) {
        this.userWaypoints = userWaypoints;
    }

    public Array getFullWaypoints() {
        return fullWaypoints;
    }

    public void setFullWaypoints(Array fullWaypoints) {
        this.fullWaypoints = fullWaypoints;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public static Attribute getTransportationTypeAttribute() {
        return new Attribute("transportationType", "transportation_type", AttributeType.TEXT, tableName, false);
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
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
        if (!(obj instanceof RouteDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        RouteDBModel oth = (RouteDBModel) obj;
        return new EqualsBuilder()
                .append(routeId, oth.routeId)
                .append(travelId, oth.travelId)
                .append(userWaypoints, oth.userWaypoints)
                .append(fullWaypoints, oth.fullWaypoints)
                .append(transportationType, oth.transportationType)
                .append(active, oth.active)
                .append(notifyEmail, oth.notifyEmail)
                .append(notifyCell, oth.notifyCell)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3873, 2717)
                .append(routeId)
                .append(travelId)
                .append(userWaypoints)
                .append(fullWaypoints)
                .append(transportationType)
                .append(active)
                .append(notifyEmail)
                .append(notifyCell)
                .toHashCode();
    }

}

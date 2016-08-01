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
public class RouteGridpointDBModel implements CRUDModel {

    private Integer routeGridpointId;
    private Integer routeId;
    private Integer gridX, gridY;

    private static final String TABLENAME = "route_gridpoint";
    public static final Attribute ROUTE_GRIDPOINT_ID_ATTRIBUTE
            = new Attribute("routeGridpointId", "route_gridpointid", AttributeType.INTEGER, TABLENAME, false);
    public static final ForeignKeyAttribute<RouteDBModel> ROUTE_ID_ATTRIBUTE
            = new ForeignKeyAttribute<>("routeId", "routeid", AttributeType.INTEGER, TABLENAME, true, RouteDBModel.class);
    public static final Attribute GRID_X_ATTRIBUTE
            = new Attribute("gridX", "gridx", AttributeType.INTEGER, TABLENAME, true);
    public static final Attribute GRID_Y_ATTRIBUTE
            = new Attribute("gridY", "gridy", AttributeType.INTEGER, TABLENAME, true);

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        out.add(ROUTE_ID_ATTRIBUTE);
        out.add(GRID_X_ATTRIBUTE);
        out.add(GRID_Y_ATTRIBUTE);
        return out;
    }

    public RouteGridpointDBModel() {
    }

    public RouteGridpointDBModel(Integer routeId, Integer gridX, Integer gridY) {
        this.routeId = routeId;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        // all field are required
        return allAttributes;
    }

    @Override
    public List<Attribute> getAllAttributeList() {
        return allAttributes;
    }

    @Override
    public String getTableName() {
        return TABLENAME;
    }

    @Override
    public void setId(int id) {
        setRouteGridpointId(id);
    }

    @Override
    public int getId() {
        return getRouteGridpointId();
    }

    @Override
    public String getIdColumnName() {
        return TABLENAME + "id";
    }

    public Integer getRouteGridpointId() {
        return routeGridpointId;
    }

    public void setRouteGridpointId(Integer routeGridpointId) {
        this.routeGridpointId = routeGridpointId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getGridX() {
        return gridX;
    }

    public void setGridX(Integer gridX) {
        this.gridX = gridX;
    }

    public Integer getGridY() {
        return gridY;
    }

    public void setGridY(Integer gridY) {
        this.gridY = gridY;
    }

}

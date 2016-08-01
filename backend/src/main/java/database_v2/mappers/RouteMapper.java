package database_v2.mappers;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.searchTerms.SimpleSearchTerm;
import database_v2.exceptions.DataAccessException;
import database_v2.models.TableJoin;
import database_v2.models.relational.RouteDBModel;
import database_v2.models.relational.TravelDBModel;
import database_v2.searchTerms.SearchTerm;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Coordinate;
import models.event.EventType;
import models.Route;
import models.Transportation;

/**
 * This class provides some static function to do the mapping between db and application models for
 * Route.
 */
public class RouteMapper {

    private final EventtypeMapper eventtypeMapper;
    private final EventMapper eventMapper;

    /**
     * Create a new RouteMapper
     */
    public RouteMapper() {
        eventtypeMapper = new EventtypeMapper();
        eventMapper = new EventMapper();
    }

    /**
     * only for testing reasons
     *
     * @param eventtypeMapper
     * @param eventMapper
     */
    public RouteMapper(EventtypeMapper eventtypeMapper, EventMapper eventMapper) {
        this.eventtypeMapper = eventtypeMapper;
        this.eventMapper = eventMapper;
    }

    /**
     * Get all the routes from the given route as they are stored in the database. This results in a
     * map that maps id's on their Route application model.
     *
     * @param travelId The id of the travel of which the routes are to be fetched.
     * @param dac An abstraction of a connection
     * @param cache Access to all cached data
     * @return a map that maps id's on their Route application model
     * @throws DataAccessException Something went wrong with the underlying database
     */
    public Map<Integer, Route> getTravelRoutes(int travelId, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {
        CRUDdao cd = dac.getCRUDdao();
        TableJoin join = new TableJoin(TravelDBModel.class, RouteDBModel.class, RouteDBModel.getTravelIdAttribute());
        List<SearchTerm> search = Arrays.asList(
                new SimpleSearchTerm(TravelDBModel.getTravelIdAttribute(), travelId)
        );

        List<RouteDBModel> dbRoutes = cd.complexSearch(join, search, RouteDBModel.class);
        Map<Integer, Route> routes = new HashMap<>();
        for (RouteDBModel dbRoute : dbRoutes) {
            Route fetchedRoute = toDomainModel(dbRoute, dac, cache);
            routes.put(dbRoute.getId(), fetchedRoute);
        }
        return routes;
    }

    /**
     * Convert a route db model to a domain model.
     *
     * @param dbRoute The db model to be converted
     * @param dac Access to the database
     * @param cache Access to cached objects
     * @return Converted domain model
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Route toDomainModel(RouteDBModel dbRoute, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {
        List<Coordinate> fullWaypoints = toCoordsList(dbRoute.getFullWaypoints());
        List<Coordinate> userWaypoints = toCoordsList(dbRoute.getUserWaypoints());

        Map<Integer, EventType> eventTypes
                = eventtypeMapper.getRouteEventtypes(dbRoute.getId(), dac, cache);

        return new Route(
                userWaypoints,
                fullWaypoints,
                Transportation.fromString(dbRoute.getTransportationType()),
                eventTypes,
                !dbRoute.getActive(),
                new ArrayList<>() // TODO fix this
        );
    }

    /**
     * Convert a route application model to a db model.
     *
     * @param travelId The id of travel to which this route belongs
     * @param route the application model to be converted
     * @param conn An abstraction of a connection
     * @return The converted db model
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public RouteDBModel toDBModel(int travelId, Route route, Connection conn)
            throws DataAccessException {

        Array userWaypoints = toCoordsArray(route.getUserWaypoints(), conn);
        Array fullWaypoints = toCoordsArray(route.getFullWaypoints(), conn);

        RouteDBModel out = new RouteDBModel(
                travelId,
                userWaypoints,
                fullWaypoints,
                route.getTransportationType().toString(),
                !route.isMuted(),
                Boolean.FALSE, // TODO replace this placeholders
                Boolean.FALSE // TODO replace this placeholders
        );
        return out;

    }

    private List<Coordinate> toCoordsList(Array array)
            throws DataAccessException {
        List<Coordinate> coords = new ArrayList<>();

        // fetch and parse all coordinates from the database array
        try {
            // not all routes required waypoints, so this value can be null
            if (array != null) {
                // postgresql casts numeric to BigDecimal
                BigDecimal[][] bigDecims = (BigDecimal[][]) array.getArray();
                for (BigDecimal[] bd : bigDecims) {
                    Coordinate coord = new Coordinate(
                            bd[0].doubleValue(),
                            bd[1].doubleValue()
                    );
                    coords.add(coord);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return coords;
    }

    private Array toCoordsArray(List<Coordinate> coords, Connection conn)
            throws DataAccessException {
        if (coords == null || coords.isEmpty()) {
            return null;
        }
        // first we prepare some low level conversion to arrays
        Double[][] waypoints = new Double[coords.size()][2];
        for (int i = 0; i < waypoints.length; i++) {
            Coordinate wp = coords.get(i);
            waypoints[i][0] = wp.getLat();
            waypoints[i][1] = wp.getLon();
        }
        try {
            return conn.createArrayOf("numeric", waypoints);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}

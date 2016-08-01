package database_v2.utils;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DataAccessProvider;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.ForeignKeyNotFoundException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.relational.RouteGridpointDBModel;
import database_v2.searchTerms.SearchTerm;
import database_v2.searchTerms.SimpleSearchTerm;
import datacoupler.DataCoupler;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.Coordinate;
import models.Route;
import org.apache.commons.logging.LogFactory;
import org.javatuples.Pair;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Event en route preprocessing module. We draw in mind a grid over the area where events can be.
 * This grid has a discrete number of grid points. Each event and route is preprocessed and assigned
 * to certain grid points. (one for events, more for routes).
 */
public class Rasterizer {

    private final TaskExecutor executor;
    private final DataAccessProvider dap;

    private final double centerLat, centerLon;
    private final double rasterSize;
    private final double xmin, xmax, ymin, ymax;
    private final double gridSize = 18000; // how far does the grid go in all directions

    /**
     * create a new instance, handled by Spring
     *
     * @param dap Access to the database
     * @param executor Thread pool
     */
    public Rasterizer(DataAccessProvider dap, ThreadPoolTaskExecutor executor) {
        this.dap = dap;
        this.executor = executor;
        centerLat = Coordinate.refy;
        centerLon = Coordinate.refx;
        rasterSize = 100.0; // in meter
        xmin = centerLon - gridSize;
        xmax = centerLon + gridSize;
        ymin = centerLat - gridSize;
        ymax = centerLat + gridSize;
    }

    /**
     * This function will first rasterize the route. This means it will calculate all grid points
     * where this route runs through. These grid points are stored in the database. Then the
     * function will call the data coupler to couple the route to relevant events.
     *
     * @param routeId Id of the route to be processed
     * @param route the route itself
     * @param coupler event coupling module
     */
    public void processRoute(int routeId, Route route, DataCoupler coupler) {
        // the preprocessing is done on an other thread then the controller thread.
        // This way the user doesn't have to wait until the route is processed,
        // which is a time consuming calculation.
        executor.execute(() -> {
            Set<Pair<Integer, Integer>> gridPoints
                    = rasterize(route.getFullWaypoints());
            try {
                setGridPointsOfRoute(routeId, gridPoints);
            } catch (DataAccessException | RecordNotFoundException ex) {
                LogFactory.getLog(getClass()).error(
                        "Error in Rasterizer preprocessor while accessin db", ex);
            }
            // once we have calculated and inserted all route info, we can start
            // matching it with existing events
            coupler.coupleRoute(routeId, route, gridPoints);
        });
    }

    private Set<Pair<Integer, Integer>> rasterize(List<Coordinate> waypoints) {
        Set<Pair<Integer, Integer>> out = new HashSet<>();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            Coordinate beginCoord = waypoints.get(i);
            Coordinate endCoord = waypoints.get(i + 1);
            try {
                Pair<Pair<Double, Double>, Pair<Double, Double>> clippedLine
                        = cohSutClipLine(beginCoord, endCoord);
                DDA(clippedLine.getValue0(), clippedLine.getValue1(), out);
            } catch (OutOfRasterException ex) {
                // the entire line lies out of Ghent
                continue;
            }
        }
        return out;
    }

    private void DDA(Pair<Double, Double> start, Pair<Double, Double> end, Set<Pair<Integer, Integer>> set) {
        Pair<Integer, Integer> startGrid = cartesianToRaster(start);
        Pair<Integer, Integer> endGrid = cartesianToRaster(end);
        set.add(startGrid);
        double dx = end.getValue0() - start.getValue0();
        double dy = end.getValue1() - start.getValue1();
        int dxGrid = Math.abs(endGrid.getValue0() - startGrid.getValue0());
        int dyGrid = Math.abs(endGrid.getValue1() - startGrid.getValue1());
        int steps;
        if (dxGrid > dyGrid) {
            steps = dxGrid;
        } else {
            steps = dyGrid;
        }
        double xInc = dx / steps;
        double yInc = dy / steps;
        double x = start.getValue0();
        double y = start.getValue1();
        for (int i = 0; i < steps; i++) {
            x += xInc;
            y += yInc;
            set.add(new Pair<>(
                    (int) (Math.signum(x) * (((int) (Math.abs(x) / rasterSize)) + 1)),
                    (int) (Math.signum(y) * (((int) (Math.abs(y) / rasterSize)) + 1))
            ));
        }
    }

    public Pair<Integer, Integer> coordToRaster(Coordinate coord) {
        return new Pair<>(
                (int) (Math.signum(coord.getX()) * (((int) (Math.abs(coord.getX()) / rasterSize)) + 1)),
                (int) (Math.signum(coord.getY()) * (((int) (Math.abs(coord.getY()) / rasterSize)) + 1))
        );
    }

    private Pair<Integer, Integer> cartesianToRaster(Pair<Double, Double> cartPoint) {
        return new Pair<>(
                (int) (Math.signum(cartPoint.getValue0()) * (((int) (Math.abs(cartPoint.getValue0()) / rasterSize)) + 1)),
                (int) (Math.signum(cartPoint.getValue1()) * (((int) (Math.abs(cartPoint.getValue1()) / rasterSize)) + 1))
        );
    }

    private final int INSIDE = 0; // 0000
    private final int LEFT = 1;   // 0001
    private final int RIGHT = 2;  // 0010
    private final int BOTTOM = 4; // 0100
    private final int TOP = 8;    // 1000

    private int cohSutOutCode(double x, double y) {
        int code = INSIDE;

        if (x < xmin) {
            // left of clip window
            code |= LEFT;
        } else if (x > xmax) {
            // right of clip window
            code |= RIGHT;
        }

        if (y < ymin) {
            // below clip window
            code |= BOTTOM;
        } else if (y > ymax) {
            // above clip window
            code |= TOP;
        }

        return code;
    }

    private Pair<Pair<Double, Double>, Pair<Double, Double>> cohSutClipLine(
            Coordinate begin, Coordinate end)
            throws OutOfRasterException {
        double x0 = begin.getX();
        double x1 = end.getX();
        double y0 = begin.getY();
        double y1 = end.getY();

        int beginCode = cohSutOutCode(x0, y0);
        int endCode = cohSutOutCode(x1, y1);
        boolean accept = false;
        boolean changed = false;
        while (true) {
            if ((beginCode | endCode) == 0) {
                accept = true;
                break;
            } else if ((beginCode & endCode) != 0) {
                break;
            } else {
                changed = true;
                double newX, newY;
                int outCode = beginCode != 0 ? beginCode : endCode;
                if ((outCode & TOP) != 0) {
                    newX = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
                    newY = ymax;
                } else if ((outCode & BOTTOM) != 0) {
                    newX = x0 + (x1 - x0) * (ymin - y0) / (y1 - y0);
                    newY = ymin;
                } else if ((outCode & RIGHT) != 0) {
                    newY = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
                    newX = xmax;
                } else {
                    newY = y0 + (y1 - y0) * (xmin - x0) / (x1 - x0);
                    newX = xmin;
                }

                // Now we move outside point to intersection point to clip
                // and get ready for next pass.
                if (outCode == beginCode) {
                    x0 = newX;
                    y0 = newY;
                    beginCode = cohSutOutCode(x0, y0);
                } else {
                    x1 = newX;
                    y1 = newY;
                    endCode = cohSutOutCode(x1, y1);
                }
            }
        }
        if (accept) {
            if (!changed) {
                return new Pair<>(
                        new Pair<>(begin.getX(), begin.getY()),
                        new Pair<>(end.getX(), end.getY())
                );
            } else {
                return new Pair<>(
                        new Pair<>(x0, y0),
                        new Pair<>(x1, y1)
                );
            }
        } else {
            throw new OutOfRasterException();
        }
    }

    private void setGridPointsOfRoute(int routeId, Set<Pair<Integer, Integer>> gridpoints)
            throws DataAccessException, RecordNotFoundException {
        try (DataAccessContext dac = dap.getDataAccessContext()) {
            CRUDdao cd = dac.getCRUDdao();
            // fist, make sure that all old gridpoints from the route are removed
            // because this may be an update of an existing route
            List<SearchTerm> search = Arrays.asList(
                    new SimpleSearchTerm(RouteGridpointDBModel.ROUTE_ID_ATTRIBUTE, routeId)
            );
            cd.delete(RouteGridpointDBModel.class, search);

            // now we can start inserting the new gridpoints:
            for (Pair<Integer, Integer> pair : gridpoints) {
                RouteGridpointDBModel dbGridpoint
                        = new RouteGridpointDBModel(routeId, pair.getValue0(), pair.getValue1());
                try {
                    cd.create(dbGridpoint);
                } catch (AlreadyExistsException | ForeignKeyNotFoundException ex) {
                    // should normally not be happening, scince it's a set so unique
                    LogFactory.getLog(getClass()).error(
                            "should normally not be happening, scince it's a set so unique", ex);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    public class OutOfRasterException extends Exception {
    }

}

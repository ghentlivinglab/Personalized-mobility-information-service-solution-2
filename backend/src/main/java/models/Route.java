package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import models.event.EventType;
import models.services.Service;

/**
 * Model to represent a route of a travel.
 */
public class Route extends EventMatchable {

    private List<Coordinate> userWaypoints;
    private List<Coordinate> fullWaypoints;
    private Transportation transportationType;
    private List<Service> services;

    /**
     * Default constructor for a route.
     *
     * @param userWaypoints A list of Coordinate that defines any changes to the
     * default generated route (does not include start and endpoint).
     * @param fullWaypoints A full description of the route in the form of all
     * waypoints it passes.
     * @param transportationType The transportationtype of the route.
     * @param notifyForEventTypes A map of eventtypes with as key their ID's,
     * defines the eventtypes this route needs notifications for.
     * @param muted Object of the model MuteNotification, checks if this
     * location is not muted by the user. Once set this can only be changed via
     * the object itself.
     * @param services
     */
    public Route(List<Coordinate> userWaypoints, List<Coordinate> fullWaypoints,
            Transportation transportationType, Map<Integer, EventType> notifyForEventTypes,
            boolean muted, List<Service> services) {
        super(muted, notifyForEventTypes);
        if (userWaypoints == null) {
            throw new IllegalArgumentException("wayPoints can't be null");
        }
        this.userWaypoints = userWaypoints;
        this.fullWaypoints = fullWaypoints;
        if (transportationType == null) {
            throw new IllegalArgumentException("Transportationtypes can't be null");
        }
        this.transportationType = transportationType;
        if (services == null) {
            throw new IllegalArgumentException("Services can't be null");
        }
        this.services = services;
    }

    public List<Coordinate> getUserWaypoints() {
        return Collections.unmodifiableList(userWaypoints);
    }

    public List<Coordinate> getFullWaypoints() {
        return Collections.unmodifiableList(fullWaypoints);
    }

    /**
     * The getter of the transportation types of the route.
     *
     * @return the transportation types of the route.
     */
    public Transportation getTransportationType() {
        return transportationType;
    }

    /**
     * This method returns the active services in a List for this Service. A
     * copy of the original List is returned. Changes to the original List are
     * prevented this way.
     *
     * @return a copy of the original List services.
     */
    public List<Service> getServices() {
        return Collections.unmodifiableList(services);
    }

    /**
     * This method adds a new Service to the List of services of the Service.
     *
     * @param service the Service that needs to be added.
     */
    public void addService(Service service) {
        services.add(service);
    }

    /**
     * This method removes a Service from the List of services of the Service.
     *
     * @param service the Service that needs to be removed.
     */
    public void removeService(Service service) {
        services.remove(service);
    }
}

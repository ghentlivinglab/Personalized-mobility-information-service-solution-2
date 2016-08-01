package models;

import models.address.Address;
import models.repetition.Repetition;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.services.Service;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model to represent a travel.
 */
public class Travel {

    private String name;
    private Map<Integer, Route> routes;
    private LocalTime beginDate;
    private LocalTime endDate;
    private Boolean isArrivalTime;
    private List<Repetition> recurring;
    private List<Service> services;
    private Address startPoint;
    private Address endPoint;

    /**
     * This constructor makes a new Travel.
     *
     * @param name the string that represents the name of the Travel.
     * @param routes the Map of routes of the Travel. The key of the Map is an
     * integer, representing the id of the route. The value of the key is an
     * object of the class Route.
     * @param beginDate this Date represents the begin of a time interval.
     * @param endDate this Date represents the end of a time interval.
     * @param isArrivalTime this boolean indicates whether or not the interval
     * is for the departure or arrival of the travel.
     * @param startPoint the coordinates of the start point of the Travel.
     * @param endPoint the coordinates of the end point of the Travel.
     * @param recurring the list with elements of type Repetition, which
     * indicate when the travel should be repeated.
     * @param services the list with Services which indicate which services
     * should get notifications from the application.
     */
    public Travel(String name, LocalTime beginDate, LocalTime endDate, Boolean isArrivalTime, Address startPoint, Address endPoint,
            List<Repetition> recurring, Map<Integer, Route> routes, List<Service> services) {
        if ( name == null ) {
            throw new IllegalArgumentException("The name of Travel can't be null");
        }
        this.name = name;
        if ( routes == null) {
            throw new IllegalArgumentException("Routes of a Travel can't be null");
        }
        this.routes = routes;
        if ( beginDate == null ) {
            throw new IllegalArgumentException("The begin date of Travel can't be null");
        }
        this.beginDate = beginDate;
        if ( endDate == null ) {
            throw new IllegalArgumentException("The end date of Travel can't be null");
        }
        this.endDate = endDate;
        if ( isArrivalTime == null ) {
            throw new IllegalArgumentException("The isArrivalTime of Travel can't be null");
        }
        this.isArrivalTime = isArrivalTime;
        if ( recurring == null ) {
            throw new IllegalArgumentException("The recurring of Travel can't be null");
        }
        this.recurring = recurring;
        this.services = services;
        if ( startPoint == null ) {
            throw new IllegalArgumentException("The start point of Travel can't be null");
        }
        this.startPoint = startPoint;
        if ( endPoint == null ) {
            throw new IllegalArgumentException("The end point of Travel can't be null");
        }
        this.endPoint = endPoint;
    }

    /**
     * The method returns the name of the Travel.
     *
     * @return the String which is the name of the Travel.
     */
    public String getName() {
        return name;
    }

    /**
     * This method changes the name of the Travel.
     *
     * @param name the new name for the Travel.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the Routes for the Travel. The routes are returned in
     * a HashMap, which is the copy of the original one. By doing this, no
     * changes can be made to the original through this method.
     *
     * @return
     */
    public Map<Integer, Route> getRoutes() {
        return routes;
    }

    /**
     * This method adds a Route to the Travel.
     *
     * @param id the id of the Route that needs to be added.
     * @param route the Route itself that needs to be added.
     */
    public void addRoute(Integer id, Route route) {
        routes.put(id, route);
    }

    /**
     * This method removes one Route from the Map with Routes.
     *
     * @param id the id of the Route that needs to be removed.
     */
    public void removeRoute(Integer id) {
        routes.remove(id);
    }

    /**
     * This method returns the begin date of the Travel.
     *
     * @return the begin date of the Travel.
     */
    public LocalTime getBeginDate() {
        return beginDate;
    }

    /**
     * This method changes the Begin date of the Travel.
     *
     * @param date the new begin date.
     */
    public void setBeginDate(LocalTime date) {
        this.beginDate = date;
    }

    /**
     * This method returns the end date of the Travel.
     *
     * @return the end date of the Travel.
     */
    public LocalTime getEndDate() {
        return endDate;
    }

    /**
     * This method changes the end date of the Travel.
     *
     * @param date the new end date of the Travel.
     */
    public void setEndDate(LocalTime date) {
        this.endDate = date;
    }

    /**
     * This method returns whether or not the time interval of the Travel is for
     * the arrival time or departure time. If this method returns false, the
     * time is the departure time. If this method returns true, the time is the
     * arrival time.
     *
     * @return boolean, either true or false.
     */
    public Boolean isArrivalTime() {
        return isArrivalTime;
    }

    /**
     * This method allows to change whether or not this it the arrival time.
     *
     * @param arrivalTime the new boolean representing whether or not the times
     * in this class are arrival times.
     */
    public void setArrivalTime(Boolean arrivalTime) {
        isArrivalTime = arrivalTime;
    }

    /**
     * This method returns the list of objects of Repetition. The method returns
     * a copy of the original List. This makes sure no alterations can be made
     * through this method to the original list.
     *
     * @return a copy of the original list.
     */
    public List<Repetition> getRecurring() {
        return new ArrayList<>(recurring);
    }

    /**
     * This method changes the List of Repetition. This method is private to
     * prevent false modifications to the class from the outside worlds.
     *
     * @param recurring the new List containing Repetition objects.
     */
    public void setRecurring(List<Repetition> recurring) {
        this.recurring = recurring;
    }

    /**
     * This method adds a new Repetition to the List of recurring.
     *
     * @param repetition the repetition that needs to be added.
     */
    public void addRecurring(Repetition repetition) {
        this.recurring.add(repetition);
    }

    /**
     * This method deletes a repetition from the list of recurring.
     *
     * @param repetition the repetition that needs to be deleted.
     */
    public void removeRecurring(Repetition repetition) {
        this.recurring.remove(repetition);
    }

    /**
     * This method returns the Coordinates of the start point of the Travel.
     *
     * @return coordinates of the start point of the travel.
     */
    public Address getStartPoint() {
        return this.startPoint;
    }

    /**
     * This method returns the Coordinates of the end point of the Travel.
     *
     * @return coordinates of the end point of the travel.
     */
    public Address getEndPoint() {
        return this.endPoint;
    }

    /**
     * This method changes the start point of the Travel.
     *
     * @param start the coordinates of the new start point.
     */
    public void setStartPoint(Address start) {
        if (start == endPoint) {
            throw new IllegalArgumentException("The coordinate of the start and endpoint must be different.");
        } else {
            this.startPoint = start;
        }
    }

    /**
     * This method changes the end point of the Travel.
     *
     * @param end the coordinates of the new end point.
     */
    public void setEndPoint(Address end) {
        if (startPoint != end) {
            this.endPoint = end;
        } else {
            throw new IllegalArgumentException("The coordinate of the start and endpoint must be different.");
        }
    }

    /**
     * This method returns the active services in a List for this Travel. A copy
     * of the original List is returned. Changes to the original List are
     * prevented this way.
     *
     * @return a copy of the original List services.
     */
    public List<Service> getServices() {
        return new ArrayList<>(services);
    }

    /**
     * This method adds a new Service to the List of services of the Travel.
     *
     * @param service the Service that needs to be added.
     */
    public void addService(Service service) {
        services.add(service);
    }

    /**
     * This method removes a Service from the List of services of the Travel.
     *
     * @param service the Service that needs to be removed.
     */
    public void removeService(Service service) {
        services.remove(service);
    }

    /**
     * This method calculates whether or not the user wants to have travel
     * information today.
     *
     * @return the boolean indicating whether or not the user is doing this
     * travel today.
     */
    public boolean today() {
        Boolean result = false;
        for (int i = 0; i < recurring.size(); i++) {
            result = result || recurring.get(i).today();
        }
        return result;
    }
    
    /**
     * This method updates the common data of this object with the data from
     * another travel object. The data updated includes:
     * - name
     * - beginDate
     * - endDate
     * - isArrivalTime
     * - recurring
     * - startPoint
     * - endPoint
     *
     * @param travel The object wherefrom the data needs to be updated
     */
    public void updateData (Travel travel) {
        this.name = travel.getName();
        this.beginDate = travel.getBeginDate();
        this.endDate = travel.getEndDate();
        this.isArrivalTime = travel.isArrivalTime();
        this.recurring = travel.getRecurring();
        this.startPoint = travel.getStartPoint();
        this.endPoint = travel.getEndPoint();
    }

    /**
     * The override of the hashCode() function.
     *
     * @return hashCodeBuilder.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 37).
                append(name).
                append(routes).
                append(beginDate).
                append(endDate).
                append(isArrivalTime).
                append(recurring).
                append(startPoint).
                append(endPoint).
                append(services).
                toHashCode();
    }

    /**
     * The override of the equals function. This allows us to compare objects to
     * objects of Travel.
     *
     * @param obj that needs to be compared to the Travel.
     * @return boolean that indicates whether or not the two objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Travel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Travel rhs = (Travel) obj;
        return new EqualsBuilder().
                append(name, rhs.name).
                append(routes, rhs.routes).
                append(beginDate, rhs.beginDate).
                append(endDate, rhs.endDate).
                append(isArrivalTime, rhs.isArrivalTime).
                append(recurring, rhs.recurring).
                append(startPoint, rhs.startPoint).
                append(endPoint, rhs.endPoint).
                append(services, rhs.services).
                isEquals();

    }
}

package models;

//TODO: inputvalidation, maybe other naming of setters?, instead of getting muted, calling muted when it needs to be changed?
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import models.address.Address;
import models.event.EventType;
import models.services.Service;

/**
 * Model to represent a saved location for a user.
 */
public class Location extends EventMatchable {

    private Address address;
    private String name;
    private int radius;
    private List<Service> services;

    /**
     * Default constructor for a Location.
     *
     * @param address Object of the model Address, defines the address of the
     * location.
     * @param name User specified name for the location.
     * @param radius Radius in which the user wants to receive notifications (in
     * meter).
     * @param muteNotification Object of the model MuteNotification, checks if
     * this location is not muted by the user. Once set this can only be changed
     * via the object itself.
     * @param notifyForEventTypes which events should be notified.
     * @param services the services on which the user wants to be notified.
     */
    public Location(Address address, String name, int radius, boolean muteNotification,
            Map<Integer, EventType> notifyForEventTypes, List<Service> services) {
        super(muteNotification, notifyForEventTypes);
        if (address == null) {
            throw new IllegalArgumentException("The address can't be null.");
        }
        this.address = address;
        if (name == null) {
            throw new IllegalArgumentException("The name can't be null.");
        }
        this.name = name;
        this.radius = radius;
        if (services == null) {
            this.services = new ArrayList<>();
        } else {
            this.services = services;
        }
    }

    /**
     * Getter for the address.
     *
     * @return Address address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Getter for the name.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the radius.
     *
     * @return int radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Setter for the address.
     *
     * @param address Object of the model Address, defines the address of the
     * location.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Setter for the name.
     *
     * @param name User specified name for the location.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the radius.
     *
     * @param radius Radius in which the user wants to receive notifications (in
     * meter).
     */
    public void setRadius(int radius) {
        this.radius = radius;
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

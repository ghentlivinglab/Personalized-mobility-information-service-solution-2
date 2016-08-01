package models.users;

import models.services.Service;
import models.exceptions.InvalidPasswordException;
import java.util.HashMap;
import java.util.Map;
import models.Location;
import models.Travel;
import javax.mail.internet.AddressException;
import models.Mutable;
import models.services.Email;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This model represents the User of a system. This class is abstract and
 * implements all the basic operations a User should have.
 */
public class User extends Mutable {

    protected String firstName;
    protected String lastName;
    protected Password password;
    protected Email email;
    protected Map<Integer, Location> locations;
    protected Map<Integer, Travel> travels;
    protected Map<String, Service> services;
    protected String refreshToken;
    protected boolean isOperator, isAdmin;
    protected String pushToken;

    /**
     * This constructor is used to transform database data to a domain user
     * model.
     *
     * @param firstName
     * @param lastName
     * @param password
     * @param email
     * @param emailValidated
     * @param locations
     * @param travels
     * @param notificationsMuted
     * @param refreshToken
     * @param isOperator
     * @param isAdmin
     * @param pushToken
     * @throws AddressException
     */
    public User(String firstName, String lastName, Password password, String email, boolean emailValidated,
            Map<Integer, Location> locations, Map<Integer, Travel> travels, boolean notificationsMuted,
            String refreshToken, boolean isOperator, boolean isAdmin, String pushToken)
            throws AddressException {
        super(notificationsMuted);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = new Email(email);
        if (emailValidated) {
            this.email.validate();
        }
        this.locations = locations;
        this.travels = travels;
        this.refreshToken = refreshToken;
        this.isAdmin = isAdmin;
        this.isOperator = isOperator;
        // services are no longer used in this milestone
        services = new HashMap<>();
        this.pushToken = pushToken;
    }

    /**
     * Create a basic user from the data we get from the API. Used in the DTO
     * mapper.
     *
     * @param firstName
     * @param lastName
     * @param password
     * @param email
     * @param notificationsMuted
     * @throws InvalidPasswordException
     * @throws AddressException
     */
    public User(String firstName, String lastName, String password, String email,
            boolean notificationsMuted)
            throws InvalidPasswordException, AddressException {

        super(notificationsMuted);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = new Password(password);
        this.email = new Email(email);
        this.locations = new HashMap<>();
        this.travels = new HashMap<>();
        this.isOperator = false;
        this.isAdmin = false;
        // no cell phone used in this milestone
        services = new HashMap<>();
    }
    
    public User (String password, String email, boolean notificationsMuted) throws AddressException {
        super(notificationsMuted);
        this.password = new Password(password);
        this.email = new Email(email);
        this.locations = new HashMap<>();
        this.travels = new HashMap<>();
    }

    /**
     * This method returns the first name of the User.
     *
     * @return, a String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method changes the first name of the User.
     *
     * @param firstName, a String
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This method returns the last name of the User.
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method changes the last name of the User.
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the password of the User.
     *
     * @return password
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Sets the password of the User.
     *
     * @param password, an instance of the class Password.
     */
    public void setPassword(Password password) {
        this.password = password;
    }

    /**
     * Sets the password of the User. A new password object is created in this
     * method.
     *
     * @param password, a String.
     * @throws models.exceptions.InvalidPasswordException
     */
    public void setPassword(String password) throws InvalidPasswordException {
        this.password = new Password(password);
    }

    /**
     * This method returns a copy of the Email of the User.
     *
     * @return email (copy)
     */
    public Email getEmail() {
        return email;
    }

    /**
     * This method sets the Email of the User.
     *
     * @param email the new email address for the User.
     */
    public void setEmail(Email email) {
        this.email = email;
    }

    /**
     *
     * @return emailadress as string
     */
    public String getEmailAsString() {
        return email.toString();
    }

    /**
     * This method sets the Email of the User. A new Email object is created in
     * this method.
     *
     * @param email a String representing a new email address.
     * @throws javax.mail.internet.AddressException This exception will be
     * thrown if the email address is invalid.
     */
    public void setEmail(String email) throws AddressException {
        this.email = new Email(email);
    }

    /**
     * This method returns all the Locations of the User in a map.
     *
     * @return copy of the Location List.
     */
    public Map<Integer, Location> getLocations() {
        return new HashMap<>(locations);
    }

    /**
     * This method adds a new location to the Locations of the User.
     *
     * @param id the id of the give location
     * @param location that needs to be added to the list of locations.
     */
    public void addLocation(Integer id, Location location) {
        locations.put(id, location);
    }

    /**
     * This method allows a user to remove one of his/her locations.
     *
     * @param id the id of the location that needs to be removed.
     */
    public void removeLocation(Integer id) {
        locations.remove(id);
    }

    /**
     * This method adds a new Service to the collection of services of the User.
     *
     * @param service the new service that needs to be added.
     */
    public void addService(Service service) {
        services.put(service.getKey(), service);
    }

    /**
     * This method gets a specific service with the given key. When the user
     * doesn't have this service, it returns null.
     *
     * @param key String key of the service
     * @return The requested service, or null if not present.
     */
    public Service getService(String key) {
        if (!services.containsKey(key)) {
            return null;
        }
        return services.get(key);
    }

    /**
     * This method returns a copy of the map of all services of this user.
     *
     * @return all services of this user
     */
    public Map<String, Service> getServices() {
        return new HashMap<>(services);
    }

    /**
     * This method allows to remove a specific Services. If no service of this
     * type was present, no action will be taken.
     *
     * @param key the Service object that needs to be removed.
     */
    public void removeService(String key) {
        services.remove(key);
    }

    /**
     * This method adds a Travel to the Map of Travels of a User.
     *
     * @param id an Integer representing the id of the Travel
     * @param travel the travel that needs to be added to the Map of Travels.
     */
    public void addTravel(Integer id, Travel travel) {
        travels.put(id, travel);
    }

    /**
     * This method allows the User to remove one of his/her Travels.
     *
     * @param id the Integer representing the id of the Travel that needs to be
     * deleted.
     */
    public void removeTravel(Integer id) {
        travels.remove(id);
    }
    
    /**
     * This method returns a HashMap containing all the travels of the User. It
     * returns a copy of the original Map to make sure no modifications can be
     * made through this method.
     *
     * @return a HashMap that contains all the travels of the User. A copy of
     * the original one.
     */
    public Map<Integer, Travel> getTravels() {
        return new HashMap<>(travels);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void makeAdmin() {
        isAdmin = true;
        isOperator = false;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public void makeOperator() {
        isOperator = true;
        isAdmin = false;
    }
    
    public void makeUser() {
        isOperator = false;
        isAdmin = false;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
    
    /**
     * This method updates the common data of this object with the data from
     * another user object. The data updated includes: - firstName - lastName -
     * services - mutenotifications
     *
     * @param user The object wherefrom the data needs to be updated
     */
    public void updateData(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        setMuted(user.isMuted());
    }

    /**
     * This method overrides the hashcode method.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(firstName).
                append(lastName).
                append(password).
                append(email).
                append(locations).
                append(travels).
                append(services).
                append(muteNotifications).
                toHashCode();
    }

    /**
     * This object overrides the equals method of the User.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        User rhs = (User) obj;
        return new EqualsBuilder().
                append(firstName, rhs.firstName).
                append(lastName, rhs.lastName).
                append(password, rhs.password).
                append(email, rhs.email).
                append(locations, rhs.locations).
                append(muteNotifications, rhs.muteNotifications).
                append(travels, rhs.travels).
                append(services, rhs.services).
                isEquals();
    }

}

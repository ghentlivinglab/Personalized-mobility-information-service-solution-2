package models.services;

import models.Mutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class represents a Service which can be provided to a User. The
 * extentions of this class will be specific services a User can subscribe to.
 * This class defines the basic features of a Service.
 *
 * @author hannedesutter
 */
public abstract class Service extends Mutable {

    protected String name;
    protected boolean validated;

    /**
     * This constructor creates a new Service. There are two required fields,
     * muteNotifications and name.
     *
     * @param muteNotifications an instance of MuteNotification which indicates
     * whether the User wants to receive notifications from this Service.
     * @param name the name of the Service.
     * @param validated the boolean field indicated whether or not the Service
     * is validated.
     */
    public Service(boolean muteNotifications, String name, boolean validated) {
        super(muteNotifications);
        this.name = name;
        this.validated = validated;
    }

    /**
     * This constructor creates a new Service. The validated field is
     * automatically set to false in this constructor. There are two required
     * fields, @notifications and @name.
     *
     * @param notifications an instance of MuteNotification which indicates
     * whether the User wants to receive notifications from this Service.
     * @param name the name of the Service.
     */
    public Service(boolean notifications, String name) {
        super(notifications);
        this.name = name;
        this.validated = false;
    }

    /**
     * This constructor creates a new Service. There is one required field for
     * this constructor. The @name of the Service. The validated field is
     * automatically set to false in this constructor. The notifications will
     * automatically be enabled by using this constructor.
     *
     * @param name the name of the Service.
     */
    public Service(String name) {
        super(false);
        this.name = name;
    }

    /**
     * The copy constructor for Service.
     *
     * @param service the service that needs to be copied.
     */
    public Service(Service service) {
        super(false);
        this.name = service.getName();
        this.validated = service.validated;
    }

    /**
     * This method returns whether or not the Service is validated.
     *
     * @return boolean which indicates whether or not the Service is validated.
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * This method validated the Service.
     */
    public void validate() {
        this.validated = true;
    }
    
    /**
     * This method invalidated the Service.
     */
    public void inValidate () {
        this.validated = false;
    }

    /**
     * This is the getter for the name of the Service.
     *
     * @return the name of the service.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This is the setter for the name of the Service.
     *
     * @param name the new name for the Service.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return name;
    }

    @Override
    public abstract String toString();

    /**
     * This is the override function for services.
     *
     * @param obj the object that needs to be compared.
     * @return boolean indication whether or not the object is equal to the
     * Service.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if (!(obj instanceof Service)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Service rhs = (Service) obj;
        return new EqualsBuilder().
                append(name, rhs.name).
                append(muteNotifications, rhs.muteNotifications).
                isEquals();
    }

    /**
     * This is the override function of the hash function for services.
     *
     * @return the hash of the service.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(name).
                append(muteNotifications).
                toHashCode();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.services;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The model of the Service Email address. This class extends the abstract class
 * Service. This class uses an instance of the class InternetAddress to
 * represent and validate the email address.
 *
 * @author hannedesutter
 */
public class Email extends Service {

    private InternetAddress emailAddres;
    public static final String KEY = "email";

    /**
     * The constructor of the Service Email.
     *
     * @param address the email address.
     * @param muteNotifications whether or not the notifications should be enabled
     * for this email address.
     * @param validated whether or not this email address is validated.
     * @throws javax.mail.internet.AddressException
     */
    public Email(InternetAddress address, boolean muteNotifications, boolean validated) throws AddressException {
        super(muteNotifications, KEY, validated);
        address.validate();
        if (address == null) {
            throw new IllegalArgumentException("Email address can't be null.");
        }
        this.emailAddres = address;
    }

    /**
     * The constructor of Email, only requiring the email address to be filled
     * in.
     *
     * @param address the email address.
     * @throws javax.mail.internet.AddressException
     */
    public Email(InternetAddress address) throws AddressException {
        super(false, KEY, false);
        if (address == null) {
            throw new IllegalArgumentException("Email address can't be null.");
        }
        address.validate();
        this.emailAddres = address;
    }

    /**
     *
     * @param email
     * @throws AddressException
     */
    public Email(String email) throws AddressException {
        super(false, KEY, false);
        if (email == null) {
            throw new IllegalArgumentException("Email address can't be null.");
        }
        this.emailAddres = new InternetAddress(email);
        emailAddres.validate();
    }

    /**
     * This is the copy constructor for an Email.
     *
     * @param email the email that needs to be copied.
     */
    public Email(Email email) {
        super(email);
        this.emailAddres = (InternetAddress) email.getEmailAddress().clone();
    }

    /**
     * The getter for the email address.
     *
     * @return the email address.
     */
    public InternetAddress getEmailAddress() {
        return emailAddres;
    }

    /**
     * This method validates the Email address.
     *
     * @throws AddressException if the Email is not valid.
     */
    public void validateEmail() throws AddressException {
        this.emailAddres.validate();
    }

    /**
     * The override function of the equals method of the class Email.
     *
     * @param obj the object that needs to be compared.
     * @return whether or not the object is equal to the service.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && new EqualsBuilder().append(emailAddres, ((Email) obj).emailAddres).isEquals();
    }

    @Override
    public String toString() {
        return emailAddres.toString();
    }

    /**
     * The override function of the hashcode() function for the class Email.
     *
     * @return the hashcode of the Email.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(emailAddres).
                append(name).
                append(muteNotifications).
                append(validated).
                toHashCode();
    }

}

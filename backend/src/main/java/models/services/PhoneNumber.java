/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.services;

import models.exceptions.InvalidPhoneNumberException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The PhoneNumber class provides the User with a phone number. 
 * This class is an extention of the Class Service. 
 * The user can choose whether or not if he/she wants notifications sent through this service. 
 * The notifications sent by this class will be text messages. 
 * @author hannedesutter
 */
public class PhoneNumber extends Service {
    
    private final String phoneNumber;
    public static final String KEY = "phonenumber";
    
    /**
     * This constructor initialized a Phone Number as a Service for a User. 
     * In the constructor are two arguments, the @notifications parameter and the @phoneNumber parameter. 
     * 
     * @param notifications this boolean indicates if the User wants to receive notifications through this service.
     * @param phoneNumber this is the String representation of the Phone Number.
     * @throws InvalidPhoneNumberException This exception will be thrown if the @phoneNumber is invalid. 
     */
    public PhoneNumber(boolean notifications, String phoneNumber) throws InvalidPhoneNumberException {
        super(notifications, KEY);
        isValid(phoneNumber);
        this.phoneNumber=phoneNumber;
    }

    /**
     * This constructor initialized a Phone Number as a Service for a User. 
     * In the constructor are two arguments, the @notifications parameter and the @phoneNumber parameter.
     * 
     * @param phoneNumber
     * @throws InvalidPhoneNumberException This exception will be thrown if the @phoneNumber is invalid. 
     */
    public PhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        super(KEY);
        isValid(phoneNumber);
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * This method will preform all checks to make sure the Phone Number is Valid. 
     * @param phoneNumber the String representation of the Phone Number that needs to be checked. 
     * @throws InvalidPhoneNumberException 
     */
    public final void isValid(String phoneNumber) throws InvalidPhoneNumberException{
        checkLength(phoneNumber);
        checkNumbers(phoneNumber);
    }
    
    /**
     * This method checks whether or not the Phone Number has a valid length. 
     * The length should be 10, if the length differs, the Number is invalid. 
     * @param phoneNumber the String representation of the Phone Number that needs to be checked. 
     * @throws InvalidPhoneNumberException thrown if the Phone Number has another length than 10.
     */
    public void checkLength(String phoneNumber) throws InvalidPhoneNumberException{
        if(phoneNumber.length()!=10){
            throw new InvalidPhoneNumberException("The Phone Number has an incorrect Length. It should be 10, recieved: "+ phoneNumber.length());
        }
    }
    
    /**
     * This method checks whether or not the Phone Number has a valid characters in it.
     * All characters should be numbers [0-9]. If not, the number is invalid. 
     * @param phoneNumber the String representation of the Phone Number that needs to be checked. 
     * @throws InvalidPhoneNumberException thrown if the Phone Number contains other characters than numbers.
     */
    public void checkNumbers(String phoneNumber) throws InvalidPhoneNumberException {
        if(!phoneNumber.matches("[0-9]+")){
            throw new InvalidPhoneNumberException("The Phone Number may only contain numbers.");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    @Override
    public String toString() {
        return phoneNumber;
    }
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof PhoneNumber)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        
        PhoneNumber rhs= (PhoneNumber) obj;
        return new EqualsBuilder().
                append(name,rhs.name).
                append(muteNotifications,rhs.muteNotifications).
                append(phoneNumber,rhs.phoneNumber).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(name).
                append(muteNotifications).
                append(phoneNumber).
                toHashCode();
    }
}

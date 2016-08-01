/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.exceptions;

/**
 *
 * @author hannedesutter
 */
public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {
    }

    public InvalidPhoneNumberException(String ex) {
        super(ex);
    }

    public InvalidPhoneNumberException(Exception ex) {
        super(ex);
    }
}

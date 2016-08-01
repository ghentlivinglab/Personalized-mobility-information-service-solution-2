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
public class InvalidCountryCodeException extends RuntimeException {

    public InvalidCountryCodeException() {
    }

    public InvalidCountryCodeException(String ex) {
        super(ex);
    }

    public InvalidCountryCodeException(Exception ex) {
        super(ex);
    }
}

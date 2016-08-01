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
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String ex) {
        super(ex);
    }

    public InvalidPasswordException(Exception ex) {
        super(ex);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.users;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.exceptions.InvalidPasswordException;

/**
 *
 * @author hannedesutter
 */
public class Password {

    private String password;
    private byte[] salt;

    /**
     * This constructor creates the new password and checks weather or not it is
     * valid.
     *
     * @param password, a String
     * @throws models.exceptions.InvalidPasswordException
     */
    public Password(String password) throws InvalidPasswordException {
        isValid(password);
        try {
            this.password = makeSafePassword(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new InvalidPasswordException("The password isn't safe.");
        }

    }

    /**
     * This constructor creates the new password and checks weather or not it is
     * valid.
     *
     * @param password, a String
     * @param salt
     * @throws models.exceptions.InvalidPasswordException
     */
    public Password(String password, byte[] salt) throws InvalidPasswordException {
        isValid(password);
        this.password = password;
        this.salt = salt;
    }

    /**
     * This method returns the String that represents the Password.
     *
     * @return
     */
    public String getStringPassword() {
        return password;
    }

    /**
     * This method changes the password. The method also checks weather or not
     * the password is valid.
     *
     * @param password a string representation of the password.
     * @throws InvalidPasswordException if the password is invalid.
     */
    public void setPassword(String password) throws InvalidPasswordException {
        isValid(password);
        try {
            this.password = makeSafePassword(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new InvalidPasswordException("The new password isn't safe.");
        }
    }

    /**
     * Checks weather or not the password is valid. If it is invalid, the method
     * will throw an exception.
     *
     * @param password a string representation of the password.
     * @throws models.exceptions.InvalidPasswordException
     */
    public static void isValid(String password) throws InvalidPasswordException {
        checkLength(password);
        checkContainsNumber(password);
    }

    /**
     * Checks weather or not the password has the minimum length.
     *
     * @param password a string representation of the password.
     * @throws models.exceptions.InvalidPasswordException
     */
    public static void checkLength(String password) throws InvalidPasswordException {
        if (password.length() < 8) {
            throw new InvalidPasswordException("The Password must contain 8 or more characters.");
        }
    }

    /**
     * Checks weather or not the password contains a number.
     *
     * @param password a string representation of the password.
     * @throws models.exceptions.InvalidPasswordException
     */
    public static void checkContainsNumber(String password) throws InvalidPasswordException {
        String numRegex = ".*[0-9].*";
        String alphaRegex = ".*[A-Z].*";
        if (password.matches(numRegex)) {
            return;
        }
        throw new InvalidPasswordException("The password must contain at least 1 number");
    }

    private String makeSafePassword(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom rnd = new SecureRandom();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        this.salt = new byte[32];
        rnd.nextBytes(salt);
        byte[] passbyte = pass.getBytes("UTF-8");

        byte[] concat = new byte[salt.length + passbyte.length];
        System.arraycopy(salt, 0, concat, 0, salt.length);
        System.arraycopy(passbyte, 0, concat, salt.length, passbyte.length);

        byte[] digest = md.digest(concat);

        return String.format("%064x", new java.math.BigInteger(1, digest));
    }

    public boolean checkSamePassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] passbyte = password.getBytes("UTF-8");

        byte[] concat = new byte[salt.length + passbyte.length];
        System.arraycopy(salt, 0, concat, 0, salt.length);
        System.arraycopy(passbyte, 0, concat, salt.length, passbyte.length);

        byte[] digest = md.digest(concat);

        return String.format("%064x", new java.math.BigInteger(1, digest)).equals(this.password);
    }

    public byte[] getSalt() {
        return salt;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Password other = (Password) obj;
        return this.password.equals(other.password);
    }
    
}

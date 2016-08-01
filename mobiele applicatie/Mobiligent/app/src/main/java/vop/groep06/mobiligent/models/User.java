package vop.groep06.mobiligent.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String refreshToken;
    private String role;
    private String accessToken;
    private String accessTokenExp;
    private static final String BASE_URL = "https://vopro6.ugent.be/api";
    private String userURL;
    private String email;
    private String firstName;
    private String lastName;

    public User() {
    }

    public User(String id, String refreshToken, String role, String accessToken, String accessTokenExp, String userURL, String email, String firstName, String lastName) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.role = role;
        this.accessToken = accessToken;
        this.accessTokenExp = accessTokenExp;
        this.userURL = userURL;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenExp() {
        return accessTokenExp;
    }

    public void setAccessTokenExp(String accessTokenExp) {
        this.accessTokenExp = accessTokenExp;
    }

    public String getUserURL() {
        if (userURL == null) {
            userURL =  BASE_URL+"/user/"+id;
        }
        return userURL;

    }

    public void setUserURL(String userURL) {
        this.userURL = BASE_URL+userURL;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMyEventsUrl() {
        return BASE_URL+"/user/"+id+"/events";
    }

    public String getMyLocationsUrl() {
        return BASE_URL+"/user/"+id+"/point_of_interest";
    }

    public String getMyTravelsUrl() {
        return BASE_URL+"/user/"+id+"/travel";
    }

    public String getChangeEmailUrl() {
        return userURL+"/change_email";
    }

    public String getChangePassUrl() {
        return userURL+"/change_password";
    }
}

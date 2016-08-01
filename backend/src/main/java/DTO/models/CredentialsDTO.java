package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialsDTO {
    
    private String email;
    private String password;

    public CredentialsDTO() {
    }

    public CredentialsDTO(String email, String password) {
        this.email = email.toLowerCase();
        this.password = password;
    }
    
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}

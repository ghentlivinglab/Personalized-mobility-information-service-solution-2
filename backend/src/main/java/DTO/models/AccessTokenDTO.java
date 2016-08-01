package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessTokenDTO {
    
    private String accessToken;
    private String expiration;

    public AccessTokenDTO() {
    }

    public AccessTokenDTO(String accessToken, String expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }

    @JsonProperty("token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("exp")
    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
    
    
}

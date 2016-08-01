package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenDTO {
    
    private String token;
    private String userId;
    private String userUrl;
    private String role;

    public RefreshTokenDTO() {
    }

    public RefreshTokenDTO(String token, String userId, String userUrl, String role) {
        this.token = token;
        this.userId = userId;
        this.userUrl = userUrl;
        this.role = role;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("user_url")
    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    
}

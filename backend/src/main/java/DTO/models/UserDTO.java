
package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author Robin Antheunis - 29/03/2016
 */
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private boolean muteNotifications;
    private ValidatedDTO validated;
    //private NotifyDTO notify;
    //TODO: links

    
    
    public UserDTO() {
        this.id = "";
        this.validated = new ValidatedDTO();
    }

    public UserDTO(String id, String firstName, String lastName, String password, String email, boolean muteNotifications, ValidatedDTO validated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email.toLowerCase();
        this.muteNotifications = muteNotifications;
        this.validated = validated;
    }
    
    public UserDTO(String id, String email, boolean muteNotifications, ValidatedDTO validated) {
        this.id = id;
        this.email = email.toLowerCase();
        this.muteNotifications = muteNotifications;
        this.validated = validated;
        this.firstName = "";
        this.lastName = "";
        this.password = "";
    }
    
    @JsonProperty("validated")
    public ValidatedDTO getValidated() {
        return validated;
    }
    
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }
    
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }
    
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
    
    @JsonProperty("email")
    public String getEmail() {
        return email.toLowerCase();
    }
    
    @JsonProperty("mute_notifications")
    public boolean isMuteNotifications() {
        return muteNotifications;
    }
    
    public void setValidated(ValidatedDTO validated) {
        this.validated = validated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void setMuteNotifications(boolean muteNotifications) {
        this.muteNotifications = muteNotifications;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.password);
        hash = 23 * hash + Objects.hashCode(this.email);
        hash = 23 * hash + (this.muteNotifications ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.validated);
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
        final UserDTO other = (UserDTO) obj;
        if (this.muteNotifications != other.muteNotifications) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.validated, other.validated)) {
            return false;
        }
        return true;
    }
    
    
    
}

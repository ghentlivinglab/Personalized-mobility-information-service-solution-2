package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeEmailDTO {
    
    private String oldEmail;
    private String newEmail;

    public ChangeEmailDTO() {
    }

    public ChangeEmailDTO(String oldEmail, String newEmail) {
        this.oldEmail = oldEmail.toLowerCase();
        this.newEmail = newEmail.toLowerCase();
    }

    @JsonProperty("old_email")
    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    @JsonProperty("new_email")
    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

}

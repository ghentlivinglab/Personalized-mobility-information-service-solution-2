package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VerificationDTO {
    
    private String emailPin;
    
    public VerificationDTO() {
        
    }

    public VerificationDTO(String emailPin) {
        this.emailPin = emailPin;
    }

    @JsonProperty("email_verification_pin")
    public String getEmailPin() {
        return emailPin;
    }

    
    public void setEmailPin(String emailPin) {
        this.emailPin = emailPin;
    }

}

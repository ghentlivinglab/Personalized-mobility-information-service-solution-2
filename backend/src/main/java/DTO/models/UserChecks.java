
package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;


public abstract class UserChecks {

    private boolean email;

    public UserChecks() {
        this.email = false;
    }

    public UserChecks(boolean email) {
        this.email = email;
    }

    @JsonProperty("email")
    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        UserChecks rhs = (UserChecks) obj;
        return new EqualsBuilder()
                .append(email, rhs.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.email ? 1 : 0);
        return hash;
    }
    
    
}


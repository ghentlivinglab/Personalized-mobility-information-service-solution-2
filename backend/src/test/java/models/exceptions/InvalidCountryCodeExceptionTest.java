package models.exceptions;

import org.junit.Test;

public class InvalidCountryCodeExceptionTest {
    
    public InvalidCountryCodeExceptionTest() {
    }
    
    @Test
    public void testConstructors() {
        //test constuctors
        InvalidCountryCodeException invalidCountryCodeException = new InvalidCountryCodeException();
        InvalidCountryCodeException invalidCountryCodeExceptionMsg = new InvalidCountryCodeException("msg");
        InvalidCountryCodeException invalidCountryCodeExceptionEx = new InvalidCountryCodeException(invalidCountryCodeException);
    }
    
}

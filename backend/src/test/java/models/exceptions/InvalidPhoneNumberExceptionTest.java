package models.exceptions;

import org.junit.Test;

public class InvalidPhoneNumberExceptionTest {
    
    public InvalidPhoneNumberExceptionTest() {
    }
    
    @Test
    public void testConstructors() {
        //test constuctors
        InvalidPhoneNumberException invalidPhoneNumberException = new InvalidPhoneNumberException();
        InvalidPhoneNumberException invalidPhoneNumberExceptionMsg = new InvalidPhoneNumberException("msg");
        InvalidPhoneNumberException invalidPhoneNumberExceptionEx = new InvalidPhoneNumberException(invalidPhoneNumberException);
    }
    
}

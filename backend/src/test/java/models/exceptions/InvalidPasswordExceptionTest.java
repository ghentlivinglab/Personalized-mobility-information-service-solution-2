package models.exceptions;

import org.junit.Test;

public class InvalidPasswordExceptionTest {
    
    public InvalidPasswordExceptionTest() {
    }
    
    @Test
    public void testConstructors() {
        //test constuctors
        InvalidPasswordException invalidPasswordException = new InvalidPasswordException();
        InvalidPasswordException invalidPasswordExceptionMsg = new InvalidPasswordException("msg");
        InvalidPasswordException invalidPasswordExceptionEx = new InvalidPasswordException(invalidPasswordException);
    }
    
}

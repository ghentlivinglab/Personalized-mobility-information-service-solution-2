package models.exceptions;

import org.junit.Test;

public class ExpiredExceptionTest {
    
    public ExpiredExceptionTest() {
    }
    
    @Test
    public void testConstructors() {
        //test constuctors
        ExpiredException expiredException = new ExpiredException();
        ExpiredException expiredExceptionMsg = new ExpiredException("msg");
        ExpiredException expiredExceptionEx = new ExpiredException(expiredException);
    }
    
}

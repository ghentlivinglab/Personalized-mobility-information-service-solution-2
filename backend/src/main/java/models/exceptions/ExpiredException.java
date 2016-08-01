
package models.exceptions;


public class ExpiredException extends Exception {
    
    public ExpiredException(){
    }
    
    public ExpiredException(String ex){
        super(ex);
    }
    
    public ExpiredException(Exception ex){
        super(ex);
    }
}

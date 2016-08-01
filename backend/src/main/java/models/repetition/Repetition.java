package models.repetition;

/**
 * This interface is a model for all types of repetition. 
 * There can be repetitions that happen on a weekly basis, but also every first month of the year. 
 * They all implement the same function today(), which indicates whether or not there is a repetition on the current day. 
 * @author hannedesutter
 */
public interface Repetition {
    
    /*
    The method today returns a boolean value. 
    This value indicates wheter or not there is a repetition on the current day.
    */
    public boolean today();
    
}

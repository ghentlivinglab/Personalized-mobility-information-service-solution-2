package database_v2.exceptions;

/**
 * A general exception to indicate something went wrong in the database
 * component. Usually this exception is a wrapper for an SQLException, but this
 * is not required.
 */
public class DataAccessException extends Exception {

    /**
     * create a new instance.
     * @param message Extra information about the exception
     * @param cause A wrapped exception that triggered this exception.
     */
    public DataAccessException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * create a new instance.
     * @param message Extra information about the exception
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * create a new instance.
     * @param cause A wrapped exception that triggered this exception.
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}

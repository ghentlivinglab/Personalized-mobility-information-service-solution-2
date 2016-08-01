package database_v2.exceptions;

/**
 * this exception is thrown when a database searched failed to get a specific
 * record.
 */
public class RecordNotFoundException extends Exception {

    /**
     * The SQL error state code that is used in Postgresql JDBC.
     */
    public static final String ERRORSTATE = "P0002";

    /**
     * Create a new instance.
     */
    public RecordNotFoundException() {
    }

    /**
     * create a new instance.
     *
     * @param message Extra information about the exception
     */
    public RecordNotFoundException(String message) {
        super(message);
    }
}

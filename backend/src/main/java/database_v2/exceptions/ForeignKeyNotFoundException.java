package database_v2.exceptions;

/**
 * This exception is thrown when a record points to another record that doesn't
 * exist.
 */
public class ForeignKeyNotFoundException extends Exception {

    /**
     * The SQL error state code that is used in Postgresql JDBC.
     */
    public static final String ERRORSTATE = "23503";

    /**
     * create a new instance.
     */
    public ForeignKeyNotFoundException() {
    }

    /**
     * create a new instance.
     *
     * @param message Extra information about the exception
     */
    public ForeignKeyNotFoundException(String message) {
        super(message);
    }
}

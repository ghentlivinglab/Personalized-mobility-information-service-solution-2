package database_v2.exceptions;

/**
 * This exception indicates a violation of a unique constraints in the database.
 */
public class AlreadyExistsException extends Exception {

    /**
     * The sql error state code that is used in Postgresql JDBC.
     */
    public static final String ERRORSTATE = "23505";

}

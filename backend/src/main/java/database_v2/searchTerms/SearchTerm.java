package database_v2.searchTerms;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The SearchTerm interface describes a restriction for a query on the relational database.
 */
public interface SearchTerm {

    /**
     * If the searchTerm has added variables to the WHERE clause of a prepared statement, then he
     * must also fill them in. This function is must be called and then the search term will fill in
     * all his variables and increment the counter. The final counter value is returned.
     *
     * @param start Start of the variables of this search term in the prepared statement
     * @param ps The prepared statement to be filled in.
     * @return The value of the variable counter
     * @throws SQLException Something went wrong with the prepared statement
     */
    int fillInStatement(int start, PreparedStatement ps) throws SQLException;

    /**
     * Get the WHERE clause for the search term. In it's simplest form, this can be something like
     * <pre>
     * table.column = ?
     * </pre>
     *
     * @return The WHERE clause string of this term.
     */
    String getWhereClause();
}

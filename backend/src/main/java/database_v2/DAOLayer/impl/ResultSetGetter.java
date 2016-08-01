package database_v2.DAOLayer.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A ResultSetGetter is used to get a specific value from a ResultSet entry. For
 * example an id column is has an AttributeType of INTEGER. Than an
 * IntegerResultSetGetter is needed to call the getInt(...) method of a
 * ResultSet. This pattern will only work when we have a mapper that returns the
 * right ResultSetGetter for each supported AttributeType. For this we refer to
 * ResultSetterFactory
 *
 * @see ResultSetterFactory
 */
public interface ResultSetGetter {

    /**
     * Perform the right getter on a ResultSet and returns the value as an
     * Object. A mapper class is needed to achieve this pattern. See
     * ResultSetterFactory.
     *
     * @see ResultSetterFactory
     * @param rs The sql ResultSet that currently points to the cell of the the
     * record that you want to retrieve.
     * @param columnName The name of the columns in the sql database of which
     * you want to retrieve the value.
     * @return The value of the of the cell where the given ResultSet points to.
     * @throws SQLException Something with getting the data from the ResultSet
     * went wrong.
     */
    public Object getResult(ResultSet rs, String columnName) throws SQLException;

}

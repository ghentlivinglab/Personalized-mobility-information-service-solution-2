package database_v2.DAOLayer.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The StatementFiller is used to set a variable of a PreparedStatement. Every
 * supported AttributeType needs a specific StatementFiller. For example, an
 * AttributeType of the type Integer will need the setInt(...) method of
 * PreparedStatement to set its value. Then the IntegerStatementFiller will
 * implement this interface by using that method, and using a cast to Integer on
 * the passed object.
 */
public interface StatementFiller {

    /**
     * This function calls a specific setter of the PreparedStatement and passes
     * the obj value. A cast is done on the obj. This pattern only works in
     * combination with a mapper, like the StatementFillerFactory, in order to
     * map Attributes to the right implementation of a StatementFiller.
     *
     * @see StatementFillerFactory
     * @param ps The sql PreparedStatement you want to "prepare" (fill in with
     * relevant data).
     * @param position The position of the data to be filled in in the
     * PreparedStatement (starts counting from 1)
     * @param obj The data to be filled in in the PreparedStatement at the given
     * position.
     * @throws SQLException Something went wrong while filling in data in the
     * PreparedStatement.
     */
    public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException;

}

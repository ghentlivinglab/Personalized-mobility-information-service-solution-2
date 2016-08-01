package database_v2.searchTerms;

import database_v2.DAOLayer.impl.StatementFactory;
import database_v2.DAOLayer.impl.StatementFiller;
import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This searchTerm can be used for tables of the type Array in order to find out
 * if the array contains all the elements of the SearchTerm data array.
 */
public class ArraySearchTerm implements SearchTerm {

    private Attribute attribute;
    private Array array;

    /**
     * Create a new instance of ArraySearchTerm.
     *
     * @param attribute The attribute modeling the column to query. This
     * attribute MUST be of the AttributeType Array. If not, an
     * IllegalArgumentException will be thrown.
     * @param array An sql array that must be contained by the records in order
     * to pass the query.
     */
    public ArraySearchTerm(Attribute attribute, Array array) {
        if (attribute.getAttributeType() != AttributeType.ARRAY) {
            throw new IllegalArgumentException("ArraySearchTerm needs Attribute of array type");
        }
        this.attribute = attribute;
        this.array = array;
    }

    @Override
    public int fillInStatement(int start, PreparedStatement ps) throws SQLException {
        StatementFiller filler = StatementFactory.getStatementFiller(attribute.getAttributeType());
        filler.setStatement(ps, start++, array);
        return start;
    }

    @Override
    public String getWhereClause() {
        // @> is the contains operator in Postgresql
        return attribute.getTableName() + "." + attribute.getColumnName() + " @> ?";
    }

}

package database_v2.searchTerms;

import database_v2.DAOLayer.impl.StatementFactory;
import database_v2.DAOLayer.impl.StatementFiller;
import database_v2.models.Attribute;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class represents a simple SearchTerm that uses equality in SQL (=). It
 * will look for all records where the cells exactly match the data.
 */
public class SimpleSearchTerm implements SearchTerm {

    private final Attribute attribute;
    private final Object term;

    /**
     * Create a new instance.
     *
     * @param attribute The attribute that represents the column on which this
     * SearchTerm applies.
     * @param term The data that must be matched.
     */
    public SimpleSearchTerm(Attribute attribute, Object term) {
        this.attribute = attribute;
        this.term = term;
    }

    @Override
    public int fillInStatement(int start, PreparedStatement ps) throws SQLException {
        StatementFiller filler = StatementFactory.getStatementFiller(attribute.getAttributeType());
        filler.setStatement(ps, start++, term);
        return start;
    }
    
    @Override
    public String getWhereClause() {
        return attribute.getTableName() + "." + attribute.getColumnName() + "=?";
    }

}

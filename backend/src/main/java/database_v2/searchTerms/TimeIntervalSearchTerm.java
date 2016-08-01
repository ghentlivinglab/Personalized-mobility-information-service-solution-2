package database_v2.searchTerms;

import database_v2.DAOLayer.impl.StatementFactory;
import database_v2.DAOLayer.impl.StatementFiller;
import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/**
 * A searchTerm to look for records that contain a time interval that overlaps with a given time
 * interval.
 */
public class TimeIntervalSearchTerm implements SearchTerm {

    private final Attribute dbBegin, dbEnd;
    private final Time begin, end;

    /**
     * Create a new search term.
     * @param dbBegin attribute of the column that contains start 1
     * @param dbEnd attribute of the value that contain end 1
     * @param begin value of start 2
     * @param end value of end 2
     */
    public TimeIntervalSearchTerm(Attribute dbBegin, Attribute dbEnd, Time begin, Time end) {
        this.dbBegin = dbBegin;
        this.dbEnd = dbEnd;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public int fillInStatement(int start, PreparedStatement ps) throws SQLException {
        StatementFiller filler = StatementFactory.getStatementFiller(AttributeType.TIME);
        filler.setStatement(ps, start++, begin);
        filler.setStatement(ps, start++, end);
        return start;
    }

    @Override
    public String getWhereClause() {
        return "(" + dbBegin.getTableName() + "." + dbBegin.getColumnName() + ", "
                + dbEnd.getTableName() + "." + dbEnd.getColumnName() + ")"
                + " OVERLAPS "
                + "(?::time without time zone, ?::time without time zone) ";
    }
}

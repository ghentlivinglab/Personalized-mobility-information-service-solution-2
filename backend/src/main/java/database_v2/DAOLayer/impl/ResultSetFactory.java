package database_v2.DAOLayer.impl;

import database_v2.models.AttributeType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to map a specific AtrributeType to its ResultSetGetter.
 *
 * @see ResultSetGetter
 */
public class ResultSetFactory {

    private static Map<AttributeType, ResultSetGetter> map = fillMap();

    private static Map<AttributeType, ResultSetGetter> fillMap() {
        Map<AttributeType, ResultSetGetter> out = new HashMap<>();
        out.put(AttributeType.TEXT, new TextResultSetGetter());
        out.put(AttributeType.INTEGER, new IntegerResultSetGetter());
        out.put(AttributeType.BOOLEAN, new BooleanResultSetGetter());
        out.put(AttributeType.DATE, new DateResultSetGetter());
        out.put(AttributeType.TIME, new TimeResultSetGetter());
        out.put(AttributeType.ARRAY, new ArrayResultSetGetter());
        out.put(AttributeType.DOUBLE, new DoubleResultSetGetter());
        out.put(AttributeType.BYTE, new ByteResultSetGetter());
        return out;
    }

    /**
     * Get the ResultSetGetter for this specific AttributeType.
     *
     * @param attributeType
     * @return
     */
    public static ResultSetGetter getResultSetGetter(AttributeType attributeType) {
        if (map.containsKey(attributeType)) {
            return map.get(attributeType);
        } else {
            throw new RuntimeException("ResultSetFactory has no ResultSetGetter for " + attributeType.toString());
        }
    }

    private static class IntegerResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getInt(columnName);
        }

    }

    private static class TextResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getString(columnName);
        }

    }

    private static class BooleanResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getBoolean(columnName);
        }

    }

    private static class DateResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getDate(columnName);
        }

    }

    private static class TimeResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getTime(columnName);
        }

    }

    private static class ArrayResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getArray(columnName);
        }

    }

    private static class DoubleResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getDouble(columnName);
        }

    }
    
    private static class ByteResultSetGetter implements ResultSetGetter {

        @Override
        public Object getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getBytes(columnName);
        }
        
    }

}

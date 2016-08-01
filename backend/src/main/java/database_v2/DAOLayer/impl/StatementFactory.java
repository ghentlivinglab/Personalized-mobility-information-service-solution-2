package database_v2.DAOLayer.impl;

import database_v2.models.AttributeType;
import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to get a specific StatementFiller belonging to an
 * AttributeType.
 *
 * @see AttributType
 */
public class StatementFactory {
    
    private static Map<AttributeType, StatementFiller> map = fillMap();

    /**
     * Fill the map with all supported AttributTypes and their StatementFiller
     *
     * @return A map from AttributeType to its corresponding StatementFiller.
     */
    private static Map<AttributeType, StatementFiller> fillMap() {
        Map<AttributeType, StatementFiller> out = new HashMap<>();
        out.put(AttributeType.INTEGER, new IntegerStatementFiller());
        out.put(AttributeType.TEXT, new TextStatementFiller());
        out.put(AttributeType.BOOLEAN, new BooleanStatementFiller());
        out.put(AttributeType.DATE, new DateStatementFiller());
        out.put(AttributeType.TIME, new TimeStatementFiller());
        out.put(AttributeType.ARRAY, new ArrayStatementFiller());
        out.put(AttributeType.DOUBLE, new DoubleStatementFiller());
        out.put(AttributeType.BYTE, new ByteStatementFiller());
        return out;
    }

    /**
     * Get the StatementFiller of a specific AttributeType.
     *
     * @see StatementFiller
     * @param attributeType The attributeType for which you want a
     * StatementFiller.
     * @return The matching StatementFiller for the given attributeType.
     */
    public static StatementFiller getStatementFiller(AttributeType attributeType) {
        if (map.containsKey(attributeType)) {
            return map.get(attributeType);
        } else {
            throw new RuntimeException("StatementFactory does not contain filler for the type: " + attributeType.toString());
        }
    }
    
    private static class IntegerStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setInt(position, (int) obj);
        }
    }
    
    private static class TextStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setString(position, (String) obj);
        }
        
    }
    
    private static class BooleanStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setBoolean(position, (Boolean) obj);
        }
        
    }
    
    private static class DateStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setDate(position, (Date) obj);
        }
        
    }
    
    private static class TimeStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setTime(position, (Time) obj);
        }
        
    }
    
    private static class ArrayStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setArray(position, (Array) obj);
        }
        
    }
    
    private static class DoubleStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setDouble(position, (double) obj);
        }
        
    }
    
    private static class ByteStatementFiller implements StatementFiller {
        
        @Override
        public void setStatement(PreparedStatement ps, int position, Object obj) throws SQLException {
            ps.setBytes(position, (byte[]) obj);
        }
        
    }
    
}

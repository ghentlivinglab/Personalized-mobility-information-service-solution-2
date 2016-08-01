package database_v2.models;

import java.sql.Array;
import java.sql.Date;
import java.sql.Time;

/**
 * This enum holds all supported datatypes for an {@see Atttribute}. There should be a 
 * one on one relation between these types and the datatypes of the columns in the relational
 * database. Each enum value also stores a Class object of the Java datatype it is supposed
 * to map to. For example, the INTEGER type will hold an Integer.class object.
 */
public enum AttributeType {

    /**
     * TEXT datatype in SQL becomes {@see java.lang.String} in Java
     */
    TEXT(String.class),
    /**
     * INTEGER datatype in SQL becomes {@see java.lang.Integer} in Java
     */
    INTEGER(Integer.class),
    /**
     * BOOLEAN datatype in SQL becomes {@see java.lang.Boolean} in Java
     */
    BOOLEAN(Boolean.class),
    /**
     * TIME datatype in SQL becomes {@see java.sql.Time} in Java
     */
    TIME(Time.class),
    /**
     * DATE datatype in SQL becomes {@see java.sql.Date} in Java
     */
    DATE(Date.class),
    /**
     * ARRAY datatype in SQL becomes {@see java.sql.Array} in Java
     */
    ARRAY(Array.class),
    /**
     * NUMERIC datatype in SQL becomes {@see java.lang.Double} in Java
     */
    DOUBLE(Double.class),
    /**
     * BYTEA datatype in SQL becomes {@see Byte[]} in Java
     */
    BYTE(byte[].class);

    private final Class attributeClass;

    private AttributeType(Class attributeClass) {
        this.attributeClass = attributeClass;
    }

    /**
     * Get the class object of the java class on which this datatype should be mapped.
     * For example, the INTEGER type will return an Integer.class object.
     * @return 
     */
    public Class getType() {
        return attributeClass;
    }
}

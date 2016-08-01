package database_v2.models;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An Attribute is a description of a specific column in the relational
 * database. This information is crucial for the internal working of the {
 *
 * @see database_v2.DAOLayer.CRUDdao}. For each field of a {
 * @see database_v2.models.CRUDModel} that is supposed to be mapped to a column
 * in the relational database an Attribute must be made.
 * <p>
 * <h2>Remark on the notion &ldquo;required&rdquo;</h2>
 * The required field indicates if this attributes is needed. This concept
 * stands separated from the required notion in the rest of the business logic.
 * The fact that objects correctly follow the api is something that must be
 * check before it is even passed to the database component. This checks should
 * not be performed in this layer of the application.
 *
 * The required field here are supposed to reflect the situation of the database
 * itself. Required means that the field must be filled in order to get models
 * that result in sensible records. This should reflect the NOT NULL statements
 * from the DDL script of the database. If a column has a NOT NULL specifier,
 * but a default value is defined, than this Attribute is NOT required, since we
 * are perfectly able to write away an object where that field is null.
 */
public class Attribute {

    /**
     * The name of the attribute.
     */
    protected String attribute;

    /**
     * The name of the column this attribute represents.
     */
    protected String columnName;

    /**
     * Indicates if the column requires data.
     */
    protected boolean required;

    /**
     * The type of the attribute.
     */
    protected AttributeType attributeType;

    /**
     * The name of the table this attribute represents.
     */
    protected String tableName;

    /**
     * Create a new Attribute.
     *
     * @param attribute The name of the attribute. <b>must match the name of its
     * getter and setter</b>
     * @param columnName The name of the column were this attribute must come in
     * the database
     * @param attributeType The AttributeType indicates the datatype of the
     * column
     * @param tableName Indicates to which table this column belongs.
     * @param required Indicates is the column requires data.
     */
    public Attribute(String attribute, String columnName, AttributeType attributeType, String tableName, boolean required) {
        this.attribute = attribute;
        this.columnName = columnName;
        this.required = required;
        this.attributeType = attributeType;
        this.tableName = tableName;
    }

    /**
     * Get the name of this attribute. This value must match the name of the
     * getter and setter of the CRUDModel tied to this attribute.
     *
     * @return The name of the attribute.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Set the name of this attribute. This value must match the name of the
     * getter and setter of the CRUDModel tied to this attribute.
     *
     * @param attribute The name of the attribute.
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Returns the name of the column in the relational database that this
     * Attribute describes.
     *
     * @return The name of the column
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the name of the column in the relational database that this
     * Attribute describes.
     *
     * @param columnName The name of the column
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Indicates is this field is required. Please read the extra information
     * concerning the notion required in the javadoc of {
     *
     * @see Attribute} class.
     * @return true if required, false otherwise.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets if this field is required. Please read the extra information
     * concerning the notion required in the javadoc of {
     *
     * @see Attribute} class.
     * @param required true if required, false otherwise.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Get the AttributeType of this attribute.
     *
     * @return The type of this attribute
     */
    public AttributeType getAttributeType() {
        return attributeType;
    }

    /**
     * Set the AttributeType of this attribute.
     *
     * @param attributeType The type of this attribute
     */
    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    /**
     * Get the exact setter name of the method in the model that must be used to
     * fill in the actual value of this attribute.
     *
     * @return the string form of the setter
     */
    public String getSetterName() {
        return "set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
    }

    /**
     * Get the exact getter name of the method in the model that must be used to
     * fill in the actual value of this attribute.
     *
     * @return the string form of the getter
     */
    public String getGetterName() {
        return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
    }

    /**
     * Get the name of the table in the relational database to which the columns
     * belongs.
     *
     * @return the name of the table
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Set the name of the table in the relational database to which the columns
     * belongs.
     *
     * @param tableName the name of the table
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(97, 127)
                .append(attribute)
                .append(columnName)
                .append(required)
                .append(attributeType)
                .append(tableName)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Attribute)) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        Attribute oth = (Attribute) obj;
        return new EqualsBuilder()
                .append(attribute, oth.attribute)
                .append(columnName, oth.columnName)
                .append(required, oth.required)
                .append(attributeType, oth.attributeType)
                .append(tableName, oth.tableName)
                .isEquals();
    }
    
    

}

package database_v2.models;

/**
 * A ForeignKeyAttribute is a specific attribute that describes a foreign key to
 * another table. A class object is stored of the CRUDModel that models the
 * referenced table.
 *
 * @param <T> The CRUDModel that models the table that the foreign key
 * references.
 */
public class ForeignKeyAttribute<T extends CRUDModel> extends Attribute {

    /**
     * Class object of the CRUDModel where this foreign key points to.
     */
    protected Class<T> referencedTable;

    /**
     * Create a new instance.
     *
     * @param attribute The name of the attribute
     * @param columnName The column name that this attribute represents.
     * @param attributeType The type of the Attribute
     * @param tableName The name of the table of the column of this attribute.
     * @param required Indicate is the column requires data.
     * @param referencedTable Class object of CRUDModel representing the table
     * to which this foreign key points.
     */
    public ForeignKeyAttribute(String attribute, String columnName, AttributeType attributeType, String tableName, boolean required,
            Class<T> referencedTable) {
        super(attribute, columnName, attributeType, tableName, required);
        this.referencedTable = referencedTable;
    }

    /**
     * Get the class object of the CRUDModel class that models the referenced
     * table.
     *
     * @return Class object of CRUDModel representing the table to which this
     * foreign key points.
     */
    public Class<T> getReferencedTable() {
        return referencedTable;
    }

}

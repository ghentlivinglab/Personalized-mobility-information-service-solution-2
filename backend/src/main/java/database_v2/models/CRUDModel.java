package database_v2.models;

import java.util.List;

/**
 * <h1>Modeling database tables</h1>
 * A CRUDModel class is supposed to be a direct model of a table in the relational
 * database. Each table requires its own CRUDModel in order so you can use the CRUDdao
 * to write and read from the database.
 * <p>
 * <b>At the moment a default constructor with no args is needed for each CRUDModel</b>
 * <p>
 * @see database_v2.DAOLayer.CRUDdao
 */
public interface CRUDModel extends Joinable {
    /**
     * Returns a list of all the attributes ({@see Attribute}) that are present and not null in this model.
     * All required attributes must be present in this list. All non required attributes are checked if
     * they are filled in (not null) and if this is the case, are added to this list.
     * <p>
     * <b>Do not add the primary key as an attribute!!!</b> Other methods are used for the id.
     * @return 
     */
    public List<Attribute> getActiveAttributeList();
    
    /**
     * Returns a list of all attributes of this model. There must be a one on one relation
     * between each attribute and each column of the table that this model represents. So
     * both required as non required attributes must be present.
     * <b>Do not add the primary key as an attribute!!!</b> Other methods are used for the id.
     * @return 
     */
    public List<Attribute> getAllAttributeList();
    
    /**
     * Returns the name of the table in the relational database for which this model
     * is a representation.
     * @return 
     */
    public String getTableName();
    
    /**
     * Set the primary key identifier of this object corresponding to its record in
     * the relational database.
     * @param id 
     */
    public void setId(int id);
    
    /**
     * Get the primary key identifier of this object corresponding to its record in
     * the relational database.
     * @return 
     */
    public int getId();
    
    /**
     * Get the name of the column that holds the primary key in the relational database.
     * @return 
     */
    public String getIdColumnName();
    
    @Override
    public default String getJoinStatementPart() {
        return getTableName();
    }
}

package database_v2.DAOLayer.impl;

import database_v2.DAOLayer.CRUDdao;
import database_v2.controlLayer.DataAccessProvider;
import database_v2.exceptions.*;
import database_v2.models.Attribute;
import database_v2.models.CRUDModel;
import database_v2.models.TableJoin;
import database_v2.searchTerms.SearchTerm;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class gives a complete implementation of the CRUDdao. This tries a
 * generic approach of DAO design to support as many possible tables with little
 * effort.
 */
public class CRUDdaoImpl extends AbstractDAO implements CRUDdao {

    /**
     * Create a new instance of the CRUDdaoImpl.
     *
     * @param connection An open SQL connection to a relational database.
     * @param dap Reference to the DataAccessProvider, an abstraction of the
     * entire database component
     */
    public CRUDdaoImpl(Connection connection, DataAccessProvider dap) {
        super(connection, dap);
    }

    @Override
    public void create(CRUDModel model)
            throws DataAccessException, AlreadyExistsException, ForeignKeyNotFoundException {
        // here we use getActiveAttributeList() in order to get only the attributes
        // that are not null
        List<Attribute> attributes = model.getActiveAttributeList();

        // make the SQL SELECT part: SELECT (col1, col2, ...) FROM ... Needs to be
        // between brackets
        String columnList = "(" + getColumnListFromAttributes(attributes) + ") ";

        // SQL values part: (?,?,...,?);
        String valuesPlaceholderList = "(";
        for (int i = 0; i < attributes.size(); i++) {
            valuesPlaceholderList += "?, ";
        }
        valuesPlaceholderList = valuesPlaceholderList.substring(0, valuesPlaceholderList.length() - 2);
        valuesPlaceholderList += ")";

        String statement = "INSERT INTO "
                + model.getTableName()
                + columnList
                + " VALUES "
                + valuesPlaceholderList
                + ";";

        try (PreparedStatement ps = prepareAutoGenerated(statement)) {
            for (int i = 0; i < attributes.size(); i++) {
                // iterate over each attribute
                Attribute att = attributes.get(i);

                // this method is the getter of the CRUDModel that holds the value of
                // this attribute
                Method getter = model.getClass().getMethod(att.getGetterName());
                // Use the StatementFactory to fill the statement correctly based upon
                // the AttributeType of the attribute. i+1 scince PreparedStatement starts
                // counting from position 1.
                StatementFactory.getStatementFiller(att.getAttributeType()).setStatement(ps, i + 1, getter.invoke(model));
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new DataAccessException("could not fetch primary key for " + model.getTableName());
                } else {
                    // fill in the id of the record in the model
                    model.setId(rs.getInt(model.getTableName() + "id"));
                }
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals(AlreadyExistsException.ERRORSTATE)) {
                throw new AlreadyExistsException();
            } else if (ex.getSQLState().equals(ForeignKeyNotFoundException.ERRORSTATE)) {
                throw new ForeignKeyNotFoundException();
            } else {
                throw new DataAccessException(ex);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException("from create in CRUDdaoImpl", ex);
        }
    }

    @Override
    public <T extends CRUDModel> T read(int id, Class<T> type) throws DataAccessException, RecordNotFoundException {
        try {
            // use default constructor to create the model we are going to return
            T model = type.newInstance();
            // here we use getAllAttributeList instead of getActiveAttributeList because we
            // don't know which columns are empty
            List<Attribute> allAttributes = model.getAllAttributeList();
            String columnList = getColumnListFromAttributes(allAttributes);
            String statement = "SELECT " + model.getIdColumnName() + ", " + columnList + " FROM " + model.getTableName()
                    + " WHERE " + model.getIdColumnName() + "=? ;";

            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // we found a record with this id, so we can already fill this in
                        model.setId(rs.getInt(model.getIdColumnName()));
                        for (Attribute att : allAttributes) {
                            // for each possible Attribute of the model we check if there is a value in the record.

                            // Here we get the setter method of the model to which we can pass the value
                            // of the cell in the found record
                            Method setter = type.getMethod(att.getSetterName(), att.getAttributeType().getType());
                            // Here we get the appropriate ResultSetGetter belonging to the given AttributeType
                            ResultSetGetter rsg = ResultSetFactory.getResultSetGetter(att.getAttributeType());
                            // Eventually call the setter with the fetched value.
                            Object setterArgument = rsg.getResult(rs, att.getColumnName());
                            setter.invoke(model, setterArgument);
                        }
                    } else {
                        // no records were found with the given id in the table modeled by type
                        throw new RecordNotFoundException();
                    }
                }
                return model;
            } catch (SQLException ex) {
                if (ex.getSQLState().equals(RecordNotFoundException.ERRORSTATE)) {
                    throw new RecordNotFoundException();
                } else {
                    throw new DataAccessException(ex);
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(CRUDModel model)
            throws DataAccessException, AlreadyExistsException, RecordNotFoundException, ForeignKeyNotFoundException {
        // Here we need getAllAttributeList. If we were to use getActiveAttributeList we would get wrong results in
        // the following case: imagine an attribute is filled in in the database. When we update this attribute in the
        // CRUDModel to null (so just drop it), than this would not be passed correctly to the database.
        List<Attribute> allAttributes = model.getAllAttributeList();

        String statement = "UPDATE " + model.getTableName() + " SET ";
        String columnList = "";
        for (Attribute att : allAttributes) {
            columnList += att.getColumnName() + "=?, ";
        }
        statement += columnList.substring(0, columnList.length() - 2);
        // we find the record that needs to be updated simply by looking for its id.
        statement += " WHERE " + model.getIdColumnName() + "=? ;";

        try (PreparedStatement ps = connection.prepareStatement(statement)) {
            for (int i = 0; i < allAttributes.size(); i++) {
                // iterate over all attributes
                Attribute att = allAttributes.get(i);
                // Get the specific getter method of the model that returns the value
                // of the Attribute.
                Method getter = model.getClass().getMethod(att.getGetterName());

                // get the appropriate StatementFiller belonging to the AttributeType. i+1 since a PreparedStatements
                // starts counting from 1.
                StatementFactory.getStatementFiller(att.getAttributeType()).setStatement(ps, i + 1, getter.invoke(model));
            }

            // here we fill in the last '?' from the prep stat. It's the WHERE clause with which we look for
            // the specific id
            ps.setInt(allAttributes.size() + 1, model.getId());
            int affectedRows = ps.executeUpdate();
            // no rows were affected, so no records were even found.
            if (affectedRows == 0) {
                throw new RecordNotFoundException();
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals(AlreadyExistsException.ERRORSTATE)) {
                throw new AlreadyExistsException();
            } else if (ex.getSQLState().equals(ForeignKeyNotFoundException.ERRORSTATE)) {
                throw new ForeignKeyNotFoundException();
            } else {
                throw new DataAccessException(ex);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(CRUDModel model) throws DataAccessException, RecordNotFoundException {
        delete(model.getId(), model.getClass());
    }

    @Override
    public <T extends CRUDModel> void delete(int id, Class<T> type) throws DataAccessException, RecordNotFoundException {
        CRUDModel model = null;
        try {
            // bit dirty, but we need a dummy object of the type class. Not all needed methods
            // can be static. We don't use the state of the object, so the fact that everything is
            // just null (or to whatever value it might be initialized) doesn't matter.
            model = type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        String statement = "DELETE FROM " + model.getTableName()
                + " WHERE " + model.getIdColumnName() + "=? ;";
        try (PreparedStatement ps = connection.prepareStatement(statement)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RecordNotFoundException();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }

    }

    @Override
    public <T extends CRUDModel> int delete(Class<T> type, List<SearchTerm> query)
            throws DataAccessException {
        CRUDModel model = null;
        try {
            // bit dirty, but we need a dummy object of the type class. Not all needed methods
            // can be static. We don't use the state of the object, so the fact that everything is
            // just null (or to whatever value it might be initialized) doesn't matter.
            model = type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        // process the search terms
        String searchList = "";
        for (SearchTerm st : query) {
            searchList += st.getWhereClause() + " AND ";
        }
        // remove the last ' AND ' (5 chars)
        searchList = searchList.substring(0, searchList.length() - 5);

        String stm = "DELETE FROM " + model.getTableName()
                + " WHERE " + searchList + ";";

        try (PreparedStatement ps = connection.prepareStatement(stm)) {
            // first we have to fill in the values on which we are going to search
            int start = 1;
            for (SearchTerm search : query) {
                start = search.fillInStatement(start, ps);
            }
            // return the number of deleted items
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public <T extends CRUDModel> List<T> listAll(Class<T> type) throws DataAccessException {
        try {
            // bit dirty, but we need a dummy object of the type class. Not all needed methods
            // can be static. We don't use the state of the object, so the fact that everything is
            // just null (or to whatever value it might be initialized) doesn't matter.
            CRUDModel prototype = type.newInstance();

            // again, we are fetching a record from the db, so all attributes are needed since
            // we have no information about which columns are null.
            List<Attribute> allAttributes = prototype.getAllAttributeList();
            String statement = "SELECT "
                    + prototype.getIdColumnName() + ", " + getColumnListFromAttributes(allAttributes)
                    + " FROM " + prototype.getTableName() + ";";

            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                List<T> out = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // create new object to be added to the return list.
                        T newInstance = type.newInstance();
                        // fill in id
                        newInstance.setId(rs.getInt(newInstance.getIdColumnName()));
                        for (Attribute att : allAttributes) {
                            // iterate over all the attributes
                            Method setter = type.getMethod(att.getSetterName(), att.getAttributeType().getType());
                            ResultSetGetter rsg = ResultSetFactory.getResultSetGetter(att.getAttributeType());
                            Object setterArgument = rsg.getResult(rs, att.getColumnName());
                            setter.invoke(newInstance, setterArgument);
                        }
                        out.add(newInstance);
                    }
                }
                return out;
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T extends CRUDModel> List<T> simpleSearch(Class<T> type, List<SearchTerm> searchTerms) throws DataAccessException {
        try {
            // bit dirty, but we need a dummy object of the type class. Not all needed methods
            // can be static. We don't use the state of the object, so the fact that everything is
            // just null (or to whatever value it might be initialized) doesn't matter.
            CRUDModel prototype = type.newInstance();

            // when fetch from database getAllAttributeList is needed because we have no prior
            // information to determine is a cell has a null value.
            List<Attribute> allAttributes = prototype.getAllAttributeList();
            String columnList = getColumnListFromAttributes(allAttributes);

            // process the search terms
            String searchList = "";
            for (SearchTerm st : searchTerms) {
                searchList += st.getWhereClause() + " AND ";
            }
            // remove the last ' AND ' (5 chars)
            searchList = searchList.substring(0, searchList.length() - 5);

            // put the statement together
            String statement = "SELECT " + prototype.getIdColumnName() + ", "
                    + columnList + " FROM " + prototype.getTableName()
                    + " WHERE " + searchList + ";";

            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                // first we have to fill in the values on which we are going to search
                int start = 1;
                for (SearchTerm search : searchTerms) {
                    start = search.fillInStatement(start, ps);
                }

                // list to be returned. Empty when no records match
                List<T> out = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Create new object from the type class to be added to out list.
                        T newInstance = type.newInstance();
                        // fill in the id.
                        newInstance.setId(rs.getInt(prototype.getIdColumnName()));
                        for (Attribute att : allAttributes) {
                            // iterate over all possible attributes of the model

                            // get the specefic setter method in order to fill in the value of
                            // the attribute
                            Method setter = type.getMethod(att.getSetterName(), att.getAttributeType().getType());
                            // get the right ResultSetGetter based on the AttributeType of the attribute
                            ResultSetGetter rsg = ResultSetFactory.getResultSetGetter(att.getAttributeType());
                            // At compile time we only know that the ResultSetGetter returns an object. But if
                            // we correctly use the ResultSetFactory, we know this value is of the right type
                            // for this Attribute, so we can pass it to the setter of the model.
                            Object setterArgument = rsg.getResult(rs, att.getColumnName());
                            setter.invoke(newInstance, setterArgument);
                        }
                        out.add(newInstance);
                    }
                }
                return out;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public <T extends CRUDModel> List<T> complexSearch(TableJoin tableJoin, List<SearchTerm> searchTerms, Class<T> returnType)
            throws DataAccessException {
        try {
            // bit dirty, but we need a dummy object of the type class. Not all needed methods
            // can be static. We don't use the state of the object, so the fact that everything is
            // just null (or to whatever value it might be initialized) doesn't matter.
            CRUDModel returnPrototype = returnType.newInstance();
            List<Attribute> allReturnAttributes = returnPrototype.getAllAttributeList();

            // IMPORTANT REMARK: we are working here with joined tables, so we have to work with
            // tablename.columnname patterns in sql instead of only columnname. If we do not do this
            // then the database might complain about ambiguous naming.
            String columnList = returnPrototype.getTableName() + "."
                    + returnPrototype.getIdColumnName() + ", ";
            for (Attribute att : allReturnAttributes) {
                columnList += returnPrototype.getTableName() + "." + att.getColumnName() + ", ";
            }
            // drop the last two characters (', ')
            columnList = columnList.substring(0, columnList.length() - 2);

            String searchList = "";
            for (SearchTerm st : searchTerms) {
                searchList += st.getWhereClause() + " AND ";
            }
            searchList = searchList.substring(0, searchList.length() - 5);

            String statement = "SELECT "
                    + columnList
                    + " FROM "
                    + tableJoin.getJoinStatementPart()
                    + " WHERE "
                    + searchList + ";";

            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                int start = 1;
                for (SearchTerm search : searchTerms) {
                    start = search.fillInStatement(start, ps);
                }

                List<T> out = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        T newInstance = returnType.newInstance();
                        newInstance.setId(rs.getInt(returnPrototype.getIdColumnName()));
                        for (Attribute att : allReturnAttributes) {
                            Method setter = returnType.getMethod(att.getSetterName(), att.getAttributeType().getType());
                            ResultSetGetter rsg = ResultSetFactory.getResultSetGetter(att.getAttributeType());
                            Object setterArgument = rsg.getResult(rs, att.getColumnName());
                            setter.invoke(newInstance, setterArgument);
                        }
                        out.add(newInstance);
                    }
                }
                return out;
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Get the column list in string form for a list of attributes. E.g.
     * table1.col1, table2.col1, ...
     *
     * @param attributes A list of attributes you want to convert to string
     * @return A string representation of the attribute columns.
     */
    private String getColumnListFromAttributes(List<Attribute> attributes) {
        // we make the column list for SELECT (col1, col2, ...) 

        String columnList = "";

        for (Attribute att : attributes) {
            columnList += att.getColumnName() + ", ";
        }

        columnList = columnList.substring(0, columnList.length() - 2);
        return columnList;
    }

}

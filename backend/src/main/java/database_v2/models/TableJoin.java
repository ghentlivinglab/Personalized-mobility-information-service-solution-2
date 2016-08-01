package database_v2.models;

/**
 * <h1>Representing join operations</h1>
 * The TableJoin class is used to represent a join operation in SQL. This always
 * exists out of three parts. We want to join a main table with a toJoin table,
 * which has a foreign key to this main table. So we can represent a join like
 * the following triplet:
 * <p>
 * (mainTable, toJoinTable, foreignKey)
 * <p>
 * In this case mainTable must be of the type Joinable. This means we can
 * construct joins recursively in order to join multiple tables. toJoinTable on
 * the other hand must be a Class object that extends from CRUDModel. This
 * represents a single table. The foreignKey is a foreignKeyAttribute from the
 * toJoinTable that references a table already present in the mainTable.
 *
 * <h2>Examples</h2>
 * Look at the following case:
 * <p>
 * <
 * pre>
 * +----------+-----------------+-----------+
 * | account  | account_address |  address  |
 * +----------+-----------------+-----------+
 * | acountid | accountid       | addressid |
 * |          | addressid       |           |
 * +----------+-----------------+-----------+
 * </pre>
 * <p>
 * We have three tables in our sql database: account, address and a link table
 * between these two. If we want to find the address belonging to a certain
 * account, we have to join these three tables. First we join account and
 * account_address with the foreign key of account_address. This is done with
 * the following logical triplets: <br>
 * join1 = (account, account_address; foreignKey(account_address -> account))
 * join2 = (join1, address, foreignKey(account_address -> address)
 *
 */
public class TableJoin implements Joinable {

    private Joinable mainTable;
    private Class<? extends CRUDModel> toJoinTable;
    private ForeignKeyAttribute<? extends CRUDModel> foreignKey;

    /**
     * Represent join of two individual tables. toJoinTable must have a foreign
     * key relation to mainTable. This foreign key is passed in the form of the
     * ForeignKeyAttribute.
     *
     * @param mainTable Primary table
     * @param toJoinTable Table with foreign key to mainTable
     * @param foreignKey This attribute represents the foreign key.
     */
    public TableJoin(Class<? extends CRUDModel> mainTable,
            Class<? extends CRUDModel> toJoinTable,
            ForeignKeyAttribute<?> foreignKey
    ) {
        try {
            this.mainTable = mainTable.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        this.toJoinTable = toJoinTable;
        this.foreignKey = foreignKey;
    }

    /**
     * Represents the join of a individual table with another TableJoin. With
     * this pattern we can recursively create TableJoins to join more than one
     * table.
     *
     * @param mainTable Primary table
     * @param toJoinTable Table with foreign key to mainTable
     * @param foreignKey This attribute represents the foreign key.
     */
    public TableJoin(Joinable mainTable,
            Class<? extends CRUDModel> toJoinTable,
            ForeignKeyAttribute<?> foreignKey
    ) {
        this.mainTable = mainTable;
        this.toJoinTable = toJoinTable;
        this.foreignKey = foreignKey;
    }

    @Override
    public String getJoinStatementPart() {
        try {
            CRUDModel toJoinTableObj = toJoinTable.newInstance();
            CRUDModel referencedTable = foreignKey.getReferencedTable().newInstance();
            String out = mainTable.getJoinStatementPart()
                    + " JOIN "
                    + toJoinTableObj.getTableName()
                    + " ON "
                    + referencedTable.getTableName() + "." + referencedTable.getIdColumnName()
                    + "="
                    + foreignKey.getTableName() + "." + foreignKey.getColumnName();
            return out;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}

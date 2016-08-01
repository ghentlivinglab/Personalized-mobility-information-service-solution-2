package database_v2.models;

/**
 * All objects that are joinable when representing an sql join operation must
 * implement this interface. See TableJoin for more information.
 *
 * @see TableJoin
 */
public interface Joinable {

    /**
     * Returns the join part of an SQL statement. When this joinable represents
     * the join of tables foo and bar, with a foreign key relation from bar to
     * foo, this function will return:
     * <p>
     * {@code
     * foo JOIN bar ON foo.fooid=bar.fooid
     * }
     *
     * @return
     */
    public String getJoinStatementPart();

}

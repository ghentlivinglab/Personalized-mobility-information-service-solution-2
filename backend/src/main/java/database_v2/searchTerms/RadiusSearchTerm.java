package database_v2.searchTerms;

import database_v2.DAOLayer.impl.StatementFactory;
import database_v2.DAOLayer.impl.StatementFiller;
import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import models.Coordinate;

/**
 * Search for records that have coordinate that lies within a certain radius.
 */
public class RadiusSearchTerm implements SearchTerm {

    private final Attribute radiusAttribute;
    private final Attribute cartX, cartY;
    private final Coordinate point;

    /**
     * Create a new search term.
     *
     * @param radiusAttribute The attribute of the column that contains the radius value
     * @param cartX attribute of the column that contains the cartesian x coordinate of the center
     * of the radius
     * @param cartY attribute of the column that contains the cartesian y coordinate of the center
     * of the radius
     * @param point The coordinate that must lie in the radius in order to match the record.
     */
    public RadiusSearchTerm(Attribute radiusAttribute, Attribute cartX, Attribute cartY, Coordinate point) {
        this.radiusAttribute = radiusAttribute;
        this.cartX = cartX;
        this.cartY = cartY;
        this.point = point;
    }

    @Override
    public int fillInStatement(int start, PreparedStatement ps)
            throws SQLException {
        StatementFiller filler = StatementFactory.getStatementFiller(AttributeType.DOUBLE);
        filler.setStatement(ps, start++, point.getX());
        filler.setStatement(ps, start++, point.getX());
        filler.setStatement(ps, start++, point.getY());
        filler.setStatement(ps, start++, point.getY());
        return start;
    }

    @Override
    public String getWhereClause() {
        String placeholder = "(RADIUS)*(RADIUS) >= ((CARTX-(?))*(CARTX-(?)) + (CARTY-(?))*(CARTY-(?)))";
        placeholder = placeholder.replaceAll("RADIUS",
                radiusAttribute.getTableName() + "." + radiusAttribute.getColumnName());
        placeholder = placeholder.replaceAll("CARTX",
                cartX.getTableName() + "." + cartX.getColumnName());
        placeholder = placeholder.replaceAll("CARTY",
                cartY.getTableName() + "." + cartY.getColumnName());
        return placeholder;
    }
}

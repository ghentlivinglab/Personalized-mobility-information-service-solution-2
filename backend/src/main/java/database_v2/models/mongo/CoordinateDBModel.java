package database_v2.models.mongo;

import database_v2.models.MongoModel;
import java.util.HashMap;
import java.util.Map;
import models.Coordinate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;

/**
 * DB model to represent a coordinate in MongoDB.
 */
public class CoordinateDBModel implements MongoModel {

    private double lat;
    private double lon;
    private double cartX, cartY;

    /**
     * Create a new Coordinate
     *
     * @param lat the latitude of the coordinate [-90, 90] (in degrees)
     * @param lon the longitude of the coordinate [-180, 180] (in degrees)
     */
    private CoordinateDBModel(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public CoordinateDBModel(Coordinate coord) {
        lat = coord.getLat();
        lon = coord.getLon();
        cartX = coord.getX();
        cartY = coord.getY();
    }

    /**
     * Parse a document to a coordinate db model. Only works with documents that
     * are from coordinates.
     *
     * @param doc The document to be parsed.
     * @return A coordinate db model containing the data from the document.
     */
    public static CoordinateDBModel parse(Document doc) {
        return new CoordinateDBModel(
                (double) doc.get("lat"),
                (double) doc.get("lon")
        );
    }

    /**
     * Get the latitude of the coordinate
     *
     * @return latitude (in degrees)
     */
    public double getLat() {
        return lat;
    }

    /**
     * Get the longitude of the coordinate
     *
     * @return longitude (in degrees)
     */
    public double getLon() {
        return lon;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CoordinateDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        CoordinateDBModel other = (CoordinateDBModel) obj;
        return new EqualsBuilder()
                .append(lat, other.lat)
                .append(lon, other.lon)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(8947, 7823)
                .append(lat)
                .append(lon)
                .toHashCode();
    }

    @Override
    public Document toDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("cartX", cartX);
        map.put("cartY", cartY);
        return new Document(map);
    }

}

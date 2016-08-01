package models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This model is used to represent a coordinate. This is used as a value, so the
 * equals method should be overwritten.
 */
public class Coordinate {

    private double lat;
    private double lon;
    private double x, y;
    public static final double refy = 51.053561;
    public static final double refx = 3.718728;

    // some preprocessing variables to speed up convertion to cartesian coordinates.
    private static final double rad = Math.PI / 180.0;
    private static final double mlat = 111132.92
            - 559.82 * Math.cos(rad * 2.0 * refy)
            + 1.175 * Math.cos(rad * 4.0 * refy)
            - 0.0023 * Math.cos(rad * 6.0 * refy);
    private static double mlon = 111412.84 * Math.cos(rad * refy)
            - 93.5 * Math.cos(rad * 3.0 * refy)
            - 0.118 * Math.cos(rad * 5.0 * refy);

    /**
     * Create a new Coordinate
     *
     * @param lat the latitude of the coordinate [-90, 90] (in degrees)
     * @param lon the longitude of the coordinate [-180, 180] (in degrees)
     */
    public Coordinate(double lat, double lon) {
        if (lat < -90.0 || lat > 90.0) {
            throw new IllegalArgumentException("lat must be in interval [-90,90]");
        }
        this.lat = lat;
        if (lon < -180.0 || lon > 180.0) {
            throw new IllegalArgumentException("lon must be in interval [-180,180]");
        }
        this.lon = lon;
        convertToCartesian();
    }

    private void convertToCartesian() {
        y = (lat - refy) * mlat;
        x = (lon - refx) * mlon;
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * This method calculates the distance between the coordinate and a give
     * coordinate. This formula does not take the hight of the coordiantes into
     * accound.
     *
     * @param coordinate the coordinate we need to know the distance to.
     * @return the distance in meters to given coordinate.
     */
    public double distance(Coordinate coordinate) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(coordinate.getLat() - lat);
        double dLng = Math.toRadians(coordinate.getLon() - lon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(coordinate.getLat()))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    public double getSquaredCartDistance(Coordinate coordinate) {
        return (x - coordinate.getX()) * (x - coordinate.getX())
                + (y - coordinate.getY()) * (y - coordinate.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Coordinate other = (Coordinate) obj;
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
}

package datasources.waze;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import models.AddressCoordinateTranslator;
import models.Coordinate;
import org.json.JSONObject;

/**
 * Get the formatted address from the google API. In order to avoid flooding that API, this will be
 * slowed down and cached.
 */
public class AddressFormatter {

    private static AddressFormatter instance;

    /**
     * Get the singleton instance.
     * @return the instance
     */
    public static AddressFormatter instance() {
        if (instance == null) {
            instance = new AddressFormatter();
        }
        return instance;
    }

    private final Cache<Coordinate, String> coordinateCache;

    private AddressFormatter() {
        coordinateCache = CacheBuilder.newBuilder()
                .maximumSize(2000)
                .recordStats()
                .build();
    }

    /**
     * Get as much info about a coordinates address as possible.
     * @param coord
     * @param jsonObject
     * @return 
     */
    public String getFormattedAddress(Coordinate coord, JSONObject jsonObject) {
        String cachedAddress = coordinateCache.getIfPresent(coord);
        if (cachedAddress != null) {
            return cachedAddress;
        }
        try {
            // max 10 calls per minute to google api
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            return "";
        }
        String formattedAddress = AddressCoordinateTranslator.getFormattedAddress(coord);
        if (formattedAddress == null || formattedAddress.equals("")) {
            StringBuilder sb = new StringBuilder();
            boolean hasCity = false;
            if (jsonObject.has("city")) {
                hasCity = true;
                sb.append(jsonObject.getString("city"));
            }
            if (jsonObject.has("street")) {
                if (hasCity) {
                    sb.append(" - ");
                }
                sb.append(jsonObject.getString("street"));
            }
            formattedAddress = sb.toString();
        } else {
            coordinateCache.put(coord, formattedAddress);
        }
        return formattedAddress;
    }

    public String getCacheStats() {
        return coordinateCache.stats().toString();
    }
}

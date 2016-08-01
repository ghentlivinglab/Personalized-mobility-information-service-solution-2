package models;

import backend.AppProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import models.exceptions.TranslationException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddressCoordinateTranslator {

    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public static String getFormattedAddress(Coordinate coord) {
        String responseString = null;
        try {
            responseString = getGoogleResponse(coord);
        } catch (TranslationException ex) {
            Logger.getLogger(AddressCoordinateTranslator.class).error(ex);
            return "";
        }

        JSONObject responseJson = new JSONObject(responseString);

        // check the status code that Google returns
        if (!responseJson.getString("status").equals("OK")) {
            Logger.getLogger(AddressCoordinateTranslator.class).error(
                    "Google GEOCODE call failed: " + responseJson.getString("status"));
            return "";
        }

        JSONArray resultsArray = responseJson.getJSONArray("results");
        return resultsArray.getJSONObject(0).getString("formatted_address");
    }

    private static String getGoogleResponse(Coordinate coord) throws TranslationException {
        // Build URL for given address.
        Logger log = Logger.getLogger(AddressCoordinateTranslator.class);
        URL url;
        try {
            String apiKey = "&key=" + AppProperties.instance().getProp(AppProperties.PROP_KEY.GOOGLE_API_KEY);
            url = new URL(
                    URL + "?latlng=" + coord.getLat() + "," + coord.getLon() + "&sensor=false" + apiKey);
            log.debug("GEOCODE request: " + url.toString());
        } catch (MalformedURLException ex) {
            throw new TranslationException("Could not encode the given Address");
        }
        // Create connection.
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException ex) {
            throw new TranslationException("Could not open url-connection for address check.");
        }

        // Create a new output stream.
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        try {
            // Copy output to output stream.
            IOUtils.copy(conn.getInputStream(), output);
        } catch (IOException ex) {
            throw new TranslationException("Could not copy output stream for address check.");
        }

        try {
            output.close();
        } catch (IOException ex) {
            throw new TranslationException("Could not close output stream for address check.");
        }
        // Make output stream a String.
        return output.toString();
    }

}

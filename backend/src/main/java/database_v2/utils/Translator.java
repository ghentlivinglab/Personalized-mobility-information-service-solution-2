package database_v2.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * This singleton can be used to get translations from an external JSON file.
 */
public class Translator {

    // SINGLETON
    private static Translator instance = new Translator();

    /**
     * Get an instance.
     */
    public static Translator instance() {
        if (instance == null) {
            instance = new Translator();
        }
        return instance;
    }

    // CLASS
    private final Map<String, String> map;

    private Translator() {
        this.map = new HashMap<>();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/translations.json")))) {
            String file = IOUtils.toString(in);
            JSONObject json = new JSONObject(file);
            json = json.getJSONObject("translations");
            for (Object key : json.keySet()) {
                map.put((String) key, json.getString((String) key));
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass()).fatal(ex);
            System.exit(1);
        }
    }

    /**
     * Get the translation of a string
     * @param toTranslate
     * @return The translation
     */
    public String getTranslation(String toTranslate) {
        return map.getOrDefault(toTranslate, null);
    }

    /**
     * Check if the string has a translation
     * @param toTranslate String to be checked
     * @return true if it has translation, false otherwise
     */
    public boolean hasTranslation(String toTranslate) {
        return map.containsKey(toTranslate);
    }

}

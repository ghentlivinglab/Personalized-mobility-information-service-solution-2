package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 */
public class AppProperties {

    // SINGLETON
    private static AppProperties instance = null;

    public static AppProperties instance() {
        if (instance == null) {
            instance = new AppProperties();
        }
        return instance;
    }

    // CLASS DEFINTION
    private final Properties properties;

    private AppProperties() {
        properties = new Properties();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                AppProperties.class.getResourceAsStream("/backend.properties")))) {
            properties.load(in);
        } catch (IOException ex) {
            Logger.getLogger(getClass()).fatal("could not load properties", ex);
            System.exit(1);
        }
    }
    
    public String getProp(PROP_KEY key) {
        return properties.getProperty(key.toString());
    }
    
    public int getPropAsInt(PROP_KEY key) {
        return Integer.parseInt(properties.getProperty(key.toString()));
    }
    
    public long getPropAsLong(PROP_KEY key) {
        return Long.parseLong(properties.getProperty(key.toString()));
    }
    
    public double getPropAsDouble(PROP_KEY key) {
        return Double.parseDouble(properties.getProperty(key.keyString));
    }

    public enum PROP_KEY {
        PGSQL_HOST("postgres_host"),
        PGSQL_POST("postgres_port"),
        PGSQL_DB_NAME("postgres_dbname"),
        PGSQL_USER("postgres_user"),
        PGSQL_PASS("postgres_password"),
        MONGO_HOST("mongo_host"),
        MONGO_PORT("mongo_port"),
        MONGO_DB_NAME("mongo_database"),
        WAZE_POLLING_FEQ("polling_freq"),
        WAZE_JSON_URL("waze_json_url"),
        GOOGLE_API_KEY("google_api_key"),
        EVENT_RECENT_TRES("event_recent_treshold"),
        EVENT_KEEP_DAYS("event_keep_days"),
        EVENT_DURATION("event_duration_prediction"),
        MATCH_ROUTE_DIST("dist_to_route_treshold"),
        MAIL_NOTIF_FREQ("max_mail_delay"),
        MOBILE_PUSH_NOTIF("google_api_key_push_notifications"),
        MAIL_SERVER_HOST("mail_server_host"),
        MAIL_SERVER_FROM("mail_server_from");

        private final String keyString;

        private PROP_KEY(String keyString) {
            this.keyString = keyString;
        }

        @Override
        public String toString() {
            return keyString;
        }
    }
}

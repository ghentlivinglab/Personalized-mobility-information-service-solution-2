package datasources.waze;

import backend.AppProperties;
import database_v2.controlLayer.*;
import database_v2.exceptions.DataAccessException;
import datasources.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import models.event.Event;
import models.event.Jam;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class WazeDataSource implements DataSource {

    private final Database db;
    private final Map<String, Event> eventBuffer = new HashMap<>();
    private final WazeEventBuilder eventBuilder;
    private final WazeJamBuilder jamBuilder;
    private final AddressFormatter addressFormatter;

    private final String eventsField = "alerts";
    private final String jamsField = "jams";

    private final long sleepTime;
    private final boolean testing;

    public WazeDataSource(Database db) {
        this.db = db;
        eventBuilder = new WazeEventBuilder(db);
        jamBuilder = new WazeJamBuilder();
        addressFormatter = AddressFormatter.instance();
        sleepTime = 70L;
        testing = false;
    }

    /**
     * ONLY FOR TESTING
     *
     * @param db
     * @param formatter
     */
    public WazeDataSource(Database db, AddressFormatter formatter) {
        this.db = db;
        eventBuilder = new WazeEventBuilder(db, formatter);
        jamBuilder = new WazeJamBuilder();
        addressFormatter = formatter;
        sleepTime = 0L;
        testing = true;
    }

    @Override
    public Collection<Event> fetchEvents() {
        eventBuffer.clear();
        String dataString = null;
        try {
            dataString = getDataDump();
        } catch (IOException ex) {
            Logger.getLogger(getClass()).error(
                    "Problems with reading Waze data dump, skipping this one", ex);
            return new ArrayList<>();
        }
        JSONObject jsonObject = new JSONObject(dataString);
        JSONArray eventJSONArray = jsonObject.getJSONArray(eventsField);
        processJSONEvents(eventJSONArray);
        if (jsonObject.has(jamsField)) {
            JSONArray jamJSONArray = jsonObject.getJSONArray("jams");
            processJSONJams(jamJSONArray);
        }
        Logger.getLogger(getClass()).info("Coordinate cache: " + addressFormatter.getCacheStats());
        return eventBuffer.values();
    }

    private void processJSONEvents(JSONArray eventJSONArray) {
        for (int i = 0; i < eventJSONArray.length(); i++) {
            try {
                Event parsedEvent = eventBuilder.buildEvent(eventJSONArray.getJSONObject(i));
                eventBuffer.put(parsedEvent.getUuid(), parsedEvent);
            } catch (Exception ex) {
                // we put a general catch block here to make this thread a bit more robust against
                // errors. If we wouldn't do this and somewhere along the line there is an exception
                // thrown, then this thread would come to an end while the backend still remains up.
                Logger.getLogger(getClass()).error(ex);
            }
        }

    }

    private final String[] jamDesc = new String[]{
        "Opgelost",
        "Lichte vertraging",
        "Lichte file",
        "Matige file",
        "Zware file",
        "Volledige stilstand"
    };

    private void processJSONJams(JSONArray jamJSONArray) {
        for (int i = 0; i < jamJSONArray.length(); i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass()).error(ex);
            }
            JSONObject jsonJam = jamJSONArray.getJSONObject(i);
            Jam parsedJam = jamBuilder.buildJam(jsonJam);
            if (jsonJam.has("blockingAlertUuid")) {
                String parentEventUuid = jsonJam.getString("blockingAlertUuid");
                if (eventBuffer.containsKey(parentEventUuid)) {
                    eventBuffer.get(parentEventUuid).addJam(parsedJam);
                } else {
                    Logger.getLogger(getClass()).debug("Waze datadump jam points to non existing event");
                }
            } else {
                try {
                    Event jamEvent = new Event(
                            jsonJam.getString("uuid"),
                            parsedJam.getLineView().get(0),
                            true,
                            parsedJam.getPublicationTimeMillis(),
                            System.currentTimeMillis(),
                            jamDesc[jsonJam.getInt("level")],
                            addressFormatter.getFormattedAddress(parsedJam.getLineView().get(0), jsonJam),
                            db.getEventtype("JAM").getValue1()
                    );
                    jamEvent.addJam(parsedJam);
                    eventBuffer.put(jamEvent.getUuid(), jamEvent);
                } catch (DataAccessException ex) {
                    Logger.getLogger(getClass()).error(ex);
                }
            }
        }
    }

    public String getDataDump() throws IOException {
        if (!testing) {
            InputStream inStream
                    = new URL(AppProperties.instance().getProp(AppProperties.PROP_KEY.WAZE_JSON_URL))
                    .openStream();
            return IOUtils.toString(inStream);
        } else {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/wazedump.json")))) {
                return IOUtils.toString(in);
            }
        }
    }

}

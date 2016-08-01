package datasources.waze;

import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import database_v2.utils.Translator;
import models.Coordinate;
import models.event.Event;
import models.event.EventType;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 */
public class WazeEventBuilder {

    private JSONObject jsonObject;
    private final Database db;
    private final AddressFormatter addressFormatter;
    private final Translator translator;

    public WazeEventBuilder(Database db) {
        this.db = db;
        this.addressFormatter = AddressFormatter.instance();
        translator = Translator.instance();
    }
    
    /**
     * ONLY FOR TESTING
     * @param db
     * @param formatter 
     */
    public WazeEventBuilder(Database db, AddressFormatter formatter) {
        this.db = db;
        this.addressFormatter = formatter;
        translator = Translator.instance();
    }

    public Event buildEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        EventType eventType;
        try {
            eventType = getEventType();
        } catch (DataAccessException ex) {
            // something went wrong with getting the event type
            Logger.getLogger(getClass()).error(ex);
            return null;
        }

        Coordinate coord = getCoordinate();
        String formattedAddress = addressFormatter.getFormattedAddress(coord, jsonObject);

        return new Event(
                jsonObject.getString("uuid"),
                getCoordinate(),
                true, // default to active
                jsonObject.getLong("pubMillis"),
                System.currentTimeMillis(),
                getDescription(),
                formattedAddress,
                eventType
        );
    }

    public Coordinate getCoordinate() {
        JSONObject locationObject = jsonObject.getJSONObject("location");
        return new Coordinate(
                locationObject.getDouble("y"),
                locationObject.getDouble("x")
        );
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (jsonObject.has("subtype")
                && translator.hasTranslation(jsonObject.getString("subtype"))) {
            sb.append(translator.getTranslation(jsonObject.getString("subtype")));
        }
        if (jsonObject.has("reportDescription")) {
            sb.append(" - ");
            sb.append(jsonObject.getString("reportDescription"));
        }
        return sb.toString();
    }

    public EventType getEventType()
            throws DataAccessException {
        return db.getEventtype(jsonObject.getString("type")).getValue1();
    }

}

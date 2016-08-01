package datasources.waze;

import java.util.ArrayList;
import java.util.List;
import models.Coordinate;
import models.event.Jam;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class WazeJamBuilder {

    private JSONObject jsonObject;

    public WazeJamBuilder() {
    }

    public Jam buildJam(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return new Jam(
                jsonObject.getString("uuid"),
                jsonObject.getLong("pubMillis"),
                getCoordinateList(),
                getSpeed(),
                jsonObject.getInt("delay")
        );
    }

    public List<Coordinate> getCoordinateList() {
        JSONArray jsonLineArray = jsonObject.getJSONArray("line");
        List<Coordinate> out = new ArrayList<>();
        for (int i = 0; i < jsonLineArray.length(); i++) {
            JSONObject jsonLinePoint = jsonLineArray.getJSONObject(i);
            out.add(new Coordinate(
                    jsonLinePoint.getDouble("y"),
                    jsonLinePoint.getDouble("x")
            ));
        }
        return out;
    }

    public int getSpeed() {
        // we get from Waze the speed in m/s. So here we convert by multiplication with 3.6.
        return (int) (jsonObject.getDouble("speed") * 3.6);
    }
}

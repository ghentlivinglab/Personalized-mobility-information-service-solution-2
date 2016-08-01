package database_v2.models.mongo;

import database_v2.models.MongoModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;

/**
 * DB model to represent a jam for MongoDB
 */
public class JamDBModel implements MongoModel {

    private String uuid;
    private long publicationTimeMillis;
    private List<CoordinateDBModel> line;
    private int speed, delay;

    public JamDBModel(String uuid, long publicationTimeMillis, List<CoordinateDBModel> line, int speed, int delay) {
        this.uuid = uuid;
        this.publicationTimeMillis = publicationTimeMillis;
        this.line = line;
        this.speed = speed;
        this.delay = delay;
    }

    /**
     * Parse a document to a Jam. Only works if the document is really a representation of a jam.
     *
     * @param doc the document to be parsed.
     * @return A parsed jam.
     */
    public static JamDBModel parse(Document doc) {
        List<CoordinateDBModel> line = new ArrayList<>();
        List<Document> lineDocs = (List<Document>) doc.get("line");
        lineDocs.forEach(lineDoc -> {
            line.add(CoordinateDBModel.parse(lineDoc));
        });
        return new JamDBModel(
                (String) doc.get("uuid"),
                (long) doc.get("publicationTimeMillis"),
                line,
                (int) doc.get("speed"),
                (int) doc.get("delay")
        );
    }

    @Override
    public Document toDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        List<Document> lineDoc = new ArrayList<>();
        line.forEach(coord -> {
            lineDoc.add(coord.toDocument());
        });
        map.put("line", lineDoc);
        map.put("publicationTimeMillis", publicationTimeMillis);
        map.put("speed", speed);
        map.put("delay", delay);
        return new Document(map);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getPublicationTimeMillis() {
        return publicationTimeMillis;
    }

    public void setPublicationTimeMillis(long publicationTimeMillis) {
        this.publicationTimeMillis = publicationTimeMillis;
    }

    public List<CoordinateDBModel> getLine() {
        return line;
    }

    public void setLine(List<CoordinateDBModel> line) {
        this.line = line;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 47)
                .append(uuid)
                .append(line)
                .append(publicationTimeMillis)
                .append(speed)
                .append(delay)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JamDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        JamDBModel rhs = (JamDBModel) obj;
        return new EqualsBuilder()
                .append(uuid, rhs.uuid)
                .append(line, rhs.line)
                .append(publicationTimeMillis, rhs.publicationTimeMillis)
                .append(speed, rhs.speed)
                .append(delay, rhs.delay)
                .isEquals();

    }

}

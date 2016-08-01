package database_v2.models.mongo;

import database_v2.models.MongoModel;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;

/**
 * DB model to represent a source
 */
public class SourceDBModel implements MongoModel{
    private String name;
    private String url;

    /**
     *
     * @param name
     * @param url
     */
    public SourceDBModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     *
     * @param doc
     * @return
     */
    public static SourceDBModel parse(Document doc) {
        return new SourceDBModel(
                doc.get("name", String.class),
                doc.get("url", String.class)
        );
    }
    
    @Override
    public Document toDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("url", url);
        return new Document(map);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SourceDBModel)) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        SourceDBModel oth = (SourceDBModel) obj;
        return new EqualsBuilder()
                .append(name, oth.name)
                .append(url, oth.url)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(71, 257)
                .append(name)
                .append(url)
                .toHashCode();
    }
    
}

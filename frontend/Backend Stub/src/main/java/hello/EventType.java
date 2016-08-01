package hello;


import java.util.List;

public class EventType {

    private String id;
    private String type;
    private String subType;
    private List<Transportation> relevantForTransportationTypes;

    public EventType(String id, String type, String subType, List<Transportation> relevantForTransportationTypes) {
        this.id = id;
        this.type = type;
        this.subType = subType;
        this.relevantForTransportationTypes = relevantForTransportationTypes;
    }

    public EventType(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public List<Transportation> getRelevantForTransportationTypes() {
        return relevantForTransportationTypes;
    }

    public void setRelevantForTransportationTypes(List<Transportation> relevantForTransportationTypes) {
        this.relevantForTransportationTypes = relevantForTransportationTypes;
    }
}

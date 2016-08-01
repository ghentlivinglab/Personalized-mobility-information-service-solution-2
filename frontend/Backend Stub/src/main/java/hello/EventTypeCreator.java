package hello;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EventTypeCreator {

    private final AtomicInteger eventTypeCounter = new AtomicInteger();

    public EventType createEventType1() {
        String id = String.valueOf(eventTypeCounter.getAndIncrement());
        String type = "ROAD_CLOSED";
        String subtype = "ROAD_CLOSED_EVENT";
        ArrayList<Transportation> relevantForTransportationsTypes = new ArrayList<Transportation>();
        relevantForTransportationsTypes.add(Transportation.bike);
        relevantForTransportationsTypes.add(Transportation.car);

        return new EventType(id, type, subtype, relevantForTransportationsTypes);
    }

    public EventType createEventType2() {
        String id = String.valueOf(eventTypeCounter.getAndIncrement());
        String type = "WEATHERHAZARD";
        String subtype = "HAZARD_ON_SHOULDER_CAR_STOPPED";
        ArrayList<Transportation> relevantForTransportationsTypes = new ArrayList<Transportation>();
        relevantForTransportationsTypes.add(Transportation.car);
        relevantForTransportationsTypes.add(Transportation.bike);
        relevantForTransportationsTypes.add(Transportation.bus);

        return new EventType(id, type, subtype, relevantForTransportationsTypes);
    }

}

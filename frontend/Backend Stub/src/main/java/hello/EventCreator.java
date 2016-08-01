package hello;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EventCreator {

    private AtomicInteger eventCounter;
    private AtomicInteger eventTypeCounter;
    private Map<String, Event> events;

    public EventCreator() {
        this.eventTypeCounter = EventTypeController.getEventTypeCounter();
        this.eventCounter = EventController.getEventCounter();
        events = new HashMap<String,Event>();
        fillEvents();
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    private void fillEvents() {
        event01();
        event02();
    }

    private void event01() {
        // Get next free id number for the event
        String id = Integer.toString(eventCounter.getAndIncrement());
        Coordinate coordinates = new Coordinate(51.038362, 3.731795);

        String publicationTime = "Sat Nov 28 18:39:55 +0000 2015";
        String lastEditTime = "Sat Nov 28 18:39:55 +0000 2015";
        String description = "Rioleringswerken";
        Jam jam = null;
        String[] source = {"Waze"};

        // create a new EventType for this event. Normally we would reuse EventTypes where possible
        String eventTypeId = Integer.toString(eventTypeCounter.getAndIncrement());
        String type = "ROAD_CLOSED";
        String subtype = "ROAD_CLOSED_EVENT";
        List<Transportation> relevant_for_trans = Arrays.asList(Transportation.bike, Transportation.bus, Transportation.car);
        EventType eventType = new EventType(eventTypeId, type, subtype, relevant_for_trans);

        Event event = new Event(id, coordinates, true, publicationTime, lastEditTime, description, jam, source, eventType);

        events.put(id, event);
    }

    private void event02() {
        // Get next free id number for the event
        String id = Integer.toString(eventCounter.getAndIncrement());
        Coordinate coordinates = new Coordinate(51.049899, 3.730930);

        String publicationTime = "Wed Feb 10 15:52:07 +0000 2016";
        String lastEditTime = "Wed Feb 10 15:52:07 +0000 2016";
        String description = null;
        Jam jam = new Jam(new Coordinate(51.049899, 3.730930), new Coordinate(51.050614, 3.726282), 5, 15);
        String[] source = {"Waze"};

        // create a new EventType for this event. Normally we would reuse EventTypes where possible
        String eventTypeId = Integer.toString(eventTypeCounter.getAndIncrement());
        String type = "JAM";
        String subtype = "JAM_HEAVY_TRAFFIC";
        List<Transportation> relevant_for_trans = Arrays.asList(Transportation.bus, Transportation.car);
        EventType eventType = new EventType(eventTypeId, type, subtype, relevant_for_trans);

        Event event = new Event(id, coordinates, true, publicationTime, lastEditTime, description, jam, source, eventType);

        events.put(id, event);
    }
}

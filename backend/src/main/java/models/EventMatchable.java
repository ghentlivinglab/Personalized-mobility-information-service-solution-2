package models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import models.event.EventType;

/**
 *
 */
public abstract class EventMatchable extends Mutable {

    protected Map<Integer, EventType> notifyForEventTypes;

    public EventMatchable(boolean muteNotification, Map<Integer, EventType> notifyForEventTypes) {
        super(muteNotification);
        if (notifyForEventTypes == null) {
            this.notifyForEventTypes = new ConcurrentHashMap<>();
        } else {
            this.notifyForEventTypes = notifyForEventTypes;
        }
    }

    public Map<Integer, EventType> getNotifyForEventTypes() {
        return notifyForEventTypes;
    }

    /**
     * Adds an EventType to the existing map of eventtypes.
     *
     * @param id the id of the given event type
     * @param eventtype Object of EventType, respresents the eventtype to add.
     */
    public void addEventType(Integer id, EventType eventtype) {
        notifyForEventTypes.put(id, eventtype);
    }

    /**
     * Deletes an EventType from the existing map of eventtypes.
     *
     * @param id the id of the Event Type.
     */
    public void deleteEventType(Integer id) {
        notifyForEventTypes.remove(id);
    }
    
}

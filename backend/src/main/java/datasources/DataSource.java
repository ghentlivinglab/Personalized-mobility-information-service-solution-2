package datasources;

import java.util.Collection;
import models.event.Event;

/**
 * Interface for a source of events
 */
public interface DataSource {

    /**
     * The method that fetches all the Events from a certain source.
     *
     * @return the collection of Events from the source.
     */
    public Collection<Event> fetchEvents();
}

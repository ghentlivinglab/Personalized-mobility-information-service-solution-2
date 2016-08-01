package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin
@RestController

@RequestMapping("/event")
public class EventController {
    private Map<String, Event> events;
    private EventCreator eventCreator;

    private static final AtomicInteger eventCounter = new AtomicInteger();
    public static AtomicInteger getEventCounter() { return eventCounter; }

    public EventController() {
        eventCreator = new EventCreator();

        events = eventCreator.getEvents();
    }

    // Returns all events (endpoint GET /event)
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<Event>> getEvents(@RequestParam(value="id",  required = false) String id) {
            return new ResponseEntity<>(new ArrayList<>(events.values()), HttpStatus.OK);

    }

    // Create a new event (endpoint POST /event)
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        event.setId(Integer.toString(eventCounter.getAndIncrement()));
        events.put(event.getId(), event);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    // Returns a specific event (endpoint GET /event/{event_id} )
    @RequestMapping(value={"/{event_id}"}, method=RequestMethod.GET)
    public ResponseEntity<Event> getEvent(@PathVariable String event_id) {
        if (events.containsKey(event_id)) {
            return new ResponseEntity<>(events.get(event_id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a specific event (endpoint PUT /event/{event_id} )
    @RequestMapping(value={"/{event_id}"}, method = RequestMethod.PUT)
    public ResponseEntity<Event> updateEvent(@PathVariable String event_id, @RequestBody Event event) {

        // Check if the event even exists
        if(!events.containsKey(event_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        events.put(event_id, event);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}

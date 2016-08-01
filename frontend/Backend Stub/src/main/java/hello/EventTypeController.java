package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin
@RestController

@RequestMapping("/eventtype")
public class EventTypeController {

    private List<EventType> eventTypes;
    private EventTypeCreator eventTypeCreator = new EventTypeCreator();
    private static AtomicInteger eventTypeCounter = new AtomicInteger();

    public EventTypeController() {
        eventTypes = new ArrayList<>();
        eventTypes.add(eventTypeCreator.createEventType1());
        eventTypes.add(eventTypeCreator.createEventType2());
    }

    public static AtomicInteger getEventTypeCounter() { return eventTypeCounter; }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EventType>> getEventTypesByTransport(@RequestParam(value = "transportation_type", required=false) final String transportationType) {

        // only return eventTypes that are relevant for a specific type of transport
        if(transportationType != null) {
            List<EventType> temp = new ArrayList<>(eventTypes);
            try {
                // Remove all EventTypes that are not relevant for transportationType
                temp.removeIf(s -> !(s.getRelevantForTransportationTypes().contains(Transportation.valueOf(transportationType))));
            } catch (IllegalArgumentException ex) {
                // This exception is thrown when a non-existing transportation type is queried
                return new ResponseEntity<List<EventType>>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            return new ResponseEntity<>(temp, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<EventType>>(eventTypes, HttpStatus.OK);
        }
    }

}

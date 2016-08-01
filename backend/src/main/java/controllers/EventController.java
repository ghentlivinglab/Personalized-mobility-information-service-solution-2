package controllers;

import DTO.models.EventDTO;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.controlLayer.Database;
import DTO.mappers.EventMapper;
import database_v2.exceptions.DataAccessException;
import java.util.ArrayList;
import models.event.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.javatuples.Pair;

import java.util.List;

/**
 * Handles incoming requests related to events.
 */
@CrossOrigin
@RestController
@RequestMapping("/event")
public class EventController {

    private final Database database;
    private final EventMapper eventMapper;

    /**
     *
     * @param database
     */
    public EventController(Database database) {
        this.database = database;
        this.eventMapper = new EventMapper();
    }

    /**
     * Get all events.
     *
     * @param recent Only return recent events
     * @return A list of all events.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EventDTO>> getEvents(
            @RequestParam(value = "recent", required = false) boolean recent) {
        List<EventDTO> events = new ArrayList<>();
        try {
            List<Pair<String, Event>> result = null;
            if(recent) {
                result = database.getRecentEvents();
            } else {
                result = database.listAllEvents();
            }
            for (Pair<String, Event> p : result) {
                events.add(eventMapper.convertToDTO(p.getValue1(), p.getValue0()));
            }
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add a new event.
     *
     * @param eventDTO the event that needs to be added.
     * @param accessToken
     * @return The created event.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<EventDTO> addEvent(@RequestBody EventDTO eventDTO,
            @RequestHeader("Authorization") String accessToken) {
        try {
            Event event = eventMapper.convertFromDTO(eventDTO);
            String id = database.createEvent(event);
            return new ResponseEntity<>(eventMapper.convertToDTO(event, id), HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a specific event.
     *
     * @param eventId The id of the event to get from the database.
     * @param accessToken
     * @return The event with the corresponding id.
     */
    @RequestMapping(value = {"/{event_id}"}, method = RequestMethod.GET)
    public ResponseEntity<EventDTO> getEvent(@PathVariable("event_id") String eventId) {
        Event event;
        try {
            event = database.getEvent(eventId);
            return new ResponseEntity<>(eventMapper.convertToDTO(event, eventId), HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Edit a certain event.
     *
     * @param eventId The id of the event.
     * @param eventDTO The event that needs to be edited.
     * @param accessToken
     * @return The event that had been adjusted.
     */
    @RequestMapping(value = {"/{event_id}"}, method = RequestMethod.PUT)
    public ResponseEntity<EventDTO> updateEvent(@PathVariable("event_id") String eventId,
            @RequestBody EventDTO eventDTO,
            @RequestHeader("Authorization") String accessToken) {
        Event event;
        try {
            event = eventMapper.convertFromDTO(eventDTO);
            database.updateEvent(eventId, event);
            return new ResponseEntity<>(eventMapper.convertToDTO(event, eventId), HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/{event_id}"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteEvent(@PathVariable("event_id") String eventId,
            @RequestHeader("Authorization") String accessToken) {
        try {
            database.deleteEvent(eventId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

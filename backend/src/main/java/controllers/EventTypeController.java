package controllers;

import DTO.mappers.EventTypeMapper;
import DTO.models.EventTypeDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import java.util.ArrayList;
import models.event.EventType;
import models.Transportation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.javatuples.Pair;

/**
 * Handles incoming requests related to event types.
 */
@CrossOrigin
@RestController
@RequestMapping("/eventtype")
public class EventTypeController {

    private final Database database;
    private final EventTypeMapper eventTypeMapper;

    /**
     * 
     * @param database Interface to communicate with the database.
     */
    public EventTypeController(Database database) {
        this.database = database;
        this.eventTypeMapper = new EventTypeMapper();
    }

    /**
     * Get all event types or all event types relevant for a certain
     * transportation type.
     *
     * @param transportationType Optional. Used when we want the event types for
     * a certain transportation type.
     * @return A list of event types.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EventTypeDTO>> getEventTypesByTransport(
            @RequestParam(value = "transportationType", required = false) final String transportationType) {
        List<EventTypeDTO> eventTypes = new ArrayList<>();
        try {
            List<Pair<Integer, EventType>> result;
            if (transportationType == null) {
                result = database.getEventtypes();
            } else {
                result = database.getEventtypes(Transportation.fromString(transportationType));
            }
            result.stream().forEach((p) -> {
                eventTypes.add(eventTypeMapper.convertToDTO(p.getValue1()));
            });
            return new ResponseEntity<>(eventTypes, HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

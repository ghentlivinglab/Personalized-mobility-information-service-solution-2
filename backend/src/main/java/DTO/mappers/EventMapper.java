package DTO.mappers;

import DTO.models.CoordinateDTO;
import DTO.models.EventDTO;
import DTO.models.EventTypeDTO;
import DTO.models.JamDTO;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.util.ArrayList;
import java.util.List;
import models.Coordinate;
import models.Transportation;
import models.event.Event;
import models.event.Jam;
import models.event.EventType;

/**
 * The class for the Event Mapper. It maps the Event model on the EventDTO model.
 */
public class EventMapper {

    private final EventTypeMapper eventTypeMapper = new EventTypeMapper();
    private final CoordinateMapper coordinateMapper = new CoordinateMapper();

    /**
     * Method to convert Event into EventDTO
     * @param event The Event to convert
     * @param id The id of the event
     * @return EventDTO object
     */
    public EventDTO convertToDTO(Event event, String id) {

        CoordinateDTO coordinateDTO = coordinateMapper.convertToDTO(event.getCoordinates());
        EventTypeDTO eventTypeDTO = eventTypeMapper.convertToDTO(event.getType());

        List<Jam> jams = event.getAllJams();
        JamDTO[] jamsDTO = new JamDTO[jams.size()];
        for (int i = 0; i < jams.size(); i++) {
            Jam jam = jams.get(i);
            List<Coordinate> line = jam.getLineView();
            CoordinateDTO[] lineDTO = new CoordinateDTO[line.size()];
            for (int j = 0; j < line.size(); j++) {
                lineDTO[j] = coordinateMapper.convertToDTO(line.get(j));
            }
            jamsDTO[i] = new JamDTO(
                    lineDTO,
                    jam.getPublicationString(),
                    jam.getSpeed(), jam.getDelay()
            );
        }

        List<Transportation> transportList = event.getTransportTypes();
        String[] relevantTransportTypes = new String[transportList.size()];
        for (int i = 0; i < transportList.size(); i++) {
            relevantTransportTypes[i] = transportList.get(i).toString();
        }

        EventDTO eventDTO = new EventDTO(
                id,
                coordinateDTO,
                event.isActive(),
                event.getPublicationString(),
                event.getLastEditString(),
                event.getDescription(),
                event.getFormattedAddress(),
                jamsDTO,
                eventTypeDTO,
                relevantTransportTypes
        );

        return eventDTO;
    }


    /**
     * Method to convert EventDTO into Event
     * @param eventDTO EventDTO object to convert into model
     * @return Event (model) object
     */
    public Event convertFromDTO(EventDTO eventDTO) {
        Coordinate coordinate = coordinateMapper.convertFromDTO(eventDTO.getCoordinates());

        ArrayList<Transportation> relTrans = new ArrayList<>();
        for (String relTransDTO1 : eventDTO.getRelevantTransportationTypes()) {
            relTrans.add(Transportation.fromString(relTransDTO1));
        }
        EventType type = new EventType(eventDTO.getType().getType(), relTrans);

        List<Jam> jams = new ArrayList<>();
        for(JamDTO jamDTO: eventDTO.getJams()) {
            List<Coordinate> line = new ArrayList<>();
            for(CoordinateDTO coord: jamDTO.getLine()) {
                line.add(coordinateMapper.convertFromDTO(coord));
            }
            jams.add(new Jam(
                    jamDTO.getPublicationTime(),
                    line,
                    jamDTO.getSpeed(),
                    jamDTO.getDelay()
            ));
        }
        
        Event out = new Event(
                coordinate,
                eventDTO.isActive(),
                eventDTO.getPublicationTime(),
                System.currentTimeMillis(),
                eventDTO.getDescription(),
                eventDTO.getFormattedAddress(),
                type
        );

        jams.forEach(jam -> {
            out.addJam(jam);
        });
        return out;
    }
}

package database_v2.mappers;

import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DatabaseCache;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import database_v2.models.mongo.CoordinateDBModel;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.mongo.JamDBModel;
import database_v2.utils.Rasterizer;
import java.util.ArrayList;
import java.util.List;
import models.Coordinate;
import models.event.Event;
import models.event.EventType;
import models.event.Jam;
import org.apache.log4j.Logger;
import org.javatuples.Pair;

/**
 * This class offers some static functions to do the mapping between a application and db model of
 * an event.
 */
public class EventMapper {

    private final EventtypeMapper eventtypeMapper;

    /**
     * Create a new EventMapper
     */
    public EventMapper() {
        eventtypeMapper = new EventtypeMapper();
    }

    /**
     * ONLY FOR TESTING REASONS, DO NOT USE THIS IN PRODUCTION CODE
     * 
     * @param eventtypeMapper mapper for event types
     */
    public EventMapper(EventtypeMapper eventtypeMapper) {
        this.eventtypeMapper = eventtypeMapper;
    }

    /**
     * Convert a database model to an application model. All dependencies are handled, so the
     * appropriate EventTypes will be filled in.
     *
     * @param dbModel The db model to be converted.
     * @param dac An abstraction of an open connection.
     * @param cache Access to all cached data.
     * @return The converted application model.
     * @throws DataAccessException Something went wrong with the underlying database.
     */
    public Event getAppModel(EventDBModel dbModel, DataAccessContext dac, DatabaseCache cache)
            throws DataAccessException {
        // fist, we get the EventType
        EventType et;
        try {
            et = eventtypeMapper.getEventType(dbModel.getEventTypeId(), dac, cache);
        } catch (RecordNotFoundException ex) {
            Logger.getLogger(getClass()).error("this means that an Event points to an non"
                    + "existing eventType, so database must be badly corrupted", ex);
            throw new DataAccessException(ex);
        }

        // first the event itself
        Event out = new Event(
                dbModel.getUuid(),
                new Coordinate(dbModel.getCoordinates().getLat(), dbModel.getCoordinates().getLon()),
                dbModel.getActive(),
                dbModel.getPublicationTimeMillis(),
                dbModel.getLastEditTimeMillis(),
                dbModel.getDescription(),
                dbModel.getFormattedAddress(),
                et
        );

        // then all his jams
        dbModel.getJams().forEach(dbJam -> {
            List<Coordinate> line = new ArrayList<>();
            dbJam.getLine().forEach(dbCoord -> {
                line.add(new Coordinate(dbCoord.getLat(), dbCoord.getLon()));
            });
            out.addJam(new Jam(
                    dbJam.getUuid(),
                    dbJam.getPublicationTimeMillis(),
                    line,
                    dbJam.getSpeed(),
                    dbJam.getDelay()
            ));
        });

        return out;
    }

    /**
     * Convert an application Event model to db model.
     *
     * @param eventTypeId The id of the EventType of this event
     * @param event The application model to be converted
     * @param rasterizer module needed to calculate grid point of event
     * @return The converted db model.
     */
    public EventDBModel toDBModel(int eventTypeId, Event event, Rasterizer rasterizer) {
        CoordinateDBModel dbCoords = new CoordinateDBModel(
                event.getCoordinates()
        );

        List<JamDBModel> dbJams = new ArrayList<>();
        event.getAllJams().forEach(jam -> {
            List<CoordinateDBModel> jamCoords = new ArrayList<>();
            jam.getLineView().forEach(jamCoord -> {
                jamCoords.add(new CoordinateDBModel(jamCoord));
            });
            dbJams.add(new JamDBModel(
                    jam.getUuid(),
                    jam.getPublicationTimeMillis(),
                    jamCoords,
                    jam.getSpeed(),
                    jam.getDelay()));
        });

        Pair<Integer, Integer> gridpoint
                = rasterizer.coordToRaster(event.getCoordinates());

        return new EventDBModel(
                dbCoords,
                event.isActive(),
                event.getPublicationTimeMillis(),
                event.getLastEditTimeMillis(),
                event.getDescription(),
                event.getFormattedAddress(),
                dbJams,
                eventTypeId,
                event.getUuid(),
                gridpoint.getValue0(),
                gridpoint.getValue1()
        );
    }
}

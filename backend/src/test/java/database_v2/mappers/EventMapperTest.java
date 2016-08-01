package database_v2.mappers;

import database_v2.models.mongo.CoordinateDBModel;
import database_v2.models.mongo.EventDBModel;
import database_v2.models.mongo.JamDBModel;
import database_v2.utils.Rasterizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Coordinate;
import models.Transportation;
import models.event.Event;
import models.event.EventType;
import models.event.Jam;
import org.javatuples.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class EventMapperTest extends AbstractDBMapperTest {

    @Mock
    private final EventtypeMapper eventtypeMapper;

    private final EventMapper eventMapper;

    public EventMapperTest() throws Exception {
        super();
        eventtypeMapper = mock(EventtypeMapper.class);
        eventMapper = new EventMapper(eventtypeMapper);
    }

    @Test
    public void getAppModelTest() throws Exception {
        int eventTypeId = 1;
        String eventId = "EVENT";

        // mock response of eventtype mapper
        EventType et = new EventType("TEST", Arrays.asList(Transportation.CAR));
        when(eventtypeMapper.getEventType(eq(eventTypeId), any(), any())).
                thenReturn(et);

        // create the event db model to be converted
        Coordinate coord = new Coordinate(50.0, 3.5);
        Coordinate jamStart = new Coordinate(50.001, 3.6);
        Coordinate jamEnd = new Coordinate(50.002, 3.61);
        List<JamDBModel> jams = new ArrayList<>();
        jams.add(new JamDBModel(
                "JAM_UUID",
                1000L,
                Arrays.asList(new CoordinateDBModel(jamStart), new CoordinateDBModel(jamEnd)),
                10, // speed
                10 // delay
        ));
        EventDBModel dbModel = new EventDBModel(
                new CoordinateDBModel(coord),
                true, // active
                1000L, // pub time millis
                1000L, // edit time millis
                "DESC",
                "ADDRESS", // formatted address
                jams,
                eventTypeId,
                "EVENT_UUID",
                12, // grid x
                5 // grid y
        );

        Event result = eventMapper.getAppModel(dbModel, dac, cache);
        // do all the checks
        assertEquals(coord, result.getCoordinates());
        assertEquals(1000L, result.getPublicationTimeMillis());
        assertEquals(true, result.isActive());
        assertEquals("DESC", result.getDescription());
        assertEquals(et, result.getType());
        assertEquals("EVENT_UUID", result.getUuid());
        assertTrue(result.getAllJams().size() == 1);
        assertNotNull(result.getJamIfPresent("JAM_UUID"));

    }

    @Test
    public void toDBModelTest() throws Exception {
        int eventTypeId = 1;
        
        // create the event to be converted
        EventType et = new EventType("TEST", Arrays.asList(Transportation.CAR));
        Coordinate coord = new Coordinate(50.0, 3.5);
        Event event = new Event(
                "EVENT_UUID",
                coord, // corrdinate
                true, // active
                1000L, // pub time millis 
                1100L, // edit time millis
                "DESC",
                "ADDRESS",
                et
        );

        Coordinate startJam = new Coordinate(50.1, 3.6);
        Coordinate endJam = new Coordinate(50.2, 3.65);
        event.addJam(new Jam(
                "JAM_UUID",
                1200L, // pub time millis
                Arrays.asList(startJam, endJam), // coordinate line
                10, // speed
                15 // delay
        ));

        // mock rasterizer
        Rasterizer rast = mock(Rasterizer.class);
        when(rast.coordToRaster(any())).thenReturn(new Pair<>(1, 2));
        
        EventDBModel result = eventMapper.toDBModel(eventTypeId, event, rast);
        // do all checks
        assertEquals("EVENT_UUID", result.getUuid());
        assertEquals(true, (boolean) result.getActive());
        assertEquals(1100L, (long) result.getLastEditTimeMillis());
        assertEquals(1000L, (long) result.getPublicationTimeMillis());
        assertTrue(result.getJams().size() == 1);
        assertEquals(1, result.getGridX());
        assertEquals(2, result.getGridY());
        assertEquals(eventTypeId, result.getEventTypeId());
    }
    
    @Test
    public void testDefaultConstructor() {
        EventMapper instance = new EventMapper();
        assertNotNull(instance);
    }
}

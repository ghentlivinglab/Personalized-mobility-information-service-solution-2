package datasources;

import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import java.util.ArrayList;
import java.util.Collection;
import models.event.Event;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class DataFetcherTest {
    @Mock
    private final Database db;
    @Mock
    private final DataSource source;
    
    private final DataFetcher df;
    
    public DataFetcherTest() {
        db = mock(Database.class);
        source = mock(DataSource.class);
        df = new DataFetcher(db, source);
    }
    
    @Before
    public void setUp() {
        
    }
    
    /**
     * Test of fetch method, of class DataFetcher.
     */
    @Test
    public void testFetch() throws Exception {
        // PATH 1
        Event ev = mock(Event.class);
        Collection<Event> events = new ArrayList<>();
        events.add(ev);
        when(source.fetchEvents()).thenReturn(events);
        df.fetch();
        
        // PATH 2
        doThrow(DataAccessException.class).when(db).createEvent(ev);
        df.fetch();
    }

}

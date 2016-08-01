package datasources.waze;

import database_v2.controlLayer.Database;
import models.event.EventType;
import org.javatuples.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class WazeDataSourceTest {
    
    public WazeDataSourceTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of fetchEvents method, of class WazeDataSource.
     */
    @Test
    public void testFetchEvents() throws Exception {
        Database db = mock(Database.class);
        EventType et = mock(EventType.class);
        when(db.getEventtype(any())).thenReturn(new Pair<>(1, et));
        AddressFormatter formatter = mock(AddressFormatter.class);
        WazeDataSource source = new WazeDataSource(db, formatter);
        
        source.fetchEvents();
        
        verify(formatter, atLeastOnce()).getFormattedAddress(any(), any());
        verify(db, atLeastOnce()).getEventtype(any());
    }
    
}

package datasources;

import backend.AppProperties;
import database_v2.controlLayer.Database;
import database_v2.exceptions.DataAccessException;
import models.event.Event;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * This module will handle all sources of events and add new events to the database.
 */
public final class DataFetcher extends Thread {

    private final Database database;
    private final DataSource source;

    /**
     * The constructor for the Data Fetcher.
     *
     * @param source the source of the data.
     * @param database the database class which provides the connection with the database.
     */
    public DataFetcher(Database database, DataSource source) {
        this.database = database;
        this.source = source;
        start();
    }

    /**
     * This method fetches the events from the DataSource. It adds the events to the database.
     *
     */
    public void fetch() {
        Logger.getLogger(getClass()).info("Starting Waze data polling...");
        Collection<Event> events = this.source.fetchEvents();
        for (Event event : events) {
            try {
                database.createEvent(event);
            } catch (DataAccessException ex) {
                Logger.getLogger(getClass()).error(ex);
            }
        }
    }

    @Override
    public void run() {
        // the properties file contains the interval in seconds
        long pollingFreq = AppProperties.instance()
                .getPropAsLong(AppProperties.PROP_KEY.WAZE_POLLING_FEQ);
        pollingFreq *= 1000;
        while (true) {
            try {
                long begin = System.currentTimeMillis();
                fetch();
                long end = System.currentTimeMillis();
                long fetchDuration = end - begin;
                long sleepTime = pollingFreq - fetchDuration;
                // no negative sleep time
                sleepTime = sleepTime < 0 ? 0 : sleepTime;
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                return;
            } catch (Exception ex) {
                Logger.getLogger(getClass()).fatal(ex);
            }
        }
    }

}

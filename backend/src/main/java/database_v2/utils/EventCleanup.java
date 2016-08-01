package database_v2.utils;

import backend.AppProperties;
import backend.AppProperties.PROP_KEY;
import database_v2.DAOLayer.EventDAO;
import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DataAccessProvider;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * This class contains a thread that will clean up old events after they have been inactive for a
 * while.
 */
public class EventCleanup extends Thread {

    private final DataAccessProvider dap;
    private final long sleepInMillis = 86400000L;

    /**
     * create a new instance.
     *
     * @param dap Access to the database.
     */
    public EventCleanup(DataAccessProvider dap) {
        this.dap = dap;
    }

    /**
     * Infinite loop to clean up old events.
     */
    @Override
    public void run() {
        String maxEventAge = AppProperties.instance().getProp(PROP_KEY.EVENT_KEEP_DAYS);
        // events doen't need to be cleaned, so we can simply stop this thread
        if (maxEventAge.toLowerCase().equals("inf")) {
            return;
        }
        int maxEventAgeInDays = Integer.parseInt(maxEventAge);
        long maxEventAgeInMillis = ((long) maxEventAgeInDays) * 24 * 60 * 60 * 1000;
        try {
            while (true) {
                try (DataAccessContext dac = dap.getDataAccessContext()) {
                    EventDAO ed = dac.getEventDAO();
                    long threshold = System.currentTimeMillis() - maxEventAgeInMillis;
                    ed.deleteOldEvents(threshold);
                } catch (SQLException ex) {
                    Logger.getLogger(getClass()).error(ex);
                }
                Thread.sleep(sleepInMillis);
            }
        } catch (InterruptedException ex) {
            return;
        }
    }
}

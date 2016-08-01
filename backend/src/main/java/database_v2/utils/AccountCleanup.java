package database_v2.utils;

import database_v2.controlLayer.DataAccessContext;
import database_v2.controlLayer.DataAccessProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains a thread that will clean up old accounts who have invalidated emails.
 */
public class AccountCleanup extends Thread {

    private final String cleanupStatement
            = "DELETE FROM account WHERE created_at < current_timestamp - interval '24 hours' AND email_validated = false;";

    private final DataAccessProvider dap;
    private final long sleepInMillis = 3600000L;
    private boolean running = true;

    /**
     * Create a new instance.
     * @param dap Access to the database.
     */
    public AccountCleanup(DataAccessProvider dap) {
        this.dap = dap;
    }

    @Override
    public void run() {
        while (running) {
            try (DataAccessContext dac = dap.getDataAccessContext()) {
                Connection conn = dac.getSQLConnection();
                try (PreparedStatement ps = conn.prepareStatement(cleanupStatement)) {
                    int numDeleted = ps.executeUpdate();
                    LogFactory.getLog(getClass()).info("Deleted " + numDeleted
                            + " accounts due to not validating their email");
                }
            } catch (Exception ex) {
                LogFactory.getLog(getClass()).error(ex);
            }
            try {
                sleep(sleepInMillis);
            } catch (InterruptedException ex) {
                running = false;
            }
        }
    }

}

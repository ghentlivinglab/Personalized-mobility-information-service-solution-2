package notifications.senders;

import backend.AppProperties;
import controllers.MailThread;
import database_v2.utils.Translator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import models.event.Event;
import models.users.User;
import notifications.NotificationSender;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;

/**
 *
 */
public class EmailNotificationSender implements NotificationSender {

    private final TaskExecutor executor;
    private final ConcurrentHashMap<String, List<Event>> buffer;
    private final Translator translator;
    private final String emailBody;

    public EmailNotificationSender(TaskExecutor executor) {
        this.executor = executor;
        buffer = new ConcurrentHashMap<>();
        translator = Translator.instance();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/mail_template.html")))) {
            emailBody = IOUtils.toString(in);
        } catch (IOException ex) {
            Logger.getLogger(getClass()).fatal("Could not load email template", ex);
            throw new RuntimeException(ex);
        }
        // sender task
        BufferProcessTask task = new BufferProcessTask();
        task.start();
    }

    private void initEmailBody() {

    }

    @Override
    public void sendNotification(int userId, User user, String eventId, Event event) {
        synchronized (buffer) {
            String email = user.getEmailAsString();
            if (!buffer.containsKey(email)) {
                buffer.put(email, new LinkedList<>());
            }
            buffer.get(email).add(event);
        }
    }

    private void sendMail(String email, List<Event> events) {
        String numNotifString = events.size() == 1
                ? "Er is 1 nieuwe melding. "
                : "Er zijn " + events.size() + " nieuwe meldingen. ";
        String body = emailBody.replace("<!--MELDING-->", numNotifString);
        StringBuilder sb = new StringBuilder();
        events.forEach(ev -> sb.append(eventToMailMessage(ev)));
        body = body.replace("<!--NOTIF_PLACEHOLDER-->", sb.toString());
        MailThread mailSender = new MailThread(email, numNotifString, body, true);
        executor.execute(mailSender);
    }

    private String eventToMailMessage(Event event) {
        StringBuilder sb = new StringBuilder();
        String translation = translator.getTranslation(event.getType().getType());
        sb.append("<tr><td>");
        sb.append(event.getFormattedAddress());
        sb.append("</td><td>");
        sb.append(translation == null ? event.getType().getType() : translation);
        sb.append("</td><td>");
        sb.append(event.getDescription());
        sb.append("</td></tr>\n");
        return sb.toString();
    }

    private class BufferProcessTask extends Thread {

        private final long sleepTimeMillis;

        public BufferProcessTask() {
            int maxMailDelay = Integer.parseInt(
                    AppProperties.instance().getProp(AppProperties.PROP_KEY.MAIL_NOTIF_FREQ));
            sleepTimeMillis = ((long) maxMailDelay) * 60 * 1000;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(sleepTimeMillis);
                } catch (InterruptedException ex) {
                    return;
                }
                Logger.getLogger(getClass()).info("Sending out " + buffer.size() + " new email notifications.");
                synchronized (buffer) {
                    buffer.forEach((k, v) -> sendMail(k, v));
                    buffer.clear();
                }
            }
        }
    }

}

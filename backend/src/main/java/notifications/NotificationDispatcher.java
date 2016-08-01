package notifications;

import java.util.Arrays;
import java.util.List;
import models.event.Event;
import models.users.User;

/**
 *
 */
public class NotificationDispatcher {

    private final List<NotificationSender> senders;

    public NotificationDispatcher(NotificationSender... senders) {
        this.senders = Arrays.asList(senders);
    }

    public void addNotification(int userId, User user, String eventId, Event event) {
        if (!user.isMuted()) {
            senders.forEach(s -> s.sendNotification(userId, user, eventId, event));
        }
    }

}

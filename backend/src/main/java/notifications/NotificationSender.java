package notifications;

import models.event.Event;
import models.users.User;

public interface NotificationSender {
    
    void sendNotification(int userId, User user, String eventId, Event event);
}

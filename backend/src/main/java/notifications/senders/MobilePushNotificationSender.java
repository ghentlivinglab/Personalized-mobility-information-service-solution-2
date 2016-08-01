package notifications.senders;

import backend.AppProperties;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import models.event.Event;
import models.users.User;
import notifications.NotificationSender;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class MobilePushNotificationSender implements NotificationSender{

    @Override
    public void sendNotification(int userId, User user, String eventId, Event event) {
        if (user.getPushToken() != null) {
            String urlstr = "https://gcm-http.googleapis.com/gcm/send";
            String key = "key="+AppProperties.instance().getProp(AppProperties.PROP_KEY.MOBILE_PUSH_NOTIF);

            try {
                URL url = new URL(urlstr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", key);
                urlConnection.connect();

                JSONObject message = new JSONObject();
                message.put("message", "Er zijn nieuwe meldingen beschikbaar!");
                JSONObject notification = new JSONObject();
                notification.put("data", message);
                notification.put("to", user.getPushToken());
                
                String httpBody = notification.toString();

                try (BufferedWriter out =
                             new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()))
                ) {
                    out.write(httpBody);
                }
                
                urlConnection.getResponseCode();

            } catch (IOException ex) {
                Logger.getLogger(getClass()).fatal("Could not send push notification");
            }
        }
    }
    
    
    
}

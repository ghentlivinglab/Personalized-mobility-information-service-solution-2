package vop.groep06.mobiligent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserPostPushToken extends AsyncTask<Void,Void,Void>{

    private String id;
    private String token;

    public UserPostPushToken (String id, String token) {
        this.id = id;
        this.token = token;
    }


    @Override
    protected Void doInBackground(Void... params) {
        String userid = id;
        if (userid != null) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(MainActivity.BASE_URL+"/user/"+userid+"/push_token");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                try (BufferedWriter out =
                             new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()))
                ) {
                    if (token == null) {
                        out.write("DELETED");
                    } else {
                        out.write(token);
                    }
                }

                urlConnection.getResponseCode();

            } catch (IOException ex) {
//                throw new RuntimeException(ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return null;
    }
}

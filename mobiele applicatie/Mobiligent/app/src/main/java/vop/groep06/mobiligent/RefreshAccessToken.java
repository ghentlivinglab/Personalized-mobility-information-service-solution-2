package vop.groep06.mobiligent;


import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import vop.groep06.mobiligent.models.User;

public class RefreshAccessToken extends AsyncTask<Void, Void, Boolean> {

    private final String accessTokenURL = MainActivity.ACCESS_TOKEN_URL;
    private User user;
    private Activity activity;

    public RefreshAccessToken(User user, Activity activity) {
        this.user = user;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        int statusCode;
        JSONObject accessToken;

        try {
            URL url = new URL(accessTokenURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", user.getRefreshToken());
            urlConnection.connect();

            JSONObject refresh_token = new JSONObject();
            refresh_token.put("token", user.getRefreshToken());
            refresh_token.put("user_id", user.getId());

            String httpBody = refresh_token.toString();

            try (BufferedWriter out =
                         new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()))
            ) {
                out.write(httpBody);
            }

            statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return false;
            }
            try (BufferedReader in =
                         new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String responseBody = convertInputStream(in);
                accessToken = new JSONObject(responseBody);
            }
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        try {
            user.setAccessToken(accessToken.getString("token"));
            user.setAccessTokenExp(accessToken.getString("exp"));
        } catch (JSONException ex) {
            throw new RuntimeException();
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (!success) {
            Toast.makeText(activity, "Verkrijgen nieuwe gegevens mislukt, gelieve verbinding te controleren en opnieuw in te loggen!", Toast.LENGTH_LONG).show();
           // ((MainActivity) activity).logout();
        }
    }

    private String convertInputStream(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}

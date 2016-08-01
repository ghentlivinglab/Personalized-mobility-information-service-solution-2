package vop.groep06.mobiligent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.List;

import vop.groep06.mobiligent.models.User;

public class ForgotPassword extends AppCompatActivity {

    private User user;
    private SharedPreferences sharedPreferences;
    private final static String SHAREDPREF_REFRESHTOKEN = "refresh_token";
    private final static String SHAREDPREF_USERID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Uri data = getIntent().getData();
        List<String> params = data.getPathSegments();
        final String email = params.get(0);
        final String password = params.get(1);

        this.user = new User();

        findViewById(R.id.resetPasswordSaveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = ((EditText) findViewById(R.id.resetPass1TextField)).getText().toString();
                String b = ((EditText) findViewById(R.id.resetPass2TextField)).getText().toString();

                if (checkPassword(a,b)) {
                    new UserResetPasswordTask(email, password, a).execute();
                }
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getBoolean(password, false)) {
            startMain();
        }
    }

    public boolean checkPassword(String a, String b) {

        if (!a.equals(b)) {
            ((EditText) findViewById(R.id.resetPass2TextField)).setError("Paswoorden komen niet overeen!");
            return false;
        }

        if (!a.matches(".*\\d+.*") || !a.matches(".*[A-Z]+.*") || !a.matches(".*[a-z]+.*")){
            ((EditText) findViewById(R.id.resetPass2TextField)).setError("Paswoord moet minstens 1 kleine letter, 1 hoofdletter en 1 cijfer bevatten!");
            return false;
        }

        if (a.length()<8) {
            ((EditText) findViewById(R.id.resetPass2TextField)).setError("Paswoord moet minstens 8 characters lang zijn!");
            return false;
        }

        return true;
    }

    private void startMain() {
        Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
        startActivity(intent);
    }

    private class UserResetPasswordTask extends AsyncTask<Void, Void, Boolean> {

        private String email;
        private String oldpassword;
        private String newpassword;
        private final String refreshTokenURL = MainActivity.REFRESH_TOKEN_URL;
        private final String accessTokenURL = MainActivity.ACCESS_TOKEN_URL;

        UserResetPasswordTask(String email, String oldpassword, String newpassword) {
            this.email = email;
            this.oldpassword = oldpassword;
            this.newpassword = newpassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            int statusCode;
            JSONObject refreshToken;
            JSONObject accessToken;
            try {
                URL url = new URL(refreshTokenURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                JSONObject credentials = new JSONObject();
                credentials.put("email", email);
                credentials.put("password", oldpassword);

                String httpBody = credentials.toString();

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
                    refreshToken = new JSONObject(responseBody);
                }

            } catch (IOException | JSONException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            try {
                user.setRefreshToken(refreshToken.getString("token"));
                user.setId(refreshToken.getString("user_id"));
                user.setRole(refreshToken.getString("role"));
                user.setUserURL(refreshToken.getString("user_url"));
            } catch (JSONException ex) {
                throw new RuntimeException();
            }

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


            try {

                URL url = new URL(user.getChangePassUrl());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", user.getAccessToken());
                urlConnection.connect();

                JSONObject request = new JSONObject();

                request.put("old_password", oldpassword);
                request.put("new_password", newpassword);

                String httpBody = request.toString();

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
                    user.setRefreshToken(accessToken.getString("token"));
                }
            } catch (IOException | JSONException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SHAREDPREF_REFRESHTOKEN, user.getRefreshToken());
                editor.putString(SHAREDPREF_USERID, user.getId());
                editor.putBoolean(oldpassword, true);
                editor.commit();
                startMain();
            } else {
                ((EditText) findViewById(R.id.resetPass2TextField)).setError(getString(R.string.error_login_failed));
                ((EditText) findViewById(R.id.resetPass2TextField)).requestFocus();
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
}

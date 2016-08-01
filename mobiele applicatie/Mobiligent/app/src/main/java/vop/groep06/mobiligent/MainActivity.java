package vop.groep06.mobiligent;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import vop.groep06.mobiligent.fragments.EventMapFragment;
import vop.groep06.mobiligent.fragments.LocationListFragment;
import vop.groep06.mobiligent.fragments.LoginFragment;
import vop.groep06.mobiligent.fragments.NoNetworkFragment;
import vop.groep06.mobiligent.fragments.RouteListFragment;
import vop.groep06.mobiligent.fragments.SettingsFragment;
import vop.groep06.mobiligent.models.User;
import vop.groep06.mobiligent.services.MyRegistrationIntentService;
import vop.groep06.mobiligent.services.QuickstartPreferences;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private FragmentManager fragmentManager;

    //fragments
    private LoginFragment loginFragment;
    private EventMapFragment eventMapFragment;
    private EventMapFragment userEventMapFragment;
    private NoNetworkFragment noNetworkFragment;
    private LocationListFragment locationListFragment;
    private RouteListFragment routeListFragment;
    private SettingsFragment settingsFragment;

    private Bundle userBundle;

    private NetworkInfo networkInfo;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private final static String SHAREDPREF_REFRESHTOKEN = "refresh_token";
    private final static String SHAREDPREF_USERID = "user_id";
    public final static String BASE_URL = "https://vopro6.ugent.be/api";
    public final static String ALL_EVENTS_URL = BASE_URL+"/event?recent=true";
    public final static String REFRESH_TOKEN_URL = BASE_URL+"/refresh_token/regular";
    public final static String ACCESS_TOKEN_URL = BASE_URL+"/access_token";
    public final static String FORGOT_PASSWORD_URL = BASE_URL+"/user/forgot_password";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                noKeyBoadOnScreen();
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = new User();

        userBundle = new Bundle();
        userBundle.putSerializable("user", user);

        loginFragment = new LoginFragment();
        loginFragment.setArguments(userBundle);
        eventMapFragment = new EventMapFragment();
        Bundle allEventsBundle = new Bundle();
        allEventsBundle.putString("eventurl", ALL_EVENTS_URL);
        eventMapFragment.setArguments(allEventsBundle);
        noNetworkFragment = new NoNetworkFragment();
        userEventMapFragment = new EventMapFragment();
        locationListFragment = new LocationListFragment();
        routeListFragment = new RouteListFragment();
        settingsFragment = new SettingsFragment();

        fragmentManager = getFragmentManager();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

//        reload();

        //TODO: crashing when no internet is available - FIX
        String userId = sharedPreferences.getString(SHAREDPREF_USERID,null);
        if (userId == null) {
            reload();
        } else {
            String refreshToken = sharedPreferences.getString(SHAREDPREF_REFRESHTOKEN, null);
            user.setId(userId);
            user.setRefreshToken(refreshToken);
            new UserLoginTask(this).execute();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        networkInfo = ((ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        noKeyBoadOnScreen();

        if (networkInfo == null) {
            reload();
        } else if (networkInfo.isConnected()) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (id == R.id.nav_map) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, eventMapFragment)
                        .commit();
                toolbar.setTitle(R.string.app_all_events);

            } else if (id == R.id.nav_login) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, loginFragment)
                        .commit();
                toolbar.setTitle(R.string.app_login);
            } else if (id == R.id.nav_logout) {
                logout();
            } else if (id == R.id.nav_my_events){
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, userEventMapFragment)
                        .commit();
                toolbar.setTitle(R.string.app_my_events);
            } else if (id == R.id.nav_my_locations) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, locationListFragment)
                        .commit();
                toolbar.setTitle(R.string.app_my_locations);
            } else if (id == R.id.nav_my_routes) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, routeListFragment)
                        .commit();
                toolbar.setTitle(R.string.app_my_routes);
            } else if (id == R.id.nav_user_settings) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, settingsFragment)
                        .commit();
                toolbar.setTitle(R.string.app_user_data);
            }
        } else {
            reload();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showNoNetworkAvailableMessage() {
        Toast.makeText(this, "Could not load data, please check your internetconnection and try again.", Toast.LENGTH_SHORT).show();
    }

    public void reload(){
        networkInfo = ((ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        noKeyBoadOnScreen();

        if (networkInfo == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_activity_content_frame, noNetworkFragment)
                    .commit();
            showNoNetworkAvailableMessage();
        } else if (networkInfo.isConnected()) {
            navigationView.setCheckedItem(R.id.nav_map);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_activity_content_frame, eventMapFragment)
                    .commit();
            toolbar.setTitle(R.string.app_all_events);
           // InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           // mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_activity_content_frame, noNetworkFragment)
                    .commit();
            showNoNetworkAvailableMessage();
        }

    }

    public void noKeyBoadOnScreen () {
        InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            if (this.getCurrentFocus() != null) {
                mgr.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public void updateToken () {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHAREDPREF_REFRESHTOKEN, user.getRefreshToken());
        editor.putString(SHAREDPREF_USERID, user.getId());
        editor.commit();
    }

    public void loggedIn() {
        updateToken();
        login();
    }

    private void changeMenuDrawer (int resId) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(resId);
        reload();
    }

    private void login () {
        new GetUserDataTask(this).execute();
        Bundle userEventBundle = new Bundle();
        userEventBundle.putString("eventurl", user.getMyEventsUrl());
        userEventBundle.putString("accesstoken", user.getAccessToken());
        userEventMapFragment.setArguments(userEventBundle);

//        Bundle userBundle = new Bundle();
//        userBundle.putSerializable("user", user);
        locationListFragment.setArguments(userBundle);
        routeListFragment.setArguments(userBundle);
        settingsFragment.setArguments(userBundle);

        // Start IntentService to register this application with GCM.
        Intent intent = new Intent(this, MyRegistrationIntentService.class);
        startService(intent);

        changeMenuDrawer(R.menu.activity_main_logged_in);
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        new UserPostPushToken(user.getId(), null).execute();

        //clean objects
        user = new User();
        userBundle.putSerializable("user", user);
        loginFragment.setArguments(userBundle);

        Toast.makeText(this, "Uitloggen succesvol!", Toast.LENGTH_SHORT).show();
        changeMenuDrawer(R.menu.activity_main_drawer);
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String accessTokenURL = "https://vopro6.ugent.be/api/access_token";
        private Context context;

        public UserLoginTask (Context context) {
            this.context = context;
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

            if (success) {
                login();
            } else {
                Toast.makeText(context, "Login failed. Try again.", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                reload();
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

    private class GetUserDataTask extends AsyncTask<Void, Void, Void> {

        JSONObject response;
        Context context;

        public GetUserDataTask (Context context) {
            super();
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(user.getUserURL());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", user.getAccessToken());

                try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    String responseBody = convertInputStream(in);
                    response = new JSONObject(responseBody);
                }

                urlConnection.getResponseCode();
            } catch ( JSONException | IOException ex) {
              //  Toast.makeText(context, "Could not load data, please try again and check your internetconnection.", Toast.LENGTH_SHORT).show();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            try {
                user.setFirstName(response.getString("first_name"));
                user.setLastName(response.getString("last_name"));
                user.setEmail(response.getString("email"));
            } catch (JSONException ex) {
                throw new RuntimeException();
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

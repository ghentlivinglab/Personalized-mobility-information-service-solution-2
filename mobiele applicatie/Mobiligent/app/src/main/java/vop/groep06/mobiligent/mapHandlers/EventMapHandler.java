package vop.groep06.mobiligent.mapHandlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.models.Coordinate;
import vop.groep06.mobiligent.models.Event;
import vop.groep06.mobiligent.models.Jam;


public class EventMapHandler  implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    GoogleMap map;
    HashMap<Polyline, Marker> jamMarkers;
    Marker lastJamMarker;
    HashMap<Marker, Event> eventHashMap;
    HashMap<String, String> eventTypes;
    String eventUrl;
    String accessToken;
    Activity activity;

    public EventMapHandler(Activity activity, String eventUrl, String accessToken) {
        this.activity = activity;
        this.eventUrl = eventUrl;
        this.accessToken = accessToken;

        jamMarkers = new HashMap<>();
        eventHashMap = new HashMap<>();
        eventTypes = new HashMap<>();
        eventTypes.put("ACCIDENT", "Ongeval");
        eventTypes.put("JAM", "File");
        eventTypes.put("WEATHERHAZARD", "Gevaar door het weer");
        eventTypes.put("HAZARD", "Gevaar op de weg");
        eventTypes.put("MISC", "Ander Type");
        eventTypes.put("CONSTRUCTION", "Wegenwerken");
        eventTypes.put("ROAD_CLOSED", "Weg afgesloten");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setMyLocationEnabled(true);
        try {
            MapsInitializer.initialize(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(51.04, 3.73), 12);
        if (location != null) {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12);
        }

        map.moveCamera(cameraUpdate);
        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                if (lastJamMarker != null) {
                    lastJamMarker.setVisible(false);
                }
                lastJamMarker = jamMarkers.get(polyline);
                lastJamMarker.setVisible(true);
                lastJamMarker.showInfoWindow();
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (lastJamMarker != null) {
                    lastJamMarker.setVisible(false);
                    lastJamMarker = null;
                }
            }
        });
        map.setOnInfoWindowClickListener(this);
        new GetEventsTask(activity).execute();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        makeEventInfo(eventHashMap.get(marker));
    }

    private void makeEventInfo (Event event) {
        final View dialogView = View.inflate(activity, R.layout.event_info, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        ((TextView)dialogView.findViewById(R.id.typeTextField)).setText(event.getType());
        ((TextView)dialogView.findViewById(R.id.descriptionTextField)).setText(event.getDescription());
        //((TextView)dialogView.findViewById(R.id.descriptionTextField)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView)dialogView.findViewById(R.id.addressTextField)).setText(event.getAddress());
        //((TextView)dialogView.findViewById(R.id.addressTextField)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView)dialogView.findViewById(R.id.publicationTextField)).setText(event.getPublicationTimeAsString());
        ((TextView)dialogView.findViewById(R.id.editTextField)).setText(event.getLastEditTimeAsString());

        dialogView.findViewById(R.id.event_info_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private class GetEventsTask extends AsyncTask<Void, Void, Void> {

        // private String urlString = "https://vopro6.ugent.be/api/event?recent=true";
        private JSONArray events;
        private ArrayList<Event> eventArrayList;
        private Activity activity;

        public GetEventsTask (Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(eventUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                if (accessToken != null) {
                    urlConnection.setRequestProperty("Authorization", accessToken);
                }

                try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    String responseBody = convertInputStream(in);
                    events = new JSONArray(responseBody);
                } finally {
                    urlConnection.disconnect();
                }
            } catch ( JSONException | IOException ex) {
                Toast.makeText(activity, "Could not load data, please try again and check your internetconnection.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            try {
                eventArrayList = new ArrayList<>();
                for (int i = 0; i < events.length(); i++) {
                    JSONObject event = (JSONObject) events.get(i);
                    String description = event.getString("description");
                    JSONObject coor = event.getJSONObject("coordinates");
                    Coordinate coordinate = new Coordinate(coor.getDouble("lat"),coor.getDouble("lon"));
                    //get all coordinates if a jam is present
                    JSONArray jams = event.getJSONArray("jams");
                    ArrayList<Jam> jamArrayList = new ArrayList<>();
                    for (int j = 0; j<jams.length(); j++) {
                        JSONObject jam = (JSONObject) jams.get(j);
                        JSONArray line = jam.getJSONArray("line");
                        ArrayList<Coordinate> jamcoor = new ArrayList<>();
                        for (int k = 0; k<line.length(); k++) {
                            JSONObject lineCoor = line.getJSONObject(k);
                            jamcoor.add(new Coordinate(lineCoor.getDouble("lat"), lineCoor.getDouble("lon")));
                        }
                        double speed = jam.getDouble("speed");
                        double delay = jam.getDouble("delay");
                        jamArrayList.add(new Jam(jamcoor, speed, delay));
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    String address = event.getString("formatted_address");
                    Date publication = formatter.parse(event.getString("publication_time"));
                    Date lastEdit = formatter.parse(event.getString("last_edit_time"));
                    String type = eventTypes.get(event.getJSONObject("type").getString("type"));

                    JSONArray transportTypes = event.getJSONArray("relevant_for_transportation_types");
                    ArrayList<String> transportation = new ArrayList<>();
                    for (int j=0; j<transportTypes.length(); j++) {
                        transportation.add(transportTypes.getString(j));
                    }

                    eventArrayList.add(new Event(description, coordinate, jamArrayList, address, publication, lastEdit, type, transportation));
                }
            } catch (JSONException | ParseException e) {
                Toast.makeText(activity, "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
            }

            for (Event event : eventArrayList) {
                if (event.getJams().isEmpty()) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(event.getCoordinate().getLat(), event.getCoordinate().getLon()))
                            .title(event.getType()));
                    eventHashMap.put(marker, event);
                } else {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.RED);
                    for (Jam jam : event.getJams()) {
                        for (Coordinate coor : jam.getLine()) {
                            polylineOptions.add(new LatLng(coor.getLat(), coor.getLon()));
                        }
                    }
                    polylineOptions.clickable(true);
                    Polyline polyline = map.addPolyline(polylineOptions);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(polyline.getPoints().get(polyline.getPoints().size()/2))
                            .title(event.getType())
                            .visible(false));
                    jamMarkers.put(polyline, marker);
                    eventHashMap.put(marker, event);
                }

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

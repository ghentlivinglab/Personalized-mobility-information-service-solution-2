package vop.groep06.mobiligent.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.models.Address;
import vop.groep06.mobiligent.models.Coordinate;
import vop.groep06.mobiligent.models.Route;
import vop.groep06.mobiligent.models.User;

public class RouteListFragment extends Fragment {

    View view;
    User user;
    ArrayAdapter<Route> routeArrayAdapter;
    FragmentManager fragmentManager;

    public RouteListFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.route_list_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.user = (User) bundle.getSerializable("user");
        }

        //android.R.id.text1 must be in constructor to make the super call in getView to work
        this.routeArrayAdapter = new ArrayAdapter<Route>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(routeArrayAdapter.getItem(position).getName());
                text2.setText(routeArrayAdapter.getItem(position).getSubText());
                return view;
            }
        };

        ListView routeListView = (ListView) view.findViewById(R.id.routeListView);
        routeListView.setAdapter(routeArrayAdapter);
        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Route route = routeArrayAdapter.getItem(position);
                RouteFragment routeFragment = new RouteFragment();
                Bundle routeBundle = new Bundle();
                routeBundle.putSerializable("route", route);
                routeBundle.putString("accesstoken", user.getAccessToken());
                routeFragment.setArguments(routeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, routeFragment )
                        .addToBackStack("route")
                        .commit();
            }
        });

        fragmentManager = getActivity().getFragmentManager();

        new GetRoutesTask().execute();

        return view;
    }


    private class GetRoutesTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<Route> travels;
        JSONArray response;

        public GetRoutesTask () {
            super();
            this.travels = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(user.getMyTravelsUrl());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", user.getAccessToken());

                try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    String responseBody = convertInputStream(in);
                    response = new JSONArray(responseBody);
                } finally {
                    urlConnection.disconnect();
                }

            } catch ( JSONException | IOException ex) {
                Toast.makeText(getActivity(), "Could not load data, please try again and check your internetconnection.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            try {
                for (int i=0; i<response.length(); i++) {
                    JSONObject travel = response.getJSONObject(i);
                    String id = travel.getString("id");
                    String name = travel.getString("name");
                    JSONObject startpoint = travel.getJSONObject("startpoint");
                    String startstreet = startpoint.getString("street");
                    String starthousenumber = startpoint.getString("housenumber");
                    String startcity = startpoint.getString("city");
                    String startcountry = startpoint.getString("country");
                    String startpostal = startpoint.getString("postal_code");
                    double startlat = startpoint.getJSONObject("coordinates").getDouble("lat");
                    double startlon = startpoint.getJSONObject("coordinates").getDouble("lon");

                    JSONObject endpoint = travel.getJSONObject("endpoint");
                    String endstreet = endpoint.getString("street");
                    String endhousenumber = endpoint.getString("housenumber");
                    String endcity = endpoint.getString("city");
                    String endcountry = endpoint.getString("country");
                    String endpostal = endpoint.getString("postal_code");
                    double endlat = endpoint.getJSONObject("coordinates").getDouble("lat");
                    double endlon = endpoint.getJSONObject("coordinates").getDouble("lon");

                    travels.add(new Route(id,
                            name,
                            new Address(startstreet, starthousenumber, startcity, startcountry, startpostal, new Coordinate(startlat, startlon)),
                            new Address(endstreet, endhousenumber, endcity, endcountry, endpostal, new Coordinate(endlat, endlon))));
                }
            } catch (JSONException ex) {
                throw new RuntimeException();
            }

            for (Route travel : travels) {
                new GetRouteTask(travel).execute();
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

    private class GetRouteTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<Route> routeArrayList;
        JSONArray response;
        Route travel;


        public GetRouteTask (Route travel) {
            super();
            this.routeArrayList = new ArrayList<>();
            this.travel = travel;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(user.getMyTravelsUrl()+travel.getUrlRoutePart());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", user.getAccessToken());

                try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    String responseBody = convertInputStream(in);
                    response = new JSONArray(responseBody);
                } finally {
                    urlConnection.disconnect();
                }

            } catch ( JSONException | IOException ex) {
                Toast.makeText(getActivity(), "Could not load data, please try again and check your internetconnection.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            try {
                for (int i=0; i<response.length(); i++) {
                    JSONObject route = response.getJSONObject(i);
                    String id = route.getString("id");
                    String transportationtype = route.getString("transportation_type");
                    JSONArray jsonwaypoints = route.getJSONArray("waypoints");
                    ArrayList<Coordinate> waypoints = new ArrayList<>();
                    for (int j=0; j<jsonwaypoints.length(); j++){
                        JSONObject waypoint = jsonwaypoints.getJSONObject(i);
                        waypoints.add(new Coordinate(waypoint.getDouble("lat"), waypoint.getDouble("lon")));
                    }
                    JSONArray jsonfullwaypoints = route.getJSONArray("full_waypoints");
                    ArrayList<Coordinate> fullwaypoints = new ArrayList<>();
                    for (int j=0; j<jsonfullwaypoints.length(); j++){
                        JSONObject fullwaypoint = jsonfullwaypoints.getJSONObject(j);
                        fullwaypoints.add(new Coordinate(fullwaypoint.getDouble("lat"), fullwaypoint.getDouble("lon")));
                    }
                    String url = user.getMyTravelsUrl()+travel.getUrlRoutePart()+"/"+id;
                    routeArrayList.add(new Route(travel.getTravelId(),
                            travel.getName(), travel.getStart(),
                            travel.getEnd(), id, waypoints, fullwaypoints,
                            transportationtype, url));
                }
            } catch (JSONException ex) {
                throw new RuntimeException();
            }

            routeArrayAdapter.addAll(routeArrayList);
            routeArrayAdapter.notifyDataSetChanged();
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

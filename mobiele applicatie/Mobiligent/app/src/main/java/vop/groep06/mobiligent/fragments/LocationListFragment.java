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
import vop.groep06.mobiligent.models.Location;
import vop.groep06.mobiligent.models.User;

public class LocationListFragment extends Fragment{

    View view;
    User user;
    ArrayAdapter<Location> locationArrayAdapter;
    FragmentManager fragmentManager;

    public LocationListFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.location_list_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.user = (User) bundle.getSerializable("user");
        }

        //android.R.id.text1 must be in constructor to make the super call in getView to work
        this.locationArrayAdapter = new ArrayAdapter<Location>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(locationArrayAdapter.getItem(position).getName());
                text2.setText(locationArrayAdapter.getItem(position).getAddress().toString());
                return view;
            }
        };

        ListView locationListView = (ListView) view.findViewById(R.id.locationListView);
        locationListView.setAdapter(locationArrayAdapter);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location location = locationArrayAdapter.getItem(position);
                LocationFragment locationFragment = new LocationFragment();
                Bundle locBundle = new Bundle();
                locBundle.putSerializable("location", location);
                locBundle.putString("accesstoken", user.getAccessToken());
                locationFragment.setArguments(locBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity_content_frame, locationFragment )
                        .addToBackStack("location")
                        .commit();
            }
        });

        fragmentManager = getActivity().getFragmentManager();

        new GetLocationsTask().execute();

        return view;
    }


    private class GetLocationsTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<Location> locationArrayList;
        JSONArray response;

        public GetLocationsTask () {
            super();
            this.locationArrayList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(user.getMyLocationsUrl());
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
                    JSONObject location = response.getJSONObject(i);
                    String id = location.getString("id");
                    String name = location.getString("name");
                    int radius = location.getInt("radius");
                    JSONObject address = location.getJSONObject("address");
                    String street = address.getString("street");
                    String housenumber = address.getString("housenumber");
                    String city = address.getString("city");
                    String country = address.getString("country");
                    String postal = address.getString("postal_code");
                    double lat = address.getJSONObject("coordinates").getDouble("lat");
                    double lon = address.getJSONObject("coordinates").getDouble("lon");
                    locationArrayList.add(
                            new Location(id,
                                    new Address(street, housenumber, city, country, postal, new Coordinate(lat, lon)),
                                    name,
                                    radius,
                                    user.getMyLocationsUrl()+"/"+id));
                }
            } catch (JSONException ex) {
                throw new RuntimeException();
            }

            locationArrayAdapter.addAll(locationArrayList);
            locationArrayAdapter.notifyDataSetChanged();
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

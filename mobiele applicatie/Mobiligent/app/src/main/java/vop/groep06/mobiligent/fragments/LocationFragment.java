package vop.groep06.mobiligent.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;


import vop.groep06.mobiligent.mapHandlers.LocationMapHandler;
import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.models.Location;

public class LocationFragment extends Fragment{

    View view;
    MapView mapView;
    private Location location;
    private String accessToken;


    public LocationFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.location_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.location = (Location) bundle.getSerializable("location");
            this.accessToken = bundle.getString("accesstoken");
        }

        ((TextView) view.findViewById(R.id.nameTextField)).setText(location.getName());
        ((TextView) view.findViewById(R.id.streetHousenumberTextField)).setText(location.getAddress().getStreet()+" "+location.getAddress().getHousenumber());
        ((TextView) view.findViewById(R.id.cityPostalTextField)).setText(location.getAddress().getPostalCode()+" "+location.getAddress().getCity());
        ((TextView) view.findViewById(R.id.countryTextField)).setText(location.getAddress().getCountry());
        ((TextView) view.findViewById(R.id.radiusTextField)).setText(Integer.toString(location.getRadius())+" meter");


        LocationMapHandler eventMapHandler = new LocationMapHandler(getActivity(), location.getEventsUrl(), accessToken, location.getAddress().getCoordinate(), location.getRadius());
        mapView = (MapView) view.findViewById(R.id.locationMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(eventMapHandler);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

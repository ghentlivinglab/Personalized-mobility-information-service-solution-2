package vop.groep06.mobiligent.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;


import java.util.HashMap;

import vop.groep06.mobiligent.mapHandlers.EventMapHandler;
import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.models.Event;


public class EventMapFragment extends Fragment {

    MapView mapView;
    String eventUrl;
    String accessToken;

    public EventMapFragment() {
        super();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.home_page_map, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.eventUrl = bundle.getString("eventurl");
            this.accessToken = bundle.getString("accesstoken", null);
        }

        EventMapHandler eventMapHandler = new EventMapHandler(getActivity(), eventUrl, accessToken);
        mapView = (MapView) view.findViewById(R.id.mapview);
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

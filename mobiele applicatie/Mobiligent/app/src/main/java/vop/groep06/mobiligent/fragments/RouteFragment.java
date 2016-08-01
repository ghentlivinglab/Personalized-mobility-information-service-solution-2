package vop.groep06.mobiligent.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import vop.groep06.mobiligent.mapHandlers.RouteMapHandler;
import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.models.Route;

public class RouteFragment extends Fragment{

    View view;
    MapView mapView;
    private Route route;
    private String accessToken;


    public RouteFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.route_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.route = (Route) bundle.getSerializable("route");
            this.accessToken = bundle.getString("accesstoken");
        }

        ((TextView) view.findViewById(R.id.nameTextField)).setText(route.getName());
        ((TextView) view.findViewById(R.id.streetHousenumberFromTextField)).setText(route.getStart().getStreet()+" "+route.getStart().getHousenumber());
        ((TextView) view.findViewById(R.id.cityPostalFromTextField)).setText(route.getStart().getPostalCode()+" "+route.getStart().getCity());
        ((TextView) view.findViewById(R.id.countryFromTextField)).setText(route.getStart().getCountry());
        ((TextView) view.findViewById(R.id.streetHousenumberToTextField)).setText(route.getEnd().getStreet()+" "+route.getEnd().getHousenumber());
        ((TextView) view.findViewById(R.id.cityPostalToTextField)).setText(route.getEnd().getPostalCode()+" "+route.getEnd().getCity());
        ((TextView) view.findViewById(R.id.countryToTextField)).setText(route.getEnd().getCountry());


        RouteMapHandler eventMapHandler = new RouteMapHandler(getActivity(), route.getEventsUrl(), accessToken, route);
        mapView = (MapView) view.findViewById(R.id.routeMapView);
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

package vop.groep06.mobiligent.mapHandlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.models.Coordinate;
import vop.groep06.mobiligent.models.Route;

public class RouteMapHandler extends EventMapHandler{

    Route route;

    public RouteMapHandler(Activity activity, String eventUrl, String accessToken, Route route) {
        super(activity, eventUrl, accessToken);
        this.route = route;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        double minLat = Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLat = 0;
        double maxLon = 0;
        for (int i = 0; i < route.getFullWaypoints().size(); i++) {
            Coordinate coor = route.getFullWaypoints().get(i);
            LatLng point = new LatLng(coor.getLat(), coor.getLon());
            options.add(point);

            if (coor.getLat()<minLat) {
                minLat = coor.getLat();
            }
            if (coor.getLat()>maxLat) {
                maxLat = coor.getLat();
            }
            if (coor.getLon()<minLon) {
                minLon = coor.getLon();
            }
            if (coor.getLon()>maxLon) {
                maxLon = coor.getLon();
            }
        }

        map.addPolyline(options);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(route.getFullWaypoints().get(0).getLat(), route.getFullWaypoints().get(0).getLon()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pointa)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(route.getFullWaypoints().get(route.getFullWaypoints().size()-1).getLat(), route.getFullWaypoints().get(route.getFullWaypoints().size()-1).getLon()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pointb)));

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(minLat, minLon), new LatLng(maxLat, maxLon)), 200));
    }


}

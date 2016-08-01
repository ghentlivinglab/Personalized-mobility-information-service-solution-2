package vop.groep06.mobiligent.mapHandlers;


import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import vop.groep06.mobiligent.models.Coordinate;

public class LocationMapHandler extends EventMapHandler {

    private Coordinate coordinate;
    private int radius;

    public LocationMapHandler(Activity activity, String eventUrl, String accessToken, Coordinate coordinate, int radius) {
        super(activity, eventUrl, accessToken);
        this.coordinate = coordinate;
        this.radius = radius;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        LatLng center = new LatLng(coordinate.getLat(), coordinate.getLon());
        map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .fillColor(0x0f00a1e6)
                    .strokeColor(0xff00a1e6));

        map.addMarker(new MarkerOptions()
                    .position(center)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(toBounds(center, radius), 50));



    }

    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }
}

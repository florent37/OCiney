package com.bdc.ociney.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LocationUtils implements LocationListener {

    public static String lastLat = null;
    public static String lastLng = null;
    LocationManager locationManager = null;
    Context context;
    LocationUtilsCallBack callBack;

    public LocationUtils(Context context, LocationUtilsCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void chercherLocation() {

        try {
            if (lastLat != null && lastLng != null) {
                callBack.onLocationFound(lastLat, lastLng);
            } else {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                else
                    callBack.onGPSNotFound();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------LOCATION LISTENER---------------------
    @Override
    public void onLocationChanged(Location location) {
        lastLat = "" + location.getLatitude();
        lastLng = "" + location.getLongitude();

        try {
            if (callBack != null)
                callBack.onLocationFound(lastLat, lastLng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public interface LocationUtilsCallBack {
        public void onLocationFound(String lng, String lat);

        public void onGPSNotFound();
    }
}

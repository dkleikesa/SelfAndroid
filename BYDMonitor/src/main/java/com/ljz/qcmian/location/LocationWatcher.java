package com.ljz.qcmian.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.ljz.qcmian.db.DBManager;
import com.ljz.qcmian.utils.LogFactory;


public class LocationWatcher {
    public static final String TAG = "LocationWatcher";

    private static class InstanceHolder {
        private static final LocationWatcher INSTANCE = new LocationWatcher();
    }

    public static LocationWatcher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LogFactory.getLLog().v(TAG, "onLocationChanged:" + location.toString() + " " + this.toString());
            DBManager.getInstance().insertLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void start(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

    }

    public void stop() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}

package com.ljz.qcmian.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationService extends Service {
    public static final String TAG = "LocationService";

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LocationWatcher.getInstance().start(this.getApplicationContext());
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationWatcher.getInstance().stop();
    }
}

package com.colrium.collect.services.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.colrium.collect.utility.AppPreferences;
import com.colrium.collect.utility.AppUtils;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;

    protected Location mCurrentLocation;

    protected long interval;

    protected String actionReceiver;

    protected Boolean gps;

    protected Boolean netWork;

    private AppPreferences appPreferences;

    public static boolean isRunning(Context context) {

        try {
            return AppUtils.isServiceRunning(context, LocationService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appPreferences = AppPreferences.getInstance(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (this.actionReceiver == null) {
            this.actionReceiver = this.appPreferences.getString("ACTION", "LOCATION.ACTION");
        }

        if (this.interval <= 0){
            this.interval = this.appPreferences.getLong("LOCATION_INTERVAL", 10000L);
        }

        if (this.gps == null) {
            this.gps = this.appPreferences.getBoolean("GPS", true);
        }

        if (this.netWork == null) {
            this.netWork = this.appPreferences.getBoolean("NETWORK", false);
        }

        buildGoogleApiClient();

        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(this.interval);
        mLocationRequest.setFastestInterval(this.interval / 2);
        if (this.gps) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        } else if (this.netWork){
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
    }

    protected void startLocationUpdates() {
        try {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (SecurityException ex) {
        }
    }

    private void updateService() {
        if (null != mCurrentLocation) {
            sendLocationBroadcast(this.mCurrentLocation);
            sendCurrentLocationBroadCast(this.mCurrentLocation);
            Log.d("Info: ", "send broadcast location data");
        } else {
            sendPermissionDeinedBroadCast();
            Log.d("Error: ", "Permission deined");
        }
    }

    private void sendLocationBroadcast(Location sbLocationData) {
        Intent locationIntent = new Intent();
        locationIntent.setAction(this.actionReceiver);
        locationIntent.putExtra(LocationServiceConstants.LOCATION_MESSAGE, sbLocationData);
        sendBroadcast(locationIntent);
    }

    private void sendCurrentLocationBroadCast(Location sbLocationData) {
        Intent locationIntent = new Intent();
        locationIntent.setAction(LocationServiceConstants.ACTION_LOCATION_SERVICE_BROADCAST);
        locationIntent.putExtra(LocationServiceConstants.LOCATION_MESSAGE, sbLocationData);
        sendBroadcast(locationIntent);
    }

    private void sendPermissionDeinedBroadCast() {
        Intent locationIntent = new Intent();
        locationIntent.setAction(LocationServiceConstants.ACTION_PERMISSION_DEINED);
        sendBroadcast(locationIntent);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateService();
        }
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateService();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

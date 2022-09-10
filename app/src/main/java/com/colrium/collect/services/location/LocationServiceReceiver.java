package com.colrium.collect.services.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

public class LocationServiceReceiver extends BroadcastReceiver {

    private LocationServiceListener locationServiceListener;

    public LocationServiceReceiver(){
    }

    public LocationServiceReceiver(LocationServiceListener locationServiceListener) {
        this.locationServiceListener = locationServiceListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null != intent && intent.getAction().equals(LocationServiceConstants.ACTION_LOCATION_SERVICE_BROADCAST)) {
            Location locationData = (Location) intent.getParcelableExtra(LocationServiceConstants.LOCATION_MESSAGE);
            locationServiceListener.onCurrentLocation(locationData);
        }

        if (null != intent && intent.getAction().equals(LocationServiceConstants.ACTION_PERMISSION_DEINED)) {
            locationServiceListener.onPermissionDenied();
        }

    }

}

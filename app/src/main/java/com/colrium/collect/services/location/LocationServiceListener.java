package com.colrium.collect.services.location;

import android.location.Location;

public interface LocationServiceListener {
    void onCurrentLocation(Location location);
    void onPermissionDenied();
}

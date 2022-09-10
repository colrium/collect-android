package com.colrium.collect.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class AppUtils {
    private static final String LOG_TAG = AppUtils.class.getSimpleName();
    /**
     * @return true If device has Android Marshmallow or above version
     */
    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isServiceRunning(Context context, Class serviceClass) throws Exception{
        if (context == null) {
            throw new Exception("isServiceRunning context is Null");
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

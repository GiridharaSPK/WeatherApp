package com.rupeek.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

import static com.rupeek.weather.utils.LogUtils.addErrorToLogs;

public class WifiUtils {
    private static final String TAG = WifiUtils.class.getSimpleName();
    private static WifiManager wifiManager;

    public static boolean isAvailable(Context context) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    public static boolean isConnected(Context context) {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            addErrorToLogs(TAG, "Cannot get if device is connected ", e);
            // TODO do whatever error handling you want here
            return false;
        }
        return mobileDataEnabled;
    }
}
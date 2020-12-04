package com.rupeek.weather.utils;

import android.util.Log;

import androidx.databinding.library.BuildConfig;

public class LogUtils {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void addInfoToLogs(String TAG, String message) {
//        if (DEBUG)
            Log.i(TAG.toUpperCase(), message);
    }

    public static void addErrorToLogs(String TAG, String message) {
//        if (DEBUG)
            Log.e(TAG.toUpperCase(), message);
    }

    public static void addErrorToLogs(String TAG, String message, Throwable t) {
//        if (DEBUG)
            Log.e(TAG.toUpperCase(), message, t);
    }
}

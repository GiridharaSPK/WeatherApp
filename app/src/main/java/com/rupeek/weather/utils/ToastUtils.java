package com.rupeek.weather.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private ToastUtils() {
        throw new Error("U will not able to instantiate it");
    }

    /**
     * @param context
     * @param message
     * @return
     */
    public static void showToastShort(Context context, String message) {
        if (!message.isEmpty())
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param message
     * @return
     */
    public static void showToastLong(Context context, String message) {

        if (!message.isEmpty())
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}


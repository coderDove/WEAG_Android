package com.weatheraggregator.app.test.utils;

import android.util.Log;

public class Utils {

    private static final String LOG_TAG = "WeatherTest";
    private static final boolean DEBUG = true;

    public static void writeLogCat(final String text) {
        if (DEBUG)
            Log.i(LOG_TAG, text);
    }

}

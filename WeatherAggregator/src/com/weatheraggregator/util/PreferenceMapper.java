package com.weatheraggregator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceMapper {
    private final static String F_USER_ID = "registration";
    private final static String F_LOCATION = "location";

    private static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putUserId(Context context, String userID) {
        SharedPreferences prefs = getSharedPrefs(context);
        prefs.edit().putString(F_USER_ID, userID).commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        return prefs.getString(F_USER_ID, null);
    }

    public static boolean isUseLocation(Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        return prefs.getBoolean(F_LOCATION, true);
    }

    public static void putIsUseLocation(Context context, boolean isUseLocation) {
        SharedPreferences prefs = getSharedPrefs(context);
        prefs.edit().putBoolean(F_LOCATION, isUseLocation).commit();
    }
}

package com.weatheraggregator.localservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weatheraggregator.util.ForecastInfoManager;

public class ForecastsUpdateReceiver extends BroadcastReceiver {
    private static final String TAG = "ForecastsUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ForecastInfoManager.getInstance().cleareCache();
        ForecastInfoManager.getInstance().loadWeatherInfo();
        Log.d(TAG, "loadWeatherInfo");
    }
}

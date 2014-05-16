package com.weatheraggregator.localservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootReceiver extends BroadcastReceiver {
    // private final String TAG = "OnBootReceiver";
    private final String BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (BOOT.equals(intent.getAction())) {
            Intent serviceLauncher = new Intent(context, WeatherRemoteService.class);
            context.startService(serviceLauncher);
        }
    }
}

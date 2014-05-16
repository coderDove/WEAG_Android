package com.weatheraggregator.localservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.activeandroid.query.Select;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.util.SyncHelper;

public class WifiConnectedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiState) {
            case WifiManager.WIFI_STATE_ENABLED:
                User user = new Select().from(User.class).executeSingle();
                if (user != null) {
                    SyncHelper syncHelper = new SyncHelper();
                    syncHelper.syncAll(context, user.getObjectId());
                }
                break;
            case WifiManager.WIFI_STATE_DISABLED:

                break;
            default:
                break;
        }
    }
}

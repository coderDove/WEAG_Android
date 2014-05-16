package com.weatheraggregator.events;

import java.util.Date;

import android.util.Log;

/**
 * This class need to Initiation events in EventBus
 */
public final class EventUpdateForecast {
    private final String TAG = "EventUpdateForecast";


    public EventUpdateForecast() {
	Log.e(TAG, "Send event: app get new forecast information");
    }

    public EventUpdateForecast(Date date) {
	Log.e(TAG, "Send event: app get new forecast information");
    }

}
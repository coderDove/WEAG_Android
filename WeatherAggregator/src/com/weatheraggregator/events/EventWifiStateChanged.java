package com.weatheraggregator.events;

public class EventWifiStateChanged {
    private final WifiState state;

    public EventWifiStateChanged(WifiState state) {
        this.state = state;
    }

    public WifiState getState() {
        return state;
    }

    public enum WifiState {
        WIFI_STATE_ENABLED, WIFI_STATE_DISABLED;
    }
}

package com.weatheraggregator.events;

public class EventCloseSlidingMenu {
    private final boolean isClose;

    public EventCloseSlidingMenu(boolean isClose) {
        this.isClose = isClose;
    }

    public boolean isClose() {
        return isClose;
    }

}

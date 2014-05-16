package com.weatheraggregator.events;

import com.weatheraggregator.entity.WeatherService;

public class EventServiceEdit {
    private EditType type;
    private WeatherService service;

    public EventServiceEdit(EditType type, WeatherService service) {
        this.type = type;
        this.service = service;
    }

    public WeatherService getService() {
        return service;
    }

    public void setService(WeatherService service) {
        this.service = service;
    }

    public EditType getType() {
        return type;
    }

    public void setType(EditType type) {
        this.type = type;
    }

    public static enum EditType {
        FAVOURITE, USE
    }
}

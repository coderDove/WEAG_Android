package com.weatheraggregator.events;

import com.weatheraggregator.entity.WeatherService;

public class EventChangeWebService {

    private WeatherService mWeatherService;
    private int position;

    public EventChangeWebService(final WeatherService service, final int position) {
        mWeatherService = service;
        this.position = position;
    }

    public WeatherService getWeatherService() {
        return mWeatherService;
    }

    public int getPosition() {
        return position;
    }

}

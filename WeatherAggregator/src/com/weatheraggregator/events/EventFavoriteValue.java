package com.weatheraggregator.events;

import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.WeatherService;

public class EventFavoriteValue {
    private FavoriteType type;
    private City city;
    private WeatherService service;


    public EventFavoriteValue(FavoriteType type, City city) {
        this.type = type;
        this.city = city;
    }

    public EventFavoriteValue(FavoriteType type, WeatherService service) {
        this.type = type;
        this.service = service;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public WeatherService getService() {
        return service;
    }

    public void setService(WeatherService service) {
        this.service = service;
    }

    public FavoriteType getType() {
        return type;
    }

    public void setType(FavoriteType type) {
        this.type = type;
    }

    public static enum FavoriteType {
        CITY, SERVICE
    }
}

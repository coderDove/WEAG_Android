package com.weatheraggregator.events;

import java.util.Date;

import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.WeatherService;

public class EventChangedCity {
    private City city;
    private Date date;
    private WeatherService service;
    private int position;

    public EventChangedCity(City currentCity, Date currentDate, WeatherService currentService, int position) {
        this.city = currentCity;
        this.date = currentDate;
        this.service = currentService;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public WeatherService getService() {
        return service;
    }

    public void setService(WeatherService service) {
        this.service = service;
    }
}

package com.weatheraggregator.entity;

import java.util.List;

public class WeatherList {

    private String ServiceId;
    private String CityId;
    private List<Forecast> Forecast;

    public String getServiceId() {
	return ServiceId;
    }

    public void setServiceId(String serviceId) {
	ServiceId = serviceId;
    }

    public String getCityId() {
	return CityId;
    }

    public void setCityId(String cityId) {
	CityId = cityId;
    }

    public List<Forecast> getForecast() {
	return Forecast;
    }

    public void setForecast(List<Forecast> forecast) {
	Forecast = forecast;
    }

}

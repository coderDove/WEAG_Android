package com.weatheraggregator.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.weatheraggregator.model.ForecastResponse;

public class LocationCityForecasts {

    private static final String F_CURRENT_CITY = "CurrentCity";
    private static final String F_WEATHER_LIST = "WeatherList";

    @SerializedName(F_CURRENT_CITY)
    private City currentCity;
    @SerializedName(F_WEATHER_LIST)
    private List<ForecastResponse> weatherList;

    public City getCurrentCity() {
	return currentCity;
    }

    public void setCurrentCity(City currentCity) {
	this.currentCity = currentCity;
    }

    public List<ForecastResponse> getWeatherList() {
	return weatherList;
    }

    public void setWeatherList(List<ForecastResponse> weatherList) {
	this.weatherList = weatherList;
    }

}

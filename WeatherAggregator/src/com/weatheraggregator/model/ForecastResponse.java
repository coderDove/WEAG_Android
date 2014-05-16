package com.weatheraggregator.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.weatheraggregator.entity.Forecast;

public class ForecastResponse {
    private String ServiceId;
    private String CityId;
    public static final String P_FORECASTS = "Forecast";

    @SerializedName(P_FORECASTS)
    List<Forecast> forecasts;

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

    public List<Forecast> getForecasts() {
	return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
	this.forecasts = forecasts;
    }
}

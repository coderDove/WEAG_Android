package com.weatheraggregator.model;

import java.util.Date;

import com.weatheraggregator.entity.Forecast;

public class CalendarWeatherItem {
    private Date date;
    private int cloudy;
    private String temp;

    public CalendarWeatherItem(Forecast forecast) {
        if (forecast != null) {
            this.date = forecast.getForecastDay();
            this.cloudy = forecast.getCloudy();
        }
    }

    public CalendarWeatherItem() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCloudy() {
        return cloudy;
    }

    public void setCloudy(int cloudy) {
        this.cloudy = cloudy;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}

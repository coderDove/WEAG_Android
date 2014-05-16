package com.weatheraggregator.model;

import java.util.Date;

import android.graphics.Bitmap;

public class ForecastInfo {

    //Not used
    private final int temp;
    private final Bitmap weatherBitmap;
    private final Date forecastDate;

    public ForecastInfo(int temp, Bitmap weatherBitmap, Date date, int condition, long id) {
        this.temp = temp;
        this.forecastDate = date;
        this.weatherBitmap = weatherBitmap;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    public int getTemp() {
        return temp;
    }

    public Bitmap getWeatherBitmap() {
        return weatherBitmap;
    }
}

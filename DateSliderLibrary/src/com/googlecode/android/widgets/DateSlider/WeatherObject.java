package com.googlecode.android.widgets.DateSlider;

import android.graphics.Bitmap;

public class WeatherObject {
    public Bitmap imgWeather;
    public Integer temp;

    public WeatherObject(Bitmap img, Integer temp) {
        this.temp = temp;
    }

    public WeatherObject() {

    }
}

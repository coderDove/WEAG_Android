package com.googlecode.android.widgets.DateSlider.timeview;

import java.util.Date;

import android.widget.ImageView;

import com.googlecode.android.widgets.DateSlider.WeatherObject;

public interface IRenderWeatherCalenderView {
    public WeatherObject renderCalendarItem(Date date, ImageView ivCloudnes);
}

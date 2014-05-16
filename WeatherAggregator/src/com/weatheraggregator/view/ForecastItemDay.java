package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.forecast_weather_day_item)
public class ForecastItemDay extends RelativeLayout {
    @ViewById
    protected TextView tvDay;

    @ViewById
    protected TextView tvTemp;

    @ViewById
    protected ImageView imgCloudy;

    public ForecastItemDay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDay(String name) {
        tvDay.setText(name);
    }

    public void setTemp(String temp) {
        tvTemp.setText(temp);
    }

    public void setCloudyFromRes(int resId) {
        imgCloudy.setImageResource(resId);
    }
}

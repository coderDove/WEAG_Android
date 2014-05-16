package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.user_forecast_item)
public class UserForecastItem extends WeatherBaseItem {
    @ViewById
    protected TextView tvHumidityValue, tvWindValue;

    public UserForecastItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHumidity(String humidity) {
        tvHumidityValue.setText(humidity);
    }

    public void setWind(String wind) {
        tvWindValue.setText(wind);
    }
}

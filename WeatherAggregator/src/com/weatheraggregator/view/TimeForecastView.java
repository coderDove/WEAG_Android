package com.weatheraggregator.view;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.time_weather_item)
public class TimeForecastView extends LinearLayout {
    @ViewById(R.id.tvDate)
    protected TextView tvTime;
    @ViewById(R.id.ivWeather)
    protected ImageView ivCloudiness;
    @ViewById(R.id.tvTemp)
    protected TextView tvTemp;

    public TimeForecastView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public TextView getTimeView() {
	return tvTime;
    }

    public TextView getTempView() {
	return tvTemp;
    }

    public ImageView getCloudinessView() {
	return ivCloudiness;
    }

    public void defaultState() {
	tvTime.setTextColor(Color.WHITE);
	tvTemp.setTextColor(Color.WHITE);
    }

    public void selectState() {
	tvTime.setTextColor(Color.BLACK);
	tvTemp.setTextColor(Color.BLACK);
    }

}

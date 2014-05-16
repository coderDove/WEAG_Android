package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.Util;

@EViewGroup(R.layout.detail_weather_top_view)
public class DetailWeatherTopView extends LinearLayout {
    @ViewById(R.id.tvTemp)
    protected TextView tvTemp;
    @ViewById(R.id.tvTempMaxValue)
    protected TextView tvTempMaxValue;
    @ViewById(R.id.tvTempMinValue)
    protected TextView tvTempMinValue;
    @ViewById(R.id.tvWetValue)
    protected TextView tvWetValue;
    @ViewById(R.id.tvWindValue)
    protected TextView tvWindValue;
    @ViewById(R.id.ivCloud)
    protected ImageView ivCloud;
    @ViewById(R.id.ivTempMax)
    protected ImageView ivTempMax;
    @ViewById(R.id.ivTempMin)
    protected ImageView ivTempMin;
    @ViewById(R.id.ivWet)
    protected ImageView ivWet;
    @ViewById(R.id.ivWind)
    protected ImageView ivWind;

    public DetailWeatherTopView(Context context) {
	super(context);
    }

    public DetailWeatherTopView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public void init(Forecast forecast) {
	if (forecast != null) {
	    final int currentTemp = MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getTemp());
	    tvTemp.setText(String.valueOf(currentTemp));
	    final int maxTemp = MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getMaxTemp());
	    tvTempMaxValue.setText(String.valueOf(maxTemp));
	    final int minTemp = MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getMinTemp());
	    tvTempMinValue.setText(String.valueOf(minTemp));
	    if (forecast.getHumidity() != null) {
		tvWetValue.setText(String.valueOf(forecast.getHumidity()));
	    } else {
		tvWetValue.setText(R.string.na);
	    }
	    tvWindValue.setText(MeasureUnitHelper.getSingleton()
		    .convertWindSpeed(forecast.getWindSpeed(), getContext()));
	    ivCloud.setImageBitmap(Util.getForecastImage(forecast
		    .getCondition()));
	} else {
	    final String na = (String) getContext().getText(R.string.na);
	    tvTemp.setText(na);
	    tvTempMaxValue.setText(na);
	    tvTempMinValue.setText(na);
	    tvWetValue.setText(na);
	    tvWindValue.setText(na);
	    ivCloud.setImageResource(R.drawable.cloudiness_45);
	}
    }

    @AfterViews
    public void initView() {

    }

}

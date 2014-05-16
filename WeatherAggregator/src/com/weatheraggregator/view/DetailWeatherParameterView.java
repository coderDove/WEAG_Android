package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.util.MeasureUnitHelper;

@EViewGroup(R.layout.detail_weather_top_view)
public class DetailWeatherParameterView extends LinearLayout {

    @ViewById(R.id.tvMinTemp)
    protected TextView tvMinTemp;
    @ViewById(R.id.tvMaxTemp)
    protected TextView tvMaxTempDegree;

    @ViewById(R.id.tvPrecipitation)
    protected TextView tvPrecipitation;

    @ViewById(R.id.tvWindDirection)
    protected TextView tvWindDirection;

    @ViewById(R.id.tvWindSpeed)
    protected TextView tvWindSpeed;

    @ViewById(R.id.tvMaxWind)
    protected TextView tvMaxWind;

    @ViewById(R.id.tvMinWind)
    protected TextView tvMinWind;

    @ViewById(R.id.tvPressure)
    protected TextView tvPressure;

    @ViewById(R.id.tvMaxPressure)
    protected TextView tvMaxPressure;

    @ViewById(R.id.tvMinPressure)
    protected TextView tvMinPressure;
    @ViewById(R.id.tvHumidity)
    protected TextView tvHumidity;

    public DetailWeatherParameterView(Context context) {
	super(context);
    }

    public DetailWeatherParameterView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public void init(Forecast forecast) {
	if (forecast != null) {
	    tvMaxTempDegree.setText(MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getMaxTemp(), getContext()));
	    tvMinTemp.setText(MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getMinTemp(), getContext()));
	    tvPrecipitation.setText(MeasureUnitHelper.getSingleton()
		    .convertPrecipitation(forecast.getPrecipitation(),
			    getContext()));
	    tvHumidity.setText(MeasureUnitHelper.getSingleton()
		    .convertHumidity(forecast.getHumidity(), getContext()));
	   // tvWindDirection.setText(forecast.getWindDirection());
	    tvWindSpeed.setText(MeasureUnitHelper.getSingleton()
		    .convertWindSpeed(forecast.getWindSpeed(), getContext()));
	    tvMaxWind.setText(MeasureUnitHelper.getSingleton()
		    .convertWindSpeed(forecast.getMaxWindSeep(), getContext()));
	    tvMinWind
		    .setText(MeasureUnitHelper.getSingleton().convertWindSpeed(
			    forecast.getMinWindSpeed(), getContext()));
	    tvPressure.setText(MeasureUnitHelper.getSingleton()
		    .convertPressure(forecast.getPressure(), getContext()));
	    tvMaxPressure.setText(MeasureUnitHelper.getSingleton()
		    .convertPressure(forecast.getMaxPressure(), getContext()));
	    tvMinPressure.setText(MeasureUnitHelper.getSingleton()
		    .convertPressure(forecast.getMinPressure(), getContext()));
	} else {
	    final String na = (String) getContext().getText(R.string.na);
	    tvMaxTempDegree.setText(na);
	    tvMinTemp.setText(na);
	    tvPrecipitation.setText(na);
	    tvHumidity.setText(na);
	    tvWindDirection.setText(na);
	    tvWindSpeed.setText(na);
	    tvMaxWind.setText(na);
	    tvMinWind.setText(na);
	    tvPressure.setText(na);
	    tvMaxPressure.setText(na);
	    tvMinPressure.setText(na);
	}
    }
}

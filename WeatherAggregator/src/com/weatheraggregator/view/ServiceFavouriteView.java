package com.weatheraggregator.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.events.EventVoteWeatherService;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.ServiceVoteHelper;
import com.weatheraggregator.util.Util;

import de.greenrobot.event.EventBus;

@EViewGroup(R.layout.service_favourite)
public class ServiceFavouriteView extends LinearLayout {

    @ViewById(R.id.tvServiceName)
    protected TextView tvServiceName;
    @ViewById(R.id.tvTempCurrent)
    protected TextView tvTempCurrent;
    @ViewById(R.id.tvTempMaxValue)
    protected TextView tvTempMaxValue;
    @ViewById(R.id.tvWetValue)
    protected TextView tvWetValue;
    @ViewById(R.id.tvTempMinValue)
    protected TextView tvTempMinValue;
    @ViewById(R.id.tvWindValue)
    protected TextView tvWindValue;
    @ViewById(R.id.tvAgree)
    protected TextView tvAgree;
    @ViewById(R.id.tvDisagree)
    protected TextView tvDisagree;

    @ViewById(R.id.ivCloud)
    protected ImageView ivCloud;
    @ViewById(R.id.ivTempMax)
    protected ImageView ivTempMax;
    @ViewById(R.id.ivWet)
    protected ImageView ivWet;
    @ViewById(R.id.ivTempMin)
    protected ImageView ivTempMin;
    @ViewById(R.id.ivWind)
    protected ImageView ivWind;
    @ViewById(R.id.btnAgree)
    protected ImageButton btnAgree;
    @ViewById(R.id.btnDisAgree)
    protected ImageButton btnDisAgree;

    @ViewById(R.id.tvDate)
    protected TextView tvDate;
    private SimpleDateFormat mSdf = new SimpleDateFormat(
	    Util.DATE_FORMAT_MMMM_YYYY, Locale.getDefault());

    private WeatherService mService;
    private City mCity;

    public ServiceFavouriteView(Context context) {
	super(context);
    }

    public ServiceFavouriteView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public void setDate(Date selectDate) {
	if (selectDate != null) {
	    tvDate.setText(mSdf.format(selectDate));
	} else {
	    tvDate.setText(getContext().getString(R.string.na));
	}
    }

    private void initTemperature(Forecast forecast) {
	final String na = (String) getContext().getString(R.string.na);
	if (forecast.getTemp() != null) {
	    tvTempCurrent.setText(MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getTemp(), getContext()));
	} else {
	    tvTempCurrent.setText(na);
	}
	final Integer maxTemp = MeasureUnitHelper.getSingleton()
		.convertTemperature(forecast.getMaxTemp());
	if (maxTemp != null) {
	    tvTempMaxValue.setText(MeasureUnitHelper.getSingleton()
		    .convertTemperature(maxTemp, getContext()));
	} else {
	    tvTempMaxValue.setText(na);
	}
	final Integer minTemp = MeasureUnitHelper.getSingleton()
		.convertTemperature(forecast.getMinTemp());
	if (minTemp != null) {
	    tvTempMinValue.setText(MeasureUnitHelper.getSingleton()
		    .convertTemperature(minTemp, getContext()));
	} else {
	    tvTempMinValue.setText(na);
	}
    }

    private void initRating(WeatherService weatherService, Date dateForecast,
	    City city) {
	if (weatherService != null) {
	    tvAgree.setText(weatherService.getVotedFor().toString());
	    tvDisagree.setText(weatherService.getVotedAgainst().toString());
	    setRatingEnabled(ServiceVoteHelper.needEnableRating(city,
		    weatherService, dateForecast));
	} else {
	    setRatingEnabled(false);
	}
    }

    public void initView(Forecast forecast, WeatherService service, Date date,
	    City city) {
	if (forecast != null) {

	    final String na = (String) getContext().getString(R.string.na);

	    initTemperature(forecast);

	    Double humidity = forecast.getHumidity();
	    if (humidity != null) {
		tvWetValue.setText(MeasureUnitHelper.getSingleton()
			.convertPressure(humidity, getContext()));
	    } else {
		tvWetValue.setText(na);
	    }
	    tvWindValue.setText(MeasureUnitHelper.getSingleton()
		    .convertWindSpeed(forecast.getWindSpeed(), getContext()));
	    ivCloud.setImageBitmap(Util.getForecastImage(forecast
		    .getCondition()));
	    if (forecast.getService() != null) {
		tvServiceName.setText(forecast.getService().getName());
	    }
	} else {
	    initEmptyView();
	}
	initRating(service, date, city);
	mCity = city;
	mService = service;
    }

    private void initEmptyView() {
	final String na = (String) getContext().getText(R.string.na);
	tvServiceName.setText(na);
	tvTempCurrent.setText(na);
	tvTempMaxValue.setText(na);
	tvTempMinValue.setText(na);
	tvWetValue.setText(na);
	tvWindValue.setText(na);
	tvAgree.setText(na);
	tvDisagree.setText(na);
	ivCloud.setImageResource(R.drawable.cloudiness_45);
	setRatingEnabled(false);
    }

    @AfterViews
    public void initView() {

    }

    public void setServiceName(final String name) {
	if (name != null) {
	    tvServiceName.setText(name);
	}
    }

    @UiThread
    public void setRatingEnabled(boolean enabled) {
	btnAgree.setEnabled(enabled);
	btnDisAgree.setEnabled(enabled);
    }

    // Favorite view set rating
    @Click({ R.id.btnAgree, R.id.btnDisAgree })
    protected void onFavoriteRatingClick(View v) {
	boolean isVote = false;
	switch (v.getId()) {
	case R.id.btnAgree:
	    isVote = true;
	    break;
	case R.id.btnDisAgree:
	    isVote = false;
	    break;
	default:
	    break;
	}

	EventBus.getDefault().post(
		new EventVoteWeatherService(isVote, mService.getObjectId(),
			mCity.getObjectId()));
	setRatingEnabled(false);
    }

}

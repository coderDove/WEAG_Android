package com.weatheraggregator.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.events.EventVoteWeatherService;
import com.weatheraggregator.util.Util;

import de.greenrobot.event.EventBus;

@EViewGroup(R.layout.service_weather_item)
public class ServiceWeatherItem extends WeatherBaseItem {
    @ViewById(R.id.imgIcon)
    protected ImageView ivLogo;
    @ViewById(R.id.ivWet)
    protected ImageView ivWet;
    @ViewById(R.id.ivMinTemp)
    protected ImageView ivMinTemp;
    @ViewById(R.id.ivMaxTemp)
    protected ImageView ivMaxTemp;
    @ViewById(R.id.ivWind)
    protected ImageView ivWind;
    @ViewById(R.id.tvServiceName)
    protected TextView tvServiceName;
    @ViewById(R.id.tvVoteAgain)
    protected TextView tvVoteAgain;
    @ViewById(R.id.tvVoteFor)
    protected TextView tvVoteFor;

    @ViewById(R.id.tvWetValue)
    protected TextView tvWetValue;
    @ViewById(R.id.tvMinTempValue)
    protected TextView tvMinTempValue;
    @ViewById(R.id.tvMaxTempValue)
    protected TextView tvMaxTempValue;
    @ViewById(R.id.tvWindValue)
    protected TextView tvWindValue;
    @ViewById(R.id.btnAgree)
    protected ImageButton btnAgree;
    @ViewById(R.id.btnDisagree)
    protected ImageButton btnDisagree;

    public ServiceWeatherItem(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public void setLogo(int logoId) {
	ivLogo.setImageResource(logoId);
    }

    public void setLogo(Bitmap logo) {
	ivLogo.setImageBitmap(logo);
    }

    public ImageView getLogoView() {
	return ivLogo;
    }

    public void setServiceName(String serviceName) {
	this.tvServiceName.setText(serviceName);
    }

    public void setWetValue(String wetValue) {
	this.tvWetValue.setText(wetValue);
    }

    public void setMinTempValue(String minTempValue) {
	this.tvMinTempValue.setText(minTempValue);
    }

    public void setMaxTempValue(String maxTempValue) {
	this.tvMaxTempValue.setText(maxTempValue);
    }

    public void setWindValue(final String windValue) {
	this.tvWindValue.setText(String.valueOf(windValue));
    }

    public void setWindDirectionImage(final Integer direction) {
	ivWind.setImageResource(Util.getWindDirectionImageCode(direction));
    }

    @AfterViews
    protected void initView() {

    }

    public void initNullData() {
	final String na = (String) getContext().getText(R.string.na);
	setTempValue(na);
	setWetValue(na);
	setMaxTempValue(na);
	setMinTempValue(na);
	ivWind.setImageBitmap(null);
	tvVoteAgain.setText(na);
	tvVoteFor.setText(na);
	setWindValue(na);
	setEnableRatingButtons(false);
	this.setTag(null);
    }

    public void setVoteAgainst(Integer vote) {
	if (vote != null) {
	    tvVoteAgain.setText(vote.toString());
	} else {
	    tvVoteAgain.setText((String) getContext().getText(R.string.na));
	}
    }

    public void setVoteFor(Integer vote) {
	if (vote != null) {
	    tvVoteFor.setText(vote.toString());
	} else {
	    tvVoteFor.setText((String) getContext().getText(R.string.na));
	}
    }

    @Click({ R.id.btnAgree, R.id.btnDisagree })
    protected void onVoteClick(View view) {
	boolean isVote = false;
	switch (view.getId()) {
	case R.id.btnAgree:
	    isVote = true;
	    break;
	case R.id.btnDisagree:
	    isVote = false;
	    break;
	default:
	    break;
	}
	if (this.getTag() != null) {
	    Forecast forecast = (Forecast) this.getTag();
	    EventBus.getDefault().post(
		    new EventVoteWeatherService(isVote,
			    forecast.getServiceId(), forecast.getCityId()));
	    setEnableRatingButtons(false);
	}
    }

    public void setEnableRatingButtons(boolean enabled) {
	btnAgree.setEnabled(enabled);
	btnDisagree.setEnabled(enabled);
    }
}

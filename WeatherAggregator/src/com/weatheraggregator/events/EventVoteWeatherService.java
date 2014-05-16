package com.weatheraggregator.events;

import android.view.View;

public class EventVoteWeatherService {
    private final boolean isVote;
    private final String serviceId;
    private final String cityId;


    public EventVoteWeatherService(boolean isVote, String serviceId,
	    String cityId) {
	this.isVote = isVote;
	this.serviceId = serviceId;
	this.cityId = cityId;
    }

    public boolean isVote() {
	return isVote;
    }

    public String getServiceId() {
	return serviceId;
    }

    public String getCityId() {
	return cityId;
    }

}

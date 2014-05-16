package com.weatheraggregator.model;

import com.google.gson.annotations.SerializedName;

public class UserForecastAllService {

    @SerializedName("CityId")
    private String mCityId;

    @SerializedName("Revision")
    private int mRevision;

    public UserForecastAllService() {

    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        this.mCityId = cityId;
    }

    public int getRevision() {
        return mRevision;
    }

    public void setRevision(int revision) {
        this.mRevision = revision;
    }

}

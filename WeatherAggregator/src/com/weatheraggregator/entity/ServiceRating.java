package com.weatheraggregator.entity;

import java.util.Date;

import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "ServiceRating", id = BaseColumns._ID)
public class ServiceRating extends BaseModel {
    public final static String F_SERVICE_ID = "service_id";
    public final static String F_DATE = "date";
    public final static String F_CITY_ID = "city_id";
    public final static String F_VOTE = "vote";
    public final static String F_SYNC = "is_sync";

    @Column(name = F_OBJECT_ID)
    @SerializedName("Id")
    protected String objectId;

    @Column(name = F_SERVICE_ID)
    private String serviceId;

    @Column(name = F_DATE)
    private Date date;

    @Column(name = F_CITY_ID)
    private String cityId;

    @Column(name = F_VOTE)
    private boolean vote;

    @Column(name = F_SYNC)
    private boolean isSync;

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

}

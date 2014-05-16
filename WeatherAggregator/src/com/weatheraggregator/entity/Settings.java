package com.weatheraggregator.entity;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Settings", id = BaseColumns._ID)
public class Settings extends Model {
    public final static String F_VALUE = "value";
    public final static String F_OBJECT_ID = "_object_id";
    public final static String F_NAME = "name";

    @Column(name = F_NAME)
    protected String name;

    @Column(name = F_OBJECT_ID)
    protected String objectId;

    @Column(name = F_VALUE)
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

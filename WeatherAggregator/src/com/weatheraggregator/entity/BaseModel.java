package com.weatheraggregator.entity;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

//Don't create table from this class 
//You need extend your model from this class
public abstract class BaseModel extends Model {
    public final static String F_OBJECT_ID = "_object_id";
    public final static String P_OBJECT_ID = "Id";
    public abstract String getObjectId();

    public abstract void setObjectId(String objectId);

    public Long getLocalObjectIdByServiceObjectId() {
        Model item = null;
        if (getObjectId() != null) {
            item = new Select(BaseColumns._ID).from(this.getClass()).where(String.format(" %s = ?", F_OBJECT_ID), getObjectId()).executeSingle();
        }
        if (item != null) {
            return item.getId();
        } else {
            return null;
        }
    }
}

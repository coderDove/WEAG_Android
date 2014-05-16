package com.weatheraggregator.entity;

import java.util.List;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;
import com.weatheraggregator.model.MeasureUnit;

@Table(name = "User", id = BaseColumns._ID)
public class User extends Model {
    public final static String F_TEMP_UNIT = "temp_unit";
    public final static String F_PRECIPITATION_UNIT = "precipitation_unit";
    public final static String F_WIND_SPEED_UNIT = "wind_speed_unit";
    public final static String F_PRESSURE_UNIT = "pressure_unit";
    public final static String F_OBJECT_ID = "_object_id";
    public final static String F_NAME = "name";
    public final static String F_IS_SYNC = "is_sync";

    @Column(name = F_IS_SYNC)
    protected boolean isSync;

    @Column(name = F_NAME)
    @SerializedName("Name")
    protected String name;

    @Column(name = F_OBJECT_ID)
    @SerializedName("Id")
    protected String objectId;

    @Column(name = F_PRESSURE_UNIT)
    @SerializedName("PressureUnit")
    private int pressureUnit;

    @Column(name = F_WIND_SPEED_UNIT)
    @SerializedName("WindSpeedUnit")
    private int speedUnit;

    @Column(name = F_TEMP_UNIT)
    @SerializedName("TempUnit")
    private int temperatureUnit;

    @Column(name = F_PRECIPITATION_UNIT)
    @SerializedName("PresipitationUnit")
    private int precipitationUnit;

    public User() {
    }

    public MeasureUnit getPressureUnit() {
        return MeasureUnit.values()[pressureUnit];
    }

    public void setPressureUnit(int pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }

    public void setTemperatureUnit(int temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public void setPrecipitationUnit(int precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

    public MeasureUnit getSpeedUnit() {
        return MeasureUnit.values()[speedUnit];
    }

    public void setSpeedUnit(int speedUnit) {
        this.speedUnit = speedUnit;
    }

    public MeasureUnit getTemperatureUnit() {
        return MeasureUnit.values()[temperatureUnit];
    }

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

    public MeasureUnit getPrecipitationUnit() {
        return MeasureUnit.values()[precipitationUnit];
    }

    public static User getUserInfo() {
        return new Select().from(User.class).executeSingle();
    }

    public List<SocialShare> getSocial() {
        return new Select().from(SocialShare.class).execute();
    }

    @Table(name = "SocialShare")
    public class SocialShare extends Model {
        public final static String F_TYPE = "type";
        public final static String F_SOCIAL_ID = "socialId";

        @Column(name = F_TYPE)
        private SocialType type;

        @Column(name = F_SOCIAL_ID)
        private String socialId;

        public SocialType getType() {
            return type;
        }

        public void setType(SocialType type) {
            this.type = type;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isSync ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
        result = prime * result + precipitationUnit;
        result = prime * result + pressureUnit;
        result = prime * result + speedUnit;
        result = prime * result + temperatureUnit;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (isSync != other.isSync)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (objectId == null) {
            if (other.objectId != null)
                return false;
        } else if (!objectId.equals(other.objectId))
            return false;
        if (precipitationUnit != other.precipitationUnit)
            return false;
        if (pressureUnit != other.pressureUnit)
            return false;
        if (speedUnit != other.speedUnit)
            return false;
        if (temperatureUnit != other.temperatureUnit)
            return false;
        return true;
    }

    public enum SocialType {
        FACEBOOK, GOOGLEPLUS, TWITTER
    }

}

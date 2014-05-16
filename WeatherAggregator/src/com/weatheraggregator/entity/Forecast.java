package com.weatheraggregator.entity;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

@Table(name = "Forecast", id = BaseColumns._ID)
public class Forecast extends BaseModel implements Cloneable, Parcelable {
    public static final String F_TEMP = "temp";
    public final static String F_MAX_TEMP = "max_temp";
    public final static String F_MIN_TEMP = "min_temp";

    public final static String F_SUNRISE = "sunrise";
    public final static String F_SUNSET = "sunset";

    public final static String F_HUMIDITY = "humidity";
    public final static String F_WIND_SPEED = "wind_speed";

    public final static String F_PRESSURE = "pressure";
    public final static String F_MAX_PRESSURE = "max_pressure";
    public final static String F_MIN_PRESSURE = "min_pressure";

    public final static String F_CONDITION = "condition";
    public final static String F_WIND_DIRECTION = "wind_direction";
    public final static String F_PRECIPITATION = "precipitation";
    public final static String F_SERVICE_ID = "service_id";
    public final static String F_CITY_ID = "city_id";
    public final static String F_IS_USER_FORECAST = "is_user_forecast";
    public final static String F_FORECAST_DAY = "forecast_day";
    public final static String F_REQUEST_DAY = "requert_day";

    public final static String F_IS_CURRENT = "is_current";
    public final static String F_REVISION = "revision";
    public final static String F_CLOUDY = "cloudy";

    public final static String F_MAX_WIND_SPEED = "max_wind_speed";
    public final static String F_MIN_WIND_SPEED = "min_wind_speed";

    // Parser constant

    public static final String P_TEMP = "Temp";
    public final static String P_MAX_TEMP = "max_temp";
    public final static String P_MIN_TEMP = "min_temp";

    public final static String P_SUNRISE = "Sunrice";
    public final static String P_SUNSET = "Sunset";

    public final static String P_HUMIDITY = "Humidity";
    public final static String P_WIND_SPEED = "WindSpeed";

    public final static String P_PRESSURE = "Pressure";
    public final static String P_MAX_PRESSURE = "MaxPressure";
    public final static String P_MIN_PRESSURE = "MinPressure";

    public final static String P_CONDITION = "WeatherCondition";
    public final static String P_WIND_DIRECTION = "WindDirection";
    public final static String P_PRECIPITATION = "Precipitation";
    public final static String P_SERVICE_ID = "ServiceId";
    public final static String P_CITY_ID = "CityId";
    // public final static String P_IS_USER_FORECAST = "is_user_forecast";
    public final static String P_UNIX_FORECAST_DAY = "UnixTimeStampDateForecast";
    public final static String P_UNIX_REQUEST_DAY = "UnixTimeStampDateRequest";
    public final static String P_FORECAST_DAY = "DateForecast";
    public final static String P_REQUEST_DAY = "DateRequest";

    public final static String P_IS_CURRENT = "IsCurrent";
    public final static String P_REVISION = "Revision";
    public final static String P_CLOUDY = "Cloudiness";

    public final static String P_MAX_WIND_SPEED = "MaxWindSpeed";
    public final static String P_MIN_WIND_SPEED = "MinWindSpeed";

    @Column(name = F_OBJECT_ID)
    @SerializedName(P_OBJECT_ID)
    protected String objectId;

    @Column(name = F_MAX_WIND_SPEED)
    @SerializedName(P_MAX_WIND_SPEED)
    private Double maxWindSpeed;

    @Column(name = F_FORECAST_DAY)
    @SerializedName(P_FORECAST_DAY)
    private Date forecastDay;

    @Column(name = F_REQUEST_DAY)
    @SerializedName(P_REQUEST_DAY)
    private Date requestDay;

    @Column(name = F_SUNRISE)
    @SerializedName(P_SUNRISE)
    private Date sunrice;

    @Column(name = F_SUNSET)
    @SerializedName(P_SUNSET)
    private Date sunset;

    @Column(name = F_CLOUDY)
    @SerializedName(P_CLOUDY)
    private Integer cloudy;

    @Column(name = F_TEMP)
    @SerializedName(P_TEMP)
    private Integer temp;

    @Column(name = F_HUMIDITY)
    @SerializedName(P_HUMIDITY)
    private Double humidity;

    @Column(name = F_WIND_SPEED)
    @SerializedName(F_WIND_SPEED)
    private Double windSpeed;

    @Column(name = F_PRESSURE)
    @SerializedName(P_PRESSURE)
    private Double pressure;

    @Column(name = F_CONDITION)
    @SerializedName(P_CONDITION)
    private Integer condition;

    @Column(name = F_WIND_DIRECTION)
    @SerializedName(P_WIND_DIRECTION)
    private Integer windDirection;

    @Column(name = F_PRECIPITATION)
    @SerializedName(P_PRECIPITATION)
    private Integer precipitation;

    @Column(name = F_SERVICE_ID)
    @SerializedName(P_SERVICE_ID)
    private String serviceId;

    @Column(name = F_CITY_ID)
    @SerializedName(P_CITY_ID)
    private String cityId;

    @Column(name = F_MAX_TEMP)
    @SerializedName(P_MAX_TEMP)
    private Integer maxTemp;

    @Column(name = F_MIN_TEMP)
    @SerializedName(P_MIN_TEMP)
    private Integer minTemp;

    @Column(name = F_MIN_WIND_SPEED)
    @SerializedName(P_MIN_WIND_SPEED)
    private Double minWindSpeed;

    @Column(name = F_MAX_PRESSURE)
    @SerializedName(P_MAX_PRESSURE)
    private Double maxPressure;

    @Column(name = F_MIN_PRESSURE)
    @SerializedName(P_MIN_PRESSURE)
    private Double minPressure;

    @Column(name = F_REVISION)
    @SerializedName(P_REVISION)
    private Integer revision;

    @Column(name = F_IS_CURRENT)
    @SerializedName(P_IS_CURRENT)
    private boolean isCurrent;

    private String detailTitle;

    public String getObjectId() {
	return objectId;
    }

    public void setObjectId(String objectId) {
	this.objectId = objectId;
    }

    public Double getMaxWindSeep() {
	return maxWindSpeed;
    }

    public void setMaxWindSpeed(Double maxWindSpeed) {
	this.maxWindSpeed = maxWindSpeed;
    }

    public Date getForecastDay() {
	return forecastDay;
    }

    public void setForecastDay(Date forecastDay) {
	this.forecastDay = forecastDay;
    }

    public Date getRequestDay() {
	return requestDay;
    }

    public void setRequestDay(Date requestDay) {
	this.requestDay = requestDay;
    }

    public Date getSunrice() {
	return sunrice;
    }

    public void setSunrice(Date sunrice) {
	this.sunrice = sunrice;
    }

    public Date getSunset() {
	return sunset;
    }

    public void setSunset(Date sunset) {
	this.sunset = sunset;
    }

    public Integer getCloudy() {
	return cloudy;
    }

    public void setCloudy(Integer cloudy) {
	this.cloudy = cloudy;
    }

    public Integer getTemp() {
	return temp;
    }

    public void setTemp(Integer temp) {
	this.temp = temp;
    }

    public Double getHumidity() {
	return humidity;
    }

    public void setHumidity(Double humidity) {
	this.humidity = humidity;
    }

    public Double getWindSpeed() {
	return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
	this.windSpeed = windSpeed;
    }

    public Double getPressure() {
	return pressure;
    }

    public void setPressure(Double pressure) {
	this.pressure = pressure;
    }

    public Integer getWindDirection() {
	return windDirection;
    }

    public void setWindDirection(Integer windDirection) {
	this.windDirection = windDirection;
    }

    public Integer getPrecipitation() {
	return precipitation;
    }

    public void setPrecipitation(Integer precipitation) {
	this.precipitation = precipitation;
    }

    public String getServiceId() {
	return serviceId;
    }

    public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
    }

    public String getCityId() {
	return cityId;
    }

    public void setCityId(String cityId) {
	this.cityId = cityId;
    }

    public Integer getMaxTemp() {
	return maxTemp;
    }

    public void setMaxTemp(Integer maxTemp) {
	this.maxTemp = maxTemp;
    }

    public Integer getMinTemp() {
	return minTemp;
    }

    public void setMinTemp(Integer minTemp) {
	this.minTemp = minTemp;
    }

    public Double getMinWindSpeed() {
	return minWindSpeed;
    }

    public void setMinWindSpeed(Double minWindSpeed) {
	this.minWindSpeed = minWindSpeed;
    }

    public Double getMaxPressure() {
	return maxPressure;
    }

    public void setMaxPressure(Double maxPressure) {
	this.maxPressure = maxPressure;
    }

    public Double getMinPressure() {
	return minPressure;
    }

    public void setMinPressure(Double minPressure) {
	this.minPressure = minPressure;
    }

    public Integer getRevision() {
	return revision;
    }

    public void setRevision(Integer revision) {
	this.revision = revision;
    }

    public boolean isCurrent() {
	return isCurrent;
    }

    public void setCurrent(boolean isCurrent) {
	this.isCurrent = isCurrent;
    }

    public void setCondition(Integer condition) {
	this.condition = condition;
    }

    public Cloudiness getCondition() {
	if (condition == null || condition > Cloudiness.N_A.getCode()) {
	    return Cloudiness.N_A;
	}
	return Cloudiness.values()[condition];
    }

    public WeatherService getService() {
	return new Select().from(WeatherService.class)
		.where(String.format("%s= ?", F_OBJECT_ID), serviceId)
		.executeSingle();
    }

    public City getCity() {
	return new Select().from(City.class)
		.where(String.format("%s= ?", F_OBJECT_ID), cityId)
		.executeSingle();
    }

    public String getDetailTitle() {
	return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
	this.detailTitle = detailTitle;
    }

    @Override
    public Forecast clone() throws CloneNotSupportedException {

	return (Forecast) super.clone();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
	result = prime * result + ((cloudy == null) ? 0 : cloudy.hashCode());
	result = prime * result
		+ ((condition == null) ? 0 : condition.hashCode());
	result = prime * result
		+ ((detailTitle == null) ? 0 : detailTitle.hashCode());
	result = prime * result
		+ ((forecastDay == null) ? 0 : forecastDay.hashCode());
	result = prime * result
		+ ((humidity == null) ? 0 : humidity.hashCode());
	result = prime * result + (isCurrent ? 1231 : 1237);
	result = prime * result
		+ ((maxPressure == null) ? 0 : maxPressure.hashCode());
	result = prime * result + ((maxTemp == null) ? 0 : maxTemp.hashCode());
	result = prime * result
		+ ((maxWindSpeed == null) ? 0 : maxWindSpeed.hashCode());
	result = prime * result
		+ ((minPressure == null) ? 0 : minPressure.hashCode());
	result = prime * result + ((minTemp == null) ? 0 : minTemp.hashCode());
	result = prime * result
		+ ((minWindSpeed == null) ? 0 : minWindSpeed.hashCode());
	result = prime * result
		+ ((objectId == null) ? 0 : objectId.hashCode());
	result = prime * result
		+ ((precipitation == null) ? 0 : precipitation.hashCode());
	result = prime * result
		+ ((pressure == null) ? 0 : pressure.hashCode());
	result = prime * result
		+ ((requestDay == null) ? 0 : requestDay.hashCode());
	result = prime * result
		+ ((revision == null) ? 0 : revision.hashCode());
	result = prime * result
		+ ((serviceId == null) ? 0 : serviceId.hashCode());
	result = prime * result + ((sunrice == null) ? 0 : sunrice.hashCode());
	result = prime * result + ((sunset == null) ? 0 : sunset.hashCode());
	result = prime * result + ((temp == null) ? 0 : temp.hashCode());
	result = prime * result
		+ ((windDirection == null) ? 0 : windDirection.hashCode());
	result = prime * result
		+ ((windSpeed == null) ? 0 : windSpeed.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Forecast other = (Forecast) obj;
	if (cityId == null) {
	    if (other.cityId != null)
		return false;
	} else if (!cityId.equals(other.cityId))
	    return false;
	if (cloudy == null) {
	    if (other.cloudy != null)
		return false;
	} else if (!cloudy.equals(other.cloudy))
	    return false;
	if (condition == null) {
	    if (other.condition != null)
		return false;
	} else if (!condition.equals(other.condition))
	    return false;
	if (detailTitle == null) {
	    if (other.detailTitle != null)
		return false;
	} else if (!detailTitle.equals(other.detailTitle))
	    return false;
	if (forecastDay == null) {
	    if (other.forecastDay != null)
		return false;
	} else if (!forecastDay.equals(other.forecastDay))
	    return false;
	if (humidity == null) {
	    if (other.humidity != null)
		return false;
	} else if (!humidity.equals(other.humidity))
	    return false;
	if (isCurrent != other.isCurrent)
	    return false;
	if (maxPressure == null) {
	    if (other.maxPressure != null)
		return false;
	} else if (!maxPressure.equals(other.maxPressure))
	    return false;
	if (maxTemp == null) {
	    if (other.maxTemp != null)
		return false;
	} else if (!maxTemp.equals(other.maxTemp))
	    return false;
	if (maxWindSpeed == null) {
	    if (other.maxWindSpeed != null)
		return false;
	} else if (!maxWindSpeed.equals(other.maxWindSpeed))
	    return false;
	if (minPressure == null) {
	    if (other.minPressure != null)
		return false;
	} else if (!minPressure.equals(other.minPressure))
	    return false;
	if (minTemp == null) {
	    if (other.minTemp != null)
		return false;
	} else if (!minTemp.equals(other.minTemp))
	    return false;
	if (minWindSpeed == null) {
	    if (other.minWindSpeed != null)
		return false;
	} else if (!minWindSpeed.equals(other.minWindSpeed))
	    return false;
	if (objectId == null) {
	    if (other.objectId != null)
		return false;
	} else if (!objectId.equals(other.objectId))
	    return false;
	if (precipitation == null) {
	    if (other.precipitation != null)
		return false;
	} else if (!precipitation.equals(other.precipitation))
	    return false;
	if (pressure == null) {
	    if (other.pressure != null)
		return false;
	} else if (!pressure.equals(other.pressure))
	    return false;
	if (requestDay == null) {
	    if (other.requestDay != null)
		return false;
	} else if (!requestDay.equals(other.requestDay))
	    return false;
	if (revision == null) {
	    if (other.revision != null)
		return false;
	} else if (!revision.equals(other.revision))
	    return false;
	if (serviceId == null) {
	    if (other.serviceId != null)
		return false;
	} else if (!serviceId.equals(other.serviceId))
	    return false;
	if (sunrice == null) {
	    if (other.sunrice != null)
		return false;
	} else if (!sunrice.equals(other.sunrice))
	    return false;
	if (sunset == null) {
	    if (other.sunset != null)
		return false;
	} else if (!sunset.equals(other.sunset))
	    return false;
	if (temp == null) {
	    if (other.temp != null)
		return false;
	} else if (!temp.equals(other.temp))
	    return false;
	if (windDirection == null) {
	    if (other.windDirection != null)
		return false;
	} else if (!windDirection.equals(other.windDirection))
	    return false;
	if (windSpeed == null) {
	    if (other.windSpeed != null)
		return false;
	} else if (!windSpeed.equals(other.windSpeed))
	    return false;
	return true;
    }

    public enum Cloudiness {
	SUNNY(0), CLOUDY_WITH_SUN(1), CLOUDY(2), OVERCAST(3), SHORT_RAIN(4), RAIN(
		5), RAIN_WITH_THUNDERSTORMS(6), HAIL(7), SNOW_WITH_RAIN(8), SNOW(
		9), BLIZZARD(10), FOG(11), SLEET(12), N_A(13);
	private int code;

	public int getCode() {
	    return code;
	}

	private Cloudiness(int code) {
	    this.code = code;
	}
    }

    protected Forecast(Parcel in) {
	objectId = in.readString();
	maxWindSpeed = in.readByte() == 0x00 ? null : in.readDouble();
	long tmpForecastDay = in.readLong();
	forecastDay = tmpForecastDay != -1 ? new Date(tmpForecastDay) : null;
	long tmpRequestDay = in.readLong();
	requestDay = tmpRequestDay != -1 ? new Date(tmpRequestDay) : null;
	long tmpSunrice = in.readLong();
	sunrice = tmpSunrice != -1 ? new Date(tmpSunrice) : null;
	long tmpSunset = in.readLong();
	sunset = tmpSunset != -1 ? new Date(tmpSunset) : null;
	cloudy = in.readByte() == 0x00 ? null : in.readInt();
	temp = in.readByte() == 0x00 ? null : in.readInt();
	humidity = in.readByte() == 0x00 ? null : in.readDouble();
	windSpeed = in.readByte() == 0x00 ? null : in.readDouble();
	pressure = in.readByte() == 0x00 ? null : in.readDouble();
	condition = in.readByte() == 0x00 ? null : in.readInt();
	windDirection = in.readByte() == 0x00 ? null : in.readInt();
	precipitation = in.readByte() == 0x00 ? null : in.readInt();
	serviceId = in.readString();
	cityId = in.readString();
	maxTemp = in.readByte() == 0x00 ? null : in.readInt();
	minTemp = in.readByte() == 0x00 ? null : in.readInt();
	minWindSpeed = in.readByte() == 0x00 ? null : in.readDouble();
	maxPressure = in.readByte() == 0x00 ? null : in.readDouble();
	minPressure = in.readByte() == 0x00 ? null : in.readDouble();
	revision = in.readByte() == 0x00 ? null : in.readInt();
	isCurrent = in.readByte() != 0x00;
	detailTitle = in.readString();
    }

    public Forecast() {

    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(objectId);
	if (maxWindSpeed == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(maxWindSpeed);
	}
	dest.writeLong(forecastDay != null ? forecastDay.getTime() : -1L);
	dest.writeLong(requestDay != null ? requestDay.getTime() : -1L);
	dest.writeLong(sunrice != null ? sunrice.getTime() : -1L);
	dest.writeLong(sunset != null ? sunset.getTime() : -1L);
	if (cloudy == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(cloudy);
	}
	if (temp == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(temp);
	}
	if (humidity == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(humidity);
	}
	if (windSpeed == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(windSpeed);
	}
	if (pressure == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(pressure);
	}
	if (condition == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(condition);
	}
	if (windDirection == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(windDirection);
	}
	if (precipitation == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(precipitation);
	}
	dest.writeString(serviceId);
	dest.writeString(cityId);
	if (maxTemp == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(maxTemp);
	}
	if (minTemp == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(minTemp);
	}
	if (minWindSpeed == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(minWindSpeed);
	}
	if (maxPressure == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(maxPressure);
	}
	if (minPressure == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeDouble(minPressure);
	}
	if (revision == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(revision);
	}
	dest.writeByte((byte) (isCurrent ? 0x01 : 0x00));
	dest.writeString(detailTitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {
	@Override
	public Forecast createFromParcel(Parcel in) {
	    return new Forecast(in);
	}

	@Override
	public Forecast[] newArray(int size) {
	    return new Forecast[size];
	}
    };
}

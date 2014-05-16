package com.weatheraggregator.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "City", id = BaseColumns._ID)
public class City extends BaseModel implements Comparable<City>, Parcelable {
    public final static String F_CODE = "code";
    public final static String F_IS_DELETED = "is_delete";
    public final static String F_NAME = "name";
    public final static String F_ORDER = "order_number";
    public final static String F_COUNTRY = "country";
    public final static String F_REGION = "region";
    public final static String F_IS_SYNC = "is_sync";
    public final static String F_LATITUDE = "latitude";
    public final static String F_LONGITUDE = "longitude";

    public final static String DEFAULT_CITY_ID = "new";
public final static String LOCATION_CITY_ID = "00000000-0000-0000-0000-000000000000";
    @Column(name = F_OBJECT_ID)
    @SerializedName("CityId")
    protected String objectId;

    @Column(name = F_IS_SYNC)
    protected boolean isSync;

    @Column(name = F_NAME)
    @SerializedName("Name")
    protected String name;

    @Column(name = F_CODE)
    @SerializedName("PostalCode")
    private Integer code;

    @Column(name = F_IS_DELETED)
    @SerializedName("IsDelete")
    private boolean isDelete;

    @Column(name = F_ORDER)
    @SerializedName("OrderIndex")
    private int order;

    @Column(name = F_COUNTRY)
    @SerializedName("Country")
    protected String country;

    @Column(name = F_REGION)
    @SerializedName("Region")
    protected String region;

    @SerializedName("Latitude")
    @Column(name = F_LATITUDE)
    protected Double latitude;

    @SerializedName("Longitude")
    @Column(name = F_LONGITUDE)
    protected Double longitude;

    private boolean isDeleteState;
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((code == null) ? 0 : code.hashCode());
	result = prime * result + ((country == null) ? 0 : country.hashCode());
	result = prime * result + (isDelete ? 1231 : 1237);
	result = prime * result + (isSync ? 1231 : 1237);
	result = prime * result
		+ ((latitude == null) ? 0 : latitude.hashCode());
	result = prime * result
		+ ((longitude == null) ? 0 : longitude.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((objectId == null) ? 0 : objectId.hashCode());
	result = prime * result + order;
	result = prime * result + ((region == null) ? 0 : region.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	City other = (City) obj;
	if (code == null) {
	    if (other.code != null)
		return false;
	} else if (!code.equals(other.code))
	    return false;
	if (country == null) {
	    if (other.country != null)
		return false;
	} else if (!country.equals(other.country))
	    return false;
	if (isDelete != other.isDelete)
	    return false;
	if (isSync != other.isSync)
	    return false;
	if (latitude == null) {
	    if (other.latitude != null)
		return false;
	} else if (!latitude.equals(other.latitude))
	    return false;
	if (longitude == null) {
	    if (other.longitude != null)
		return false;
	} else if (!longitude.equals(other.longitude))
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
	if (order != other.order)
	    return false;
	if (region == null) {
	    if (other.region != null)
		return false;
	} else if (!region.equals(other.region))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	if (name != null) {
	    return name;
	} else {
	    return "";
	}
    }

    private City(Parcel in) {
	readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
	name = in.readString();
	objectId = in.readString();
	code = in.readInt();
	if (in.readInt() == 1) {
	    isDelete = true;
	} else {
	    isDelete = false;
	}
	order = in.readInt();
	country = in.readString();
	region = in.readString();

	super.mId = in.readLong();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
	public City createFromParcel(Parcel in) {
	    return new City(in);
	}

	public City[] newArray(int size) {
	    return new City[size];
	}
    };

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	out.writeString(name);
	out.writeString(objectId);
	out.writeInt(code);
	if (isDelete) {
	    out.writeInt(1);
	} else {
	    out.writeInt(1);
	}
	out.writeInt(order);
	out.writeString(country);
	out.writeString(region);
	out.writeLong(getId());
    }

    public City() {

    }

    public City(String objectId) {
	this.objectId = objectId;
    }

    @Override
    public int compareTo(City secondCity) {
	return this.order - secondCity.getOrder();
    }

    public boolean isDeleteState() {
	return isDeleteState;
    }

    public void setDeleteState(boolean isDeleteState) {
	this.isDeleteState = isDeleteState;
    }
}

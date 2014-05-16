package com.weatheraggregator.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "WeatherService", id = BaseColumns._ID)
public class WeatherService extends BaseModel implements
	Comparable<WeatherService>, Parcelable {
    public final static String F_ISDELETE = "is_delete";
    public final static String F_DESCRIPTION = "description";
    public final static String F_URL = "url";
    public final static String F_LOGO_URL = "logo_url";
    public final static String F_IS_FAVORITE = "is_favorite";

    public final static String F_NAME = "name";
    public final static String F_IS_SYNCHRONIZED = " is_synchronized";
    public final static String F_ORDER = "order_number";
    public final static String F_VOTED_FOR = "voted_for";
    public final static String F_VOTE_AGAINST = "voted_against";
    @Column(name = F_NAME)
    @SerializedName("Name")
    protected String name;

    @Column(name = F_OBJECT_ID)
    @SerializedName("Id")
    protected String objectId;

    @Column(name = F_ISDELETE)
    @SerializedName("IsDelete")
    private Boolean isDelete;

    @Column(name = F_DESCRIPTION)
    @SerializedName("Description")
    private String description;

    @Column(name = F_URL)
    @SerializedName("Url")
    private String url;

    @Column(name = F_LOGO_URL)
    @SerializedName("LogoUrl")
    private String logoUrl;

    @SerializedName("IsFavorite")
    @Column(name = F_IS_FAVORITE)
    private Boolean isFavorite;

    @Column(name = F_ORDER)
    @SerializedName("OrderIndex")
    private Integer order;

    @Column(name = F_VOTED_FOR)
    @SerializedName("VotedFor")
    private Integer votedFor;

    @Column(name = F_VOTE_AGAINST)
    @SerializedName("VotedAgainst")
    private Integer votedAgainst;

    @Column(name = F_IS_SYNCHRONIZED)
    private Boolean isSynchronized;
    
    private boolean isVote;

    public WeatherService(Parcel in) {
	readFromParcel(in);
    }
    

    public WeatherService() {

    }

    public WeatherService(String objectId) {
	this.objectId = objectId;
    }

    public Integer getVotedFor() {
	return votedFor;
    }

    public void setVotedFor(Integer votedFor) {
	this.votedFor = votedFor;
    }

    public Integer getVotedAgainst() {
	return votedAgainst;
    }

    public void setVotedAgainst(Integer votedAgainst) {
	this.votedAgainst = votedAgainst;
    }

    public Integer getOrder() {
	return order;
    }

    public void setOrder(Integer order) {
	this.order = order;
    }

    public boolean isSynchronized() {
	return isSynchronized;
    }

    public void setSynchronized(Boolean isSynchronized) {
	this.isSynchronized = isSynchronized;
    }

    public Boolean isFavorite() {
	return isFavorite;
    }

    public void setFavorite(Boolean isFavorite) {
	this.isFavorite = isFavorite;
    }

    public Boolean isDelete() {
	return isDelete;
    }

    public void setDelete(Boolean isDelete) {
	this.isDelete = isDelete;
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

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getLogoUrl() {
	return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
	this.logoUrl = logoUrl;
    }

    public boolean isCanVote() {
        return isVote;
    }


    public void setCanVote(boolean isVote) {
        this.isVote = isVote;
    }


    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result + (isDelete ? 1231 : 1237);
	result = prime * result + (isFavorite ? 1231 : 1237);
	result = prime * result + (isSynchronized ? 1231 : 1237);
	result = prime * result + ((logoUrl == null) ? 0 : logoUrl.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((objectId == null) ? 0 : objectId.hashCode());
	result = prime * result + order;
	result = prime * result + ((url == null) ? 0 : url.hashCode());
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
	WeatherService other = (WeatherService) obj;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (isDelete != other.isDelete)
	    return false;
	if (isFavorite != other.isFavorite)
	    return false;
	if (isSynchronized != other.isSynchronized)
	    return false;
	if (logoUrl == null) {
	    if (other.logoUrl != null)
		return false;
	} else if (!logoUrl.equals(other.logoUrl))
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
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	return true;
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(name);
	dest.writeString(objectId);
	if (isDelete == null) {
	    dest.writeByte((byte) (0x02));
	} else {
	    dest.writeByte((byte) (isDelete ? 0x01 : 0x00));
	}
	dest.writeString(description);
	dest.writeString(url);
	dest.writeString(logoUrl);
	if (isFavorite == null) {
	    dest.writeByte((byte) (0x02));
	} else {
	    dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
	}
	if (order == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(order);
	}
	if (votedFor == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(votedFor);
	}
	if (votedAgainst == null) {
	    dest.writeByte((byte) (0x00));
	} else {
	    dest.writeByte((byte) (0x01));
	    dest.writeInt(votedAgainst);
	}
	if (isSynchronized == null) {
	    dest.writeByte((byte) (0x02));
	} else {
	    dest.writeByte((byte) (isSynchronized ? 0x01 : 0x00));
	}
    }

    public static final Parcelable.Creator<WeatherService> CREATOR = new Parcelable.Creator<WeatherService>() {
	@Override
	public WeatherService createFromParcel(Parcel in) {
	    return new WeatherService(in);
	}

	@Override
	public WeatherService[] newArray(int size) {
	    return new WeatherService[size];
	}
    };

    private void readFromParcel(Parcel in) {
	name = in.readString();
	objectId = in.readString();
	byte isDeleteVal = in.readByte();
	isDelete = isDeleteVal == 0x02 ? null : isDeleteVal != 0x00;
	description = in.readString();
	url = in.readString();
	logoUrl = in.readString();
	byte isFavoriteVal = in.readByte();
	isFavorite = isFavoriteVal == 0x02 ? null : isFavoriteVal != 0x00;
	order = in.readByte() == 0x00 ? null : in.readInt();
	votedFor = in.readByte() == 0x00 ? null : in.readInt();
	votedAgainst = in.readByte() == 0x00 ? null : in.readInt();
	byte isSynchronizedVal = in.readByte();
	isSynchronized = isSynchronizedVal == 0x02 ? null
		: isSynchronizedVal != 0x00;
    }

    @Override
    public int compareTo(WeatherService another) {
	return this.order - another.getOrder();
    }
}

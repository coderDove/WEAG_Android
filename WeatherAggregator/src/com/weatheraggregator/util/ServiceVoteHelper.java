package com.weatheraggregator.util;

import java.util.Date;

import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.query.Select;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.WeatherService;

public class ServiceVoteHelper {

    public static ServiceRating saveVote(Boolean vote, Date voteDate,
	    String cityId, String serviceId) {
	ServiceRating serviceRating = new ServiceRating();
	serviceRating.setDate(voteDate);
	serviceRating.setCityId(cityId);
	serviceRating.setServiceId(serviceId);
	serviceRating.setVote(vote);
	serviceRating.save();
	return serviceRating;
    }

    public static ServiceRating getServiceRating(City city,
	    WeatherService service, Date voteDate) {
	ServiceRating serviceRating = null;
	if (city != null && city.getObjectId() != null && service != null
		&& service.getObjectId() != null) {
	    serviceRating = new Select(BaseColumns._ID,
		    ServiceRating.F_CITY_ID, ServiceRating.F_SERVICE_ID,
		    ServiceRating.F_DATE, ServiceRating.F_VOTE)
		    .from(ServiceRating.class)
		    .where(String.format(
			    "%s= ? and %s=? and %s BETWEEN ? and ?",
			    ServiceRating.F_CITY_ID,
			    ServiceRating.F_SERVICE_ID, ServiceRating.F_DATE),
			    city.getObjectId(),
			    service.getObjectId(),
			    DateHelper.getStartDateByDate(voteDate).getTime()
				    .getTime(),
			    DateHelper.getEndDateByDate(voteDate).getTime()
				    .getTime()).executeSingle();
	}
	return serviceRating;
    }

    public static boolean needEnableRating(City city, WeatherService service,
	    Date dateForecast) {
	ServiceRating serviceRating = ServiceVoteHelper.getServiceRating(city,
		service, dateForecast);
	boolean enabled = false;
	if (serviceRating == null) {
	    enabled = true;
	}
	return enabled;
    }

}

package com.weatheraggregator.util;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.content.Context;
import android.util.Log;

import com.activeandroid.query.Select;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.webservice.ServiceManager;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

/**
 * class performs the synchronization of objects that can be modified by the
 * user
 * 
 */
public class SyncHelper {
    private ServiceManager mWebService;
    private final ScheduledExecutorService scheduler = Executors
	    .newSingleThreadScheduledExecutor();
    private final static String TAG = "SyncHelper";

    public SyncHelper(ServiceManager service) {
	if (service != null) {
	    mWebService = service;
	}
    }

    public SyncHelper() {

    }

    public static synchronized void syncWeatherService(
	    List<WeatherService> listOfServices) {
	for (int indexService = 0; indexService < listOfServices.size(); indexService++) {
	    listOfServices.get(indexService).setSynchronized(true);
	    listOfServices.get(indexService).save(
		    listOfServices.get(indexService)
			    .getLocalObjectIdByServiceObjectId());
	}
    }

    public synchronized static void syncUser(User user) {
	user.setSync(true);
	user.save();
    }

    public static synchronized void syncCity(List<City> listOfCities) {
	if (listOfCities != null && !listOfCities.isEmpty()) {
	    for (int indexCity = 0; indexCity < listOfCities.size(); indexCity++) {
		listOfCities.get(indexCity).setSync(true);
		listOfCities.get(indexCity).save(
			listOfCities.get(indexCity)
				.getLocalObjectIdByServiceObjectId());
	    }
	}
    }

    public void syncWeatherService(List<WeatherService> listOfServices,
	    Context context, String userId) {
	if (listOfServices != null && !listOfServices.isEmpty()
		&& context != null && userId != null) {
	    if (mWebService != null) {
		try {
		    mWebService.updateService(context, listOfServices, userId);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    } else {
		try {
		    ServiceManager.newInstance().updateService(context,
			    listOfServices, userId);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	    syncWeatherService(listOfServices);
	    Log.d(TAG, "syncWeatherService");
	}
    }

    public void syncWeatherService(Context context, String userId) {
	List<WeatherService> listOfServices = new Select()
		.from(WeatherService.class)
		.where(String
			.format("%s = 0", WeatherService.F_IS_SYNCHRONIZED))
		.execute();
	syncWeatherService(listOfServices, context, userId);
    }

    public void syncCities(List<City> listOfCities, Context context,
	    String userId) {
	if (listOfCities != null && !listOfCities.isEmpty() && context != null
		&& userId != null) {
	    if (mWebService != null) {
		try {
		    mWebService.updateFavoriteCity(context, listOfCities,
			    userId);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    } else {
		try {
		    ServiceManager.newInstance().updateFavoriteCity(context,
			    listOfCities, userId);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	    syncCity(listOfCities);
	    Log.d(TAG, "syncCity");
	}
    }

    public void syncFavoriteCities(Context context, String userId) {
	List<City> listOfCity = new Select().from(City.class)
		.where(String.format("%s = 0", City.F_IS_SYNC)).execute();
	syncCities(listOfCity, context, userId);
    }

    public boolean syncUserMeasure(User user, Context context) {
	boolean isUpdate = false;
	if (user != null && user.getObjectId() != null && !user.isSync()) {
	    if (mWebService != null) {
		try {
		    isUpdate = mWebService.updateUser(context, user);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    } else {
		try {
		    isUpdate = ServiceManager.newInstance().updateUser(context,
			    user);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	}
	if (isUpdate) {
	    syncUser(user);
	}
	Log.d(TAG, "syncUser");
	return true;
    }

    public void sycnRating(Context context) {
	List<ServiceRating> ratings = new Select().from(ServiceRating.class)
		.where(String.format("%s = 0", ServiceRating.F_SYNC)).execute();
	syncRating(context, ratings);
    }

    public void syncRating(Context context, List<ServiceRating> ratings) {
	if (ratings != null && !ratings.isEmpty()) {
	    for (int index = 0; index < ratings.size(); index++) {
		ServiceRating rating = ratings.get(index);
		try {
		    WeatherService weatherService = ServiceManager
			    .newInstance().setServiceRating(context, rating);
		    if (weatherService != null) {
			WeatherService localService = getLoaclService(weatherService
				.getObjectId());
			localService.setVotedAgainst(weatherService
				.getVotedAgainst());
			localService.setVotedFor(weatherService.getVotedFor());
			localService.save(weatherService
				.getLocalObjectIdByServiceObjectId());
			rating.setSync(true);
			rating.save();
		    }
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	}
    }

    private WeatherService getLoaclService(String serviceId) {
	return new Select()
		.from(WeatherService.class)
		.where(String.format("%s=?", WeatherService.F_OBJECT_ID),
			serviceId).executeSingle();
    }

    public void syncAll(final Context context, final String userId) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		sycnRating(context);
		syncFavoriteCities(context, userId);
		syncWeatherService(context, userId);
		User user = new Select().from(User.class)
			.where(String.format("%s = 0", User.F_IS_SYNC))
			.executeSingle();
		syncUserMeasure(user, context);
	    }
	});
    }
}

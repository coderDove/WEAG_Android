package com.weatheraggregator.localservice;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.model.ForecastResponse;
import com.weatheraggregator.model.UserForecastAllService;

public class RemoteServiceHelper {
    @Deprecated
    public synchronized List<UserForecastAllService> getUserForecastAllService() {
	List<City> cities = new Select().from(City.class).execute();
	List<UserForecastAllService> listRequest = new ArrayList<UserForecastAllService>();
	for (City city : cities) {
	    Cursor cursor = Cache
		    .openDatabase()
		    .rawQuery(
			    "SELECT max(revision) as revision FROM Forecast WHERE Forecast.city_id =?",
			    new String[] { city.getObjectId() });
	    UserForecastAllService ufas = new UserForecastAllService();
	    ufas.setCityId(city.getObjectId());
	    if (cursor != null) {
		if (cursor.moveToFirst()) {
		    int revicion = cursor.getInt(0);
		    ufas.setRevision(revicion);
		} else {
		    ufas.setRevision(0);
		}
		cursor.close();
	    }
	    listRequest.add(ufas);
	}
	return listRequest;
    }

    public void saveUserServices(List<WeatherService> listofServices) {
	if (listofServices != null && !listofServices.isEmpty()) {
	    ActiveAndroid.beginTransaction();
	    try {
		for (WeatherService service : listofServices) {
		    service.setSynchronized(true);
		    service.save(service.getLocalObjectIdByServiceObjectId());
		}
		ActiveAndroid.setTransactionSuccessful();
	    } finally {
		ActiveAndroid.endTransaction();
	    }
	}
    }

    public void loadUserCity(List<City> listOfCities) {
	if (listOfCities != null && !listOfCities.isEmpty()) {
	    ActiveAndroid.beginTransaction();
	    try {
		for (City city : listOfCities) {
		    city.setSync(true);
		    city.save(city.getLocalObjectIdByServiceObjectId());
		}
		ActiveAndroid.setTransactionSuccessful();
	    } finally {
		ActiveAndroid.endTransaction();
	    }
	}
    }

    public void saveForecast(ForecastResponse responce) {
	if (responce == null) {
	    return;
	}
	ActiveAndroid.beginTransaction();
	try {
	    if (responce.getForecasts() != null) {
		City city = new City(responce.getCityId());
		WeatherService service = new WeatherService(
			responce.getServiceId());
		for (Forecast forecast : responce.getForecasts()) {
		    forecast.setCityId(city.getObjectId());
		    forecast.setServiceId(service.getObjectId());
		    forecast.save(forecast.getLocalObjectIdByServiceObjectId());
		}
	    }
	    ActiveAndroid.setTransactionSuccessful();
	} finally {
	    ActiveAndroid.endTransaction();
	}
    }
}

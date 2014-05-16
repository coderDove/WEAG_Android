package com.weatheraggregator.localservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.LocationCityForecasts;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.interfaces.ISendBroadcast;
import com.weatheraggregator.model.ForecastResponse;
import com.weatheraggregator.util.DateHelper;
import com.weatheraggregator.util.PreferenceMapper;
import com.weatheraggregator.util.SyncHelper;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.webservice.ServiceManager;
import com.weatheraggregator.webservice.exception.ErrorType;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

public class WeatherRemoteService extends Service implements ISendBroadcast {
    public final static String TAG = WeatherRemoteService.class
	    .getCanonicalName();
    private ExecuteScheduledService mExecutor;
    private WifiConnectedReceiver wifiReceiver;
    public static final String FORECASTS_NEW_DATA = "com.weatheraggregator.localservice.ExecuteScheduledService.update";
    private RemoteServiceHelper serviceHelper = new RemoteServiceHelper();

    @Override
    public void onCreate() {
	super.onCreate();
	mExecutor = new ExecuteScheduledService(WeatherRemoteService.this);
	mExecutor.startScheduleExecutor(getApplication());
	wifiReceiver = new WifiConnectedReceiver();
	registerReceiver(wifiReceiver, new IntentFilter(
		WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    public void onDestroy() {
	if (wifiReceiver != null) {
	    unregisterReceiver(wifiReceiver);
	}
	super.onDestroy();
    }

    private String getUserId() {
	String userId = PreferenceMapper.getUserId(getApplication());
	Log.d(TAG, "userId: " + userId);
	// if (userId == null || userId.isEmpty()) {
	// throw new RuntimeException("User Id is null");
	// }
	return userId;
    }

    private IWeatherRemoteService.Stub mBinder = new IWeatherRemoteService.Stub() {
	private void sendCallback(IDataSourceServiceListener callBack,
		ErrorType errorType) throws RemoteException {
	    if (callBack != null) {
		callBack.callBack(errorType.getCode());
	    }
	}

	private void sendCallback(IDetailDataSourceServiceListener callBack,
		ErrorType errorType, List<Forecast> forecasts)
		throws RemoteException {
	    if (callBack != null) {
		callBack.callBack(errorType.getCode(), forecasts);
	    }
	}

	@Override
	public void addFavouriteCity(City city,
		IDataSourceServiceListener callBack) throws RemoteException {
	    try {
		City localCity = city;

		if (city != null) {
		    city = ServiceManager.newInstance().addFavouriteCity(
			    getApplication(), city, getUserId());
		    if (city != null) {
			localCity.setSync(true);
			localCity.setDelete(false);
			localCity.setObjectId(city.getObjectId());
			localCity.save();
			sendCallback(callBack, ErrorType.HTTP_OK);
		    }
		}
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    }
	}

	@Override
	public void removeFavouriteCity(final String cityId,
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    City city = new Select().from(City.class)
		    .where(String.format("%s = ?", City.F_OBJECT_ID), cityId)
		    .executeSingle();
	    if (city == null) {
		return;
	    }
	    try {
		List<City> cities = new ArrayList<City>();
		cities.add(city);
		// city.delete();
		Log.e(TAG, "removeFavouriteCity");

		ServiceManager.newInstance().removeCity(
			getApplicationContext(), cities, getUserId());
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		// rollbackCity(city);
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    } catch (ParseException e) {
		sendCallback(callBack, e.getErrorStatus());
		// rollbackCity(city);
		Log.e(TAG, e.getMessage());
	    }
	}

	// private void rollbackCity(City city) {
	// if (city != null) {
	// city.setDelete(false);
	// city.save(city.getLocalObjectIdByServiceObjectId());
	// }
	// }

	@Override
	public void reorderCity(final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		updateCities();
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		callBack.callBack(e.getErrorStatus().getCode());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		callBack.callBack(e.getErrorStatus().getCode());
	    }
	}

	@Override
	public void reorderWeatherService(
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		updateServices();
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    } catch (ParseException e) {
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    }
	}

	private void userRegistration() throws InternetException,
		ParseException {
	    User user = new User();
	    user.setName(Util.getAccountEmail(getApplication()));
	    user = ServiceManager.newInstance().registerUser(getApplication(),
		    user);
	    PreferenceMapper.putUserId(getApplication(), user.getObjectId());
	    user.setSync(true);
	    user.save();
	    Log.e(TAG, "userRegistration1" + user.getObjectId());
	}

	@Override
	public void userRegistration(final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		userRegistration();
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	private void loadServices() throws InternetException, ParseException {
	    List<WeatherService> listOfServices = ServiceManager.newInstance()
		    .getUserWeatherService(getApplication(), getUserId());
	    Log.d(TAG, "loadWeatherService");
	    serviceHelper.saveUserServices(listOfServices);
	}

	@Override
	public void loadWeatherService(IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		loadServices();
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	@Override
	public void updateWeatherService(
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		updateServices();
		Log.e(TAG, "updateWeatherService");
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	private void updateServices() throws InternetException, ParseException,
		RemoteException {
	    List<WeatherService> listOfService = new Select().from(
		    WeatherService.class).execute();
	    if (listOfService != null && !listOfService.isEmpty()) {
		Log.e(TAG, "updateServices");

		ServiceManager.newInstance().updateService(getApplication(),
			listOfService, getUserId());
		SyncHelper.syncWeatherService(listOfService);
	    }
	}

	private void updateCities() throws InternetException, ParseException {
	    List<City> cities = new Select().from(City.class).execute();
	    if (cities != null && !cities.isEmpty()) {
		ServiceManager.newInstance().updateFavoriteCity(
			getBaseContext(), cities, getUserId());
		SyncHelper.syncCity(cities);
	    }
	}

	private void loadUserCity() throws InternetException, ParseException {
	    List<City> listOfCity = ServiceManager.newInstance().loadUserCity(
		    getApplication(), getUserId());
	    Log.i(TAG, "loadUserCity");
	    serviceHelper.loadUserCity(listOfCity);
	}

	@Override
	public void loadUserCity(final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		loadUserCity();
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	private void loadForecastByPeriod(long uStartDate, long uEndDate)
		throws InternetException, ParseException {
	    List<ForecastResponse> forecasts = ServiceManager.newInstance()
		    .loadForecastByPeriod(getApplication(), getUserId(),
			    uStartDate, uEndDate);
	    saveForecastResponse(forecasts);
	}

	@Override
	public void loadForecastByPeriod(long uStartDate, long uEndDate,
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		loadForecastByPeriod(uStartDate, uEndDate);
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    }
	}

	@Override
	public void loadActualForecast(
		final IDataSourceServiceListener callBack, final long uDate)
		throws RemoteException {

	    try {
		List<ForecastResponse> listResponce = ServiceManager
			.newInstance().getActualForecastByDate(
				getApplication(), getUserId(), uDate);
		saveForecastResponse(listResponce);
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    }
	}

	private void saveForecastResponse(List<ForecastResponse> listResponce) {
	    if (listResponce != null && !listResponce.isEmpty()) {
		for (ForecastResponse response : listResponce) {
		    serviceHelper.saveForecast(response);
		}
	    }
	}

	@Override
	public void loadForecast(String cityId, String serviceId, long uDate,
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		List<ForecastResponse> listOfForecast = ServiceManager
			.newInstance().loadForecast(getApplication(), cityId,
				serviceId, uDate);
		Log.d(TAG, "loadForecast");
		saveForecastResponse(listOfForecast);
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		sendCallback(callBack, e.getErrorStatus());
		Log.e(TAG, e.getMessage());
	    }
	}

	@Override
	public void loadForecastByDate(long uDate,
		IDataSourceServiceListener callBack) throws RemoteException {
	    List<ForecastResponse> forecasts = null;
	    try {
		forecasts = ServiceManager.newInstance().loadForecast(
			getApplication(), getUserId(), uDate);
		saveForecastResponse(forecasts);
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }

	}

	@Override
	public void setFavouriteWeatherService(String serviceId,
		String oldServiceId, IDataSourceServiceListener callBack)
		throws RemoteException {
	    WeatherService newService = new Select()
		    .from(WeatherService.class)
		    .where(String.format("%s = ?", WeatherService.F_OBJECT_ID),
			    serviceId).executeSingle();
	    WeatherService oldService = new Select()
		    .from(WeatherService.class)
		    .where(String.format("%s = ?", WeatherService.F_OBJECT_ID),
			    oldServiceId).executeSingle();
	    if (newService != null) {
		List<WeatherService> list = new ArrayList<WeatherService>();
		list.add(newService);
		list.add(oldService);
		try {
		    ServiceManager.newInstance().updateService(
			    getApplication(), list, getUserId());
		    SyncHelper.syncWeatherService(list);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	}

	@Override
	public void updateUser(IDataSourceServiceListener callBack)
		throws RemoteException {
	    User user = new Select().from(User.class).executeSingle();
	    if (user != null) {
		try {
		    if (ServiceManager.newInstance().updateUser(
			    getApplication(), user))
			SyncHelper.syncUser(user);
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	}

	@Override
	public void synUserData(IDataSourceServiceListener callBack)
		throws RemoteException {
	    SyncHelper syncHelper = new SyncHelper(ServiceManager.newInstance());
	    try {
		syncHelper.syncAll(getApplication(), getUserId());
	    } finally {
		sendCallback(callBack, ErrorType.HTTP_OK);
	    }
	}

	@Override
	public void registerUserWithLoadingData(
		IDataSourceServiceListener callBack) throws RemoteException {
	    try {
		if (getUserId() == null) {
		    userRegistration();
		}
		loadUserCity();
		loadServices();
		Date curTime = new Date();
		loadForecastByPeriod(
			// DateHelper.getFirstDateOfMonthByDate(curTime).getTime(),
			// DateHelper.getLastDateOfMonthByDate(curTime).getTime()
			DateHelper.getFirstDateOfWeek(curTime).getTime(),
			DateHelper.getLastDateOfWeek(curTime).getTime());
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	@Override
	public void loadUserCityWithForecast(IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		loadUserCity();
		loadServices();
		Date curTime = new Date();
		loadForecastByPeriod(
			DateHelper.getFirstDateOfMonthByDate(curTime).getTime(),
			DateHelper.getLastDateOfMonthByDate(curTime).getTime());
		if (callBack != null) {
		    callBack.callBack(ErrorType.HTTP_OK.getCode());
		}
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	private boolean isFavorite(String serviceId) {
	    WeatherService localService = new Select()
		    .from(WeatherService.class)
		    .where(String.format("%s = ?", WeatherService.F_OBJECT_ID),
			    serviceId).executeSingle();
	    if (localService != null) {
		return localService.isFavorite();
	    }
	    return false;
	}

	@Override
	public void sendServiceRating(long serviceRatingId,
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    ServiceRating serviceRating = new Select()
		    .from(ServiceRating.class)
		    .where(String.format("%s=?", BaseColumns._ID),
			    serviceRatingId).executeSingle();
	    try {
		if (serviceRating != null) {
		    WeatherService weatherService = ServiceManager
			    .newInstance().setServiceRating(getApplication(),
				    serviceRating);
		    serviceRating.setSync(true);
		    WeatherService localService = new Select()
			    .from(WeatherService.class)
			    .where(String.format("%s = ?",
				    WeatherService.F_OBJECT_ID),
				    weatherService.getObjectId())
			    .executeSingle();
		    localService.setVotedAgainst(weatherService
			    .getVotedAgainst());
		    localService.setVotedFor(weatherService.getVotedFor());
		    localService.save();
		    Log.e(TAG,
			    "sendServiceRating:" + weatherService.getObjectId());
		    sendCallback(callBack, ErrorType.HTTP_OK);
		}
	    } catch (InternetException e) {
		serviceRating.setSync(false);
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		serviceRating.setSync(false);
		sendCallback(callBack, e.getErrorStatus());
	    } finally {
		if (serviceRating != null) {
		    serviceRating.save();
		}
	    }
	}

	@Override
	public void loadCityByLocation(double lat, double lon,
		final IDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		City city = ServiceManager.newInstance().getCityByLocation(lat,
			lon, getApplication());
		saveCityByLocation(city);
		sendCallback(callBack, ErrorType.HTTP_OK);
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	@Override
	public void loadCityWithForecastsByLocation(double lat, double lon,
		IDataSourceServiceListener callBack) throws RemoteException {
	    try {
		LocationCityForecasts locationCityForecasts = ServiceManager
			.newInstance().loadCityWithForecastsByLocation(lat,
				lon, getUserId(), getApplication());
		if (locationCityForecasts != null) {
		    if (PreferenceMapper.isUseLocation(getApplication())) {
			saveCityByLocation(locationCityForecasts
				.getCurrentCity());
			if (locationCityForecasts.getWeatherList() != null
				&& locationCityForecasts.getWeatherList()
					.size() > 0) {
			    for (int index = 0; index <= locationCityForecasts
				    .getWeatherList().size() - 1; index++) {
				serviceHelper
					.saveForecast(locationCityForecasts
						.getWeatherList().get(index));
			    }
			}
			Log.e(TAG, "loadCityWithForecastsByLocation :callBack");
			sendCallback(callBack, ErrorType.HTTP_OK);
		    }
		}
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus());
	    }
	}

	@Override
	public void loadDetailForecast(String cityId, String serviceId,
		long uDate, IDetailDataSourceServiceListener callBack)
		throws RemoteException {
	    try {
		List<ForecastResponse> list = new ArrayList<ForecastResponse>();
		ForecastResponse response = ServiceManager.newInstance()
			.getForecast(uDate, serviceId, cityId);
		if (!response.getForecasts().isEmpty()) {
		    list.add(response);
		    saveForecastResponse(list);
		    sendCallback(callBack, ErrorType.HTTP_OK, list.get(0)
			    .getForecasts());
		} else {
		    sendCallback(callBack, ErrorType.HTTP_OK, null);
		}
	    } catch (InternetException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus(), null);
	    } catch (ParseException e) {
		Log.e(TAG, e.getMessage());
		sendCallback(callBack, e.getErrorStatus(), null);
	    }
	}
    };

    private void saveCityByLocation(City city) {
	if (city != null) {
	    try {
		ActiveAndroid.beginTransaction();
		new Delete()
			.from(City.class)
			.where(String.format("%s = ?", City.F_OBJECT_ID),
				City.LOCATION_CITY_ID).execute();
		city.setDelete(false);
		city.setOrder(getCityOrder());
		city.save();
		ActiveAndroid.setTransactionSuccessful();
	    } finally {
		ActiveAndroid.endTransaction();
	    }
	}
    }

    private int getCityOrder() {
	Cursor cursor = Cache.openDatabase().rawQuery(
		"SELECT _id  FROM City WHERE is_delete = 0", null);
	if (cursor != null) {
	    int order = cursor.getCount();
	    cursor.close();
	    if (order != 0) {
		return order;
	    }
	}
	return 0;
    }

    @Override
    public void sendForecastUpdateBroadcast() {
	Intent intent = new Intent();
	intent.setAction(FORECASTS_NEW_DATA);
	sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
	return mBinder;
    }
}
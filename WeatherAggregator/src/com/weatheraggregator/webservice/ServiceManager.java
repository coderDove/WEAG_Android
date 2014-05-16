package com.weatheraggregator.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.LocationCityForecasts;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.model.ForecastResponse;
import com.weatheraggregator.parser.ParserManager;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.webservice.HttpTransport.WebServiceResponseStatusCode;
import com.weatheraggregator.webservice.exception.ErrorType;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

public class ServiceManager {
    public static final String TAG = "ServiceManager";
    private ServiceConfiguration mBuilder;

    public ServiceManager init(ServiceConfiguration builder) {
	mBuilder = builder;
	if (mBuilder == null) {
	    mBuilder = new ServiceConfiguration();
	}
	return this;
    }

    public ServiceManager() {
	mBuilder = new ServiceConfiguration();
    }

    public synchronized static ServiceManager newInstance() {
	return new ServiceManager().init(null);
    }

    public ServiceConfiguration getBuilder() {
	return mBuilder;
    }

    public static class ServiceConfiguration {
	private final HttpTransport mHttpTransport;
	private final ParserManager mParserManager;

	public ServiceConfiguration(HttpTransport httpTransport,
		ParserManager parserManager) {

	    if (httpTransport != null) {
		mHttpTransport = httpTransport;
	    } else {
		mHttpTransport = new HttpTransport();
	    }

	    if (parserManager != null) {
		mParserManager = parserManager;
	    } else {
		mParserManager = new ParserManager();
	    }
	}

	public ServiceConfiguration() {
	    mHttpTransport = new HttpTransport();
	    mParserManager = new ParserManager();
	}

	public HttpTransport getHttpTransport() {
	    return mHttpTransport;
	}

	public ParserManager getParserManager() {
	    return mParserManager;
	}
    }

    private String getUrlCityByLocation(double lat, double lon) {
	return String.format(URLConstant.URL_CITY_BY_LOCATION,
		String.valueOf(lat), String.valueOf(lon));
    }

    public City getCityByLocation(double lat, double lon, Context context)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.getHttpTransport().executeGet(
		    getUrlCityByLocation(lat, lon));
	    if (is != null) {
		return mBuilder.getParserManager().getData(City.class, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private String getUrlUpdateService(String userId) {
	return String.format(URLConstant.URL_SEVICES, userId);
    }

    public WeatherService setServiceRating(Context context,
	    ServiceRating serviceRating) throws InternetException,
	    ParseException {
	checkConnectedAndReachable(context);
	WeatherService responseWeatherService = null;
	InputStream is = mBuilder.mHttpTransport.executePost(
		getUrlRating(serviceRating), null);
	try {
	    if (is != null) {
		responseWeatherService = mBuilder.getParserManager().getData(
			WeatherService.class, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
	return responseWeatherService;
    }

    public String getUrlRating(final ServiceRating serviceRating) {
	if (serviceRating.isVote()) {
	    return String.format(URLConstant.URL_RATING_SERVICE,
		    serviceRating.getServiceId(), String.valueOf(1));
	} else {
	    return String.format(URLConstant.URL_RATING_SERVICE,
		    serviceRating.getServiceId(), String.valueOf(0));
	}
    }

    public boolean updateService(Context context,
	    List<WeatherService> services, String userId)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	String data = mBuilder.mParserManager.weatherServicesToJson(services);
	InputStream is = null;
	try {
	    is = mBuilder.mHttpTransport.executePost(
		    getUrlUpdateService(userId), data);
	    Integer responceCode = checkResponceCode(is);
	    if (responceCode != null
		    && responceCode.intValue() == WebServiceResponseStatusCode.OK
			    .getCode()) {
		return true;
	    }
	    return false;
	} finally {
	    closeStream(is);
	}
    }

    public City addFavouriteCity(Context context, City city, String userId)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    String data = mBuilder.mParserManager.getShortCityToJson(city);
	    is = mBuilder.mHttpTransport.executePut(
		    String.format(URLConstant.URL_CITIES, userId), data);
	    if (is != null) {
		return mBuilder.mParserManager.getData(City.class, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private Integer checkResponceCode(InputStream is) {
	if (is != null) {
	    String response = Util.inputStreamToString(is);
	    if (response != null) {
		try {
		    Integer recponseCode = Integer.valueOf(response);
		    if (recponseCode != null) {
			return recponseCode;
		    }
		} catch (NumberFormatException e) {
		    Log.e(TAG, e.getMessage());
		    return null;
		}
	    }
	}
	return null;
    }

    public boolean updateFavoriteCity(Context context, List<City> cities,
	    String userId) throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    String data = mBuilder.mParserManager.citiesToJson(cities);
	    is = mBuilder.mHttpTransport.executePost(
		    String.format(URLConstant.URL_CITIES, userId), data);
	    Integer responceCode = checkResponceCode(is);
	    if (responceCode != null
		    && responceCode.intValue() == WebServiceResponseStatusCode.OK
			    .getCode()) {
		return true;
	    }
	} catch (InternetException ex) {
	    throw new ParseException(ErrorType.INCORRECT_DATA);
	} finally {
	    closeStream(is);
	}
	return false;
    }

    public void removeCity(Context context, List<City> cities, String userId)
	    throws InternetException, ParseException {
	if (updateFavoriteCity(context, cities, userId)) {
	    for (City city : cities) {
		city.delete();
	    }
	}
    }

    public String getUrlUserCityies(String userId) {
	return String.format(URLConstant.URL_CITIES, userId);
    }

    public final List<City> loadUserCity(Context context, String userId)
	    throws InternetException, ParseException {
	if (userId == null) {
	    return null;
	}
	checkConnectedAndReachable(context);
	InputStream is = null;
	is = mBuilder.getHttpTransport().executeGet(getUrlUserCityies(userId));
	if (is != null) {
	    try {
		Type collectionType = new TypeToken<List<City>>() {
		}.getType();
		return mBuilder.mParserManager.getData(collectionType, is);
	    } finally {
		closeStream(is);
	    }
	} else {
	    throw new ParseException(ErrorType.INCORRECT_DATA);
	}
    }

    public List<ForecastResponse> loadForecast(Context context, String userId,
	    long uDate) throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.mHttpTransport.executeGet(getUrlForecastByDate(
		    userId, uDate));
	    if (is != null) {
		Type collectionType = new TypeToken<List<ForecastResponse>>() {
		}.getType();
		return mBuilder.mParserManager.getData(collectionType, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private String getUrlForecastByDate(String userId, long uDate) {
	return String.format(URLConstant.URL_FORECAST_BY_DATE, userId,
		uDate / 1000);
    }

    public List<ForecastResponse> loadForecastByPeriod(Context context,
	    String userId, long uStartDate, long uEndDate)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.getHttpTransport().executeGet(
		    getUrlForecastByPeriod(userId, uStartDate, uEndDate));
	    if (is != null) {
		Type collectionType = new TypeToken<List<ForecastResponse>>() {
		}.getType();
		return mBuilder.mParserManager.getData(collectionType, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private String getUrlForecastByPeriod(String userId, long uStartDate,
	    long uEndDate) {
	return String.format(URLConstant.URL_FORECAST_BY_PERIOD,
		uStartDate / 1000, uEndDate / 1000, userId);
    }

    public List<ForecastResponse> loadForecast(Context context, String cityId,
	    String serviceId, long uDate) throws InternetException,
	    ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.mHttpTransport
		    .executeGet(getUrlForecastByCityByServiceByDate(cityId,
			    serviceId, uDate));
	    if (is != null) {
		Type collectionType = new TypeToken<List<ForecastResponse>>() {
		}.getType();
		return mBuilder.mParserManager.getData(collectionType, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private String getUrlForecastByCityByServiceByDate(String cityId,
	    String serviceId, long uDate) {
	return String.format(URLConstant.URL_FORECAST, cityId, serviceId,
		uDate / 1000);
    }

    private String getUrlActualForecat(String userId, long uDate) {
	return String.format(URLConstant.URL_ACTUAL_FORECAST_DATA, userId,
		uDate);
    }

    public List<ForecastResponse> getActualForecastByDate(Context context,
	    String userId, long uDate) throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.getHttpTransport().executeGet(
		    getUrlActualForecat(userId, uDate));
	    if (is != null) {
		Type collectionType = new TypeToken<List<ForecastResponse>>() {
		}.getType();
		return mBuilder.mParserManager.getData(collectionType, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    public boolean isWebServiceReachable(Context context) {
	HttpTransport httpTransport = new HttpTransport();

	try {
	    return httpTransport.codeReachableUrlQuery(URLConstant.SERVER_HOST) == HttpStatus.SC_OK;
	} catch (InternetException e) {
	    Log.e(TAG, e.getMessage());
	    return false;
	}
    }

    // public boolean isWebServiceReachable(Context context) {
    // try {
    // return isURLReachable(context);
    // if (InetAddress.getByName(URLConstant.SERVER_HOST).isReachable(
    // HttpTransport.CONNECTION_TIMEOUT)) {
    // return true;
    // }
    // } catch (UnknownHostException e) {
    // Log.e(TAG, e.getMessage());
    // } catch (IOException e) {
    // Log.e(TAG, e.getMessage());
    // }
    // return false;
    // }

    public boolean isConnectedToInternet(final Context context) {
	if (context == null) {
	    return false;
	}
	ConnectivityManager cm = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	if (null != activeNetwork) {
	    return true;
	}
	return false;
    }

    public String getUrlCitySearcher(String searchCity) {
	return String.format(URLConstant.URL_SEARCH_CITY, searchCity, Locale
		.getDefault().getLanguage());
    }

    private String getUrlUserServices(String userId) {
	return String.format(URLConstant.URL_SEVICES, userId);
    }

    public void checkConnectedAndReachable(Context context)
	    throws InternetException {
	if (!isConnectedToInternet(context)) {
	    throw new InternetException(ErrorType.NO_INTERNET_CONNECTION);
	}

	if (!isWebServiceReachable(context)) {
	    throw new InternetException(ErrorType.HTTP_HOST_NOT_EVAILABLE);
	}
    }

    public List<WeatherService> getUserWeatherService(Context context,
	    String userId) throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.mHttpTransport.executeGet(getUrlUserServices(userId));
	    if (is != null) {
		Type collectionType = new TypeToken<List<WeatherService>>() {
		}.getType();
		return mBuilder.mParserManager.getData(collectionType, is);
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private String getUrlForecast(City city, WeatherService service, long uDate) {
	return String.format(URLConstant.URL_FORECAST, city.getObjectId(),
		service.getObjectId(), uDate);
    }

    public List<City> searchCities(final Context context, String searchValue)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	if (searchValue != null && !searchValue.trim().isEmpty()) {
	    InputStream is = null;
	    try {
		is = getBuilder().getHttpTransport().executeGet(
			getUrlCitySearcher(mBuilder.mHttpTransport
				.getEncodeUrl(searchValue)));
		if (is != null) {
		    Type collectionType = new TypeToken<List<City>>() {
		    }.getType();
		    return mBuilder.mParserManager.getData(collectionType, is);
		} else {
		    throw new ParseException(ErrorType.INCORRECT_DATA);
		}
	    } finally {
		closeStream(is);
	    }
	} else {
	    return new ArrayList<City>();
	}
    }

    private void closeStream(InputStream is) {
	if (is != null) {
	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    public boolean updateUser(Context context, User user)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;

	is = mBuilder.getHttpTransport().executePost(
		URLConstant.URL_REGISTER_USER,
		mBuilder.getParserManager().userToJson(user));
	if (is != null) {
	    Integer responceCode = checkResponceCode(is);
	    if (responceCode != null
		    && responceCode.intValue() == WebServiceResponseStatusCode.OK
			    .getCode()) {
		return true;
	    }
	}
	return false;
    }

    public User registerUser(Context context, User user)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	InputStream is = null;
	try {
	    is = mBuilder.getHttpTransport().executePut(
		    URLConstant.URL_REGISTER_USER,
		    mBuilder.getParserManager().userToJson(user));
	    if (is != null) {
		User userResponce = mBuilder.getParserManager().getData(
			User.class, is);
		if (userResponce != null) {
		    return userResponce;
		}
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
	return null;
    }

//    public List<ForecastResponse> getForecasts(Context context, City city,
//	    WeatherService serice) throws InternetException, ParseException {
//	checkConnectedAndReachable(context);
//	InputStream is = null;
//	try {
//	    is = mBuilder.mHttpTransport
//		    .executeGet(getUrlForecast(city, serice));
//	    if (is != null) {
//		Type collectionType = new TypeToken<List<ForecastResponse>>() {
//		}.getType();
//		return mBuilder.mParserManager.getData(collectionType, is);
//	    } else {
//		throw new ParseException(ErrorType.INCORRECT_DATA);
//	    }
//	} finally {
//	    closeStream(is);
//	}
//    }

    public LocationCityForecasts loadCityWithForecastsByLocation(double lat,
	    double lon, String userId, Context context)
	    throws InternetException, ParseException {
	checkConnectedAndReachable(context);
	LocationCityForecasts locationCityForecasts = null;
	InputStream is = null;
	try {
	    is = mBuilder.getHttpTransport().executeGet(
		    getUrlCityWithForecastByLocation(lat, lon, userId));
	    if (is != null) {
		locationCityForecasts = mBuilder.getParserManager().getData(
			LocationCityForecasts.class, is);
		return locationCityForecasts;
	    } else {
		throw new ParseException(ErrorType.INCORRECT_DATA);
	    }
	} finally {
	    closeStream(is);
	}
    }

    private String getDetailForecatUrl(long date, String serviceId,
	    String cityId) {
	return String.format(URLConstant.URL_DETAIL_FORECAST, cityId,
		serviceId, date);
    }

    public ForecastResponse getForecast(long date, String serviceId,
	    String cityId) throws InternetException, ParseException {
	InputStream is = null;
	Log.d(TAG, getDetailForecatUrl(date, serviceId, cityId));
	is = mBuilder.getHttpTransport().executeGet(
		getDetailForecatUrl(date, serviceId, cityId));
	if (is != null) {
	    try {
		return mBuilder.mParserManager.getData(ForecastResponse.class,
			is);
	    } finally {
		closeStream(is);
	    }
	} else {
	    throw new ParseException(ErrorType.INCORRECT_DATA);
	}
    }

    private String getUrlCityWithForecastByLocation(double lat, double lon,
	    String userId) {
	return String.format(URLConstant.URL_CITY_WITH_FARECASTS_BY_LOCATION,
		String.valueOf(lat), String.valueOf(lon), userId);
    }
}

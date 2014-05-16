package com.weatheraggregator.app.test.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.common.base.Preconditions;
import com.weatheraggregator.activity.MainActivity_;
import com.weatheraggregator.app.test.utils.Utils;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.LocationCityForecasts;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.model.ForecastResponse;
import com.weatheraggregator.parser.ParserManager;
import com.weatheraggregator.webservice.HttpTransport;
import com.weatheraggregator.webservice.ServiceManager;
import com.weatheraggregator.webservice.ServiceManager.ServiceConfiguration;
import com.weatheraggregator.webservice.URLConstant;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

public class WebServiceTest extends
	ActivityInstrumentationTestCase2<MainActivity_> {

    // private ServiceManager mServiceManager;

    public WebServiceTest() {
	super(MainActivity_.class);
    }

    public WebServiceTest(Class<MainActivity_> activityClass) {
	super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	getActivity();
	MockitoAnnotations.initMocks(this);
	// initData();
	Utils.writeLogCat(WebServiceTest.class.getSimpleName() + " setUp");
    }

    // /**
    // * Initializes private variables;
    // */
    // private void initData()
    // {
    // HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
    // ParserManager parserManager = new ParserManager();
    // ParserManager spyParserManager = Mockito.spy(parserManager);
    // ServiceConfiguration builder = new
    // ServiceConfiguration(mockHttpTransport, spyParserManager);
    // ServiceManager mServiceManager =
    // ServiceManager.newInstance().init(builder);
    // }

    @Override
    protected void tearDown() throws Exception {
	Utils.writeLogCat(WebServiceTest.class.getSimpleName() + " tearDown");
	super.tearDown();
    }

    @MediumTest
    public void testRegistrationNullDataUser() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	try {
	    Mockito.when(
		    mServiceManager.getBuilder().getHttpTransport()
			    .executePut(URLConstant.URL_REGISTER_USER, null))
		    .thenReturn(null);
	    Mockito.when(
		    mServiceManager.getBuilder().getParserManager()
			    .userToJson(null)).thenReturn(null);

	    mServiceManager.registerUser(getActivity(), null);

	    Mockito.verify(mServiceManager.getBuilder().getParserManager())
		    .userToJson(null);
	    Mockito.verify(mServiceManager.getBuilder().getHttpTransport())
		    .executePut(URLConstant.URL_REGISTER_USER, null);
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	}

    }

    @MediumTest
    public void testRegistrationInvalidData() {
	User user = new User();
	user.setName(null);
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	try {
	    mServiceManager.registerUser(getActivity(), user);
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	}
    }

    @MediumTest
    public void testRegistrationValidDataUser() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	User user = new User();
	user.setName("Test user");
	String json = mServiceManager.getBuilder().getParserManager()
		.userToJson(user);
	InputStream is = com.weatheraggregator.util.Util
		.stringToInputStream(json);
	try {
	    Mockito.when(
		    mServiceManager.getBuilder().getHttpTransport()
			    .executePut(URLConstant.URL_REGISTER_USER, json))
		    .thenReturn(is);
	    User userResponse = mServiceManager.registerUser(getActivity(),
		    user);
	    Mockito.verify(mServiceManager.getBuilder().getHttpTransport())
		    .executePut(URLConstant.URL_REGISTER_USER, json);
	    Preconditions.checkNotNull(userResponse,
		    "User not registered, because response is null");
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		// expected error
	    }
	}
    }

    @SmallTest
    public void testLoadUserCity() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	List<City> cities = new ArrayList<City>();
	for (int i = 0; i < 4; i++) {
	    City city = new City();
	    city.setName("City " + i);
	    cities.add(city);
	}
	String json = mServiceManager.getBuilder().getParserManager()
		.citiesToJson(cities);
	InputStream is = com.weatheraggregator.util.Util
		.stringToInputStream(json);
	final String userId = "123456789";
	try {
	    Mockito.when(
		    mServiceManager
			    .getBuilder()
			    .getHttpTransport()
			    .executeGet(
				    mServiceManager.getUrlUserCityies(userId)))
		    .thenReturn(is);
	    List<City> citiesResponse = mServiceManager.loadUserCity(
		    getActivity().getApplicationContext(), userId);
	    Mockito.verify(mServiceManager.getBuilder().getHttpTransport())
		    .executeGet(mServiceManager.getUrlUserCityies(userId));
	    Preconditions.checkNotNull(citiesResponse,
		    "Cities not loaded, because response is null");
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	}
    }

    @SmallTest
    public void testInvalidLoadUserCity() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	try {
	    Mockito.when(
		    mServiceManager
			    .getBuilder()
			    .getHttpTransport()
			    .executeGet(mServiceManager.getUrlUserCityies(null)))
		    .thenReturn(null);
	    List<City> listCities = mServiceManager.loadUserCity(getActivity()
		    .getApplicationContext(), null);
	    if (listCities != null) {
		fail("Response must be null");
	    }
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	}
    }

    @MediumTest
    public void testSearchCity() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	List<City> cities = new ArrayList<City>();
	for (int i = 0; i < 4; i++) {
	    City city = new City();
	    city.setName("City " + i);
	    cities.add(city);
	}
	String json = mServiceManager.getBuilder().getParserManager()
		.citiesToJson(cities);
	InputStream is = com.weatheraggregator.util.Util
		.stringToInputStream(json);
	final String searchText = "New Yourk";
	final String encodeSearchText = Uri.encode(searchText);
	Mockito.when(
		mServiceManager.getBuilder().getHttpTransport()
			.getEncodeUrl(searchText)).thenReturn(encodeSearchText);
	final String searchUrl = mServiceManager
		.getUrlCitySearcher(encodeSearchText);
	try {
	    Mockito.when(
		    mServiceManager.getBuilder().getHttpTransport()
			    .executeGet(searchUrl)).thenReturn(is);
	    List<City> citiesResponse = mServiceManager.searchCities(
		    getActivity().getApplicationContext(), searchText);
	    Mockito.verify(mServiceManager.getBuilder().getHttpTransport())
		    .executeGet(searchUrl);
	    Preconditions.checkNotNull(citiesResponse,
		    "Cities not searched, because response is null");
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		// expected error
	    }
	}
    }

    @SmallTest
    public void testSendValidRating() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = Mockito.mock(ParserManager.class);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, parserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	ServiceRating serviceRating = getValidServiceRating();
	String responseJson = mServiceManager.getBuilder().getParserManager()
		.weatherServiceToJson(getValidWeatherService());
	InputStream responseStream = com.weatheraggregator.util.Util
		.stringToInputStream(responseJson);
	try {
	    final String url = mServiceManager.getUrlRating(serviceRating);
	    Mockito.when(
		    mServiceManager.getBuilder().getHttpTransport()
			    .executePost(url, null)).thenReturn(responseStream);
	    WeatherService weatherService = mServiceManager.setServiceRating(
		    getActivity(), serviceRating);
	    Preconditions.checkNotNull(weatherService,
		    "Weather service not update, because response is null");
	    Mockito.verify(mServiceManager.getBuilder().getHttpTransport()
		    .executePost(url, null));
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	} finally {
	    if (responseStream != null) {
		try {
		    responseStream.close();
		} catch (IOException e) {
		    // expected error
		}
	    }
	}
    }

    @SmallTest
    public void testSendInvalidRating() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	ServiceRating serviceRating = getInvalidServiceRating();
	try {
	    Mockito.when(
		    mServiceManager
			    .getBuilder()
			    .getHttpTransport()
			    .executePost(
				    mServiceManager.getUrlRating(serviceRating),
				    null)).thenReturn(null);
	    WeatherService weatherService = mServiceManager.setServiceRating(
		    getActivity(), serviceRating);
	    Mockito.verify(mServiceManager
		    .getBuilder()
		    .getHttpTransport()
		    .executePost(mServiceManager.getUrlRating(serviceRating),
			    null));
	    if (weatherService != null) {
		fail("Test is fail, because response is incorrect. Response must be null");
	    }
	} catch (InternetException e) {
	    // expected error
	} catch (ParseException e) {
	    // expected error
	}
    }

    private ServiceRating getValidServiceRating() {
	ServiceRating serviceRating = new ServiceRating();
	serviceRating.setServiceId("dd622061-bbae-482b-ae51-ba84a85b6311");
	serviceRating.setVote(true);
	serviceRating.setDate(new Date());
	return serviceRating;
    }

    private ServiceRating getInvalidServiceRating() {
	ServiceRating serviceRating = new ServiceRating();
	serviceRating.setServiceId(null);
	serviceRating.setVote(false);
	serviceRating.setDate(null);
	return serviceRating;
    }

    private WeatherService getValidWeatherService() {
	WeatherService weatherService = new WeatherService();
	return weatherService;
    }

    @LargeTest
    private void testSendValidLocation() {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parserManager = new ParserManager();
	ParserManager spyParserManager = Mockito.spy(parserManager);
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, spyParserManager);
	ServiceManager mServiceManager = ServiceManager.newInstance().init(
		builder);
	final String url = "http:\\test.com";
	final LocationCityForecasts responseData = getLocationCityForecasts();
	final String responseJson = new ParserManager()
		.locationCityForecastsToJson(responseData);
	InputStream is = null;
	try {
	    is = com.weatheraggregator.util.Util
		    .stringToInputStream(responseJson);
	    Mockito.when(
		    builder.getParserManager().getData(
			    LocationCityForecasts.class, is)).thenReturn(
		    responseData);
	    Mockito.when(builder.getHttpTransport().executeGet(url))
		    .thenReturn(is);
	    LocationCityForecasts locationCityForecasts = mServiceManager
		    .loadCityWithForecastsByLocation(38.123456, 42.123456,
			    "dd622061-bbae-482b-ae51-ba84a85b6311",
			    getInstrumentation().getContext());
	    Mockito.verify(builder.getHttpTransport().executeGet(url));
	    Preconditions.checkNotNull(locationCityForecasts,
		    "Response is null");
	    Preconditions.checkNotNull(locationCityForecasts.getCurrentCity(),
		    "Response city is null");
	    if (!locationCityForecasts.getCurrentCity().getObjectId()
		    .equals("0000000-0000-0000-0000-000000000000")) {
		fail("Response city has incorrect objectId");
	    }
	} catch (InternetException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	} finally {
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private LocationCityForecasts getLocationCityForecasts() {
	LocationCityForecasts locationCityForecasts = new LocationCityForecasts();

	City city = new City();
	city.setName("Kremenchuk");
	city.setObjectId("0000000-0000-0000-0000-000000000000");
	locationCityForecasts.setCurrentCity(city);

	List<ForecastResponse> weatherList = new ArrayList<ForecastResponse>();
	for (int i = 0; i < 2; i++) {
	    ForecastResponse w = new ForecastResponse();
	    w.setServiceId("dd622061-bbae-482b-ae51-ba84a85b6311");
	    w.setCityId("dd622061-bbae-482b-ae51-ba84a85b6311");
	    w.setForecasts(getListForecast());
	}
//	locationCityForecasts.setWeatherList(weatherList);

	return locationCityForecasts;
    }

    private List<Forecast> getListForecast() {
	List<Forecast> listForecasts = new ArrayList<Forecast>();
	for (int i = 0; i < 5; i++) {
	    Forecast f = new Forecast();
	    f.setCityId("dd622061-bbae-482b-ae51-ba84a85b6311");
	    f.setObjectId("dd622061-bbae-482b-ae51-ba84a85b6311");
	    listForecasts.add(f);
	}
	return listForecasts;
    }

    private City getCity() {
	City city = new City();
	city.setCode(10);
	city.setName("City");
	city.setCountry("country");
	city.setRegion("region");
	return city;
    }

    @SmallTest
    public void testSendUserCity() throws InternetException, ParseException {
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parser = Mockito.mock(ParserManager.class);

	City city = getCity();
	ParserManager pm = new ParserManager();
	Mockito.when(
		mockHttpTransport.executePut("", pm.getShortCityToJson(city)))
		.thenReturn(
			com.weatheraggregator.util.Util.stringToInputStream(pm
				.getShortCityToJson(city)));
	ServiceConfiguration builder = new ServiceConfiguration(
		mockHttpTransport, parser);
	ServiceManager serviceManager = ServiceManager.newInstance().init(
		builder);

	City cityResult = serviceManager.addFavouriteCity(getActivity(), city,
		"0000000-0000-0000-0000-000000000000");
	assertEquals(true, cityResult != null);
    }

    public void testUpdateFavoriteCity() {

	// Todo: write test
	HttpTransport mockHttpTransport = Mockito.mock(HttpTransport.class);
	ParserManager parser = Mockito.mock(ParserManager.class);
    }

    private List<ForecastResponse> getResponceForecast() {
	List<ForecastResponse> list = new ArrayList<ForecastResponse>();
	for (int i = 0; i <= 5; i++) {
	    ForecastResponse item = new ForecastResponse();
	    item.setForecasts(getListForecast());
	    item.setCityId("0000000-0000-0000-0000-00000000000" + i);
	    item.setServiceId("0000000-0000-0000-0000-00000000000" + i);
	    list.add(item);
	}
	return list;
    }
}

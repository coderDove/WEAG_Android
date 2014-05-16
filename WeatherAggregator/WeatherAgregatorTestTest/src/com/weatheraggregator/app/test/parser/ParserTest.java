package com.weatheraggregator.app.test.parser;

import java.io.InputStream;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.common.base.Preconditions;
import com.weatheraggregator.activity.MainActivity_;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.parser.ParserManager;

public class ParserTest extends ActivityInstrumentationTestCase2<MainActivity_> {

    public ParserTest(Class<MainActivity_> activityClass) {
	super(activityClass);
    }

    public ParserTest() {
	super(MainActivity_.class);
    }

    @MediumTest
    public void testParserCity() throws Exception {
	City city = new City();
	city.setCode(10);
	city.setName("City Name");
	city.setCountry("Country");
	city.setOrder(1);
	city.setRegion("Regoin");
	city.setObjectId("d62e586c-110e-47b5-8bab-d9fe0b74ef68");
	city.setDelete(false);

	ParserManager pm = new ParserManager();
	Preconditions.checkNotNull(pm);

	String jsonCity = pm.cityToJson(city);
	Preconditions.checkNotNull(jsonCity);

	InputStream is = com.weatheraggregator.util.Util
		.stringToInputStream(jsonCity);
	City parsedCity = pm.getData(City.class, is);
	Preconditions.checkNotNull(parsedCity);

	assertEquals("Object Id not equals", true,
		city.getObjectId().equals(parsedCity.getObjectId()));

	assertEquals("Name not equals", true,
		city.getName().equals(parsedCity.getName()));

	assertEquals("Delete not equals", true,
		city.isDelete() == parsedCity.isDelete());

	assertEquals("Order not equals", true,
		city.getOrder() == parsedCity.getOrder());

	assertEquals("Region not equals", true,
		city.getRegion().equals(parsedCity.getRegion()));

	assertEquals("Code not equals", true,
		city.getCode() == parsedCity.getCode());
    }

    @MediumTest
    public void testParserForecast() throws Exception {
	Forecast forecast = new Forecast();
	forecast.setCurrent(false);
	forecast.setObjectId("00000000-0000-0000-0000-000000000000");
	forecast.setTemp(14);
	forecast.setMaxTemp(18);
	forecast.setMinTemp(10);
	forecast.setWindSpeed(Double.valueOf(4));
	forecast.setWindDirection(179);
	forecast.setMaxWindSpeed(Double.valueOf(4));
	forecast.setMinWindSpeed(Double.valueOf(3));
	forecast.setHumidity(Double.valueOf(50));
	forecast.setPrecipitation(Integer.valueOf(0));
	forecast.setMaxPressure(Double.valueOf(10));
	forecast.setMinPressure(Double.valueOf(0));
	forecast.setPressure(Double.valueOf(101300));
	forecast.setCondition(1);
	forecast.setCloudy(36);
	forecast.setRevision(95430);

	// String jsonData =
	// "{\n \"Id\":\"00000000-0000-0000-0000-000000000000\",\n \"IsCurrent\":false,\n \"Temp\":1,\n \"MaxTemp\":2,\n \"MinTemp\":3,\n \"WindDirection\":\"s/w\",\n \"WindSpeed\":4,\n \"MaxWindSpeed\":5,\n \"MinWindSpeed\":6,\n \"Humidity\":7,\n \"Precipitation\":8,\n \"Pressure\":9,\n \"MaxPressure\":10,\n \"MinPressure\":11,\n \"WeatherCondition\":12,\n \"Cloudiness\":0,\n \"Revision\":123\n}";
	String jsonData = "{\n \"Id\":\"00000000-0000-0000-0000-000000000000\",\"IsCurrent\":false,\"Temp\":14,\"MaxTemp\":18,\"MinTemp\":10, \"WindDirection\":179,\"WindSpeed\":4,\"MaxWindSpeed\":4,\"MinWindSpeed\":3,\"Humidity\":50,\"Precipitation\":0,\"Pressure\":101300,\"MaxPressure\":10,\"MinPressure\":0,\"WeatherCondition\":1,\"DateRequest\":\"2014-03-24T10:04:09+00:00\",\"UnixTimeStampDateRequest\":1395655449.6063535,\"DateForecast\":\"2014-03-24T05:00:00+00:00\",\"UnixTimeStampDateForecast\":1395637200.0, \"Cloudiness\":36, \"Sunrise\":\"2014-03-24T03:39:38+00:00\", \"UnixTimeStampSunrise\":1394854778.0, \"Sunset\":\"2014-03-24T16:05:22+00:00\", \"UnixTimeStampSunset\":1394899522.0,\"Revision\":95430\n}";

	ParserManager pm = new ParserManager();
	InputStream is = null;
	Forecast parsForecast = null;
	is = com.weatheraggregator.util.Util.stringToInputStream(jsonData);
	parsForecast = pm.getData(Forecast.class, is);

	Preconditions.checkNotNull(parsForecast);

	assertEquals("Cloudy not equals", true,
		forecast.getCloudy().intValue() == parsForecast.getCloudy()
			.intValue());

	assertEquals("Condition not equals", true,
		forecast.getCondition() == parsForecast.getCondition());

	assertEquals("MaxPressure not equals", true, forecast.getMaxPressure()
		.doubleValue() == parsForecast.getMaxPressure().doubleValue());

	assertEquals("MaxTemp not equals", true, forecast.getMaxTemp()
		.intValue() == parsForecast.getMaxTemp().intValue());

	assertEquals("MaxWindSeep not equals", true, forecast.getMaxWindSeep()
		.doubleValue() == parsForecast.getMaxWindSeep().doubleValue());

	assertEquals("MinPressure not equals", true, forecast.getMinPressure()
		.doubleValue() == parsForecast.getMinPressure().doubleValue());

	assertEquals("MinTemp not equals", true, forecast.getMinTemp()
		.intValue() == parsForecast.getMinTemp().intValue());

	assertEquals("MinWindSpeed not equals", true, forecast
		.getMinWindSpeed().doubleValue() == parsForecast
		.getMinWindSpeed().doubleValue());

	assertEquals("ObjectId not equals", true, forecast.getObjectId()
		.equals(parsForecast.getObjectId()));

	assertEquals("Precipitation not equals", true, forecast
		.getPrecipitation().doubleValue() == parsForecast
		.getPrecipitation().doubleValue());

	assertEquals("Pressure not equals", true, forecast.getPressure()
		.doubleValue() == parsForecast.getPressure().doubleValue());

	assertEquals("Revision not equals", true, forecast.getRevision()
		.intValue() == parsForecast.getRevision().intValue());

	assertEquals("WindDirection not equals", true, forecast
		.getWindDirection().intValue() == parsForecast
		.getWindDirection().intValue());
    }

    @MediumTest
    public void testParserUserData() throws Exception {
	User user = new User();
	user.setName("test@gmail.com");
	user.setPrecipitationUnit(1);
	user.setPressureUnit(2);
	user.setSpeedUnit(3);
	user.setTemperatureUnit(5);

	ParserManager pm = new ParserManager();
	String jsonUser = pm.userToJson(user);
	InputStream is = com.weatheraggregator.util.Util
		.stringToInputStream(jsonUser);

	User parserUser = pm.getData(User.class, is);
	assertEquals("User not equals", true, parserUser.equals(user));
    }

    @SmallTest
    public void testParserWeatherService() throws Exception {
	WeatherService weatherService = new WeatherService();
	weatherService.setObjectId("00000000-0000-0000-0000-000000000000");
	weatherService.setName("HamWeather");
	weatherService.setUrl("http://www.hamweather.com/");
	weatherService.setLogoUrl("http://www.hamweather.com/logo");
	weatherService.setDescription("description");
	weatherService.setOrder(3);
	weatherService.setDelete(false);
	weatherService.setFavorite(true);
	weatherService.setVotedFor(Integer.valueOf(2));
	weatherService.setVotedAgainst(Integer.valueOf(1));

	final String jsonData = "{ \"Id\": \"00000000-0000-0000-0000-000000000000\", \"Name\": \"HamWeather\", \"Url\": \"http://www.hamweather.com/\", \"LogoUrl\": \"http://www.hamweather.com/logo\", \"Description\": \"description\", \"ApiUrl\": \"http://api.aerisapi.com/\", \"OrderIndex\": 3, \"IsDelete\": false, \"IsFavorite\": true, \"VotedFor\": 2, \"VotedAgainst\": 1 }";
	ParserManager pm = new ParserManager();
	InputStream is = null;
	WeatherService parsWeatherService = null;
	try {
	    is = com.weatheraggregator.util.Util.stringToInputStream(jsonData);
	    parsWeatherService = pm.getData(WeatherService.class, is);
	    Preconditions.checkNotNull(parsWeatherService);

	    assertEquals("Name not equals", true, parsWeatherService.getName()
		    .equals(weatherService.getName()));
	    assertEquals("Url not equals", true, parsWeatherService.getUrl()
		    .equals(weatherService.getUrl()));
	    assertEquals("Logo url not equals", true, parsWeatherService
		    .getLogoUrl().equals(weatherService.getLogoUrl()));
	    assertEquals("Description not equals", true, parsWeatherService
		    .getDescription().equals(weatherService.getDescription()));
	    assertEquals("Order not equals", true,
		    parsWeatherService.getOrder() == weatherService.getOrder());
	    assertEquals("VotedFor not equals", true, parsWeatherService
		    .getVotedFor().intValue() == weatherService.getVotedFor()
		    .intValue());
	    assertEquals("VotedAgainst not equals", true, parsWeatherService
		    .getVotedAgainst().intValue() == weatherService
		    .getVotedAgainst().intValue());
	    assertEquals("isDelete not equals", true,
		    parsWeatherService.isDelete() == weatherService.isDelete());
	    assertEquals("isFavorite not equals", true,
		    parsWeatherService.isFavorite() == weatherService
			    .isFavorite());

	} finally {
	    if (is != null)
		is.close();
	}
    }
}

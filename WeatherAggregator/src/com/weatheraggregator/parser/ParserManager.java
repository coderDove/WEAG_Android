package com.weatheraggregator.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.LocationCityForecasts;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.model.ForecastResponse;
import com.weatheraggregator.model.UserForecastAllService;
import com.weatheraggregator.webservice.exception.ErrorType;
import com.weatheraggregator.webservice.exception.ParseException;

public class ParserManager {
    private static final String TAG = "ParserManager";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMAT_WITHOUT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";

    public final static String NAME = "Name";
    public final static String ID = "Id";
    public final static String PRECIPITATION_UNIT = "PresipitationUnit";
    public final static String REVISION = "Revision";
    public final static String WIND_SPEED_UNIT = "WindSpeedUnit";
    public final static String TEMP_UNIT = "TempUnit";
    public final static String PRESSURE_UNIT = "PressureUnit";
    public final static String CITY_ID = "CityId";
    public final static String COUNTRY = "Country";
    public final static String REGION = "Region";
    public final static String URL = "Url";
    public final static String SERVICE_ID = "ServiceId";
    public final static String USER_ID = "UserId";
    public final static String ORDER = "OrderIndex";
    public final static String IS_DELETE = "IsDelete";
    public final static String IS_FAVOURITE = "IsFavorite";
    public final static String POSTAL_CODE = "PostalCode";
    public final static String LATITUDE = "Latitude";
    public final static String LONGITUDE = "Longitude";
    public final static String DESCRIPTION = "Description";
    public final static String LOGO_URL = "LogoUrl";
    public final static String VOTED_FOR = "VotedFor";
    public final static String VOTED_AGAINST = "VotedAgainst";
    // public final static String REVISION = "Revision";
    private Gson gson;

    public ParserManager() {
	GsonBuilder builder = new GsonBuilder();
	builder.serializeNulls();
	builder.setDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
	// builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
	// {
	// public Date deserialize(JsonElement json, Type typeOfT,
	// JsonDeserializationContext context)
	// throws JsonParseException {
	// SimpleDateFormat isoFormat = new SimpleDateFormat(DATE_FORMAT);
	// isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	// try {
	// return isoFormat.parse(json.getAsJsonPrimitive()
	// .getAsString());
	// } catch (java.text.ParseException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	// });

	gson = builder.create();
    }

    public <T> T getData(Class<T> classOfT, InputStream stream)
	    throws ParseException {
	if (stream == null) {
	    throw new ParseException(new NullPointerException());
	}

	JsonElement jsonElement = parsData(stream);
	return gson.fromJson(jsonElement, classOfT);
    }

    private JsonElement parsData(InputStream stream) {
	Reader reader = new InputStreamReader(stream);
	JsonReader jreader = new JsonReader(reader);
	JsonParser parser = new JsonParser();
	JsonElement jsonElement = parser.parse(jreader);
	try {
	    reader.close();
	} catch (IOException e) {
	    Log.e(TAG, e.getMessage());
	}
	return jsonElement;
    }

    public String citiesToJson(List<City> cities) {
	JsonArray array = new JsonArray();
	for (City city : cities) {
	    array.add(getCity(city));
	}
	return array.toString();
    }

    private JsonObject getCity(City city) {
	if (city != null) {
	    JsonObject data = new JsonObject();
	    data.addProperty(NAME, city.getName());
	    if (city.getObjectId() != null
		    && !city.getObjectId().equals(City.DEFAULT_CITY_ID)) {
		data.addProperty(CITY_ID, city.getObjectId());
	    }
	    data.addProperty(REGION, city.getRegion());
	    data.addProperty(COUNTRY, city.getCountry());
	    data.addProperty(ORDER, city.getOrder());
	    data.addProperty(POSTAL_CODE, city.getCode());
	    data.addProperty(IS_DELETE, city.isDelete());
	    data.addProperty(LATITUDE, city.getLatitude());
	    data.addProperty(LONGITUDE, city.getLongitude());
	    return data;
	}
	return null;
    }

    public String cityToJson(City city) {
	JsonObject data = getCity(city);
	if (data == null) {
	    return null;
	}
	return data.toString();
    }

    public String getShortCityToJson(City city) {
	if (city != null) {
	    JsonObject data = new JsonObject();
	    data.addProperty(NAME, city.getName());
	    data.addProperty(REGION, city.getRegion());
	    data.addProperty(COUNTRY, city.getCountry());
	    data.addProperty(POSTAL_CODE, city.getCode());
	    return data.toString();
	}
	return null;
    }

    public String weatherServicesToJson(List<WeatherService> services) {
	if (services == null || services.isEmpty()) {
	    return null;
	}
	JsonArray array = new JsonArray();
	for (WeatherService weatherService : services) {
	    array.add(getWeatherService(weatherService));
	}
	return array.toString();
    }

    private JsonObject getWeatherService(WeatherService service) {
	JsonObject data = new JsonObject();
	data.addProperty(ID, service.getObjectId());
	data.addProperty(ORDER, service.getOrder());
	data.addProperty(IS_DELETE, service.isDelete());
	data.addProperty(IS_FAVOURITE, service.isFavorite());
	return data;
    }

    public String weatherServiceToJson(WeatherService weatherService) {
	if (weatherService != null) {
	    JsonObject data = new JsonObject();
	    data.addProperty(DESCRIPTION, weatherService.getDescription());
	    data.addProperty(LOGO_URL, weatherService.getLogoUrl());
	    data.addProperty(NAME, weatherService.getName());
	    data.addProperty(ORDER, weatherService.getOrder());
	    data.addProperty(URL, weatherService.getUrl());
	    data.addProperty(VOTED_FOR, weatherService.getVotedFor());
	    data.addProperty(VOTED_AGAINST, weatherService.getVotedAgainst());
	    return data.toString();
	}
	return null;
    }

    public String userToJson(User user) {
	if (user != null && user.getName() != null && !user.getName().isEmpty()) {
	    JsonObject data = new JsonObject();
	    if (user.getObjectId() != null && !user.getObjectId().isEmpty())
		data.addProperty(ID, user.getObjectId());
	    data.addProperty(NAME, user.getName());
	    data.addProperty(PRECIPITATION_UNIT, user.getPrecipitationUnit()
		    .getCode());
	    data.addProperty(PRESSURE_UNIT, user.getPressureUnit().getCode());
	    data.addProperty(WIND_SPEED_UNIT, user.getSpeedUnit().getCode());
	    data.addProperty(TEMP_UNIT, user.getTemperatureUnit().getCode());
	    return data.toString();
	}
	return null;
    }

    public String userForecastRequestToJson(
	    List<UserForecastAllService> listRequest) {
	JsonArray array = new JsonArray();
	if (listRequest != null) {
	    for (UserForecastAllService ufas : listRequest) {
		JsonObject data = new JsonObject();
		data.addProperty(CITY_ID, ufas.getCityId());
		data.addProperty(REVISION, ufas.getRevision());
		array.add(data);
	    }
	}
	return array.toString();
    }

    public <T> T getData(Type typeOfT, InputStream stream)
	    throws ParseException {
	if (stream == null) {
	    throw new ParseException(new NullPointerException());
	}
	JsonElement jsonElement = parsData(stream);
	return gson.fromJson(jsonElement, typeOfT);
    }

    public String locationCityForecastsToJson(
	    LocationCityForecasts locationCityForecasts) {
	return gson.toJson(locationCityForecasts);
    }

    private boolean peekValueIsNull(JsonReader reader) {
	try {
	    if (reader.peek() == JsonToken.NULL) {
		return true;
	    } else {
		return false;
	    }
	} catch (IOException e) {
	    Log.e(TAG, e.getMessage());
	    return false;
	}
    }
}
//public List<ForecastResponse> parseForecast(InputStream is)
//	    throws ParseException {
//	JsonReader reader = null;
//	try {
//	    reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
//	} catch (UnsupportedEncodingException e1) {
//	    Log.e(TAG, e1.getMessage());
//	    throw new ParseException(ErrorType.INCORRECT_DATA);
//	}
//	List<ForecastResponse> forecastResponses = new ArrayList<ForecastResponse>();
//	try {
//	    reader.beginArray();
//	    String cityId = null;
//	    String serviceId = null;
//	    ForecastResponse forecastResponse = new ForecastResponse();
//	    List<Forecast> forecasts = new ArrayList<Forecast>();
//	    while (reader.hasNext()) {
//		reader.beginObject();
//		while (reader.hasNext()) {
//		    String name = reader.nextName();
//		    if (name.equalsIgnoreCase(Forecast.P_CITY_ID)) {
//			cityId = reader.nextString();
//		    } else if (name.equalsIgnoreCase(Forecast.P_SERVICE_ID)) {
//			serviceId = reader.nextString();
//		    } else if (name
//			    .equalsIgnoreCase(ForecastResponse.P_FORECASTS)) {
//			reader.beginArray();
//			while (reader.hasNext()) {
//			    reader.beginObject();
//			    forecasts.add(parseForecast(reader, cityId,
//				    serviceId));
//			    reader.endObject();
//			}
//			reader.endArray();
//		    }
//		}
//		reader.endObject();
//		forecastResponse.setCityId(cityId);
//		forecastResponse.setServiceId(serviceId);
//		forecastResponse.setForecasts(forecasts);
//		forecastResponses.add(forecastResponse);
//	    }
//	    reader.endArray();
//	} catch (IOException e) {
//	    throw new ParseException(ErrorType.INCORRECT_DATA);
//	}
//	return forecastResponses;
//}
//
//private Forecast parseForecast(JsonReader reader, String cityId,
//	    String serviceId) throws ParseException {
//	Forecast forecast = new Forecast();
//	forecast.setCityId(cityId);
//	forecast.setServiceId(serviceId);
//	try {
//	    while (reader.hasNext()) {
//		String fieldName = reader.nextName();
//		if (fieldName.equalsIgnoreCase(Forecast.P_OBJECT_ID)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setObjectId(reader.nextString());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_IS_CURRENT)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setCurrent(reader.nextBoolean());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_TEMP)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setTemp(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_MAX_TEMP)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMaxTemp(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_MIN_TEMP)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMinTemp(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_MIN_TEMP)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMinTemp(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_MIN_TEMP)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMinTemp(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName
//			.equalsIgnoreCase(Forecast.P_WIND_DIRECTION)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setWindDirection(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_WIND_SPEED)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setWindSpeed(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName
//			.equalsIgnoreCase(Forecast.P_MAX_WIND_SPEED)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMaxWindSpeed(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName
//			.equalsIgnoreCase(Forecast.P_MIN_WIND_SPEED)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMinWindSpeed(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_HUMIDITY)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setHumidity(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_PRESSURE)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setPressure(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_MAX_PRESSURE)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMaxPressure(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_MIN_PRESSURE)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setMinPressure(reader.nextDouble());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_CONDITION)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setCondition(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName
//			.equalsIgnoreCase(Forecast.P_UNIX_REQUEST_DAY)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setRequestDay(new Date((long) reader
//				.nextDouble() * 1000));
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName
//			.equalsIgnoreCase(Forecast.P_UNIX_FORECAST_DAY)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setForecastDay(new Date((long) reader
//				.nextDouble() * 1000));
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_CLOUDY)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setCloudy(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else if (fieldName.equalsIgnoreCase(Forecast.P_REVISION)) {
//		    if (!peekValueIsNull(reader)) {
//			forecast.setRevision(reader.nextInt());
//		    } else {
//			reader.skipValue();
//		    }
//		} else {
//		    reader.skipValue();
//		}
//	    }
//	} catch (IOException e) {
//	    throw new ParseException(e, ErrorType.INCORRECT_DATA);
//	}
//	return forecast;
//}
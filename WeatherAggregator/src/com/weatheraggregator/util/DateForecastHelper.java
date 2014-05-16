package com.weatheraggregator.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.provider.BaseColumns;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.model.ForecastInfoKey;

public class DateForecastHelper {
    /**
     * Save data to cache which using for displaying forecasts
     * 
     * @param month
     *            - occur selection forecast
     */
    public void saveForecastToCache(Date month) {
	List<Forecast> result = this.getActualForecastByDate(month);
	for (int indexForecast = 0; indexForecast < result.size(); indexForecast++) {
	    Forecast forecast = result.get(indexForecast);
	    WeatherCache.getInstance().put(
		    new ForecastInfoKey(forecast.getCityId(),
			    forecast.getServiceId(),
			    DateHelper.clearTime(forecast.getForecastDay())),
		    forecast);
	}
    }

    // Load forecasts by city and service. It's result method where caching
    // forecasts data
    private int mIndex;

    private List<Forecast> getForecasts(City city, Date firstDateOfMonth,
	    Date lastDateOfMonth, WeatherService weatherService) {
	List<Forecast> listResultForecast = new ArrayList<Forecast>();
	List<Forecast> listOfForecast = SQLiteUtils.rawQuery(
		Forecast.class,
		ConstantQuery.Q_FORECAST_FOR_CASHE,
		new String[] { city.getObjectId(),
			weatherService.getObjectId(),
			String.valueOf(firstDateOfMonth.getTime()),
			String.valueOf(lastDateOfMonth.getTime()) });
	if (listOfForecast != null && !listOfForecast.isEmpty()) {
	    Date now = new Date();
	    for (mIndex = 0; mIndex < listOfForecast.size(); mIndex++) {
		Forecast forecast = listOfForecast.get(mIndex);
		Date dateForecat = forecast.getForecastDay();
		Forecast resultForecast = null;
		if (!DateHelper.equalsIgnoreTime(now, dateForecat)) {
		    resultForecast = getForecastNearTwelve(getForecastsByDate(
			    dateForecat, listOfForecast, mIndex));
		} else {
		    resultForecast = getForecastNearCurrentTime(getForecastsByDate(
			    dateForecat, listOfForecast, mIndex));
		}
		listResultForecast.add(resultForecast);
	    }
	    return listResultForecast;
	}
	return null;
    }

    private List<Forecast> getForecastsByDate(Date forecastDate,
	    List<Forecast> forecasts, int forecatIndex) {
	List<Forecast> listDayForecasts = new ArrayList<Forecast>();
	int index = forecatIndex--;
	for (; index < forecasts.size(); index++) {
	    Forecast forecast = forecasts.get(index);
	    if (DateHelper.equalsIgnoreTime(forecastDate,
		    forecast.getForecastDay())) {
		listDayForecasts.add(forecast);
	    } else {
		break;
	    }
	    forecatIndex++;
	}
	mIndex = forecatIndex;
	return listDayForecasts;
    }

    private Forecast getForecastNearTwelve(List<Forecast> forecasts) {
	if (forecasts != null && !forecasts.isEmpty()) {

	    Forecast result = forecasts.get(0);
	    Date twelve = DateHelper.getTwelve(result.getForecastDay());
	    long min = Long.MAX_VALUE;
	    for (Forecast forecast : forecasts) {
		long value = (int) Math.abs(forecast.getForecastDay().getTime()
			- twelve.getTime());
		if (min > value) {
		    min = value;
		    result = forecast;
		}
	    }
	    return result;
	}
	return null;
    }

    private List<City> getCities() {
	return new Select(City.F_NAME, BaseColumns._ID, City.F_OBJECT_ID)
		.from(City.class)
		.where(String.format("%s=0", City.F_IS_DELETED)).execute();
    }

    private List<WeatherService> getWeatherServices() {
	return new Select().from(WeatherService.class).execute();
    }

    /**
     * Return actual forecast by month
     * 
     * @param forecastMonth
     *            -occur sampling forecast
     * @return actual forecasts for cities and weather service
     */

    public List<Forecast> getActualForecastByDate(Date forecastMonth) {
	Date firstDateOfMonth = DateHelper
		.getFirstDateOfMonthByDate(forecastMonth);
	Date lastDateOfMonth = DateHelper
		.getLastDateOfMonthByDate(forecastMonth);

	// Date firstDateOfMonth = DateHelper.getFirstDateOfWeek(forecastMonth);
	// Date lastDateOfMonth = DateHelper.getLastDateOfWeek(forecastMonth);

	List<City> cities = getCities();

	List<WeatherService> listServices = getWeatherServices();

	List<Forecast> cache = new ArrayList<Forecast>();

	if (listServices != null && !listServices.isEmpty()) {
	    for (int serviceIndex = 0; serviceIndex < listServices.size(); serviceIndex++) {
		WeatherService weatherService = listServices.get(serviceIndex);

		if (cities != null && !cities.isEmpty()) {
		    for (int cityIndex = 0; cityIndex < cities.size(); cityIndex++) {
			City city = cities.get(cityIndex);
			List<Forecast> cacheResult = getForecasts(city,
				firstDateOfMonth, lastDateOfMonth,
				weatherService);
			if (cacheResult != null)
			    cache.addAll(cacheResult);
		    }
		}
	    }
	}
	return cache;
    }

    /**
     * 
     * @param forecasts
     *            - All forecasts by period
     * @return actual forecast
     */
    private Forecast getForecastNearCurrentTime(List<Forecast> forecasts) {
	if (forecasts != null && !forecasts.isEmpty()) {
	    Forecast result = forecasts.get(0);
	    Date now = new Date();
	    long min = Long.MAX_VALUE;
	    for (Forecast forecast : forecasts) {
		long value = (int) Math.abs(forecast.getForecastDay().getTime()
			- now.getTime());
		if (min > value) {
		    min = value;
		    result = forecast;
		}
	    }
	    return result;
	}
	return null;
    }
}

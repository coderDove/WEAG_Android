package com.weatheraggregator.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.weatheraggregator.events.EventUpdateForecast;
import com.weatheraggregator.localservice.ExecuteScheduledService;

import de.greenrobot.event.EventBus;

public class ForecastInfoManager {
    public final static String TAG = "DateForecastInfoManager";
    private volatile static ForecastInfoManager sSingleton;
    private ScheduledExecutorService scheduledService;
    private ConcurrentHashMap<Integer, Integer> loadingWeek;

    public synchronized static ForecastInfoManager getInstance() {
	if (null == sSingleton) {
	    sSingleton = new ForecastInfoManager();
	}
	return sSingleton;
    }

    private ForecastInfoManager() {
	loadingWeek = new ConcurrentHashMap<Integer, Integer>();
	scheduledService = Executors.newSingleThreadScheduledExecutor();
    }

    public boolean isLoading(Date date) {
	if (loadingWeek != null) {
	    int week = getWeekOfYear(date);
	    Log.e(TAG, "isLoading" + week);
	    return loadingWeek.containsKey(getWeekOfYear(date));
	}
	return false;
    }

    public void remove(Date date) {
	int week = getWeekOfYear(date);
	Log.e(TAG, "remove" + week);
	loadingWeek.remove(week);
    }

    public void cleareCache() {
	loadingWeek.clear();
    }

    private Integer getWeekOfYear(Date date) {
	Calendar calender = Calendar.getInstance();
	calender.setTime(date);
	Integer weekOfyear = calender.get(Calendar.WEEK_OF_YEAR);
	return weekOfyear;
    }

    public void addLoad(Date date) {
	Integer weekOfyear = getWeekOfYear(date);
	loadingWeek.put(weekOfyear, weekOfyear);
	Log.e(TAG, "addLoad" + weekOfyear);
    }

    public synchronized void loadWeatherInfo() {
	loadDateWeatherInfo(new Date());
    }

    /**
     * Save actual forecast to cache
     * 
     * @param date
     *            - actual date
     */

    public synchronized void loadDateWeatherInfo(final Date date) {
	if (date == null) {
	    return;
	}
	Log.e(TAG, "loadWeatherInfo : start");
	scheduledService.submit(new Runnable() {
	    @Override
	    public void run() {
		Date currentDate = DateHelper.clearTime(DateHelper
			.getFirstDateOfMonthByDate(date));
		Integer weekOfYear = getWeekOfYear(currentDate);
		Integer week = loadingWeek.get(weekOfYear);
		if (week == null) {
		    addLoad(date);
		    DateForecastHelper helper = new DateForecastHelper();
		    helper.saveForecastToCache(date);
		    EventBus.getDefault().post(new EventUpdateForecast());
		}
	    }
	});
	//
	// if (Util.hasHoneycomb()) {
	// new TaskDateLoadWeatherInfo().executeOnExecutor(
	// scheduledService, date);
	// } else {
	// new TaskDateLoadWeatherInfo().execute(date);
	// }
    }

    /**
     * Task loaded cache of forecast
     * 
     * @param month
     */
    private static class TaskDateLoadWeatherInfo extends
	    AsyncTask<Date, Void, Void> {

	@Override
	protected void onPostExecute(Void result) {
	    super.onPostExecute(result);
	    Log.e(TAG, "TaskWeatherInfo");
	    EventBus.getDefault().post(new EventUpdateForecast());
	}

	@Override
	protected Void doInBackground(Date... params) {
	    Date date = params[0];
	    DateForecastHelper helper = new DateForecastHelper();
	    helper.saveForecastToCache(date);
	    return null;
	}
    }

    // private static class TaskHourLoadWeatherInfo extends
    // AsyncTask<Object, Void, Date> {
    //
    // @Override
    // protected void onPostExecute(Date result) {
    // super.onPostExecute(result);
    // Log.e(TAG, "TaskWeatherInfo");
    // // EventBus.getDefault().post(new EventUpdateHourForecast(result));
    // }
    //
    // @Override
    // protected Date doInBackground(Object... params) {
    // Date date = (Date) params[0];
    // Context context = (Context) params[1];
    //
    // HourForecastHelper helper = new HourForecastHelper();
    // List<Forecast> list = helper.getForecastsByMonth(date);
    // saveHourForecastsToCache(list);
    // if (list == null || list.size() == 0) {
    //
    // // ServiceManager sm = ServiceManager.newInstance();
    // // sm.loadForecastByPeriod(context, userId, uStartDate,
    // // uEndDate)
    //
    // }
    // return date;
    // }
    //
    // private void saveHourForecastsToCache(List<Forecast> forecasts) {
    // if (forecasts != null && forecasts.size() > 0) {
    // for (int location = 0; forecasts.size() > location; location++) {
    // Forecast forecast = forecasts.get(location);
    // HourWeatherCache.getInstance().put(
    // new ForecastInfoKey(forecast.getCityId(),
    // forecast.getServiceId(),
    // DateHelper.clearMinuteWithSecond(forecast
    // .getForecastDay())), forecast);
    // }
    // }
    // }
    // }
}

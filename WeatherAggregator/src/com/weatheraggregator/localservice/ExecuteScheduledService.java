package com.weatheraggregator.localservice;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import com.activeandroid.query.Select;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.interfaces.ISendBroadcast;
import com.weatheraggregator.model.ForecastResponse;
import com.weatheraggregator.webservice.ServiceManager;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

public class ExecuteScheduledService {
    public static final String TAG = "ExecuteScheduledService";

    private final ScheduledExecutorService scheduler = Executors
	    .newSingleThreadScheduledExecutor();

    @SuppressWarnings("rawtypes")
    private ScheduledFuture futureScheduler = null;
    private final int PERIOD = 10;// min;
    private ISendBroadcast mServiceSendBroadcast;

    public ExecuteScheduledService(ISendBroadcast remoteService) {
	mServiceSendBroadcast = remoteService;
    }

    public void stopScheduleExecutor() {
	if (futureScheduler != null)
	    futureScheduler.cancel(true);
    }

    public void startScheduleExecutor(final Context context) {
	stopScheduleExecutor();
	final Runnable timer = new Runnable() {
	    @Override
	    public void run() {
		RemoteServiceHelper serviceHelper = new RemoteServiceHelper();
		User user = new Select().from(User.class).executeSingle();
		try {
		    long unixTime = new Date().getTime()/1000;
		    List<ForecastResponse> forecasts = ServiceManager
			    .newInstance().getActualForecastByDate(context,
				    user.getObjectId(), unixTime);
		    if (forecasts != null && !forecasts.isEmpty()) {
			for (int indexForecast = 0; indexForecast <= forecasts
				.size() - 1; indexForecast++) {
			    serviceHelper.saveForecast(forecasts
				    .get(indexForecast));
			}
		    }
		} catch (InternetException e) {
		    Log.e(TAG, e.getMessage());
		} catch (ParseException e) {
		    Log.e(TAG, e.getMessage());
		}
		Log.e(TAG, "startScheduleExecutor");
		if (mServiceSendBroadcast != null) {
		    mServiceSendBroadcast.sendForecastUpdateBroadcast();
		}
	    }
	};
	futureScheduler = scheduler.scheduleAtFixedRate(timer, PERIOD, PERIOD,
		TimeUnit.MINUTES);
    }
}

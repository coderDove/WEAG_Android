package com.weatheraggregator.localservice;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.util.PreferenceMapper;

public class RemoteWeatherServiceManager {
    private final static String TAG = "RemoteWeatherServiceManager";
    private final int THREAD_POOL = 1;
    private boolean mBound = false;
    private IWeatherRemoteService serviceApi;
    private final ScheduledExecutorService scheduler = Executors
	    .newScheduledThreadPool(THREAD_POOL);
    private String userId;
    // TODO need consider time connection
    private final Object mMonitorBound = new Object();

    private ServiceConnection mConnection;

    private void createConnection() {
	mConnection = new ServiceConnection() {
	    @Override
	    public void onServiceConnected(ComponentName componentName,
		    IBinder service) {
		mBound = true;
		if (null == serviceApi) {
		    serviceApi = IWeatherRemoteService.Stub
			    .asInterface(service);
		}
		synchronized (mMonitorBound) {
		    mMonitorBound.notifyAll();
		}
	    }

	    @Override
	    public void onServiceDisconnected(ComponentName componentName) {
		mBound = false;
	    }
	};
    }

    private void prepareConnect() {
	synchronized (mMonitorBound) {
	    if (!mBound) {
		try {
		    mMonitorBound.wait();
		} catch (InterruptedException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	}
    }

    public void setRating(final ServiceRating rating,
	    final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		prepareConnect();
		if (mBound && null != serviceApi) {
		    try {
			if (rating != null && rating.getId() != null) {
			    serviceApi.sendServiceRating(rating.getId(),
				    callBack);
			}
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		    Log.d(TAG, "setRating");
		}
	    }
	});
    }

    public void userRegistration(final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		prepareConnect();
		if (mBound && null != serviceApi) {
		    try {
			serviceApi.userRegistration(callBack);
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		}
	    }
	});
    }

    public void bindService(Context context) {
	userId = PreferenceMapper.getUserId(context);
	if (!mBound) {
	    if (mConnection == null) {
		createConnection();
	    }

	    Intent intent = new Intent(context, WeatherRemoteService.class);
	    context.startService(intent);
	    context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	    Log.d(TAG, "bindService");
	}
    }

    public List<City> loadCities(final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.loadUserCity(callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
	return null;
    }

    public void loadWeatherServices(final IDataSourceServiceListener callBack) {
	scheduler.schedule(new Runnable() {
	    @Override
	    public void run() {
		prepareConnect();
		if (mBound && serviceApi != null) {
		    try {
			prepareInvokeMethods();
			serviceApi.loadWeatherService(callBack);
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		}

	    }
	}, 0, TimeUnit.SECONDS);
    }

    public void addToFavorite(final City city,
	    final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.addFavouriteCity(city, callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void reorderService(final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.reorderWeatherService(callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void setFavouriteService(final String newServiceId,
	    final String oldServiceId, final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.setFavouriteWeatherService(newServiceId,
			    oldServiceId, callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void reorderCity(final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.reorderCity(callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void loadActualForecast(final IDataSourceServiceListener callBack,
	    final Date date) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.loadActualForecast(callBack, date.getTime());
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void removeCityFromFavourite(final City city,
	    final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.removeFavouriteCity(city.getObjectId(), callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    private boolean prepareInvokeMethods() {
	prepareConnect();
	if (mBound && serviceApi != null) {
	    return true;
	}
	return false;
    }

    public void updateWeatherService(final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.updateWeatherService(callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void updateUser(final IDataSourceServiceListener callBack) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		prepareConnect();
		if (mBound && serviceApi != null) {
		    try {
			serviceApi.updateUser(callBack);
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		}
	    }
	});
    }

    public void loadForecastByDate(final Date date,
	    final IDataSourceServiceListener callBack) {
	if (date != null) {
	    scheduler.execute(new Runnable() {
		@Override
		public void run() {
		    try {
			prepareInvokeMethods();
			serviceApi.loadForecastByDate(date.getTime(), callBack);
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		}
	    });
	}
    }

    public void loadForecastByPeriod(final Date startDate, final Date endDate,
	    final IDataSourceServiceListener callBacks) {
	if (startDate != null && endDate != null) {
	    scheduler.execute(new Runnable() {
		@Override
		public void run() {
		    try {
			prepareInvokeMethods();
			serviceApi.loadForecastByPeriod(startDate.getTime(),
				endDate.getTime(), callBacks);
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		}
	    });
	}
    }

    public void registerUserWithLoadingData(
	    final IDataSourceServiceListener callBack, final Context context) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.registerUserWithLoadingData(callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void unbindService(Context context) {
	if (mBound) {
	    mBound = false;
	    context.unbindService(mConnection);
	}
    }

    public void syncUserData(final IDataSourceServiceListener callBak) {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		prepareConnect();
		if (mBound && serviceApi != null) {
		    if (userId != null) {
			try {
			    serviceApi.synUserData(callBak);
			} catch (RemoteException e) {
			    Log.e(TAG, e.getMessage());
			}
		    }
		}
	    }
	});
    }

    public void loadDetailForecast(final String serviceId, final String cityId,
	    final Date date, final IDetailDataSourceServiceListener callBack) {
	scheduler.submit(new Runnable() {
	    @Override
	    public void run() {
		prepareInvokeMethods();
		try {
		    serviceApi.loadDetailForecast(cityId, serviceId,
			    date.getTime() / 1000, callBack);
		} catch (RemoteException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
    }

    public void loadCityByLocation(final double lat, final double lon,
	    final IDataSourceServiceListener callBack) throws RemoteException {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.loadCityByLocation(lat, lon, callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }

    public void loadCityWithForecastsByLocation(final double lat,
	    final double lon, final IDataSourceServiceListener callBack)
	    throws RemoteException {
	scheduler.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    prepareInvokeMethods();
		    serviceApi.loadCityWithForecastsByLocation(lat, lon,
			    callBack);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	});
    }
}

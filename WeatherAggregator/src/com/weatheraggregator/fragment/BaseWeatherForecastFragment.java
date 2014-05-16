package com.weatheraggregator.fragment;

import java.util.Date;

import android.os.RemoteException;
import android.widget.ImageView;

import com.googlecode.android.widgets.DateSlider.WeatherObject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weatheraggregator.activity.MainActivity;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.Forecast.Cloudiness;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.model.ForecastInfoKey;
import com.weatheraggregator.util.DateHelper;
import com.weatheraggregator.util.ForecastInfoManager;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.util.WeatherCache;

@EFragment
public abstract class BaseWeatherForecastFragment extends BaseFragment {
    protected WeatherObject mWeatherObject = new WeatherObject();
    private DisplayImageOptions mOptions;

    protected DisplayImageOptions getDisplayOption() {
	if (mOptions == null) {
	    mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
		    .resetViewBeforeLoading(true).build();
	}
	return mOptions;
    }

    protected WeatherObject getDateSliderItem(final Date date,
	    final ImageView ivCloudnes, City city, WeatherService service) {
	if (city != null && service != null) {
	    Forecast forecast = null;

	    forecast = WeatherCache.getInstance().get(
		    new ForecastInfoKey(city.getObjectId(), service
			    .getObjectId(), DateHelper.clearTime(date)));

	    if (forecast != null) {
		mWeatherObject.temp = MeasureUnitHelper.getSingleton()
			.convertTemperature(forecast.getTemp());
		ImageLoader.getInstance().displayImage(
			Util.getCloudyImagePath(forecast.getCondition()),
			ivCloudnes, getDisplayOption());
		return mWeatherObject;
	    } 
	}
	ImageLoader.getInstance().displayImage(
		Util.getCloudyImagePath(Cloudiness.N_A), ivCloudnes,
		getDisplayOption());
	mWeatherObject.temp = null;
	mWeatherObject.imgWeather = null;
	return mWeatherObject;
    }

    private Object monitor = new Object();
    
    @Background
    protected void loadForecast(final Date date) {
	synchronized (monitor) {
	    if (!ForecastInfoManager.getInstance().isLoading(date)) {
		ForecastInfoManager.getInstance().addLoad(date);
		((MainActivity) getActivity()).getManager()
			.loadForecastByPeriod(
				DateHelper.getFirstDateOfWeek(date),
				DateHelper.getLastDateOfWeek(date),
				new IDataSourceServiceListener.Stub() {
				    @Override
				    public void callBack(int statusCode)
					    throws RemoteException {
					ForecastInfoManager.getInstance()
						.remove(date);
					ForecastInfoManager.getInstance()
						.loadDateWeatherInfo(date);
				    }
				});
	    }
	}
    }

    // protected WeatherObject getHourSliderItem(final Date date,
    // final ImageView ivCloudnes, City city, WeatherService service) {
    // if (city != null && service != null) {
    // Forecast forecast = null;
    //
    // forecast = HourWeatherCache.getInstance().get(
    // new ForecastInfoKey(city.getObjectId(), service
    // .getObjectId(), DateHelper
    // .clearMinuteWithSecond(date)));
    //
    // if (forecast != null) {
    // mWeatherObject.temp = MeasureUnitHelper.getSingleton()
    // .convertTemperature(forecast.getTemp());
    // ImageLoader.getInstance().displayImage(
    // Util.getCloudyImagePath(forecast.getCondition()),
    // ivCloudnes, getDisplayOption());
    // return mWeatherObject;
    // }
    // }
    // ImageLoader.getInstance().displayImage(
    // Util.getCloudyImagePath(Cloudiness.N_A), ivCloudnes,
    // getDisplayOption());
    // mWeatherObject.temp = null;
    // return mWeatherObject;
    // }
}

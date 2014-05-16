package com.weatheraggregator.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.model.ForecastInfoKey;
import com.weatheraggregator.util.DateHelper;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.ServiceVoteHelper;
import com.weatheraggregator.util.WeatherCache;
import com.weatheraggregator.view.ServiceWeatherItem;
import com.weatheraggregator.view.ServiceWeatherItem_;

public class ForecastServiceAdapter extends ArrayAdapter<WeatherService> {
    private City mCity;
    private Date mDateForecast;
    private DisplayImageOptions mOptions;
    private static final String FORMAT_RATING = "%d, %d";

    public ForecastServiceAdapter(Context context,
	    List<WeatherService> objects, City city, Date dateForecast) {
	super(context, 0, objects);
	this.mCity = city;
	this.mDateForecast = dateForecast;
    }

    public Date getForecastDate() {
	return mDateForecast;
    }

//    @Override
//    public int getCount() {
//	if (mServiceList != null) {
//	    return mServiceList.size();
//	}
//	return 0;
//    }
//
//    @Override
//    public WeatherService getItem(int position) {
//	if (mServiceList != null && !mServiceList.isEmpty()) {
//	    return mServiceList.get(position);
//	}
//	return null;
//    }

    private ServiceWeatherItem getWeatherServiceView(View convertView) {
	ServiceWeatherItem view = null;
	if (convertView == null) {
	    view = ServiceWeatherItem_.build(getContext(), null);
	} else {
	    if (convertView instanceof ServiceWeatherItem_) {
		view = (ServiceWeatherItem) convertView;
	    } else {
		view = ServiceWeatherItem_.build(getContext(), null);
	    }
	}
	return view;
    }

    private void bindWeatherServiceView(final ServiceWeatherItem view,
	    final WeatherService service) {
	if (service != null && mCity != null) {
	    Forecast forecast = WeatherCache
		    .getInstance()
		    .get(new ForecastInfoKey(mCity.getObjectId(), service
			    .getObjectId(), DateHelper.clearTime(mDateForecast)));
	    view.setTag(forecast);
	    view.setServiceName(service.getName());
	    if (forecast != null) {
		view.setTempValue(MeasureUnitHelper.getSingleton()
			.convertTemperature(forecast.getTemp(), getContext()));

		view.setWetValue(MeasureUnitHelper.getSingleton()
			.convertPressure(forecast.getPressure(), getContext()));

		view.setMaxTempValue(MeasureUnitHelper
			.getSingleton()
			.convertTemperature(forecast.getMaxTemp(), getContext()));

		view.setMinTempValue(MeasureUnitHelper
			.getSingleton()
			.convertTemperature(forecast.getMinTemp(), getContext()));

		view.setWindDirectionImage(forecast.getWindDirection());
		view.setWindValue(MeasureUnitHelper
			.getSingleton()
			.convertWindSpeed(forecast.getWindSpeed(), getContext()));
		view.setEnableRatingButtons(null == ServiceVoteHelper
			.getServiceRating(mCity, service, mDateForecast));
	    } else {
		view.initNullData();
	    }
	    ImageLoader.getInstance().displayImage(service.getLogoUrl(),
		    view.getLogoView(), getDisplayOption());
	    view.setVoteAgainst(service.getVotedAgainst());
	    view.setVoteFor(service.getVotedFor());
	} else {
	    view.initNullData();
	}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ServiceWeatherItem view = getWeatherServiceView(convertView);
	bindWeatherServiceView(view, getItem(position));
	return view;
    }

    private DisplayImageOptions getDisplayOption() {
	if (mOptions == null) {
	    mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
		    .resetViewBeforeLoading(true).build();
	}
	return mOptions;
    }

}

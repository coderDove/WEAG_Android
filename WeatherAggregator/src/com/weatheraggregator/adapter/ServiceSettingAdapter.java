package com.weatheraggregator.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.view.ServicesSettingItem;
import com.weatheraggregator.view.ServicesSettingItem_;

public class ServiceSettingAdapter extends ArrayAdapter<WeatherService> {

    // private List<WeatherService> mServicesList;
    private DisplayImageOptions mOptions;
    private WeatherService mOldFavoriteService;
    private OnCheckedChangeListener mCheckListener;

    public ServiceSettingAdapter(Context context,
	    List<WeatherService> services, OnCheckedChangeListener checkListener) {
	super(context, 0, services);
	mOldFavoriteService = new Select().from(WeatherService.class)
		.where(String.format("%s=?", WeatherService.F_IS_FAVORITE), 1)
		.executeSingle();
	// mServicesList = services;
	mCheckListener = checkListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ServicesSettingItem view = null;
	if (convertView != null) {
	    view = (ServicesSettingItem) convertView;
	} else {
	    view = ServicesSettingItem_.build(getContext(), null);
	}
	WeatherService item = getItem(position);
	bind(view, item);
	return view;
    }

    private void bind(ServicesSettingItem view, WeatherService item) {
	view.setServiceName(item.getName());
	ImageLoader.getInstance().displayImage(item.getLogoUrl(),
		view.getLogo(), getDisplayOption());
	view.setFavorite(item);
	view.setCheckedListener(mCheckListener);
	view.setUsedService(item);
	if (item.getDescription() != null) {
	    view.setDescription(item.getDescription());
	} else {
	    view.setDescription("");
	}
    }

    private DisplayImageOptions getDisplayOption() {
	if (mOptions == null) {
	    mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
		    .resetViewBeforeLoading(true).build();
	}
	return mOptions;
    }

    public WeatherService getOldFavoriteService() {
	return mOldFavoriteService;
    }

    public void setOldFavoriteService(WeatherService oldFavoriteService) {
	this.mOldFavoriteService = oldFavoriteService;
    }

}

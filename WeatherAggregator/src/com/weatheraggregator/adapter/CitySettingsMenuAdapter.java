package com.weatheraggregator.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.weatheraggregator.entity.City;
import com.weatheraggregator.view.CitySettingItem;
import com.weatheraggregator.view.CitySettingItem_;

public class CitySettingsMenuAdapter extends ArrayAdapter<City> {
    private List<City> mCities;

    public CitySettingsMenuAdapter(Context context, List<City> cities) {
	super(context, 0, cities);
	this.mCities = cities;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return super.getItemId(position);
    }
    public List<City> getListOfCities() {
	return mCities;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
	CitySettingItem view = null;
	if (convertView != null) {
	    view = (CitySettingItem) convertView;
	} else {
	    view = CitySettingItem_.build(getContext());
	}
	view.bind(getItem(position));
	return view;
    }

    @Override
    public void remove(City city) {
	mCities.remove(city);
    }

    public void clearData() {
	if (mCities != null) {
	    mCities.clear();
	}
    }

}

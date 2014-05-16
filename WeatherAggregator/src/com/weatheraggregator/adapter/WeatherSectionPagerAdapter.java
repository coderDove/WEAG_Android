package com.weatheraggregator.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weatheraggregator.entity.City;
import com.weatheraggregator.fragment.WeatherServiceListFragment;

public class WeatherSectionPagerAdapter extends FragmentPagerAdapter {
    private int mCount = 0;
    private List<City> mCities;
    private final String NAN_CITY = "N/A";

    public WeatherSectionPagerAdapter(FragmentManager fm, List<City> cities) {
	super(fm);
	if (cities != null) {
	    this.mCount = cities.size();
	    this.mCities = cities;

	}
    }

    @Override
    public Fragment getItem(int position) {
	Bundle bundle = new Bundle();
	bundle.putInt(WeatherServiceListFragment.F_POSITION, position);
	City city = getCityPosition(position);
	if (city != null) {
	    bundle.putString(WeatherServiceListFragment.F_CITY_ID,
		    city.getObjectId());
	}
	return WeatherServiceListFragment.getNewInstance(bundle);
    }

    private City getCityPosition(int position) {
	if (mCities != null && mCities.size() > position) {
	    return mCities.get(position);
	} else {
	    return null;
	}
    }

    @Override
    public CharSequence getPageTitle(int position) {
	if (mCities.get(position).getName() != null) {
	    return mCities.get(position).getName();
	}
	return NAN_CITY;
    }

    @Override
    public int getCount() {
	return mCount;
    }

}

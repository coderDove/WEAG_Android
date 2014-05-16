package com.weatheraggregator.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weatheraggregator.activity.DetailWeatherActivity;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.fragment.DetailWeatherFragment;

public class DetailWeatherInfiniteFragmentPagerAdapter extends
	FragmentPagerAdapter {

    private long mDateInMillis;
    private static final int FIRST_POSITION = 300;
    private String mCityId = null;
    private WeatherService mWeatherService = null;
    private static final long MILIS_IN_DAY = 86400000;
    private static final int COUNT = 3;

    public DetailWeatherInfiniteFragmentPagerAdapter(FragmentManager fm,
	    WeatherService weatherService, final String cityId, long dateInMilis) {
	super(fm);
	mDateInMillis = dateInMilis;
	mWeatherService = weatherService;
	mCityId = cityId;
    }

    public void setCityId(String cityId) {
	this.mCityId = cityId;
    }

    @Override
    public Fragment getItem(int newPosition) {
	int diff = 0;
	long date = mDateInMillis;
	if (FIRST_POSITION > newPosition) {
	    diff = FIRST_POSITION - newPosition;
	    date = getPreviusDay(diff);
	} else if (FIRST_POSITION < newPosition) {
	    diff = newPosition - FIRST_POSITION;
	    date = getNextDay(diff);
	}

	Bundle bundle = new Bundle();
	bundle.putInt(DetailWeatherFragment.F_POSITION, newPosition);
	bundle.putString(DetailWeatherFragment.F_CITY_ID, mCityId);
	if (mWeatherService != null) {
	    bundle.putString(DetailWeatherFragment.F_SERVICE_ID,
		    mWeatherService.getObjectId());
	}
	bundle.putLong(DetailWeatherActivity.F_DATE_IN_MILLISECONDS, date);
	return DetailWeatherFragment.getNewInstance(bundle);
    }

    public long getDate(int newPosition) {
	int diff = 0;
	long date = mDateInMillis;
	if (FIRST_POSITION > newPosition) {
	    diff = FIRST_POSITION - newPosition;
	    date = getPreviusDay(diff);
	} else if (FIRST_POSITION < newPosition) {
	    diff = newPosition - FIRST_POSITION;
	    date = getNextDay(diff);
	}
	return date;
    }

    @Override
    public int getCount() {
	return COUNT;
    }

    private long getNextDay(final int diff) {
	return mDateInMillis + MILIS_IN_DAY * diff;
    }

    private long getPreviusDay(final int diff) {
	return mDateInMillis - MILIS_IN_DAY * diff;
    }
}

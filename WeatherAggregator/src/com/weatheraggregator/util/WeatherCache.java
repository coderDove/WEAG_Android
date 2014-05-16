package com.weatheraggregator.util;

import android.support.v4.util.LruCache;

import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.model.ForecastInfoKey;

public class WeatherCache extends LruCache<ForecastInfoKey, Forecast> {

    public WeatherCache(int maxSize) {
	super(maxSize);
    }

    private volatile static WeatherCache _singleton = null;

    public synchronized static WeatherCache getInstance() {
	if (null == _singleton) {
	    long maxMemory = Runtime.getRuntime().maxMemory() / 4;
	    maxMemory = Math.max(1024L * 1024L * 4L, maxMemory);
	    _singleton = new WeatherCache((int) (maxMemory));
	}
	return _singleton;
    }

    @Override
    protected void entryRemoved(boolean evicted, ForecastInfoKey key,
	    Forecast oldValue, Forecast newValue) {
	// When the entry is dismissed we can do the work and recycle the bitmap
	// super hast to be called AFTERWARDS
	super.entryRemoved(evicted, key, oldValue, newValue);
    }
}

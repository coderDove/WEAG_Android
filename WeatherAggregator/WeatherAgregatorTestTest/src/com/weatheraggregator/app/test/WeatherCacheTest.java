package com.weatheraggregator.app.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

public class WeatherCacheTest extends InstrumentationTestCase {
    private static final String TAG = "WeatherCacheTest";

    @Override
    protected void setUp() throws Exception {
	Log.d(TAG, "setUp(): " + getName());
	super.setUp();
    }
    @Override
    protected void tearDown() throws Exception {
	Log.d(TAG, "tearDown(): " + getName());
	super.tearDown();
    }
}

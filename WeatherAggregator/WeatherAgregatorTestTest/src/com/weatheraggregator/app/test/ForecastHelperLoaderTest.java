package com.weatheraggregator.app.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

public class ForecastHelperLoaderTest extends InstrumentationTestCase{
    private static final String TAG = "ForecastHelperLoaderTest";
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

    @SmallTest
    public void testSaveForecastToCache(){
	//ForecastHelperLoader forecastHelper = new ForecastHelperLoader();
	//forecastHelper.getActualForecastByDate(new Date());
    }
}

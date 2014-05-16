package com.weatheraggregator.app.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.weatheraggregator.entity.Forecast.Cloudiness;
import com.weatheraggregator.util.Util;

public class UtilTest extends InstrumentationTestCase {
    private static final String TAG = "MeasureHelperTest";

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

    // @LargeTest
    // public void testGetAccountEmail() {
    // Context context = new MockContext();
    // Util.getAccountEmail(context);
    // assertEquals(true, Util.getAccountEmail(context) != null);
    // }

    @SmallTest
    public void testgetCloudyImagePath() {
	assertEquals(true,
		Util.getCloudyImagePath(Cloudiness.CLOUDY_WITH_SUN) != null);
	assertEquals(true, Util.getCloudyImagePath(null) != null);
    }

    @SmallTest
    public void testGetForecastImage() {
	assertEquals(true, Util.getForecastImage(null) != null);
	assertEquals(true,
		Util.getForecastImage(Cloudiness.CLOUDY_WITH_SUN) != null);
    }

    @SmallTest
    public void testGetWindDirectionImageCode() {
	assertEquals(true,
		Util.getWindDirectionImageCode(Integer.valueOf(45)) != 0);
	assertEquals(true, Util.getWindDirectionImageCode(null) != 0);
    }

    @SmallTest
    public void testGetWindApproximation() {
	assertEquals(0, Util.getWindApproximation(0));
	assertEquals(0, Util.getWindApproximation(14));
	assertEquals(45, Util.getWindApproximation(35));
	assertEquals(45, Util.getWindApproximation(55));
	assertEquals(90, Util.getWindApproximation(74));
	assertEquals(90, Util.getWindApproximation(100));
	assertEquals(135, Util.getWindApproximation(124));
	assertEquals(135, Util.getWindApproximation(145));
	assertEquals(180, Util.getWindApproximation(176));
	assertEquals(180, Util.getWindApproximation(200));
	assertEquals(225, Util.getWindApproximation(215));
	assertEquals(225, Util.getWindApproximation(235));
	assertEquals(270, Util.getWindApproximation(268));
	assertEquals(270, Util.getWindApproximation(280));
	assertEquals(315, Util.getWindApproximation(300));
    }
}

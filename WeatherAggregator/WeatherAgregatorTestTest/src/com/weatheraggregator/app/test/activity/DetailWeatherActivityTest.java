package com.weatheraggregator.app.test.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.mockito.MockitoAnnotations;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.google.common.base.Preconditions;
import com.weatheraggregator.activity.AbstractWeatherActivity;
import com.weatheraggregator.activity.DetailWeatherActivity_;
import com.weatheraggregator.activity.ShareActivity_;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.WeatherService;

public class DetailWeatherActivityTest extends
	ActivityInstrumentationTestCase2<DetailWeatherActivity_> {

    public DetailWeatherActivityTest() {
	super(DetailWeatherActivity_.class);
    }

    public DetailWeatherActivityTest(Class<DetailWeatherActivity_> activityClass) {
	super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	getActivity();
	MockitoAnnotations.initMocks(this);
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
    }

    private Intent getValidIntent() {
	Intent intent = new Intent();
	intent.putExtra(AbstractWeatherActivity.F_OBJECT_ID,
		"0000000000000000000000000000");
	return intent;
    }

    private Intent getInValidIntent() {
	return null;
    }

    @SuppressWarnings("unchecked")
    @MediumTest
    public void testChangeCityFromActionBar() {
	final List<City> mockCityList = new ArrayList<City>(10);
	for (int i = 0; i < 10; i++) {
	    City city = new City();
	    city.setName("City: " + i);
	    mockCityList.add(city);
	}
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		getActivity().setCityDataForSpinner(mockCityList, 0);
	    }
	});
	Espresso.onView(ViewMatchers.withId(R.id.spCity)).perform(
		ViewActions.click());
	Espresso.onView(
		Matchers.allOf(ViewMatchers.withId(R.id.tvCity), ViewMatchers
			.hasSibling(ViewMatchers.withText(mockCityList.get(5)
				.getName())))).perform(ViewActions.click());
    }

    @SuppressWarnings("unchecked")
    @MediumTest
    public void testChooseShareSocial() {
	ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
		ShareActivity_.class.getName(), null, false);
	Espresso.onView(ViewMatchers.withId(R.id.spShare)).perform(
		ViewActions.click());
	Espresso.onView(
		Matchers.anyOf(ViewMatchers
			.withContentDescription(getInstrumentation()
				.getTargetContext().getString(R.string.twitter))))
		.perform(ViewActions.click());
	ShareActivity_ shareActivity = (ShareActivity_) getInstrumentation()
		.waitForMonitorWithTimeout(activityMonitor, 10);
	Preconditions.checkNotNull(shareActivity, "ShareActivity didn't start");
	shareActivity.finish();
    }

    @MediumTest
    public void testSwipeWithValidDataViewPager() {
	setActivityIntent(getValidIntent());
	testSwipeViewPager(getValidCities(), getValidWeatherService());
    }

    @MediumTest
    public void testIntentInvalidData() {
	setActivityIntent(getInValidIntent());
	testSwipeViewPager(new ArrayList<City>(10), new WeatherService());
    }

    @LargeTest
    public void testIntentNullData() {
	setActivityIntent(getInValidIntent());
	testSwipeViewPager(null, null);
    }

    private void testSwipeViewPager(final List<City> cities,
	    final WeatherService weatherService) {

	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		if (getActivity() != null)
		    getActivity().initViewPagerAdapter(cities, weatherService,
			    new Date().getTime());
	    }
	});
	Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(
		ViewActions.swipeLeft());
	Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(
		ViewActions.swipeLeft());
	Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(
		ViewActions.swipeLeft());
	Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(
		ViewActions.swipeLeft());
	Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(
		ViewActions.swipeLeft());
	Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(
		ViewActions.swipeLeft());
	getActivity().finish();
    }

    private List<City> getValidCities() {
	List<City> cities = new ArrayList<City>(10);
	for (int i = 0; i < 10; i++) {
	    City city = new City();
	    city.setName("City: " + i);
	    city.setObjectId("000-000-000-000-000-111");
	    cities.add(city);
	}
	return cities;
    }

    private WeatherService getValidWeatherService() {
	WeatherService weatherService = new WeatherService();
	weatherService.setName("Test Name");
	weatherService.setObjectId("000-000-000-000-000-111");
	weatherService.setVotedAgainst(1);
	weatherService.setVotedFor(2);
	return weatherService;
    }

}

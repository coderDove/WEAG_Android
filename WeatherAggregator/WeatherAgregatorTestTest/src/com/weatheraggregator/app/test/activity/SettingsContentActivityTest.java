package com.weatheraggregator.app.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.mockito.MockitoAnnotations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.google.common.base.Preconditions;
import com.weatheraggregator.activity.SettingsContentActivity.SettingType;
import com.weatheraggregator.activity.SettingsContentActivity_;
import com.weatheraggregator.app.test.utils.Utils;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.fragment.SettingsSearchCityFragment;
import com.weatheraggregator.fragment.WeatherServiceSettingFragment;

/**
 * Unit tests for
 * {@link com.weatheraggregator.activity.SettingsContentActivityTest}.
 */
public class SettingsContentActivityTest extends
	ActivityInstrumentationTestCase2<SettingsContentActivity_> {

    public SettingsContentActivityTest() {
	super(SettingsContentActivity_.class);
    }

    public SettingsContentActivityTest(
	    Class<SettingsContentActivity_> activityClass) {
	super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	getActivity();
	MockitoAnnotations.initMocks(this);
	Utils.writeLogCat(MainActivityTest.class.getSimpleName() + " setUp");
    }

    @Override
    protected void tearDown() throws Exception {
	Utils.writeLogCat(MainActivityTest.class.getSimpleName() + " tearDown");
	super.tearDown();
    }

    @SuppressWarnings("unchecked")
    @SmallTest
    public void testAddNewuserCity() {
	getActivity().setSettingsFragment(SettingType.CITY);
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.edSearch))
		.perform(ViewActions.typeText("New York"));
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.edSearch))
		.perform(ViewActions.closeSoftKeyboard());
	final SettingsSearchCityFragment fragment = getSettingsSearchCityFragment();
	Preconditions.checkNotNull(fragment,
		"SettingsSearchCityFragment not found on activity");
	final List<City> listOfCities = getFakeListCities();
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		fragment.updateAdapter(listOfCities);
	    }
	});
	Espresso.onData(
		Matchers.allOf(Matchers.is(City.class),
			Matchers.is(listOfCities.get(2))))
		.inAdapterView(
			ViewMatchers
				.withId(com.weatheraggregator.app.R.id.lvSerchCities))
		.atPosition(0).perform(ViewActions.click());
    }

    /**
     * Finds SettingsSearchCityFragment on activity
     * 
     * @return SettingsSearchCityFragment or null if not found
     */
    private SettingsSearchCityFragment getSettingsSearchCityFragment() {
	FragmentManager fm = getActivity().getSupportFragmentManager();
	Fragment fragment = fm
		.findFragmentById(com.weatheraggregator.app.R.id.content);
	if (fragment != null && fragment instanceof SettingsSearchCityFragment) {
	    return (SettingsSearchCityFragment) fragment;
	} else {
	    return null;
	}
    }

    /**
     * 
     * @return list with 10 cities
     */
    private List<City> getFakeListCities() {
	List<City> listOfCities = new ArrayList<City>();
	for (int i = 0; i < 10; i++) {
	    City city = new City();
	    city.setName("City: " + i);
	    listOfCities.add(city);
	}
	return listOfCities;
    }

    @SmallTest
    public void testChangeUIUnitsMeasure() {
	getActivity().setSettingsFragment(SettingType.UNIT);

	// Temperature
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbCelsius))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers
			.withId(com.weatheraggregator.app.R.id.rbFahrenheit))
		.perform(ViewActions.click());

	// Wind speed
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbMeter))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbKilometer))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbMile))
		.perform(ViewActions.click());

	// Pressure
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbMmHg))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbMbar))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbKPa))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbInHg))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.rbPsi))
		.perform(ViewActions.click());
    }

    @SuppressWarnings("unchecked")
    @SmallTest
    public void testSwitchFavoriteWebService() {
	getActivity().setSettingsFragment(SettingType.SERVICE);
	// need sleep because fragment does not keep attach to activity
	try {
	    Thread.sleep(3000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	final WeatherServiceSettingFragment fragment = getWeatherServiceSettingFragment();
	Preconditions.checkNotNull(fragment,
		"WeatherServiceSettingFragment not found on activity");
	final List<WeatherService> listOfWeatherServices = getFakeWeatherServices();
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		fragment.updateAdapter(listOfWeatherServices, null);
	    }
	});

	Espresso.onData(
		Matchers.allOf(Matchers.is(WeatherService.class),
			Matchers.is(listOfWeatherServices.get(2))))
		.inAdapterView(
			ViewMatchers
				.withId(com.weatheraggregator.app.R.id.lvServices))
		.atPosition(0)
		.onChildView(
			ViewMatchers
				.withId(com.weatheraggregator.app.R.id.cbFavorite))
		.perform(ViewActions.click());
    }

    /**
     * 
     * @return fake list of WeatherService with 10 size
     */
    private List<WeatherService> getFakeWeatherServices() {
	List<WeatherService> listOfWeatherServices = new ArrayList<WeatherService>();
	for (int i = 0; i < 10; i++) {
	    WeatherService ws = new WeatherService();
	    ws.setName("Weather Service: " + i);
	    ws.setObjectId("d62e586c-110e-47b5-8bab-d9fe0b74ef68");
	    ws.setDelete(Boolean.valueOf(false));
	    listOfWeatherServices.add(ws);
	}
	return listOfWeatherServices;
    }

    /**
     * Finds SettingsSearchCityFragment on activity
     * 
     * @return WeatherServiceSettingFragment or null if not found
     */
    private WeatherServiceSettingFragment getWeatherServiceSettingFragment() {
	FragmentManager fm = getActivity().getSupportFragmentManager();
	Fragment fragment = fm
		.findFragmentById(com.weatheraggregator.app.R.id.content);
	if (fragment != null
		&& fragment instanceof WeatherServiceSettingFragment) {
	    return (WeatherServiceSettingFragment) fragment;
	} else {
	    return null;
	}
    }

    @SuppressWarnings("unchecked")
    @SmallTest
    public void testSwitchOnWeatherService() {
	getActivity().setSettingsFragment(SettingType.SERVICE);
	// need sleep because fragment does not keep attach to activity
	try {
	    Thread.sleep(3000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	final WeatherServiceSettingFragment fragment = getWeatherServiceSettingFragment();
	Preconditions.checkNotNull(fragment,
		"WeatherServiceSettingFragment not found on activity");
	final List<WeatherService> listOfWeatherServices = getFakeWeatherServices();
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		fragment.updateAdapter(listOfWeatherServices, null);
	    }
	});

	for (int i = 0; i < 5; i++) {
	    Espresso.onData(
		    Matchers.allOf(Matchers.is(WeatherService.class),
			    Matchers.is(listOfWeatherServices.get(i))))
		    .inAdapterView(
			    ViewMatchers
				    .withId(com.weatheraggregator.app.R.id.lvServices))
		    .atPosition(0)
		    .onChildView(
			    ViewMatchers
				    .withId(com.weatheraggregator.app.R.id.tbUse))
		    .perform(ViewActions.click());
	}
    }

}

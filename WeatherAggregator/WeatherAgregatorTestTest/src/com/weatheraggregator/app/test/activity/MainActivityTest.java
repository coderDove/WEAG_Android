package com.weatheraggregator.app.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.google.common.base.Preconditions;
import com.weatheraggregator.activity.AboutActivity_;
import com.weatheraggregator.activity.DetailWeatherActivity_;
import com.weatheraggregator.activity.MainActivity_;
import com.weatheraggregator.activity.SettingsContentActivity_;
import com.weatheraggregator.adapter.CitySettingsMenuAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.app.test.utils.Utils;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.Forecast.Cloudiness;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.model.MeasureUnit;
import com.weatheraggregator.util.DialogManager;
import com.weatheraggregator.util.DialogValidationTextMessagesManager;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.webservice.exception.ErrorType;

/**
 * Unit tests for {@link com.weatheraggregator.activity.MainActivity}. *
 */
public class MainActivityTest extends
	ActivityInstrumentationTestCase2<MainActivity_> {

    public MainActivityTest() {
	super(MainActivity_.class);
    }

    public MainActivityTest(Class<MainActivity_> activityClass) {
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
    public void testChangeCityValidData() {
	final List<City> mockCityList = Mockito.mock(List.class);
	mockCityList.add(new City());
	mockCityList.add(new City());
	Mockito.when(mockCityList.get(0)).thenReturn(new City());
	Mockito.when(mockCityList.get(1)).thenReturn(new City());
	City validValue = new City();
	validValue.setName("ValidValue");
	mockCityList.add(validValue);
	Mockito.when(mockCityList.get(2)).thenReturn(validValue);
	Mockito.when(mockCityList.size()).thenReturn(3);
	getActivity().runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		getActivity().setCityDataForSpinner(mockCityList, 0);
	    }
	});
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.spAB))
		.perform(ViewActions.click());
	// Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.tvCity),
	// ViewMatchers.hasSibling(ViewMatchers.withText(mockCityList.get(0).getName())))).perform(ViewActions.click());
    }

    @SmallTest
    public void testUIAddNewUserCity() {
	openLeftSlideMenu();
	Espresso.onView(
		ViewMatchers
			.withId(com.weatheraggregator.app.R.id.btnUserCities))
		.perform(ViewActions.click());
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.llAddNew))
		.perform(ViewActions.click());
    }

    @SuppressWarnings("unchecked")
    @SmallTest
    public void testChangeCityFromActionBar() {
	final List<City> mockCityList = new ArrayList<City>(10);
	for (int i = 0; i < 10; i++) {
	    City city = new City();
	    city.setName("City: " + i);
	    mockCityList.add(city);
	}
	getActivity().runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		getActivity().setCityDataForSpinner(mockCityList, 0);
	    }
	});
	Espresso.onView(ViewMatchers.withId(R.id.spAB)).perform(
		ViewActions.click());
	Espresso.onView(
		Matchers.allOf(ViewMatchers.withId(R.id.tvCity), ViewMatchers
			.hasSibling(ViewMatchers.withText(mockCityList.get(5)
				.getName())))).perform(ViewActions.click());
    }

    // Needed not mock context for getting resource
    @MediumTest
    public void testGetShareMessage() {
	Forecast testForecast = Mockito.mock(Forecast.class);
	Mockito.when(testForecast.getTemp()).thenReturn(Integer.valueOf(5));
	Mockito.when(testForecast.getMaxTemp()).thenReturn(Integer.valueOf(5));
	Mockito.when(testForecast.getMinTemp()).thenReturn(Integer.valueOf(5));
	Mockito.when(testForecast.getWindSpeed()).thenReturn(
		Double.valueOf(5.0));

	assertEquals(true,
		Util.getShareMessage(getActivity(), testForecast) != null);
	assertEquals(true, Util.getShareMessage(null, testForecast) != null);
	assertEquals(true, Util.getShareMessage(getActivity(), null) != null);
    }

    // --------------------------------------CitySettingsMenuAdapter----------------------------------

    @MediumTest
    public void testConstructor() {
	assertEquals(true, getAdapter() != null);
    }

    @SmallTest
    private CitySettingsMenuAdapter getAdapter() {
	List<City> cities = Mockito.mock(List.class);
	CitySettingsMenuAdapter adapter = new CitySettingsMenuAdapter(
		getActivity(), cities);
	return adapter;
    }

    @MediumTest
    public void testGetListOfCities() {
	assertEquals(true, getAdapter().getListOfCities() != null);
    }

    @MediumTest
    public void testRemoveAndClear() {
	List<City> cities = Mockito.mock(List.class);
	City city = Mockito.mock(City.class);
	cities.add(city);
	CitySettingsMenuAdapter adapter = new CitySettingsMenuAdapter(
		getActivity(), cities);
	adapter.remove(city);
	assertEquals(true, cities.size() == 0);
    }

    @SmallTest
    public void testOpenAbout() {
	openLeftSlideMenu();
	ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
		AboutActivity_.class.getName(), null, false);
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.btnAbout))
		.perform(ViewActions.click());
	AboutActivity_ aboutActivity = (AboutActivity_) getInstrumentation()
		.waitForMonitorWithTimeout(activityMonitor, 5);
	Preconditions.checkNotNull(aboutActivity, "AboutActivity didn't start");
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.webView))
		.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	aboutActivity.finish();
    }

    @MediumTest
    public void testAddCityFromActionBar() {
	ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
		SettingsContentActivity_.class.getName(), null, false);
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.ivAddCity))
		.perform(ViewActions.click());
	SettingsContentActivity_ settingsContentActivity = (SettingsContentActivity_) getInstrumentation()
		.waitForMonitorWithTimeout(activityMonitor, 5);
	Preconditions.checkNotNull(settingsContentActivity,
		"SettingsContentActivity didn't start");
	settingsContentActivity.finish();
    }

    @MediumTest
    public void testOpenSettingsUnitsOfMeasure() {
	openLeftSlideMenu();
	ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
		SettingsContentActivity_.class.getName(), null, false);
	Espresso.onView(
		ViewMatchers
			.withId(com.weatheraggregator.app.R.id.btnUserMeasure))
		.perform(ViewActions.click());
	SettingsContentActivity_ settingsContentActivity = (SettingsContentActivity_) getInstrumentation()
		.waitForMonitorWithTimeout(activityMonitor, 5);
	Preconditions.checkNotNull(settingsContentActivity,
		"SettingsContentActivity didn't start");
	settingsContentActivity.finish();
    }

    @MediumTest
    public void testOpenUserService() {
	openLeftSlideMenu();
	ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
		SettingsContentActivity_.class.getName(), null, false);
	Espresso.onView(
		ViewMatchers
			.withId(com.weatheraggregator.app.R.id.btnUserServices))
		.perform(ViewActions.click());
	SettingsContentActivity_ settingsContentActivity = (SettingsContentActivity_) getInstrumentation()
		.waitForMonitorWithTimeout(activityMonitor, 5);
	Preconditions.checkNotNull(settingsContentActivity,
		"SettingsContentActivity didn't start");
	settingsContentActivity.finish();
    }

    /**
     * Slides to left and show setting fragment
     */
    private void openLeftSlideMenu() {
	Espresso.onView(
		ViewMatchers.withId(com.weatheraggregator.app.R.id.pager))
		.perform(ViewActions.swipeRight());
    }

    // TODO: need research, why test ViewPager
    @SuppressWarnings("unchecked")
    @LargeTest
    private void testOpenDetailWeatherServiceFromFavoriteService() {
	ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
		DetailWeatherActivity_.class.getName(), null, false);
	final List<City> cityList = getCityList();
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		getActivity().updateCityPagerAndSpinner(cityList);
	    }
	});
	// Espresso.onData(
	// Matchers.allOf(Matchers.is(TextView.class),
	// Matchers.containsString("WeatherUa")))
	// .inAdapterView(ViewMatchers.withText(cityList.get(0).getName()))
	// .perform(ViewActions.click());
	Espresso.onView(
		Matchers.allOf(ViewMatchers.withId(R.id.tvServiceName),
			ViewMatchers.withText(cityList.get(0).getName())))
		.perform(ViewActions.click());
	DetailWeatherActivity_ detailWeatherActivity = (DetailWeatherActivity_) getInstrumentation()
		.waitForMonitorWithTimeout(activityMonitor, 5);
	Preconditions.checkNotNull(detailWeatherActivity,
		"DetailWeatherActivity didn't start");
	detailWeatherActivity.finish();
    }

    private List<City> getCityList() {
	List<City> cityList = new ArrayList<City>();
	for (int i = 0; i < 3; i++) {
	    City city = new City();
	    city.setName("City" + i);
	    city.setObjectId("d62e586c-110e-47b5-8bab-d9fe0b74ef68");
	    cityList.add(city);
	}
	return cityList;
    }

    @SmallTest
    public void testGetCloudyStringValue() {
	assertEquals(getActivity().getString(R.string.cloudy_with_sun),
		Util.getCloudyStringValue(getActivity(),
			Cloudiness.CLOUDY_WITH_SUN));
	assertEquals(getActivity().getString(R.string.na),
		Util.getCloudyStringValue(getActivity(), null));
	assertEquals(getActivity().getString(R.string.blizzard),
		Util.getCloudyStringValue(getActivity(), Cloudiness.BLIZZARD));
	assertEquals(getActivity().getString(R.string.hail),
		Util.getCloudyStringValue(getActivity(), Cloudiness.HAIL));
	assertEquals(getActivity().getString(R.string.cloudy),
		Util.getCloudyStringValue(getActivity(), Cloudiness.CLOUDY));
	assertEquals(getActivity().getString(R.string.fog),
		Util.getCloudyStringValue(getActivity(), Cloudiness.FOG));
	assertEquals(getActivity().getString(R.string.overcast),
		Util.getCloudyStringValue(getActivity(), Cloudiness.OVERCAST));
	assertEquals(getActivity().getString(R.string.rain),
		Util.getCloudyStringValue(getActivity(), Cloudiness.RAIN));
	assertEquals(getActivity().getString(R.string.rain_with_thunderstorms),
		Util.getCloudyStringValue(getActivity(),
			Cloudiness.RAIN_WITH_THUNDERSTORMS));
	assertEquals(getActivity().getString(R.string.short_rain),
		Util.getCloudyStringValue(getActivity(), Cloudiness.SHORT_RAIN));
	assertEquals(getActivity().getString(R.string.slee),
		Util.getCloudyStringValue(getActivity(), Cloudiness.SLEET));
	assertEquals(getActivity().getString(R.string.snow),
		Util.getCloudyStringValue(getActivity(), Cloudiness.SNOW));
	assertEquals(getActivity().getString(R.string.snow_with_rain),
		Util.getCloudyStringValue(getActivity(),
			Cloudiness.SNOW_WITH_RAIN));
	assertEquals(getActivity().getString(R.string.sunny),
		Util.getCloudyStringValue(getActivity(), Cloudiness.SUNNY));
    }

    @SmallTest
    public void testGetWindSpeed() {
	User mokUser = Mockito.mock(User.class);
	Mockito.when(mokUser.getSpeedUnit()).thenReturn(MeasureUnit.M_S);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(getActivity().getString(R.string.m_s), MeasureUnitHelper
		.getSingleton().getWindSpeed(getActivity()));

	Mockito.when(mokUser.getSpeedUnit()).thenReturn(MeasureUnit.KM_H);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(getActivity().getString(R.string.km_h), MeasureUnitHelper
		.getSingleton().getWindSpeed(getActivity()));

	Mockito.when(mokUser.getSpeedUnit()).thenReturn(MeasureUnit.MI_H);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(getActivity().getString(R.string.mi_h), MeasureUnitHelper
		.getSingleton().getWindSpeed(getActivity()));
    }

    @SmallTest
    public void testGetTempUnitType() {
	User mokUser = Mockito.mock(User.class);
	Mockito.when(mokUser.getTemperatureUnit()).thenReturn(
		MeasureUnit.CELSIUS);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(getActivity().getString(R.string.celsius),
		MeasureUnitHelper.getSingleton().getTempUnitType(getActivity()));

	Mockito.when(mokUser.getTemperatureUnit()).thenReturn(
		MeasureUnit.FAHRENHEIT);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(getActivity().getString(R.string.fahrenheit),
		MeasureUnitHelper.getSingleton().getTempUnitType(getActivity()));
    }

    @SmallTest
    public void testGetWindSpeedPosition() {
	User mokUser = Mockito.mock(User.class);
	Mockito.when(mokUser.getSpeedUnit()).thenReturn(MeasureUnit.M_S);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(MeasureUnitHelper.getSingleton().getWindSpeedPosition(), 0);

	Mockito.when(mokUser.getSpeedUnit()).thenReturn(MeasureUnit.KM_H);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(MeasureUnitHelper.getSingleton().getWindSpeedPosition(), 1);

	Mockito.when(mokUser.getSpeedUnit()).thenReturn(MeasureUnit.MI_H);
	MeasureUnitHelper.getSingleton().init(mokUser);
	assertEquals(MeasureUnitHelper.getSingleton().getWindSpeedPosition(), 2);
    }

    private void testDialogSingleton() {
	assertEquals(DialogManager.getInstance(getActivity()),
		DialogManager.getInstance(getActivity()));
    }

    @SmallTest
    public void testAlertDialog1() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		testDialogSingleton();
		DialogManager
			.getInstance(getActivity())
			.makeAlert(
				getActivity()
					.getString(
						com.weatheraggregator.app.R.string.dlg_host_not_available));
	    }
	});
	Espresso.onView(
		ViewMatchers
			.withText(getActivity()
				.getString(
					com.weatheraggregator.app.R.string.dlg_host_not_available)))
		.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testAlertDialog2() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogManager.getInstance(getActivity()).makeAlert("test msg");
	    }
	});

	Espresso.onView(ViewMatchers.withText("test msg")).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testAlertDialog3() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogManager.getInstance(getActivity()).makeAlert(
			"test title", "test msg", null);
	    }
	});
	Espresso.onView(ViewMatchers.withText("test msg")).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testConnfirmDialog1() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogManager.getInstance(getActivity()).makeConfirm(
			R.string.dlg_host_not_available, null, null);
	    }
	});

	Espresso.onView(
		ViewMatchers.withText(getActivity().getString(
			R.string.dlg_host_not_available))).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testConnfirmDialog2() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogManager.getInstance(getActivity()).makeConfirm(
			R.string.dlg_host_not_available, null, null);
	    }
	});
	Espresso.onView(
		ViewMatchers.withText(getActivity().getString(
			R.string.dlg_host_not_available))).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testConnfirmDialog3() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogManager.getInstance(getActivity()).makeConfirm(
			"test title", "test message", null, null);
	    }
	});

	Espresso.onView(ViewMatchers.withText("test message")).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testDialogValidationHost() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogValidationTextMessagesManager.newInstance(getActivity())
			.makeAlert(ErrorType.HTTP_HOST_NOT_EVAILABLE, null);
	    }
	});
	Espresso.onView(
		ViewMatchers.withText(getActivity().getString(
			R.string.dlg_host_not_available))).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testDialogValidationInternet() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogValidationTextMessagesManager.newInstance(getActivity())
			.makeAlert(ErrorType.NO_INTERNET_CONNECTION, null);
	    }
	});
	Espresso.onView(
		ViewMatchers.withText(getActivity().getString(
			R.string.dlg_no_internet_connection))).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testDialogValidationUnknow() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogValidationTextMessagesManager.newInstance(getActivity())
			.makeAlert(ErrorType.UNKNOW_ERROR, null);
	    }
	});
	Espresso.onView(
		ViewMatchers.withText(getActivity().getString(
			R.string.dlg_host_not_available))).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }

    @SmallTest
    public void testDialogValidationUserNotRegistered() {
	getInstrumentation().runOnMainSync(new Runnable() {
	    @Override
	    public void run() {
		DialogValidationTextMessagesManager.newInstance(getActivity())
			.makeAlert(ErrorType.USER_NOT_AUHTORISED, null);
	    }
	});
	Espresso.onView(
		ViewMatchers.withText(getActivity().getString(
			R.string.dlg_host_not_available))).check(
		ViewAssertions.matches(ViewMatchers.isDisplayed()));
	Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(
		ViewActions.click());
    }
    @SmallTest
    private void testStartActivity() {
	assertEquals(true, getActivity() != null);
	assertEquals(getActivity(), getActivity());
	getActivity().finish();
    }
}

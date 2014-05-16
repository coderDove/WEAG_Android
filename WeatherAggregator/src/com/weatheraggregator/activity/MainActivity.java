package com.weatheraggregator.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.activeandroid.query.Select;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.activity.SettingsContentActivity.SettingType;
import com.weatheraggregator.adapter.CityMenuAdapter;
import com.weatheraggregator.adapter.WeatherSectionPagerAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.events.EventChangedCity;
import com.weatheraggregator.events.EventUpdateDataFromLocation;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.util.DateHelper;
import com.weatheraggregator.util.DialogValidationTextMessagesManager;
import com.weatheraggregator.util.ForecastInfoManager;
import com.weatheraggregator.util.UserSelectDateCasher;
import com.weatheraggregator.view.ActionBarMainView;
import com.weatheraggregator.view.MainViewPager;
import com.weatheraggregator.webservice.exception.ErrorType;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends AbstractWeatherActivity {

    public static final int DETAIL_ACTIVITY_CODE = 980;
    public static final String F_CITY_POSITION = "f_city_position";
    public static final String F_USER_DATE = "user_select_date";
    @ViewById(R.id.pager)
    protected MainViewPager mPager;
    private WeatherSectionPagerAdapter mWeatherSectionAdapter;
    private List<City> mListOfCity = new ArrayList<City>();
    private int mCityPosition = -1;
    private ActionBarMainView mActionBarView;

    private void initData() {
	initCityListWithAdapters();
    }

    private boolean mIsInitViewRun = false;

    // use mIsInitViewRun variable because
    // https://github.com/excilys/androidannotations/issues/449
    @AfterViews
    protected void initView() {
	if (mIsInitViewRun) {
	    return;
	}
	mIsInitViewRun = true;
	styleActionBar();
	mPager.setChildId(R.id.llContainer);
	mPager.setOnPageChangeListener(mPagerListener);
    }

    public int getCurrentViewPagerPosition() {
	return mPager.getCurrentItem();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	UserSelectDateCasher.getSingleton().clear();
    }

    @Override
    protected void onResume() {
	super.onResume();
	initData();
    }

    @Override
    protected void onPause() {
	mCityPosition = mPager.getCurrentItem();
	super.onPause();
    }

    public void styleActionBar() {
	mActionBarView = com.weatheraggregator.view.ActionBarMainView_
		.build(this);
	super.styleActionBar(mActionBarView);
	mActionBarView.setAddCityClickListener(mAddCityListener);
	mActionBarView.setCitySelectListener(mOnItemSelectedListener);
	setHomePageCloseListener(mActionBarView.getHomeButton());
    }

    public int getSelectionCity() {
	if (mActionBarView != null) {
	    return mActionBarView.getSelectCityPosition();
	}
	return 0;
    }

    @UiThread
    public void setCityDataForSpinner(List<City> cities, int position) {
	if (cities != null) {
	    mActionBarView.setCityAdapter(new CityMenuAdapter(this, cities));
	    if (position > 0 && cities.size() > position)
		mActionBarView.selectCityPosition(position);
	} else {
	    mActionBarView.setCityAdapter(null);
	}
    }

    @UiThread
    public void setCityDataForViewPager(List<City> cities) {
	if (cities != null) {
	    mWeatherSectionAdapter = new WeatherSectionPagerAdapter(
		    getSupportFragmentManager(), cities);
	    mPager.setAdapter(mWeatherSectionAdapter);
	}
    }

    @UiThread
    protected void updateCityPagerAndSpinner(List<City> listOfCity) {

	if (!listOfCity.isEmpty()) {
	    mCurrentCity = listOfCity.get(0);
	} else {
	    mCurrentCity = null;
	}
	setCityDataForViewPager(listOfCity);
	setCityDataForSpinner(listOfCity, mCityPosition);
    }

    @Background
    protected void initCityListWithAdapters() {
	List<City> newListOfCity = new Select().from(City.class)
		.orderBy(City.F_ORDER)
		.where(String.format("%s = ?", City.F_IS_DELETED), 0).execute();
	if (newListOfCity == null) {
	    newListOfCity = new ArrayList<City>();
	}
	if (!isEquals(mListOfCity, newListOfCity)) {
	    mListOfCity = newListOfCity;
	    updateCityPagerAndSpinner(mListOfCity);
	}
    }

    private boolean isEquals(List<City> oldList, List<City> newList) {
	if (oldList.size() != newList.size()) {
	    return false;
	}
	for (int index = 0; index < newList.size(); index++) {
	    if (!newList.get(index).equals(oldList.get(index))) {
		return false;
	    }
	}
	return true;
    }

    private OnPageChangeListener mPagerListener = new OnPageChangeListener() {
	@Override
	public void onPageSelected(int page) {
	    changingCityByPosition(page);
	    Log.d(MainActivity.class.getSimpleName(), "change page: " + page);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
    };

    public void changingCityByPosition(int index) {
	mActionBarView.selectCityPosition(index);
    }

    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
		int position, long id) {
	    changeCity(position);
	    mActionBarView.selectCityPosition(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
    };

    private OnClickListener mAddCityListener = new OnClickListener() {

	@Override
	public void onClick(View v) {
	    com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_(
		    MainActivity.this);
	    Intent intent = builder.get();
	    intent.putExtra(SettingsContentActivity.F_SETTING,
		    SettingType.CITY.getCode());
	    builder.start();
	}
    };

    /**
     * change current city in viewpager
     * 
     * @param position
     */
    public void changeCity(final int position) {
	if (mPager != null && mListOfCity != null
		&& position < mListOfCity.size()) {
	    mCurrentCity = mListOfCity.get(position);
	    mPager.setCurrentItem(position);
	}
    }

    // Load forecast from service and prepare cache
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK && data != null
		&& requestCode == DETAIL_ACTIVITY_CODE) {
	    Integer position = data.getExtras().getInt(F_CITY_POSITION);
	    if (position != null && mCityPosition != position.intValue()) {
		mCityPosition = position.intValue();
		changeCity(mCityPosition);
		changingCityByPosition(mCityPosition);
	    }
	}
	// TODO: check resultCode for load forecast
	if (resultCode == RESULT_OK) {
	    Date now = new Date();
	    getManager().loadForecastByPeriod(
		    DateHelper.getFirstDateOfWeek(now),
		    DateHelper.getLastDateOfWeek(now),
		    new IDataSourceServiceListener.Stub() {
			@Override
			public void callBack(int statusCode)
				throws RemoteException {
			    if (statusCode == ErrorType.HTTP_OK.getCode()) {
				ForecastInfoManager.getInstance().cleareCache();
				ForecastInfoManager.getInstance()
					.loadWeatherInfo();
			    } else {
				showToast(ErrorType.values()[statusCode]);
			    }
			}
		    });
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @UiThread
    protected void showToast(ErrorType type) {
	DialogValidationTextMessagesManager.newInstance(MainActivity.this)
		.makeToast(type);
    }

    public void onEventMainThread(EventUpdateDataFromLocation event) {
	// initCityListWithAdapters();
    }
}

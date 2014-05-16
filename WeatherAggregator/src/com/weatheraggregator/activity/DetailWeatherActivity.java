package com.weatheraggregator.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.provider.BaseColumns;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.activeandroid.query.Select;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.sharesociallibrary.app.utils.SocialType;
import com.weatheraggregator.adapter.CityMenuAdapter;
import com.weatheraggregator.adapter.DetailWeatherInfiniteFragmentPagerAdapter;
import com.weatheraggregator.adapter.InfinitePagerAdapter;
import com.weatheraggregator.adapter.ShareSocialAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.events.EventChangedCity;
import com.weatheraggregator.events.EventUpdateDataFromLocation;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.view.ActionBarDetailView;
import com.weatheraggregator.view.DateViewPager;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.detail_weather)
public class DetailWeatherActivity extends AbstractWeatherActivity {
    public static final String TAG = "DetailWeatherActivity";

    public static final String F_DATE_IN_MILLISECONDS = "detail_date_in_milliseconds";

    @ViewById(R.id.pager)
    public DateViewPager mPager;

    private int mCityPosition = 0;
    public DetailWeatherInfiniteFragmentPagerAdapter mAdapter;
    private List<City> mListOfCity = new ArrayList<City>();
    private long mTimeInmilliseconds = new Date().getTime();
    private ActionBarDetailView mActionBarDetailView;
    private ShareSocialAdapter mShareSocialAdapter;

    private boolean isInitViewRun = false;

    /**
     * When activity initialize, OnItemSelectedListener called. Why? Need
     * research.
     */
    private boolean mInitShareSocialItem = false;

    @AfterViews
    protected void initView() {
	if (isInitViewRun) {
	    return;
	}
	isInitViewRun = true;
	EventBus.clearCaches();
	styleActionBar();
	mPager.setChildId(R.id.llContainer);
	mPager.setOnPageChangeListener(mPagerListener);
    }

    @Background
    protected void initListsWithAdapters() {
	mListOfCity = new Select(City.F_NAME, City.F_REGION, City.F_REGION,
		City.F_OBJECT_ID, BaseColumns._ID).from(City.class)
		.orderBy(City.F_ORDER)
		.where(String.format("%s = ?", City.F_IS_DELETED), 0).execute();
	final String serviceId = getServiceId();
	if (serviceId != null) {
	    mCurrentService = new Select()
		    .from(WeatherService.class)
		    .where(String.format("%s = ?", WeatherService.F_OBJECT_ID),
			    serviceId).executeSingle();
	}
	if (mCurrentService == null) {
	    mCurrentService = new Select().from(WeatherService.class)
		    .where(String.format("%s = 0", WeatherService.F_ORDER))
		    .executeSingle();
	}

	if (mListOfCity != null) {
	    if (!mListOfCity.isEmpty()) {
		mCurrentCity = mListOfCity.get(0);
	    } else {
		mCurrentCity = null;
	    }
	    if (getIntent() != null) {
		mTimeInmilliseconds = getIntent().getLongExtra(
			F_DATE_IN_MILLISECONDS, new Date().getTime());
	    }
	    initViewPagerAdapter(mListOfCity, mCurrentService,
		    mTimeInmilliseconds);
	    setCityDataForSpinner(mListOfCity, mCityPosition);
	}
    }

    @UiThread
    public void setCityDataForSpinner(List<City> cities, int position) {
	if (cities != null) {
	    mActionBarDetailView.setCityAdapter(new CityMenuAdapter(this,
		    cities));
	    if (position > 0 && cities.size() > position)
		mActionBarDetailView.selectCityPosition(position);
	} else {
	    mActionBarDetailView.setCityAdapter(null);
	}
    }

    public void styleActionBar() {
	mActionBarDetailView = com.weatheraggregator.view.ActionBarDetailView_
		.build(this);
	super.styleActionBar(mActionBarDetailView);
	mActionBarDetailView.setCitySelectListener(mOnItemSelectedListener);
	mShareSocialAdapter = new ShareSocialAdapter(this);
	mActionBarDetailView
		.setShareItemSelectedListener(mShareItemSelectedListener);
	mActionBarDetailView.serShareAdapter(mShareSocialAdapter);
	setHomePageCloseListener(mActionBarDetailView.getHomeButton());
    }

    private OnItemSelectedListener mShareItemSelectedListener = new OnItemSelectedListener() {

	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
		int position, long id) {
	    if (mInitShareSocialItem) {
		com.weatheraggregator.activity.ShareActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.ShareActivity_.IntentBuilder_(
			DetailWeatherActivity.this);
		Intent intent = builder.get();
		Forecast forecast = null;
		SocialType type = mShareSocialAdapter.getItem(position);
		intent.putExtra(ShareActivity.SHARE_MESSAGE,
			Util.getShareMessage(getApplicationContext(), forecast));
		intent.putExtra(ShareActivity.F_SOCIAL_TYPE, type);
		builder.start();
	    } else {
		mInitShareSocialItem = true;
	    }
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
    };

    @UiThread
    protected void initViewPagerAdapter(List<City> listOfCity,
	    WeatherService weatherService, long currentDate) {
	if (weatherService != null && listOfCity != null
		&& !listOfCity.isEmpty()) {
	    mAdapter = new DetailWeatherInfiniteFragmentPagerAdapter(
		    getSupportFragmentManager(), weatherService, listOfCity
			    .get(0).getObjectId(), currentDate);
	    PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mAdapter);
	    mPager.setAdapter(wrappedAdapter);
	} else {
	    mPager.setAdapter(null);
	}
    }

    private void initData() {
	if (getIntent() != null && getIntent().getExtras() != null) {
	    Integer position = getIntent().getExtras().getInt(
		    MainActivity.F_CITY_POSITION);
	    if (position != null) {
		mCityPosition = position.intValue();
	    }
	}
	initListsWithAdapters();
    }

    private String getServiceId() {
	String serviceId = null;
	Intent intent = getIntent();
	if (intent != null) {
	    serviceId = intent
		    .getStringExtra(AbstractWeatherActivity.F_OBJECT_ID);
	}
	return serviceId;
    }

    private OnPageChangeListener mPagerListener = new OnPageChangeListener() {
	@Override
	public void onPageSelected(int page) {
	    changeCurrentCityId(page);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
    };

    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
		int position, long id) {
	    changeCity(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
    };

    private void changeCity(final int position) {
	if (mPager != null && mListOfCity != null
		&& position < mListOfCity.size()) {
	    mCurrentCity = mListOfCity.get(position);
	    if (mCurrentCity != null) {
		changingCityByPosition(position);
		changeCurrentCityId(position);
		Intent intent = new Intent();
		intent.putExtra(MainActivity.F_CITY_POSITION, position);
		setResult(RESULT_OK,intent);
		EventBus.getDefault().post(
			new EventChangedCity(mCurrentCity, new Date(
				mTimeInmilliseconds), null, mPager
				.getCurrentItem()));
	    }
	}
    }

    public void changingCityByPosition(int index) {
	mActionBarDetailView.selectCityPosition(index);
    }

    private void changeCurrentCityId(final int page) {
	if (mAdapter != null && mCurrentCity != null) {
	    mAdapter.setCityId(mCurrentCity.getObjectId());
	}
    }

    @Override
    protected void onResume() {
	super.onResume();
	initData();
    }

    public void onEventMainThread(EventUpdateDataFromLocation event) {
	// initData();
    }

    @Override
    public void onBackPressed() {
	Intent intent = new Intent();
	intent.putExtra(MainActivity.F_CITY_POSITION,
		mActionBarDetailView.getSelectCityPosition());
	super.onBackPressed();
    }
}

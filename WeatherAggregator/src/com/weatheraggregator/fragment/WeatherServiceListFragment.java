package com.weatheraggregator.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.googlecode.android.widgets.DateSlider.SliderContainer;
import com.googlecode.android.widgets.DateSlider.SliderContainer.OnTimeChangeListener;
import com.googlecode.android.widgets.DateSlider.WeatherObject;
import com.googlecode.android.widgets.DateSlider.timeview.IRenderWeatherCalenderView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.activity.AbstractWeatherActivity;
import com.weatheraggregator.activity.DetailWeatherActivity;
import com.weatheraggregator.activity.MainActivity;
import com.weatheraggregator.adapter.ForecastServiceAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.events.EventUpdateForecast;
import com.weatheraggregator.events.EventVoteWeatherService;
import com.weatheraggregator.interfaces.IUpdateFavoriteService;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.model.DrawerMenuItem;
import com.weatheraggregator.model.ForecastInfoKey;
import com.weatheraggregator.util.DateHelper;
import com.weatheraggregator.util.ServiceVoteHelper;
import com.weatheraggregator.util.UserSelectDateCasher;
import com.weatheraggregator.util.WeatherCache;
import com.weatheraggregator.view.ServiceFavouriteView;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.weather_service_list)
public class WeatherServiceListFragment extends BaseWeatherForecastFragment
	implements IUpdateFavoriteService {
    public static final String TAG = "WeatherServiceListFragment";
    public static final String F_POSITION = "position";
    public static final String F_CITY_ID = "cityId";
    // private static final String F_FRAGMENT_PAGER_POSITION =
    // "fragment_position";
    private City mCurrentCity;
    private Date mCurrentDate = new Date();
    private WeatherService mFavoriteService;
    private ForecastServiceAdapter mForecastAdapter;
    private List<WeatherService> mServices = new ArrayList<WeatherService>();
    private int mSelectListItemPosition;
    private int mSelectTopItemPosition;
    private int mFragmentPosition;

    @ViewById(R.id.dateSliderContainer)
    protected SliderContainer mDateSliderContainer;

    @ViewById(R.id.serviceFavourite)
    protected ServiceFavouriteView mServiceFavourite;

    @ViewById(R.id.lvWeather)
    protected ListView lvWeather;

    public WeatherServiceListFragment() {
    }

    public static WeatherServiceListFragment getNewInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	WeatherServiceListFragment fragment = new WeatherServiceListFragment_();
	fragment.setArguments(bundle);
	return fragment;
    }

    @UiThread
    protected void initAdapter(List<WeatherService> listOfServices) {
	if (getActivity() != null) {
	    mForecastAdapter = new ForecastServiceAdapter(getActivity(),
		    listOfServices, mCurrentCity, mCurrentDate);
	    lvWeather.setAdapter(mForecastAdapter);
	}
	if (mSelectListItemPosition <= listOfServices.size() - 1) {
	    lvWeather.setSelectionFromTop(mSelectListItemPosition,
		    mSelectTopItemPosition);
	}
    }

    @ItemClick(R.id.lvWeather)
    protected void onServiceItemClick(int position) {
	if (mServices != null && !mServices.isEmpty()
		&& position < mServices.size()) {
	    gotoDetailWeatherForecast(mServices.get(position).getObjectId());
	}
    }

    @Click(R.id.serviceFavourite)
    protected void onServiceFavoriteClick(View v) {
	if (mFavoriteService != null) {
	    gotoDetailWeatherForecast(mFavoriteService.getObjectId());
	}
    }

    private void setSelectDateToDateSlider(Long selectDate) {
	mDateSliderContainer.setTime(Calendar.getInstance());
	if (selectDate != null) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(selectDate.longValue());
	    mDateSliderContainer.setTime(calendar);
	}
    }

    @UiThread
    protected void initDateSlider() {
	mServiceFavourite.setDate(Calendar.getInstance().getTime());
	mDateSliderContainer.setOnTimeChangeListener(mDateSlideListener);
	if (mCurrentCity != null) {
	    setSelectDateToDateSlider(UserSelectDateCasher.getSingleton()
		    .getDate(mCurrentCity.getObjectId()));
	}
	mDateSliderContainer
		.setOnRenderWeatherCalendarView(new IRenderWeatherCalenderView() {
		    @Override
		    public WeatherObject renderCalendarItem(Date date,
			    final ImageView ivCloudnes) {
			WeatherObject data = getDateSliderItem(date,
				ivCloudnes, mCurrentCity, mFavoriteService);
			return data;
		    }
		});
	mDateSliderContainer.refresh();
    }

    @Override
    public void onResume() {
	initData();
	super.onResume();
    }

    private boolean mIsInitViewRun = false;

    @AfterViews
    protected void initView() {
	if (mIsInitViewRun) {
	    return;
	}
	mIsInitViewRun = true;
	initData();
    }

    protected void initServicesList() {
	if (getActivity() != null) {
	    mServices = new Select()
		    .from(WeatherService.class)
		    .where(String.format("%s = ? and %s = ?",
			    WeatherService.F_ISDELETE,
			    WeatherService.F_IS_FAVORITE), 0, 0)
		    .orderBy(WeatherService.F_ORDER).execute();
	}
	initAdapter(mServices);
    }

    @Override
    @Background
    protected void initData() {
	loadDataFromBundle();
	initForecastData(mCurrentCity, mCurrentDate);
	initServicesList();
	initDateSlider();
	Log.e(TAG, "EventUpdateForecast1 " + mCurrentCity.getName());
    }

    private void loadDataFromBundle() {
	String cityId = getArguments().getString(F_CITY_ID);
	mCurrentCity = new Select().from(City.class)
		.where(String.format("%s = ?", City.F_OBJECT_ID), cityId)
		.executeSingle();
	mFragmentPosition = getArguments().getInt(F_POSITION);
    }

    /**
     * Update favorite forecast view with data
     * 
     * @param city
     *            - current city
     * @param date
     *            - current date
     */
    protected void initForecastData(City city, Date date) {
	initFavoriteWeatherService();
	Forecast forecast = WeatherCache.getInstance().get(
		new ForecastInfoKey(city.getObjectId(), mFavoriteService
			.getObjectId(), DateHelper.clearTime(date)));
	if (forecast == null) {
	    loadForecastIfNeed(date);
	}
	updateFavoriteForecastView(forecast);
	initAdapter(mServices);
    }

    private void loadForecastIfNeed(Date date) {
	loadForecast(date);
    }

    // TODO: All query need start not in UI thread
    private void initFavoriteWeatherService() {
	mFavoriteService = new Select().from(WeatherService.class)
		.where(String.format("%s = 1", WeatherService.F_IS_FAVORITE))
		.executeSingle();
	if (mFavoriteService == null) {
	    mFavoriteService = new Select().from(WeatherService.class)
		    .where(String.format("%s = 0", WeatherService.F_ORDER))
		    .executeSingle();
	}
    }

    private OnTimeChangeListener mDateSlideListener = new OnTimeChangeListener() {
	@Override
	public void onTimeChange(Calendar time) {

	}

	@Override
	public void onTimeChangeSelected(Calendar time) {
	    mCurrentDate = time.getTime();
	    mServiceFavourite.setDate(mCurrentDate);
	    initForecastData(mCurrentCity, mCurrentDate);
	    UserSelectDateCasher.getSingleton().putUserSelectDate(
		    mCurrentCity.getObjectId(), time.getTime().getTime());
	}
    };

    /**
     * Update favorite forecast view
     */
    @UiThread
    public void updateFavoriteForecastView(Forecast forecast) {
	if (mServiceFavourite != null) {
	    mServiceFavourite.initView(forecast, mFavoriteService,
		    mCurrentDate, mCurrentCity);
	    mServiceFavourite.setServiceName(mFavoriteService.getName());
	}
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
	EventBus.getDefault().unregister(this);
	super.onDetach();
    }

    /**
     * Open DetailWeatherActivity
     * 
     * @param objectId
     *            - service object id
     */
    private void gotoDetailWeatherForecast(final String objectId) {
	com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_(
		getActivity());
	Intent intent = builder.get();
	intent.putExtra(AbstractWeatherActivity.F_TYPE_ITEM,
		DrawerMenuItem.SERVICE_ITEM);
	intent.putExtra(AbstractWeatherActivity.F_OBJECT_ID, objectId);

	intent.putExtra(MainActivity.F_CITY_POSITION,
		((MainActivity) getActivity()).getSelectionCity());
	if (mCurrentDate != null) {
	    intent.putExtra(DetailWeatherActivity.F_DATE_IN_MILLISECONDS,
		    mCurrentDate.getTime());
	}
	builder.startForResult(MainActivity.DETAIL_ACTIVITY_CODE);
    }

    @Background
    protected void saveRating(EventVoteWeatherService event) {
	if (event != null
		&& getActivity() != null
		&& mCurrentCity != null
		&& event.getCityId().equalsIgnoreCase(
			mCurrentCity.getObjectId())) {
	    ServiceRating rating = ServiceVoteHelper.saveVote(event.isVote(),
		    mCurrentDate, event.getCityId(), event.getServiceId());
	    ((AbstractWeatherActivity) getActivity()).getManager().setRating(
		    rating, new IDataSourceServiceListener.Stub() {
			@Override
			public void callBack(int statusCode)
				throws RemoteException {
			    if (getActivity() != null) {
				initServicesList();
				initForecastData(mCurrentCity, mCurrentDate);
			    }
			}
		    });
	}
    }

    public void onEventMainThread(EventUpdateForecast event) {
	if (getActivity() != null) {
	    if (getActivity() instanceof MainActivity
		    && ((MainActivity) getActivity())
			    .getCurrentViewPagerPosition() == mFragmentPosition) {
		if (mForecastAdapter != null) {
		    mForecastAdapter.notifyDataSetChanged();
		}
		if (mDateSliderContainer != null) {
		    mDateSliderContainer.refresh();
		}
		initForecastData(mCurrentCity, mCurrentDate);
		Log.e(TAG, "EventUpdateForecast " + mCurrentCity.getName());
	    }
	}
    }

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {
	// hide keyboard if need
    }

    public void onEventMainThread(EventVoteWeatherService event) {
	if (getActivity() != null) {
	    saveRating(event);
	    mSelectListItemPosition = lvWeather.getFirstVisiblePosition();
	    View v = lvWeather.getChildAt(0);
	    mSelectTopItemPosition = (v == null) ? 0 : v.getTop();
	}
    }
}

package com.weatheraggregator.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.lucasr.twowayview.ISelectItem;
import org.lucasr.twowayview.TwoWayView;
import org.lucasr.twowayview.TwoWayView.OnScrollListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weatheraggregator.activity.AbstractWeatherActivity;
import com.weatheraggregator.activity.DetailWeatherActivity;
import com.weatheraggregator.adapter.TimeForecastAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.Forecast.Cloudiness;
import com.weatheraggregator.entity.ServiceRating;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.events.EventChangeWebService;
import com.weatheraggregator.events.EventChangedCity;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.interfaces.IUpdateFavoriteService;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.localservice.IDetailDataSourceServiceListener;
import com.weatheraggregator.util.DateHelper;
import com.weatheraggregator.util.DialogValidationTextMessagesManager;
import com.weatheraggregator.util.ForecastHelperLoader;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.ServiceVoteHelper;
import com.weatheraggregator.util.UserSelectDateCasher;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.view.DetailWeatherParameterView;
import com.weatheraggregator.view.TimeForecastView;
import com.weatheraggregator.webservice.exception.ErrorType;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.detail_weather_fragment)
public class DetailWeatherFragment extends BaseWeatherForecastFragment
	implements IUpdateFavoriteService {
    public static final String TAG = "DetailWeaherFragment";
    public static final String F_POSITION = "position";
    public static final String F_CITY_ID = "cityId";
    public static final String F_SERVICE_ID = "serviceId";
    private final int TIME_WAIT_FOR_RENDER = 300;

    @ViewById(R.id.tvWeatherServiceName)
    protected TextView mTvvWeatherServiceName;
    @ViewById(R.id.tvAgree)
    protected TextView mTvAgree;
    @ViewById(R.id.tvDisagree)
    protected TextView mTvDisagree;
    @ViewById(R.id.tvDate)
    protected TextView mTvDate;
    @ViewById(R.id.detailWeatherParametrView)
    protected DetailWeatherParameterView mDetailWeatherView;

    @ViewById(R.id.lvTimeForecast)
    protected TwoWayView mLvTimeForecast;
    private TimeForecastAdapter mTimeForecastAdapter;

    @ViewById(R.id.btnAgree)
    protected ImageButton mBtnAgree;
    @ViewById(R.id.btnDisAgree)
    protected ImageButton mBtnDisAgree;

    private City mCurrentCity;
    private Date mCurrentDate = new Date();
    private int mPosition;
    private WeatherService mWeatherService;
    private List<Forecast> mListOfForecasts;

    @ViewById(R.id.tvTemp)
    protected TextView tvTemp;
    @ViewById(R.id.tvSunriseValue)
    protected TextView tvSunriseValue;
    @ViewById(R.id.tvSunsetValue)
    protected TextView tvSunsetValue;
    @ViewById(R.id.ivCloudy)
    protected ImageView ivCloudy;
    @ViewById(R.id.tvCloudnessType)
    protected TextView tvCloudnessType;
    private DisplayImageOptions mOptions;

    private int mSelectItem = 0;

    public static DetailWeatherFragment getNewInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	DetailWeatherFragment fragment = new DetailWeatherFragment_();
	fragment.setArguments(bundle);
	return fragment;
    }

    protected DisplayImageOptions getDisplayOption() {
	if (mOptions == null) {
	    mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
		    .resetViewBeforeLoading(true).build();
	}
	return mOptions;
    }

    private void updateTopView(Forecast forecast) {
	String na = getString(R.string.na);
	if (forecast != null) {
	    tvTemp.setText(MeasureUnitHelper.getSingleton().convertTemperature(
		    forecast.getTemp(), getActivity()));
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    if (forecast.getSunrice() != null) {
		tvSunriseValue.setText(sdf.format(forecast.getSunrice()));
	    } else {
		tvSunriseValue.setText(na);
	    }
	    if (forecast.getSunset() != null) {
		tvSunsetValue.setText(sdf.format(forecast.getSunset()));
	    } else {
		tvSunsetValue.setText(na);
	    }
	    tvCloudnessType.setText(Util.getCloudyStringValue(getActivity(),
		    forecast.getCondition()));
	    ImageLoader.getInstance().displayImage(
		    Util.getCloudyImagePath(forecast.getCondition()), ivCloudy,
		    getDisplayOption());
	} else {
	    ImageLoader.getInstance().displayImage(
		    Util.getCloudyImagePath(Cloudiness.N_A), ivCloudy,
		    getDisplayOption());
	    tvTemp.setText(na);
	    tvSunriseValue.setText(na);
	    tvSunsetValue.setText(na);
	    tvCloudnessType.setText(na);
	}
    }

    @Override
    public void onResume() {
	initData();
	super.onResume();
    }

    private void updateFavoriteView(int position) {
	if (mListOfForecasts != null && mListOfForecasts.size() > position
		&& position >= 0) {
	    updateFavoriteForecastView(mListOfForecasts.get(position));
	} else {
	    updateFavoriteForecastView(null);
	}
    }

    private void initTimeSlider() {
	mLvTimeForecast.setHorizontalScrollBarEnabled(false);
	mLvTimeForecast.setSelectListener(new ISelectItem() {
	    @Override
	    public void onSelectItem(int position) {
		updateFavoriteView(position);
	    }

	});
	mLvTimeForecast.setOnScrollListener(new OnScrollListener() {
	    @Override
	    public void onScrollStateChanged(TwoWayView view, int scrollState) {
	    }

	    private View getSelectedView(TwoWayView view) {
		int next = 3;
		int select = 2;
		int previous = 1;
		View selectView;
		View previousView;
		View nextView;

		nextView = view.getChildAt(next);
		previousView = view.getChildAt(previous);
		selectView = view.getChildAt(select);
		int center = view.getLeftBorder() - view.getDefaultChildWidth()
			/ 2;

		if (center > nextView.getLeft()) {
		    selectView = nextView;
		}

		if (center < previousView.getLeft()) {
		    selectView = previousView;
		}
		return selectView;
	    }

	    @Override
	    public void onScroll(TwoWayView view, int firstVisibleItem,
		    int visibleItemCount, int totalItemCount) {
		if (!view.isTouchUp()) {
		    if (view.getChildCount() != 0) {
			View selectView = getSelectedView(view);
			view.setSelectItem(selectView);
			for (int position = 0; view.getChildCount() > position; position++) {
			    TimeForecastView item = (TimeForecastView) view
				    .getChildAt(position);
			    LayoutParams params = item.getLayoutParams();
			    params.width = view.getDefaultChildWidth();
			    item.setLayoutParams(params);

			    if (selectView == item) {
				item.selectState();
			    } else {
				item.defaultState();
			    }
			}
		    }
		} else {
		    view.setTouchUp(false);
		}
	    }
	});
    }

    @UiThread
    protected void uptadeForecastInformation(List<Forecast> forecasts) {
	if (getActivity() != null) {
	    mTimeForecastAdapter = new TimeForecastAdapter(getActivity(),
		    forecasts);
	    mLvTimeForecast.setAdapter(mTimeForecastAdapter);
	    mLvTimeForecast.postDelayed(new Runnable() {
		@Override
		public void run() {
		    mLvTimeForecast.setSelection(mSelectItem);
		    updateFavoriteView(mSelectItem);
		}
	    }, TIME_WAIT_FOR_RENDER);
	    initEnableRating(mCurrentCity, mWeatherService);
	}
    }

    private boolean mIsInitViewRun = false;

    @SuppressLint("SimpleDateFormat")
    @AfterViews
    protected void initView() {
	if (mIsInitViewRun) {
	    return;
	}
	mIsInitViewRun = true;
	loadDataFromBundle();
	// loadForecast();
    }

    private void initDetailForecast() {
	mListOfForecasts = new Select()
		.from(Forecast.class)
		.where(String.format(
			"(%s =? or %s =? or %s = ? or %s =?) and %s =? and %s=?",
			Forecast.F_FORECAST_DAY, Forecast.F_FORECAST_DAY,
			Forecast.F_FORECAST_DAY, Forecast.F_FORECAST_DAY,
			Forecast.F_CITY_ID, Forecast.F_SERVICE_ID),
			DateHelper.getDateWithHour(mCurrentDate, 4).getTime()
				.getTime(),
			DateHelper.getDateWithHour(mCurrentDate, 10).getTime()
				.getTime(),
			DateHelper.getDateWithHour(mCurrentDate, 18).getTime()
				.getTime(),
			DateHelper.getDateWithHour(mCurrentDate, 22).getTime()
				.getTime(), mCurrentCity.getObjectId(),
			mWeatherService.getObjectId())
		.orderBy(Forecast.F_FORECAST_DAY)
		.groupBy(Forecast.F_FORECAST_DAY).execute();

	if (mListOfForecasts == null) {
	    mListOfForecasts = new ArrayList<Forecast>();
	}
	prepareForecastList(mListOfForecasts);
	uptadeForecastInformation(mListOfForecasts);
    }

    @Background
    protected void loadForecast() {
	// initDetailForecast();
	loadForecastFromService();
    }

    private void initCurrentForecast() {
	if (mListOfForecasts != null) {
	    mListOfForecasts.get(mSelectItem).setDetailTitle(
		    getString(R.string.now));
	    Calendar calendar = Calendar.getInstance();
	    for (int index = 0; index < mListOfForecasts.size(); index++) {
		Forecast forecast = mListOfForecasts.get(index);
		calendar.setTime(forecast.getForecastDay());
		int min = calendar.get(Calendar.MINUTE);

		if (min == 0) {// || hour == 6 || hour == 12 || hour == 18
		    forecast.setCurrent(false);
		}

		if (mListOfForecasts.get(index).isCurrent()) {
		    mListOfForecasts.get(index).setDetailTitle(
			    getString(R.string.now));
		    mSelectItem = index;
		}
	    }
	}
    }

    private void loadForecastFromService() {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(mCurrentDate);
	// calendar.add(Calendar.HOUR, 6);
	((AbstractWeatherActivity) getActivity()).getManager()
		.loadDetailForecast(mWeatherService.getObjectId(),
			mCurrentCity.getObjectId(), calendar.getTime(),
			new IDetailDataSourceServiceListener.Stub() {

			    @Override
			    public void callBack(int statusCode,
				    List<Forecast> detailForecast)
				    throws RemoteException {
				if (statusCode == ErrorType.HTTP_OK.getCode()) {
				    if (detailForecast != null)
					if (mListOfForecasts == null
						|| mListOfForecasts.isEmpty()
						|| !mListOfForecasts
							.equals(detailForecast)) {
					    mListOfForecasts = detailForecast;
					    initCurrentForecast();
					    uptadeForecastInformation(mListOfForecasts);
					}
				} else {
				    showToast(ErrorType.values()[statusCode]);
				}
			    }
			});
    }

    private int getCurrentForecastPosition() {
	Calendar calendar = Calendar.getInstance();
	int hour = calendar.get(Calendar.HOUR_OF_DAY);
	return Util.getHourApproximation(hour) - 1;
    }

    private Forecast getForecast() {
	List<Forecast> currentForecast = new Select()
		.from(Forecast.class)
		.where(String.format(
			" %s = ? and %s = ?and  %s BETWEEN ? and ? ",
			Forecast.F_CITY_ID, Forecast.F_SERVICE_ID,
			Forecast.F_FORECAST_DAY),
			mCurrentCity.getObjectId(),
			mWeatherService.getObjectId(),
			DateHelper.getStartDateByDate(mCurrentDate).getTime()
				.getTime(),
			DateHelper.getEndDateByDate(mCurrentDate).getTime()
				.getTime()).groupBy(Forecast.F_FORECAST_DAY)
		.orderBy(Forecast.F_FORECAST_DAY).execute();

	ForecastHelperLoader helper = new ForecastHelperLoader();

	Forecast forecast = helper.getForecastNearCurrentTime(currentForecast,
		getCurrentTime());
	Forecast actualForecast = null;
	try {
	    if (forecast != null) {
		actualForecast = forecast.clone();
		actualForecast.setDetailTitle(getString(R.string.now));
		actualForecast.setCurrent(true);
	    }
	} catch (CloneNotSupportedException e) {
	    Log.e(TAG, e.getMessage());
	}
	return actualForecast;
    }

    private Date getCurrentTime() {
	Calendar current = Calendar.getInstance();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(mCurrentDate);
	calendar.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY));
	calendar.set(Calendar.MINUTE, current.get(Calendar.MINUTE));
	return calendar.getTime();
    }

    private void prepareForecastList(List<Forecast> forecasts) {
	int detailValueCount = 4;
	if (forecasts != null && forecasts.size() == detailValueCount) {
	    mSelectItem = getCurrentForecastPosition();
	    forecasts.add(mSelectItem, getForecast());
	} else {
	    Forecast curForecast = getForecast();
	    if (curForecast != null) {
		forecasts.add(0, getForecast());
	    }
	    // mLvTimeForecast.central();
	    mSelectItem = 1;
	}
    }

    // TODO: use next sprint
    private void initTimeForecast() {
	((DetailWeatherActivity) getActivity()).getManager()
		.loadForecastByDate(mCurrentDate,
			new IDataSourceServiceListener.Stub() {
			    @Override
			    public void callBack(int statusCode)
				    throws RemoteException {
				// uptadeForecastInformation();
			    }
			});
    }

    protected void initData() {
	displayRating();
	updateTopView(null);
	updateFavoriteForecastView(null);
	if (mWeatherService != null) {
	    mTvvWeatherServiceName.setText(mWeatherService.getName());
	}
	mTvDate.setText(new SimpleDateFormat("EEEE,  MMMM dd, yyyy")
		.format(mCurrentDate));
	initTimeSlider();
	loadForecast();
	UserSelectDateCasher.getSingleton().putUserSelectDate(
		mCurrentCity.getObjectId(),
		((DetailWeatherActivity) getActivity()).mAdapter
			.getDate(((DetailWeatherActivity) getActivity()).mPager
				.getCurrentItem()));
    }

    /**
     * Displays rating of weather service
     */
    @UiThread
    protected void displayRating() {
	if (mWeatherService != null && mWeatherService.getVotedFor() != null
		&& mWeatherService.getVotedAgainst() != null) {
	    mTvAgree.setText(mWeatherService.getVotedFor().toString());
	    mTvDisagree.setText(mWeatherService.getVotedAgainst().toString());
	}
    }

    private void loadDataFromBundle() {
	mPosition = getArguments().getInt(F_POSITION);
	final String cityId = getArguments().getString(F_CITY_ID);
	if (cityId != null) {
	    mCurrentCity = new Select().from(City.class)
		    .where(String.format("%s =?", City.F_OBJECT_ID), cityId)
		    .executeSingle();
	}
	final String serviceId = getArguments().getString(F_SERVICE_ID);
	if (serviceId != null) {
	    mWeatherService = new Select()
		    .from(WeatherService.class)
		    .where(String.format("%s = ?", WeatherService.F_OBJECT_ID),
			    serviceId).executeSingle();
	}
	mCurrentDate = new Date(getArguments().getLong(
		DetailWeatherActivity.F_DATE_IN_MILLISECONDS,
		new Date().getTime()));
    }

    private void initEnableRating(City city, WeatherService service) {
	ServiceRating serviceRating = ServiceVoteHelper.getServiceRating(city,
		service, mCurrentDate);
	boolean enabled = serviceRating == null && mListOfForecasts != null
		&& !mListOfForecasts.isEmpty();
	setEnableRatingButtons(enabled);
    }

    private void setEnableRatingButtons(boolean enabled) {
	mBtnAgree.setEnabled(enabled);
	mBtnDisAgree.setEnabled(enabled);
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
	super.onDetach();
	EventBus.getDefault().unregister(this);
    }

    @UiThread
    public void updateFavoriteForecastView(Forecast forecast) {
	if (mDetailWeatherView != null) {
	    mDetailWeatherView.init(forecast);
	}
	updateTopView(forecast);
    }

    @Click({ R.id.btnAgree, R.id.btnDisAgree })
    protected void clickRating(View view) {
	if (mWeatherService != null) {
	    int votedFor = mWeatherService.getVotedFor().intValue();
	    int votedAgainst = mWeatherService.getVotedAgainst().intValue();
	    Boolean vote = false;
	    switch (view.getId()) {
	    case R.id.btnAgree:
		votedFor++;
		vote = true;
		break;
	    case R.id.btnDisAgree:
		votedAgainst++;
		break;
	    }
	    updateServiceWithNewRating(votedFor, votedAgainst);
	    ServiceRating serviceRating = ServiceVoteHelper.saveVote(vote,
		    mCurrentDate, mCurrentCity.getObjectId(),
		    mWeatherService.getObjectId());
	    setEnableRatingButtons(false);
	    ((com.weatheraggregator.activity.DetailWeatherActivity_) getActivity())
		    .getManager().setRating(serviceRating,
			    new IDataSourceServiceListener.Stub() {
				@Override
				public void callBack(int statusCode)
					throws RemoteException {
				    displayRating();
				}
			    });
	}
    }

    private void updateServiceWithNewRating(final int votedFor,
	    final int votedAgainst) {
	if (mWeatherService != null) {
	    mWeatherService.setVotedFor(Integer.valueOf(votedFor));
	    mWeatherService.setVotedAgainst(Integer.valueOf(votedAgainst));

	    mWeatherService.save(mWeatherService
		    .getLocalObjectIdByServiceObjectId());
	}
    }

    public void onEventMainThread(EventChangedCity event) {
	if (event != null & getActivity() != null) {
	    mCurrentCity = event.getCity();
	    loadForecast();
	    initEnableRating(mCurrentCity, mWeatherService);
	}
    }

    public void onEventMainThread(EventChangeWebService event) {
	if (event != null && getActivity() != null) {
	    if (mPosition == event.getPosition()) {
		mWeatherService = event.getWeatherService();
	    }
	}
    }

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {
	// Hide keyboard if need
    }
}

package com.weatheraggregator.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.android.gms.internal.bt;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.mobeta.android.dslv.DragSortListView;
import com.weatheraggregator.activity.BaseActivity;
import com.weatheraggregator.activity.SettingsContentActivity;
import com.weatheraggregator.activity.SettingsContentActivity.SettingType;
import com.weatheraggregator.adapter.CitySettingsMenuAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.events.EventRemoveItemFromDragAndDropListView;
import com.weatheraggregator.events.EventUpdateDataFromLocation;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.util.PreferenceMapper;
import com.weatheraggregator.webservice.exception.ErrorType;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.setting_cities_fragment)
public class SettingCitiesListFragment extends SettingBaseFragment {
    public static final String TAG = "SettingCitiesListFragment";
    public final static int MAX_COUNT_CITY = 5;
    @ViewById(R.id.lvCities)
    protected DragSortListView mLvCities;
    @ViewById(R.id.tbLocation)
    protected ToggleButton mTbLocation;
    @ViewById(R.id.tvEmpty)
    protected TextView mTvEmptyMessage;

    private List<City> listOfCities = new ArrayList<City>();
    private CitySettingsMenuAdapter mAdapter;
    private boolean isOrder = false;

    public SettingCitiesListFragment() {
	super();
    }

    private int getCountCity() {
	Cursor cursor = Cache.openDatabase().rawQuery(
		"SELECT _id  FROM City WHERE is_delete = 0", null);
	if (cursor != null) {
	    int order = cursor.getCount();
	    cursor.close();
	    if (order != 0) {
		return order;
	    }
	}
	return 0;
    }

    private boolean isCanAddCity() {
	if (getCountCity() >= MAX_COUNT_CITY) {
	    return false;
	}
	return true;
    }

    public static SettingCitiesListFragment getNewInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	SettingCitiesListFragment fragment = new SettingCitiesListFragment_();
	fragment.setArguments(bundle);
	return fragment;
    }

    private void diaplayEmptyInforTitle() {
	mTvEmptyMessage.setVisibility(View.VISIBLE);
	if (mAdapter != null) {
	    if (mAdapter.getCount() > 0) {
		mTvEmptyMessage.setVisibility(View.GONE);
	    }
	}
    }

    @UiThread
    protected void initAdapter() {
	if (listOfCities != null) {
	    mAdapter = new CitySettingsMenuAdapter(getActivity(), listOfCities);
	    mLvCities.setAdapter(mAdapter);
	} else if (mAdapter != null) {
	    mAdapter.clearData();
	}
	diaplayEmptyInforTitle();
    }

    @Background
    protected void deleteCityFromFavorite(City city) {
	city.setDelete(true);
	city.setSync(false);
	city.save();
	// Need call after set property delete = true
	((BaseActivity) getActivity()).getManager().removeCityFromFavourite(
		city, new IDataSourceServiceListener.Stub() {
		    @Override
		    public void callBack(int statusCode) throws RemoteException {
			showToast(ErrorType.values()[statusCode]);
		    }
		});
	initCitiesList();
	reOrderAsync(listOfCities);
    }

    @Background
    protected void initCitiesList() {
	listOfCities = new Select(City.F_NAME, City.F_OBJECT_ID, City.F_REGION,
		City.F_COUNTRY, BaseColumns._ID).from(City.class)
		.orderBy(City.F_ORDER)
		.where(String.format("%s = ?", City.F_IS_DELETED), 0).execute();
	if (getActivity() != null)
	    initAdapter();
    }

    private void initEnableAddNewCity() {
	mLLAddNew.setEnabled(isCanAddCity());
    }

    @Override
    protected void initData() {
	initCitiesList();
	sendFavorite();
	initEnableAddNewCity();
	mTvEmptyMessage.setVisibility(View.VISIBLE);
    }

    @Background
    protected void sendFavorite() {
	List<City> cities = new Select()
		.from(City.class)
		.where(String.format(" %s = ?", City.F_OBJECT_ID),
			City.DEFAULT_CITY_ID).execute();
	if (cities != null && !cities.isEmpty()) {
	    getActivity().setResult(Activity.RESULT_OK);
	    for (int cityIndex = 0; cityIndex < cities.size(); cityIndex++) {
		City city = cities.get(cityIndex);
		((BaseActivity) getActivity()).getManager().addToFavorite(city,
			new IDataSourceServiceListener.Stub() {
			    @Override
			    public void callBack(int statusCode)
				    throws RemoteException {
				initCitiesList();
			    }
			});
	    }
	}
    }

    @Override
    public void onResume() {
	initData();
	super.onResume();
    }

    @AfterViews
    protected void initView() {
	mLvCities.setDropListener(onDrop);
	mLvCities.setRemoveListener(onRemove);
	mLvCities.setDragScrollProfile(ssProfile);
	mLvCities.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	mTbLocation
		.setOnCheckedChangeListener(mOnLocationCheckedChangeListener);
	mTbLocation.setChecked(PreferenceMapper.isUseLocation(getActivity()));
	initData();
    }

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
	@Override
	public void remove(int which) {
	    // mAdapter.remove(mAdapter.getItem(which));
	    mAdapter.notifyDataSetChanged();
	    initEnableAddNewCity();
	    // deleteCityFromFavorite(mAdapter.getItem(which));
	}
    };

    private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
	@Override
	public float getSpeed(float w, long t) {
	    if (w > 0.8f) {
		// Traverse all views in a millisecond
		return ((float) mAdapter.getCount()) / 0.001f;
	    } else {
		return 10.0f * w;
	    }
	}
    };

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
	@Override
	public void drop(int from, int to) {
	    if (from == to) {
		return;
	    }
	    initEnableAddNewCity();
	    City item = mAdapter.getItem(from);
	    mAdapter.remove(item);
	    mAdapter.insert(item, to);
	    mAdapter.notifyDataSetChanged();

	    List<City> cities = mAdapter.getListOfCities();
	    reOrderAsync(new ArrayList<City>(cities));
	}
    };

    @Background
    protected void reOrderAsync(List<City> cities) {
	reOrder(cities);
    }

    private void reOrder(List<City> cities) {
	try {
	    // Log.e(TAG, "reOrder");
	    ActiveAndroid.beginTransaction();
	    if (cities != null && !cities.isEmpty()) {
		isOrder = true;
		for (int indexCity = 0; indexCity < cities.size(); indexCity++) {
		    City city = cities.get(indexCity);
		    city.setOrder(indexCity);
		    // Log.e(TAG, String.format("%s = %d, id = %d",
		    // city.getName(), city.getOrder(), city.getId()));
		    city.save(city.getLocalObjectIdByServiceObjectId());
		}
		ActiveAndroid.setTransactionSuccessful();
		// Log.e(TAG, "setTransactionSuccessful");
	    }
	} finally {
	    ActiveAndroid.endTransaction();
	}
    }

    @ViewById(R.id.llAddNew)
    protected LinearLayout mLLAddNew;

    @Click(R.id.llAddNew)
    protected void onAddNewCity() {
	com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_(
		getActivity());
	Intent intent = builder.get();
	intent.putExtra(SettingsContentActivity.F_SETTING,
		SettingType.CITY.getCode());
	builder.start();
    }

    private OnCheckedChangeListener mOnLocationCheckedChangeListener = new OnCheckedChangeListener() {

	@Override
	public void onCheckedChanged(CompoundButton buttonView,
		boolean isChecked) {
	    if (isChecked) {
		((BaseActivity) getActivity()).startUpdateLocation();
	    } else {
		((BaseActivity) getActivity()).stopUpdateLocation();
		deleteLocationData();
	    }
	}
    };

    @Background
    protected void deleteLocationData() {
	ActiveAndroid.beginTransaction();
	try {
	    Delete deleteCity = new Delete();
	    deleteCity
		    .from(City.class)
		    .where(String.format("%s = ?", City.F_OBJECT_ID),
			    City.LOCATION_CITY_ID).execute();

	    Delete deleteForecast = new Delete();
	    deleteForecast
		    .from(Forecast.class)
		    .where(String.format("%s = ?", Forecast.F_CITY_ID),
			    City.LOCATION_CITY_ID).execute();
	    ActiveAndroid.setTransactionSuccessful();
	} finally {
	    ActiveAndroid.endTransaction();
	}
	listOfCities = new Select(City.F_NAME, City.F_OBJECT_ID, City.F_REGION,
		City.F_COUNTRY, BaseColumns._ID).from(City.class)
		.orderBy(City.F_ORDER)
		.where(String.format("%s = ?", City.F_IS_DELETED), 0).execute();
	reOrder(listOfCities);
	initCitiesList();
    }

    @Override
    public void onSaveData() {
	if (isOrder) {
	    ((BaseActivity) getActivity()).getManager().reorderCity(null);
	}
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

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {

    }

    public void onEventMainThread(EventRemoveItemFromDragAndDropListView event) {
	if (event != null && mAdapter != null) {
	    City city = mAdapter.getItem(event.getPosition());
	    deleteCityFromFavorite(city);
	}
    }

    public void onEventMainThread(EventUpdateDataFromLocation event) {
	if (PreferenceMapper.isUseLocation(getActivity())) {
	    initData();
	} else {
	    deleteLocationData();
	}
    }

}

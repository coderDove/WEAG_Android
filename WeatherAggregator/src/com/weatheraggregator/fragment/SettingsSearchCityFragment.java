package com.weatheraggregator.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.activity.BaseActivity;
import com.weatheraggregator.adapter.SettingSearchCityAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.util.DialogManager;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.view.ViewSearch;
import com.weatheraggregator.view.ViewSearch.ISearchCity;
import com.weatheraggregator.webservice.ServiceManager;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

@EFragment(R.layout.setting_search_city_fragment)
public class SettingsSearchCityFragment extends SettingBaseFragment {
    public static final String TAG = "SettingsSearchCityFragment";

    @ViewById(R.id.lvSerchCities)
    protected ListView mLvSearch;
    @ViewById(R.id.searchView)
    protected ViewSearch mSearchView;
    @ViewById(R.id.tvEmpty)
    protected TextView mTvEmptyMessage;

    private List<City> mListOfCities = new ArrayList<City>();

    private AsyncTask<String, Void, List<City>> searchTask = null;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SettingSearchCityAdapter mAdapter;

    @Override
    public void onDetach() {
	mExecutor.shutdownNow();
	super.onDetach();
    }

    public static SettingsSearchCityFragment getNewInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	SettingsSearchCityFragment fragment = new SettingsSearchCityFragment_();
	fragment.setArguments(bundle);
	return fragment;
    }

    @UiThread
    protected void initAdapter(List<City> listOfCities) {
	if (listOfCities != null && !listOfCities.isEmpty())
	    mTvEmptyMessage.setVisibility(View.GONE);
	updateAdapter(listOfCities);
    }

    public void updateAdapter(List<City> listOfCities) {
	if (listOfCities != null) {
	    mAdapter = new SettingSearchCityAdapter(getActivity(), listOfCities);
	    mLvSearch.setAdapter(mAdapter);
	}
    }

    @Override
    @AfterViews
    protected void initData() {
	mTvEmptyMessage.setVisibility(View.VISIBLE);
	mSearchView.setSearchListener(new ISearchCity() {
	    @Override
	    public void searchCityResult(String cityName) {
		searchCities(cityName);
	    }
	});
    }

    private int getCityOrder() {
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

    @Background
    protected void addNewCity(int position) {
	if (mListOfCities != null && !mListOfCities.isEmpty()
		&& getCityOrder() <= SettingCitiesListFragment.MAX_COUNT_CITY) {
	    City city = mListOfCities.get(position);
	    City cityInDB = new Select()
		    .from(City.class)
		    .where(String.format("%s = ? and %s = ? and %s = ?",
			    City.F_NAME, City.F_COUNTRY, City.F_REGION),
			    city.getName() == null ? "" : city.getName(),
			    city.getCountry() == null ? "" : city.getCountry(),
			    city.getRegion() == null ? "" : city.getRegion())
		    .executeSingle();
	    if (cityInDB == null) {
		city.setObjectId(City.DEFAULT_CITY_ID);
		city.setOrder(getCityOrder());
		Log.e(TAG, " " + getCityOrder() + "  " + city.getName());
		city.save();
		sendFavorite(city);
	    }
	}
	doFinish();
    }

    @Background
    protected void sendFavorite(City city) {
	((BaseActivity) getActivity()).getManager().addToFavorite(city,
		new IDataSourceServiceListener.Stub() {
		    @Override
		    public void callBack(int statusCode) throws RemoteException {
		    }
		});
    }

    @UiThread
    protected void doFinish() {
	getActivity().finish();
    }

    @ItemClick(R.id.lvSerchCities)
    public void weatherServiceListItemSelected(int position) {
	if (getCityOrder() >= SettingCitiesListFragment.MAX_COUNT_CITY) {
	    DialogManager.getInstance(getActivity()).makeAlert(
		    R.string.limit_city);
	} else {
	    addNewCity(position);
	}

    }

    @SuppressLint("NewApi")
    protected void searchCities(String searchCity) {
	if (getActivity() == null)
	    return;
	if (searchCity != null) {
	    if (searchTask != null) {
		searchTask.cancel(true);
	    }

	    searchTask = new AsyncTask<String, Void, List<City>>() {

		protected void onPreExecute() {
		    mSearchView.progressVisibility(true);
		    super.onPreExecute();
		}

		@Override
		protected List<City> doInBackground(String... params) {
		    String search = params[0];
		    try {
			if (isCancelled()) {
			    return null;
			}
			if (isCancelled()) {
			    return null;
			}
			mListOfCities.clear();
			initAdapter(mListOfCities);
			return ServiceManager.newInstance().searchCities(
				getActivity(), search);
		    } catch (InternetException e) {
			Log.e(TAG, e.getMessage());
			showToast(e.getErrorStatus());
		    } catch (ParseException e) {
			showToast(e.getErrorStatus());
			Log.e(TAG, e.getMessage());
		    }
		    return null;
		}

		protected void onPostExecute(List<City> result) {
		    mSearchView.progressVisibility(false);

		    if (!isCancelled() && getActivity() != null) {
			if (result != null) {
			    mListOfCities = result;
			} else {
			    mListOfCities.clear();
			}
			initAdapter(mListOfCities);
		    }
		}
	    };

	    if (Util.hasHoneycomb()) {
		searchTask.executeOnExecutor(mExecutor, searchCity);
	    } else {
		searchTask.execute(searchCity);
	    }
	}
    }

    @Override
    public void onSaveData() {
    }

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {
	if (mSearchView != null && mSearchView.getETSearch() != null) {
	    Util.closeSoftKeyboard(mSearchView.getETSearch(), getActivity());
	}
    }

}

package com.weatheraggregator.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.weatheraggregator.events.EventUpdateDataFromLocation;
import com.weatheraggregator.interfaces.ILocationChanged;
import com.weatheraggregator.localservice.ForecastsUpdateReceiver;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.localservice.LocationManager;
import com.weatheraggregator.localservice.RemoteWeatherServiceManager;
import com.weatheraggregator.localservice.WeatherRemoteService;
import com.weatheraggregator.util.PreferenceMapper;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends SherlockFragmentActivity {

    private RemoteWeatherServiceManager mServiceManager;
    private ForecastsUpdateReceiver mReceiver;
    private LocationManager mLocationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	mServiceManager = new RemoteWeatherServiceManager();
	initForecastReceiver();
	mLocationManager = new LocationManager(this, mLocationChanged);
	mLocationManager.onCreate(savedInstanceState);
    }

    private void initForecastReceiver() {
	if (mReceiver == null) {
	    mReceiver = new ForecastsUpdateReceiver();
	}
    }

    /**
     * Create custom ActionBar
     */
    protected void styleActionBar(View view) {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayShowTitleEnabled(false);
	actionBar.setDisplayUseLogoEnabled(false);
	actionBar.setDisplayHomeAsUpEnabled(false);
	actionBar.setDisplayShowCustomEnabled(true);
	actionBar.setDisplayShowHomeEnabled(false);
	actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	actionBar.setCustomView(view);
    }

    private OnClickListener mOnHomeClickListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
	    finish();
	}
    };

    /**
     * 
     * @param view
     *            which set OnClickListener. If you click on view, menu is closed
     */
    public void setHomePageCloseListener(View view) {
	if (view != null) {
	    view.setOnClickListener(mOnHomeClickListener);
	}
    }

    private void registerForecastReceiver() {
	if (mReceiver == null) {
	    initForecastReceiver();
	}
	IntentFilter intentFilter = new IntentFilter();
	intentFilter.addAction(WeatherRemoteService.FORECASTS_NEW_DATA);
	registerReceiver(mReceiver, intentFilter,
		WeatherRemoteService.FORECASTS_NEW_DATA, null);
    }

    @Override
    protected void onStart() {
	super.onStart();
	registerForecastReceiver();
	if (mServiceManager != null) {
	    mServiceManager.bindService(this);
	}
	mLocationManager.onStart();
    }

    @Override
    protected void onStop() {
	if (mServiceManager != null) {
	    mServiceManager.unbindService(this);
	}
	if (mReceiver != null) {
	    unregisterReceiver(mReceiver);
	}
	mLocationManager.onStop();
	super.onStop();
    }

    public RemoteWeatherServiceManager getManager() {
	return mServiceManager;
    }

    private ILocationChanged mLocationChanged = new ILocationChanged() {

	@Override
	public void locationChanged(Location location) {
	    if (location != null&&PreferenceMapper.isUseLocation(BaseActivity.this)) {
		try {
		    getManager().loadCityWithForecastsByLocation(
			    location.getLatitude(), location.getLongitude(),
			    callBackUpdateLocation);
		} catch (RemoteException e) {
		    e.printStackTrace();
		}
	    }
	}
    };

    private IDataSourceServiceListener callBackUpdateLocation = new IDataSourceServiceListener.Stub() {

	@Override
	public void callBack(int statusCode) throws RemoteException {
	    Log.d(LocationManager.class.getSimpleName(),
		    "update data from location");
	    EventBus.getDefault().post(new EventUpdateDataFromLocation());
	}
    };

    @Override
    protected void onResume() {
	super.onResume();
	mLocationManager.onResume();
    }

    @Override
    protected void onPause() {
	mLocationManager.onPause();
	super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	mLocationManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startUpdateLocation() {
	PreferenceMapper.putIsUseLocation(this, true);
	mLocationManager.startUpdateLocation();
    }

    public void stopUpdateLocation() {
	PreferenceMapper.putIsUseLocation(this, false);
	mLocationManager.stopUpdateLocation();
    }

}

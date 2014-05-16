package com.weatheraggregator.localservice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.weatheraggregator.interfaces.ILocationChanged;
import com.weatheraggregator.util.PreferenceMapper;

public class LocationManager implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private ILocationChanged locationChanged;
    private static final String TAG = "LocationManager";
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 90000;
    private static final int UPDATE_INTERVAL = 200000;
    private static final int FAST_INTERVAL = 100000;
    private static final String KEY_UPDATES_ON = "KEY_UPDATES_ON";
    private static final String PREFERENCES_NAME = "LocationManager";

    private SherlockFragmentActivity mActivity;
    private LocationRequest mLocationRequest;
    private boolean mUpdatesRequested;
    private LocationClient mLocationClient;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    public LocationManager(SherlockFragmentActivity activity,
	    ILocationChanged locationChanged) {
	this.mActivity = activity;
	this.locationChanged = locationChanged;
    }

    public void startUpdateLocation() {
	Log.d(TAG, "Start gps tracking");
	PreferenceMapper.putIsUseLocation(mActivity, true);
	mUpdatesRequested = true;
	if (servicesConnected()) {
	    startPeriodicUpdates();
	}
    }

    private void startPeriodicUpdates() {
	if (mLocationClient.isConnected()) {
	    mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}
    }

    public static class ErrorDialogFragment extends DialogFragment {
	private Dialog mDialog;

	public ErrorDialogFragment() {
	    super();
	    mDialog = null;
	}

	public void setDialog(Dialog dialog) {
	    mDialog = dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    return mDialog;
	}
    }

    private void showErrorDialog(int errorCode) {
	Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
		mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

	if (errorDialog != null) {

	    ErrorDialogFragment errorFragment = new ErrorDialogFragment();

	    errorFragment.setDialog(errorDialog);

	    errorFragment.show(mActivity.getSupportFragmentManager(), null);
	}
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	switch (requestCode) {
	case CONNECTION_FAILURE_RESOLUTION_REQUEST:
	    switch (resultCode) {
	    case Activity.RESULT_OK:
		break;
	    }
	}
    }

    public void onCreate(Bundle savedInstanceState) {
	mPrefs = mActivity.getSharedPreferences(PREFERENCES_NAME,
		Context.MODE_PRIVATE);
	mEditor = mPrefs.edit();
	mUpdatesRequested = false;
	mLocationRequest = LocationRequest.create();
	mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	mLocationRequest.setInterval(UPDATE_INTERVAL);
	mLocationRequest.setFastestInterval(FAST_INTERVAL);
	mLocationClient = new LocationClient(mActivity, this, this);
    }

    public void onPause() {
	mEditor.putBoolean(KEY_UPDATES_ON, mUpdatesRequested);
	mEditor.commit();
    }

    public void onStart() {
	mLocationClient.connect();
    }

    public void onResume() {
	if (mPrefs.contains(KEY_UPDATES_ON)) {
	    mUpdatesRequested = mPrefs.getBoolean(KEY_UPDATES_ON, false);

	} else {
	    mEditor.putBoolean(KEY_UPDATES_ON, false);
	    mEditor.commit();
	}
    }

    public void onStop() {
	if (mLocationClient.isConnected()) {
	    mLocationClient.removeLocationUpdates(this);
	}
	mLocationClient.disconnect();
    }

    private boolean servicesConnected() {
	int resultCode = GooglePlayServicesUtil
		.isGooglePlayServicesAvailable(mActivity);
	if (ConnectionResult.SUCCESS == resultCode) {
	    Log.d(TAG, "Google Play services is available.");
	    return true;
	} else {
	    Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
		    resultCode, mActivity,
		    CONNECTION_FAILURE_RESOLUTION_REQUEST);
	    if (errorDialog != null) {
		ErrorDialogFragment errorFragment = new ErrorDialogFragment();
		errorFragment.setDialog(errorDialog);
		errorFragment.show(mActivity.getSupportFragmentManager(), null);
	    }
	}
	return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
	if (connectionResult.hasResolution()) {
	    try {
		connectionResult.startResolutionForResult(mActivity,
			CONNECTION_FAILURE_RESOLUTION_REQUEST);
	    } catch (IntentSender.SendIntentException e) {
		e.printStackTrace();
	    }
	} else {
//	    showErrorDialog(connectionResult.getErrorCode());
	}
    }

    @Override
    public void onConnected(Bundle bundle) {
	// mUpdatesRequested = PreferenceMapper.isUseLocation(mActivity);
	Log.d(TAG, "Connected : " + mUpdatesRequested);
	if (mUpdatesRequested) {
	    startPeriodicUpdates();
	}
    }

    @Override
    public void onDisconnected() {
	Log.d(TAG, "Disconnected");
    }

    @Override
    public void onLocationChanged(Location location) {
	if (location != null) {
	    Log.d(LocationManager.TAG,
		    "onLocationChanged: " + location.getLatitude() + ","
			    + location.getLongitude());
	}
	if (locationChanged != null) {
	    locationChanged.locationChanged(location);
	}
    }

    public void stopUpdateLocation() {
	Log.d(TAG, "Stop gps tracking");
	mUpdatesRequested = false;
	PreferenceMapper.putIsUseLocation(mActivity, false);
	if (servicesConnected()) {
	    mLocationClient.removeLocationUpdates(this);
	}
    }

}

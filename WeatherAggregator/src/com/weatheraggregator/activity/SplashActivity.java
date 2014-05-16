package com.weatheraggregator.activity;

import java.util.Calendar;
import java.util.Date;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.weatheraggregator.app.R;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.util.DateForecastHelper;
import com.weatheraggregator.util.DialogValidationTextMessagesManager;
import com.weatheraggregator.util.PreferenceMapper;
import com.weatheraggregator.webservice.exception.ErrorType;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    public final static String TAG = "SpaleshActivity";
    private final long MINIMUM_SPLASH_ELAPSE_TIME = 1000;
    private boolean mCanceled = false;
    private boolean mLoaded = false;
    private boolean mAwaited = false;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @AfterViews
    protected void initView() {
	View splash = findViewById(R.id.splashscreen);
	// Though property animations are support up from Honeycomb, some
	// devices don't support it
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	    splash.setAlpha(0f);
	    splash.setScaleX(0.0f);
	    splash.setScaleY(0.0f);
	    splash.animate().alpha(1f).scaleX(1f).scaleY(1f).rotation(0f)
		    .setDuration(1000).start();
	}

	// Next activity should start after loading of application data has been
	// done but not before
	// splash screen timer elapsed

	// Start lazy loading of application data
	lazyLoading();

	// create a handler to wait at least a specific time before splashscreen
	// hides
	new Handler().postDelayed(new Runnable() {
	    @Override
	    public void run() {
		if (!mCanceled) {
		    mAwaited = true;
		    startNextActivity();
		}
	    }
	}, MINIMUM_SPLASH_ELAPSE_TIME);
	if (PreferenceMapper.isUseLocation(SplashActivity.this)) {
	    startUpdateLocation();
	}
    }

    @Override
    public void onBackPressed() {
	mCanceled = true;
	super.onBackPressed();
    }

    protected void lazyLoading() {
	checkUserRegistrationAndLoadingData();
    }

    private void checkStatusCode(int statusCode) {
	if (SplashActivity.this != null
		&& statusCode == ErrorType.HTTP_OK.getCode()) {
	    SplashActivity.this.runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    startNextActivity();
		}
	    });
	} else {
	    // TODO: make dialog
	    final ErrorType type = ErrorType.values()[statusCode];
	    SplashActivity.this.runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    DialogValidationTextMessagesManager.newInstance(
			    SplashActivity.this).makeAlert(type,
			    new OnClickListener() {
				@Override
				public void onClick(
					DialogInterface paramDialogInterface,
					int paramInt) {
				    switch (paramInt) {
				    case DialogInterface.BUTTON_NEGATIVE:
					SplashActivity.this
						.runOnUiThread(new Runnable() {
						    @Override
						    public void run() {
							gotoMain();
						    }
						});
					break;
				    case DialogInterface.BUTTON_POSITIVE:
					stopUpdateLocation();
					android.os.Process
						.killProcess(android.os.Process
							.myPid());
					break;
				    default:
					break;
				    }
				}
			    });
		}
	    });
	}
    }

    private void gotoMain() {
	SplashActivity.this.finish();
	overridePendingTransition(R.anim.fade_in, R.anim.stay);
	MainActivity_.intent(SplashActivity.this).start();
    }

    private void checkUserRegistrationAndLoadingData() {
	getManager().registerUserWithLoadingData(
		new IDataSourceServiceListener.Stub() {
		    @Override
		    public void callBack(int statusCode) throws RemoteException {
			// TODO check statusCode
			if (!mCanceled) {
			    Log.d(TAG, "---loadCityWithForecast---");
			    DateForecastHelper helper = new DateForecastHelper();
			    helper.saveForecastToCache(new Date());
			    Calendar calender = Calendar.getInstance();
			    calender.set(Calendar.MONTH, Calendar.FEBRUARY);
			    mLoaded = true;
			    checkStatusCode(statusCode);
			}
		    }
		}, this);
    }

    protected synchronized void startNextActivity() {
	// This is OK because of synchronized call
	if (mAwaited && mLoaded) {
	    mLoaded = false;
	    // TODO: Start next activity after splash screen
	    gotoMain();
	}
    }
}

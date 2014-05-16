package com.weatheraggregator.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.googlecode.androidannotations.annotations.EActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.events.EventCloseSlidingMenu;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.events.EventUpdateDataFromLocation;
import com.weatheraggregator.fragment.SettingFragment;
import com.weatheraggregator.fragment.SettingFragment_;
import com.weatheraggregator.fragment.SettingFragment_.FragmentBuilder_;
import com.weatheraggregator.interfaces.ILocationChanged;
import com.weatheraggregator.localservice.ForecastsUpdateReceiver;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.localservice.LocationManager;
import com.weatheraggregator.localservice.RemoteWeatherServiceManager;
import com.weatheraggregator.localservice.WeatherRemoteService;

import de.greenrobot.event.EventBus;

@EActivity
public abstract class AbstractWeatherActivity extends SlidingFragmentActivity {
    public static final String F_TYPE_ITEM = "menu_item_type";
    public static final String F_OBJECT_ID = "menu_item_id";

    private SettingFragment mFrag;
    protected WeatherService mCurrentService;
    protected City mCurrentCity;

    private RemoteWeatherServiceManager mServiceManager;
    private ForecastsUpdateReceiver mReceiver;
    private LocationManager mLocationManager;

    public static final int REQUEST_CODE = 121;

    private void registerForecastReceiver() {
	if (mReceiver == null) {
	    initForecastReceiver();
	}
	IntentFilter intentFilter = new IntentFilter(
		WeatherRemoteService.FORECASTS_NEW_DATA);
	registerReceiver(mReceiver, intentFilter);
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
	    getSlidingMenu().showMenu();
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

    public RemoteWeatherServiceManager getManager() {
	return mServiceManager;
    }

    @Override
    protected void onStart() {
	super.onStart();
	if (mServiceManager != null) {
	    mServiceManager.bindService(this);
	}
	registerForecastReceiver();
	mLocationManager.onStart();
    }

    @Override
    protected void onStop() {
	mServiceManager.unbindService(this);
	if (mReceiver != null) {
	    unregisterReceiver(mReceiver);
	}
	mLocationManager.onStop();
	super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	mServiceManager = new RemoteWeatherServiceManager();
	initSlidingMenu(savedInstanceState);
	initForecastReceiver();
	mLocationManager = new LocationManager(this, mLocationChanged);
	mLocationManager.onCreate(savedInstanceState);
    }

    private ILocationChanged mLocationChanged = new ILocationChanged() {

	@Override
	public void locationChanged(Location location) {
	    if (location != null) {

		// getManager().loadCityByLocation(location.getLatitude(),
		// location.getLongitude());
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
	public void callBack(int statusCode) throws RemoteException {
	    Log.d(LocationManager.class.getSimpleName(),
		    "update data from location");
	    EventBus.getDefault().post(new EventUpdateDataFromLocation());
	}

    };

    private void initSettingFragment(Bundle savedInstanceState) {
	if (savedInstanceState == null) {
	    FragmentTransaction t = this.getSupportFragmentManager()
		    .beginTransaction();
	    FragmentBuilder_ builder = SettingFragment_.builder();
	    mFrag = builder.build();
	    t.replace(R.id.slide_menu, mFrag, SettingFragment.TAG);
	    t.commit();
	} else {
	    mFrag = (SettingFragment) this.getSupportFragmentManager()
		    .findFragmentById(R.id.slide_menu);
	}
    }

    private void initSlidingView() {
	SlidingMenu sm = getSlidingMenu();
	sm.setShadowWidthRes(R.dimen.mainmenu_sliding_width);
	sm.setBehindOffsetRes(R.dimen.mainmenu_sliding_offset);
	sm.setFadeDegree(0.35f);
	sm.setMode(SlidingMenu.LEFT);
	sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	sm.setOnOpenListener(mOpenSlidingMenuListener);
	sm.setOnCloseListener(mCloseSlidingMenuListener);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
	setBehindContentView(R.layout.menu_frame);
	initSettingFragment(savedInstanceState);
	initSlidingView();
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private OnCloseListener mCloseSlidingMenuListener = new OnCloseListener() {
	@Override
	public void onClose() {
	    EventBus.getDefault().post(new EventCloseSoftKeyBoard());
	}
    };

    private OnOpenListener mOpenSlidingMenuListener = new OnOpenListener() {
	@Override
	public void onOpen() {
	    EventBus.getDefault().post(new EventCloseSoftKeyBoard());
	}
    };

    @Override
    protected void onResume() {
	EventBus.getDefault().register(this);
	super.onResume();
	mLocationManager.onResume();
    }

    @Override
    protected void onPause() {
	EventBus.getDefault().unregister(this);
	mLocationManager.onPause();
	super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	mLocationManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startUpdateLocation() {
	mLocationManager.startUpdateLocation();
    }

    public void stopUpdateLocation() {
	mLocationManager.stopUpdateLocation();
    }

    public void onEventMainThread(EventCloseSlidingMenu event) {
	if (event != null) {
	    getSlidingMenu().toggle();
	}
    }

    // public void stopProgressBar()
    // {
    // if (mProgressBar != null)
    // {
    // try
    // {
    // mProgressBar.dismiss();
    // mProgressBar = null;
    // }
    // catch (IllegalStateException e)
    // {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // public void showDialog(String textMessage, Context context)
    // {
    // mProgressBar = new UpdateProgressDialog(context, textMessage);
    // mProgressBar.show(getSupportFragmentManager(), null);
    // }
    //
    // public void showProgressLine(){
    //
    // }

}

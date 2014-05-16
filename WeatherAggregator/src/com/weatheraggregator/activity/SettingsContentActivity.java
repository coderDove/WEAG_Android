package com.weatheraggregator.activity;

import android.os.Bundle;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.mobeta.android.dslv.IRemoveItemListener;
import com.weatheraggregator.app.R;
import com.weatheraggregator.events.EventRemoveItemFromDragAndDropListView;
import com.weatheraggregator.fragment.SettingCitiesListFragment;
import com.weatheraggregator.fragment.SettingsSearchCityFragment;
import com.weatheraggregator.fragment.UserMeasureSettingFragment;
import com.weatheraggregator.fragment.WeatherServiceSettingFragment;
import com.weatheraggregator.interfaces.ISaveDataListener;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.setting_content_activity)
public class SettingsContentActivity extends BaseActivity implements
	IRemoveItemListener {
    public final static String F_SETTING = "setting_item";

    @AfterViews
    protected void initView() {
	EventBus.clearCaches();
	getSherlock().setTitle(R.string.title_settings);
	Bundle bundle = getIntent().getExtras();
	if (bundle != null) {
	    int type = bundle.getInt(F_SETTING);
	    SettingType settingType = SettingType.values()[type];
	    setSettingsFragment(settingType);
	}
    }

    public void setSettingsFragment(final SettingType settingType) {
	switch (settingType) {
	case CITY:
	    showCitiesSettingsList();
	    break;
	case SERVICE:
	    showServiceSettingList();
	    break;
	case UNIT:
	    showMeasureUser();
	    break;
	case USER_CITIES_LIST:
	    showUserCityList();
	    break;
	case SETTING_USER_SERVICES_LIST:
	    showUserServices();
	    break;
	default:
	    break;
	}
    }

    private void showUserServices() {
	getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content,
			WeatherServiceSettingFragment.getNewInstance(null),
			WeatherServiceSettingFragment.TAG).commit();
    }

    private void showUserCityList() {
	getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content,
			SettingCitiesListFragment.getNewInstance(null),
			SettingCitiesListFragment.TAG).commit();
    }

    private void showCitiesSettingsList() {
	getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content,
			SettingsSearchCityFragment.getNewInstance(null),
			SettingsSearchCityFragment.TAG).commit();
    }

    private void showServiceSettingList() {
	getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content,
			WeatherServiceSettingFragment.getNewInstance(null),
			WeatherServiceSettingFragment.TAG).commit();
    }

    private void showMeasureUser() {
	getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content,
			UserMeasureSettingFragment.getNewInstance(null),
			UserMeasureSettingFragment.TAG).commit();
    }

    public static enum SettingType {
	CITY(0), SERVICE(1), UNIT(2), USER_CITIES_LIST(3), SETTING_USER_SERVICES_LIST(
		4);
	private int mCode;

	SettingType(int code) {
	    this.mCode = code;
	}

	public int getCode() {
	    return mCode;
	}
    }

    @Override
    public void onBackPressed() {
	ISaveDataListener fragment = (ISaveDataListener) getSupportFragmentManager()
		.findFragmentById(R.id.content);
	if (fragment != null) {
	    fragment.onSaveData();
	}
	super.onBackPressed();
    }

    @Override
    public void onRemove(int position) {
	EventBus.getDefault().post(
		new EventRemoveItemFromDragAndDropListView(position));
    }

}

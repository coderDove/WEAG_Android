package com.weatheraggregator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.activity.AbstractWeatherActivity;
import com.weatheraggregator.activity.SettingsContentActivity;
import com.weatheraggregator.activity.SettingsContentActivity.SettingType;
import com.weatheraggregator.app.R;
import com.weatheraggregator.events.EventCloseSlidingMenu;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.model.DrawerMenuItem;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.setting_fragment)
public class SettingFragment extends BaseFragment {
    public static final String TAG = "SettingFragment";

    @ViewById(R.id.btnUserCities)
    protected Button btnCities;
    @ViewById(R.id.btnUserServices)
    protected Button btnServices;
    @ViewById(R.id.btnUserMeasure)
    protected Button btnMeasure;
    @ViewById(R.id.btnAbout)
    protected Button btnAbout;

    private BaseDialogFragment mDialog;

    public SettingFragment() {
	super();
    }

    public static SettingFragment newInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	SettingFragment fragment = new SettingFragment();
	fragment.setArguments(bundle);
	return fragment;
    }

    @AfterViews
    protected void initView() {
	initData();
    }

    // protected void initCityMenu() {
    // List<City> cities = new Select(City.F_NAME, City.F_OBJECT_ID,
    // City.F_REGION, City.F_COUNTRY, BaseColumns._ID)
    // .from(City.class).orderBy(City.F_ORDER)
    // .where(String.format("%s = ?", City.F_IS_DELETED), 0).execute();
    // listMenu.add(new DrawerMenuItem(0, getString(R.string.setting_city),
    // "", DrawerMenuItem.HEADER));
    // mCitiesCount = 1;
    // if (cities != null && !cities.isEmpty()) {
    // mCitiesCount += cities.size();
    // int order = listMenu.size() - 1;
    // for (int indexCity = 0; indexCity < cities.size(); indexCity++) {
    // City city = cities.get(indexCity);
    // listMenu.add(new DrawerMenuItem(order++, city.getName(), city
    // .getObjectId(), DrawerMenuItem.CITY_ITEM));
    // }
    // }
    // listMenu.add(new DrawerMenuItem(mCitiesCount++,
    // getString(R.string.my_city_list), null,
    // DrawerMenuItem.SETTING_USER_CITY_LIST));
    // }
    //
    // protected void initServicesMenu() {
    // List<WeatherService> services = new Select(WeatherService.F_NAME,
    // WeatherService.F_OBJECT_ID, BaseColumns._ID)
    // .from(WeatherService.class).orderBy(WeatherService.F_ORDER)
    // .where(String.format("%s=?", WeatherService.F_ISDELETE), 0)
    // .execute();
    // int order = mCitiesCount++;
    // listMenu.add(new DrawerMenuItem(order,
    // getString(R.string.setting_service), "", DrawerMenuItem.HEADER));
    // mServicesCount = 2;
    // if (services != null && !services.isEmpty()) {
    // mServicesCount += services.size();
    // for (int indexCity = 0; indexCity < services.size(); indexCity++) {
    // WeatherService service = services.get(indexCity);
    // listMenu.add(new DrawerMenuItem(order++, service.getName(),
    // service.getObjectId(), DrawerMenuItem.SERVICE_ITEM));
    // }
    // }
    // listMenu.add(new DrawerMenuItem(order++,
    // getString(R.string.my_service_list), null,
    // DrawerMenuItem.SETTING_USER_SERVICE_LIST));
    // }

    private void showSettingUserCityList() {
	com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_(
		getActivity());
	Intent intent = builder.get();

	intent.putExtra(SettingsContentActivity.F_SETTING,
		SettingType.USER_CITIES_LIST.getCode());
	builder.startForResult(AbstractWeatherActivity.REQUEST_CODE);
    }

    private void showSettingUserService() {
	com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_(
		getActivity());
	Intent intent = builder.get();

	intent.putExtra(SettingsContentActivity.F_SETTING,
		SettingType.SETTING_USER_SERVICES_LIST.getCode());
	builder.start();
    }

    private void showUserCity(DrawerMenuItem itemValue, int itemPosition) {
	if (getActivity() != null
		&& getActivity() instanceof com.weatheraggregator.activity.DetailWeatherActivity_) {
	    com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_(
		    getActivity());
	    Intent intent = builder.get();

	    intent.putExtra(AbstractWeatherActivity.F_TYPE_ITEM,
		    itemValue.getType());
	    intent.putExtra(AbstractWeatherActivity.F_OBJECT_ID,
		    itemValue.getValue());
	    builder.start();
	} else {
	    ((com.weatheraggregator.activity.MainActivity_) getActivity())
		    .changeCity(itemPosition - 1);
	    EventBus.getDefault().post(new EventCloseSlidingMenu(true));
	}
    }

    private void showServiceForecastView(DrawerMenuItem itemValue) {
	if (getActivity() != null
		&& getActivity() instanceof com.weatheraggregator.activity.DetailWeatherActivity_) {
	    com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_(
		    getActivity());
	    Intent intent = builder.get();
	    intent.putExtra(AbstractWeatherActivity.F_TYPE_ITEM,
		    itemValue.getType());
	    intent.putExtra(AbstractWeatherActivity.F_OBJECT_ID,
		    itemValue.getValue());
	    builder.start();
	} else {
	    com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.DetailWeatherActivity_.IntentBuilder_(
		    getActivity());
	    Intent intent = builder.get();
	    intent.putExtra(AbstractWeatherActivity.F_TYPE_ITEM,
		    itemValue.getType());
	    intent.putExtra(AbstractWeatherActivity.F_OBJECT_ID,
		    itemValue.getValue());
	    builder.start();
	}
    }

    private void showMeasureView() {
	com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_(
		getActivity());
	Intent intent = builder.get();
	intent.putExtra(SettingsContentActivity.F_SETTING,
		SettingType.UNIT.getCode());
	builder.start();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
	initView();
	super.onResume();
    }

    @Click({ R.id.btnUserCities, R.id.btnUserServices, R.id.btnUserMeasure,
	    R.id.btnAbout })
    protected void clickMenu(View view) {
	switch (view.getId()) {
	case R.id.btnUserCities:
	    showSettingUserCityList();
	    break;
	case R.id.btnUserServices:
	    showSettingUserService();
	    break;
	case R.id.btnUserMeasure:
	    showMeasureView();
	    break;
	case R.id.btnAbout:
	    showAbout();
	    break;
	}
    }

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {

    }

    private void showAbout() {
	com.weatheraggregator.activity.AboutActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.AboutActivity_.IntentBuilder_(
		getActivity());
	builder.start();
    }

}

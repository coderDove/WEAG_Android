package com.weatheraggregator.activity;

import android.content.Intent;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.weatheraggregator.activity.SettingsContentActivity.SettingType;
import com.weatheraggregator.app.R;

@EActivity(R.layout.settings_menu_activity)
public class SettingsMenuActivity extends BaseActivity {

    @AfterViews
    protected void initView() {
        getSherlock().setTitle(R.string.title_settings);
        initActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Click({R.id.btnCities, R.id.btnServices, R.id.btnUnit})
    protected void onButtonClick(View clickedView) {
        com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_ builder = new com.weatheraggregator.activity.SettingsContentActivity_.IntentBuilder_(this);
        Intent intent = builder.get();
        switch (clickedView.getId()) {
            case R.id.btnCities:
                intent.putExtra(SettingsContentActivity.F_SETTING, SettingType.CITY.getCode());
                break;
            case R.id.btnServices:
                intent.putExtra(SettingsContentActivity.F_SETTING, SettingType.SERVICE.getCode());
                break;
            case R.id.btnUnit:
                intent.putExtra(SettingsContentActivity.F_SETTING, SettingType.UNIT.getCode());
                break;
            default:
                break;
        }
        builder.start();
    }
}

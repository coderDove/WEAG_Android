package com.weatheraggregator.activity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.weatheraggregator.app.R;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.statistic_chart)
public class StatisticActivity extends BaseActivity {
    @AfterViews
    protected void initView() {
        EventBus.clearCaches();
    }


}

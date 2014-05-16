package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.ab_city_spinner)
public class ABSpinnerCity extends LinearLayout {

    @ViewById
    protected TextView tvCity;

    @ViewById
    protected TextView tvRegion;

    public ABSpinnerCity(Context context) {
        super(context);
    }

    public ABSpinnerCity(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCityName(final String city) {
        tvCity.setText(city);
    }

    public void setRegionName(final String region) {
        tvRegion.setText(region);
    }

}

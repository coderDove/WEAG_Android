package com.weatheraggregator.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.City;

@EViewGroup(R.layout.city_list_item)
public class CitySettingItem extends RelativeLayout {
    @ViewById
    protected TextView tvCityName, tvFullName;
    private String getRegion(City city) {
	StringBuilder region = new StringBuilder();
	if (city.getCountry() != null && !city.getCountry().isEmpty()) {
	    region.append(city.getCountry());
	}
	if (city.getRegion() != null && !city.getRegion().isEmpty()) {
	    if (region.length() > 0)
		region.append(", ");
	    region.append(city.getRegion());
	}
	return region.toString();
    }

    public void bind(City city) {
	String cityName = city.getName();
	if (cityName != null && !cityName.isEmpty())
	    this.setCityName(cityName);
	this.setFullCityName(getRegion(city));
    }

    public CitySettingItem(Context context) {
	super(context);
    }

    public void setCityName(String city) {
	tvCityName.setText(city);
    }

    public void setFullCityName(String fullName) {
	tvFullName.setText(fullName);
    }

    @AfterViews
    void onAfterInitView() {
    }

}

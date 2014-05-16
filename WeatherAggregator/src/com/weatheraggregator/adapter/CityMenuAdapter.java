package com.weatheraggregator.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.R;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.view.ABSpinnerCity;
import com.weatheraggregator.view.ABSpinnerCity_;

public class CityMenuAdapter extends ArrayAdapter<City> implements SpinnerAdapter {

    public CityMenuAdapter(Context context, List<City> cities) {
        super(context, R.layout.sherlock_spinner_dropdown_item, 0, cities);
    }

    private String getRegion(City city) {
        StringBuilder region = new StringBuilder();
        if (city.getCountry() != null) {
            region.append(city.getCountry());
        }
        if (city.getRegion() != null && !city.getRegion().isEmpty()) {
            if (region.length() > 0)
                region.append(", ");
            region.append(city.getRegion());
        }
        return region.toString();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ABSpinnerCity view;
        if (convertView == null) {
            view = ABSpinnerCity_.build(getContext());
        } else {
            view = (ABSpinnerCity) convertView;
        }

        view.setCityName(getItem(position).getName());
        view.setRegionName(getRegion(getItem(position)));
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDropDownView(position, convertView, parent);
    }
}

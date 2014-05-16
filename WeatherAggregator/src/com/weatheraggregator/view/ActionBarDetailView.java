package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.adapter.CityMenuAdapter;
import com.weatheraggregator.adapter.ShareSocialAdapter;
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.ab_detail_view)
public class ActionBarDetailView extends LinearLayout {

    @ViewById
    protected Spinner spCity;
    @ViewById
    protected Spinner spShare;
    @ViewById
    protected ImageButton ivHomeButton;

    public ActionBarDetailView(Context context) {
	super(context);
    }

    public ActionBarDetailView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public View getHomeButton() {
	return ivHomeButton;
    }

    public void setCityAdapter(CityMenuAdapter adapter) {
	if (spCity != null) {
	    spCity.setAdapter(adapter);
	}
    }

    public void setCitySelectListener(OnItemSelectedListener listener) {
	spCity.setOnItemSelectedListener(listener);
    }

    public int getSelectCityPosition() {
	return spCity.getSelectedItemPosition();
    }

    public void selectCityPosition(int position) {
	if (spCity != null) {
	    spCity.setSelection(position);
	}
    }

    public void serShareAdapter(ShareSocialAdapter adapter) {
	if (spShare != null) {
	    spShare.setAdapter(adapter);
	}
    }

    public void setShareItemSelectedListener(OnItemSelectedListener listener) {
	if (spShare != null) {
	    spShare.setOnItemSelectedListener(listener);
	}
    }

}

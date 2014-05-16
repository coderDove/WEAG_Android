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
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.ab_main_view)
public class ActionBarMainView extends LinearLayout {
    @ViewById
    protected Spinner spAB;
    @ViewById
    protected ImageButton ivHomeButton;
    @ViewById
    protected ImageButton ivAddCity;

    public ActionBarMainView(Context context) {
	super(context);
    }

    public ActionBarMainView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public void setCityAdapter(CityMenuAdapter adapter) {
	if (spAB != null) {
	    spAB.setAdapter(adapter);
	}
    }

    public void setCitySelectListener(OnItemSelectedListener listener) {
	spAB.setOnItemSelectedListener(listener);
    }

    public int getSelectCityPosition() {
	return spAB.getSelectedItemPosition();
    }

    public void selectCityPosition(int position) {
	if (spAB != null) {
	    spAB.setSelection(position);
	}
    }

    public void setAddCityClickListener(OnClickListener listener) {
	ivAddCity.setOnClickListener(listener);
    }

    public View getHomeButton() {
	return ivHomeButton;
    }

}

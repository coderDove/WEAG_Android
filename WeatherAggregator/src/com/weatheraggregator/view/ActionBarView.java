package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.adapter.CityMenuAdapter;
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.ab_view)
public class ActionBarView extends LinearLayout {

    @ViewById
    protected Spinner spAB;
    @ViewById
    protected ImageButton ivHomeButton;
    @ViewById
    protected ImageButton ivAddCity;
    @ViewById
    protected ImageButton ivShare;

    public ActionBarView(Context context) {
        super(context);
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(CityMenuAdapter adapter) {
        if (spAB != null) {
            spAB.setAdapter(adapter);
        }
    }

    public void setSelectListener(OnItemSelectedListener listener) {
        spAB.setOnItemSelectedListener(listener);
    }

    public int getSelectPosition() {
        return spAB.getSelectedItemPosition();
    }

    public void selectPosition(int position) {
        if (spAB != null) {
            spAB.setSelection(position);
        }
    }

    public void hideButton(ActionBarButtonType type) {
        ImageButton iv = getButton(type);
        if (iv != null) {
            ViewGroup.LayoutParams params = iv.getLayoutParams();
            params.width = 0;
            iv.setLayoutParams(params);
        }
    }

    public void visibleButton(ActionBarButtonType type) {
        ImageButton iv = getButton(type);
        if (iv != null) {
            ViewGroup.LayoutParams params = iv.getLayoutParams();
            params.width = LayoutParams.WRAP_CONTENT;
            iv.setLayoutParams(params);
        }
    }

    public void setOnClickListener(OnClickListener listener,
                                   ActionBarButtonType type) {
        ImageButton iv = getButton(type);
        if (iv != null) {
            iv.setOnClickListener(listener);
        }
    }

    public ImageButton getButton(ActionBarButtonType type) {
        switch (type) {
            case HOME:
                return ivHomeButton;
            case ADD_NEW_CITY:
                return ivAddCity;
            case SHARE:
                return ivShare;
        }
        return null;
    }

    public enum ActionBarButtonType {
        HOME, ADD_NEW_CITY, SHARE;
    }
}

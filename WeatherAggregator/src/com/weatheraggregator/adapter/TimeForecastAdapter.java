package com.weatheraggregator.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.Forecast.Cloudiness;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.view.TimeForecastView;
import com.weatheraggregator.view.TimeForecastView_;

public class TimeForecastAdapter extends ArrayAdapter<Forecast> {
    // after this sprint this variable set final
    private int FORECAST_COUNT = 9;
    private int EMPTY_ITEM_INDEX_LEFT = 1;
    // private int EMPTY_ITEM_COUNT_RIGHT = 7;
    private int EMPTY_ITEM_COUNT_VIEW = 4;
    public static final int DISPLAY_ITEM_COUNT = 5;
    private DisplayImageOptions mOptions;
    private List<Forecast> mListOfForecast;

    public TimeForecastAdapter(Context context, List<Forecast> objects) {
	super(context, 0, 0, objects);
	mListOfForecast = objects;

	// for (int i = 0; i < 3; i++) {
	// mListOfForecast.add(new Forecast());
	// }
    }

    private int getEmptyItemLeft() {
	return EMPTY_ITEM_INDEX_LEFT;
    }

    private int getEmptyItemRight() {
	return mListOfForecast.size() + EMPTY_ITEM_INDEX_LEFT + 1;
    }

    public void setList(List<Forecast> listOfForecast) {
	mListOfForecast = listOfForecast;
    }

    @Override
    public int getCount() {
	if (mListOfForecast != null && mListOfForecast.size() > 0) {
	    return FORECAST_COUNT = mListOfForecast.size()
		    + EMPTY_ITEM_COUNT_VIEW;
	} else {
	    return 0;
	}
    }

    @Override
    public Forecast getItem(int position) {
	if (position > getEmptyItemLeft() && position < getEmptyItemRight()) {
	    if (mListOfForecast.size() > 0) {
		return mListOfForecast
			.get(position - EMPTY_ITEM_INDEX_LEFT - 1);
	    }
	}
	return null;
    }

    protected DisplayImageOptions getDisplayOption() {
	if (mOptions == null) {
	    mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
		    .resetViewBeforeLoading(true).build();
	}
	return mOptions;
    }

    private void setVisibility(boolean isVisible, TimeForecastView view) {
	if (!isVisible) {
	    view.getTimeView().setVisibility(View.INVISIBLE);
	    view.getTempView().setVisibility(View.INVISIBLE);
	    view.getCloudinessView().setVisibility(View.INVISIBLE);
	} else {
	    view.getTimeView().setVisibility(View.VISIBLE);
	    view.getTempView().setVisibility(View.VISIBLE);
	    view.getCloudinessView().setVisibility(View.VISIBLE);
	}
    }

    @SuppressLint("SimpleDateFormat")
    private TimeForecastView bindView(Forecast item, TimeForecastView view) {
	if (item != null) {
	    setVisibility(true, view);
	    TextView tvTime = view.getTimeView();
	    TextView tvTemp = view.getTempView();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    if (item.isCurrent()) {
		tvTime.setText(item.getDetailTitle());
	    } else {
		tvTime.setText(sdf.format(item.getForecastDay()));
	    }
	    ImageLoader.getInstance().displayImage(
		    Util.getCloudyImagePath(item.getCondition()),
		    view.getCloudinessView(), getDisplayOption());
	    if (item.getTemp() != null) {
		tvTemp.setText(String.valueOf(MeasureUnitHelper.getSingleton()
			.convertTemperature(item.getTemp())));
	    } else {
		tvTemp.setText(R.string.na);
	    }
	} else {
	    setVisibility(false, view);
	}

	return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	TimeForecastView view;
	if (convertView != null) {
	    view = (TimeForecastView) convertView;
	} else {
	    view = TimeForecastView_.build(getContext(), null);
	}
	return bindView(getItem(position), view);
    }
}

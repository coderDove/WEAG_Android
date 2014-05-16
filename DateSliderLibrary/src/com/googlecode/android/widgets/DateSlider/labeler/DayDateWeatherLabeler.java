package com.googlecode.android.widgets.DateSlider.labeler;

import android.content.Context;

import com.googlecode.android.widgets.DateSlider.timeview.TimeView;
import com.googlecode.android.widgets.DateSlider.timeview.WeatherDateView;

public class DayDateWeatherLabeler extends DayLabeler {

    public DayDateWeatherLabeler(String formatString) {
        super(formatString);
    }

    @Override
    public TimeView createView(Context context, boolean isCenterView, int textSize, int imgeSize) {// 30
        if (textSize > 0) {
            return new WeatherDateView(context, isCenterView, textSize, 8, 0f, imgeSize);
        } else {
            return new WeatherDateView(context, isCenterView, 30, 8, 0f, imgeSize);
        }
    }
}

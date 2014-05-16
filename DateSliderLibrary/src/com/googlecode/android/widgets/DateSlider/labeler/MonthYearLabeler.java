package com.googlecode.android.widgets.DateSlider.labeler;

import android.content.Context;

import com.googlecode.android.widgets.DateSlider.timeview.TimeView;
import com.googlecode.android.widgets.DateSlider.timeview.WeatherDateView;

/**
 * A Labeler that displays months using TimeLayoutViews.
 */
public class MonthYearLabeler extends MonthLabeler {
    /**
     * The format string that specifies how to display the month. Since this
     * class uses a TimeLayoutView, the format string should consist of two
     * strings separated by a space.
     *
     * @param formatString
     */
    public MonthYearLabeler(String formatString) {
        super(formatString);
    }

    @Override
    public TimeView createView(Context context, boolean isCenterView, int textSize, int imgeSize) {
        if (textSize > 0) {
            return new WeatherDateView(context, isCenterView, textSize, 8, 0.8f, imgeSize);
        } else {
            return new WeatherDateView(context, isCenterView, 25, 8, 0.8f, imgeSize);
        }
    }
}

package com.googlecode.android.widgets.DateSlider.labeler;

import java.util.Calendar;

import android.content.Context;
import android.util.Log;

import com.googlecode.android.widgets.DateSlider.TimeObject;
import com.googlecode.android.widgets.DateSlider.timeview.TimeView;
import com.googlecode.android.widgets.DateSlider.timeview.WeatherTimeView;

public class TimeWeatherLabeler extends Labeler {
    private static final int MINUTEINTERVAL = 60;
    private final String mFormatString;

    public TimeWeatherLabeler(String formatString) {
	super(80, 60);
	mFormatString = formatString;
    }

    @Override
    public TimeView createView(Context context, boolean isCenterView,
	    int textSize, int imgeSize) {// 30
	if (textSize > 0) {
	    return new WeatherTimeView(context, isCenterView, textSize, 8, 0f,
		    imgeSize);
	} else {
	    return new WeatherTimeView(context, isCenterView, 30, 8, 0f,
		    imgeSize);
	}
    }

    @Override
    public TimeObject add(long time, int val) {
	return timeObjectfromCalendar(Util.addMinutes(time, val
		* MINUTEINTERVAL));
    }

    /**
     * override this method to set the inital TimeObject to a multiple of
     * MINUTEINTERVAL
     */
    @Override
    public TimeObject getElem(long time) {
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(time);
	c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) / MINUTEINTERVAL
		* MINUTEINTERVAL);
	Log.v("GETELEM", "getelem: " + c.get(Calendar.MINUTE));
	return timeObjectfromCalendar(c);
    }

    @Override
    protected TimeObject timeObjectfromCalendar(Calendar c) {
	return Util.getTime(c, mFormatString, MINUTEINTERVAL);
    }
}

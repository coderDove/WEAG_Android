package com.googlecode.android.widgets.DateSlider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.googlecode.android.widgets.DateSlider.timeview.IRenderWeatherCalenderView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * This is a container class for ScrollLayouts. It coordinates the scrolling
 * between them, so that if one is scrolled, the others are scrolled to keep a
 * consistent display of the time. It also notifies an optional observer anytime
 * the time is changed.
 */
public class SliderContainer extends LinearLayout {
    private Calendar mTime = null;
    private OnTimeChangeListener mOnTimeChangeListener;
    private int minuteInterval;
    private List<ScrollLayout> scrollLayoutsChilds = new ArrayList<ScrollLayout>();
    private int defaultHeight;

    private IRenderWeatherCalenderView renderListener;

    public SliderContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof ScrollLayout) {
                scrollLayoutsChilds.add((ScrollLayout) v);
                final ScrollLayout sl = (ScrollLayout) v;
                sl.setOnScrollListener(new ScrollLayout.OnScrollListener() {
                    public void onScroll(long x) {
                        mTime.setTimeInMillis(x);
                        arrangeScrollers(sl);
                    }

                    @Override
                    public void onTouchUp() {
                        mOnTimeChangeListener.onTimeChangeSelected(mTime);
                    }
                });
            }
        }
    }

    /**
     * Set the current time and update all of the child ScrollLayouts
     * accordingly.
     *
     * @param calendar
     */
    public void setTime(Calendar calendar) {
        mTime = Calendar.getInstance(calendar.getTimeZone());
        mTime.setTimeInMillis(calendar.getTimeInMillis());
        arrangeScrollers(null);
    }

    /**
     * Get the current time
     *
     * @return The current time
     */
    public Calendar getTime() {
        return mTime;
    }

    /**
     * sets the minimum date that the scroller can scroll
     *
     * @param c the minimum date (inclusiv)
     */
    public void setMinTime(Calendar c) {
        if (mTime == null) {
            throw new RuntimeException("You have to call setTime before setting a MinimumTime!");
        }
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof ScrollLayout) {
                ScrollLayout scroller = (ScrollLayout) v;
                scroller.setMinTime(c.getTimeInMillis());
            }
        }
    }

    /**
     * sets the maximum date that the scroller can scroll
     *
     * @param c the maximum date (inclusive)
     */
    public void setMaxTime(Calendar c) {
        if (mTime == null) {
            throw new RuntimeException("You have to call setTime before setting a MinimumTime!");
        }
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof ScrollLayout) {
                ScrollLayout scroller = (ScrollLayout) v;
                scroller.setMaxTime(c.getTimeInMillis());
            }
        }
    }

    /**
     * sets the minute interval of the scroll layouts.
     *
     * @param minInterval
     */
    public void setMinuteInterval(int minInterval) {
        this.minuteInterval = minInterval;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof ScrollLayout) {
                ScrollLayout scroller = (ScrollLayout) v;
                scroller.setMinuteInterval(minInterval);
            }
        }
    }

    /**
     * Sets the OnTimeChangeListener, which will be notified anytime the time is
     * set or changed.
     *
     * @param l
     */
    public void setOnTimeChangeListener(OnTimeChangeListener l) {
        mOnTimeChangeListener = l;
    }

    /**
     * Pushes our current time into all child ScrollLayouts, except the source
     * of the time change (if specified)
     *
     * @param source The ScrollLayout that generated the time change, or null if
     *               this isn't the result of a ScrollLayout-generated time change.
     */
    private void arrangeScrollers(ScrollLayout source) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v == source) {
                continue;
            }
            if (v instanceof ScrollLayout) {
                ScrollLayout scroller = (ScrollLayout) v;
                scroller.setTime(mTime.getTimeInMillis());
            }
        }

        if (mOnTimeChangeListener != null) {
            if (minuteInterval > 1) {
                int minute = mTime.get(Calendar.MINUTE) / minuteInterval * minuteInterval;

                mTime.set(Calendar.MINUTE, minute);
            }
            mOnTimeChangeListener.onTimeChange(mTime);
        }
    }

    public void setOnRenderWeatherCalendarView(IRenderWeatherCalenderView i) {
        renderListener = i;
        if (renderListener != null) {
            if (scrollLayoutsChilds != null && !scrollLayoutsChilds.isEmpty()) {
                for (ScrollLayout sl : scrollLayoutsChilds) {
                    sl.setRenderWeatherItem(renderListener);
                }
            }
        }
    }

    public static interface OnTimeChangeListener {
        public void onTimeChange(Calendar time);

        public void onTimeChangeSelected(Calendar time);
    }

    boolean isDisplayWeatherInformation = true;

//    public void startAnimationDown() {
//        if (!isDisplayWeatherInformation) {
//            isDisplayWeatherInformation = true;
//            if (!scrollLayoutsChilds.isEmpty()) {
//                for (ScrollLayout child : scrollLayoutsChilds) {
//                    child.startAnimationDown();
//                }
//                int childAdditionalHaight = scrollLayoutsChilds.get(0).getAdditionalLayoutHeight();
//                this.startAnimation(new AniExpand(this, defaultHeight - childAdditionalHaight, defaultHeight, true));
//                ViewGroup.LayoutParams params = scrollLayoutsChilds.get(0).getLayoutParams();
//                params.height = defaultHeight;
//            }
//        }
//    }

    public void refresh() {
	for (ScrollLayout child : scrollLayoutsChilds) {
	    child.refresh();
	}
    }

//    public void startAnimationUp() {
//        if (isDisplayWeatherInformation) {
//            isDisplayWeatherInformation = false;
//            if (!scrollLayoutsChilds.isEmpty()) {
//                if (defaultHeight <= 0) {
//                    defaultHeight = this.getMeasuredHeight();
//                }
//                if (defaultHeight <= 0) {
//                    throw new RuntimeException("SliderContainer default Height equal 0");
//                }
//                for (ScrollLayout child : scrollLayoutsChilds) {
//                    child.startAnimationUp();
//                }
//                int childAdditionalHaight = scrollLayoutsChilds.get(0).getAdditionalLayoutHeight();
//                this.startAnimation(new AniExpand(this, defaultHeight, defaultHeight - childAdditionalHaight, true));
//                ViewGroup.LayoutParams params = scrollLayoutsChilds.get(0).getLayoutParams();
//                params.height = defaultHeight;
//            }
//        }
//    }

}

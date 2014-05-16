package com.googlecode.android.widgets.DateSlider;

import java.lang.reflect.Method;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class AniExpand extends Animation {
    private final LinearLayout mView;
    private final int startHeight;
    private final int finishHeight;
    private static final int DURATION = 100;
    private boolean vertical;

    public AniExpand(LinearLayout view, int startHeight, int finishHeight, boolean vertical) {
        mView = view;
        this.vertical = vertical;
        this.startHeight = startHeight;
        this.finishHeight = finishHeight;
        setDuration(DURATION);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }

    public AniExpand(LinearLayout view, int startHeight, int finishHeight, boolean vertical, int duration) {
        mView = view;
        this.vertical = vertical;
        this.startHeight = startHeight;
        this.finishHeight = finishHeight;
        setDuration(duration);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final int newHeight;
        if (vertical) {
            newHeight = (int) ((finishHeight - startHeight) * interpolatedTime + startHeight);
            mView.getLayoutParams().height = newHeight;
        } else {
            newHeight = (int) ((finishHeight - startHeight) * interpolatedTime + startHeight);
            mView.getLayoutParams().width = newHeight;
        }

        mView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    // ===================================================================
    // get height view
    public static int getDesireHeight(View view) {
        try {
            Method m = view.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
            m.setAccessible(true);
            m.invoke(view, MeasureSpec.makeMeasureSpec(view.getWidth(), MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(view.getHeight(), MeasureSpec.UNSPECIFIED));
            Log.i("tag", "");
        } catch (Exception e) {
            return -1;
        }

        int measuredHeight = view.getMeasuredHeight();
        return measuredHeight;
    }

    // get Width view
    public static int getDesireWidth(View view) {
        try {
            Method m = view.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
            m.setAccessible(true);
            m.invoke(view, MeasureSpec.makeMeasureSpec(view.getWidth(), MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(view.getHeight(), MeasureSpec.UNSPECIFIED));
            Log.i("tag", "");
        } catch (Exception e) {
            return -1;
        }

        int measuredWidth = view.getMeasuredWidth();
        return measuredWidth;
    }
}

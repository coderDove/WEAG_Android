package com.weatheraggregator.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import com.weatheraggregator.adapter.InfinitePagerAdapter;

public class DateViewPager extends MainViewPager {

    public DateViewPager(Context context) {
	super(context);
    }

    public DateViewPager(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
	super.setAdapter(adapter);
	setCurrentItem(0);
    }

    @Override
    public void setCurrentItem(int item) {
	if (getAdapter() != null) {
	    item = getOffsetAmount() + (item % getAdapter().getCount());
	}
	super.setCurrentItem(item);

    }

    private int getOffsetAmount() {
	if (getAdapter() != null
		&& getAdapter() instanceof InfinitePagerAdapter) {
	    InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getAdapter();
	    return infAdapter.getRealCount() * 100;
	} else {
	    return 0;
	}
    }

}

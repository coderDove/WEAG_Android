package com.weatheraggregator.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class InfinitePagerAdapter extends PagerAdapter {

    private PagerAdapter adapter;
    private static final int MAX_SIZE = 500000;

    public InfinitePagerAdapter(PagerAdapter adapter) {
	this.adapter = adapter;
    }

    @Override
    public int getCount() {
	return MAX_SIZE;
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by instantiateItem(ViewGroup, int). This method is required
     * for a PagerAdapter to function properly.
     * 
     * @param view
     *            Page View to check for association with <b>object</b>
     * @param obj
     *            Object to check for association with <b>view</b>
     * @return true if view is associated with the key object <b>obj</b>
     */
    @Override
    public boolean isViewFromObject(View view, Object obj) {
	return adapter.isViewFromObject(view, obj);
    }

    /**
     * Remove a page for the given position. The adapter is responsible for
     * removing the view from its container, although it only must ensure this
     * is done by the time it returns from finishUpdate(ViewGroup).
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
	adapter.destroyItem(container, position, object);
    }

    /**
     * Called when the a change in the shown pages has been completed. At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     */
    @Override
    public void finishUpdate(ViewGroup container) {
	adapter.finishUpdate(container);
    }

    /**
     * Create the page for the given position. The adapter is responsible for
     * adding the view to the container given here, although it only must ensure
     * this is done by the time it returns from finishUpdate(ViewGroup).
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
	return adapter.instantiateItem(container, position);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
	adapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
	return adapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
	adapter.startUpdate(container);
    }

    public int getRealCount() {
	return adapter.getCount();
    }

}

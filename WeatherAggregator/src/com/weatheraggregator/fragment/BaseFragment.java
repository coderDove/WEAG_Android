package com.weatheraggregator.fragment;

import android.support.v4.app.Fragment;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.util.DialogValidationTextMessagesManager;
import com.weatheraggregator.webservice.exception.ErrorType;

@EFragment
public abstract class BaseFragment extends Fragment {
    protected abstract void initData();

    public abstract void onEventMainThread(EventCloseSoftKeyBoard event);

    @UiThread
    protected void showToast(ErrorType type) {
	if (getActivity() != null)
	    DialogValidationTextMessagesManager.newInstance(getActivity())
		    .makeToast(type);
    }
}

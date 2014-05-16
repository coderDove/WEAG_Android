package com.weatheraggregator.fragment;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.weatheraggregator.interfaces.ISaveDataListener;
import com.weatheraggregator.util.DialogValidationTextMessagesManager;
import com.weatheraggregator.webservice.exception.ErrorType;

@EFragment
public abstract class SettingBaseFragment extends BaseFragment implements
	ISaveDataListener {



}

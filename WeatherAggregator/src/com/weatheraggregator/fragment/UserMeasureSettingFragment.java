package com.weatheraggregator.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.activeandroid.query.Select;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.activity.BaseActivity;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.interfaces.ISaveDataListener;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.model.MeasureUnit;
import com.weatheraggregator.util.MeasureUnitHelper;
import com.weatheraggregator.webservice.exception.ErrorType;

@EFragment(R.layout.setting_measure_fragment)
public class UserMeasureSettingFragment extends BaseFragment implements
	ISaveDataListener {

    public final static String TAG = "UserMeasureSettingFragment";

    @ViewById(R.id.rgTemperature)
    protected RadioGroup rgTemperature;
    @ViewById(R.id.rbCelsius)
    protected RadioButton rbCelsius;
    @ViewById(R.id.rbFahrenheit)
    protected RadioButton rbFahrenheit;

    @ViewById(R.id.rgSpeed)
    protected RadioGroup rgSpeed;
    @ViewById(R.id.rbMeter)
    protected RadioButton rbMeter;
    @ViewById(R.id.rbKilometer)
    protected RadioButton rbKilometer;
    @ViewById(R.id.rbMile)
    protected RadioButton rbMile;

    @ViewById(R.id.rgPressure)
    protected RadioGroup rgPressure;
    @ViewById(R.id.rbMmHg)
    protected RadioButton rbMmHg;
    @ViewById(R.id.rbMbar)
    protected RadioButton rbMbar;
    @ViewById(R.id.rbKPa)
    protected RadioButton rbKPa;
    @ViewById(R.id.rbInHg)
    protected RadioButton rbInHg;
    @ViewById(R.id.rbPsi)
    protected RadioButton rbPsi;

    private User mUser;

    public static UserMeasureSettingFragment getNewInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	UserMeasureSettingFragment fragment = new UserMeasureSettingFragment_();
	fragment.setArguments(bundle);
	return fragment;
    }

    public UserMeasureSettingFragment() {
	super();
    }

    @Override
    @AfterViews
    protected void initData() {
	loadMeasureToUI();
    }

    /**
     * Loads measure from preferences and set it to user interface
     */
    @Background
    protected void loadMeasureToUI() {
	mUser = new Select().from(User.class).executeSingle();
	if (mUser != null) {
	    MeasureUnitHelper.getSingleton().init(mUser);
	    setTemperatureSettings(mUser);
	    setWindSpeedSettings(mUser);
	    setPressureSettings(mUser);
	}
    }

    /**
     * Sets checked RadioButtons for Temperature settings
     * 
     * @param user
     */
    @UiThread
    protected void setTemperatureSettings(User user) {
	switch (user.getTemperatureUnit()) {
	case FAHRENHEIT:
	    rbFahrenheit.setChecked(true);
	    break;
	default:
	case CELSIUS:
	    rbCelsius.setChecked(true);
	    break;
	}
    }

    /**
     * Sets checked RadioButtons for Wind Speed settings
     * 
     * @param user
     */
    @UiThread
    protected void setWindSpeedSettings(User user) {
	switch (user.getSpeedUnit()) {
	case KM_H:
	    rbKilometer.setChecked(true);
	    break;
	case MI_H:
	    rbMile.setChecked(true);
	    break;
	default:
	case M_S:
	    rbMeter.setChecked(true);
	    break;
	}
    }

    /**
     * Sets checked RadioButtons for Pressure settings
     * 
     * @param user
     */
    @UiThread
    protected void setPressureSettings(User user) {
	switch (user.getPressureUnit()) {
	case MBAR:
	    rbMbar.setChecked(true);
	    break;
	case PSI:
	    rbPsi.setChecked(true);
	    break;
	case IN_HG:
	    rbInHg.setChecked(true);
	    break;
	case PA:
	    rbPsi.setChecked(true);
	    break;
	default:
	case MM_HG:
	    rbMmHg.setChecked(true);
	    break;
	}
    }

    /**
     * Saves measure to database.
     * 
     * @param user
     */
    @Background
    protected void saveMeasureToDataBase(User user) {
	MeasureUnitHelper.getSingleton().init(user);
	user.setTemperatureUnit(getTemperatureFromCheckedButtons());
	user.setSpeedUnit(getWindSpeedFromCheckedButtons());
	user.setPressureUnit(getPressureFromCheckedButtons());
	user.setSync(false);
	user.save();
    }

    /**
     * 
     * @return temperature which check
     */
    private int getTemperatureFromCheckedButtons() {
	switch (rgTemperature.getCheckedRadioButtonId()) {
	case R.id.rbCelsius:
	    return MeasureUnit.CELSIUS.getCode();
	case R.id.rbFahrenheit:
	    return MeasureUnit.FAHRENHEIT.getCode();
	}
	return MeasureUnit.CELSIUS.getCode();
    }

    /**
     * 
     * @return wind speed which check
     */
    private int getWindSpeedFromCheckedButtons() {
	switch (rgSpeed.getCheckedRadioButtonId()) {
	case R.id.rbMeter:
	    return MeasureUnit.M_S.getCode();
	case R.id.rbKilometer:
	    return MeasureUnit.KM_H.getCode();
	case R.id.rbMile:
	    return MeasureUnit.MI_H.getCode();
	}
	return MeasureUnit.M_S.getCode();
    }

    /**
     * 
     * @return pressure which check
     */
    private int getPressureFromCheckedButtons() {
	switch (rgPressure.getCheckedRadioButtonId()) {
	case R.id.rbMmHg:
	    return MeasureUnit.MM_HG.getCode();
	case R.id.rbMbar:
	    return MeasureUnit.MBAR.getCode();
	case R.id.rbKPa:
	    return MeasureUnit.PA.getCode();
	case R.id.rbInHg:
	    return MeasureUnit.IN_HG.getCode();
	case R.id.rbPsi:
	    return MeasureUnit.PSI.getCode();
	}
	return MeasureUnit.MM_HG.getCode();
    }

    @Override
    public void onSaveData() {
	saveMeasureToDataBase(mUser);
	((BaseActivity) getActivity()).getManager().updateUser(
		new IDataSourceServiceListener.Stub() {

		    @Override
		    public void callBack(int statusCode) throws RemoteException {
			showToast(ErrorType.values()[statusCode]);
		    }
		});
    }

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {
	// TODO hide keyboard

    }
}

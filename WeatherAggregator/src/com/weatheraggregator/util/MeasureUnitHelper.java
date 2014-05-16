package com.weatheraggregator.util;

import android.content.Context;

import com.activeandroid.query.Select;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.model.MeasureUnit;

public class MeasureUnitHelper {
    private final double FAHRENHEIT = 1.8;
    private final int CELSIUS = 1;
    private final int WIND_METR_SEC = 1;
    private final double WIND_KM_HOUR = 0.277778;
    private final double WIND_KM_SEC = 0.001;
    private final double WIND_MI_H = 0.44704;

    private final double PRESSURE_mmHg = 1;
    private final double PRESSURE_inHg = 25.4;
    private final double PRESSURE_mbar = 1.33322;
    private final double PRESSURE_kPa = 133.322368;
    private final double PRESSURE_psi = 0.01933678;

    private static volatile MeasureUnitHelper _singletone;

    private User mUser;

    public synchronized static MeasureUnitHelper getSingleton() {
	if (_singletone == null) {
	    _singletone = new MeasureUnitHelper();
	}
	return _singletone;
    }

    private MeasureUnitHelper() {
	mUser = new Select().from(User.class).executeSingle();
    }

    public void init(User user) {
	mUser = user;
    }

    private boolean isUserExist() {
	if (mUser == null) {
	    return false;
	}
	return true;
    }

    public Integer convertTemperature(Integer celsius) {
	if (celsius == null) {
	    return null;
	}
	if (isUserExist()) {
	    if (mUser.getTemperatureUnit() == MeasureUnit.CELSIUS) {
		return Integer.valueOf((int) (celsius * CELSIUS));
	    } else {
		return Integer.valueOf((int) (celsius * FAHRENHEIT));
	    }
	}
	return celsius;
    }

    public String convertTemperature(Integer celsius, Context context) {
	if (celsius == null) {
	    return context.getString(R.string.nan);
	}
	if (isUserExist()) {
	    if (mUser.getTemperatureUnit() == MeasureUnit.CELSIUS) {
		return String.format("%d%s", (int) (celsius * CELSIUS),
			context.getString(R.string.celsius));
	    } else {
		return String.format("%d%s", (int) (celsius * FAHRENHEIT),
			context.getString(R.string.fahrenheit));
	    }
	}
	return String.format("%d%s", (int) (celsius * CELSIUS),
		context.getString(R.string.celsius));
    }

    public Double convertWindSpeed(Double windSeed) {
	if (windSeed == null)
	    return null;
	if (isUserExist()) {
	    if (mUser.getSpeedUnit() == MeasureUnit.KM_H) {
		return windSeed * WIND_KM_HOUR;
	    }

	    if (mUser.getSpeedUnit() == MeasureUnit.M_S) {
		return WIND_METR_SEC * windSeed.doubleValue();
	    }

	    if (mUser.getSpeedUnit() == MeasureUnit.KM_S) {
		return WIND_KM_SEC * windSeed;
	    }

	    if (mUser.getSpeedUnit() == MeasureUnit.MI_H) {
		return WIND_MI_H * windSeed;
	    }
	}
	return windSeed.doubleValue();
    }

    public String convertWindSpeed(Double windSeed, Context context) {
	if (windSeed == null) {
	    return context.getString(R.string.nan);
	}
	if (isUserExist()) {
	    if (mUser.getSpeedUnit() == MeasureUnit.KM_H) {
		return String.format("%.1f %s", windSeed * WIND_KM_HOUR,
			context.getString(R.string.km_h));
	    }

	    if (mUser.getSpeedUnit() == MeasureUnit.M_S) {
		return String.format("%.1f %s", WIND_METR_SEC * windSeed,
			context.getString(R.string.m_s));
	    }

	    if (mUser.getSpeedUnit() == MeasureUnit.KM_S) {
		return String.format("%.1f %s", WIND_KM_SEC * windSeed,
			context.getString(R.string.km_h));
	    }

	    if (mUser.getSpeedUnit() == MeasureUnit.MI_H) {
		return String.format("%.1f %s", WIND_MI_H * windSeed,
			context.getString(R.string.mi_h));
	    }
	}
	return String.valueOf(windSeed);
    }

    public String convertHumidity(Double humidity, Context context) {
	if (humidity == null) {
	    return context.getString(R.string.na);
	}
	return String.format("%.2f %s", humidity,
		context.getString(R.string.percent));
    }

    public Double convertHumidity(Double humidity) {
	return humidity;
    }

    public String convertPrecipitation(Integer precipitation, Context context) {
	if (precipitation == null) {
	    return context.getString(R.string.nan);
	}
	return String.format("%d %s", precipitation,
		context.getString(R.string.mm));
    }

    public Integer convertPrecipitation(Integer precipitation) {
	return precipitation;
    }

    public Double convertPressure(Double pressure) {
	if (pressure == null) {
	    return null;
	}
	if (isUserExist()) {
	    if (mUser.getPressureUnit() == MeasureUnit.IN_HG) {
		return PRESSURE_inHg * pressure;
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.MM_HG) {
		return PRESSURE_mmHg * pressure;
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.PA) {
		return PRESSURE_kPa * pressure;
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.PSI) {
		return PRESSURE_psi * pressure;
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.MBAR) {
		return PRESSURE_mbar * pressure;
	    }
	}
	return pressure;
    }

    public String convertPressure(Double pressure, Context context) {
	if (pressure == null) {
	    return context.getString(R.string.nan);
	}

	if (isUserExist()) {
	    if (mUser.getPressureUnit() == MeasureUnit.IN_HG) {
		return String.format("%.2f %s", PRESSURE_inHg * pressure,
			context.getString(R.string.inHg));
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.MM_HG) {
		return String.format("%.2f %s", PRESSURE_mmHg * pressure,
			context.getString(R.string.mmHg));
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.PA) {
		return String.format("%.2f %s", PRESSURE_kPa * pressure,
			context.getString(R.string.kPa));
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.PSI) {
		return String.format("%.2f %s", PRESSURE_psi * pressure,
			context.getString(R.string.psi));
	    }

	    if (mUser.getPressureUnit() == MeasureUnit.MBAR) {
		return String.format("%.2f %s", PRESSURE_mbar * pressure,
			context.getString(R.string.mbar));
	    }
	}
	return String.valueOf(pressure);
    }

    public String getTemp(Context context) {
	if (isUserExist()) {
	    switch (mUser.getTemperatureUnit()) {
	    case CELSIUS:
		return context.getResources().getString(R.string.celsius);
	    case FAHRENHEIT:
		return context.getResources().getString(R.string.fahrenheit);
	    default:
		break;
	    }
	}
	return context.getResources().getString(R.string.celsius);
    }

    public String getPressure(Context context) {
	if (isUserExist()) {
	    switch (mUser.getPressureUnit()) {
	    case MM_HG:
		return context.getResources().getString(R.string.mmHg);
	    case MBAR:
		return context.getResources().getString(R.string.mbar);
	    case PSI:
		return context.getResources().getString(R.string.psi);
	    case IN_HG:
		return context.getResources().getString(R.string.inHg);
	    case PA:
		return context.getResources().getString(R.string.kPa);
	    default:
		break;
	    }
	}
	return context.getResources().getString(R.string.mmHg);
    }

    public String getWindSpeed(Context context) {
	if (isUserExist()) {
	    switch (mUser.getSpeedUnit()) {
	    case M_S:
		return context.getResources().getString(R.string.m_s);
	    case KM_H:
		return context.getResources().getString(R.string.km_h);
	    case MI_H:
		return context.getResources().getString(R.string.mi_h);
	    default:
		break;
	    }
	}
	return context.getResources().getString(R.string.m_s);
    }

    public int getTempPosition() {
	if (isUserExist()) {
	    switch (mUser.getTemperatureUnit()) {
	    case CELSIUS:
		return 0;
	    case FAHRENHEIT:
		return 1;
	    default:
		break;
	    }
	}
	return 0;
    }

    public int getImageResourceUnit() {
	if (isUserExist()) {
	    if (mUser.getTemperatureUnit() == null) {
		return R.drawable.temp_46;
	    }
	    switch (mUser.getTemperatureUnit()) {
	    case CELSIUS:
		return R.drawable.temp_46;
	    case FAHRENHEIT:
		return R.drawable.temp_47;
	    default:
		return R.drawable.temp_46;
	    }
	}
	return R.drawable.temp_46;
    }

    public String getTempUnitType(Context context) {
	if (isUserExist()) {
	    if (mUser.getTemperatureUnit() == null) {
		return context.getResources().getString(R.string.celsius);
	    }
	    switch (mUser.getTemperatureUnit()) {
	    case CELSIUS:
		return context.getResources().getString(R.string.celsius);
	    case FAHRENHEIT:
		return context.getResources().getString(R.string.fahrenheit);
	    default:
		return context.getResources().getString(R.string.celsius);
	    }
	}
	return context.getResources().getString(R.string.celsius);
    }

    public int getPressurePosition() {
	if (isUserExist()) {
	    if (mUser.getPressureUnit() != null) {

		switch (mUser.getPressureUnit()) {
		case MM_HG:
		    return 0;
		case MBAR:
		    return 1;
		case PSI:
		    return 4;
		case IN_HG:
		    return 3;
		case PA:
		    return 2;
		default:
		    break;
		}
	    }
	}
	return 0;
    }

    public int getWindSpeedPosition() {
	if (isUserExist()) {
	    if (mUser.getSpeedUnit() != null) {
		switch (mUser.getSpeedUnit()) {
		case M_S:
		    return 0;
		case KM_H:
		    return 1;
		case MI_H:
		    return 2;
		default:
		    break;
		}
	    }
	}
	return 0;
    }

    public int getTempFromPosition(final int position) {
	if (isUserExist()) {
	    switch (position) {
	    case 0:
		return MeasureUnit.CELSIUS.getCode();
	    case 1:
		return MeasureUnit.FAHRENHEIT.getCode();
	    }
	}
	return 0;
    }

    public int getPressureFromPosition(final int position) {
	if (isUserExist()) {
	    switch (position) {
	    case 0:
		return MeasureUnit.MM_HG.getCode();
	    case 1:
		return MeasureUnit.MBAR.getCode();
	    case 2:
		return MeasureUnit.PA.getCode();
	    case 3:
		return MeasureUnit.IN_HG.getCode();
	    case 4:
		return MeasureUnit.PSI.getCode();
	    }
	}
	return 0;
    }

    public int getWindSpeedFromPosition(final int position) {
	if (isUserExist()) {
	    switch (position) {
	    case 0:
		return MeasureUnit.M_S.getCode();
	    case 1:
		return MeasureUnit.KM_H.getCode();
	    case 2:
		return MeasureUnit.MI_H.getCode();
	    }
	}
	return 0;
    }
}

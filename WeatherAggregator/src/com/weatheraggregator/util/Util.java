package com.weatheraggregator.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.Forecast;
import com.weatheraggregator.entity.Forecast.Cloudiness;
import com.weatheraggregator.model.WeatherWerviceEnum;

public class Util {
    public static final String DATE_FORMAT_YYYY_MMMM_DD = "yyyy-MMMM-dd HH:mm";
    public static final String DATE_FORMAT_DD_MMMM_YYYY = "dd.MMMM.yyyy";
    public static final String DATE_FORMAT_MMMM_YYYY = "MMMM.yyyy";

    public static String getDeviceId(Context context) {
	TelephonyManager tmg = (TelephonyManager) context
		.getSystemService(Context.TELEPHONY_SERVICE);
	String deviceIMEI = tmg.getDeviceId();
	String userMail = getAccountEmail(context);
	String deviceInfoString = deviceIMEI + userMail + Build.BOARD
		+ Build.BRAND + Build.CPU_ABI + Build.DEVICE + Build.ID
		+ Build.MANUFACTURER + Build.MODEL + Build.PRODUCT;

	UUID uid = UUID.nameUUIDFromBytes(deviceInfoString.getBytes());
	return uid.toString();
    }

    // test user androidtestuser@gmail.com
    // test user defaylt333@gmail.com
    public static String getAccountEmail(Context context) {
	AccountManager accountManager = AccountManager.get(context);
	Account account = getAccount(accountManager);
	// return "androidtestuser@gmail.com";

	if (account == null) {
	    return "defaylt333@gmail.com";
	} else {
	    return account.name;
	}
    }

    public static boolean hasHoneycomb() {
	return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    // use only for testing
    public static String inputStreamToString(java.io.InputStream is) {
	java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	// s.close();
	String value = s.hasNext() ? s.next() : "";
	// s.close();
	return value;
    }

    // use only for testing
    public static InputStream stringToInputStream(String text) {
	if (text != null) {
	    return new ByteArrayInputStream(text.getBytes());
	} else {
	    return null;
	}
    }

    private static Account getAccount(AccountManager accountManager) {
	Account[] accounts = accountManager.getAccountsByType("com.google");
	Account account;
	if (accounts.length > 0) {
	    account = accounts[0];
	} else {
	    account = null;
	}
	return account;
    }

    /**
     * 
     * @param cloudyType
     * @return uri of cloudiness image
     */
    public static String getCloudyImagePath(Cloudiness cloudyType) {
	// String imageUri = "drawable://" + R.drawable.image;
	StringBuilder imageUri = new StringBuilder("drawable://");// R.drawable.icon;
	if (cloudyType == null) {
	    return imageUri.append(R.drawable.cloudiness_45).toString();
	}
	switch (cloudyType) {
	case BLIZZARD:
	    imageUri.append(R.drawable.cloudiness_23);
	    break;
	case CLOUDY_WITH_SUN:
	    imageUri.append(R.drawable.cloudiness_8);
	    break;
	case HAIL:
	    imageUri.append(R.drawable.cloudiness_24);
	    break;
	case N_A:
	    imageUri.append(R.drawable.cloudiness_45);
	    break;
	case CLOUDY:
	    imageUri.append(R.drawable.cloudiness_14);
	    break;
	case FOG:
	    imageUri.append(R.drawable.cloudiness_12);
	    break;
	case OVERCAST:
	    imageUri.append(R.drawable.cloudiness_25);
	    break;
	case RAIN:
	    imageUri.append(R.drawable.cloudiness_18);
	    break;
	case RAIN_WITH_THUNDERSTORMS:
	    imageUri.append(R.drawable.cloudiness_15);
	    break;
	case SHORT_RAIN:
	    imageUri.append(R.drawable.cloudiness_17);
	    break;
	case SLEET:
	    imageUri.append(R.drawable.cloudiness_24);
	    break;
	case SNOW:
	    imageUri.append(R.drawable.cloudiness_22);
	    break;
	case SNOW_WITH_RAIN:
	    imageUri.append(R.drawable.cloudiness_24);
	    break;
	case SUNNY:
	    imageUri.append(R.drawable.cloudiness_2);
	    break;
	default:
	    imageUri.append(R.drawable.cloudiness_45);
	    break;
	}
	return imageUri.toString();
    }

    public static Bitmap getForecastImage(Cloudiness cloudy) {
	String imageUri = Util.getCloudyImagePath(cloudy);
	return ImageLoader.getInstance().loadImageSync(imageUri);
    }

    public static int getWindDirectionImageCode(final Integer direction) {
	if (direction == null) {
	    // TODO need diction n/a image
	    return R.drawable.direction_0;
	}
	Integer windDirection = Integer.valueOf(getWindApproximation(direction
		.intValue()));
	switch (windDirection) {
	case 0:
	case 360:
	    return R.drawable.direction_0;
	case 45:
	    return R.drawable.direction_45;
	case 90:
	    return R.drawable.direction_90;
	case 135:
	    return R.drawable.direction_135;
	case 180:
	    return R.drawable.direction_180;
	case 225:
	    return R.drawable.direction_225;
	case 270:
	    return R.drawable.direction_270;
	case 315:
	    return R.drawable.direction_315;
	default:
	    // TODO need diction n/a image
	    return R.drawable.direction_0;
	}
    }

    public static String getCloudyStringValue(Context context,
	    Cloudiness cloudyType) {
	if (cloudyType == null) {
	    return context.getString(R.string.na);
	}
	switch (cloudyType) {
	case BLIZZARD:
	    return context.getString(R.string.blizzard);
	case CLOUDY_WITH_SUN:
	    return context.getString(R.string.cloudy_with_sun);
	case HAIL:
	    return context.getString(R.string.hail);
	case N_A:
	    return context.getString(R.string.na);
	case CLOUDY:
	    return context.getString(R.string.cloudy);
	case FOG:
	    return context.getString(R.string.fog);
	case OVERCAST:
	    return context.getString(R.string.overcast);
	case RAIN:
	    return context.getString(R.string.rain);
	case RAIN_WITH_THUNDERSTORMS:
	    return context.getString(R.string.rain_with_thunderstorms);
	case SHORT_RAIN:
	    return context.getString(R.string.short_rain);
	case SLEET:
	    return context.getString(R.string.slee);
	case SNOW:
	    return context.getString(R.string.snow);
	case SNOW_WITH_RAIN:
	    return context.getString(R.string.snow_with_rain);
	case SUNNY:
	    return context.getString(R.string.sunny);
	default:
	    return context.getString(R.string.na);
	}
    }

    public static String getCloudyTypeUri(Context context, Cloudiness cloudyType) {
	// TODO:
	StringBuilder imageUri = new StringBuilder("drawable://");
	if (cloudyType == null) {
	    return imageUri.append(R.drawable.cloudiness_45).toString();
	}
	switch (cloudyType) {
	case BLIZZARD:
	    return imageUri.append(R.drawable.cloudiness_45).toString();
	    // return context.getString(R.string.temp);
	case CLOUDY_WITH_SUN:
	    imageUri.append(R.drawable.cloudiness_8);
	    break;
	case HAIL:
	    imageUri.append(R.drawable.cloudiness_24);
	    break;
	case N_A:
	    imageUri.append(R.drawable.cloudiness_45);
	    break;
	case CLOUDY:
	    imageUri.append(R.drawable.cloudiness_14);
	    break;
	case FOG:
	    imageUri.append(R.drawable.cloudiness_12);
	    break;
	case OVERCAST:
	    imageUri.append(R.drawable.cloudiness_25);
	    break;
	case RAIN:
	    imageUri.append(R.drawable.cloudiness_18);
	    break;
	case RAIN_WITH_THUNDERSTORMS:
	    imageUri.append(R.drawable.cloudiness_15);
	    break;
	case SHORT_RAIN:
	    imageUri.append(R.drawable.cloudiness_17);
	    break;
	case SLEET:
	    imageUri.append(R.drawable.cloudiness_24);
	    break;
	case SNOW:
	    imageUri.append(R.drawable.cloudiness_22);
	    break;
	case SNOW_WITH_RAIN:
	    imageUri.append(R.drawable.cloudiness_24);
	    break;
	case SUNNY:
	    imageUri.append(R.drawable.cloudiness_2);
	    break;
	default:
	    imageUri.append(R.drawable.cloudiness_45);
	    break;
	}
	return imageUri.toString();
    }

    public static void closeSoftKeyboard(EditText et, Context context) {
	if (et != null && context != null) {
	    InputMethodManager imm = (InputMethodManager) context
		    .getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}
    }

    /**
     * 
     * @param context
     * @param forecast
     *            - which will share
     * @return message for share
     */
    public static String getShareMessage(Context context, Forecast forecast) {
	if (context != null && forecast != null) {
	    Integer currentTemp = MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getTemp());
	    Integer maxTemp = MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getMaxTemp());
	    Integer minTemp = MeasureUnitHelper.getSingleton()
		    .convertTemperature(forecast.getMinTemp());
	    String wind = MeasureUnitHelper.getSingleton().convertWindSpeed(
		    forecast.getWindSpeed(), context);
	    if (currentTemp != null && maxTemp != null && minTemp != null
		    && wind != null) {
		return String.format(context.getString(R.string.share_message),
			currentTemp, minTemp, maxTemp, wind);
	    }
	}
	return "";
    }

    @SuppressLint("NewApi")
    public static int getWindowWidth(Context context) {
	int width;
	if (android.os.Build.VERSION.SDK_INT >= 13) {
	    WindowManager wm = (WindowManager) context
		    .getSystemService(Context.WINDOW_SERVICE);
	    Display display = wm.getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    width = size.x;
	} else {
	    WindowManager wm = (WindowManager) context
		    .getSystemService(Context.WINDOW_SERVICE);
	    Display display = wm.getDefaultDisplay();
	    width = display.getWidth(); // deprecated
	}
	return width;
    }

    public String getWeatherServiceLogoUri(WeatherWerviceEnum service) {
	StringBuilder imageUri = new StringBuilder("drawable://");
	switch (service) {
	case HAMWEATHER:
	    imageUri.append("");
	    break;
	case WEATHERUA:
	    imageUri.append("");
	    break;
	case YAHOO:
	    imageUri.append("");
	    break;
	case YANDEX:
	    imageUri.append("");
	    break;
	case ZUPR:
	    imageUri.append("");
	    break;
	default:
	    break;
	}
	return imageUri.toString();
    }

    // NSArray *windDirections = @[@0, @45, @90, @135, @180, @225, @270, @315,
    // @360];
    public static int getWindApproximation(int windDirection) {
	int[] directions = new int[] { 0, 45, 90, 135, 180, 225, 270, 315, 360 };
	for (int index = 0; index <= directions.length - 1; index++) {
	    if (windDirection <= directions[index]) {
		int firsAngle = Math.abs(directions[index] - windDirection);
		int secondAngle = Math.abs(windDirection
			- (directions[index] - 45));
		if (firsAngle < secondAngle) {
		    return directions[index];
		} else {
		    return directions[index - 1];
		}
	    }
	}
	return 0;
    }

    public static int getHourApproximation(int hour) {
	int[] hoursPeriod = new int[] { 0, 6, 12, 18, 24 };
	for (int index = 0; index <= hoursPeriod.length - 2; index++) {
	    if (hour >= hoursPeriod[index] && hoursPeriod[index + 1] >= hour) {
		return index + 1;
	    }
	}
	return 0;
    }
}

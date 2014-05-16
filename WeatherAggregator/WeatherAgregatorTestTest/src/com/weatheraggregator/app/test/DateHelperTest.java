package com.weatheraggregator.app.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.weatheraggregator.app.test.utils.Utils;
import com.weatheraggregator.util.DateHelper;

public class DateHelperTest extends InstrumentationTestCase {
    private static final String TAG = "DateHelperTest";

    @Override
    protected void setUp() throws Exception {
	Log.d(TAG, "setUp(): " + getName());
	super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
	Log.d(TAG, "tearDown(): " + getName());
	super.tearDown();
    }

    @SmallTest
    public void testCreateDate() {
	Date testDate = DateHelper.createDate(2014, 2, 5, 3, 55);
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR, 2014);
	calendar.set(Calendar.MONTH, 2);
	calendar.set(Calendar.DAY_OF_MONTH, 5);
	calendar.set(Calendar.HOUR_OF_DAY, 3);
	calendar.set(Calendar.MINUTE, 55);

	Calendar testCalendar = Calendar.getInstance();
	testCalendar.setTime(testDate);
	assertEquals("Test fail: day of month not equal", true,
		calendar.get(Calendar.DAY_OF_MONTH) == testCalendar
			.get(Calendar.DAY_OF_MONTH));
	assertEquals("Test fail: year not equal", true,
		calendar.get(Calendar.YEAR) == testCalendar.get(Calendar.YEAR));
	assertEquals("Test fail: month not equal", true,
		calendar.get(Calendar.MONTH) == testCalendar
			.get(Calendar.MONTH));
	assertEquals("Test fail: hour not equal", true,
		calendar.get(Calendar.HOUR_OF_DAY) == testCalendar
			.get(Calendar.HOUR_OF_DAY));
	assertEquals("Test fail: minute not equal", true,
		calendar.get(Calendar.MINUTE) == testCalendar
			.get(Calendar.MINUTE));
    }

    @SmallTest
    public void testGetStartDateByDate() {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.HOUR, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	calendar.set(Calendar.SECOND, 0);

	long timeInMillis = DateHelper.getStartDateByDate(new Date()).getTimeInMillis();

	assertEquals("Test fail: time not equal", true,
		calendar.getTimeInMillis() == timeInMillis);
    }

    @SmallTest
    public void testGetEndDateByDate() {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.HOUR, 23);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);
	calendar.set(Calendar.MILLISECOND, 999);

	long timeInMillis = DateHelper.getEndDateByDate(new Date()).getTimeInMillis();
	assertEquals("Test fail: time not equal", true,
		calendar.getTimeInMillis() == timeInMillis);
    }

    @SmallTest
    public void testCreateCurrentBeginDayCalendar() {
	Calendar calendar = new GregorianCalendar();
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);

	Calendar testCalendar = DateHelper.createCurrentBeginDayCalendar();

	if (!testCalendar.equals(calendar)) {
	    Utils.writeLogCat("Test fail: time not equal");
	}
    }

    @SmallTest
    public void testClearTime() {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);

	Date testDate = DateHelper.clearTime(new Date());
	Calendar testCalendar = Calendar.getInstance();
	testCalendar.setTime(testDate);

	assertEquals("Test fail: hour not equal", true,
		calendar.get(Calendar.HOUR_OF_DAY) == testCalendar
			.get(Calendar.HOUR_OF_DAY));
	assertEquals("Test fail: minute not equal", true,
		calendar.get(Calendar.MINUTE) == testCalendar
			.get(Calendar.MINUTE));
	assertEquals("Test fail: second not equal", true,
		calendar.get(Calendar.SECOND) == testCalendar
			.get(Calendar.SECOND));
	assertEquals("Test fail: millisecond not equal", true,
		calendar.get(Calendar.MILLISECOND) == testCalendar
			.get(Calendar.MILLISECOND));
    }

    @SmallTest
    public void testEqualsIgnoreTime() {
	assertEquals("Test fail: date not equal", true,
		DateHelper.equalsIgnoreTime(new Date(), new Date()));
    }

    @SmallTest
    public void testGetMonthOfDate() {
	Calendar testDate = Calendar.getInstance();
	final int MONTH = 10;
	testDate.set(Calendar.MONTH, MONTH);
	testDate.set(Calendar.DAY_OF_MONTH, 1);
	assertEquals("Test fail: month not equal", MONTH,
		DateHelper.getMonthOfDate(testDate.getTime()));
    }

    @SmallTest
    public void testGetTwelve() {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.HOUR_OF_DAY, 12);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.MILLISECOND, 0);

	Date testDate = DateHelper.getTwelve(new Date());
	Calendar testCalendar = Calendar.getInstance();
	testCalendar.setTime(testDate);
	assertEquals("Test fail: hour not equal", true,
		calendar.get(Calendar.HOUR_OF_DAY) == testCalendar
			.get(Calendar.HOUR_OF_DAY));
	assertEquals("Test fail: minute not equal", true,
		calendar.get(Calendar.MINUTE) == testCalendar
			.get(Calendar.MINUTE));
	assertEquals("Test fail: millisecond not equal", true,
		calendar.get(Calendar.MILLISECOND) == testCalendar
			.get(Calendar.MILLISECOND));
    }

    @SmallTest
    public void testGetLastDateOfMonthByDate() {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,
		calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	calendar.set(Calendar.HOUR, 23);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);

	Date testDate = DateHelper.getLastDateOfMonthByDate(new Date());
	Calendar testCalendar = Calendar.getInstance();
	testCalendar.setTime(testDate);

	assertEquals("Test fail: month not equal", true,
		calendar.get(Calendar.MONTH) == testCalendar
			.get(Calendar.MONTH));
	assertEquals("Test fail: hour not equal", true,
		calendar.get(Calendar.HOUR_OF_DAY) == testCalendar
			.get(Calendar.HOUR_OF_DAY));
	assertEquals("Test fail: minute not equal", true,
		calendar.get(Calendar.MINUTE) == testCalendar
			.get(Calendar.MINUTE));
    }
}

package com.weatheraggregator.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
    public static Date createDate(final int year, final int month,
	    final int day, final int hour, final int minute) {

	Calendar calendar = new GregorianCalendar(year, month, day, hour,
		minute);
	return calendar.getTime();
    }

    public static Calendar getStartDateByDate(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	calendar.set(Calendar.SECOND, 0);
	return calendar;
    }

    public static Date clearMinuteWithSecond(Date date) {
	Calendar calendar = Calendar.getInstance();
	if (date != null)
	    calendar.setTime(date);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	calendar.set(Calendar.SECOND, 0);
	return calendar.getTime();
    }

    public static void decrementMonthCalendar(Calendar calendar) {
	calendar.add(Calendar.MONTH, -1);
    }

    public static void incrementMonthCalendar(Calendar calendar) {
	calendar.add(GregorianCalendar.MONTH, 1);
    }

    public static Date incrementDate(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.DAY_OF_YEAR, 1);
	return calendar.getTime();
    }

    public static Date decrementDate(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.DAY_OF_YEAR, -1);
	return calendar.getTime();
    }

    public static Calendar getEndDateByDate(Date date) {
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);
	c.set(year, month, day, 23, 59, 59);
	c.set(Calendar.SECOND, 59);
	c.set(Calendar.MILLISECOND, 999);
	return c;
    }

    public static Calendar getDateWithHour(Date date, int hour) {
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);
	c.set(year, month, day, hour, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.MILLISECOND, 0);
	return c;
    }

    public static Calendar createCurrentBeginDayCalendar() {
	Calendar c = new GregorianCalendar();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);
	c.set(year, month, day, 0, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.MILLISECOND, 0);
	return c;
    }

    public static Date clearTime(final Date date) {
	Calendar c = new GregorianCalendar();
	c.setTime(date);
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);
	c.set(year, month, day, 0, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.MILLISECOND, 0);

	return c.getTime();
    }

    public static boolean equalsIgnoreTime(final Date date1, final Date date2) {
	Date clearedDate1 = DateHelper.clearTime(date1);
	Date clearedDate2 = DateHelper.clearTime(date2);
	return clearedDate1.equals(clearedDate2);
    }

    public static Date getFirstDateOfMonthByDate(Date date) {
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	c.set(year, month, 1, 0, 0);
	return c.getTime();
    }

    public static int getMonthOfDate(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.MONTH);
    }

    public static Date getTwelve(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY, 12);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	return calendar.getTime();
    }

    public static Date getLastDateOfMonthByDate(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_MONTH,
		calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	calendar.set(Calendar.HOUR, 23);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);
	calendar.set(Calendar.MILLISECOND, 999);
	return calendar.getTime();
    }

    public static Date getFirstDateOfWeek(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	calendar.set(Calendar.HOUR, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	return calendar.getTime();
    }

    public static Date getLastDateOfWeek(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);

	calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	calendar.add(Calendar.DATE, 5);
	calendar.set(Calendar.HOUR, 24);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);
	calendar.set(Calendar.MILLISECOND, 999);
	return calendar.getTime();
    }
}

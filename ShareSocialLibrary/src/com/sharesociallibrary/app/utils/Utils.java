package com.sharesociallibrary.app.utils;

import android.util.Log;

import com.sharesociallibrary.app.ShareSocialManager;

public class Utils
{

	private static final String LOG_TAG = ShareSocialManager.class.getSimpleName();
	private static final String LOG_FORMAT = "%s : %s";
	private static final boolean DEBUG = true;

	public static void printLog(final String tag, final String message)
	{
		if (DEBUG)
		{
			Log.d(LOG_TAG, getLogMessage(tag, message));
		}
	}

	public static void printError(final String tag, final String message)
	{
		if (DEBUG)
		{
			Log.e(LOG_TAG, getLogMessage(tag, message));
		}
	}

	/**
	 * Get format message for logs
	 * 
	 * @param tag
	 * @param message
	 * @return format message
	 */
	private static String getLogMessage(final String tag, final String message)
	{
		return String.format(LOG_FORMAT, tag, message);
	}

}

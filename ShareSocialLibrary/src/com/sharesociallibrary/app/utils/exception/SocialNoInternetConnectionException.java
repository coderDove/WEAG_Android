package com.sharesociallibrary.app.utils.exception;

/**
 * Need throw when internet connection is failed
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public class SocialNoInternetConnectionException extends SocialException
{

	public SocialNoInternetConnectionException()
	{
		super("No internet connection");
	}

}

package com.sharesociallibrary.app.utils.exception;

/**
 * Unknown error
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public class SocialUnknownErrorException extends SocialException
{

	public SocialUnknownErrorException()
	{
		super("Unknown error");
	}

}

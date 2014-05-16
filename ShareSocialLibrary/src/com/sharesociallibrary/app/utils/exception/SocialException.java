package com.sharesociallibrary.app.utils.exception;

/**
 * Base class of exceptions
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public abstract class SocialException extends Exception
{
	public SocialException(String message)
	{
		super(message);
	}

	public SocialException(Throwable cause)
	{
		super(cause);
	}

	public SocialException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

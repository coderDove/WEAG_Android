package com.sharesociallibrary.app.utils.exception;

/**
 * Need throw when response not parse
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public class SocialParseException extends SocialException
{

	public SocialParseException()
	{
		super("Response parse exception");
	}

}

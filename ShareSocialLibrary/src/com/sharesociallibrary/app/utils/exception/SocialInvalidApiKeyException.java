package com.sharesociallibrary.app.utils.exception;

/**
 * Need throw when api key is empty or incorrect
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public class SocialInvalidApiKeyException extends SocialException
{

	public SocialInvalidApiKeyException()
	{
		super("Api key is empty or incorrect");
	}

}

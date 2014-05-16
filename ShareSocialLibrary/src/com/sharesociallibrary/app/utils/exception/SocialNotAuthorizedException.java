package com.sharesociallibrary.app.utils.exception;

/**
 * Need throw when not authorized in social network
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public class SocialNotAuthorizedException extends SocialException
{

	public SocialNotAuthorizedException()
	{
		super("Authorization failed");
	}

}

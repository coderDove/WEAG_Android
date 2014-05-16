package com.sharesociallibrary.app.utils.exception;

/**
 * Need throw when share post is duplicate
 * 
 * @author Viacheslav.Titov
 * 
 */
@SuppressWarnings("serial")
public class SocialDuplicateMessageException extends SocialException
{

	public SocialDuplicateMessageException()
	{
		super("Share post is duplicate");
	}

}

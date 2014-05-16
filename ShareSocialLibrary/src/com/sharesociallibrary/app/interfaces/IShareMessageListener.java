package com.sharesociallibrary.app.interfaces;

import com.sharesociallibrary.app.utils.SocialType;
import com.sharesociallibrary.app.utils.exception.SocialException;

/**
 * 
 * @author Viacheslav.Titov
 * 
 */
public interface IShareMessageListener
{
	/**
	 * Need call when social share is starting
	 * 
	 * @param social
	 *            - name of social
	 */
	public void onShareRun(final SocialType social);

	/**
	 * Need call when social share is finished
	 * 
	 * @param social
	 *            - name of social
	 * @param postId
	 *            - id of post message
	 */
	public void onShareComplete(final SocialType social, final String postId);

	/**
	 * Need call when throw exception in sharing process
	 * 
	 * @param error
	 */
	public void onShareError(final SocialType social, final SocialException error);

	public void onFinished();

	/**
	 * Show progress dialog
	 * 
	 * @param social
	 *            - name of social
	 */
	public void showProgressDialog(SocialType social);

	/**
	 * Dismiss progress dialog, if it showing
	 */
	public void dismissProgressDialog();

}

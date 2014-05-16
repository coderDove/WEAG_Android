package com.sharesociallibrary.app.socials;

import com.sharesociallibrary.app.interfaces.IManagerLifeCycle;
import com.sharesociallibrary.app.interfaces.IShareMessageListener;
import com.sharesociallibrary.app.interfaces.ISocialCompleteRunListener;
import com.sharesociallibrary.app.utils.Utils;

/**
 * Base abstract class for all managers of socials
 * 
 * @author Viacheslav.Titov
 * 
 */
public abstract class BaseAbstractManager implements IManagerLifeCycle
{
	
	/**
	 * Write message to LogCat, debug type
	 * 
	 * @param tag
	 * @param message
	 */
	public static void printLog(final String tag, final String message)
	{
		Utils.printLog(tag, message);
	}

	/**
	 * Write message to LogCat, error type
	 * 
	 * @param tag
	 * @param message
	 */
	public static void printError(final String tag, final String message)
	{
		Utils.printError(tag, message);
	}
	
	/**
	 * Share message to social network
	 * 
	 * @param message
	 * @param listener
	 *            - Callback listener
	 *            {@link com.sharesociallibrary.app.interfaces.IShareMessageListener}
	 */
	public abstract void sendMessage(final String message, final IShareMessageListener listener);

	public abstract void setSocialCompleteRunListener(final ISocialCompleteRunListener listener);

	/**
	 * Method need call when share message is complete
	 */
	public abstract void onPostExecute();

}

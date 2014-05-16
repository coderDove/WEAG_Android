package com.sharesociallibrary.app.socials.twitter;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharesociallibrary.app.R;
import com.sharesociallibrary.app.interfaces.IShareMessageListener;
import com.sharesociallibrary.app.interfaces.ISocialCompleteRunListener;
import com.sharesociallibrary.app.socials.BaseAbstractManager;
import com.sharesociallibrary.app.utils.SocialType;
import com.sharesociallibrary.app.utils.exception.SocialDuplicateMessageException;
import com.sharesociallibrary.app.utils.exception.SocialException;
import com.sharesociallibrary.app.utils.exception.SocialInvalidApiKeyException;
import com.sharesociallibrary.app.utils.exception.SocialNotAuthorizedException;

/**
 * Class manager for twitter network social
 * 
 * @author Viacheslav.Titov
 * 
 */
public final class TwitterManager extends BaseAbstractManager
{
	private static final String TOKEN_PARAM = "TOKEN_PARAM";
	private static final String TOKEN_SECRET_PARAM = "TOKEN_SECRET_PARAM";

	private static final String OAUTH_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";
	private static final String OAUTH_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";
	private static final String OAUTH_AUTHORIZE = "https://api.twitter.com/oauth/authorize";

	private static final String FORMAT_URL_DENIED = "%s/?denied";
	private static final String FORMAT_URL_OAUTH_TOKEN = "%s/?oauth_token";

	private ISocialCompleteRunListener mSocialCompleteRunListener;
	private IShareMessageListener mShareMessageListener;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private OAuthProvider httpOauthprovider;
	private String mMessage;
	private String mToken;
	private String mSecretToket;
	private Context mContext;
	private WebView mWebView;
	private AlertDialog mDialog;
	private String mApiKey;
	private String mApiSecret;
	private String mCallbackUrl;

	public TwitterManager(Activity activity, String apiKey, String apiSecret, String callbackUrl)
	{
		mContext = activity;
		mToken = getToken();
		mSecretToket = getTokenSecret();
		mApiKey = apiKey;
		mApiSecret = apiSecret;
		mCallbackUrl = callbackUrl;
		// try
		// {
		// verifyApiSettings();
		// }
		// catch (SocialInvalidApiKeyException e)
		// {
		// e.printStackTrace();
		// }
	}

	private void verifyApiSettings() throws SocialInvalidApiKeyException
	{
		if (mApiKey == null || mApiKey.isEmpty() || mApiSecret == null || mApiSecret.isEmpty() || mCallbackUrl == null || mCallbackUrl.isEmpty())
		{
			throw new SocialInvalidApiKeyException();
		}
	}

	@Override
	public void sendMessage(String message, final IShareMessageListener listener)
	{
		mMessage = message;
		mShareMessageListener = listener;
		if (isAutorized())
		{
			showDialogAuthorizationOrLogout();
		}
		else
		{
			auth();
		}
	}

	private void sendMessage()
	{
		new TwittAsync().execute();
	}

	/**
	 * Display dialog if user authorized. Dialog proposes logout or share for
	 * authorized user.
	 */
	private void showDialogAuthorizationOrLogout()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle(R.string.twitter);
		builder.setMessage(R.string.twitter_logout_message);
		builder.setPositiveButton(R.string.ok, mDialogClickListener);
		builder.setNegativeButton(R.string.cancel, mDialogClickListener);
		mDialog = builder.create();
		mDialog.show();
	}

	/**
	 * ClickListener for authorizationOrLogout dialog
	 */
	private OnClickListener mDialogClickListener = new OnClickListener()
	{

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
				case DialogInterface.BUTTON_POSITIVE:
					logout();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					sendMessage();
					break;
			}
		}
	};

	/**
	 * LogOut from Twitter. Clear current session.
	 */
	private void logout()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(TOKEN_PARAM);
		editor.remove(TOKEN_SECRET_PARAM);
		editor.commit();
		CookieSyncManager.createInstance(mContext);
		CookieManager.getInstance().removeAllCookie();
		auth();
	}

	@Override
	public void setSocialCompleteRunListener(ISocialCompleteRunListener listener)
	{
		mSocialCompleteRunListener = listener;
	}

	@Override
	public void onPostExecute()
	{
		mSocialCompleteRunListener.onSocialCompleteRun(SocialType.TWITTER);
	}

	/**
	 * run authorization
	 */
	private void auth()
	{
		new OAUthLinkLoader().execute();
	}

	/**
	 * 
	 * @return true if user still authorizetion
	 */
	private boolean isAutorized()
	{
		return mToken != null && mSecretToket != null;
	}

	/**
	 * 
	 * @return token of authorization
	 */
	private String getToken()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		return prefs.getString(TOKEN_PARAM, null);
	}

	/**
	 * 
	 * @return token secret of authorizetion
	 */
	private String getTokenSecret()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		return prefs.getString(TOKEN_SECRET_PARAM, null);
	}

	/**
	 * AsyncTask which load url of authorizetion
	 * 
	 * @author Viacheslav.Titov
	 * 
	 */
	private final class OAUthLinkLoader extends AsyncTask<Void, Void, String>
	{

		private SocialNotAuthorizedException mSocialException = null;

		@Override
		protected String doInBackground(Void... params)
		{
			httpOauthConsumer = new CommonsHttpOAuthConsumer(mApiKey, mApiSecret);
			httpOauthprovider = new DefaultOAuthProvider(OAUTH_REQUEST_TOKEN, OAUTH_ACCESS_TOKEN, OAUTH_AUTHORIZE);
			String authUrl = null;
			try
			{
				authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, mCallbackUrl);
			}
			catch (OAuthMessageSignerException e)
			{
				mSocialException = new SocialNotAuthorizedException();
			}
			catch (OAuthNotAuthorizedException e)
			{
				mSocialException = new SocialNotAuthorizedException();
			}
			catch (OAuthExpectationFailedException e)
			{
				mSocialException = new SocialNotAuthorizedException();
			}
			catch (OAuthCommunicationException e)
			{
				mSocialException = new SocialNotAuthorizedException();
			}
			return authUrl;
		}

		@Override
		protected void onPostExecute(String url)
		{
			if (url != null)
			{
				mWebView.loadUrl(url);
			}
			else
			{
				mShareMessageListener.onShareError(SocialType.TWITTER, mSocialException);
				TwitterManager.this.onPostExecute();
			}
			super.onPostExecute(url);
		}

	}

	/**
	 * Custom WebViewClient for twitter WebView
	 */
	private WebViewClient mTwitterWebClient = new WebViewClient()
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			mWebView.loadUrl(url);
			mWebView.requestFocus(View.FOCUS_DOWN);
			return super.shouldOverrideUrlLoading(view, url);
		}

		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon)
		{
			if (url.startsWith(String.format(FORMAT_URL_DENIED, mCallbackUrl)))
			{
				mWebView.stopLoading();
			}
			if (url.startsWith(String.format(FORMAT_URL_OAUTH_TOKEN, mCallbackUrl)))
			{
				mWebView.stopLoading();
				processTokenUrl(url);
			}
		};
	};

	private void processTokenUrl(String url)
	{
		Uri uri = Uri.parse(url);
		String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
		String token = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_TOKEN);
		new TokenValidator().execute(verifier, token);
	}

	/**
	 * AsyncTask for validation of token
	 * 
	 * @author Viacheslav.Titov
	 * 
	 */
	private final class TokenValidator extends AsyncTask<String, SocialException, SocialException>
	{
		@Override
		protected SocialException doInBackground(String... params)
		{
			SocialException socialException = null;
			try
			{
				httpOauthprovider.retrieveAccessToken(httpOauthConsumer, params[0]);
				String oAuthToken = httpOauthConsumer.getToken();
				String oAuthTokenSecret = httpOauthConsumer.getTokenSecret();

				if (oAuthToken != null && oAuthTokenSecret != null)
				{
					saveTokens(oAuthToken, oAuthTokenSecret);
					return null;
				}
				else
				{
					socialException = new SocialInvalidApiKeyException();
				}

			}
			catch (OAuthMessageSignerException e)
			{
				socialException = new SocialInvalidApiKeyException();
			}
			catch (OAuthNotAuthorizedException e)
			{
				socialException = new SocialNotAuthorizedException();
			}
			catch (OAuthExpectationFailedException e)
			{
				socialException = new SocialInvalidApiKeyException();
			}
			catch (OAuthCommunicationException e)
			{
				socialException = new SocialInvalidApiKeyException();
			}
			return socialException;
		}

		@Override
		protected void onPostExecute(SocialException exception)
		{
			if (exception == null)
			{
				new TwittAsync().execute();
			}
			else
			{
				mShareMessageListener.onShareError(SocialType.TWITTER, exception);
				TwitterManager.this.onPostExecute();
			}
			super.onPostExecute(exception);
		}
	}

	/**
	 * AsyncTask which create twitt
	 * 
	 * @author Viacheslav.Titov
	 * 
	 */
	private final class TwittAsync extends AsyncTask<Void, SocialException, SocialException>
	{

		@Override
		protected void onPreExecute()
		{
			mShareMessageListener.showProgressDialog(SocialType.TWITTER);
			super.onPreExecute();
		}

		@Override
		protected SocialException doInBackground(Void... params)
		{
			AccessToken act = new AccessToken(mToken, mSecretToket);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(mApiKey, mApiSecret);
			twitter.setOAuthAccessToken(act);
			SocialException socialException = null;
			try
			{
				StatusUpdate statusUpdate = new StatusUpdate(mMessage);
				twitter.updateStatus(statusUpdate);
			}
			catch (TwitterException e)
			{
				socialException = new SocialDuplicateMessageException();
			}
			return socialException;
		}

		@Override
		protected void onPostExecute(SocialException exception)
		{
			super.onPostExecute(exception);
			if (exception == null)
			{
				mShareMessageListener.onShareComplete(SocialType.TWITTER, null);
			}
			else
			{
				mShareMessageListener.onShareError(SocialType.TWITTER, exception);
			}
			TwitterManager.this.onPostExecute();
		}
	}

	/**
	 * Save toekn and secret params of current session
	 * 
	 * @param token
	 * @param secret
	 */
	private void saveTokens(String token, String secret)
	{
		mToken = token;
		mSecretToket = secret;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(TOKEN_PARAM, token);
		editor.putString(TOKEN_SECRET_PARAM, secret);
		editor.commit();
	}

	/**
	 * set WebView
	 * 
	 * @param webView
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void setWebView(WebView webView)
	{
		mWebView = webView;
		if (mWebView != null)
		{
			mWebView.setVerticalScrollBarEnabled(false);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.setWebViewClient(mTwitterWebClient);
			mWebView.getSettings().setJavaScriptEnabled(true);
		}
	}

	@Override
	public void onResume()
	{

	}

	@Override
	public void onPause()
	{

	}

	@Override
	public void onDestroy()
	{

	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

	}

	@Override
	public void onCreate(Bundle bundle, Activity activity)
	{

	}

}

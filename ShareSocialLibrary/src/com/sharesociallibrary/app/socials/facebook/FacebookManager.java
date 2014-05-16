package com.sharesociallibrary.app.socials.facebook;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.internal.SessionTracker;
import com.facebook.internal.Utility;
import com.sharesociallibrary.app.interfaces.IShareMessageListener;
import com.sharesociallibrary.app.interfaces.ISocialCompleteRunListener;
import com.sharesociallibrary.app.socials.BaseAbstractManager;
import com.sharesociallibrary.app.utils.SocialType;
import com.sharesociallibrary.app.utils.exception.SocialParseException;
import com.sharesociallibrary.app.utils.exception.SocialUnknownErrorException;

/**
 * Class manager of Facebook social network
 * 
 * @author Viacheslav.Titov
 * 
 */
public final class FacebookManager extends BaseAbstractManager implements Session.StatusCallback
{

	private static final String TAG = FacebookManager.class.getSimpleName();
	private static final List<String> GENERAL_PERMISSION_ARRAY = Arrays.asList("publish_actions");
	private static final String GRAPH_PATH_FEED = "me/feed";
	private static final String PARAMS_MESSAGE = "message";
	private static final int REQUEST_CODE_FACEBOOK_CANCEL = 64206;

	private SessionTracker mSessionTracker;
	private String mUserId;
	private String mShareMessage;
	private IShareMessageListener mShareMessageListener;
	private ISocialCompleteRunListener mSocialCompleteRunListener;
	private Session.OpenRequest mOpenRequest;
	private Context mContext;
	private UiLifecycleHelper mUiHelper;
	private Builder mBuilder;

	public FacebookManager(Activity activity)
	{
		mSessionTracker = new SessionTracker(activity, this, null, false);
		mContext = activity;
		printFacebookKeyHash(activity, TAG);
		mOpenRequest = new Session.OpenRequest(activity);
	}

	// TODO: not used now
	public static class Builder
	{
		private boolean confirmLogout = false;

		public Builder()
		{}

		public Builder(boolean isConfirmLogout)
		{
			setConfirmLogout(isConfirmLogout);
		}

		public boolean isConfirmLogout()
		{
			return confirmLogout;
		}

		public void setConfirmLogout(boolean confirmLogout)
		{
			this.confirmLogout = confirmLogout;
		}

	}

	@Override
	public void sendMessage(final String message, final IShareMessageListener listener)
	{
		mShareMessage = message;
		mShareMessageListener = listener;
		login();
	}

	/**
	 * LogIn if need and get current session.
	 */
	private void login()
	{
		Session currentSession = mSessionTracker.getSession();
		if (currentSession == null || currentSession.getState().isClosed())
		{
			mSessionTracker.setSession(null);
			String applicationId = Utility.getMetadataApplicationId(mContext);
			Session session = new Session.Builder(mContext).setApplicationId(applicationId).build();
			Session.setActiveSession(session);
			currentSession = session;
		}
		if (!currentSession.isOpened() && mOpenRequest != null)
		{
			mOpenRequest.setDefaultAudience(SessionDefaultAudience.EVERYONE);
			mOpenRequest.setPermissions(GENERAL_PERMISSION_ARRAY);
			mOpenRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
			currentSession.openForPublish(mOpenRequest);
		}
	}

	/**
	 * Share message to user wall
	 * 
	 * @param message
	 * @param session
	 */
	public void shareMessage(final String message, Session session)
	{
		// Session session = Session.getActiveSession();
		if (session != null)
		{
			String graphPath = GRAPH_PATH_FEED;
			Bundle postParams = new Bundle();
			printLog(TAG, "share message: " + message);
			postParams.putString(PARAMS_MESSAGE, message);
			Request request = new Request(session, graphPath, postParams, HttpMethod.POST, mCallback);
			new FacebookRequestAsyncTask(request).execute();
		}
		else
		{
			printLog(TAG, "no active session");
			mShareMessageListener.onShareComplete(SocialType.FACEBOOK, null);
			onPostExecute();
		}
	}

	private final class FacebookRequestAsyncTask extends RequestAsyncTask
	{
		public FacebookRequestAsyncTask(Request request)
		{
			super(request);
		}

		@Override
		protected void onPreExecute()
		{
			mShareMessageListener.showProgressDialog(SocialType.FACEBOOK);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Response> result)
		{
			super.onPostExecute(result);
			mShareMessageListener.dismissProgressDialog();
		}
	}

	private Request.Callback mCallback = new Callback()
	{

		@Override
		public void onCompleted(Response response)
		{
			JSONObject graphResponse = null;
			String postId = null;
			try
			{
				graphResponse = response.getGraphObject().getInnerJSONObject();
				postId = graphResponse.getString("id");
				printLog(TAG, "post id: " + postId);
				mShareMessageListener.onShareComplete(SocialType.FACEBOOK, postId);
			}
			catch (JSONException e)
			{
				mShareMessageListener.onShareError(SocialType.FACEBOOK, new SocialParseException());
			}
			catch (NullPointerException e)
			{
				if (response.getError() != null)
				{
					mShareMessageListener.onShareError(SocialType.FACEBOOK, new SocialUnknownErrorException());
				}
			}
			onPostExecute();
		}
	};

	public String getUserId()
	{
		return mUserId;
	}

	@Override
	public void call(Session session, SessionState state, Exception exception)
	{
		onSessionStateChange(session, state, exception);
	}

	/**
	 * Need call when state of session is changed.
	 * 
	 * @param session
	 * @param state
	 * @param exception
	 */
	public void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		if (state.isOpened())
		{
			if (state.equals(SessionState.OPENED))
			{
				shareMessage(mShareMessage, session);
			}
		}
	}

	@Override
	public void setSocialCompleteRunListener(ISocialCompleteRunListener listener)
	{
		mSocialCompleteRunListener = listener;
	}

	@Override
	public void onPostExecute()
	{
		mSocialCompleteRunListener.onSocialCompleteRun(SocialType.FACEBOOK);
		clearSession();
	}

	/**
	 * clear current session
	 */
	public void clearSession()
	{
		if (Session.getActiveSession() != null)
		{
			Session.getActiveSession().closeAndClearTokenInformation();
		}
	}

	@Override
	public void onResume()
	{
		if (mUiHelper != null)
		{
			mUiHelper.onResume();
		}
	}

	@Override
	public void onPause()
	{
		if (mUiHelper != null)
		{
			mUiHelper.onPause();
		}
	}

	@Override
	public void onDestroy()
	{
		if (mUiHelper != null)
		{
			mUiHelper.onDestroy();
		}
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
		if (mUiHelper != null)
		{
			mUiHelper.onSaveInstanceState(outState);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mUiHelper != null)
		{
			mUiHelper.onActivityResult(requestCode, resultCode, data);
		}
		if (requestCode == REQUEST_CODE_FACEBOOK_CANCEL && resultCode != -1)
		{
			onPostExecute();
		}
	}

	@Override
	public void onCreate(Bundle bundle, Activity activity)
	{
		mUiHelper = new UiLifecycleHelper(activity, this);
		mUiHelper.onCreate(bundle);
	}

	/**
	 * print to log facebook key hash
	 * 
	 * @param context
	 * @param tag
	 */
	private void printFacebookKeyHash(Context context, final String tag)
	{
		if (context != null)
		{
			try
			{
				PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures)
				{
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					printLog(tag, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			}
			catch (NameNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
		}
	}
}

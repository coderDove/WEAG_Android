package com.sharesociallibrary.app.socials.gplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.sharesociallibrary.app.interfaces.IShareMessageListener;
import com.sharesociallibrary.app.interfaces.ISocialCompleteRunListener;
import com.sharesociallibrary.app.socials.BaseAbstractManager;
import com.sharesociallibrary.app.utils.SocialType;

/**
 * Class manager of Google Plus social network
 * 
 * @author Viacheslav.Titov
 * 
 */
public final class GooglePlusManager extends BaseAbstractManager implements ConnectionCallbacks, OnConnectionFailedListener
{

	private static final int GOOGLE_PLUS_REQUEST_CODE = 100;
	private static final int GOOGLE_PLUS_RESULT_VALID_CODE = 0;
	private static final int GOOGLE_PLUS_RESULT_INVALID_CODE = -1;
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String SHARE_TYPE = "text/plain";

	private ISocialCompleteRunListener mSocialCompleteRunListener;
	private Activity mAcitivity;
	private PlusClient mPlusClient;

	public GooglePlusManager(Activity activity)
	{
		mAcitivity = activity;
	}

	@Override
	public void sendMessage(String message, final IShareMessageListener listener)
	{
		PlusShare.Builder builder = new PlusShare.Builder(mAcitivity);
		builder.setType(SHARE_TYPE);
		builder.setText(message).getIntent();
		Intent shareIntent = builder.getIntent();
		mAcitivity.startActivityForResult(shareIntent, GOOGLE_PLUS_REQUEST_CODE);
	}

	@Override
	public void setSocialCompleteRunListener(ISocialCompleteRunListener listener)
	{
		mSocialCompleteRunListener = listener;
	}

	@Override
	public void onPostExecute()
	{
		mSocialCompleteRunListener.onSocialCompleteRun(SocialType.GOOGLE_PLUS);
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
		if (mPlusClient != null)
		{
			mPlusClient.connect();
		}
	}

	@Override
	public void onStop()
	{
		if (mPlusClient != null)
		{
			mPlusClient.disconnect();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == Activity.RESULT_OK)
		{
			mPlusClient.connect();
			return;
		}
		if (GOOGLE_PLUS_REQUEST_CODE == requestCode)
		{
			// switch (resultCode)
			// {
			// case GooglePlusManager.GOOGLE_PLUS_RESULT_VALID_CODE:
			//
			// break;
			// case GooglePlusManager.GOOGLE_PLUS_RESULT_INVALID_CODE:
			// default:
			//
			// break;
			// }
			onPostExecute();
		}
	}

	@Override
	public void onCreate(Bundle bundle, Activity activity)
	{
		mPlusClient = new PlusClient.Builder(activity, this, this).build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{

	}

	@Override
	public void onConnected(Bundle bundle)
	{

	}

	@Override
	public void onDisconnected()
	{

	}

}

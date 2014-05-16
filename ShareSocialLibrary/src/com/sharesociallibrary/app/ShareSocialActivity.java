package com.sharesociallibrary.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sharesociallibrary.app.ShareSocialManager.Builder;
import com.sharesociallibrary.app.interfaces.IShareMessageListener;
import com.sharesociallibrary.app.utils.SocialType;
import com.sharesociallibrary.app.utils.Utils;
import com.sharesociallibrary.app.utils.exception.SocialException;

public class ShareSocialActivity extends FragmentActivity implements IShareMessageListener
{

	private ShareProgressDialog mProgressBar;
	private ShareSocialManager mShareSocialManager;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		init();
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onCreate(bundle, this);
		}
	}

	private void init()
	{
		if (getIntent() != null && getIntent().getExtras() != null)
		{
			Builder builder = getIntent().getExtras().getParcelable(ShareSocialManager.INTENT_BUILDER);
			if (builder != null)
			{
				mShareSocialManager = builder.create(this, this);
				mShareSocialManager.execute();
			}
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onResume();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onPause();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onDestroy();
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onStart();
		}

	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onStop();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onSaveInstanceState(outState);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mShareSocialManager != null)
		{
			mShareSocialManager.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onShareComplete(SocialType social, String postId)
	{
		dismissProgressDialog();
		if (social == SocialType.TWITTER)
		{
			removeTwitterSettings();
		}
	}

	@Override
	public void onShareError(final SocialType social, SocialException error)
	{
		Utils.printError(social.name(), error.getMessage());
		dismissProgressDialog();
	}

	@Override
	public void onFinished()
	{
		dismissProgressDialog();
		finish();
	}

	@SuppressLint("ValidFragment")
	private static final class ShareProgressDialog extends DialogFragment
	{

		private Context mContext;
		private String mMessage;

		public ShareProgressDialog(Context context, int resMessage)
		{
			super();
			mContext = context;
			mMessage = context.getString(resMessage);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			final ProgressDialog dialog = new ProgressDialog(mContext);
			dialog.setMessage(mMessage);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setOnKeyListener(new OnKeyListener()
			{

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
				{
					if (event.getAction() == KeyEvent.KEYCODE_BACK)
					{
						return false;
					}
					return true;
				}
			});
			return dialog;
		}

		@Override
		public void show(FragmentManager manager, String tag)
		{
			try
			{
				super.show(manager, tag);
			}
			catch (IllegalStateException e)
			{
			}
		}

	}

	@Override
	public void onShareRun(SocialType social)
	{
		dismissProgressDialog();
		if (social == SocialType.TWITTER)
		{
			setTwitterSettings();
		}
	}

	public void setTwitterSettings()
	{
		if (mShareSocialManager != null)
		{
			setContentView(R.layout.twitter_layout);
			WebView webView = (WebView) findViewById(R.id.webView);
			mShareSocialManager.setTwitterWebView(webView, this);
		}
	}

	public void removeTwitterSettings()
	{
		WebView webView = (WebView) findViewById(R.id.webView);
		ViewGroup vg = (ViewGroup) findViewById(android.R.id.content).getRootView();
		if (webView != null && vg != null)
		{
			vg.removeView(webView);
		}
	}

	@Override
	public void showProgressDialog(SocialType social)
	{
		switch (social)
		{
			case FACEBOOK:
				mProgressBar = new ShareProgressDialog(ShareSocialActivity.this, R.string.share_facebook);
				break;
			case TWITTER:
				mProgressBar = new ShareProgressDialog(ShareSocialActivity.this, R.string.share_twitter);
				break;
			default:
				dismissProgressDialog();
				break;
		}
		if (mProgressBar != null)
		{
			mProgressBar.show(getSupportFragmentManager(), null);
		}
	}

	@Override
	public void dismissProgressDialog()
	{
		if (mProgressBar != null)
		{
			try
			{
				mProgressBar.dismiss();
				mProgressBar = null;
			}
			catch (IllegalStateException e)
			{
				e.printStackTrace();
			}
		}
	}

}

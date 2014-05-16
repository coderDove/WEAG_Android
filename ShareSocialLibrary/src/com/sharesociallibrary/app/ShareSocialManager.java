package com.sharesociallibrary.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

import com.sharesociallibrary.app.interfaces.IManagerLifeCycle;
import com.sharesociallibrary.app.interfaces.IShareMessageListener;
import com.sharesociallibrary.app.interfaces.ISocialCompleteRunListener;
import com.sharesociallibrary.app.socials.facebook.FacebookManager;
import com.sharesociallibrary.app.socials.gplus.GooglePlusManager;
import com.sharesociallibrary.app.socials.twitter.TwitterManager;
import com.sharesociallibrary.app.utils.SocialType;

/**
 * General manager. It include all social network managers and control them
 * 
 * @author Viacheslav.Titov
 * 
 */
public final class ShareSocialManager implements IManagerLifeCycle
{

	public static final String INTENT_BUILDER = "builder";

	private Builder mBuilder;
	private IShareMessageListener mShareMessageListener;

	private FacebookManager mFacebook;
	private GooglePlusManager mGooglePlus;
	private TwitterManager mTwitter;

	public ShareSocialManager(Builder builder)
	{
		mBuilder = builder;
	}

	public ShareSocialManager(Activity activity, IShareMessageListener shareMessageListener, Builder builder)
	{
		this(builder);
		initSocials(activity);
		mShareMessageListener = shareMessageListener;
		mFacebook.setSocialCompleteRunListener(mSocialCompleteRunListener);
		mGooglePlus.setSocialCompleteRunListener(mSocialCompleteRunListener);
		mTwitter.setSocialCompleteRunListener(mSocialCompleteRunListener);
	}

	/**
	 * initialize managers of socials
	 * 
	 * @param activity
	 */
	private void initSocials(Activity activity)
	{
		mFacebook = new FacebookManager(activity);
		mGooglePlus = new GooglePlusManager(activity);
		mTwitter = new TwitterManager(activity, mBuilder.getTwitterApiKey(), mBuilder.getTwitterApiSecret(), mBuilder.getTwitterCallbackUrl());
	}

	/**
	 * start new activity for sharing
	 * 
	 * @param activity
	 */
	private void startShareActivity(Activity activity)
	{
		Intent intent = new Intent(activity, ShareSocialActivity.class);
		intent.putExtra(INTENT_BUILDER, mBuilder);
		activity.startActivity(intent);
	}

	/**
	 * Class configurator for sharing
	 * 
	 * @author Viacheslav.Titov
	 * 
	 */
	public static final class Builder implements Parcelable
	{
		private List<SocialType> mSocials;
		private String mShareMessage;
		private String mTwitterApiSecret;
		private String mTwitterApiKey;
		private String mTwitterCallbackUrl;

		public Builder()
		{
			mSocials = new ArrayList<SocialType>();
		}

		public ShareSocialManager create()
		{
			return new ShareSocialManager(this);
		}

		public ShareSocialManager create(Activity activity, IShareMessageListener shareMessageListener)
		{
			return new ShareSocialManager(activity, shareMessageListener, this);
		}

		public void setSocials(List<SocialType> socials)
		{
			this.mSocials = socials;
		}

		public List<SocialType> getSocials()
		{
			return mSocials;
		}

		public void addSocial(SocialType social)
		{
			mSocials.add(social);
		}

		public String getShareMessage()
		{
			return mShareMessage;
		}

		public void setShareMessage(final String message)
		{
			this.mShareMessage = message;
		}

		public String getTwitterApiSecret()
		{
			return mTwitterApiSecret;
		}

		public void setTwitterApiSecret(String apiSecret)
		{
			this.mTwitterApiSecret = apiSecret;
		}

		public String getTwitterApiKey()
		{
			return mTwitterApiKey;
		}

		public void setTwitterApiKey(String apiKey)
		{
			this.mTwitterApiKey = apiKey;
		}

		public String getTwitterCallbackUrl()
		{
			return mTwitterCallbackUrl;
		}

		public void setTwitterCallbackUrl(String mTwitterCallbackUrl)
		{
			this.mTwitterCallbackUrl = mTwitterCallbackUrl;
		}

		protected Builder(Parcel in)
		{
			if (in.readByte() == 0x01)
			{
				mSocials = new ArrayList<SocialType>();
				in.readList(mSocials, SocialType.class.getClassLoader());
			}
			else
			{
				mSocials = null;
			}
			mShareMessage = in.readString();
			mTwitterApiSecret = in.readString();
			mTwitterApiKey = in.readString();
			mTwitterCallbackUrl = in.readString();
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			if (mSocials == null)
			{
				dest.writeByte((byte) (0x00));
			}
			else
			{
				dest.writeByte((byte) (0x01));
				dest.writeList(mSocials);
			}
			dest.writeString(mShareMessage);
			dest.writeString(mTwitterApiSecret);
			dest.writeString(mTwitterApiKey);
			dest.writeString(mTwitterCallbackUrl);
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<Builder> CREATOR = new Parcelable.Creator<Builder>()
		{
			@Override
			public Builder createFromParcel(Parcel in)
			{
				return new Builder(in);
			}

			@Override
			public Builder[] newArray(int size)
			{
				return new Builder[size];
			}
		};
	}

	/**
	 * start new activity and run share message to wall in social network
	 * 
	 * @param activity
	 */
	public void share(Activity activity)
	{
		startShareActivity(activity);
	}

	/**
	 * run share
	 */
	public void execute()
	{
		List<SocialType> socials = mBuilder.getSocials();
		if (socials != null && !socials.isEmpty())
		{
			SocialType s = socials.get(0);
			removeSocialFromList(s);
			switch (s)
			{
				case FACEBOOK:
					mFacebook.sendMessage(getShareMessage(), mShareMessageListener);
					break;
				case GOOGLE_PLUS:
					mGooglePlus.sendMessage(getShareMessage(), mShareMessageListener);
					break;
				case TWITTER:
					mTwitter.sendMessage(getShareMessage(), mShareMessageListener);
					break;
			}
		}
		else
		{
			mShareMessageListener.onFinished();
		}
	}

	public String getShareMessage()
	{
		return mBuilder.getShareMessage();
	}

	/**
	 * Remove social network from list of socials
	 * 
	 * @param social
	 */
	private void removeSocialFromList(SocialType social)
	{
		mShareMessageListener.onShareRun(social);
		mBuilder.getSocials().remove(social);
	}

	/**
	 * 
	 * @param social
	 */
	public void completeSocial(SocialType social)
	{
		mSocialCompleteRunListener.onSocialCompleteRun(social);
	}

	private ISocialCompleteRunListener mSocialCompleteRunListener = new ISocialCompleteRunListener()
	{

		@Override
		public void onSocialCompleteRun(SocialType social)
		{
			execute();
		}
	};

	/**
	 * set WebView for twitter
	 * 
	 * @param webView
	 * @param activity
	 */
	public void setTwitterWebView(WebView webView, Activity activity)
	{
		mTwitter.setWebView(webView);
	}

	@Override
	public void onResume()
	{
		mFacebook.onResume();
		mGooglePlus.onResume();
		mTwitter.onResume();
	}

	@Override
	public void onPause()
	{
		mFacebook.onPause();
		mGooglePlus.onPause();
		mTwitter.onPause();
	}

	@Override
	public void onDestroy()
	{
		mFacebook.onDestroy();
		mGooglePlus.onDestroy();
		mTwitter.onDestroy();
	}

	@Override
	public void onStart()
	{
		mFacebook.onStart();
		mGooglePlus.onStart();
		mTwitter.onStart();
	}

	@Override
	public void onStop()
	{
		mFacebook.onStop();
		mGooglePlus.onStop();
		mTwitter.onStop();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		mFacebook.onSaveInstanceState(outState);
		mGooglePlus.onSaveInstanceState(outState);
		mTwitter.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mFacebook.onActivityResult(requestCode, resultCode, data);
		mGooglePlus.onActivityResult(requestCode, resultCode, data);
		mTwitter.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreate(Bundle bundle, Activity activity)
	{
		mFacebook.onCreate(bundle, activity);
		mGooglePlus.onCreate(bundle, activity);
		mTwitter.onCreate(bundle, activity);
	}
}

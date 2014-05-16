package com.example.sharesocialexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sharesociallibrary.app.ShareSocialManager;
import com.sharesociallibrary.app.ShareSocialManager.Builder;
import com.sharesociallibrary.app.utils.SocialType;

public class MainActivity extends FragmentActivity
{

	private static final String TAG = MainActivity.class.getSimpleName();

	private EditText mShareText;
	private CheckBox mCbFacebook;
	private CheckBox mCbTwitter;
	private CheckBox mCbGooglePlus;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initControls();
	}

	private void initControls()
	{
		Button btn = (Button) findViewById(R.id.btnShare);
		btn.setOnClickListener(mOnClickListener);
		mShareText = (EditText) findViewById(R.id.etShareText);
		mCbFacebook = (CheckBox) findViewById(R.id.cbFacebook);
		mCbTwitter = (CheckBox) findViewById(R.id.cbTwitter);
		mCbGooglePlus = (CheckBox) findViewById(R.id.cbGooglePlus);
	}

	private OnClickListener mOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mShareText.getWindowToken(), 0);
			final String shareMessage = mShareText.getText().toString();
			Builder builder = new ShareSocialManager.Builder();
			builder.setShareMessage(shareMessage);
			if (mCbFacebook.isChecked())
			{
				builder.addSocial(SocialType.FACEBOOK);
			}
			if (mCbTwitter.isChecked())
			{
				builder.addSocial(SocialType.TWITTER);
			}
			if (mCbGooglePlus.isChecked())
			{
				builder.addSocial(SocialType.GOOGLE_PLUS);
			}
			ShareSocialManager.getInstance().setBuilder(builder);
			ShareSocialManager.getInstance().share(MainActivity.this);
		}
	};

}

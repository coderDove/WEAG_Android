package com.weatheraggregator.activity;

import android.content.Intent;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.sharesociallibrary.app.ShareSocialManager;
import com.sharesociallibrary.app.ShareSocialManager.Builder;
import com.sharesociallibrary.app.utils.SocialType;
import com.weatheraggregator.app.R;
import com.weatheraggregator.util.Util;

@EActivity(R.layout.share_activity)
public class ShareActivity extends SherlockFragmentActivity {

    public static final String SHARE_MESSAGE = "share_message";
    public static final String F_SOCIAL_TYPE = "social_type";

    private SocialType mSocialType;

    @ViewById(R.id.etShareText)
    protected EditText mShareText;

    private void initControls() {
	styleActionBar();
    }

    @AfterViews
    protected void init() {
	initControls();
	initData();
    }

    private void initData() {
	String message = getIntent().getStringExtra(SHARE_MESSAGE);
	setShareText(message);
	mSocialType = getSocialTypeFromBundle();
	setTitleActionBar(mSocialType);
    }

    private void setShareText(String message) {
	mShareText.setText(message);
	if (message != null) {
	    final int messageLength = message.length();
	    mShareText.setSelection(messageLength);
	}
    }

    private void setTitleActionBar(SocialType type) {
	ActionBar ab = getSupportActionBar();
	switch (type) {
	case FACEBOOK:
	    ab.setTitle(R.string.facebook);
	    break;
	case TWITTER:
	    ab.setTitle(R.string.twitter);
	    break;
	case GOOGLE_PLUS:
	    ab.setTitle(R.string.google_plus);
	    break;
	}
    }

    private void styleActionBar() {
	ActionBar ab = getSupportActionBar();
	ab.setDisplayShowHomeEnabled(false);
    }

    private SocialType getSocialTypeFromBundle() {
	Intent intent = getIntent();
	SocialType type = null;
	if (intent != null) {
	    type = (SocialType) intent.getSerializableExtra(F_SOCIAL_TYPE);
	}
	return type;
    }

    @Click(R.id.btnShare)
    protected void share() {
	Util.closeSoftKeyboard(mShareText, getApplicationContext());
	final String shareMessage = mShareText.getText().toString();
	Builder builder = new ShareSocialManager.Builder();
	builder.setShareMessage(shareMessage);
	builder.addSocial(mSocialType);
	ShareSocialManager shareSocialManager = builder.create();
	shareSocialManager.share(ShareActivity.this);
    }

}

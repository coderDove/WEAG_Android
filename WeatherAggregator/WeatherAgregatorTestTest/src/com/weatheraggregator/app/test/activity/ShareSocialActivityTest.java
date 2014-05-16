package com.weatheraggregator.app.test.activity;

import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.sharesociallibrary.app.ShareSocialActivity;
import com.sharesociallibrary.app.ShareSocialManager;
import com.sharesociallibrary.app.ShareSocialManager.Builder;
import com.sharesociallibrary.app.utils.SocialType;

public class ShareSocialActivityTest extends
	ActivityInstrumentationTestCase2<ShareSocialActivity> {

    private static final String TAG = ShareSocialActivityTest.class
	    .getSimpleName();

    private ShareSocialActivity mActivity;

    public ShareSocialActivityTest() {
	super(ShareSocialActivity.class);
    }

    public ShareSocialActivityTest(Class<ShareSocialActivity> activityClass) {
	super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	// setActivityIntent(getIntentInvalid());
	mActivity = getActivity();
	MockitoAnnotations.initMocks(this);
	Log.d(TAG, "setUp()");
    }

    @Override
    protected void tearDown() throws Exception {
	Log.d(TAG, "tearDown()");
	super.tearDown();
    }

    // @SmallTest
    // public void testIntentNull()
    // {
    // setActivityIntent(getIntentNull());
    // }

    private Intent getIntentNull() {
	Intent intent = null;
	return intent;
    }

    private Intent getIntentValid() {
	Builder builder = new ShareSocialManager.Builder();
	builder.setShareMessage("Hello world!");
	builder.addSocial(SocialType.FACEBOOK);
	builder.addSocial(SocialType.TWITTER);
	builder.setTwitterApiKey("QRpAbBYi2ERIumhMBSMCOg");
	builder.setTwitterApiSecret("BCiqO1I00IVDg1jxURGOoaAFzEvSCxNIklj0frJGj0");
	builder.setTwitterCallbackUrl("http://weather.aggregator.callback.com");
	builder.addSocial(SocialType.GOOGLE_PLUS);

	Intent intent = new Intent();
	intent.putExtra(ShareSocialManager.INTENT_BUILDER, builder);
	return intent;
    }

    private Intent getIntentInvalid() {
	Builder builder = new ShareSocialManager.Builder();
	builder.setShareMessage(null);
	builder.setTwitterApiKey(null);
	builder.setTwitterApiSecret(null);
	builder.setTwitterCallbackUrl(null);

	Intent intent = new Intent();
	intent.putExtra(ShareSocialManager.INTENT_BUILDER, builder);
	return intent;
    }

    @MediumTest
    public void testActivityIntentValidData() {
	setActivityIntent(getIntentValid());
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	getActivity().finish();
    }

    @MediumTest
    public void testActivityIntentInValidData() {
	setActivityIntent(getIntentInvalid());
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	getActivity().finish();
    }

    @MediumTest
    public void testActivityIntentNull() {
	setActivityIntent(getIntentNull());
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	getActivity().finish();
    }

    // @SmallTest
    private void testWebView() {
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		mActivity.setTwitterSettings();
	    }
	});
	// TODO: WebView doesn't have time to initialize
	Espresso.onView(
		ViewMatchers.withId(com.sharesociallibrary.app.R.id.webView))
		.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		mActivity.removeTwitterSettings();
	    }
	});
    }

    @SmallTest
    public void testProgressBar() {
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		mActivity.showProgressDialog(SocialType.FACEBOOK);
	    }
	});
	Espresso.onView(
		ViewMatchers
			.withText(com.sharesociallibrary.app.R.string.share_facebook))
		.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	getInstrumentation().runOnMainSync(new Runnable() {

	    @Override
	    public void run() {
		mActivity.dismissProgressDialog();
	    }
	});
    }
}

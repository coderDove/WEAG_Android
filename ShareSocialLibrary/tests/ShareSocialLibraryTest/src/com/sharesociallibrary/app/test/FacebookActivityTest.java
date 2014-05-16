package com.sharesociallibrary.app.test;

import org.mockito.MockitoAnnotations;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import com.sharesociallibrary.app.ShareSocialActivity;

public class FacebookActivityTest extends ActivityInstrumentationTestCase2<ShareSocialActivity>
{

	private static final String TAG = FacebookActivityTest.class.getSimpleName();

	public FacebookActivityTest()
	{
		super(ShareSocialActivity.class);
	}

	public FacebookActivityTest(Class<ShareSocialActivity> activityClass)
	{
		super(activityClass);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		getActivity();
		MockitoAnnotations.initMocks(this);
		Log.d(TAG, "setUp()");
	}

	@Override
	protected void tearDown() throws Exception
	{
		Log.d(TAG, "tearDown()");
		super.tearDown();
	}

	@MediumTest
	public void testSendMessageFacebook()
	{
//		FacebookManager.getInstance().sendMessage(getActivity(), "");
	}

}

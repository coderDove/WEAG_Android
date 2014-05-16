package com.weatheraggregator.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;

@EActivity(R.layout.about_layout)
public class AboutActivity extends FragmentActivity {

    private static final String ASSET_ABOUT = "file:///android_asset/about.html";
    private static final String TYPE = "text/plain";
    private static final String SUBJECT = "subject of email";
    private static final String TEXT = "body of email";
    private static final String EMAIL = "example@example.com";

    @ViewById(R.id.webView)
    protected WebView mWebView;

    @AfterViews
    protected void initView() {
	// mWebView.loadUrl(ASSET_ABOUT);
	mWebView.setBackgroundColor(0x00000000);
    }

    @Click(R.id.btnContactUs)
    protected void onClick(View view) {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType(TYPE);
	intent.putExtra(Intent.EXTRA_EMAIL, EMAIL);
	intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
	intent.putExtra(Intent.EXTRA_TEXT, TEXT);
	try {
	    startActivity(intent);
	} catch (android.content.ActivityNotFoundException ex) {
	    Toast.makeText(AboutActivity.this,
		    "There are no email clients installed.", Toast.LENGTH_SHORT)
		    .show();
	}
    }

}

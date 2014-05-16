package com.weatheraggregator.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import com.sharesociallibrary.app.utils.SocialType;
import com.weatheraggregator.app.R;

public class ShareSocialAdapter extends BaseAdapter implements SpinnerAdapter {

    private final List<SocialType> mSocials = Arrays.asList(
	    SocialType.FACEBOOK, SocialType.TWITTER, SocialType.GOOGLE_PLUS);
    private Context mContext;

    public ShareSocialAdapter(Context context) {
	mContext = context;
    }

    @Override
    public int getCount() {
	return mSocials.size();
    }

    @Override
    public SocialType getItem(int position) {
	return mSocials.get(position);
    }

    @Override
    public long getItemId(int position) {
	return 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	ImageView iv = null;
	if (convertView == null) {
	    iv = (ImageView) View.inflate(mContext,
		    R.layout.social_spinner_layout, null);
	} else {
	    iv = (ImageView) convertView;
	}
	switch (getItem(position)) {
	case FACEBOOK:
	    iv.setImageResource(R.drawable.facebook);
	    iv.setContentDescription(mContext.getString(R.string.facebook));
	    break;
	case GOOGLE_PLUS:
	    iv.setImageResource(R.drawable.google_plus);
	    iv.setContentDescription(mContext.getString(R.string.google_plus));
	    break;
	case TWITTER:
	    iv.setImageResource(R.drawable.twitter);
	    iv.setContentDescription(mContext.getString(R.string.twitter));
	    break;
	}
	return iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ImageView iv = null;
	if (convertView == null) {
	    iv = (ImageView) View.inflate(mContext,
		    R.layout.social_spinner_layout, null);
	} else {
	    iv = (ImageView) convertView;
	}
	iv.setImageResource(R.drawable.share);
	return iv;
    }

}

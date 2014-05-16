package com.weatheraggregator.view;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup
public abstract class WeatherBaseItem extends RelativeLayout {
    @ViewById
    protected ImageView imgRating, imgCloud, imgServiceLogo;
    @ViewById
    protected TextView tvTempValue, tvRatingIndex, tvServiceName;

    public WeatherBaseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWeatherTypeImageFromRes(int resId) {
        imgCloud.setImageResource(resId);
    }

    public void setWeatherTypeImage(Bitmap image) {
        imgCloud.setImageBitmap(image);
    }

    public void setServiceLogoFromRes(int resId) {
        imgServiceLogo.setImageResource(resId);
    }

    public void setServiceLogoFromRes(Bitmap image) {
        imgServiceLogo.setImageBitmap(image);
    }

    public void setServiceRatingFromRes(int resId) {
        imgRating.setImageResource(resId);
    }

    public void setServiceRatingFromRes(Bitmap image) {
        imgRating.setImageBitmap(image);
    }

    public void setTempValue(String temp) {
        tvTempValue.setText(temp);
    }

    public void setServiceName(String name) {
        tvServiceName.setText(name);
    }
}

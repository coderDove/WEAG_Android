package com.googlecode.android.widgets.DateSlider.timeview;

import java.util.Date;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.android.widgets.DateSlider.R;
import com.googlecode.android.widgets.DateSlider.TimeObject;
import com.googlecode.android.widgets.DateSlider.WeatherObject;

public class WeatherDateView extends LinearLayout implements TimeView {
    private TextView tvDate;
    private ImageView ivWeather;
    private TextView tvTemp;
    private LinearLayout llWeather;
    protected long endTime, startTime;
    protected int imgResId;
    protected int temp;
    protected String text;
    private LinearLayout llPanel;
    protected boolean isCenter = false, isOutOfBounds = false;

    public WeatherDateView(Context context, boolean isCenterView, int topTextSize, int bottomTextSize, float lineHeight, int imageSize) {
        super(context);
        initView(isCenterView, topTextSize, bottomTextSize, lineHeight, imageSize);
    }

    private void initView(boolean isCenterView, int topTextSize, int bottomTextSize, float lineHeight, int imageSize) {
        View rootView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.date_weather_item, this);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        ivWeather = (ImageView) rootView.findViewById(R.id.ivWeather);
        android.view.ViewGroup.LayoutParams ivParams = ivWeather.getLayoutParams();
        ivParams.height = imageSize;
        ivParams.width = imageSize;
        ivWeather.setLayoutParams(ivParams);
        tvTemp = (TextView) rootView.findViewById(R.id.tvTemp);
        llWeather = (LinearLayout) rootView.findViewById(R.id.llWeather);
        llPanel = (LinearLayout) rootView.findViewById(R.id.llPanel);
        //
        tvDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, topTextSize);
        tvTemp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, topTextSize);
        if (isCenterView) {
            isCenter = isCenterView;       
            WeatherViewHelper.selectViewState(llPanel, tvTemp, tvDate);
//            tvDate.setTypeface(Typeface.DEFAULT_BOLD);
//            tvDate.setTextColor(0xFF333333);
//            tvDate.setTypeface(Typeface.DEFAULT_BOLD);
//            tvDate.setTextColor(0xFF444444);
            // tvTemp.setPadding(0, 5 - (int) (topTextSize / 15.0), 0, 0);
        } else {
            WeatherViewHelper.devaultViewState(llPanel, tvTemp, tvDate);
            // / tvTemp.setPadding(0, 5, 0, 0);
//            tvDate.setTextColor(0xFF666666);
//            tvDate.setTextColor(0xFF666666);
        }
    }

    private void renderView(WeatherObject appData) {
        if (appData != null) {
            Integer temp = appData.temp;
            if (temp != null) {
                tvTemp.setText(String.valueOf(appData.temp));
            } else {
                tvTemp.setText("N/A");
            }
            // ivWeather.setImageBitmap(appData.imgWeather);
        } else {
            tvTemp.setText("N/A");
            ivWeather.setImageBitmap(null);
        }

    }

    public LinearLayout getLlWeather() {
        return llWeather;
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public ImageView getIvWeathe() {
        return ivWeather;
    }

    public TextView getTvTemp() {
        return tvTemp;
    }

    @Override
    public void setVals(TimeObject to, boolean isShowMoreInfo, IRenderWeatherCalenderView l) {
        this.startTime = to.startTime;
        this.endTime = to.endTime;
        this.text = to.text.toString();
        tvDate.setText(to.text);
        if (l != null) {
            WeatherObject appData = l.renderCalendarItem(new Date(to.endTime), ivWeather);
            renderView(appData);
        }
        // else
        // {
        // throw new
        // RuntimeException("Context not implement IRenderWeatherCalenderView interface");
        // }
        this.invalidate();
        if (!isShowMoreInfo) {
            android.view.ViewGroup.LayoutParams ivParams = llWeather.getLayoutParams();
            ivParams.height = 1;
            llWeather.setLayoutParams(ivParams);
        }

    }

    @Override
    public void setVals(TimeView other, boolean isShowMoreInfo, IRenderWeatherCalenderView l) {
        text = other.getTimeText().toString();
        tvDate.setText(other.getTimeText());
        startTime = other.getStartTime();
        endTime = other.getEndTime();

        if (l != null) {
            WeatherObject appData = l.renderCalendarItem(new Date(endTime), ivWeather);
            renderView(appData);
        }
        // else
        // {
        // throw new
        // RuntimeException("Context not implement IRenderWeatherCalenderView interface");
        // }
        if (!isShowMoreInfo) {
            android.view.ViewGroup.LayoutParams ivParams = llWeather.getLayoutParams();
            ivParams.height = 0;
            llWeather.setLayoutParams(ivParams);
        }
    }

    @Override
    public String getTimeText() {
        return text;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public boolean isOutOfBounds() {
        return isOutOfBounds;
    }

    @Override
    public void setOutOfBounds(boolean outOfBounds) {
        if (outOfBounds && !isOutOfBounds) {
            tvDate.setTextColor(0x44666666);
        } else if (!outOfBounds && isOutOfBounds) {
            tvDate.setTextColor(0xFF666666);
        }
        isOutOfBounds = outOfBounds;
    }
}

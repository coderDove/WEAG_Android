package com.googlecode.android.widgets.DateSlider.timeview;

import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherViewHelper {

    public static final void devaultViewState(LinearLayout llPanel, TextView tvTemp,TextView tvDate){
	tvDate.setTextColor(Color.WHITE);
	tvTemp.setTextColor(Color.WHITE);
//	tvDate.setBackgroundColor(0xDD888888);
//	tvTemp.setBackgroundColor(0xDD888888);
	//llPanel.setBackgroundColor(Color.BLACK);	
    }
    
    public static final void selectViewState(LinearLayout llPanel, TextView tvTemp,TextView tvDate){
	tvDate.setTextColor(Color.BLACK);
	tvTemp.setTextColor(Color.BLACK);
//	tvDate.setBackgroundColor(Color.WHITE);
//	tvTemp.setBackgroundColor(Color.WHITE);
	//llPanel.setBackgroundColor(Color.WHITE);	
    }
    
}

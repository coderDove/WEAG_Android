package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.WeatherService;

@EViewGroup(R.layout.service_setting_item)
public class ServicesSettingItem extends RelativeLayout {
    @ViewById
    protected TextView tvServiceName, tvFullName;
    @ViewById(R.id.imgLogo)
    protected ImageView imgLogo;
    @ViewById
    protected CheckBox cbFavorite;

    @ViewById(R.id.tbUse)
    protected ToggleButton tbUseService;

    public ServicesSettingItem(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public void setServiceName(String name) {
	tvServiceName.setText(name);
    }

    public void setDescription(String description) {
	tvFullName.setText(description);
    }

    public ImageView getLogo() {
	return imgLogo;
    }

    @AfterViews
    void onAfterInitView() {

    }

    public void setCheckedListener(OnCheckedChangeListener checkListener) {
	cbFavorite.setOnCheckedChangeListener(checkListener);
	tbUseService.setOnCheckedChangeListener(checkListener);
    }

    public void setUsedService(WeatherService service) {
	tbUseService.setTag(service);
	Boolean isDelete = service.isDelete();
	if (isDelete != null) {
	    tbUseService.setChecked(!service.isDelete());
	} else {
	    tbUseService.setChecked(true);
	}
    }

    public void setFavorite(WeatherService service) {
	cbFavorite.setTag(service);
	Boolean isFavorite = service.isFavorite();
	if (isFavorite != null) {
	    cbFavorite.setChecked(isFavorite.booleanValue());
	} else {
	    cbFavorite.setChecked(false);
	}
    }
}

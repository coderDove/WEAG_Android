package com.weatheraggregator.fragment;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.mobeta.android.dslv.DragSortListView;
import com.weatheraggregator.activity.BaseActivity;
import com.weatheraggregator.adapter.ServiceSettingAdapter;
import com.weatheraggregator.app.R;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.events.EventCloseSoftKeyBoard;
import com.weatheraggregator.events.EventServiceEdit;
import com.weatheraggregator.events.EventServiceEdit.EditType;
import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.util.ForecastInfoManager;
import com.weatheraggregator.webservice.exception.ErrorType;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.settings_service_list_fragment)
public class WeatherServiceSettingFragment extends SettingBaseFragment {
    public static final String TAG = "WeatherServiceSettingFragment";
    private ServiceSettingAdapter mAdapter = null;
    private boolean isEdit = false;
    @ViewById(R.id.lvServices)
    protected DragSortListView mLvWeatherServices;
    @ViewById(R.id.tvEmpty)
    protected TextView mTvEmptyMessage;

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	EventBus.getDefault().register(WeatherServiceSettingFragment.this);
    }

    @Override
    public void onDetach() {
	EventBus.getDefault().unregister(WeatherServiceSettingFragment.this);
	super.onDetach();
    }

    public static WeatherServiceSettingFragment getNewInstance(Bundle bundle) {
	if (bundle == null) {
	    bundle = new Bundle();
	}
	WeatherServiceSettingFragment fragment = new WeatherServiceSettingFragment_();
	fragment.setArguments(bundle);
	return fragment;
    }

    @Override
    protected void initData() {
	initServices();
    }

    @UiThread
    protected void initServiceAdapter(List<WeatherService> services) {
	updateAdapter(services, mCheckListener);
    }

    public void updateAdapter(List<WeatherService> services,
	    OnCheckedChangeListener checkListener) {
	if (services != null && getActivity() != null) {
	    mAdapter = new ServiceSettingAdapter(getActivity(), services,
		    checkListener);
	    mLvWeatherServices.setAdapter(mAdapter);
	    
	    mTvEmptyMessage.setVisibility(View.GONE);
	    if (services.isEmpty())
		mTvEmptyMessage.setVisibility(View.VISIBLE);
	}
    }

    private void setFavoriteService(List<WeatherService> services) {
	if (getActivity() != null) {
	    WeatherService favorite = new Select()
		    .from(WeatherService.class)
		    .where(String
			    .format("%s = ?", WeatherService.F_IS_FAVORITE),
			    1).executeSingle();
	    if (favorite == null && services != null && services.size() > 0) {
		favorite = services.get(0);
		favorite.setFavorite(true);
		favorite.save();
	    }
	}
    }

    @Background
    protected void initServices() {
	if (getActivity() != null) {
	    List<WeatherService> services = new Select()
		    .from(WeatherService.class).orderBy(WeatherService.F_ORDER)
		    .execute();
	    setFavoriteService(services);
	    if (services != null) {
		initServiceAdapter(services);
		ForecastInfoManager.getInstance().loadDateWeatherInfo(
			new Date());
	    }
	}
    }

    @AfterViews
    protected void initView() {
	mLvWeatherServices.setDropListener(onDrop);
	mLvWeatherServices.setDragScrollProfile(ssProfile);
	mLvWeatherServices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	initData();
    }

    private synchronized void setFavouriteService(WeatherService item) {
	WeatherService oldFavoriteService = new Select()
		.from(WeatherService.class)
		.where(String.format("%s=?", WeatherService.F_IS_FAVORITE), 1)
		.executeSingle();
	ActiveAndroid.beginTransaction();
	try {
	    if (oldFavoriteService != null) {
		if (!item.getObjectId()
			.equals(oldFavoriteService.getObjectId())) {
		    oldFavoriteService.setFavorite(false);
		    oldFavoriteService.setSynchronized(false);
		    item.setFavorite(true);
		    item.setDelete(false);
		    item.setSynchronized(false);
		    item.save();
		    oldFavoriteService.save();
		    ((BaseActivity) getActivity()).getManager()
			    .setFavouriteService(item.getObjectId(),
				    oldFavoriteService.getObjectId(),
				    new IDataSourceServiceListener.Stub() {
					@Override
					public void callBack(int statusCode)
						throws RemoteException {
					    if (statusCode != ErrorType.HTTP_OK
						    .getCode()) {
						showToast(ErrorType.values()[statusCode]);
					    }
					}
				    });
		    initServices();
		}
		ActiveAndroid.setTransactionSuccessful();
	    }
	} finally {
	    ActiveAndroid.endTransaction();
	}
    }

    private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
	@Override
	public float getSpeed(float w, long t) {
	    if (w > 0.8f) {
		// Traverse all views in a millisecond
		return ((float) mAdapter.getCount()) / 0.001f;
	    } else {
		return 10.0f * w;
	    }
	}
    };

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
	@Override
	public void drop(int from, int to) {
	    WeatherService item = mAdapter.getItem(from);
	    mAdapter.remove(item);
	    mAdapter.insert(item, to);
	    mAdapter.notifyDataSetChanged();
	    reOrderServices();
	}
    };

    @Background
    protected void reOrderServices() {
	if (mAdapter != null && mAdapter.getCount() > 0) {
	    isEdit = true;
	    ActiveAndroid.beginTransaction();
	    try {
		for (int indexCity = 0; indexCity < mAdapter.getCount(); indexCity++) {
		    WeatherService service = mAdapter.getItem(indexCity);
		    service.setOrder(indexCity);
		    service.setSynchronized(false);
		    service.save();
		}
		ActiveAndroid.setTransactionSuccessful();
	    } finally {
		ActiveAndroid.endTransaction();
	    }
	}
    }

    public void onEventMainThread(EventServiceEdit event) {
	// if (event.getType() == EditType.FAVOURITE) {
	// if (getActivity() != null && mAdapter != null) {
	// setFavouriteService(event.getService());
	// }
	// }
	if (event.getType() == EditType.USE) {
	    event.getService().save();
	}
	isEdit = true;
    }

    @Override
    public void onSaveData() {
	if (isEdit) {
	    ((BaseActivity) getActivity()).getManager().reorderService(
		    new IDataSourceServiceListener.Stub() {
			@Override
			public void callBack(int statusCode)
				throws RemoteException {
			    showToast(ErrorType.values()[statusCode]);
			}
		    });
	}
    }

    @Override
    public void onEventMainThread(EventCloseSoftKeyBoard event) {
    }

    private OnCheckedChangeListener mCheckListener = new OnCheckedChangeListener() {
	@Override
	public void onCheckedChanged(CompoundButton buttonView,
		boolean isChecked) {
	    WeatherService service = (WeatherService) buttonView.getTag();
	    switch (buttonView.getId()) {
	    case R.id.cbFavorite:
		clickFavoriteService(buttonView, service, isChecked);
		break;
	    case R.id.tbUse:
		clickOnOffService(buttonView, service, isChecked);
		break;
	    default:
		break;
	    }
	}
    };

    /**
     * Need call when click on favorite button
     * 
     * @param buttonView
     *            - CheckBox (Star), favorite button
     * @param service
     *            - Favorite service
     * @param isChecked
     */
    private synchronized void clickFavoriteService(CompoundButton buttonView,
	    WeatherService service, boolean isChecked) {
	WeatherService oldFavoriteService = mAdapter.getOldFavoriteService();
	if (service.isFavorite() != null && isChecked != service.isFavorite()) {
	    if (oldFavoriteService != null
		    && !service.getObjectId().equals(
			    oldFavoriteService.getObjectId())) {
		setFavouriteService(service);
		isEdit = true;
		// EventBus.getDefault().post(
		// new EventServiceEdit(EditType.FAVOURITE, service));
	    }
	}
	if (oldFavoriteService != null
		&& service.getObjectId().equals(
			oldFavoriteService.getObjectId())) {
	    buttonView.setChecked(true);
	}
	oldFavoriteService = service;
	mAdapter.setOldFavoriteService(oldFavoriteService);
    }

    /**
     * Need call when click on switch ON/OFF service
     * 
     * @param buttonView
     *            - Toggle Button
     * @param service
     *            - WeatherService which click
     * @param isChecked
     */
    private void clickOnOffService(CompoundButton buttonView,
	    WeatherService service, boolean isChecked) {
	if (service.isDelete() != null
		&& !service.isDelete().booleanValue() != isChecked) {
	    if (service.isFavorite() != null
		    && service.isFavorite().booleanValue()) {
		buttonView.setChecked(true);
	    } else {
		service.setDelete(!isChecked);
		EventBus.getDefault().post(
			new EventServiceEdit(EditType.USE, service));
	    }
	}
    }
}

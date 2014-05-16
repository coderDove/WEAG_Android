package com.weatheraggregator.util;

import com.weatheraggregator.app.R;
import com.weatheraggregator.webservice.exception.ErrorType;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogValidationTextMessagesManager {

    private DialogManager mDialogManager;

    public synchronized static DialogValidationTextMessagesManager newInstance(
	    Context context) {
	DialogValidationTextMessagesManager sInstance = new DialogValidationTextMessagesManager(
		context);
	return sInstance;
    }

    public DialogValidationTextMessagesManager(Context context) {
	mDialogManager = DialogManager.getInstance(context);
    }

    public void makeAlert(ErrorType type, OnClickListener listener) {
	switch (type) {
	case HTTP_HOST_NOT_EVAILABLE:
	    makeAlertHost(listener);
	    break;
	case NO_INTERNET_CONNECTION:
	    makeAlertInternetConnection(listener);
	    break;
	case UNKNOW_ERROR:
	    makeAlertUncnowError(listener);
	    break;
	case USER_NOT_AUHTORISED:
	    makeAlertUserNotRegister(listener);
	    break;
	case INCORRECT_DATA:
	    // I know that it's method for display dialog
	    makeToastIncorrectData();
	    break;
	default:
	    break;
	}
    }

    public void makeToast(ErrorType type) {
	switch (type) {
	case INCORRECT_DATA:
	    makeToastIncorrectData();
	    break;
	case NO_INTERNET_CONNECTION:
	    makeInternetConnection();
	    break;
	case HTTP_HOST_NOT_EVAILABLE:
	    makeToastHost();
	    break;
	default:
	    break;
	}
    }

    private void makeToastHost() {
	mDialogManager.makeToast(R.string.toast_host_not_available);
    }

    private void makeInternetConnection() {
	mDialogManager.makeToast(R.string.toas_no_internet_connection);
    }

    private void makeToastIncorrectData() {
	mDialogManager.makeToast(R.string.toast_problem_load_data);
    }

    private void makeAlertHost(OnClickListener listener) {
	if (listener == null) {
//	    listener = new OnClickListener() {
//		@Override
//		public void onClick(DialogInterface paramDialogInterface,
//			int paramInt) {
//		    android.os.Process.killProcess(android.os.Process.myPid());
//		}
//	    };
	}
	mDialogManager.makeConfirm(R.string.dlg_host_not_available, listener,listener);
    }

    private void makeAlertUserNotRegister(OnClickListener listener) {
	mDialogManager.makeAlert(R.string.dlg_host_not_available, listener);
    }

    private void makeAlertUncnowError(OnClickListener listener) {
	mDialogManager.makeAlert(R.string.dlg_host_not_available, listener);
    }

    private void makeAlertInternetConnection(OnClickListener listener) {
	if (listener == null) {
	    listener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface paramDialogInterface,
			int which) {
		  //  android.os.Process.killProcess(android.os.Process.myPid());
		    // switch (which) {
		    // case DialogInterface.BUTTON_POSITIVE:
		    //
		    // break;
		    // case DialogInterface.BUTTON_NEGATIVE:
		    // break;
		    // default:
		    // break;
		    // }
		}
	    };
	}
	mDialogManager.makeConfirm(R.string.dlg_no_internet_connection,
		listener,listener);
    }

}

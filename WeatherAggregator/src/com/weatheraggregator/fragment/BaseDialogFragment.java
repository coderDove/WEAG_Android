package com.weatheraggregator.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class BaseDialogFragment extends DialogFragment {

    private AlertDialog mBaseDialog;

    public BaseDialogFragment() {
        super();
    }

    public static BaseDialogFragment newInstanceState() {
        return new BaseDialogFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    private AlertDialog createDialog() {
        if (mBaseDialog == null) {
            mBaseDialog = new AlertDialog.Builder(getActivity()).create();
        }
        return mBaseDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialog();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        try {
            return super.show(transaction, tag);
        } catch (IllegalStateException e) {
            Log.d(BaseDialogFragment.class.getSimpleName(), e.getMessage());
        }
        return -1;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            Log.d(BaseDialogFragment.class.getSimpleName(), e.getMessage());
        }
    }

    public AlertDialog getBaseDialog() {
        return createDialog();
    }

    public void setBaseDialog(AlertDialog baseDialog) {
        this.mBaseDialog = baseDialog;
    }

}

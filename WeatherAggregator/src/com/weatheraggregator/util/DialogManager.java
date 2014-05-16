package com.weatheraggregator.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DialogManager {
    /**
     * DialogManager version 1.0 Manage different dialogs and contexts by one
     * instance class
     * 
     * @author Allan Ariel Leite Menezes Santos
     * @email allan.ariel1987@gmail.com
     * @myProjectsInFacebook www.facebook.com/mnidersoft
     * @myProjectsInPlayStore https://play.google.com/store/search?q=mnidersoft
     */

    private final String WARNING = "Warning";
    private final String PLEASE_WAIT = "Please, wait...";

    private static DialogManager instance;

    private Dialog dialog;
    private Context context;

    /**
     * Private construct, do nothing
     */
    private DialogManager() {
    }

    /**
     * Singleton which gets the unique instance of DialogFactory and manage
     * contexts automatically
     * 
     * @param context
     *            Sets the current context of the current Activity
     * @return The unique instance of DialogFactory
     */
    public static synchronized DialogManager getInstance(Context context) {
	if (instance == null) {
	    instance = new DialogManager();
	}
	instance.context = context;
	return instance;
    }

    /**
     * Dismiss the dialog
     */
    public void dismiss() {
	if (this.dialog != null) {
	    this.dialog.dismiss();
	    this.dialog = null;
	    return;
	}
    }

    /**
     * Shows a Alert Dialog with a custom title, the message and the button
     * listener
     * 
     * @param title
     *            Title of your dialog
     * @param message
     *            Message of your dialog
     * @param listener
     *            Listener which be execute after click on button
     */
    public void makeAlert(String title, String message, OnClickListener listener) {
	this.dismiss();
	this.dialog = new AlertDialog.Builder(this.context)
		.setTitle(title)
		.setMessage(message)
		.setCancelable(false)
		.setPositiveButton(this.context.getString(android.R.string.ok),
			listener).show();
    }

    /**
     * Shows a Alert Dialog with a custom title, the message and the button
     * listener
     * 
     * @param titleId
     *            Title id of your dialog
     * @param messageId
     *            Message id of your dialog
     * @param listener
     *            Listener which be execute after click on button
     */
    public void makeAlert(int titleId, int messageId, OnClickListener listener) {
	this.dismiss();
	this.dialog = new AlertDialog.Builder(this.context)
		.setTitle(titleId)
		.setMessage(messageId)
		.setCancelable(false)
		.setPositiveButton(this.context.getString(android.R.string.ok),
			listener).show();
    }

    /**
     * Shows a Alert Dialog with a default title, the message and the button
     * listener
     * 
     * @param message
     *            Message of your dialog
     * @param listener
     *            Listener which be execute after click on button
     */
    public void makeAlert(String message, OnClickListener listener) {
	this.makeAlert(this.WARNING, message, listener);
    }

    /**
     * Shows a Alert Dialog with a default title, the message and the button
     * listener
     * 
     * @param messageId
     *            Message id of your dialog
     * @param listener
     *            Listener which be execute after click on button
     */
    public void makeAlert(int messageId, OnClickListener listener) {
	this.makeAlert(this.WARNING, this.context.getString(messageId),
		listener);
    }

    /**
     * Shows a Alert Dialog with a default title, the message and without button
     * listener
     * 
     * @param message
     *            Message of your dialog
     */
    public void makeAlert(String message) {
	this.makeAlert(message, null);
    }

    /**
     * Shows a Alert Dialog with a default title, the message and without button
     * listener
     * 
     * @param messageId
     *            Message id of your dialog
     */
    public void makeAlert(int messageId) {
	this.makeAlert(messageId, null);
    }

    /**
     * Shows a Confirm Dialog which has a custom title, the message and yes
     * button listener
     * 
     * @param title
     *            Title of your dialog
     * @param message
     *            Message of your dialog
     * @param yesListener
     *            Listener which be execute after click on yes button
     */
    public void makeConfirm(String title, String message,
	    OnClickListener yesListener, OnClickListener noListener) {
	this.dismiss();
	this.dialog = new AlertDialog.Builder(this.context)
		.setTitle(title)
		.setMessage(message)
		.setCancelable(false)
		.setPositiveButton(
			this.context.getString(android.R.string.yes),
			yesListener)
		.setNegativeButton(this.context.getString(android.R.string.no),
			noListener).show();
    }

    /**
     * Shows a Confirm Dialog which has a custom title, the message and yes
     * button listener
     * 
     * @param titleId
     *            Title id of your dialog
     * @param messageId
     *            Message id of your dialog
     * @param yesListener
     *            Listener which be execute after click on yes button
     */
    public void makeConfirm(int titleId, int messageId,
	    OnClickListener yesListener) {
	this.dismiss();
	this.dialog = new AlertDialog.Builder(this.context)
		.setTitle(titleId)
		.setMessage(messageId)
		.setCancelable(false)
		.setPositiveButton(
			this.context.getString(android.R.string.yes),
			yesListener)
		.setNegativeButton(this.context.getString(android.R.string.no),
			null).show();
    }

    /**
     * Shows a Confirm Dialog which has a default title, the message and yes
     * button listener
     * 
     * @param message
     *            Message of your dialog
     * @param yesListener
     *            Listener which be execute after click on yes button
     */
    public void makeConfirm(String message, OnClickListener yesListener,
	    OnClickListener noListener) {
	this.makeConfirm(this.WARNING, message, yesListener, noListener);
    }

    /**
     * Shows a Confirm Dialog which has a default title, the message and yes
     * button listener
     * 
     * @param messageId
     *            Message id of your dialog
     * @param yesListener
     *            Listener which be execute after click on yes button
     */
    public void makeConfirm(int messageId, OnClickListener yesListener,
	    OnClickListener noListener) {
	this.makeConfirm(this.WARNING, this.context.getString(messageId),
		yesListener, noListener);
    }

    /**
     * Shows a custom View with has a custom title and can be cancelable
     * 
     * @param title
     *            Title of your dialog
     * @param view
     *            View of your dialog
     * @param cancelable
     *            If your dialog can be cancelable
     */
    public void showContent(String title, View view, boolean cancelable) {
	this.dismiss();
	AlertDialog.Builder builder = new AlertDialog.Builder(this.context)
		.setTitle(title).setView(view).setCancelable(cancelable);
	if (title != null) {
	    builder.setTitle(title);
	}
	this.dialog = builder.show();

	this.setOnDismissListener(view);
    }

    /**
     * Shows a custom View with has a custom title and can be cancelable
     * 
     * @param titleId
     *            Title id of your dialog
     * @param view
     *            View of your dialog
     * @param cancelable
     *            If your dialog can be cancelable
     */
    public void showContent(int titleId, View view, boolean cancelable) {
	this.dismiss();
	AlertDialog.Builder builder = new AlertDialog.Builder(this.context)
		.setTitle(titleId).setView(view).setCancelable(cancelable);
	this.dialog = builder.show();

	this.setOnDismissListener(view);
    }

    /**
     * Remove all views when dialog is dismissing. This is important because of
     * "the specified child already has a parent" exception
     * 
     * @param view
     *            View of your dialog
     */
    private void setOnDismissListener(final View view) {
	this.dialog.setOnDismissListener(new OnDismissListener() {
	    @Override
	    public void onDismiss(DialogInterface dialog) {
		ViewGroup parent = (ViewGroup) view.getParent();
		parent.removeAllViews();
	    }
	});
    }

    /**
     * Shows a custom View with has a custom title and can be cancelable
     * 
     * @param title
     *            Title id of your dialog
     * @param view
     *            View of your dialog
     */
    public void showContent(String title, View view) {
	this.showContent(title, view, true);
    }

    /**
     * Shows a custom View with has a custom title and can be cancelable
     * 
     * @param titleId
     *            Title of your dialog
     * @param view
     *            View of your dialog
     */
    public void showContent(int titleId, View view) {
	this.showContent(titleId, view, true);
    }

    /**
     * Shows a custom View with has a default title and can be cancelable
     * 
     * @param view
     *            View of your dialog
     * @param cancelable
     *            If your dialog can be cancelable
     */
    public void showContent(View view, boolean cancelable) {
	this.showContent(null, view, cancelable);
    }

    /**
     * Shows a custom View that can be cancelable
     * 
     * @param view
     *            View of your dialog
     */
    public void showContent(View view) {
	this.showContent(null, view, true);
    }

    /**
     * Shows a Wait Screen which has a default message and can't be cancelable
     */
    public void showWaiting() {
	this.showWaiting(this.PLEASE_WAIT, false);
    }

    /**
     * Shows a Wait Screen which has a default message and can be cancelable
     * 
     * @param cancelable
     *            If your dialog can be close by back button
     */
    public void showWaiting(boolean cancelable) {
	this.showWaiting(this.PLEASE_WAIT, cancelable);
    }

    /**
     * Shows a Wait Screen which has a custom message and can be cancelable
     * 
     * @param message
     *            The message of this dialog
     * @param cancelable
     *            If your dialog can be close by back button
     */
    public void showWaiting(String message, boolean cancelable) {
	this.dismiss();
	this.dialog = ProgressDialog.show(this.context, "", message, true,
		cancelable);
	this.dialog.show();
    }

    /**
     * Shows a Wait Screen which has a custom message and can be cancelable
     * 
     * @param messageId
     *            Message id of this dialog
     * @param cancelable
     *            If your dialog can be close by back button
     */
    public void showWaiting(int messageId, boolean cancelable) {
	this.dismiss();
	this.dialog = ProgressDialog.show(this.context, "",
		this.context.getString(messageId), true, cancelable);
	this.dialog.show();
    }

    /**
     * Verifies if your dialog is showing or not
     * 
     * @return If your dialog is showing
     */
    public boolean isShowing() {
	return this.dialog.isShowing();
    }

    public void makeToast(int textid) {
	Toast.makeText(context, context.getString(textid), Toast.LENGTH_SHORT)
		.show();
    }

    public void makeToast(String message) {
	if (message != null) {
	    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
    }
}

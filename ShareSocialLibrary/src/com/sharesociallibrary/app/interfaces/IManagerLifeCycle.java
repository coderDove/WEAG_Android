package com.sharesociallibrary.app.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Life cycle interface for social managers
 * 
 * @author Viacheslav.Titov
 * 
 */
public interface IManagerLifeCycle
{
	public void onResume();

	public void onPause();

	public void onDestroy();

	public void onStart();

	public void onStop();

	public void onSaveInstanceState(Bundle outState);

	public void onActivityResult(int requestCode, int resultCode, Intent data);

	public void onCreate(Bundle bundle, Activity activity);

}

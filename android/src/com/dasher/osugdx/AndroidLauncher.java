package com.dasher.osugdx;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AndroidLauncher extends AndroidApplication {


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
			if (!Environment.isExternalStorageManager()) {
				Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
				startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
				if (!Environment.isExternalStorageManager()) {
					Toast.makeText(this, "You must supply the permission if you want the game to work properly on android 11!", Toast.LENGTH_LONG).show();
				}
			}
		} else {
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
			}
			if (checkSelfPermission(READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
				requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
			}
		}

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useCompass = false;
		config.useImmersiveMode = true;
		config.useAccelerometer = false;
		config.useGyroscope = false;
		config.useWakelock = true;
		config.useRotationVectorSensor = false;

		initialize(new OsuGame(new AndroidToast(this)), config);
	}
}

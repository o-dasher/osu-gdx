package com.dasher.osugdx;

import android.Manifest;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}
		if (checkSelfPermission(READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
			requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
		}
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useAccelerometer = false;
		config.useGyroscope = false;
		config.hideStatusBar = true;
		config.useWakelock = true;
		config.useRotationVectorSensor = false;
		config.useImmersiveMode = true;
		initialize(new OsuGame(), config);
	}
}

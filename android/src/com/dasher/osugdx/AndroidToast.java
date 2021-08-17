package com.dasher.osugdx;

import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;

public class AndroidToast implements PlatformToast {
    private final AndroidApplication app;

    public AndroidToast(AndroidApplication app) {
        this.app = app;
    }

    @Override
    public void log(String text) {
        app.runOnUiThread(() ->
                Toast.makeText(app.getContext(), text, Toast.LENGTH_SHORT).show()
        );
    }
}

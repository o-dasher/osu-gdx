package com.dasher.osugdx;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.Beatmaps.OSZParser;
import com.dasher.osugdx.IO.GameIO;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;

public class ImportOSZ extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getData() != null) {
            String path = getIntent().getData().getPath();
            FileHandle file = Gdx.files.external(path);
            GameIO gameIO = new GameIO();
            Json json = new Json();
            BeatMapStore beatMapStore = new BeatMapStore(gameIO, json, new PlatformToast() {});
            OSZParser oszParser = new OSZParser(gameIO, beatMapStore);
            oszParser.parseOSZ(file);
            Toast.makeText(this, "Imported BeatmapSet: " + file.path(), Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }
}

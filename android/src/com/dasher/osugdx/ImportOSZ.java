package com.dasher.osugdx;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.dasher.osugdx.osu.Beatmaps.BeatMapStore;
import com.dasher.osugdx.osu.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.osu.Beatmaps.OSZParser;
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
            PlatformToast toast = new PlatformToast() {};
            BeatmapUtils beatmapUtils = new BeatmapUtils();
            // TODO: BROKEN
            BeatMapStore beatMapStore = new BeatMapStore(new OsuGame(toast));
            beatmapUtils.setBeatMapStore(beatMapStore);
            OSZParser oszParser = new OSZParser(gameIO, beatMapStore);
            beatMapStore.setOszParser(oszParser);
            oszParser.parseOSZ(file);
            Toast.makeText(this, "Imported BeatmapSet: " + file.path(), Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }
}

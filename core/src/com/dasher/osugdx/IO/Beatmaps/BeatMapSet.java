package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import me.zeroeightysix.osureader.OsuFile;

public class BeatMapSet extends ArrayList<Koohii.Map> implements Serializable {
    public String beatmapSetFolderPath;
    public File folder;
    public ArrayList<File> beatmapFiles = new ArrayList<>();

    public BeatMapSet(@NotNull FileHandle beatmapSetFolder) {
        this.folder = new File(Gdx.files.getExternalStoragePath() + "/" + beatmapSetFolder.path());
        this.beatmapSetFolderPath = beatmapSetFolder.path();
    }
}

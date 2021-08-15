package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

import me.zeroeightysix.osureader.OsuFile;

public class BeatMapSet extends LinkedList<OsuFile> implements Serializable {
    public String beatmapSetFolderPath;
    public int amountBeatmaps = 0;
    public File folder;
    public LinkedList<File> beatmapFiles = new LinkedList<>();

    public BeatMapSet(@NotNull FileHandle beatmapSetFolder) {
        this.folder = new File(Gdx.files.getExternalStoragePath() + "/" + beatmapSetFolder.path());
        this.beatmapSetFolderPath = beatmapSetFolder.path();
    }

    @Override
    public boolean add(OsuFile osuFile) {
        amountBeatmaps++;
        return super.add(osuFile);
    }
}

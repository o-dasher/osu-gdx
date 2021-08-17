package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class BeatMapSet implements Serializable {
    public String beatmapSetFolderPath;
    public Array<Koohii.Map> beatmaps = new Array<>();
    private final String defaultString = "";

    private BeatMapSet() {}  // NO-ARG CONSTRUCTOR FOR JSON PARSER!

    private Koohii.@Nullable Map getFirstBeatmap() {
        return 0 < beatmaps.size? beatmaps.get(0) : null;
    }

    public String getArtist() {
        return getFirstBeatmap() == null? defaultString : getFirstBeatmap().artist;
    }

    public String getCreator() {
        return getFirstBeatmap() == null? defaultString : getFirstBeatmap().creator;
    }

    public String getTitle() {
        return getFirstBeatmap() == null? defaultString : getFirstBeatmap().title;
    }

    public FileHandle getFolder() {
        return Gdx.files.external(beatmapSetFolderPath);
    }

    public BeatMapSet(@NotNull FileHandle beatmapSetFolder) {
        this.beatmapSetFolderPath = beatmapSetFolder.path();
    }
}

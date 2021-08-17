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
    private final String defaultString = "UNKNOWN";

    private BeatMapSet() {}  // NO-ARG CONSTRUCTOR FOR JSON PARSER!

    public String getArtist() {
        return beatmaps.first() == null? defaultString : beatmaps.first().artist;
    }

    public String getCreator() {
        return beatmaps.first() == null? defaultString : beatmaps.first().creator;
    }

    public String getTitle() {
        return beatmaps.first() == null? defaultString : beatmaps.first().title;
    }

    public FileHandle getFolder() {
        return Gdx.files.external(beatmapSetFolderPath);
    }

    public BeatMapSet(@NotNull FileHandle beatmapSetFolder) {
        this.beatmapSetFolderPath = beatmapSetFolder.path();
    }

    @Override
    public String toString() {
        return getArtist() + " // " + getCreator() + " - " + getTitle();
    }
}

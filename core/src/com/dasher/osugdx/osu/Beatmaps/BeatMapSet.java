package com.dasher.osugdx.osu.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class BeatMapSet {
    public String beatmapSetFolderPath;
    public Array<Beatmap> beatmaps = new Array<>();
    private final String defaultString = "UNKNOWN";

    private BeatMapSet() {}  // NO-ARG CONSTRUCTOR FOR JSON PARSER!

    public String getArtist() {
        return beatmaps.first() == null? defaultString : beatmaps.first().getMetadata().getArtist();
    }

    public String getCreator() {
        return beatmaps.first() == null? defaultString : beatmaps.first().getMetadata().getCreator();
    }

    public String getTitle() {
        return beatmaps.first() == null? defaultString : beatmaps.first().getMetadata().getTitle();
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

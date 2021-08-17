package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class BeatMapSet implements Serializable {
    public String beatmapSetFolderPath;
    public Array<Koohii.Map> beatmaps = new Array<>();

    private BeatMapSet() {}  // NO-ARG CONSTRUCTOR FOR JSON PARSER!

    public FileHandle getFolder() {
        return Gdx.files.external(beatmapSetFolderPath);
    }

    public BeatMapSet(@NotNull FileHandle beatmapSetFolder) {
        this.beatmapSetFolderPath = beatmapSetFolder.path();
    }
}

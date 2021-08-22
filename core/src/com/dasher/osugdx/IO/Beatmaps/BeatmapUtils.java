package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.files.FileHandle;
import com.dasher.osugdx.IO.IOHelper;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.parser.BeatmapException;
import lt.ekgame.beatmap_analyzer.parser.BeatmapParser;

public class BeatmapUtils {
    private final BeatmapParser beatmapParser = new BeatmapParser();
    private BeatMapStore beatMapStore;


    public Beatmap createMap(@NotNull FileHandle mapFile) {
        InputStream reader = mapFile.read();
        Beatmap beatmap = null;

        try {
            beatmap = beatmapParser.parse(reader);
        } catch (BeatmapException e) {
            beatMapStore.deleteBeatmapFile(null, mapFile);
            e.printStackTrace();
        }

        if (beatmap != null) {
            beatmap.beatmapFilePath = mapFile.path();
        }

        IOHelper.close(reader);

        return beatmap;
    }

    public void setBeatMapStore(BeatMapStore beatMapStore) {
        this.beatMapStore = beatMapStore;
    }
}

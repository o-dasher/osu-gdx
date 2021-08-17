package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.files.FileHandle;
import com.dasher.osugdx.IO.IOHelper;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class BeatmapUtils {
    private BeatMapStore beatMapStore;

    protected BufferedReader getMapReader(@NotNull FileHandle file) {
        BufferedReader beatmapBR = null;
        try {
            beatmapBR = new BufferedReader(new InputStreamReader(new FileInputStream(file.file())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return beatmapBR;
    }

    public Koohii.Map createMap(FileHandle mapFile) {
        BufferedReader reader = getMapReader(mapFile);
        Koohii.Map map;
        try {
            map = new Koohii.Parser().map(reader);
        } catch (IOException e) {
            e.printStackTrace();
            IOHelper.close(reader);
            return null;
        }  catch (UnsupportedOperationException e) {
            System.out.println("Can't parse other beatmap modes...");
            IOHelper.close(reader);
            beatMapStore.deleteBeatmapFile(mapFile);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading beatmap: " + mapFile.path());
            IOHelper.close(reader);
            beatMapStore.deleteBeatmapFile(mapFile);
            return null;
        }
        if (map != null) {
            map.beatmapFilePath = mapFile.path();
        }
        IOHelper.close(reader);
        return map;
    }

    public void setBeatMapStore(BeatMapStore beatMapStore) {
        this.beatMapStore = beatMapStore;
    }
}

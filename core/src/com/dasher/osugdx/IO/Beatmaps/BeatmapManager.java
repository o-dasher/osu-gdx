package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class BeatmapManager {
    private final BeatMapStore beatMapStore;
    private BeatMapSet currentBeatmapSet;
    private Beatmap currentMap;
    private final PlatformToast toast;
    private final BeatmapUtils beatmapUtils;

    public BeatmapManager(BeatMapStore beatMapStore, PlatformToast toast, BeatmapUtils beatmapUtils) {
        this.beatMapStore = beatMapStore;
        this.toast = toast;
        this.beatmapUtils = beatmapUtils;
    }

    public BeatMapSet getCurrentBeatmapSet() {
        return currentBeatmapSet;
    }

    public void randomizeCurrentBeatmapSet() {
        setCurrentBeatmapSet(beatMapStore.getBeatMapSets().random());
    }

    public void setCurrentBeatmapSet(BeatMapSet currentBeatmapSet) {
        if (currentBeatmapSet == null) {
            return;
        }
        this.currentBeatmapSet = currentBeatmapSet;
        Beatmap beatmapSetFirstMap = currentBeatmapSet.beatmaps.first();
        if (beatmapSetFirstMap == null) {
            currentBeatmapSet.getFolder().delete();
            beatMapStore.getBeatMapSets().removeValue(currentBeatmapSet, true);
            randomizeCurrentBeatmapSet();
        } else {
            System.out.println("Selected mapSet: " + currentBeatmapSet.toString());
            setCurrentMap(beatmapSetFirstMap);
        }
    }

    public Beatmap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Beatmap newMap) {
        if (!currentBeatmapSet.beatmaps.contains(newMap, true)) {
            toast.log("Abnormal beatmap selected!");
            setCurrentMap(currentBeatmapSet.beatmaps.first());
        }
        if (this.currentMap != null) {
            this.currentMap.freeResources();
        }
        this.currentMap = beatmapUtils.createMap(Gdx.files.external(newMap.beatmapFilePath));
        System.out.println("Selected map: " + this.currentMap.toString());
    }
}

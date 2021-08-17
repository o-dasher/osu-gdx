package com.dasher.osugdx.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;
import com.github.francesco149.koohii.Koohii;

public class BeatmapManager {
    private final BeatMapStore beatMapStore;
    private BeatMapSet currentBeatmapSet;
    private Koohii.Map currentMap;
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
        Koohii.Map beatmapSetFirstMap = currentBeatmapSet.beatmaps.first();
        if (beatmapSetFirstMap == null) {
            currentBeatmapSet.getFolder().delete();
            beatMapStore.getBeatMapSets().removeValue(currentBeatmapSet, true);
            randomizeCurrentBeatmapSet();
        } else {
            System.out.println("Selected mapSet: " + currentBeatmapSet.toString());
            setCurrentMap(beatmapSetFirstMap);
        }
    }

    public Koohii.Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Koohii.Map currentMap) {
        if (!currentBeatmapSet.beatmaps.contains(currentMap, true)) {
            toast.log("Abnormal beatmap selected!");
            setCurrentMap(currentBeatmapSet.beatmaps.first());
        }
        if (this.currentMap != null) {
            this.currentMap.freeResources();
        }
        this.currentMap = beatmapUtils.createMap(Gdx.files.external(currentMap.beatmapFilePath));
        System.out.println("Selected map: " + currentMap.toString());
    }
}

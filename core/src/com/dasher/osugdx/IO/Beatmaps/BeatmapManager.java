package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class BeatmapManager {
    private final BeatMapStore beatMapStore;
    private BeatMapSet currentBeatmapSet;
    private Beatmap currentMap;
    private Music currentMusic;
    private final PlatformToast toast;
    private final BeatmapUtils beatmapUtils;
    private final AudioManager audioManager;

    public BeatmapManager(BeatMapStore beatMapStore, PlatformToast toast, BeatmapUtils beatmapUtils, AudioManager audioManager) {
        this.beatMapStore = beatMapStore;
        this.toast = toast;
        this.beatmapUtils = beatmapUtils;
        this.audioManager = audioManager;
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
        if (currentMap != null) {
            currentMap.freeResources();
        }
        currentMap = beatmapUtils.createMap(Gdx.files.external(newMap.beatmapFilePath));
        if (currentMusic != null && currentMusic.isPlaying()) {
            audioManager.stopMusic(currentMusic);
        }
        currentMusic = Gdx.audio.newMusic(
                Gdx.files.external(
                        currentBeatmapSet.beatmapSetFolderPath
                                + "/" + currentMap.getGenerals().getAudioFileName())
        );
        currentMusic.setOnCompletionListener((m) -> setCurrentMap(currentMap));
        audioManager.playMusic(currentMusic);
        System.out.println("Selected map: " + this.currentMap.toString());
    }
}

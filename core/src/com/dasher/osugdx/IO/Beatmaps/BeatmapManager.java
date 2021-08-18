package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
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

    public void setCurrentBeatmapSet(BeatMapSet newBeatmapSet) {
        if (newBeatmapSet == null) {
            return;
        }

        if (currentBeatmapSet != null) {
            for (Beatmap beatmap: currentBeatmapSet.beatmaps) {
                beatmap.freeResources();
            }
        }

        Beatmap beatmapSetFirstMap = newBeatmapSet.beatmaps.first();
        if (beatmapSetFirstMap == null) {
            newBeatmapSet.getFolder().delete();
            beatMapStore.getBeatMapSets().removeValue(newBeatmapSet, true);
            randomizeCurrentBeatmapSet();
        } else {
            System.out.println("Selected mapSet: " + newBeatmapSet.toString());

            Array<Beatmap> loadedBeatmaps = new Array<>();
            for (Beatmap beatmap: newBeatmapSet.beatmaps) {
                FileHandle beatmapFile = Gdx.files.external(beatmap.beatmapFilePath);
                loadedBeatmaps.add(beatmapUtils.createMap(beatmapFile));
            }

            newBeatmapSet.beatmaps.clear();
            newBeatmapSet.beatmaps.addAll(loadedBeatmaps);

            this.currentBeatmapSet = newBeatmapSet;
            setCurrentMap(currentBeatmapSet.beatmaps.first());
        }
    }

    private void setupMusic(Beatmap newMap) {
        if (currentMusic != null && currentMusic.isPlaying()) {
            audioManager.stopMusic(currentMusic);
        }
        String musicPath = currentBeatmapSet.beatmapSetFolderPath + "/" + newMap.getGenerals().getAudioFileName();
        FileHandle musicFile = Gdx.files.external(musicPath);
        currentMusic = Gdx.audio.newMusic(musicFile);
        currentMusic.setOnCompletionListener((m) -> {
            System.out.println("Beatmap music finished!");
            setCurrentMap(newMap);
        });
        audioManager.playMusic(currentMusic);
    }


    public Beatmap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Beatmap newMap) {
        if (!currentBeatmapSet.beatmaps.contains(newMap, true)) {
            toast.log("Abnormal beatmap selected!");
            setCurrentMap(currentBeatmapSet.beatmaps.first());
        }
        setupMusic(newMap);
        currentMap = newMap;
        System.out.println("Selected map: " + currentMap.toString());
    }
}

package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Framework.Interfaces.Listenable;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.GameScenes.SoundSelect.SoundSelectScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapGenerals;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class BeatmapManager implements Listenable<BeatmapManagerListener>, BeatmapManagerListener {
    private final BeatMapStore beatMapStore;
    private BeatMapSet currentBeatmapSet;
    private Beatmap currentMap;
    private String previousBeatmapSetFolder = "";
    private Music currentMusic;
    private long timeLastMap;
    private final OsuGame game;
    private final PlatformToast toast;
    private final BeatmapUtils beatmapUtils;
    private final AudioManager audioManager;
    private boolean isFirstBeatmapLoaded = false;
    private final Array<BeatmapManagerListener> beatmapManagerListeners = new Array<>();

    public BeatmapManager(OsuGame game, BeatMapStore beatMapStore, PlatformToast toast, BeatmapUtils beatmapUtils, AudioManager audioManager) {
        this.game = game;
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
            randomizeCurrentBeatmapSet();
            return;
        }

        if (currentBeatmapSet != null) {
            for (Beatmap beatmap: currentBeatmapSet.beatmaps) {
                beatmap.freeResources();
            }
        }
        
        if (newBeatmapSet.beatmaps.isEmpty()) {
            newBeatmapSet.getFolder().delete();
            beatMapStore.getBeatMapSets().removeValue(newBeatmapSet, true);
            randomizeCurrentBeatmapSet();
        } else {
            System.out.println("Selected mapSet: " + newBeatmapSet.toString());
            currentBeatmapSet = newBeatmapSet;
            reInitBeatmapSet(currentBeatmapSet);
            this.onNewBeatmapSet(currentBeatmapSet);
            if (currentBeatmapSet.beatmaps.isEmpty()) {
                handleEmptyBeatmapSet(currentBeatmapSet);
            } else {
                setCurrentMap(currentBeatmapSet.beatmaps.first());
            }
        }
    }

    private void reInitBeatmapSet(@NotNull BeatMapSet beatMapSet) {
        for (int i = 0; i < beatMapSet.beatmaps.size; i++) {
            Beatmap beatmap = beatMapSet.beatmaps.get(i);
            FileHandle beatmapFile = Gdx.files.external(beatmap.beatmapFilePath);
            if (beatmapFile.exists()) {
                beatMapSet.beatmaps.set(i, beatmapUtils.createMap(beatmapFile));
            } else {
                handleEmptyBeatmapSet(beatMapSet);
            }
        }
    }

    private void setupMusic(@NotNull Beatmap newMap) {
        String newFolder = currentBeatmapSet.beatmapSetFolderPath + "/";
        if (newMap.getGenerals() == null) {
            beatMapStore.deleteBeatmapFile(null, Gdx.files.external(newMap.beatmapFilePath));
            if (game.getScreen() instanceof SoundSelectScreen) {
                game.getScreen().show();
            }
            return;
        }

        String newMusicPath = newFolder + newMap.getGenerals().getAudioFileName();
        String currentMusicPath = currentMap != null && currentMap.getGenerals() != null?
                previousBeatmapSetFolder + currentMap.getGenerals().getAudioFileName() : "";

        if (currentMusic != null && !newMap.equals(currentMap)) {
            // WE DON'T RESTART MUSIC IF ITS SAME MAP ON UI SCREEN
            if (!(newMusicPath.equals(currentMusicPath) && game.getScreen() instanceof UIScreen) || !currentMusic.isPlaying()) {
                currentMusic.dispose();
            }
        }

        boolean isReplayingBeatmapMusic;

        // WE DON'T WANT TO RELOAD THE MUSIC IF IT'S THE SAME MUSIC REPLAYING
        if (!newMusicPath.equals(currentMusicPath)) {
            isReplayingBeatmapMusic = false;
            FileHandle musicFile = Gdx.files.external(newMusicPath);
            try {
                currentMusic = Gdx.audio.newMusic(musicFile);
                currentMusic.setOnCompletionListener((music) -> {
                    System.out.println("Beatmap music finished!");
                    if (currentMusic == music) {
                        randomizeCurrentBeatmapSet();
                    }
                });
            } catch (Exception e) {
                toast.log("Failed to create map music for: " + musicFile.name());
                e.printStackTrace();
            }
            System.out.println("New music: " + newMusicPath);
        } else {
            isReplayingBeatmapMusic = true;
            System.out.println("Replaying beatmap music: " + newMusicPath);
        }

        Screen gameScreen = game.getScreen();
        if (currentMusic != null) {
            if (gameScreen != null && !(gameScreen instanceof IntroScreen)) {
                startMusicPlaying(newMap, isReplayingBeatmapMusic);
            }
        }

        previousBeatmapSetFolder = newFolder;
    }

    public Beatmap getCurrentMap() {
        return currentMap;
    }

    private void handleEmptyBeatmapSet(BeatMapSet beatMapSet) {
        beatMapStore.deleteBeatmapFile(beatMapSet, null);
        randomizeCurrentBeatmapSet();
        if (game.getScreen() instanceof SoundSelectScreen) {
            game.getScreen().show();
        }
    }

    public void setCurrentMap(Beatmap newMap) {
        if (!currentBeatmapSet.beatmaps.contains(newMap, true)) {
            toast.log("Abnormal beatmap selected!");
            beatMapStore.deleteBeatmapFile(currentBeatmapSet, null);
            if (currentBeatmapSet.beatmaps.isEmpty()) {
                handleEmptyBeatmapSet(currentBeatmapSet);
            } else {
                setCurrentMap(currentBeatmapSet.beatmaps.first());
            }
        }
        setupMusic(newMap);
        currentMap = newMap;
        timeLastMap = System.nanoTime();
        onNewBeatmap(currentMap);
        System.out.println("Selected map: " + currentMap.toString());
        isFirstBeatmapLoaded = true;
    }

    public void startMusicPlaying() {
        startMusicPlaying(currentMap, false);
    }

    public void startMusicPlaying(Beatmap beatmap, boolean isReplayingBeatmapMusic) {
      if (currentMusic != null) {
          // Playing audio on render thread simply because
          // spawning multiple submits for music play will cause memory leaks eventually
          audioManager.playMusic(currentMusic);
          currentMusic.setPosition(beatmap.getGenerals().getPreviewTime());
        }
    }

    public boolean isFirstBeatmapLoaded() {
        return isFirstBeatmapLoaded;
    }

    public long getTimeLastMap() {
        return timeLastMap;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    @Override
    public Array<BeatmapManagerListener> getListeners() {
        return beatmapManagerListeners;
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {
        for (BeatmapManagerListener listener: beatmapManagerListeners) {
            listener.onNewBeatmap(beatmap);
        }
    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {
        for (BeatmapManagerListener listener: beatmapManagerListeners) {
            listener.onNewBeatmapSet(beatMapSet);
        }
    }
}

package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Framework.Interfaces.Listenable;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class BeatmapManager implements Listenable<BeatmapManagerListener>, BeatmapManagerListener {
    private final BeatMapStore beatMapStore;
    private BeatMapSet currentBeatmapSet;
    private Beatmap currentMap;
    private Music currentMusic;
    private long timeLastMap;
    private final OsuGame game;
    private final PlatformToast toast;
    private final BeatmapUtils beatmapUtils;
    private final AudioManager audioManager;
    private boolean isFirstBeatmapLoaded = false;
    private int startMusicPlayingCalls;
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

            currentBeatmapSet = newBeatmapSet;
            setCurrentMap(currentBeatmapSet.beatmaps.first());
        }
    }

    private void setupMusic(Beatmap newMap) {
        if (currentMusic != null && currentMusic.isPlaying() && !newMap.equals(currentMap)) {
            currentMusic.dispose();
        }

        String folder = currentBeatmapSet.beatmapSetFolderPath + "/";
        String newMusicPath = folder + newMap.getGenerals().getAudioFileName();
        String currentMusicPath = currentMap != null?
                folder + currentMap.getGenerals().getAudioFileName() : null;

        // WE DON'T WANT TO RELOAD THE MUSIC IF IT'S THE SAME MUSIC REPLAYING
        if (currentMusicPath != null && !newMusicPath.equals(currentMusicPath) || currentMusic == null) {
            FileHandle musicFile = Gdx.files.external(newMusicPath);
            currentMusic = Gdx.audio.newMusic(musicFile);
            currentMusic.setOnCompletionListener((music) -> {
                System.out.println("Beatmap music finished!");
                setCurrentMap(newMap);
            });
            System.out.println("New music: " + newMusicPath);
        } else {
            System.out.println("Replaying beatmap music: " + newMap.toString());
        }

        Screen gameScreen = game.getScreen();

        if (gameScreen instanceof UIScreen) {
            currentMusic.setPosition(newMap.getGenerals().getPreviewTime());
        }
        if (gameScreen != null && !(gameScreen instanceof IntroScreen)) {
            audioManager.playMusic(currentMusic);
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
        setupMusic(newMap);
        currentMap = newMap;
        timeLastMap = System.nanoTime();
        onNewBeatmap(currentMap);
        System.out.println("Selected map: " + currentMap.toString());
        isFirstBeatmapLoaded = true;
    }

    public void startMusicPlaying() {
        startMusicPlayingCalls++;
        if (startMusicPlayingCalls != 1) {
            throw new IllegalStateException(
                    "This should only be called when the screen is switched to the MenuScreen"
            );
        } else if (currentMusic != null) {
            currentMusic.play();
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
}

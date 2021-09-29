package com.dasher.osugdx.osu.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.dasher.osugdx.Audio.GameMusic;
import com.dasher.osugdx.Framework.Interfaces.Listenable;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.GameScenes.SoundSelect.SoundSelectScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;
import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;


public class BeatmapManager implements Listenable<BeatmapManagerListener>, BeatmapManagerListener, BeatmapManagerReferencesListener {
    private final BeatMapStore beatMapStore;
    private final OsuGame game;
    private final PlatformToast toast;
    private final Array<BeatmapManagerListener> beatmapManagerListeners = new Array<>();
    private BeatMapSet currentBeatmapSet;
    private Beatmap currentMap;
    private String previousBeatmapSetFolder = "";
    private GameMusic currentMusic;
    private long timeLastMap;
    private boolean isFirstBeatmapLoaded = false;

    public BeatmapManager(OsuGame game, BeatMapStore beatMapStore, PlatformToast toast) {
        this.game = game;
        this.beatMapStore = beatMapStore;
        this.toast = toast;
    }

    public BeatMapSet getCurrentBeatmapSet() {
        return currentBeatmapSet;
    }

    public void setCurrentBeatmapSet(BeatMapSet newBeatmapSet) {
        if (newBeatmapSet == null) {
            randomizeCurrentBeatmapSet();
            return;
        }

        if (newBeatmapSet.beatmaps.isEmpty()) {
            newBeatmapSet.getFolder().delete();
            beatMapStore.getBeatMapSets().removeValue(newBeatmapSet, true);
            randomizeCurrentBeatmapSet();
        } else {
            System.out.println("Selected mapSet: " + newBeatmapSet.toString());
            currentBeatmapSet = newBeatmapSet;
            this.onNewBeatmapSet(currentBeatmapSet);
            if (currentBeatmapSet.beatmaps.isEmpty()) {
                handleEmptyBeatmapSet(currentBeatmapSet);
            } else {
                setCurrentMap(currentBeatmapSet.beatmaps.first());
            }
        }
    }

    public void randomizeCurrentBeatmapSet() {
        BeatMapSet previousBeatmapSet = currentBeatmapSet;
        while (currentBeatmapSet == previousBeatmapSet) {
            setCurrentBeatmapSet(beatMapStore.getBeatMapSets().random());
        }
    }

    // Return whether it's the same music repeating itself
    private boolean setupMusic(@NotNull Beatmap newMap) {
        String newFolder = currentBeatmapSet.beatmapSetFolderPath + "/";
        if (newMap.getGenerals() == null) {
            beatMapStore.deleteBeatmapFile(null, Gdx.files.external(newMap.beatmapFilePath));
            if (game.getScreen() instanceof SoundSelectScreen) {
                game.getScreen().show();
            }
            return false;
        }

        String newMusicPath = newFolder + newMap.getGenerals().getAudioFileName();
        String currentMusicPath = currentMap != null && currentMap.getGenerals() != null ?
                previousBeatmapSetFolder + currentMap.getGenerals().getAudioFileName() : "";

        if (currentMusic != null && !newMap.equals(currentMap)) {
            // WE DON'T RESTART MUSIC IF ITS SAME MAP ON UI SCREEN
            if (!(newMusicPath.equals(currentMusicPath) && game.getScreen() instanceof UIScreen)) {
                game.parrot.stopMusic(currentMusic, true);
                GameMusic finalMusic = currentMusic;
                finalMusic.dispose();
            }
        }

        boolean isReplayingBeatmapMusic;

        // WE DON'T WANT TO RELOAD THE MUSIC IF IT'S THE SAME MUSIC REPLAYING
        if (!newMusicPath.equals(currentMusicPath)) {
            isReplayingBeatmapMusic = false;
            FileHandle musicFile = Gdx.files.external(newMusicPath);
            try {
                currentMusic = game.audioFactory.newMusic(Gdx.audio.newMusic(musicFile));
                currentMusic.setAsynchronous(true);
                currentMusic.getMusic().setOnCompletionListener((music) -> {
                    System.out.println("Beatmap music finished!");
                    if (currentMusic.hashCode() == music.hashCode()) {
                        // EVEN THOUGH THE THREAD THAT IS PLAYING THE MUSIC IS OR NOT THE GL
                        // THREAD POSTRUNNABLE MUST BE CALLED NO IDEA WHY THOUGH
                        Gdx.app.postRunnable(this::randomizeCurrentBeatmapSet);
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
        return isReplayingBeatmapMusic;
    }

    public Beatmap getCurrentMap() {
        return currentMap;
    }
    private boolean isProcessingDiff = false;

    public void setCurrentMap(Beatmap newMap) {
        if (!isProcessingDiff) {
            if (!currentBeatmapSet.beatmaps.contains(newMap, true)) {
                toast.log("Abnormal beatmap selected!");
                beatMapStore.deleteBeatmapFile(currentBeatmapSet, null);
                if (currentBeatmapSet.beatmaps.isEmpty()) {
                    handleEmptyBeatmapSet(currentBeatmapSet);
                } else {
                    setCurrentMap(currentBeatmapSet.beatmaps.first());
                }
            }
        }
        isProcessingDiff = true;
        onPreBeatmapChange();
        setupMusic(newMap);
        currentMap = newMap;
        for (int i = 0; i < currentBeatmapSet.beatmaps.size; i++) {
            Beatmap beatmap = currentBeatmapSet.beatmaps.get(i);
            if (beatmap.beatmapFilePath.equals(currentMap.beatmapFilePath)) {
                currentBeatmapSet.beatmaps.set(i, beatmap);
            }
        }
        setBeatmapReference(currentMap);
        long time = TimeUtils.millis();
        System.out.println(TimeUtils.timeSinceMillis(time) + "ms to load beatmap");
        if (currentMap == null) {
            randomizeCurrentBeatmapSet();
        }
        timeLastMap = System.nanoTime();
        onNewBeatmap(currentMap);
        System.out.println("Selected map: " + currentMap.toString());
        isFirstBeatmapLoaded = true;
        isProcessingDiff = false;
    }

    private void handleEmptyBeatmapSet(BeatMapSet beatMapSet) {
        beatMapStore.deleteBeatmapFile(beatMapSet, null);
        game.asyncExecutor.submit(() -> {
            randomizeCurrentBeatmapSet();
            if (game.getScreen() instanceof SoundSelectScreen) {
                game.getScreen().show();
            }
            return null;
        });
    }

    public void startMusicPlaying() {
        startMusicPlaying(currentMap, false);
    }

    public void startMusicPlaying(Beatmap beatmap, boolean isReplayingBeatmapMusic) {
        if (currentMusic != null && (!isReplayingBeatmapMusic || !currentMusic.getMusic().isPlaying())) {
            System.out.println("Starting playing music from Thread: " + Thread.currentThread().getName());
            currentMusic.playParrot(false, true); // <-- THIS NEEDS TO BE OUTSIDE OF THREAD
            currentMusic.forcePosition(beatmap.getGenerals().getPreviewTime());
        }
    }

    public boolean isFirstBeatmapLoaded() {
        return isFirstBeatmapLoaded;
    }

    public long getTimeLastMap() {
        return timeLastMap;
    }

    public Music getCurrentMusic() {
        return currentMusic == null? null : currentMusic.getMusic();
    }

    @Override
    public Array<BeatmapManagerListener> getListeners() {
        return beatmapManagerListeners;
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {
        for (BeatmapManagerListener listener : beatmapManagerListeners) {
            updateListenerReference(listener, beatmap);
            listener.onNewBeatmap(beatmap);
        }
    }

    public void updateListenerReference(BeatmapManagerListener listener, Beatmap beatmap) {
        if (listener instanceof BeatmapManagerReferencesListener) {
            if (beatmap.beatmapFilePath.equals(((BeatmapManagerReferencesListener) listener)
                    .getBeatmapReference().beatmapFilePath)
            ) {
                ((BeatmapManagerReferencesListener) listener).setBeatmapReference(beatmap);
            }
        }
    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {
        for (BeatmapManagerListener listener : beatmapManagerListeners) {
            listener.onNewBeatmapSet(beatMapSet);
        }
    }

    @Override
    public void onPreBeatmapChange() {
        for (BeatmapManagerListener listener : beatmapManagerListeners) {
            listener.onPreBeatmapChange();
        }
    }

    @Override
    public Beatmap getBeatmapReference() {
        return null;
    }

    @Override
    public void setBeatmapReference(Beatmap beatmap) {
        // INDEXED BECAUSE ASYNCHRONOUS;
        for (int i = 0; i < beatmapManagerListeners.size; i++) {
            updateListenerReference(beatmapManagerListeners.get(i), beatmap);
        }
    }
}

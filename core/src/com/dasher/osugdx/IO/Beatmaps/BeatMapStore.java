package com.dasher.osugdx.IO.Beatmaps;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.IO.GameIO;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import me.zeroeightysix.osureader.OsuFile;
import me.zeroeightysix.osureader.OsuReader;
import me.zeroeightysix.osureader.parse.OsuFileParser;
import me.zeroeightysix.osureader.parse.StandardOsuFileParser;

public class BeatMapStore {
    public final int VERSION = 1;
    private boolean finishedLoadingCache = false;
    private final Array<String> specialFiles = new Array<>();
    private final Array<BeatMapSet> tempCachedBeatmaps = new Array<>();
    private final Array<BeatMapSet> beatMapSets = new Array<>();
    private boolean beatmapLoadingStarted = false;
    private final FileHandle songsDir;
    private final Iterator<FileHandle> beatMapSetFoldersIterator;
    private boolean finishedLoading = false;
    private final OsuFileParser osuFileParser;
    private boolean libraryChanged = false;
    private final Long beatmapStoreCreationTime;
    public final File libCacheFile;
    private final Preferences beatmapStorePrefs;
    private ObjectOutputStream ostream;
    private ObjectInputStream istream;

    public BeatMapStore(@NotNull GameIO gameIO) {
        this.songsDir = gameIO.getSongsDir();
        String libCacheFilePath = songsDir.path() + "/.beatmap_cache.db";
        beatmapStorePrefs = Gdx.app.getPreferences(getClass().getSimpleName());
        libCacheFile = Gdx.files.external(libCacheFilePath).file();
        beatMapSetFoldersIterator = new Array<>(songsDir.list()).iterator();
        osuFileParser = new StandardOsuFileParser();
        beatmapStoreCreationTime = System.nanoTime();
        setupCaching();
        specialFiles.add(libCacheFile.getName());
    }

    private void verifyVersion() {
        int version = beatmapStorePrefs.getInteger("VERSION", VERSION);
        if (version != VERSION) {
            System.out.println("Outdated library, clearing cache and reloading");
            clearCache();
        }
    }

    private void setupCaching() {
        System.out.println("Library db " + (libCacheFile.exists()? "does" : "doesn't") + " exists!");
        if (libCacheFile.exists()) {
            verifyVersion();
            try {
                istream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(libCacheFile)));
            } catch (IOException e) {
                e.printStackTrace();
                libraryChanged = true;
                clearCache();
            }
        } else {
            try {
                if (libCacheFile.createNewFile()) {
                    System.out.println("Created a new beatmap cache library successfully");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearCache() {
        if (libCacheFile.exists()) {
            if (libCacheFile.delete()) {
                setupCaching();
                finishCacheLoading();
                libraryChanged = true;
                System.out.println("CLEARED LIBRARY CACHE!");
            } else {
                System.out.println("Failed to clear library cache");
            }
        }
    }

    private boolean isBeatmapSetValid(@NotNull BeatMapSet beatMapSet) {
        return !beatMapSet.isEmpty() && beatMapSet.folder.exists() && beatMapSet.folder.isDirectory();
    }

    private void loadCache() {
        System.out.println("Started reading cache");

        while (!isFinishedLoadingCache()) {
            loadNextCachedBeatmapSet();
        }
    }

    public void loadNextCachedBeatmapSet() {
        if (istream == null) {
            return;
        }
        try {
            Object cachedBeatmapSet = istream.readObject();
            if (cachedBeatmapSet instanceof BeatMapSet) {
                BeatMapSet beatMapSet = (BeatMapSet) cachedBeatmapSet;
                if (isBeatmapSetValid(beatMapSet)) {
                    beatMapSets.add(beatMapSet);
                    tempCachedBeatmaps.add(beatMapSet);
                    for (OsuFile osuFile: beatMapSet) {
                        osuFile.wasCachedInTheStart = true;
                        logBeatmapLoaded(osuFile);
                    }
                }
            }
        } catch (EOFException e) {
            System.out.println("Iterated through all maps saved in the db");
            try {
                istream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            finishCacheLoading();
        } catch (Exception e) {
            e.printStackTrace();
            clearCache();
        }
    }

    private void finishCacheLoading() {
        finishedLoadingCache = true;
        System.out.println("Cache was read completely, found " + tempCachedBeatmaps.size + " beatmapSets in cache");
    }

    private void deleteBeatmapFile(@NotNull File beatmapFile) {
        if (beatmapFile.delete()) {
            System.out.println("Deleted the beatmap to save resources");
        } else {
            System.out.println("Couldn't deleted beatmap to save resources");
        }
    }

    private void saveBeatmapSetInCache(@NotNull BeatMapSet beatMapSet) {
        if (ostream != null) {
            try {
                ostream.writeObject(beatMapSet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean verifyIfMapIsLoadedInCache(BeatMapSet beatMapSet, FileHandle beatmapFile, boolean initialBeatmapLoading) {
        boolean isMapLoadedInCache = false;
        if (beatMapSet == null) {
            return false;
        }
        if (initialBeatmapLoading) {
            for (OsuFile file: beatMapSet) {
                if (file.wasCachedInTheStart) {
                    isMapLoadedInCache = true;
                    break;
                }
            }
        } else {
            for (File matchBeatmapFile : beatMapSet.beatmapFiles) {
                if (matchBeatmapFile.exists()) {
                    if (matchBeatmapFile.equals(beatmapFile.file())) {
                        isMapLoadedInCache = true;
                        break;
                    }
                } else {
                    deleteBeatmapFile(matchBeatmapFile);
                }
            }
        }
        return isMapLoadedInCache;
    }

    private void loadBeatmap(BeatMapSet beatMapSet, BeatMapSet cacheBeatmapSet, @NotNull FileHandle beatmapFile, boolean initialBeatmapLoading) {
        if (!beatmapFile.extension().equals("osu")) {
            return;
        }

        File javaBeatmapFile = beatmapFile.file();
        boolean isMapLoadedInCache = verifyIfMapIsLoadedInCache(cacheBeatmapSet, beatmapFile, initialBeatmapLoading);
        OsuFile beatMap = null;

        if (isMapLoadedInCache) {
            return;
        } else {
            if (ostream == null) {
                try {
                    ostream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(libCacheFile)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            libraryChanged = true;
        }

        try {
            beatMap = OsuReader.read(javaBeatmapFile, osuFileParser);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading beatmap: " + javaBeatmapFile.getPath());
            deleteBeatmapFile(javaBeatmapFile);
        }

        if (beatMap != null) {
            beatMapSet.add(beatMap);
            beatMapSet.beatmapFiles.add(javaBeatmapFile);
            logBeatmapLoaded(beatMap);
        }
    }

    private void logBeatmapLoaded(OsuFile beatMap) {
        try {
            System.out.println(
                    "Loaded new beatmap: "
                            + beatMap.getMetadata().getTitle()
                            + " [" + beatMap.getMetadata().getVersion() + "]");
        } catch (Exception ignore) {}
    }

    private BeatMapSet getSamePathBeatmapSetInCache(String beatmapSetFolderPath) {
        BeatMapSet cacheBeatMapSet = null;
        for (BeatMapSet cachedBeatmapSet: tempCachedBeatmaps) {
            if (cachedBeatmapSet.beatmapSetFolderPath.equals(beatmapSetFolderPath)) {
                cacheBeatMapSet = cachedBeatmapSet;
            }
        }
        return cacheBeatMapSet;
    }

    private void loadBeatmapSet(@NotNull FileHandle beatMapSetFolder) {
        if (!beatMapSetFolder.isDirectory()) {
            if (!specialFiles.contains(beatMapSetFolder.name(), false)) {
                beatMapSetFolder.delete();
            } else {
                System.out.println("Prevented file deletion!");
            }
            return;
        }

        FileHandle[] list = beatMapSetFolder.list();
        BeatMapSet samePathBeatmapSet = getSamePathBeatmapSetInCache(beatMapSetFolder.path());
        BeatMapSet beatMapSet = samePathBeatmapSet == null? new BeatMapSet(beatMapSetFolder) : samePathBeatmapSet;

        for (FileHandle file : list) {
           loadBeatmap(beatMapSet, samePathBeatmapSet, file, true);
        }

        addNewBeatmapSet(beatMapSet, samePathBeatmapSet, beatMapSetFolder);
    }

    public void addNewBeatmapSet(@NotNull BeatMapSet beatMapSet, BeatMapSet cacheBeatmapSet, FileHandle beatMapSetFolder) {
        if (!isBeatmapSetValid(beatMapSet)) {
            if (!specialFiles.contains(beatMapSetFolder.name(), false)) {
                beatMapSetFolder.delete();
            }
            return;
        }
        if (cacheBeatmapSet == null) {
            if (isBeatmapSetValid(beatMapSet)) {
                beatMapSets.add(beatMapSet);
                saveBeatmapSetInCache(beatMapSet);
                System.out.println("Added new beatmapSet: " + beatMapSet.folder.getName());
            }
        }
    }

    private void loadFirstSteps() {
        beatmapLoadingStarted = true;
    }

    private void onLibraryChange() {
        System.out.println("Changes to beatmap library detected!");
        beatmapStorePrefs.putInteger("VERSION", VERSION);
        for (BeatMapSet beatMapSet: tempCachedBeatmaps) {
            saveBeatmapSetInCache(beatMapSet);
        }
    }

    private void loadFinalSteps() {
        System.out.println("Starting library loading final steps");
        if (tempCachedBeatmaps.size != beatMapSets.size) {
            libraryChanged = true;
        }
        if (libraryChanged) {
            onLibraryChange();
        }
        tempCachedBeatmaps.clear();
        System.out.println(
                ((System.nanoTime() - beatmapStoreCreationTime) / 1e6) + "ms to load " +
                        beatMapSets.size + " beatmapSets"
        );
        finishedLoading = true;
        if (ostream != null) {
            try {
                ostream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadAllBeatmaps() {
        loadFirstSteps();
        for (FileHandle fileHandle : songsDir.list()) {
            loadBeatmapSet(fileHandle);
        }
        loadFinalSteps();
    }

    public void loadNextBeatMapSet() {
        loadFirstSteps();
        if (finishedLoading) {
            return;
        }
        if (beatMapSetFoldersIterator.hasNext()) {
            loadBeatmapSet(beatMapSetFoldersIterator.next());
        } else {
           loadFinalSteps();
        }
    }

    public boolean isFinishedLoading() {
        return finishedLoading;
    }

    public boolean isFinishedLoadingCache() {
        return finishedLoadingCache;
    }

    public boolean isBeatmapLoadingStarted() {
        return beatmapLoadingStarted;
    }
}

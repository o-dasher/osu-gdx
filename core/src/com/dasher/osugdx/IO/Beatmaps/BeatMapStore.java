package com.dasher.osugdx.IO.Beatmaps;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.IO.GameIO;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BeatMapStore {
    private final int VERSION = 3;
    private boolean finishedLoadingCache = false;
    private final Array<String> specialFiles = new Array<>();
    private final Array<BeatMapSet> tempCachedBeatmaps = new Array<>();
    private final Array<BeatMapSet> beatMapSets = new Array<>();
    private final FileHandle songsDir;
    private boolean libraryChanged = false;
    private final Long beatmapStoreCreationTime;
    private final File libCacheFile;
    private final Preferences beatmapStorePrefs;
    private boolean loadedAllBeatmaps = false;
    private ObjectOutputStream ostream;
    private ObjectInputStream istream;

    public BeatMapStore(@NotNull GameIO gameIO) {
        this.songsDir = gameIO.getSongsDir();
        String libCacheFilePath = songsDir.path() + "/.beatmap_cache.db";
        beatmapStorePrefs = Gdx.app.getPreferences(getClass().getSimpleName());
        libCacheFile = Gdx.files.external(libCacheFilePath).file();
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
            } catch (EOFException e) {
                System.out.println("Empty cache!");
                libraryChanged = true;
                finishCacheLoading();
            } catch (IOException e) {
                e.printStackTrace();
                libraryChanged = true;
                clearCache();
                finishCacheLoading();
            }
        } else {
            try {
                if (libCacheFile.createNewFile()) {
                    System.out.println("Created a new beatmap cache library successfully");
                    finishCacheLoading();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearCache() {
        libraryChanged = true;
        if (libCacheFile.exists()) {
            if (libCacheFile.delete()) {
                setupCaching();
                System.out.println("CLEARED LIBRARY CACHE!");
            } else {
                System.out.println("Failed to clear library cache");
            }
        }
    }

    private boolean isBeatmapSetValid(@NotNull BeatMapSet beatMapSet) {
        return !beatMapSet.isEmpty() && beatMapSet.folder.exists() && beatMapSet.folder.isDirectory();
    }

    public void loadCache() {
        System.out.println("Started reading cache");

        while (!isFinishedLoadingCache()) {
            loadNextCachedBeatmapSet();
        }

        if (!isFinishedLoadingCache()) {
            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            finishCacheLoading();
        }
    }

    public void loadNextCachedBeatmapSet() {
        if (istream == null) {
            return;
        }

        Object cachedBeatmapSet = null;
        try {
            cachedBeatmapSet = istream.readObject();
        } catch (EOFException e) {
            finishCacheLoading();
        } catch (IOException | ClassNotFoundException e) {
            clearCache();
            e.printStackTrace();
        }

        if (cachedBeatmapSet instanceof BeatMapSet) {
            BeatMapSet beatMapSet = (BeatMapSet) cachedBeatmapSet;
            if (isBeatmapSetValid(beatMapSet)) {
                beatMapSets.add(beatMapSet);
                tempCachedBeatmaps.add(beatMapSet);
                for (Koohii.Map map : beatMapSet) {
                    System.out.print("DB CACHE: ");
                    logBeatmapLoaded(map);
                }
            }
        }
    }

    private void finishCacheLoading() {
        finishedLoadingCache = true;
        System.out.println("Cache was read completely, found " + tempCachedBeatmaps.size + " beatmapSets in cache");
    }

    private void deleteBeatmapFile(@NotNull File beatmapFile) {
        if (beatmapFile.exists()) {
            if (beatmapFile.delete()) {
                System.out.println("Deleted the beatmap to save resources");
            } else {
                System.out.println("Couldn't deleted beatmap to save resources");
            }
        } else {
            libraryChanged = true;
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

    private boolean verifyIfMapIsLoadedInCache(BeatMapSet beatMapSet, FileHandle beatmapFile) {
        boolean isMapLoadedInCache = false;
        if (beatMapSet == null) {
            return false;
        }
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
        return isMapLoadedInCache;
    }

    private void loadBeatmap(BeatMapSet beatMapSet, BeatMapSet cacheBeatmapSet, @NotNull FileHandle beatmapFile) {
        if (!beatmapFile.extension().equals("osu")) {
            return;
        }

        File javaBeatmapFile = beatmapFile.file();
        boolean isMapLoadedInCache = verifyIfMapIsLoadedInCache(cacheBeatmapSet, beatmapFile);
        Koohii.Map beatMap = null;

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
            BufferedReader beatmapBR = null;
            try {
                beatmapBR = new BufferedReader(new InputStreamReader(new FileInputStream(javaBeatmapFile)));
                beatMap = new Koohii.Parser().map(beatmapBR);
            } catch (UnsupportedOperationException e) {
                System.out.println("Can't parse other beatmap modes...");
                beatmapBR.close();
                deleteBeatmapFile(javaBeatmapFile);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading beatmap: " + javaBeatmapFile.getPath());
                beatmapBR.close();
                deleteBeatmapFile(javaBeatmapFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (beatMap != null) {
            beatMapSet.add(beatMap);
            beatMapSet.beatmapFiles.add(javaBeatmapFile);
            logBeatmapLoaded(beatMap);
        }
    }

    private void logBeatmapLoaded(Koohii.Map beatMap) {
        try {
            System.out.println("Loaded new beatmap: " + beatMap.title + " [" + beatMap.version + "]");
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
           loadBeatmap(beatMapSet, samePathBeatmapSet, file);
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
        System.out.println("Started loading beatmaps...");
    }

    private void onLibraryChange() {
        System.out.println("Changes to beatmap library detected!");
        beatmapStorePrefs.putInteger("VERSION", VERSION);
        beatmapStorePrefs.flush();
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
        if (ostream != null) {
            try {
                ostream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadedAllBeatmaps = true;
    }

    public void loadAllBeatmaps() {
        loadFirstSteps();
        for (FileHandle file : songsDir.list()) {
            loadBeatmapSet(file);
        }
        loadFinalSteps();
    }

    public boolean isFinishedLoadingCache() {
        return finishedLoadingCache;
    }

    public boolean isLoadedAllBeatmaps() {
        return loadedAllBeatmaps;
    }
}

package com.dasher.osugdx.IO.Beatmaps;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.dasher.osugdx.IO.GameIO;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BeatMapStore {
    private final int VERSION = 8;
    private boolean finishedLoadingCache = false;
    private final Array<String> specialFiles = new Array<>();
    private final Array<BeatMapSet> tempCachedBeatmaps = new Array<>();
    private final Array<BeatMapSet> beatMapSets = new Array<>();
    private final FileHandle songsDir;
    private boolean libraryChanged = false;
    private final Long beatmapStoreCreationTime;
    private final FileHandle libCacheFile;
    private final Preferences beatmapStorePrefs;
    private boolean loadedAllBeatmaps = false;
    private final Json json;

    public BeatMapStore(@NotNull GameIO gameIO, Json json) {
        this.songsDir = gameIO.getSongsDir();
        this.json = json;
        beatmapStorePrefs = Gdx.app.getPreferences(getClass().getSimpleName());
        libCacheFile = Gdx.files.external(songsDir.path() + "/.beatmap_db.json");
        beatmapStoreCreationTime = System.nanoTime();
        specialFiles.add(libCacheFile.name());
        setupCaching();
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
        } else {
            libCacheFile.write(false);
            System.out.println("Created a new beatmap cache library successfully");
            finishCacheLoading();
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
            finishCacheLoading();
        }
    }

    private boolean isBeatmapSetValid(@NotNull BeatMapSet beatMapSet) {
        FileHandle folder = beatMapSet.getFolder();
        return !beatMapSet.beatmaps.isEmpty() && folder.exists() && folder.isDirectory();
    }

    @SuppressWarnings("unchecked")
    public void loadCache() {
        System.out.println("Started reading cache");

        Array<BeatMapSet> cachedSets = null;
        try {
            cachedSets = json.fromJson(Array.class, BeatMapSet.class, libCacheFile);
        } catch (Exception e) {
            e.printStackTrace();
            clearCache();
        }

        if (cachedSets != null) {
            beatMapSets.addAll(cachedSets);
            tempCachedBeatmaps.addAll(beatMapSets);
        }

        if (!isFinishedLoadingCache()) {
            finishCacheLoading();
        }
    }

    private void finishCacheLoading() {
        finishedLoadingCache = true;
        System.out.println("Cache was read completely, found " + tempCachedBeatmaps.size + " beatmapSets in cache");
    }

    private void deleteBeatmapFile(@NotNull FileHandle beatmapFile) {
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

    private boolean verifyIfMapIsLoadedInCache(BeatMapSet beatMapSet, FileHandle beatmapFile) {
        boolean isMapLoadedInCache = false;
        if (beatMapSet == null) {
            return false;
        }
        for (Koohii.Map map: beatMapSet.beatmaps) {
            FileHandle file = Gdx.files.external(map.beatmapFilePath);
            if (file.exists()) {
                if (file.equals(beatmapFile)) {
                    isMapLoadedInCache = true;
                    break;
                }
            } else {
                deleteBeatmapFile(file);
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
            libraryChanged = true;
        }

        try {
            BufferedReader beatmapBR = null;
            try {
                beatmapBR = new BufferedReader(new InputStreamReader(new FileInputStream(javaBeatmapFile)));
                beatMap = new Koohii.Parser().map(beatmapBR);
                beatMap.beatmapFilePath = beatmapFile.path();
                beatMap.freeResources();
            } catch (UnsupportedOperationException e) {
                System.out.println("Can't parse other beatmap modes...");
                if (beatmapBR != null) {
                    beatmapBR.close();
                }
                deleteBeatmapFile(beatmapFile);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading beatmap: " + javaBeatmapFile.getPath());
                if (beatmapBR != null) {
                    beatmapBR.close();
                }
                deleteBeatmapFile(beatmapFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (beatMap != null) {
            beatMapSet.beatmaps.add(beatMap);
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
                System.out.println("Added new beatmapSet: " + beatMapSet.getFolder().name());
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
        libCacheFile.writeString(json.prettyPrint(beatMapSets), false);
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

package com.dasher.osugdx.IO.Beatmaps;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.dasher.osugdx.IO.GameIO;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
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
    private final List<String> specialFiles = new LinkedList<>();
    private List<BeatMapSet> tempCachedBeatmaps = new LinkedList<>();
    private List<BeatMapSet> beatMapSets = new LinkedList<>();
    private boolean beatmapLoadingStarted = false;
    private final FileHandle songsDir;
    private final Iterator<FileHandle> beatMapSetFoldersIterator;
    private boolean finishedLoading = false;
    private final OsuFileParser osuFileParser;
    private boolean libraryChanged = false;
    private final Long beatmapStoreCreationTime;
    private final File libCacheFile;
    private final Preferences beatmapStorePrefs;

    public BeatMapStore(@NotNull GameIO gameIO) {
        this.songsDir = gameIO.getSongsDir();
        String libCacheFilePath = songsDir.path() + "/.beatmap_cache.db";
        beatmapStorePrefs = Gdx.app.getPreferences(getClass().getSimpleName());
        libCacheFile = gameIO.getJavaFile(libCacheFilePath);
        beatMapSetFoldersIterator = new LinkedList<>(Arrays.asList(songsDir.list())).iterator();
        osuFileParser = new StandardOsuFileParser();
        beatmapStoreCreationTime = System.nanoTime();
        CompletableFuture.runAsync(this::setupCaching).whenComplete((res, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            }
            finishCacheLoading();
        });
        specialFiles.add(libCacheFile.getName());
    }

    private void setupCaching() {
        if (libCacheFile.exists()) {
            try {
                loadCache();
            } catch (Exception e) {
                e.printStackTrace();
                clearCache();
            }
        } else {
            System.out.println("Library db doesn't exist!");
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
                libraryChanged = true;
                System.out.println("CLEARED LIBRARY CACHE!");
            } else {
                System.out.println("Failed to clear library cache");
            }
        }
    }

    private void loadCache() throws IOException, ClassNotFoundException {
        System.out.println("Started reading cache");

        int version = beatmapStorePrefs.getInteger("VERSION");
        if (version != VERSION) {
            System.out.println("Outdated library, clearing cache and reloading");
            clearCache();
            return;
        }

        ObjectInputStream istream;
        try {
            istream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(libCacheFile)));
        } catch (EOFException e) {
            e.printStackTrace();
            libraryChanged = true;
            clearCache();
            return;
        }

        while (true) {
            try {
                Object cachedBeatmapSet = istream.readObject();
                if (cachedBeatmapSet instanceof BeatMapSet) {
                    BeatMapSet beatMapSet = (BeatMapSet) cachedBeatmapSet;
                    beatMapSets.add(beatMapSet);
                    tempCachedBeatmaps.add(beatMapSet);
                }
            } catch (EOFException e) {
                System.out.println("Iterated through all maps saved in the db");
                break;
            }
        }

        istream.close();
    }

    private void finishCacheLoading() {
        finishedLoadingCache = true;
        System.out.println("Cache was read completely, found " + tempCachedBeatmaps.size() + " beatmapSets in cache");
    }

    private void saveToCache() {
        System.out.println("Saving cache");
        beatmapStorePrefs.putInteger("VERSION", VERSION);
        try {
            ObjectOutputStream ostream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(libCacheFile)));
            for (BeatMapSet beatMapSet: beatMapSets) {
                ostream.writeObject(beatMapSet);
            }
            ostream.close();
            beatmapStorePrefs.flush();
            System.out.println("Saved cache successfully");
        } catch (IOException e) {
            System.out.println("Failed to save cache");
            e.printStackTrace();
        }
    }

    private void deleteBeatmapFile(@NotNull File beatmapFile) {
        if (beatmapFile.delete()) {
            System.out.println("Deleted the beatmap to save resources");
        } else {
            System.out.println("Couldn't deleted beatmap to save resources");
        }
    }

    private void loadBeatmap(BeatMapSet beatMapSet, BeatMapSet cacheBeatmapSet, @NotNull FileHandle beatmapFile) {
        if (!beatmapFile.extension().equals("osu")) {
            return;
        }

        File javaBeatmapFile = new File(Gdx.files.getExternalStoragePath() + "/" + beatmapFile.path());
        boolean isMapLoadedInCache = false;
        OsuFile beatMap = null;

        if (cacheBeatmapSet != null) {
            for (File matchBeatmapFile : cacheBeatmapSet.beatmapFiles) {
                if (matchBeatmapFile.exists()) {
                    if (matchBeatmapFile.equals(javaBeatmapFile)) {
                        isMapLoadedInCache = true;
                        break;
                    }
                } else {
                    deleteBeatmapFile(matchBeatmapFile);
                }
            }
        }

        if (isMapLoadedInCache) {
            return;
        } else {
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
            if (!specialFiles.contains(beatMapSetFolder.name())) {
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
        if (beatMapSet.isEmpty()) {
            beatMapSetFolder.delete();
        } else {
            if (cacheBeatmapSet == null) {
                beatMapSets.add(beatMapSet);
            }
            System.out.println("Added new beatmapSet: " + beatMapSet.get(0).getMetadata().getTitle());
        }
    }

    private void loadFirstSteps() {
        beatmapLoadingStarted = true;
    }

    private void verifyDeletedCacheBeatmapSets() {
        for (BeatMapSet cachedMapSet: tempCachedBeatmaps) {
            if (!cachedMapSet.folder.exists()) {
                for (BeatMapSet beatMapSet: beatMapSets) {
                    if (cachedMapSet.beatmapSetFolderPath.equals(beatMapSet.beatmapSetFolderPath)) {
                        beatMapSet.clear();
                    }
                }
                libraryChanged = true;
            }
        }
    }

    private void loadFinalSteps() {
        System.out.println("Starting library loading final steps");
        verifyDeletedCacheBeatmapSets();
        if (tempCachedBeatmaps.size() != beatMapSets.size()) {
            libraryChanged = true;
        }
        tempCachedBeatmaps.clear();
        beatMapSets = beatMapSets.stream()
                .filter(beatmapSet -> !beatmapSet.isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));
        if (libraryChanged) {
            System.out.println("Changes to beatmap library detected!");
            saveToCache();
        }
        System.out.println(
                ((System.nanoTime() - beatmapStoreCreationTime) / 1e6) + "ms to load " +
                        beatMapSets.size() + " beatmapSets"
        );
        finishedLoading = true;
    }

    public void loadAllBeatmaps(boolean parallel) {
        loadFirstSteps();

        if (parallel) {
            Arrays.stream(songsDir.list()).parallel().forEach(this::loadBeatmapSet);
        } else {
            for (FileHandle fileHandle : songsDir.list()) {
                loadBeatmapSet(fileHandle);
            }
        }

        loadFinalSteps();
    }

    public void loadNextBeatMapSet() {
        loadFirstSteps();
        if (finishedLoading) {
            return;
        }
        BeatMapSet currentBeatmapSet;
        if (beatMapSetFoldersIterator.hasNext()) {
            loadBeatmapSet(beatMapSetFoldersIterator.next());
        } else {
           loadFinalSteps();
        }
    }

    private BeatMapSet currentBeatmapSet = null;
    private BeatMapSet currentCachedBeatmapSet = null;
    private FileHandle currentBeatmapSetFolder = null;
    private Iterator<FileHandle> currentBeatmapSetFilesIterator = null;

    public void loadNextBeatMapSetGradually() {
        loadFirstSteps();
        if (finishedLoading) {
            return;
        }
        if (currentBeatmapSetFilesIterator == null) {
            if (beatMapSetFoldersIterator.hasNext()) {
                FileHandle beatmapSetFolder = beatMapSetFoldersIterator.next();
                System.out.println(beatmapSetFolder.name());
                currentBeatmapSet = new BeatMapSet(beatmapSetFolder);
                currentBeatmapSetFolder = beatmapSetFolder;
                currentCachedBeatmapSet = getSamePathBeatmapSetInCache(currentBeatmapSetFolder.path());
                currentBeatmapSetFilesIterator = Arrays.asList(beatmapSetFolder.list()).iterator();
            } else {
                loadFinalSteps();
            }
        } else {
            if (currentBeatmapSetFilesIterator.hasNext()) {
                FileHandle nextBeatmapFile = currentBeatmapSetFilesIterator.next();
                loadBeatmap(currentBeatmapSet, currentCachedBeatmapSet, nextBeatmapFile);
            } else {
                addNewBeatmapSet(currentBeatmapSet, currentCachedBeatmapSet, currentBeatmapSetFolder);
                currentBeatmapSet = null;
                currentCachedBeatmapSet = null;
                currentBeatmapSetFilesIterator = null;
            }
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

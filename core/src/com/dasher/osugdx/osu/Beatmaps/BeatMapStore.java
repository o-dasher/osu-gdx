package com.dasher.osugdx.osu.Beatmaps;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.osu.Mods.ModManagerListener;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.Objects;


import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public class BeatMapStore implements ModManagerListener {
    private final int VERSION = 63;
    private final String versionKey = "VERSION";
    private final Array<String> specialFiles = new Array<>();
    private final Array<BeatMapSet> tempCachedBeatmaps = new Array<>();
    private final Array<BeatMapSet> beatMapSets = new Array<>();
    private final FileHandle songsDir;
    private final Long beatmapStoreCreationTime;
    private final FileHandle libCacheFile;
    private final Preferences beatmapStorePrefs;
    private OSZParser oszParser;
    private boolean finishedLoadingCache = false;
    private boolean loadedAllBeatmaps = false;
    private final OsuGame game;
    private BeatMapSet mainDefaultBeatmapSet;

    public BeatMapStore(@NotNull OsuGame game) {
        this.songsDir = game.gameIO.getSongsDir();
        this.game = game;
        beatmapStorePrefs = Gdx.app.getPreferences(getClass().getSimpleName());
        libCacheFile = Gdx.files.local(".beatmap_db");
        beatmapStoreCreationTime = System.nanoTime();
        specialFiles.add(libCacheFile.name());
        setupCaching();
    }

    public void setOszParser(OSZParser oszParser) {
        this.oszParser = oszParser;
    }

    private void verifyVersion() {
        int version = beatmapStorePrefs.getInteger(versionKey, VERSION);
        if (version != VERSION) {
            System.out.println("Outdated library, clearing cache and reloading");
            clearCache();
        }
    }

    private void setupCaching() {
        System.out.println("Library db " + (libCacheFile.exists() ? "does" : "doesn't") + " exists!");
        if (libCacheFile.exists()) {
            verifyVersion();
        } else {
            libCacheFile.write(false);
            System.out.println("Created a new beatmap cache library successfully");
            finishCacheLoading();
        }
    }

    private void clearCache() {
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
    public Array<BeatMapSet> getCache() {
        Array<BeatMapSet> cachedSets = new Array<>();
        if (libCacheFile.exists()) {
            try {
                cachedSets = game.json.fromJson(
                        Array.class, BeatMapSet.class, libCacheFile.read()
                );
            } catch (Exception e) {
                e.printStackTrace();
                clearCache();
            }
        } else {
            clearCache();
        }
        return cachedSets;
    }

    public void loadCache() {
        if (isFinishedLoadingCache()) {
            return;
        }

        System.out.println("Started reading cache");

        Array<BeatMapSet> cachedSets = getCache();
        Array<BeatMapSet> invalidSets = new Array<>();
        Array<Beatmap> invalidBeatmaps = new Array<>();
        for (BeatMapSet cachedSet: cachedSets) {
            for (Beatmap beatmap: cachedSet.beatmaps) {
                FileHandle file = Gdx.files.external(beatmap.beatmapFilePath);
                if (!file.exists()) {
                    invalidBeatmaps.add(beatmap);
                }
            }
            for (Beatmap beatmap: invalidBeatmaps) {
                cachedSet.beatmaps.removeValue(beatmap, true);
            }
            invalidBeatmaps.clear();
            if (cachedSet.beatmaps.isEmpty()) {
                invalidSets.add(cachedSet);
            }
        }
        for (BeatMapSet beatMapSet: invalidSets) {
            cachedSets.removeValue(beatMapSet, true);
        }
        beatMapSets.addAll(cachedSets);
        for (BeatMapSet beatMapSet: beatMapSets) {
            if (!isBeatmapSetValid(beatMapSet)) {
                deleteBeatmapFile(beatMapSet, null);
                beatMapSets.removeValue(beatMapSet, true);
            }
        }
        tempCachedBeatmaps.addAll(beatMapSets);
        boolean logBeatmaps = true;
        if (logBeatmaps) {
            for (BeatMapSet beatMapSet : tempCachedBeatmaps) {
                for (Beatmap map : beatMapSet.beatmaps) {
                    System.out.print("CACHE DB: ");
                    logBeatmapLoaded(map);
                }
                System.out.print("CACHE DB: ");
                System.out.println(logBeatmapSet(beatMapSet));
            }
        }

        if (!isFinishedLoadingCache()) {
            finishCacheLoading();
        }
    }

    private void finishCacheLoading() {
        finishedLoadingCache = true;
        System.out.println("Cache was read completely, found " + tempCachedBeatmaps.size + " beatmapSets in cache");
    }

    protected void deleteBeatmapFile(@Null BeatMapSet beatMapSet, @Null FileHandle beatmapFile) {
        if (beatmapFile != null && beatmapFile.exists()) {
            if (beatmapFile.delete()) {
                for (BeatMapSet tempSet: beatMapSets) {
                    if (removeInvalidBeatmapSet(tempSet, beatmapFile.path())) {
                        System.out.println("Deleted the beatmap to save resources");
                        break;
                    }
                }
            } else {
                System.out.println("Couldn't deleted beatmap to save resources");
            }
        }

        if (beatMapSet != null) {
            removeInvalidBeatmapSet(beatMapSet, beatmapFile == null? null : beatmapFile.path());
        }

        if (loadedAllBeatmaps) {
            saveCache();
        }
    }

    private boolean removeInvalidBeatmapSet(@NotNull BeatMapSet beatMapSet, @Null String invalidPath) {
        for (Beatmap beatmap: beatMapSet.beatmaps) {
            if (beatmap.beatmapFilePath.equals(invalidPath)) {
                beatMapSet.beatmaps.removeValue(beatmap, true);
                return true;
            }
        }
        if (beatMapSet.beatmaps.isEmpty() || invalidPath == null) {
            Gdx.files.external(beatMapSet.beatmapSetFolderPath).delete();
            beatMapSets.removeValue(beatMapSet, true);
        }
        return false;
    }

    private boolean verifyIfMapIsLoadedInCache(BeatMapSet beatMapSet, FileHandle beatmapFile) {
        boolean isMapLoadedInCache = false;
        if (beatMapSet == null) {
            return false;
        }
        for (Beatmap map : beatMapSet.beatmaps) {
            FileHandle file = Gdx.files.external(map.beatmapFilePath);
            if (file.exists()) {
                if (file.equals(beatmapFile)) {
                    isMapLoadedInCache = true;
                    break;
                }
            } else {
                deleteBeatmapFile(beatMapSet, file);
            }
        }
        return isMapLoadedInCache;
    }

    private void loadBeatmap(BeatMapSet beatMapSet, BeatMapSet cacheBeatmapSet, @NotNull FileHandle beatmapFile) {
        if (!beatmapFile.extension().equals("osu")) {
            return;
        }

        boolean isMapLoadedInCache = verifyIfMapIsLoadedInCache(cacheBeatmapSet, beatmapFile);

        if (isMapLoadedInCache) {
            return;
        } else {
            System.out.println("Library changed, map not found in cache!");
        }

        Beatmap beatMap = game.beatmapUtils.createMap(
                beatmapFile,
                true, true, true,
                true, true, true
        );

        if (beatMap == null) {
            deleteBeatmapFile(null, beatmapFile);
        } else {
            if (beatMap.getGamemode() != GameMode.OSU) {
                System.out.println("Found beatmap with wrong game mode deleting it...");
                deleteBeatmapFile(null, beatmapFile);
                return;
            }
            beatMap.freeResources();
            beatMapSet.beatmaps.add(beatMap);
            logBeatmapLoaded(beatMap);
        }
    }

    private void logBeatmapLoaded(@NotNull Beatmap beatMap) {
        System.out.println("Loaded new beatmap: " + beatMap.toString());
    }

    private BeatMapSet getSamePathBeatmapSetInCache(String beatmapSetFolderPath) {
        BeatMapSet cacheBeatMapSet = null;
        for (BeatMapSet cachedBeatmapSet : tempCachedBeatmaps) {
            if (cachedBeatmapSet.beatmapSetFolderPath.equals(beatmapSetFolderPath)) {
                cacheBeatMapSet = cachedBeatmapSet;
                break;
            }
        }
        return cacheBeatMapSet;
    }

    public BeatMapSet loadBeatmapSet(@NotNull FileHandle beatMapSetFolder) {
        if (!beatMapSetFolder.isDirectory()) {
            if (!specialFiles.contains(beatMapSetFolder.name(), false)) {
                beatMapSetFolder.delete();
            } else {
                System.out.println("Prevented file deletion!");
            }
            return null;
        }

        BeatMapSet samePathBeatmapSet = getSamePathBeatmapSetInCache(beatMapSetFolder.path());
        BeatMapSet beatMapSet = samePathBeatmapSet == null ? new BeatMapSet(beatMapSetFolder) : samePathBeatmapSet;

        for (FileHandle file : beatMapSetFolder.list(pathname -> pathname.getName().endsWith("osu"))) {
            loadBeatmap(beatMapSet, samePathBeatmapSet, file);
        }

        addNewBeatmapSet(beatMapSet, samePathBeatmapSet, beatMapSetFolder);
        if (loadedAllBeatmaps) {
            saveCache();
        }

        return beatMapSet;
    }

    public void addNewBeatmapSet(@NotNull BeatMapSet beatMapSet, BeatMapSet cacheBeatmapSet, FileHandle beatMapSetFolder) {
        if (!isBeatmapSetValid(beatMapSet)) {
            if (!specialFiles.contains(beatMapSetFolder.name(), false)) {
                beatMapSetFolder.delete();
            }
            return;
        }
        if (cacheBeatmapSet == null && isBeatmapSetValid(beatMapSet)) {
            beatMapSets.add(beatMapSet);
            System.out.println(logBeatmapSet(beatMapSet));
        }
    }

    @Contract(pure = true)
    private @NotNull String logBeatmapSet(@NotNull BeatMapSet beatMapSet) {
        return "New BeatmapSet: " + beatMapSet.beatmapSetFolderPath;
    }

    private boolean isSavingCache = false;

    public void saveCache() {
        isSavingCache = true;
        beatmapStorePrefs.putInteger(versionKey, VERSION);
        beatmapStorePrefs.flush();
        game.modManager.calculateBeatmaps(Mods.NOMOD);
    }

    public void loadAllBeatmaps() {
        System.out.println("Started loading beatmaps...");

        String baseTracksPath = "Tracks/";
        String welcomeOSZ = baseTracksPath + "welcome.osz";
        String circlesOSZ = baseTracksPath + "circles.osz";
        String trianglesOSZ = baseTracksPath + "triangles.osz";

        StringBuilder mainTrackStringSB = new StringBuilder();
        char[] selectedMainTrack = welcomeOSZ.toCharArray();

        for (char character: selectedMainTrack) {
            mainTrackStringSB.append(character);
        }

        String mainTrackString = mainTrackStringSB.toString();
        FileHandle mainDefaultTrack = Gdx.files.internal(mainTrackString);
        String mainDefaultTrackPath = oszParser.parseOSZ(mainDefaultTrack).path();

        Array<String> notMainTracks = new Array<>();
        notMainTracks.add(welcomeOSZ);
        notMainTracks.add(circlesOSZ);
        notMainTracks.add(trianglesOSZ);

        for (String track: notMainTracks) {
            if (track.toUpperCase().equals(mainTrackString)) {
                notMainTracks.removeValue(track, true);
            }
        }

        FileHandle[] validFiles = songsDir.list(
                pathname ->
                pathname.isDirectory() &&
                Objects.requireNonNull(pathname.list((dir, name) -> name.endsWith("osu"))).length != 0);

        for (FileHandle validFile : validFiles) {
            BeatMapSet beatMapSet = loadBeatmapSet(validFile);
            if (validFile.path().equals(mainDefaultTrackPath)) {
                mainDefaultBeatmapSet = beatMapSet;
            }
        }

        if (mainDefaultBeatmapSet == null) {
            System.out.println("Installing default maps!");
            for (String track: notMainTracks) {
                oszParser.parseOSZ(Gdx.files.internal(track));
            }
        }

        if (tempCachedBeatmaps.size != beatMapSets.size) {
            System.out.println("Cached beatmaps size doesn't match beatmapSets size!");
        }
        saveCache();
        tempCachedBeatmaps.clear();
        double loadTime = ((System.nanoTime() - beatmapStoreCreationTime) / 1e6);
        System.out.println("Loaded " + beatMapSets.size + " BeatmapSets in " + loadTime + "ms");
        loadedAllBeatmaps = true;
    }

    public boolean isFinishedLoadingCache() {
        return finishedLoadingCache;
    }

    public boolean isLoadedAllBeatmaps() {
        return loadedAllBeatmaps;
    }

    public Array<BeatMapSet> getBeatMapSets() {
        return beatMapSets;
    }

    public BeatMapSet getMainDefaultBeatmapSet() {
        return mainDefaultBeatmapSet;
    }

    @Override
    public void onBeatmapCalculated(Beatmap beatmap) {

    }

    @Override
    public void onCompleteCalculation() {
        if (isSavingCache) {
            Array<BeatMapSet> array = new Array<>(beatMapSets);
            for (int i = 0; i < array.size; i++) {
                BeatMapSet beatmapSet = array.get(i);
                for (Beatmap beatmap: beatmapSet.beatmaps) {
                    Beatmap copy = game.beatmapManager.reInitBeatmap(beatmap, false);
                    copy.setBaseStars(0);
                    copy.freeResources();
                }
            }
            libCacheFile.writeString(game.json.toJson(array), false);
            isSavingCache = false;
            System.out.println("Saved beatmap cache successfully");
        }
    }
}
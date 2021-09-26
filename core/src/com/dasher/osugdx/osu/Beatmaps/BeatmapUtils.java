package com.dasher.osugdx.osu.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTextureListener;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTextureManager;
import com.dasher.osugdx.IO.IOHelper;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.io.InputStream;
import java.util.NoSuchElementException;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;
import lt.ekgame.beatmap_analyzer.parser.BeatmapException;
import lt.ekgame.beatmap_analyzer.parser.BeatmapParser;

public class BeatmapUtils {
    private final BeatmapParser beatmapParser = new BeatmapParser();
    private final ReusableTextureManager reusableTextureManager = new ReusableTextureManager();
    private final TextureLoader.TextureParameter textureParameter;
    private com.dasher.osugdx.osu.Beatmaps.BeatMapStore beatMapStore;

    public BeatmapUtils() {
        textureParameter = new TextureLoader.TextureParameter();
        textureParameter.genMipMaps = true;
        textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
    }

    public Beatmap createMap(@NotNull Beatmap beatmap) {
        return createMap(Gdx.files.external(beatmap.beatmapFilePath), true, true, true, true, true, true);
    }

    public Beatmap createMap(
            @NotNull FileHandle mapFile,
            boolean parseGeneral, boolean parseObjects, boolean parseTimingPoints,
            boolean parseEditor, boolean parseDifficulties, boolean parseMetadata
    ) {
        InputStream reader = mapFile.read();
        Beatmap beatmap = null;

        try {
            beatmap = beatmapParser.parse(
                    reader, parseGeneral, parseObjects, parseTimingPoints,
                    parseEditor, parseDifficulties, parseMetadata
            );
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return beatmap;
        } catch (BeatmapException e) {
            beatMapStore.deleteBeatmapFile(null, mapFile);
            e.printStackTrace();
        }

        if (beatmap != null) {
            beatmap.beatmapFilePath = mapFile.path();
        }

        IOHelper.close(reader);

        return beatmap;
    }

    public void setBeatMapStore(BeatMapStore beatMapStore) {
        this.beatMapStore = beatMapStore;
    }

    public @Nullable Texture getBackground(@NotNull Beatmap beatmap, ReusableTextureListener listener) {
        for (BreakPeriod breakPeriod: beatmap.getBreaks()) {
            if (breakPeriod.isBackground()) {
                FileHandle beatmapFile = Gdx.files.external(beatmap.beatmapFilePath);
                String bgFileName = breakPeriod.getBackgroundFileName();
                FileHandle bgFile = Gdx.files.external(beatmapFile.parent() + "/" + bgFileName);
                if (bgFile.exists()) {
                    return reusableTextureManager.getTexture(bgFile, textureParameter, listener);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}

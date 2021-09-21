package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTexture;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTextureListener;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTextureManager;
import com.dasher.osugdx.IO.IOHelper;
import com.github.francesco149.koohii.Koohii;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;
import lt.ekgame.beatmap_analyzer.parser.BeatmapException;
import lt.ekgame.beatmap_analyzer.parser.BeatmapParser;

public class BeatmapUtils {
    private final BeatmapParser beatmapParser = new BeatmapParser();
    private final ReusableTextureManager reusableTextureManager = new ReusableTextureManager();
    private BeatMapStore beatMapStore;

    public Beatmap createMap(@NotNull FileHandle mapFile) {
        InputStream reader = mapFile.read();
        Beatmap beatmap = null;

        try {
            beatmap = beatmapParser.parse(reader);
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
                ReusableTexture texture = bgFile.exists()?
                        reusableTextureManager.getTexture(bgFile, true, listener) : null;
                if (texture == null) {
                    return null;
                } else {
                    texture.setFilter(
                            Texture.TextureFilter.MipMapLinearLinear,
                            Texture.TextureFilter.MipMapLinearLinear
                    );
                    return texture;
                }
            }
        }
        return null;
    }
}

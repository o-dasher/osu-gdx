package com.dasher.osugdx.assets;

import com.badlogic.gdx.audio.Music;

import org.jetbrains.annotations.NotNull;

public class MusicHolder extends com.dasher.osugdx.assets.AssetHolder<Music> {

    public MusicHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.musicsPath);
    }

}

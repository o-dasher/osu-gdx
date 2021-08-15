package com.dasher.osugdx.assets;

import com.badlogic.gdx.audio.Sound;

import org.jetbrains.annotations.NotNull;

public class SoundsHolder extends AssetHolder<Sound> {
    public SoundsHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.soundsPath);
    }
}

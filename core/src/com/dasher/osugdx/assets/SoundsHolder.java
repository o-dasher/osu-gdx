package com.dasher.osugdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;

import org.jetbrains.annotations.NotNull;

public class SoundsHolder extends AssetHolder<Sound> {
    public AssetDescriptor<Sound> welcome;
    public AssetDescriptor<Sound> seeya;

    public SoundsHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.soundsPath.path());
        welcome = addAsset(assetPaths.soundsPath.intro + "welcome.mp3", Sound.class);
        seeya = addAsset(assetPaths.soundsPath.intro + "seeya.mp3", Sound.class);
    }
}

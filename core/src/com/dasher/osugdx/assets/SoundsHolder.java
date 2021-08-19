package com.dasher.osugdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;

import org.jetbrains.annotations.NotNull;

public class SoundsHolder extends AssetHolder<Sound> {
    // Intro
    public AssetDescriptor<Sound> welcome;
    public AssetDescriptor<Sound> seeya;

    // Menu
    public AssetDescriptor<Sound> buttonBackSelect;
    public AssetDescriptor<Sound> buttonDirectSelect;
    public AssetDescriptor<Sound> buttonEditSelect;
    public AssetDescriptor<Sound> buttonGenericSelect;
    public AssetDescriptor<Sound> buttonHover;
    public AssetDescriptor<Sound> buttonPlaySelect;
    public AssetDescriptor<Sound> buttonSoloSelect;
    public AssetDescriptor<Sound> osuLogoDownBeat;
    public AssetDescriptor<Sound> osuLogoHeartBeat;
    public AssetDescriptor<Sound> osuLogoSelect;


    public SoundsHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.soundsPath.path());
        welcome = addAsset(assetPaths.soundsPath.intro + "welcome.mp3", Sound.class);
        seeya = addAsset(assetPaths.soundsPath.intro + "seeya.mp3", Sound.class);
        buttonBackSelect = addAsset(assetPaths.soundsPath.menu + "button-back-select.wav", Sound.class);
        buttonDirectSelect = addAsset(assetPaths.soundsPath.menu + "button-direct-select.wav", Sound.class);
        buttonEditSelect= addAsset(assetPaths.soundsPath.menu + "button-edit-select.wav", Sound.class);
        buttonGenericSelect = addAsset(assetPaths.soundsPath.menu + "button-generic-select.wav", Sound.class);
        buttonHover = addAsset(assetPaths.soundsPath.menu + "button-hover.wav", Sound.class);
        buttonPlaySelect = addAsset(assetPaths.soundsPath.menu + "button-play-select.wav", Sound.class);
        buttonSoloSelect = addAsset(assetPaths.soundsPath.menu + "button-solo-select.wav", Sound.class);
        osuLogoDownBeat = addAsset(assetPaths.soundsPath.menu + "osu-logo-downbeat.wav", Sound.class);
        osuLogoHeartBeat = addAsset(assetPaths.soundsPath.menu + "osu-logo-heartbeat.wav", Sound.class);
        osuLogoSelect = addAsset(assetPaths.soundsPath.menu + "osu-logo-select.wav", Sound.class);
    }
}

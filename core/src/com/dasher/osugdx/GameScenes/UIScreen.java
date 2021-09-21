package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.audio.Sound;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public abstract class UIScreen extends GameScreen {
    protected Sound buttonBackSelect;
    protected Sound buttonDirectSelect;
    protected Sound buttonEditSelect;
    protected Sound buttonGenericSelect;
    protected Sound buttonPlaySelect;
    protected Sound buttonSoloSelect;
    protected Sound osuLogoDownBeat;
    protected Sound osuLogoHeartBeat;
    protected Sound osuLogoSelect;

    public UIScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void show() {
        super.show();
        buttonBackSelect = assetManager.get(assetManager.sounds.buttonBackSelect);
        buttonDirectSelect = assetManager.get(assetManager.sounds.buttonDirectSelect);
        buttonEditSelect = assetManager.get(assetManager.sounds.buttonEditSelect);
        buttonGenericSelect = assetManager.get(assetManager.sounds.buttonGenericSelect);
        buttonPlaySelect = assetManager.get(assetManager.sounds.buttonPlaySelect);
        buttonSoloSelect = assetManager.get(assetManager.sounds.buttonSoloSelect);
        osuLogoDownBeat = assetManager.get(assetManager.sounds.osuLogoDownBeat);
        osuLogoHeartBeat = assetManager.get(assetManager.sounds.osuLogoHeartBeat);
        osuLogoSelect = assetManager.get(assetManager.sounds.osuLogoSelect);
    }
}

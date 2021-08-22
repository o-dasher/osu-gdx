package com.dasher.osugdx.GameScenes.MainMenu;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.Framework.Stages.SwitcherStage;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.MenuLogo;
import com.dasher.osugdx.GameScenes.ScreenWithBackgroundMusic;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class MenuScreen extends UIScreen implements ScreenWithBackgroundMusic {
    private MenuLogo menuLogo;
    private MenuLogo logoOverlay;
    private SwitcherStage menuStage;

    public MenuScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Texture logoTexture = assetManager.get(assetManager.textures.logo);
        Sound heartBeat = assetManager.get(assetManager.sounds.osuLogoHeartBeat);
        Sound downBeat = assetManager.get(assetManager.sounds.osuLogoDownBeat);
        Sound logoSelect = assetManager.get(assetManager.sounds.osuLogoSelect);
        Sound hover = assetManager.get(assetManager.sounds.buttonHover);

        menuLogo = new MenuLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);
        logoOverlay = new MenuLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);
        logoOverlay.setAlpha(0.1f);
        logoOverlay.setScale(logoOverlay.getScaleX() * 0.95f);
        menuStage = new SwitcherStage(game, viewport, true);

        menuStage.addActor(logoOverlay);
        menuStage.addActor(menuLogo);

        beatmapManager.startMusicPlaying();
        beatFactory.addListener(menuLogo);

        inputMultiplexer.addProcessor(menuStage);
    }

    @Override
    public void render(float delta) {
        backgroundStage.act(delta);
        backgroundStage.draw();
        menuLogo.colorLayer();
        logoOverlay.colorLayer();
        logoOverlay.generateTriangles(delta);
        menuLogo.generateTriangles(delta);
        menuStage.act(delta);
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        show();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        beatFactory.removeListener(menuLogo);
    }

    @Override
    public void dispose() {
        //menuStage.dispose();
    }
}

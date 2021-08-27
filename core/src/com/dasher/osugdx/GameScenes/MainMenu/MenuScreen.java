package com.dasher.osugdx.GameScenes.MainMenu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Stages.SwitcherStage;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.LogoButton;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.MainLogo;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.OverlayLogo;
import com.dasher.osugdx.GameScenes.ScreenWithBackgroundMusic;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class MenuScreen extends UIScreen implements ScreenWithBackgroundMusic {
    private MainLogo menuLogo;
    private OverlayLogo logoOverlay;
    private SwitcherStage menuStage;
    private SwitcherStage buttonsStage;

    public MenuScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Texture logoTexture = assetManager.get(assetManager.textures.logo);
        Sound heartBeat = assetManager.get(assetManager.sounds.osuLogoHeartBeat);
        Sound downBeat = assetManager.get(assetManager.sounds.osuLogoDownBeat);
        Music logoSelect = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.osuLogoSelect.fileName));
        Sound hover = assetManager.get(assetManager.sounds.buttonHover);
        Texture playButtonTex = assetManager.get(assetManager.textures.playButton);
        Texture optionsButtonTex = assetManager.get(assetManager.textures.optionsButton);
        Texture exitButtonTex = assetManager.get(assetManager.textures.exitButton);

        menuLogo = new MainLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);
        logoOverlay = new OverlayLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);

        LogoButton playButton = new LogoButton(playButtonTex, menuLogo, 1);
        LogoButton optionsButton = new LogoButton(optionsButtonTex, menuLogo, 2);
        LogoButton exitButton = new LogoButton(exitButtonTex, menuLogo, 3);

        menuStage = new SwitcherStage(game, viewport, true);
        menuStage.addActor(logoOverlay);
        menuStage.addActor(menuLogo);

        buttonsStage = new SwitcherStage(game, viewport, true);
        buttonsStage.addActor(playButton);
        buttonsStage.addActor(optionsButton);
        buttonsStage.addActor(exitButton);

        beatmapManager.startMusicPlaying();
        beatFactory.addListener(menuLogo);

        inputMultiplexer.addProcessor(menuStage);
        inputMultiplexer.addProcessor(buttonsStage);
    }

    @Override
    public void render(float delta) {
        viewport.apply(true);
        backgroundStage.act(delta);
        backgroundStage.draw();
        logoOverlay.setPosition(menuLogo.getX(), menuLogo.getY());
        buttonsStage.act(delta);
        buttonsStage.draw();
        menuLogo.colorLayer();
        menuStage.act(delta);
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        menuLogo.onResize();
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

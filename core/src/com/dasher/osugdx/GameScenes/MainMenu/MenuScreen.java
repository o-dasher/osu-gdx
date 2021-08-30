package com.dasher.osugdx.GameScenes.MainMenu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dasher.osugdx.Framework.Stages.FadingStage;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.*;
import com.dasher.osugdx.GameScenes.ScreenWithBackgroundMusic;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class MenuScreen extends UIScreen implements ScreenWithBackgroundMusic {
    private MainLogo menuLogo;
    private OverlayLogo logoOverlay;
    private Stage menuStage;
    private Stage buttonsStage;
    private final Texture logoTexture;
    private final Sound heartBeat;
    private final Sound downBeat;
    private final Sound hover;
    private final Texture playButtonTex;
    private final Texture optionsButtonTex;
    private final Texture exitButtonTex;
    private final Music logoSelect;


    public MenuScreen(@NotNull OsuGame game) {
        super(game);
        logoTexture = assetManager.get(assetManager.textures.logo);
        heartBeat = assetManager.get(assetManager.sounds.osuLogoHeartBeat);
        downBeat = assetManager.get(assetManager.sounds.osuLogoDownBeat);
        hover = assetManager.get(assetManager.sounds.buttonHover);
        playButtonTex = assetManager.get(assetManager.textures.playButton);
        optionsButtonTex = assetManager.get(assetManager.textures.optionsButton);
        exitButtonTex = assetManager.get(assetManager.textures.exitButton);
        logoSelect = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.osuLogoSelect.fileName));
    }

    @Override
    public void show() {
        super.show();

        menuLogo = new MainLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);
        logoOverlay = new OverlayLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);

        LogoButton playButton = new PlayButton(playButtonTex, menuLogo, 1);
        LogoButton optionsButton = new OptionButton(optionsButtonTex, menuLogo, 2);
        LogoButton exitButton = new ExitButton(exitButtonTex, menuLogo, 3);

        menuStage = new Stage(viewport);
        menuStage.addActor(menuLogo);

        buttonsStage = new Stage(viewport);
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

        // ACT
        backgroundStage.act(delta);
        buttonsStage.act(delta);
        menuStage.act(delta);
        logoOverlay.setPosition(menuLogo.getX(), menuLogo.getY());

        // DRAW
        backgroundStage.draw();
        buttonsStage.draw();
        menuLogo.colorLayer();
        menuStage.draw();
        batch.begin();
        logoOverlay.draw(batch, logoOverlay.getColor().a);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
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

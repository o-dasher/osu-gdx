package com.dasher.osugdx.GameScenes.MainMenu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.*;
import com.dasher.osugdx.GameScenes.ScreenWithBackgroundMusic;
import com.dasher.osugdx.GameScenes.SoundSelect.SoundSelectScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;
import org.jetbrains.annotations.NotNull;


public class MenuScreen extends UIScreen implements ScreenWithBackgroundMusic {
    private MainLogo menuLogo;
    private OverlayLogo logoOverlay;
    private Stage menuStage;
    private Stage overlayStage;
    private Stage buttonsStage;
    private final Texture logoTexture;
    private final Sound heartBeat;
    private final Sound downBeat;
    private final Sound hover;
    private final Sound logoSelect;
    private final Texture playButtonTex;
    private final Texture optionsButtonTex;
    private final Texture exitButtonTex;


    public MenuScreen(@NotNull OsuGame game) {
        super(game);
        logoTexture = assetManager.get(assetManager.textures.logo);
        heartBeat = game.audioFactory.newSound(assetManager.get(assetManager.sounds.osuLogoHeartBeat));
        downBeat = game.audioFactory.newSound(assetManager.get(assetManager.sounds.osuLogoDownBeat));
        hover = game.audioFactory.newSound(assetManager.get(assetManager.sounds.buttonHover));
        logoSelect = game.audioFactory.newSound(assetManager.get(assetManager.sounds.osuLogoSelect.fileName));
        playButtonTex = assetManager.get(assetManager.textures.playButton);
        optionsButtonTex = assetManager.get(assetManager.textures.optionsButton);
        exitButtonTex = assetManager.get(assetManager.textures.exitButton);
    }

    @Override
    public void show() {
        super.show();

        menuLogo = new MainLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);
        logoOverlay = new OverlayLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover, menuLogo);
        logoOverlay.setTouchable(Touchable.disabled);

        LogoButton playButton = new PlayButton(game, playButtonTex, menuLogo, hover, 0);
        LogoButton optionsButton = new OptionButton(game, optionsButtonTex, menuLogo, hover, 1);
        LogoButton exitButton = new ExitButton(game, exitButtonTex, menuLogo, hover,2);

        menuStage = new Stage(viewport, batch);
        menuStage.addActor(menuLogo);

        overlayStage = new Stage(viewport, batch);
        overlayStage.addActor(logoOverlay);

        buttonsStage = new Stage(viewport, batch);
        buttonsStage.addActor(playButton);
        buttonsStage.addActor(optionsButton);
        buttonsStage.addActor(exitButton);

        if (!beatmapManager.getCurrentMusic().isPlaying()) {
            beatmapManager.startMusicPlaying();
        }

        beatFactory.addListener(menuLogo);

        inputMultiplexer.addProcessor(buttonsStage);
        inputMultiplexer.addProcessor(menuStage);
    }

    public void toSoundSelectScreen() {
        this.switchScreen(new SoundSelectScreen(game));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // ACT
        backgroundStage.act(delta);
        buttonsStage.act(delta);
        menuStage.act(delta);
        overlayStage.act(delta);

        // DRAW
        viewport.apply(true);
        backgroundStage.draw();
        menuLogo.renderBeatCircles(delta);
        buttonsStage.draw();
        menuLogo.colorLayer();
        menuStage.draw();
        overlayStage.draw();

        renderFade(delta);
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
        menuStage.dispose();
        overlayStage.dispose();
        buttonsStage.dispose();
    }
}

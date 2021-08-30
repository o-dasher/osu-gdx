package com.dasher.osugdx.GameScenes.MainMenu;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dasher.osugdx.Audio.GameSound;
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
    private final GameSound heartBeat;
    private final GameSound downBeat;
    private final GameSound hover;
    private final GameSound logoSelect;
    private final Texture playButtonTex;
    private final Texture optionsButtonTex;
    private final Texture exitButtonTex;


    public MenuScreen(@NotNull OsuGame game) {
        super(game);
        logoTexture = assetManager.get(assetManager.textures.logo);
        heartBeat = new GameSound(assetManager.get(assetManager.sounds.osuLogoHeartBeat));
        downBeat = new GameSound(assetManager.get(assetManager.sounds.osuLogoDownBeat));
        hover = new GameSound(assetManager.get(assetManager.sounds.buttonHover));
        logoSelect = new GameSound(assetManager.get(assetManager.sounds.osuLogoSelect.fileName));
        playButtonTex = assetManager.get(assetManager.textures.playButton);
        optionsButtonTex = assetManager.get(assetManager.textures.optionsButton);
        exitButtonTex = assetManager.get(assetManager.textures.exitButton);
    }

    @Override
    public void show() {
        super.show();

        menuLogo = new MainLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);
        logoOverlay = new OverlayLogo(game, logoTexture, heartBeat, downBeat, logoSelect, hover);

        LogoButton playButton = new PlayButton(playButtonTex, menuLogo, hover, 1);
        LogoButton optionsButton = new OptionButton(optionsButtonTex, menuLogo, hover, 2);
        LogoButton exitButton = new ExitButton(exitButtonTex, menuLogo, hover,3);

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
        // ACT
        backgroundStage.act(delta);
        buttonsStage.act(delta);
        menuStage.act(delta);
        logoOverlay.setPosition(menuLogo.getX(), menuLogo.getY());

        // DRAW
        viewport.apply(true);
        backgroundStage.draw();
        menuLogo.renderBeatCircles(delta);
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

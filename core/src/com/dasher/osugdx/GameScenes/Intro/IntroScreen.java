 package com.dasher.osugdx.GameScenes.Intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.dasher.osugdx.Framework.Stages.SwitcherStage;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.Intro.Actors.LogoActor;
import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

 public class IntroScreen extends UIScreen {
    private SwitcherStage introStage;
    private Texture logoTexture;
    private Music seeyaSound;
    private Music welcomeSound;
    private boolean didRunSwitchScreenTimerTask = false;

    public IntroScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        logoTexture = assetManager.get(assetManager.textures.logo);
        LogoActor osuLogo = new LogoActor(game, logoTexture);

        // IntroStage
        introStage = new SwitcherStage(game, viewport, true, true);
        introStage.addActor(osuLogo);
        introStage.fadeIn();

        // USING MUSIC API BECAUSE THE FILES ARE TOO HEAVY FOR ANDROID SOUND I GUESS
        seeyaSound = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.seeya.fileName));
        welcomeSound = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.welcome.fileName));

        audioManager.playMusic(welcomeSound);
    }

    @Override
    public void render(float delta) {
        introStage.act(delta);
        introStage.draw();
        if (beatMapStore.isLoadedAllBeatmaps() && beatmapManager.isFirstBeatmapLoaded()) {
            if (!didRunSwitchScreenTimerTask) {
                didRunSwitchScreenTimerTask = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        introStage.switchScreen(new MenuScreen((game)));
                    }
                }, 2);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        introStage.dispose();
        logoTexture.dispose();
        seeyaSound.dispose();
        welcomeSound.dispose();
    }
}

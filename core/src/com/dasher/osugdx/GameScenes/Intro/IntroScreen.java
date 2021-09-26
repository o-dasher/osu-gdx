 package com.dasher.osugdx.GameScenes.Intro;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.audio.Music;
 import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.math.Interpolation;
 import com.badlogic.gdx.scenes.scene2d.Stage;
 import com.badlogic.gdx.scenes.scene2d.actions.Actions;
 import com.badlogic.gdx.utils.Timer;
 import com.dasher.osugdx.Audio.GameMusic;
 import com.dasher.osugdx.GameScenes.Intro.Actors.LogoActor;
 import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
 import com.dasher.osugdx.GameScenes.UIScreen;
 import com.dasher.osugdx.OsuGame;

 import org.jetbrains.annotations.NotNull;

 public
 class IntroScreen extends UIScreen {
    private Stage introStage;
    private GameMusic seeyaSound;
    private GameMusic welcomeSound;
    private Texture logoTexture;
    private LogoActor osuLogo;
    private boolean canSwitchScreen;

    public IntroScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        logoTexture = new Texture(Gdx.files.internal(assetManager.textures.logo.fileName));
        osuLogo = new LogoActor(game, logoTexture, cleanupTime);
        float scale = osuLogo.getBaseScale() / 2;
        osuLogo.addAction(
                Actions.sequence(
                    Actions.scaleTo(scale, scale, cleanupTime, Interpolation.smooth),
                        Actions.run(() -> game.canSwitchIntroScreen = true)
                )
        );

        // IntroStage
        introStage = new Stage(viewport);
        introStage.addActor(osuLogo);

        // USING MUSIC API BECAUSE THE FILES ARE TOO HEAVY FOR ANDROID SOUND I GUESS
        seeyaSound = audioFactory.newMusic(Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.seeya.fileName)));
        welcomeSound = audioFactory.newMusic(Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.welcome.fileName)));

        parrot.playMusic(welcomeSound);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        viewport.apply(true);

        if (backgroundStage != null) {
            backgroundStage.act(delta);
        }
        introStage.act(delta);
        introStage.draw();

        if (beatMapStore == null || beatmapManager == null) {
            resetGlobals();
        } else {
            if (
                beatMapStore.isLoadedAllBeatmaps()
                        && beatmapManager.isFirstBeatmapLoaded()
                        && canSwitchScreen && !game.calledToSwitchScreen
            ) {
                osuLogo.switchCleanup();
                switchScreen(new MenuScreen(game, this));
            }
        }

        renderFade(delta);
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
        logoTexture.dispose();
        introStage.dispose();
        seeyaSound.getMusic().dispose();
        welcomeSound.getMusic().dispose();
    }

     public void setCanSwitchScreen(boolean canSwitchScreen) {
         this.canSwitchScreen = canSwitchScreen;
     }
 }

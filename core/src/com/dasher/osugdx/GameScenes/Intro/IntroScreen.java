 package com.dasher.osugdx.GameScenes.Intro;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.audio.Music;
 import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.scenes.scene2d.actions.Actions;
 import com.dasher.osugdx.Framework.Stages.FadingStage;
 import com.dasher.osugdx.GameScenes.Intro.Actors.LogoActor;
 import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
 import com.dasher.osugdx.GameScenes.UIScreen;
 import com.dasher.osugdx.OsuGame;

 import org.jetbrains.annotations.NotNull;

 public
 class IntroScreen extends UIScreen {
    private FadingStage introStage;
    private Music seeyaSound;
    private Music welcomeSound;
    private LogoActor osuLogo;
    private boolean didSetupBeatmaps;

    public IntroScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Texture logoTexture = assetManager.get(assetManager.textures.logo);
        osuLogo = new LogoActor(game, logoTexture, cleanupTime);

        // IntroStage
        introStage = new FadingStage(viewport, cleanupTime);
        introStage.addActor(osuLogo);

        // USING MUSIC API BECAUSE THE FILES ARE TOO HEAVY FOR ANDROID SOUND I GUESS
        seeyaSound = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.seeya.fileName));
        welcomeSound = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.welcome.fileName));

        audioManager.playMusic(welcomeSound);
    }

    @Override
    public void render(float delta) {
        viewport.apply(true);

        introStage.act(delta);
        introStage.draw();

        if (beatMapStore.isLoadedAllBeatmaps() && beatmapManager.isFirstBeatmapLoaded() && !welcomeSound.isPlaying()) {
            introStage.fadeOut();
            osuLogo.switchCleanup();
            switchScreen(new MenuScreen(game));
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
        seeyaSound.dispose();
        welcomeSound.dispose();
    }
}

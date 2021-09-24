 package com.dasher.osugdx.GameScenes.Intro;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.audio.Music;
 import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.scenes.scene2d.Stage;
 import com.dasher.osugdx.GameScenes.Intro.Actors.LogoActor;
 import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
 import com.dasher.osugdx.GameScenes.UIScreen;
 import com.dasher.osugdx.OsuGame;

 import org.jetbrains.annotations.NotNull;

 public
 class IntroScreen extends UIScreen {
    private Stage introStage;
    private Music seeyaSound;
    private Music welcomeSound;
    private LogoActor osuLogo;

    public IntroScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Texture logoTexture = assetManager.get(assetManager.textures.logo);
        osuLogo = new LogoActor(game, logoTexture, cleanupTime);

        // IntroStage
        introStage = new Stage(viewport);
        introStage.addActor(osuLogo);

        // USING MUSIC API BECAUSE THE FILES ARE TOO HEAVY FOR ANDROID SOUND I GUESS
        seeyaSound = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.seeya.fileName));
        welcomeSound = Gdx.audio.newMusic(Gdx.files.internal(assetManager.sounds.welcome.fileName));

        welcomeSound.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        viewport.apply(true);

        introStage.act(delta);
        introStage.draw();

        if (beatMapStore.isLoadedAllBeatmaps() && beatmapManager.isFirstBeatmapLoaded() && !welcomeSound.isPlaying()) {
            osuLogo.switchCleanup();
            switchScreen(new MenuScreen(game));
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
        introStage.dispose();
        seeyaSound.dispose();
        welcomeSound.dispose();
    }
}

 package com.dasher.osugdx.GameScenes.Intro;

import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.Menu.MenuScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

 public class IntroScreen extends GameScreen {
    private IntroStage introStage;

    public IntroScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        Texture logoTexture = assetManager.get(assetManager.textures.logo);
        introStage = new IntroStage(game, viewport, logoTexture);
    }

    @Override
    public void render(float delta) {
        introStage.act(delta);
        introStage.draw();
        if (beatMapStore.isLoadedAllBeatmaps()) {
            introStage.switchScreen(new MenuScreen((game)));
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
    }
}

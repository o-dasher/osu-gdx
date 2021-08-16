 package com.dasher.osugdx.GameScenes.Intro;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.Menu.MenuScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

 public class IntroScreen extends GameScreen {
    private IntroStage introStage;
    private boolean startedLoadingCache = false;
    private boolean startedLoadingBeatmaps = false;

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
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            if (!startedLoadingCache) {
                startedLoadingCache = true;
                beatMapStore.loadCache();
            } else if (beatMapStore.isFinishedLoadingCache() && !startedLoadingBeatmaps) {
                startedLoadingBeatmaps = true;
                beatMapStore.loadAllBeatmaps();
            }
        } else {
            if (!startedLoadingCache) {
                CompletableFuture.runAsync(() -> {
                    startedLoadingCache = true;
                    beatMapStore.loadCache();
                }).whenCompleteAsync((res, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                    }
                    startedLoadingBeatmaps = true;
                    beatMapStore.loadAllBeatmaps();
                });
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
    }
}

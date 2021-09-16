package com.dasher.osugdx.GameScenes.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class GamePlayScreen extends GameScreen {
    public GamePlayScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        shapeRenderer.prepareToIncreaseAlpha(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.increaseAlphaByStep(0.01f);
        float[] shapeCenter = shapeRenderer.calculateCenteredValues(viewport);
        float yCenterMultiplier = 1.625f;
        shapeRenderer.rect(
                shapeCenter[0], shapeCenter[1] / yCenterMultiplier,
                shapeCenter[2], shapeCenter[3] * yCenterMultiplier
        );
        shapeRenderer.end();
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

    }
}

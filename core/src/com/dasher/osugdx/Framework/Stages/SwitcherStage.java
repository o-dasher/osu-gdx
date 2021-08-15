package com.dasher.osugdx.Framework.Stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SwitcherStage extends FadingStage {
    private Game game;
    private boolean withFadeOut;

    public SwitcherStage(Game game, Viewport viewport, boolean fadeOut) {
        super(viewport);
        init(game, fadeOut);
    }

    public SwitcherStage(Game game, Viewport viewport, boolean willFadeIn, boolean fadeOut, float fadeInBaseAlpha) {
        super(viewport, willFadeIn, fadeInBaseAlpha);
        init(game, fadeOut);
    }

    public SwitcherStage(Game game, Viewport viewport, boolean willFadeIn, boolean fadeOut) {
        super(viewport, willFadeIn);
        init(game, fadeOut);
    }

    public SwitcherStage(Game game, Viewport viewport, Batch batch, boolean fadeOut) {
        super(viewport, batch);
        init(game, fadeOut);
    }

    public SwitcherStage(Game game, Viewport viewport, Batch batch, boolean willFadeIn, boolean fadeOut, float fadeInBaseAlpha) {
        super(viewport, batch, willFadeIn, fadeInBaseAlpha);
        init(game, fadeOut);
    }

    public SwitcherStage(Game game, Viewport viewport, Batch batch, boolean willFadeIn, boolean fadeOut) {
        super(viewport, batch, willFadeIn);
        init(game, fadeOut);
    }

    private void init(Game game, boolean fadeOut) {
        withFadeOut = fadeOut;
        this.game = game;
    }

    private void applySwitch(Screen newScreen) {
        game.getScreen().dispose();
        game.setScreen(newScreen);
    }

    public void switchScreen(Screen newScreen) {
        if (withFadeOut) {
            addAction(
                    Actions.sequence(
                            getFadeOutAction(),
                            Actions.run(() -> Gdx.app.postRunnable(() -> applySwitch(newScreen)))
                    )
            );
        } else {
            Gdx.app.postRunnable(() -> applySwitch(newScreen));
        }
    }
}

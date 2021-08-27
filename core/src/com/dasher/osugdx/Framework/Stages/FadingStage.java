package com.dasher.osugdx.Framework.Stages;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FadingStage extends Stage {
    private final float fadeTime;

    public FadingStage(Viewport viewport, float fadeTime) {
        super(viewport);
        this.fadeTime = fadeTime;
        fadeIn();
    }

    public FadingStage(Viewport viewport, Batch batch, float fadeTime) {
        super(viewport, batch);
        this.fadeTime = fadeTime;
        fadeIn();
    }

    public void fadeIn() {
        getRoot().getColor().a = 0;
        getRoot().addAction(Actions.fadeIn(fadeTime));
    }

    public void fadeOut() {
        addAction(Actions.fadeOut(fadeTime));
    }
}

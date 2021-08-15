package com.dasher.osugdx.Framework.Stages;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Interfaces.Fadeable;

public class FadingStage extends Stage implements Fadeable {
    private boolean willFadeIn = false;
    private final float defaultFadeInAlpha = 0;
    private float fadeInBaseAlpha = defaultFadeInAlpha;

    public FadingStage(boolean willFadeIn) {
        super();
        prepareToFadeIn(willFadeIn, defaultFadeInAlpha);
    }

    public FadingStage(Viewport viewport) {
        super(viewport);
    }

    public FadingStage(Viewport viewport, boolean willFadeIn) {
        super(viewport);
        prepareToFadeIn(willFadeIn, defaultFadeInAlpha);
    }

    public FadingStage(Viewport viewport, boolean willFadeIn, float fadeInBaseAlpha) {
        super(viewport);
        prepareToFadeIn(willFadeIn, fadeInBaseAlpha);
    }

    public FadingStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    public FadingStage(Viewport viewport, Batch batch, boolean willFadeIn, float fadeInBaseAlpha) {
        super(viewport, batch);
        prepareToFadeIn(willFadeIn, fadeInBaseAlpha);
    }

    public FadingStage(Viewport viewport, Batch batch, boolean willFadeIn) {
        super(viewport, batch) ;
        prepareToFadeIn(willFadeIn, defaultFadeInAlpha);

    }

    public void prepareToFadeIn(boolean willFadeIn, float fadeInBaseAlpha) {
        this.willFadeIn = willFadeIn;
        this.fadeInBaseAlpha = fadeInBaseAlpha;
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if (willFadeIn) {
            actor.addAction(Actions.alpha(fadeInBaseAlpha));
        }
    }

    @Override
    public void fadeIn() {
        for (Actor actor: getActors()) {
            actor.addAction(getFadeInAction());
        }
    }

    @Override
    public void fadeOut() {
        for (Actor actor: getActors()) {
            actor.addAction(getFadeOutAction());
        }
    }
}

package com.dasher.osugdx.GameScenes.Intro.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.Framework.Interfaces.SwitcherStageListener;
import com.dasher.osugdx.OsuGame;

import com.dasher.osugdx.Images.GameImage;

public class LogoActor extends GameImage implements SwitcherStageListener {
    private final float scaleDuration = 0.5f;

    public LogoActor(OsuGame game, Texture logoTexture) {
        super(game, logoTexture, false);
        setOrigin(Align.center);
        setBaseScale(1);
        applyCentering();
        float scale = getBaseScale() / 2;
        addAction(Actions.scaleTo(scale, scale, scaleDuration, Interpolation.smooth));
    }

    @Override
    public void onSwitch() {
        addAction(Actions.scaleTo(getBaseScale(), getBaseScale(), scaleDuration, Interpolation.smooth));
    }
}

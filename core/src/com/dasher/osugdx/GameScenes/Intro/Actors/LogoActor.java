package com.dasher.osugdx.GameScenes.Intro.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.Framework.Interfaces.ScreenSwitchListener;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;

public class LogoActor extends GameImage implements ScreenSwitchListener {
    private final float cleanupTime;

    public LogoActor(OsuGame game, Texture logoTexture, float cleanupTime) {
        super(game, logoTexture, true);
        this.cleanupTime = cleanupTime;
        setOrigin(Align.center);
        setBaseScale(1);
        applyCentering();
    }

    @Override
    public void switchCleanup() {
        addAction(Actions.scaleTo(getBaseScale(), getBaseScale(), cleanupTime, Interpolation.smooth));
    }
}

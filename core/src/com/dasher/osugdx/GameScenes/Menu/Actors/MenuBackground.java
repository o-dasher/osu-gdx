package com.dasher.osugdx.GameScenes.Menu.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.dasher.osugdx.OsuGame;

import com.dasher.osugdx.Images.GameImage;

public class MenuBackground extends GameImage {
    public MenuBackground(OsuGame game, Texture texture, boolean forceCenter) {
        super(game, texture, forceCenter);

        setScale(0.5f);

        int moveByForever = 10;
        float durationIdle = 1;
        float backgroundBrightness = 1;

        addAction(
                Actions.forever(
                        Actions.parallel(
                                Actions.sequence(
                                        Actions.moveBy(moveByForever, moveByForever, durationIdle),
                                        Actions.moveBy(-moveByForever, -moveByForever, durationIdle)
                                ),
                                Actions.alpha(backgroundBrightness, durationIdle)
                        )
                )
        );
    }
}

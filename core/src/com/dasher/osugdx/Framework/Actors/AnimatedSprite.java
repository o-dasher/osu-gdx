package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimatedSprite extends AnimatedActor<Sprite> {
    public AnimatedSprite(Animation<Sprite> animation) {
        super(animation);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentT.draw(batch);
    }
}

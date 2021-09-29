package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class AnimatedSprite extends AnimatedActor<Sprite> {
    public AnimatedSprite(Animation<Sprite> animation) {
        super(animation);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentT.draw(batch);
    }

    @Override
    public void setScaleX(float scaleX) {
        for (Sprite sprite: getSprites()) {
            sprite.setScale(scaleX, sprite.getScaleY());
        }
    }

    @Override
    public void setScaleY(float scaleY) {
        for (Sprite sprite: getSprites()) {
            sprite.setScale(sprite.getScaleX(), scaleY);
        }
    }

    @Override
    public void innerSetPosition(float x, float y) {
        for (Sprite sprite: getSprites()) {
            sprite.setPosition(x, y);
        }
    }

    public Array<Sprite> getSprites() {
        return allTs;
    }
}

package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AnimatedActor<T> extends Actor{
    Animation<T> animation;
    T currentT;

    float time = 0f;

    public AnimatedActor(Animation<T> animation) {
        this.animation = animation;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        time += delta;
        currentT = animation.getKeyFrame(time, true);
    }

    @Override
    public abstract void draw(Batch batch, float parentAlpha);
}
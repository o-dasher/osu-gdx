package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;


public abstract class AnimatedActor<T> extends Actor {
    protected final Array<T> allTs = new Array<>();
    protected final Animation<T> animation;
    protected T currentT;

    float time = 0f;

    public AnimatedActor(@NotNull Animation<T> animation) {
        this.animation = animation;
        for (T t: animation.getKeyFrames()) {
            allTs.add(t);
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);
        time += delta;
        currentT = animation.getKeyFrame(time, true);
    }

    @Override
    public abstract void draw(Batch batch, float parentAlpha);

    public abstract void setScaleX(float scaleX);

    @Override
    public abstract void setScaleY(float scaleY);

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        setScale(scaleXY, scaleXY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        innerSetPosition(x, y);
    }

    public abstract void innerSetPosition(float x, float y);

    public Animation<T> getAnimation() {
        return animation;
    }
}

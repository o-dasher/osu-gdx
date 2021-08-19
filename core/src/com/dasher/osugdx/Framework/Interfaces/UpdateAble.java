package com.dasher.osugdx.Framework.Interfaces;

import com.badlogic.gdx.Gdx;

public interface UpdateAble {
    default void update() { update(Gdx.graphics.getDeltaTime()); }
    void update(float delta);
}

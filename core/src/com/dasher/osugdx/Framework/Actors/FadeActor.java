package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dasher.osugdx.Framework.Interfaces.Fadeable;

public class FadeActor extends Actor implements Fadeable {

    @Override
    public void fadeIn() {
        addAction(getFadeInAction());
    }

    @Override
    public void fadeOut() {
        addAction(getFadeOutAction());
    }
}

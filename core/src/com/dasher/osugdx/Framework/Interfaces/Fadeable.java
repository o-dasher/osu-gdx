package com.dasher.osugdx.Framework.Interfaces;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public interface Fadeable {
    float defaultFadeDuration = 1f;

    void fadeIn();
    void fadeOut();

    default Action getFadeInAction() {
        return Actions.fadeIn(defaultFadeDuration);
    }

    default Action getFadeOutAction() {
        return Actions.fadeOut(defaultFadeDuration);
    }
}

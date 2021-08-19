package com.dasher.osugdx.Framework.Interfaces;

import com.badlogic.gdx.utils.Array;

public interface Listenable<T> {
    Array<T> getListeners();
    default void addListener(T listener) {
        getListeners().add(listener);
    }
    default void removeListener(T listener) {
        getListeners().removeValue(listener, true);
    }
}

package com.dasher.osugdx.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public abstract class InputHelper {
    private InputHelper() {}

    public static boolean isBackPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK);
    }
}

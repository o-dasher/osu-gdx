package com.dasher.osugdx.Framework.Helpers;

import com.badlogic.gdx.Gdx;


public class CenteringHelper {
    public static float WORLD_WIDTH = Gdx.graphics.getWidth();
    public static float WORLD_HEIGHT = Gdx.graphics.getHeight();

    public static float getCenterX(float width) {
        return WORLD_WIDTH / 2f - width / 2f;
    }

    public static float getCenterY(float height) {
        return WORLD_HEIGHT / 2f - height / 2f;
    }
}

package com.dasher.osugdx.Framework.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.jetbrains.annotations.NotNull;

public class GraphicHelper {
    public static int getAspectRatio() {
        return Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    }
    public static int getAspectRatio(@NotNull Viewport viewport) {
        return (int) (viewport.getWorldWidth() / viewport.getWorldHeight());
    }
}

package com.dasher.osugdx.Graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.dasher.osugdx.assets.GameAssetManager;

import org.jetbrains.annotations.NotNull;

public class Fonts {
    public BitmapFont baseBitmapFont;

    public Fonts(@NotNull GameAssetManager gameAssetManager) {
        baseBitmapFont = new BitmapFont(false);
    }
}

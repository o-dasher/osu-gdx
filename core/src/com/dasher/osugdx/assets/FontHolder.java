package com.dasher.osugdx.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.jetbrains.annotations.NotNull;

public class FontHolder extends AssetHolder<BitmapFont> {
    public FontHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.fontsPath);
    }
}

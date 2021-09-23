package com.dasher.osugdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.jetbrains.annotations.NotNull;

public class FontHolder extends AssetHolder<BitmapFont> {
    public AssetDescriptor<BitmapFont> aller;

    public FontHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.fontsPath.path());
    }
}

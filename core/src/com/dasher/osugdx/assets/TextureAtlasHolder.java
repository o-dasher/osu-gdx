package com.dasher.osugdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import org.jetbrains.annotations.NotNull;

public class TextureAtlasHolder extends com.dasher.osugdx.assets.AssetHolder<TextureAtlas> {
    public AssetDescriptor<TextureAtlas> menuBackgrounds;

    public TextureAtlasHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.textures.path());
    }
}

package com.dasher.osugdx.assets;


import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

import org.jetbrains.annotations.NotNull;

public class TextureHolder extends AssetHolder<Texture> {
    final TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();

    public AssetDescriptor<Texture> logo;
    public AssetDescriptor<Texture> menuBackground;

    public TextureHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.textures.path());
        textureParameter.genMipMaps = true;
        textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;

        logo = addAsset(assetPaths.textures.menu + "logo.png", Texture.class, textureParameter);
        menuBackground = addAsset(assetPaths.textures.menu + "menu-background-1.jpg", Texture.class, textureParameter);
    }
}
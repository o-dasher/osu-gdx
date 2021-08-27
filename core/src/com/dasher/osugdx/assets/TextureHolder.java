package com.dasher.osugdx.assets;


import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;

public class TextureHolder extends AssetHolder<Texture> {
    public final TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();

    public AssetDescriptor<Texture> logo;
    public AssetDescriptor<Texture> menuBackground;
    public AssetDescriptor<Texture> playButton;
    public AssetDescriptor<Texture> optionsButton;
    public AssetDescriptor<Texture> exitButton;
    public Array<AssetDescriptor<Texture>> menuBackgrounds = new Array<>();

    public TextureHolder(@NotNull AssetPaths assetPaths) {
        super(assetPaths.textures.path());

        textureParameter.genMipMaps = true;
        textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;

        logo = addAsset(assetPaths.textures.menu + "logo.png", Texture.class, textureParameter);
        playButton = addAsset(assetPaths.textures.menu + "play.png", Texture.class, textureParameter);
        optionsButton = addAsset(assetPaths.textures.menu + "options.png", Texture.class, textureParameter);
        exitButton = addAsset(assetPaths.textures.menu + "exit.png", Texture.class, textureParameter);

        for (int i = 1; i < 8; i++) {
            menuBackgrounds.add(addAsset(assetPaths.textures.menu + "menu-background-" + i + ".jpg", Texture.class, textureParameter));
        }

        menuBackground = menuBackgrounds.random();
    }
}
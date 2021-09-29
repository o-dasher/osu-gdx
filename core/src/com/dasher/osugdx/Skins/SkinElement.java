package com.dasher.osugdx.Skins;

import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;

public class SkinElement {
    private final Texture texture;
    private final Sprite sprite;
    private final boolean isHD;
    private final Skin skin;
    private final FileHandle file;

    public SkinElement(FileHandle file, @NotNull ElementString elementString, Skin skin, TextureLoader.@NotNull TextureParameter textureParameter) {
        this.isHD = elementString.isHD();
        this.file = file;
        this.texture = new Texture(file, textureParameter.genMipMaps);
        texture.setFilter(textureParameter.minFilter, textureParameter.magFilter);
        this.sprite = new Sprite(texture);
        this.skin = skin;
        if (isHD) {
            sprite.setSize(texture.getWidth() / 2f, texture.getHeight() / 2f);
        }
    }

    public int getDimension(int dimension) {
        return isHD? dimension / 2 : dimension;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isHD() {
        return isHD;
    }

    public Skin getSkin() {
        return skin;
    }

    public void dispose() {
        texture.dispose();
    }

    public FileHandle getFile() {
        return file;
    }
}

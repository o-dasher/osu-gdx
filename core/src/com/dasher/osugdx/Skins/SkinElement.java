package com.dasher.osugdx.Skins;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import org.jetbrains.annotations.NotNull;

public class SkinElement {
    private final Texture texture;
    private final boolean isHD;
    private final Skin skin;

    public SkinElement(FileHandle file, @NotNull ElementString elementString, Skin skin) {
        this.isHD = elementString.isHD();
        this.texture = new Texture(file);
        this.skin = skin;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isHD() {
        return isHD;
    }

    public Skin getSkin() {
        return skin;
    }
}

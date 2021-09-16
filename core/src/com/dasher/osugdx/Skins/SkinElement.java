package com.dasher.osugdx.Skins;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class SkinElement {
    private final Texture texture;
    private final boolean isHD;

    public SkinElement(FileHandle file, ElementString elementString) {
        this.isHD = elementString.isHD();
        this.texture = new Texture(file);
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isHD() {
        return isHD;
    }
}

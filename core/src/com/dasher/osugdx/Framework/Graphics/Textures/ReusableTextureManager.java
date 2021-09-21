package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

import org.jetbrains.annotations.NotNull;

public class ReusableTextureManager {
    private final ObjectMap<String, ReusableTexture> textures = new ObjectMap<>();

    public ReusableTexture getTexture(FileHandle file, ReusableTextureListener listener) {
        return getTexture(file, false, listener);
    }

    public ReusableTexture getTexture(@NotNull FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        return textures.containsKey(file.name())
                ? textures.get(file.name())
                : new ReusableTexture(file, useMipMaps, listener);
    }
}

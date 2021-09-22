package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import org.jetbrains.annotations.NotNull;

public class ReusableTextureManager {
    private final ObjectMap<String, ReusableTexture> textures = new ObjectMap<>();

    private @NotNull ReusableTexture newTexture(FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        ReusableTexture newTexture = new ReusableTexture(file, useMipMaps, listener);
        textures.put(file.name(), newTexture);
        return newTexture;
    }

    public ReusableTexture getTexture(FileHandle file, ReusableTextureListener listener) {
        return getTexture(file, false, listener);
    }

    public ReusableTexture getTexture(@NotNull FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        ReusableTexture nextTexture;
        if (textures.containsKey(file.name())) {
            ReusableTexture loadedTexture = textures.get(file.name());
            if (loadedTexture.isDisposed()) {
                textures.remove(file.name());
                nextTexture = newTexture(file, useMipMaps, listener);
            } else {
                nextTexture = loadedTexture;
                if (!nextTexture.getListeners().contains(listener, true)) {
                    nextTexture.getListeners().add(listener);
                }
            }
        } else {
            nextTexture = newTexture(file, useMipMaps, listener);
        }
        return nextTexture;
    }
}

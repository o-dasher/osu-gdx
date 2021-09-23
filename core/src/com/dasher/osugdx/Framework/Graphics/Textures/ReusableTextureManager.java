package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import org.jetbrains.annotations.NotNull;

public class ReusableTextureManager {
    private final ObjectMap<Integer, ReusableTexture> textures = new ObjectMap<>();

    private @NotNull ReusableTexture newTexture(FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        ReusableTexture newTexture = new ReusableTexture(file, useMipMaps, listener);
        textures.put(file.hashCode(), newTexture);
        return newTexture;
    }

    public ReusableTexture getTexture(FileHandle file, ReusableTextureListener listener) {
        return getTexture(file, false, listener);
    }

    public ReusableTexture getTexture(@NotNull FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        ReusableTexture nextTexture;
        if (textures.containsKey(file.hashCode())) {
            ReusableTexture loadedTexture = textures.get(file.hashCode());
            if (loadedTexture.isDisposed()) {
                textures.remove(file.hashCode());
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

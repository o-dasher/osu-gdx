package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

import org.jetbrains.annotations.NotNull;

public class ReusableTextureManager {
    private final ObjectMap<Integer, ReusableTexture> textures = new ObjectMap<>();

    private @NotNull ReusableTexture newTexture(FileHandle file, TextureParameter textureParameter, ReusableTextureListener listener) {
        ReusableTexture newTexture = new ReusableTexture(file, textureParameter.genMipMaps, listener);
        newTexture.setFilter(textureParameter.minFilter, textureParameter.magFilter);
        textures.put(file.hashCode(), newTexture);

        return newTexture;
    }

    public ReusableTexture getTexture(@NotNull FileHandle file, TextureParameter textureParameter, ReusableTextureListener listener) {
        ReusableTexture nextTexture;
        if (textures.containsKey(file.hashCode())) {
            ReusableTexture loadedTexture = textures.get(file.hashCode());
            if (loadedTexture.isDisposed()) {
                textures.remove(file.hashCode());
                nextTexture = newTexture(file, textureParameter, listener);
            } else {
                nextTexture = loadedTexture;
                if (!nextTexture.getListeners().contains(listener, true)) {
                    nextTexture.getListeners().add(listener);
                }
            }
        } else {
            nextTexture = newTexture(file, textureParameter, listener);
        }
        return nextTexture;
    }
}

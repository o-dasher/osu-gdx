package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class ReusableTexture extends Texture {
    private final FileHandle file;
    private final ReusableTextureListener listener;

    public ReusableTexture(FileHandle file, ReusableTextureListener listener) {
        super(file);
        this.file = file;
        this.listener = listener;
    }

    public ReusableTexture(FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        super(file, useMipMaps);
        this.file = file;
        this.listener = listener;
    }

    @Override
    public void dispose() {
        if (listener.shouldDispose(this)) {
            super.dispose();
        }
    }

    public FileHandle getFile() {
        return file;
    }
}

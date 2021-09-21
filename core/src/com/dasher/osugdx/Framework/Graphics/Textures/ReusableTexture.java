package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Interfaces.Listenable;

public class ReusableTexture extends Texture implements Listenable<ReusableTextureListener> {
    private final Array<ReusableTextureListener> listeners = new Array<>();
    private final FileHandle file;
    private boolean isDisposed = false;

    public ReusableTexture(FileHandle file, ReusableTextureListener listener) {
        super(file);
        this.file = file;
        listeners.add(listener);
    }

    public ReusableTexture(FileHandle file, boolean useMipMaps, ReusableTextureListener listener) {
        super(file, useMipMaps);
        this.file = file;
        listeners.add(listener);
    }

    @Override
    public void dispose() {
        for (ReusableTextureListener listener: listeners) {
            if (listener.shouldDispose(this)) {
                forceDispose();
            }
        }
    }

    public void forceDispose() {
        super.dispose();
        isDisposed = true;
    }

    public FileHandle getFile() {
        return file;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    @Override
    public Array<ReusableTextureListener> getListeners() {
        return listeners;
    }
}

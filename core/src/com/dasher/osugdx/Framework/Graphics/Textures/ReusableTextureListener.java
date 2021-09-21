package com.dasher.osugdx.Framework.Graphics.Textures;

import com.badlogic.gdx.graphics.Texture;

public interface ReusableTextureListener {
    boolean shouldDispose(Texture texture);
}

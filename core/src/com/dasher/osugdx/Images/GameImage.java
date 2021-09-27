package com.dasher.osugdx.Images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.dasher.osugdx.Framework.Actors.CenteredImage;
import com.dasher.osugdx.OsuGame;

public class GameImage extends CenteredImage {
    protected final OsuGame game;
    private float baseScale = 0;

    public GameImage(OsuGame game, boolean forceCenter) {
        super(forceCenter);
        this.game = game;
    }

    public GameImage(OsuGame game, Texture texture, boolean forceCenter) {
        super(texture, forceCenter);
        this.game = game;
    }

    public GameImage(OsuGame game, TextureRegion region, boolean forceCenter) {
        super(region, forceCenter);
        this.game = game;
    }

    public GameImage(OsuGame game, Drawable drawable, boolean forceCenter) {
        super(drawable, forceCenter);
        this.game = game;
    }

    public void setBaseScale(float baseScale) {
        setScale(baseScale);
        this.baseScale = baseScale;
    }

    public float getBaseScale() {
        return baseScale;
    }
}

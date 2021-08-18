package com.dasher.osugdx.Images;

import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.Framework.Actors.CenteredImage;
import com.dasher.osugdx.OsuGame;

public class GameImage extends CenteredImage {
    private final OsuGame game;
    private float baseScale = 0;

    public GameImage(OsuGame game, boolean forceCenter) {
        super(forceCenter);
        this.game = game;
        applyToGameScale();
    }

    public GameImage(OsuGame game, Texture texture, boolean forceCenter) {
        super(texture, forceCenter);
        this.game = game;
        applyToGameScale();
    }

    public void setBaseScale(float baseScale) {
        setScale(baseScale);
        this.baseScale = baseScale;
    }

    public float getBaseScale() {
        return baseScale;
    }

    public void applyToGameScale() {
        setScale((baseScale == 0? getScaleX() : baseScale) * game.uiConfig.getScale());
    }
}

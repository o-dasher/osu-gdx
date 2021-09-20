package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;

import org.jetbrains.annotations.NotNull;

public class CenteredImage extends BuffedImage {
    private final boolean forceCenter;

    public CenteredImage(boolean forceCenter) {
        super();
        this.forceCenter = forceCenter;
        applyCentering();
    }

    public CenteredImage(Texture texture, boolean forceCenter) {
        super(texture);
        this.forceCenter = forceCenter;
        applyCentering();
    }

    public CenteredImage(TextureRegion region, boolean forceCenter) {
        super(region);
        this.forceCenter = forceCenter;
        applyCentering();
    }

    public CenteredImage(Drawable drawable, boolean forceCenter) {
        super(drawable);
        this.forceCenter = forceCenter;
        applyCentering();
    }

    public void applyCentering(@NotNull Viewport viewport) {
        setPosition(getCenterX(viewport), getCenterY(viewport));
    }

    public float getCenterX(Viewport viewport) {
        return viewport.getWorldWidth() / 2 - getWidth() / 2;
    }

    public float getCenterY(Viewport viewport) {
        return viewport.getWorldHeight() / 2 - getHeight() / 2;
    }

    public void applyCentering() {
        setPosition(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (forceCenter) {
            applyCentering();
        }
    }
}

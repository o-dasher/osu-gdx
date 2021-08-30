package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.Texture;
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

    public void applyCentering(@NotNull Viewport viewport) {
        setPosition(
                viewport.getWorldWidth() / 2 - getWidth() / 2,
                viewport.getWorldHeight() / 2 - getHeight() / 2
        );
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

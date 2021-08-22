package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;

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

    public void applyCentering(Viewport viewport) {
        setPosition(
                viewport.getWorldWidth() / 2 - getWidth() / 2,
                viewport.getWorldHeight() / 2 - getHeight() / 2
        );
    }

    public void applyCentering() {
        setPosition(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
    }

    private void forceCentering() {
        if (forceCenter) {
            applyCentering();
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        forceCentering();
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        forceCentering();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        forceCentering();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        forceCentering();
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        forceCentering();
    }
}

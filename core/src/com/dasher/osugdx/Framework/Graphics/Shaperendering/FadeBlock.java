package com.dasher.osugdx.Framework.Graphics.Shaperendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.jetbrains.annotations.NotNull;

public abstract class FadeBlock {
    private final Color color;
    private boolean isFadingIn = false;
    private boolean isFadingOut = false;
    private float alphaIncreaseDivisor = 1;
    private final ShapeRenderer shapeRenderer;
    private final Viewport viewport;

    public FadeBlock(Color color, ShapeRenderer shapeRenderer) {
        this(color, shapeRenderer, null);
    }

    public FadeBlock(@NotNull Color color, ShapeRenderer shapeRenderer, @Null Viewport viewport) {
        this.color = color.cpy();
        this.color.a = 0;
        this.shapeRenderer = shapeRenderer;
        this.viewport = viewport;
    }

    public void update(float delta) {
        float w;
        float h;
        if (viewport == null) {
            w = Gdx.graphics.getWidth();
            h = Gdx.graphics.getHeight();
        } else {
            w = viewport.getWorldWidth();
            h = viewport.getWorldHeight();
        }
        if (isFadingIn || isFadingOut) {
            if (isFadingIn) {
                color.a = Math.min(1, color.a + delta / alphaIncreaseDivisor);
                if (color.a >= 1) {
                    isFadingOut = true;
                    isFadingIn = false;
                    onFadeIn();
                }
            } else {
                color.a = Math.max(0, color.a - delta / alphaIncreaseDivisor);
                if (color.a <= 0) {
                    isFadingOut = false;
                    onFadeOut();
                }
            }
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(color);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, w, h);
            shapeRenderer.end();
        }
    }

    public void setAlphaIncreaseDivisor(float alphaIncreaseDivisor) {
        this.alphaIncreaseDivisor = alphaIncreaseDivisor;
    }

    public void setFade(boolean isFadingIn) {
        this.isFadingIn = isFadingIn;
    }

    public abstract void onFadeIn();
    public abstract void onFadeOut();
}

package com.dasher.osugdx.Framework.Graphics.Shaperendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;

import org.jetbrains.annotations.NotNull;

public class BuffedShapeRenderer extends ShapeRenderer {
    public BuffedShapeRenderer() {
        super();
    }
    public BuffedShapeRenderer(int maxVertices) {
        super(maxVertices);
    }
    public BuffedShapeRenderer(int maxVertices, ShaderProgram defaultShader) {
        super(maxVertices, defaultShader);
    }

    public void prepareToIncreaseAlpha(Color color) {
        setColor(color);
        getColor().a = 0;
    }

    public void increaseAlphaByStep(float step) {
        getColor().a = getColor().a < 1? getColor().a + step : 1;
        if (getColor().a + step > 1) {
            getColor().a = 1;
        }
    }

    public float[] calculateCenteredValues(@NotNull Viewport viewport) {
        float halfShapeW = viewport.getWorldWidth() / 2;
        float halfShapeH = viewport.getWorldHeight() / 2;
        return new float[] {
                com.dasher.osugdx.Framework.Helpers.CenteringHelper.getCenterX(halfShapeW),
                CenteringHelper.getCenterY(halfShapeH),
                halfShapeW,
                halfShapeH
        };
    }
}

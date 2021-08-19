package com.dasher.osugdx.Framework.Polygons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.dasher.osugdx.Framework.Interfaces.SimpleDrawable;

public class DasherPolygon extends Polygon implements SimpleDrawable<ShapeRenderer> {
    public Color color;
    public ShapeRenderer.ShapeType shapeType;

    public DasherPolygon(float[] vertices) {
        super(vertices);
    }

    @Override
    public void draw(ShapeRenderer drawer) {
        if (color != null) {
            drawer.setColor(color);
        }
    }
}

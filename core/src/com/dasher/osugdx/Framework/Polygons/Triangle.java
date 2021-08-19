package com.dasher.osugdx.Framework.Polygons;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Triangle extends DasherPolygon {
    public Triangle(float[] vertices) {
        super(vertices);
        if (vertices.length != 6) {
            throw new IllegalArgumentException("Triangles should have 5 vertices!");
        }
    }

    private float getPos(int lefts) {
        int posAccumulator = 0;
        float[] vertices = getVertices();
        for (int i = 0; i < vertices.length; i++) {
            if (i % 2 == lefts) {
                posAccumulator += vertices[i];
            }
        }
        return posAccumulator / 3f;
    }

    @Override
    public float getX() {
        return getPos(0);
    }

    @Override
    public float getY() {
        return getPos(1);
    }

    private void move(float by, int lefts) {
        float[] vertices = getVertices();
        for (int i = 0; i < vertices.length; i++) {
            if (i % 2 == lefts || lefts < 0) {
                vertices[i] += by;
            }
        }
    }

    public void move(float by) {
        move(by, -1);
    }

    public void move(float byX, float byY) {
        moveX(byX);
        moveY(byY);
    }

    public void moveX(float byX) {
        move(byX, 0);
    }

    public void moveY(float byY) {
        move(byY, 1);
    }

    @Override
    public void draw(ShapeRenderer drawer) {
        super.draw(drawer);
        if (color != null) {
            drawer.setColor(color);
        }
        if (shapeType != null) {
            drawer.set(shapeType);
        }
        float[] vertices = getVertices();
        drawer.triangle(
                vertices[0], vertices[1],
                vertices[2], vertices[3],
                vertices[4], vertices[5]
        );
    }
}

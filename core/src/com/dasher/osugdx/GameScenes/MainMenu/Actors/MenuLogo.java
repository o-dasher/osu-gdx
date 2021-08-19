package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Interfaces.UpdateAble;
import com.dasher.osugdx.Framework.Polygons.Triangle;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Timing.BeatListener;

import java.util.Random;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class MenuLogo extends GameImage implements BeatListener, UpdateAble {
    private final Viewport viewport;
    private final Random random;
    private final ShapeRenderer shapeRenderer;
    private final Array<Color> triangleColors = new Array<>();

    public MenuLogo(OsuGame game, Texture texture, boolean forceCenter) {
        super(game, texture, forceCenter);
        shapeRenderer = game.shapeRenderer;
        viewport = game.viewport;
        random = game.random;
        setOriginCenter();
        setScale(0.5f);
        triangleColors.add(Color.RED);
        triangleColors.add(Color.PURPLE);
        triangleColors.add(Color.OLIVE);
    }

    @Override
    public void onNewBeat(TimingPoint timingPoint) {
        float amountScale = 0.05f;
        float time = (float) (timingPoint.getBeatLength() / 1000)/ 2;
        addAction(Actions.sequence(
                Actions.scaleBy(+amountScale, +amountScale, time),
                Actions.scaleBy(-amountScale, -amountScale, time)
        ));
    }

    @Override
    public void onFourthBeat(TimingPoint timingPoint) {

    }

    private void colorLayer() {
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, (getScaleX() * getWidth()) * 0.45f, 64);
        shapeRenderer.end();
    }

    final Array<Triangle> triangles = new Array<>();
    final int maxTriangles = 10;

    @Override
    public void update(float delta) {
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        colorLayer();
        if (triangles.size < maxTriangles) {
            float randomPositionX = random.nextInt(150);
            float randomPositionY = random.nextInt((int) viewport.getWorldHeight());
            randomPositionX = randomPositionX % 2 == 0? +randomPositionX : -randomPositionX;
            float pos = viewport.getWorldWidth() / 2 + randomPositionX;
            float[] vertexes = new float[] {
                    pos-15f, randomPositionY,
                    pos+15f, randomPositionY,
                    pos, 25+ randomPositionY
            };
            Triangle triangle = new Triangle(vertexes);
            triangle.shapeType = ShapeRenderer.ShapeType.Filled;
            triangle.color = triangleColors.random();
            triangle.color.a = 0;
            triangles.add(triangle);
        }
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for (Triangle triangle: triangles) {
            if (triangle.getY() > viewport.getWorldHeight()) {
                triangles.removeValue(triangle, true);
            } else {
                if (triangle.color.a < 0.5f) {
                    triangle.color.a += 0.05 * delta;
                }
                triangle.moveY(100 * delta);
                triangle.draw(shapeRenderer);
            }
        }
        shapeRenderer.end();
    }
}

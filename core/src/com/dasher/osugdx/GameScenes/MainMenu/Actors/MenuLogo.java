package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Polygons.Triangle;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Timing.BeatListener;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class MenuLogo extends GameImage implements BeatListener {
    private final Viewport viewport;
    private final Random random;
    private final ShapeRenderer shapeRenderer;
    private float alpha = 1;
    private final AudioManager audioManager;
    private final Sound heartbeat;
    private final Sound downbeat;
    private final Array<Triangle> triangles = new Array<>();
    private final float scaleBy = 0.025f;

    public MenuLogo(OsuGame game, Texture texture, Sound heartbeat, Sound downbeat, Sound select, Sound hoverSound) {
        super(game, texture, false);
        shapeRenderer = game.shapeRenderer;
        viewport = game.viewport;
        random = game.random;
        audioManager = game.audioManager;
        this.heartbeat = heartbeat;
        this.downbeat = downbeat;
        
        setPosition(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
        setOriginCenter();
        setScale(0.5f);

        int maxTriangles = 10;
        for (int i = 0; i < maxTriangles; i++) {
            Triangle triangle = new Triangle(getRandomTriangleVertices());
            Color color = Color.PINK.cpy();
            color.r += i;
            triangle.color = color;
            restartTriangleColor(triangle);
            triangles.add(triangle);
        }

        toEnterExitScaledImage(scaleBy / 2, 0.25f);
        toButton(select, audioManager);
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                audioManager.playSound(hoverSound);
                super.enter(event, x, y, pointer, fromActor);
            }
        });
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        addAction(Actions.alpha(alpha));
    }

    private void pulseToBeat(@NotNull TimingPoint timingPoint, boolean isQuad) {
        float amountScale = scaleBy;
        amountScale = isQuad? amountScale * 2 : amountScale;

        float scaleIn = -amountScale;
        float scaleOut = +amountScale;
        float time = (float) (timingPoint.getBeatLength() / 1000) / 2;

        addAction(Actions.sequence(
                Actions.scaleBy(scaleIn, scaleIn, time),
                Actions.run(() -> {
                    if (isQuad) {
                        audioManager.playSound(downbeat);
                    } else {
                        audioManager.playSound(heartbeat);
                    }
                }),
                Actions.scaleBy(scaleOut, scaleOut, time)
        ));
    }

    @Override
    public void onNewBeat(TimingPoint timingPoint) {
        pulseToBeat(timingPoint, false);
    }

    @Override
    public void onFourthBeat(TimingPoint timingPoint) {
        pulseToBeat(timingPoint, true);
    }

    public void restartTriangleColor(@NotNull Triangle triangle) {
        triangle.color.a = alpha;
    }

    public float[] getRandomTriangleVertices() {
        float randomPositionX = random.nextInt(75);
        int minY = (int) (getY() + getHeight() / 3);
        int maxY = (int) ((getY() + getHeight() / 2));
        float randomPositionY = random.nextInt(maxY-minY)+minY;
        randomPositionX = randomPositionX % 2 == 0? +randomPositionX : -randomPositionX;
        float pos = getX() + getWidth() / 2 + randomPositionX;
        float size = 200 * getScaleX();
        return new float[] {
                pos-size, randomPositionY,
                pos+size, randomPositionY,
                pos, size*2+randomPositionY
        };
    }

    public void generateTriangles(float delta) {
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Triangle triangle: triangles) {
            float heightLimit = getY() + getHeight() * 0.5727f;
            if (triangle.getY() > heightLimit) {
                triangle.setVertices(getRandomTriangleVertices());
                restartTriangleColor(triangle);
            } else {
                triangle.moveY(100 * delta);
                triangle.draw(shapeRenderer);
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
    }

    public void colorLayer() {
        Color color = Color.PINK.cpy();
        color.a = alpha;
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, (getScaleX() * getWidth()) * 0.45f);
        shapeRenderer.end();
    }
}
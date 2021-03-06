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
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Interfaces.ResizeListener;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Timing.BeatListener;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class MainLogo extends MenuLogo implements BeatListener, ResizeListener {
    private final float scaleBy = 0.025f;
    private final ShapeRenderer shapeRenderer;
    private boolean isClicked = false;
    public final Array<LogoButton> buttons = new Array<>();

    public MainLogo(
            OsuGame game, Texture texture,
            Sound heartbeat, Sound downbeat, Sound select, Sound hoverSound
    ) {
        super(game, texture, heartbeat, downbeat, select, hoverSound);
        shapeRenderer = game.shapeRenderer;
        toEnterExitScaledImage(scaleBy / 2, 0.25f);
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverSound.play();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                select.stop();
                select.play();
                isClicked = !(getX() == getCenterX(game.viewport));
                float time = 0.25f;
                for (LogoButton logoButton: buttons) {
                    logoButton.addAction(isClicked? Actions.fadeOut(time) : Actions.fadeIn(time));
                }
                float moveX = isClicked? getCenterX(game.viewport) : getX() - getWidth() / 6;
                addAction(Actions.rotateBy(360, time));
                addAction(Actions.moveTo(moveX, getCenterY(game.viewport), time));
                return false;
            }
        });
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
                        downbeat.play();
                        beatCircles.add(new FourthBeatCircle(this, timingPoint));
                    } else {
                        heartbeat.play();
                        beatCircles.add(new BeatCircle(this, timingPoint));
                    }
                }),
                Actions.scaleBy(scaleOut, scaleOut, time)
        ));
    }

    private final Array<BeatCircle> beatCircles = new Array<>();

    @Override
    public void onNewBeat(TimingPoint timingPoint) {
        pulseToBeat(timingPoint, false);
    }

    @Override
    public void onFourthBeat(TimingPoint timingPoint) {
        pulseToBeat(timingPoint, true);
    }

    private final Color colorLayerColor = Color.PINK.cpy();

    public void colorLayer() {
        shapeRenderer.setColor(colorLayerColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
                getX() + getWidth() / 2,
                getY() + getHeight() / 2,
                ((getWidth() / 2) * getScaleX()) * 0.9f
        );
        shapeRenderer.end();
    }

    public void renderBeatCircles(float delta) {
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (BeatCircle beatCircle: beatCircles) {
            beatCircle.update(delta);
            if (beatCircle.color.a == 0) {
                beatCircles.removeValue(beatCircle, true);
            } else {
                shapeRenderer.setColor(beatCircle.color);
                shapeRenderer.circle(beatCircle.x, beatCircle.y, beatCircle.radius);
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void act(float delta) {
        for (Action action: getActions()) {
            if (action instanceof MoveToAction) {
                for (LogoButton logoButton: buttons) {
                    logoButton.onLogoMove();
                }
                break;
            }
        }
        super.act(delta);
    }

    @Override
    public void onResize() {
        applyCentering(getStage().getViewport());
        for (LogoButton logoButton: buttons) {
            logoButton.getColor().a = 0;
        }
    }
}

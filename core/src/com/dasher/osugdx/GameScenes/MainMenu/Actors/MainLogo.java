package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Interfaces.ResizeListener;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Timing.BeatListener;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;
import org.jetbrains.annotations.NotNull;

public class MainLogo extends MenuLogo implements BeatListener, ResizeListener {
    private final float scaleBy = 0.025f;
    private final ShapeRenderer shapeRenderer;
    private final Vector2 baseVec;
    private boolean isClicked;
    public final Array<LogoButton> buttons = new Array<>();

    public MainLogo(
            OsuGame game, Texture texture,
            Sound heartbeat, Sound downbeat, Sound select, Sound hoverSound
    ) {
        super(game, texture, heartbeat, downbeat, select, hoverSound);
        baseVec = new Vector2(getX(), getY());
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
                boolean isClicked = !(getX() == baseVec.x && getY() == baseVec.y);
                onClick(isClicked);
                return false;
            }
        });
    }

    private void onClick(boolean isClicked) {
        this.isClicked = isClicked;
        float time = 0.25f;
        if (isClicked) {
            for (LogoButton logoButton: buttons) {
                logoButton.addAction(Actions.fadeOut(time));
            }
            addAction(Actions.moveTo(baseVec.x, baseVec.y, time));
        } else {
            for (LogoButton logoButton: buttons) {
                logoButton.addAction(Actions.fadeIn(time));
            }
            float moveTo = getX() - getWidth() / 5;
            addAction(Actions.moveTo(moveTo, baseVec.y, time));
        }
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
                    } else {
                        heartbeat.play();
                    }
                }),
                Actions.scaleBy(scaleOut, scaleOut, time)
        ));
    }

    private final Array<BeatCircle> beatCircles = new Array<>();

    @Override
    public void onNewBeat(TimingPoint timingPoint) {
        pulseToBeat(timingPoint, false);
        beatCircles.add(new BeatCircle(this, timingPoint));
    }

    @Override
    public void onFourthBeat(TimingPoint timingPoint) {
        pulseToBeat(timingPoint, true);
        beatCircles.add(new FourthBeatCircle(this, timingPoint));
    }

    private final Color colorLayerColor = Color.PINK.cpy();

    public void colorLayer() {
        colorLayerColor.a = getParent().getColor().a;
        shapeRenderer.setColor(colorLayerColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
                getX() + getWidth() / 2,
                getY() + getHeight() / 2,
                getWidth() * 0.45f * getScaleX(),
                64
        );
        shapeRenderer.end();
    }

    public void renderBeatCircles(float delta) {
        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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
        Gdx.gl.glDisable(GL30.GL_BLEND);
        Gdx.gl.glLineWidth(1);
    }

    @Override
    public void act(float delta) {
        baseVec.set(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
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
        baseVec.set(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
        onClick(true);
    }

    public boolean isClicked() {
        return isClicked;
    }
}

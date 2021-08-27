package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Interfaces.ResizeListener;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Timing.BeatListener;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class MainLogo extends MenuLogo implements BeatListener, ResizeListener {
    private final float scaleBy = 0.025f;
    private final ShapeRenderer shapeRenderer;
    private final Vector2 baseVec;
    public final Array<LogoButton> buttons = new Array<>();

    public MainLogo(
            OsuGame game, Texture texture,
            Sound heartbeat, Sound downbeat, Music select, Sound hoverSound
    ) {
        super(game, texture, heartbeat, downbeat, select, hoverSound);
        baseVec = new Vector2(getX(), getY());
        shapeRenderer = game.shapeRenderer;
        toEnterExitScaledImage(scaleBy / 2, 0.25f);
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                audioManager.playSound(hoverSound);
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!select.isPlaying()) {
                    audioManager.playMusicSFX(select);
                }
                boolean isClicked = !(getX() == baseVec.x && getY() == baseVec.y);
                onClick(isClicked);
                return false;
            }
        });
    }

    private void onClick(boolean isClicked) {
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
            float moveTo = getX() - getWidth() / 4;
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

    private final Color colorLayerColor = Color.PINK.cpy();

    private void colorLayer() {
        colorLayerColor.a = getParent().getColor().a;
        shapeRenderer.setColor(colorLayerColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
                getX() + getWidth() / 2,
                getY() + getHeight() / 2,
                getWidth() * 0.45f * getScaleX()
        );
        shapeRenderer.end();
    }

    @Override
    public void act(float delta) {
        baseVec.set(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
        colorLayer();
        super.act(delta);
    }

    @Override
    public void onResize() {
        baseVec.set(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
        onClick(true);
    }
}

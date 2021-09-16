package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public abstract class LogoButton extends Image {
    private final MainLogo logo;
    private final int pos;
    protected final OsuGame game;
    private final float moveBy = getWidth() * 0.05f;

    private boolean isClickable() {
        return getColor().a == 1;
    }

    public LogoButton(OsuGame game, Texture texture, @NotNull MainLogo logo, Sound hoverSound, int pos) {
        super(texture);

        this.game = game;
        this.logo = logo;
        this.pos = pos;
        getColor().a = 0;
        logo.buttons.add(this);
        float time = 0.125f;
        onLogoMove();

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (isClickable()) {
                    hoverSound.play();
                    addAction(Actions.moveTo(getTargetX() + moveBy, getY(), time));
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (isClickable()) {
                    addAction(Actions.moveTo(getTargetX() - moveBy, getY(), time));
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isClickable()) {
                    onClick();
                }
                return false;
            }
        });
    }

    private float getTargetX() {
        return (logo.getX() + getWidth() * 0.9f) - ((getWidth() * 0.02f) * pos);
    }

    private float getTargetY() {
        return ((logo.getY() + logo.getHeight() * 0.55f) - ((getHeight() * 1.25f) * pos));
    }

    public void onLogoMove() {
        setPosition(getTargetX() - moveBy, getTargetY());
    }

    abstract void onClick();
}

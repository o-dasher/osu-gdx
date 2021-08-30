package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.jetbrains.annotations.NotNull;

public class LogoButton extends Image {
    private final MainLogo logo;
    private final int pos;

    public LogoButton(Texture texture, @NotNull MainLogo logo, Sound hoverSound, int pos) {
        super(texture);

        this.logo = logo;
        this.pos = pos;
        getColor().a = 0;
        onLogoMove();
        logo.buttons.add(this);

        float time = 0.125f;
        float moveBy = getWidth() * 0.05f;

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverSound.play();
                addAction(Actions.moveTo(getTargetX() + moveBy, getY(), time));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                addAction(Actions.moveTo(getTargetX() - moveBy, getY(), time));
            }
        });
    }

    private float getTargetX() {
        float x = logo.getX() + getWidth() * 0.9f;
        for (int i = 0; i < pos; i++) {
            x -= i == 0? 0 : getWidth() * 0.02f;
        }
        return x;
    }

    private float getTargetY() {
        float y = (logo.getY() + logo.getHeight() * 0.55f);
        for (int i = 0; i < pos; i++) {
            y -= i == 0? 0 : getHeight() * 1.25f;
        }
        return y;
    }

    public void onLogoMove() {
        setPosition(getTargetX(), getTargetY());
    }
}

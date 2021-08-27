package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import org.jetbrains.annotations.NotNull;

public class LogoButton extends Image {
    private final MainLogo logo;
    private final int pos;

    public LogoButton(Texture texture, @NotNull MainLogo logo, int pos) {
        super(texture);
        this.logo = logo;
        this.pos = pos;
        addAction(Actions.fadeOut(0));
        logo.buttons.add(this);
    }

    @Override
    public void act(float delta) {
        float x = logo.getX() + getWidth() * 0.8f;
        float y = (logo.getY() + logo.getHeight() / 2);
        for (int i = 0; i < pos; i++) {
            y = i == 0? y : y - getHeight();
        }
        setPosition(x, y);
        super.act(delta);
    }
}

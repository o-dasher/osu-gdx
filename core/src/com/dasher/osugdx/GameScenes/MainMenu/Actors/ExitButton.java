package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.jetbrains.annotations.NotNull;

public class ExitButton extends LogoButton {
    public ExitButton(Texture texture, @NotNull MainLogo logo, int pos) {
        super(texture, logo, pos);
    }
}

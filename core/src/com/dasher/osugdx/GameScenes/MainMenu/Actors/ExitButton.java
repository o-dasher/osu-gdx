package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class ExitButton extends LogoButton {
    public ExitButton(OsuGame game, Texture texture, @NotNull MainLogo logo, Sound hoverSound, int pos) {
        super(game, texture, logo, hoverSound, pos);
    }

    @Override
    void onClick() {

    }
}

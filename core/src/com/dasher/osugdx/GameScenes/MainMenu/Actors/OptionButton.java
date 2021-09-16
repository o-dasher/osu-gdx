package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class OptionButton extends LogoButton {
    public OptionButton(OsuGame game, Texture texture, @NotNull MainLogo logo, Sound hover, int pos) {
        super(game, texture, logo, hover, pos);
    }

    @Override
    void onClick() {

    }
}

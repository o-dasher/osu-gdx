package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
import com.dasher.osugdx.OsuGame;


import org.jetbrains.annotations.NotNull;


public class PlayButton extends LogoButton {
    public PlayButton(OsuGame game, Texture texture, @NotNull MainLogo logo, Sound hover, int pos) {
        super(game, texture, logo, hover, pos);
    }

    @Override
    void onClick() {
        ((MenuScreen) game.getScreen()).toSoundSelectScreen();
    }
}

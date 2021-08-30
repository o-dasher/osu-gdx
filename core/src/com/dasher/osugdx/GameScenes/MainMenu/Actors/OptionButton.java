package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import org.jetbrains.annotations.NotNull;

public class OptionButton extends LogoButton {
    public OptionButton(Texture texture, @NotNull MainLogo logo, Sound hover, int pos) {
        super(texture, logo, hover, pos);
    }
}

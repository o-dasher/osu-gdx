package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.Audio.GameSound;
import org.jetbrains.annotations.NotNull;

public class ExitButton extends LogoButton {
    public ExitButton(Texture texture, @NotNull MainLogo logo, GameSound hoverSound, int pos) {
        super(texture, logo, hoverSound, pos);
    }
}

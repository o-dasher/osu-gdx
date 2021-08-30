package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.OsuGame;

public class OverlayLogo extends MenuLogo {
    public OverlayLogo(OsuGame game, Texture texture, Sound heartbeat, Sound downbeat, Sound select, Sound hoverSound) {
        super(game, texture, heartbeat, downbeat, select, hoverSound);
        getColor().a = 0.5f;
        setScale(getScaleX() * 0.95f);
    }
}

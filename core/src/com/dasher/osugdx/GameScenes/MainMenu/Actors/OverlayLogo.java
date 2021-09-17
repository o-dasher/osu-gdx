package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.dasher.osugdx.OsuGame;

public class OverlayLogo extends MenuLogo {
    private final MainLogo mainLogo;

    public OverlayLogo(OsuGame game, Texture texture, Sound heartbeat, Sound downbeat, Sound select, Sound hoverSound, MainLogo mainLogo) {
        super(game, texture, heartbeat, downbeat, select, hoverSound);
        this.mainLogo = mainLogo;
        getColor().a = 0.5f;
        setScale(getScaleX() * 0.95f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setPosition(mainLogo.getX(), mainLogo.getY());
        setRotation(mainLogo.getRotation());
    }
}

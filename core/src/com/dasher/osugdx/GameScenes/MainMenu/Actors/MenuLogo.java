package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;

public abstract class MenuLogo extends GameImage {
    protected final AudioManager audioManager;
    protected final Sound heartbeat;
    protected final Sound downbeat;
    protected final Sound hoverSound;
    protected final Sound select;

    public MenuLogo(OsuGame game, Texture texture, Sound heartbeat, Sound downbeat, Sound select, Sound hoverSound) {
        super(game, texture, false);
        audioManager = game.audioManager;
        this.heartbeat = heartbeat;
        this.downbeat = downbeat;
        this.hoverSound = hoverSound;
        this.select = select;

        setPosition(CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()));
        setOrigin(Align.center);
        setAlign(Align.center);
        setBaseScale(0.5f);
        setScale(getBaseScale());
    }

}
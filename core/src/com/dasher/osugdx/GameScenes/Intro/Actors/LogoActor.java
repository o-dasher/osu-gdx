package com.dasher.osugdx.GameScenes.Intro.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.OsuGame;

import com.dasher.osugdx.Images.GameImage;

public class LogoActor extends GameImage {
    public LogoActor(OsuGame game, Texture logoTexture) {
        super(game, logoTexture, true);
        setScale(0.5f);
        applyToGameScale();
    }
}

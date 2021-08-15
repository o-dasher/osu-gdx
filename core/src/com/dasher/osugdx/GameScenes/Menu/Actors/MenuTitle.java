package com.dasher.osugdx.GameScenes.Menu.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.OsuGame;

import com.dasher.osugdx.Images.GameImage;

public class MenuTitle extends GameImage {
    public MenuTitle(OsuGame game, Texture texture, boolean forceCenter) {
        super(game, texture, forceCenter);

        setPosition(com.dasher.osugdx.Framework.Helpers.CenteringHelper.getCenterX(getWidth()), CenteringHelper.getCenterY(getHeight()) * 2);
        setScale(0.75f);
        toEnterExitScaledImage();
    }
}

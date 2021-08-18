package com.dasher.osugdx.GameScenes.MainMenu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Stages.SwitcherStage;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.MenuBackground;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.MenuPlayButton;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.MenuTitle;


public class MenuStage extends SwitcherStage {
    protected com.dasher.osugdx.GameScenes.MainMenu.Actors.MenuBackground menuBackground;

    public MenuStage(
            OsuGame game, Viewport viewport, Texture menuBackgroundTexture,
            Texture menuTitleTexture, Texture menuPlayButtonTexture, Sound menuButtonClickSound
    ) {
        super(game, viewport, true, true);

        addActor(menuBackground = new MenuBackground(game, menuBackgroundTexture, true));
        addActor(new MenuTitle(game, menuTitleTexture, false));
        addActor(new MenuPlayButton(game, menuPlayButtonTexture, false, this, menuButtonClickSound));

        fadeIn();
    }
}

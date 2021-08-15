package com.dasher.osugdx.GameScenes.Intro;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Stages.SwitcherStage;
import com.dasher.osugdx.GameScenes.Intro.Actors.LogoActor;
import com.dasher.osugdx.OsuGame;

public class IntroStage extends SwitcherStage {
    public IntroStage(OsuGame game, Viewport viewport, Texture logoTexture) {
        super(game, viewport, true, true);
        addActor(new LogoActor(game, logoTexture));
        fadeIn();
    }
}

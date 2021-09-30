package com.dasher.osugdx.GameScenes.Gameplay.Osu;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.dasher.osugdx.Framework.Actors.AnimatedSprite;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.OsuElements;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuObject;

public class OsuHitCircle extends OsuCircleObject {
    public OsuHitCircle(@NotNull OsuObject baseObject, OsuGame game, OsuPlayScreen screen) {
        super(baseObject, game, screen);
        init(
                new Image(skin.elements.get(OsuElements.HIT_CIRCLE).getSprite()),
                new Image(skin.elements.get(OsuElements.APPROACH_CIRCLE).getSprite()),
                new AnimatedSprite(skin.animatedElements.get(OsuElements.HIT_CIRCLE_OVERLAY).getAnimation())
        );
    }
}

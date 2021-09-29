package com.dasher.osugdx.GameScenes.Gameplay.Osu;

import com.badlogic.gdx.math.Vector2;
import com.dasher.osugdx.GameScenes.Gameplay.GameObject;
import com.dasher.osugdx.GameScenes.Gameplay.GamePlayScreen;
import com.dasher.osugdx.GameScenes.Gameplay.StatisticType;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuObject;

public abstract class OsuGameObject extends GameObject<OsuObject> {
    protected float approachTime;

    public OsuGameObject(@NotNull OsuObject baseObject, OsuGame game, GamePlayScreen screen) {
        super(baseObject, game, screen);
        approachTime = statistics.get(StatisticType.AR) / 1000;
        Vector2 position = baseObject.getPosition();
        setPosition(position.x, position.y);
    }
}

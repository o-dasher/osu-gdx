package com.dasher.osugdx.GameScenes.Gameplay.Osu;

import com.badlogic.gdx.utils.ObjectMap;
import com.dasher.osugdx.GameScenes.Gameplay.AbstractPlayScreen;
import com.dasher.osugdx.GameScenes.Gameplay.GameObject;
import com.dasher.osugdx.GameScenes.Gameplay.GameplayUtils;
import com.dasher.osugdx.GameScenes.Gameplay.StatisticType;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.HitObject;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuBeatmap;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuCircle;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuObject;

public class OsuPlayScreen extends AbstractPlayScreen<OsuObject, OsuBeatmap> {
    public OsuPlayScreen(@NotNull OsuGame game, @NotNull OsuBeatmap beatmap) {
        super(game, beatmap);
    }

    @Override
    public OsuGameObject getObject(HitObject hitObjectData) {
        if (hitObjectData instanceof OsuObject) {
            if (hitObjectData instanceof OsuCircle) {
                OsuHitCircle object = new OsuHitCircle((OsuObject) hitObjectData, game, this);
                ((OsuHitCircle) object).onResize();
                return object;
            }
        }
        return null;
    }

    @Override
    public OsuBeatmap getBeatmap(Beatmap beatmap) {
        return (OsuBeatmap) beatmapUtils.createMap(beatmap);
    }

    @Override
    public void putStatistics(ObjectMap<StatisticType, Float> statisticTimes) {
        statisticTimes.put(StatisticType.CS, (float) gameplayBeatmap.getCS());
        statisticTimes.put(StatisticType.AR, GameplayUtils.mapDifficultyRange((float) ((OsuBeatmap) gameplayBeatmap).getAR(), 1800, 1200, 450));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (GameObject<OsuObject> object: gameObjects) {
            if (object instanceof OsuCircleObject) {
                ((OsuCircleObject) object).onResize();
            }
        }
    }
}

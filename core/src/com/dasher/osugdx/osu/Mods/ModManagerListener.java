package com.dasher.osugdx.osu.Mods;

import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public interface ModManagerListener {
    void onBeatmapCalculated(Beatmap beatmap);
    void onCompleteCalculation(Array<BeatMapSet> calculatedBeatmapSets);
}

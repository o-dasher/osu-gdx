package com.dasher.osugdx.osu.Mods;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public interface ModManagerListener {
    void onBeatmapCalculated(Beatmap beatmap);
}

package com.dasher.osugdx.IO.Beatmaps;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public interface BeatmapManagerListener {
    void onNewBeatmap(Beatmap beatmap);
}

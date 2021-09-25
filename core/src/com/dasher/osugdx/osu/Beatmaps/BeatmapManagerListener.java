package com.dasher.osugdx.osu.Beatmaps;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public interface BeatmapManagerListener {
    void onNewBeatmap(Beatmap beatmap);
    void onNewBeatmapSet(BeatMapSet beatMapSet);
    void onPreBeatmapChange();;
}

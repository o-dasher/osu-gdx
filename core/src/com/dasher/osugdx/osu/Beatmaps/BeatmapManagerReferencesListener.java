package com.dasher.osugdx.osu.Beatmaps;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public interface BeatmapManagerReferencesListener {
    Beatmap getBeatmapReference();
    void setBeatmapReference(Beatmap beatmap);
}

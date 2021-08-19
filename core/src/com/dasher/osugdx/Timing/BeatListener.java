package com.dasher.osugdx.Timing;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public interface BeatListener {
    void onFourthBeat(TimingPoint timingPoint);
    void onNewBeat(TimingPoint timingPoint);
}

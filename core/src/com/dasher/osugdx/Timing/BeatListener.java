package com.dasher.osugdx.Timing;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public interface BeatListener {
    default double getBeatDivisor() { return 1; }
    void onFourthBeat(TimingPoint timingPoint);
    void onNewBeat(TimingPoint timingPoint);
}

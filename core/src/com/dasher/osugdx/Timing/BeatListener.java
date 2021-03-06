package com.dasher.osugdx.Timing;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public interface BeatListener {
    default double getBeatDivisor() { return 1; }
    default float getMinimalBeatLength() {
        return 250 / 1000f;
    }
    default void onFourthBeat(TimingPoint timingPoint) {
        onNewBeat(timingPoint);
    };
    void onNewBeat(TimingPoint timingPoint);
}

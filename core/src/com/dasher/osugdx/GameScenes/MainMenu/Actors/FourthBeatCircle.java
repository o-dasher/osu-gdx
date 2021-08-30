package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class FourthBeatCircle extends BeatCircle {
    public FourthBeatCircle(MainLogo logo, TimingPoint timingPoint) {
        super(logo, timingPoint);
        radiusIncrease *= 2;
    }
}

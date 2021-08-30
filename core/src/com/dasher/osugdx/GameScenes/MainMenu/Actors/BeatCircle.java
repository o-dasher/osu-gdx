package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.dasher.osugdx.Timing.BeatListener;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;


public class BeatCircle extends Circle {
    private final MainLogo logo;
    private final TimingPoint timingPoint;
    protected float radiusIncrease = 100;
    public Color color = Color.WHITE.cpy();

    public BeatCircle(MainLogo logo, TimingPoint timingPoint) {
        super(logo.getX(), logo.getY(), logo.getWidth() * 0.45f * logo.getScaleX());
        this.logo = logo;
        this.timingPoint = timingPoint;
    }

    public void update(float delta) {
        x = logo.getX() + logo.getWidth() / 2;
        y = logo.getY() + logo.getHeight() / 2;
        radius += radiusIncrease * delta + timingPoint.getBeatLength() / 1000;
        color.a = Math.max(0, color.a - delta);
    }
}

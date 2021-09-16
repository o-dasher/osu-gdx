package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;


public class BeatCircle extends Circle {
    private final MainLogo logo;
    private final TimingPoint timingPoint;
    private final float baseAlpha = 0.25f;
    protected float radiusIncrease = 100;
    public Color color = Color.WHITE.cpy();

    public BeatCircle(@NotNull MainLogo logo, TimingPoint timingPoint) {
        super(logo.getX(), logo.getY(), (logo.getWidth() / 2) * logo.getScaleX());
        this.logo = logo;
        this.timingPoint = timingPoint;
        color.a = baseAlpha;
    }

    public void update(float delta) {
        x = logo.getX() + logo.getWidth() / 2;
        y = logo.getY() + logo.getHeight() / 2;
        radius += radiusIncrease * delta + timingPoint.getBeatLengthS();
        color.a = (float) Math.max(0, color.a - (delta + timingPoint.getBeatLengthS() * delta) * baseAlpha);
    }
}

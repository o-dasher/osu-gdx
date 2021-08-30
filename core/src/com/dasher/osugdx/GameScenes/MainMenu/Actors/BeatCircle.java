package com.dasher.osugdx.GameScenes.MainMenu.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;


public class BeatCircle extends Circle {
    private final MainLogo logo;
    protected float radiusIncrease = 100;
    public Color color = Color.WHITE.cpy();

    public BeatCircle(MainLogo logo) {
        super(logo.getX(), logo.getY(), logo.getWidth() * 0.45f * logo.getScaleX());
        this.logo = logo;
    }

    public void update(float delta) {
        x = logo.getX() + logo.getWidth() / 2;
        y = logo.getY() + logo.getHeight() / 2;
        radius += radiusIncrease * delta;
        color.a = color.a < 0? 0 : color.a - 0.75f * delta;
    }
}

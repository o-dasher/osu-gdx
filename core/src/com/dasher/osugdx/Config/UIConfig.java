package com.dasher.osugdx.Config;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class UIConfig {
    private float scale = 1;

    public float getScale() {
        return Gdx.app.getType() == Application.ApplicationType.Android? scale : scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}

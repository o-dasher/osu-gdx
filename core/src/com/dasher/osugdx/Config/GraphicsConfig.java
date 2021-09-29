package com.dasher.osugdx.Config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class GraphicsConfig {
    private boolean isPostProcessingEnabled = false;

    public boolean isPostProcessingEnabled() {
        return isPostProcessingEnabled;
    }

    public void setPostProcessingEnabled(boolean postProcessingEnabled) {
        isPostProcessingEnabled = postProcessingEnabled;
    }
}

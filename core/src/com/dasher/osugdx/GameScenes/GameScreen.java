package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.assets.GameAssetManager;

import org.jetbrains.annotations.NotNull;

public abstract class GameScreen implements Screen {
    protected OsuGame game;
    protected GameAssetManager assetManager;
    protected AudioManager audioManager;
    protected UIConfig uiConfig;
    protected Batch batch;
    protected Viewport viewport;
    protected BuffedShapeRenderer shapeRenderer;
    protected GlyphLayout glyphLayout;
    protected BeatMapStore beatMapStore;

    public GameScreen(@NotNull OsuGame game) {
        this.game = game;
        assetManager = game.assetManager;
        audioManager = game.audioManager;
        uiConfig = game.uiConfig;
        batch = game.batch;
        viewport = game.viewport;
        shapeRenderer = game.shapeRenderer;
        glyphLayout = game.glyphLayout;
        beatMapStore = game.beatMapStore;
    }
}

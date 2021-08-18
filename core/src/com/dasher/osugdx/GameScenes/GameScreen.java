package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.assets.GameAssetManager;

import org.jetbrains.annotations.NotNull;

public abstract class GameScreen implements Screen {
    protected final OsuGame game;
    protected final GameAssetManager assetManager;
    protected final AudioManager audioManager;
    protected final UIConfig uiConfig;
    protected final Batch batch;
    protected final Viewport viewport;
    protected final BuffedShapeRenderer shapeRenderer;
    protected final  GlyphLayout glyphLayout;
    protected final BeatMapStore beatMapStore;
    protected final BeatmapManager beatmapManager;
    protected final BeatmapUtils beatmapUtils;

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
        beatmapManager = game.beatmapManager;
        beatmapUtils = game.beatmapUtils;
    }
}

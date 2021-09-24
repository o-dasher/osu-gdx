package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.FadeBlock;
import com.dasher.osugdx.Graphics.Fonts;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.IO.Beatmaps.OSZParser;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinManager;
import com.dasher.osugdx.Timing.BeatFactory;
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
    protected final BeatFactory beatFactory;
    protected final WorkingBackground workingBackground;
    protected final InputMultiplexer inputMultiplexer;
    protected final Stage backgroundStage;
    protected final OSZParser oszParser;
    protected final SkinManager skinManager;
    protected final Fonts fonts;
    protected float cleanupTime;
    protected AsyncExecutor asyncExecutor;

    public GameScreen(@NotNull OsuGame game) {
        this.game = game;
        cleanupTime = game.cleanupTime;
        assetManager = game.assetManager;
        audioManager = game.audioManager;
        fonts = game.fonts;
        uiConfig = game.uiConfig;
        batch = game.batch;
        viewport = game.viewport;
        shapeRenderer = game.shapeRenderer;
        glyphLayout = game.glyphLayout;
        beatMapStore = game.beatMapStore;
        beatmapManager = game.beatmapManager;
        beatmapUtils = game.beatmapUtils;
        beatFactory = game.beatFactory;
        backgroundStage = game.backgroundStage;
        workingBackground = game.workingBackground;
        inputMultiplexer = game.inputMultiplexer;
        asyncExecutor = game.asyncExecutor;
        skinManager = game.skinManager;
        oszParser = game.oszParser;
    }

    @Override
    public void show() {
        inputMultiplexer.clear();
    }

    protected void renderFade(float delta) {
        if (game.calledToSwitchScreen) {
            game.fadeBlock.update(delta);
        }
    }

    @Override
    public void render(float delta) {
    }

    public void switchScreen(Screen screen) {
        if (!game.calledToSwitchScreen) {
            game.calledToSwitchScreen = true;
            game.nextScreen = screen;
            game.fadeBlock.setFade(true);
        }
    }
}

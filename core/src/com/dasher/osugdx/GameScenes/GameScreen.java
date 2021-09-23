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
    private Screen nextScreen;

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

    private static boolean isFirstFadeRender = true;
    private static boolean calledToSwitchScreen = false;
    private static final Color fadeBlockColor = Color.BLACK.cpy();
    private static boolean isFadingIn = false;
    private static boolean isFadingOut = false;

    @Override
    public void show() {
        inputMultiplexer.clear();
    }

    protected void renderFade(float delta) {
        if (calledToSwitchScreen) {
            if (isFirstFadeRender) {
                isFirstFadeRender = false;
                fadeBlockColor.a = 0;
            }
            if (isFadingIn || isFadingOut) {
                if (isFadingIn) {
                    fadeBlockColor.a = Math.min(1, fadeBlockColor.a + delta / cleanupTime);
                    if (fadeBlockColor.a >= 1) {
                        isFadingOut = true;
                        isFadingIn = false;
                        game.getScreen().dispose();
                        game.setScreen(nextScreen);
                    }
                } else {
                    fadeBlockColor.a = Math.max(0, fadeBlockColor.a - delta / cleanupTime);
                    if (fadeBlockColor.a <= 0) {
                        isFadingOut = false;
                        calledToSwitchScreen = false;
                        isFirstFadeRender = true;
                    }
                }
                Gdx.gl.glEnable(GL30.GL_BLEND);
                Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
                shapeRenderer.setColor(fadeBlockColor);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                shapeRenderer.end();
            }
        }
    }

    @Override
    public void render(float delta) {
    }

    public void switchScreen(Screen screen) {
        if (!calledToSwitchScreen) {
            nextScreen = screen;
            calledToSwitchScreen = true;
            isFadingIn = true;
        }
    }
}

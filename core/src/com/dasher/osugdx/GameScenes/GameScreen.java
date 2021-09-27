package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.dasher.osugdx.Audio.AudioFactory;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.Graphics.Fonts;
import com.dasher.osugdx.osu.Beatmaps.BeatMapStore;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;
import com.dasher.osugdx.osu.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.osu.Beatmaps.OSZParser;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinManager;
import com.dasher.osugdx.Timing.BeatFactory;
import com.dasher.osugdx.assets.GameAssetManager;
import com.dasher.osugdx.osu.Mods.ModManager;
import com.rafaskoberg.gdx.parrot.Parrot;

import org.jetbrains.annotations.NotNull;

public abstract class GameScreen implements Screen {
    protected OsuGame game;
    protected GameAssetManager assetManager;
    protected UIConfig uiConfig;
    protected Batch batch;
    protected Viewport viewport;
    protected BuffedShapeRenderer shapeRenderer;
    protected GlyphLayout glyphLayout;
    protected BeatMapStore beatMapStore;
    protected BeatmapManager beatmapManager;
    protected BeatmapUtils beatmapUtils;
    protected BeatFactory beatFactory;
    protected WorkingBackground workingBackground;
    protected InputMultiplexer inputMultiplexer;
    protected Stage backgroundStage;
    protected OSZParser oszParser;
    protected SkinManager skinManager;
    protected Fonts fonts;
    protected float cleanupTime;
    protected AsyncExecutor asyncExecutor;
    protected AudioFactory audioFactory;
    protected ModManager modManager;
    protected Parrot parrot;
    protected VfxManager vfxManager;
    protected GaussianBlurEffect backgroundBlurEffect;

    public GameScreen(@NotNull OsuGame game) {
        this.game = game;
        resetGlobals();
    }

    public void resetGlobals() {
        cleanupTime = game.cleanupTime;
        assetManager = game.assetManager;
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
        audioFactory = game.audioFactory;
        modManager = game.modManager;
        parrot = game.parrot;
        vfxManager = game.vfxManager;
        backgroundBlurEffect = game.backgroundBlurEffect;
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

    protected void renderBackground(float delta) {
        viewport.apply(true);
        backgroundStage.act(delta);
        vfxManager.addEffect(backgroundBlurEffect);
        vfxManager.beginInputCapture();
        backgroundStage.draw();
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
        vfxManager.removeEffect(backgroundBlurEffect);
        viewport.apply();
    }

    public void switchScreen(Screen screen) {
        if (!game.calledToSwitchScreen) {
            game.calledToSwitchScreen = true;
            game.nextScreen = screen;
            game.fadeBlock.setFade(true);
        }
    }
}

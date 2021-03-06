package com.dasher.osugdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.dasher.osugdx.Audio.AudioFactory;
import com.dasher.osugdx.Config.GameConfig;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.FadeBlock;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Tasks.ClockTask;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.GameScenes.WorkingBackground;
import com.dasher.osugdx.Graphics.Fonts;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatMapStore;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.osu.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.osu.Beatmaps.OSZParser;
import com.dasher.osugdx.IO.GameIO;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;
import com.dasher.osugdx.Skins.SkinManager;
import com.dasher.osugdx.Timing.BeatFactory;
import com.dasher.osugdx.assets.GameAssetManager;
import com.dasher.osugdx.osu.Mods.ModManager;
import com.rafaskoberg.gdx.parrot.Parrot;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class OsuGame extends Game implements BeatmapManagerListener {
	public VfxManager vfxManager;
	public SpriteBatch batch;
	public Viewport viewport;
	public GameAssetManager assetManager;
	public InputMultiplexer inputMultiplexer;
	public BuffedShapeRenderer shapeRenderer;
	public GlyphLayout glyphLayout;
	public Fonts fonts;
	public GameIO gameIO;
	public String gameName;
	public BeatMapStore beatMapStore;
	public Json json;
	public OSZParser oszParser;
	public PlatformToast toast;
	public BeatmapManager beatmapManager;
	public BeatmapUtils beatmapUtils;
	public BeatFactory beatFactory;
	public Random random;
	public WorkingBackground workingBackground;
	public Stage backgroundStage;
	public AsyncExecutor asyncExecutor;
	public SkinManager skinManager;
	public float cleanupTime;
	public FadeBlock fadeBlock;
	public Screen nextScreen;
	public AudioFactory audioFactory;
	public ModManager modManager;
	public boolean calledToSwitchScreen;
	public boolean canSwitchIntroScreen;
	public boolean loadedAllAssets;
	public Parrot parrot;
	public AsyncResult<Null> loadBeatmapsTask;
	public GaussianBlurEffect backgroundBlurEffect;
	public GameConfig config;
	public float currentMusicPosition;

	public int WORLD_WIDTH;
	public int WORLD_HEIGHT;

	public OsuGame(PlatformToast toast) {
		this.toast = toast;
	}

	public OsuGame(PlatformToast toast, @NotNull Runnable runnable) {
		this(toast);
		runnable.run();
	}

	@Override
	public void create () {
		gameName = "osu!gdx";
		WORLD_WIDTH = 800;
		WORLD_HEIGHT = 600;
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
		batch = new SpriteBatch();
		assetManager = new GameAssetManager();
		inputMultiplexer = new InputMultiplexer();
		parrot = new Parrot();
		cleanupTime = 0.3f;
		config = new GameConfig();
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		Gdx.input.setInputProcessor(inputMultiplexer);
		asyncExecutor = new AsyncExecutor(Runtime.getRuntime().availableProcessors(), "MAIN EXECUTOR");
		audioFactory = new AudioFactory(this);
		vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
		setScreen(new IntroScreen(this));
		loadBeatmapsTask = asyncExecutor.submit(() -> {
			json = new Json();
			random = new Random();
			gameIO = new GameIO();
			gameIO.setup(gameName);
			modManager = new ModManager(this);
			beatmapUtils = new BeatmapUtils();
			beatMapStore = new BeatMapStore(this);
			modManager.addListener(beatMapStore);
			beatmapUtils.setBeatMapStore(beatMapStore);
			beatmapManager = new BeatmapManager(this, beatMapStore, toast);
			oszParser = new OSZParser(gameIO, beatMapStore, beatmapManager);
			oszParser.addListener(beatMapStore);
			beatMapStore.setOszParser(oszParser);
			oszParser.parseImportDirectory();
			beatMapStore.loadCache();
			beatMapStore.loadAllBeatmaps();
			return null;
		});
		assetManager.load();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(
				GL20.GL_COLOR_BUFFER_BIT
						| GL20.GL_DEPTH_BUFFER_BIT
						| (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0)
		);

		viewport.apply();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		if (shapeRenderer != null) {
			shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
		}

		if (beatmapManager != null) {
			Music currentMusic = beatmapManager.getCurrentMusic();
			if (currentMusic != null) {
				currentMusicPosition = currentMusic.getPosition();
				if (currentMusic.isPlaying()) {
					beatFactory.update();
				}
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(WORLD_WIDTH, WORLD_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
		}

		float delta = Gdx.graphics.getDeltaTime();

		if (config.getGraphics().isPostProcessingEnabled()) {
			vfxManager.cleanUpBuffers();
			vfxManager.update(delta);
		}

		try {
			parrot.update(delta);
		} catch (Exception e) {
			e.printStackTrace();
		}

		audioFactory.update(delta);
		super.render();

		if (assetManager.update()) {
			loadedAllAssets = true;
			Screen currentScreen = getScreen();
			if (currentScreen instanceof IntroScreen && canSwitchIntroScreen && loadBeatmapsTask.isDone()) {
				canSwitchIntroScreen = false;
				beatmapManager.randomizeCurrentBeatmapSet();
				backgroundStage = new Stage(viewport, batch);
				skinManager = new SkinManager(this);
				shapeRenderer = new BuffedShapeRenderer();
				fadeBlock = new FadeBlock(Color.BLACK, shapeRenderer, viewport) {
					@Override
					public void onFadeIn() {
						getScreen().dispose();
						setScreen(nextScreen);
					}
					@Override
					public void onFadeOut() {
						calledToSwitchScreen = false;
					}
				};
				config.loadConfig();
				glyphLayout = new GlyphLayout();
				fadeBlock.setAlphaIncreaseDivisor(cleanupTime);
				fonts = new Fonts(assetManager);
				Texture bgTexture = assetManager.get(assetManager.textures.menuBackgrounds.random());
				workingBackground = new WorkingBackground(this, bgTexture);
				beatFactory = new BeatFactory(beatmapManager, this);
				backgroundStage.addActor(workingBackground);
				beatmapManager.addListener(workingBackground);
				beatmapManager.addListener(beatFactory);
				backgroundBlurEffect = new GaussianBlurEffect();
				backgroundBlurEffect.setPasses(5);
				skinManager.changeSkin(skinManager.getDefaultDir());
				((IntroScreen) currentScreen).setCanSwitchScreen(true);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		if (vfxManager.getResultBuffer() != null) {
			vfxManager.resize(width, height);
		}
		// CENTERINGHELPER SETTING SHOULD BE THE FIRST CALL AFTER VIEWPORT UPDATE ALWAYS
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
		System.out.println("New resolution: " + width + ", " + height);
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		assetManager.dispose();
		vfxManager.dispose();
		if (backgroundBlurEffect != null) {
			backgroundBlurEffect.dispose();
		}
		if (batch != null) {
			batch.dispose();
		}
		if (shapeRenderer != null) {
			shapeRenderer.dispose();
		}
	}


	@Override
	public void onNewBeatmap(Beatmap beatmap) {
	}

	@Override
	public void onNewBeatmapSet(BeatMapSet beatMapSet) {

	}

	@Override
	public void onPreBeatmapChange() {

	}
}

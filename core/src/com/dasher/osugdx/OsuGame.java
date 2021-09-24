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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.FadeBlock;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.GameScenes.WorkingBackground;
import com.dasher.osugdx.Graphics.Fonts;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.IO.Beatmaps.OSZParser;
import com.dasher.osugdx.IO.GameIO;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;
import com.dasher.osugdx.Skins.SkinManager;
import com.dasher.osugdx.Timing.BeatFactory;
import com.dasher.osugdx.assets.GameAssetManager;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class OsuGame extends Game implements BeatmapManagerListener {
	public SpriteBatch batch;
	public Viewport viewport;
	public GameAssetManager assetManager;
	public AudioManager audioManager;
	public UIConfig uiConfig;
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
	public boolean calledToSwitchScreen;

	private int WORLD_WIDTH;
	private int WORLD_HEIGHT;

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
		if (Gdx.app.getType() == Application.ApplicationType.Android) {
			float androidMultiplier = 0.85f;
			WORLD_WIDTH *= androidMultiplier;
			WORLD_HEIGHT *= androidMultiplier;
		}
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
		cleanupTime = 0.25f;
		json = new Json();
		batch = new SpriteBatch();
		shapeRenderer = new BuffedShapeRenderer();
		assetManager = new GameAssetManager();
		audioManager = new AudioManager();
		glyphLayout = new GlyphLayout();
		uiConfig = new UIConfig();
		random = new Random();
		inputMultiplexer = new InputMultiplexer();
		assetManager.load();
		gameIO = new GameIO();
		gameIO.setup(gameName);
		beatmapUtils = new BeatmapUtils();
		beatMapStore = new BeatMapStore(gameIO, json, beatmapUtils);
		beatmapUtils.setBeatMapStore(beatMapStore);
		oszParser = new OSZParser(gameIO, beatMapStore);
		beatMapStore.setOszParser(oszParser);
		beatmapManager = new BeatmapManager(this, beatMapStore, toast, beatmapUtils, audioManager);
		beatFactory = new BeatFactory(beatmapManager);
		beatmapManager.addListener(this);
		asyncExecutor = new AsyncExecutor(Runtime.getRuntime().availableProcessors());
		backgroundStage = new Stage(viewport, batch);
		skinManager = new SkinManager(this);
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
		fadeBlock.setAlphaIncreaseDivisor(cleanupTime);
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
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
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		Gdx.input.setInputProcessor(inputMultiplexer);

		Music currentMusic = beatmapManager.getCurrentMusic();
		if (currentMusic != null && currentMusic.isPlaying()) {
			beatFactory.update();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(WORLD_WIDTH, WORLD_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
		}

		super.render();

		if (assetManager.update()) {
			if (getScreen() == null) {
				fonts = new Fonts(assetManager);
				Texture bgTexture = assetManager.get(assetManager.textures.menuBackgrounds.random());
				workingBackground = new WorkingBackground(this, bgTexture);
				backgroundStage.addActor(workingBackground);
				beatmapManager.addListener(workingBackground);
				skinManager.changeSkin(skinManager.getDefaultDir());
				asyncExecutor.submit(() -> {
					oszParser.parseImportDirectory();
					beatMapStore.loadCache();
					beatMapStore.loadAllBeatmaps();
					Gdx.app.postRunnable(() -> beatmapManager.randomizeCurrentBeatmapSet());
					return null;
				});
				setScreen(new IntroScreen(this));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		// CENTERINGHELPER SETTING SHOULD BE THE FIRST CALL AFTER VIEWPORT UPDATE ALWAYS
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
		System.out.println("New resolution: " + width + ", " + height);
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		assetManager.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}


	@Override
	public void onNewBeatmap(Beatmap beatmap) {
		beatFactory.onNewBeatmap(beatmap);
	}

	@Override
	public void onNewBeatmapSet(BeatMapSet beatMapSet) {

	}
}

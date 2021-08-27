package com.dasher.osugdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.GameScenes.WorkingBackground;
import com.dasher.osugdx.Graphics.Fonts;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.IO.Beatmaps.OSZParser;
import com.dasher.osugdx.IO.GameIO;
import com.dasher.osugdx.PlatformSpecific.Toast.PlatformToast;
import com.dasher.osugdx.Timing.BeatFactory;
import com.dasher.osugdx.assets.GameAssetManager;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class OsuGame extends Game implements BeatmapManagerListener {
	public SpriteBatch batch;
	public Viewport viewport;
	public Viewport uiViewport;
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
	private Texture tempBackgroundTexture;

	private final int WORLD_WIDTH = 800;
	private final int WORLD_HEIGHT = 600;

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
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
		uiViewport = new ScreenViewport();
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
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
		beatMapStore = new BeatMapStore(gameIO, json, toast, beatmapUtils);
		beatmapUtils.setBeatMapStore(beatMapStore);
		oszParser = new OSZParser(gameIO, beatMapStore);
		beatMapStore.setOszParser(oszParser);
		beatmapManager = new BeatmapManager(this, beatMapStore, toast, beatmapUtils, audioManager);
		beatFactory = new BeatFactory(beatmapManager);
		tempBackgroundTexture = new Texture(Gdx.files.internal(assetManager.textures.menuBackground.fileName), true);
		workingBackground = new WorkingBackground(this, tempBackgroundTexture);
		beatmapManager.addListener(workingBackground);
		beatmapManager.addListener(this);
		asyncExecutor = new AsyncExecutor(Runtime.getRuntime().availableProcessors());
		backgroundStage = new Stage(viewport);
		backgroundStage.addActor(workingBackground);
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		asyncExecutor.submit(() -> {
			oszParser.parseImportDirectory();
			if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
				beatMapStore.loadCache();
			}
			beatMapStore.loadAllBeatmaps();
			Gdx.app.postRunnable(() -> 	beatmapManager.randomizeCurrentBeatmapSet());
			return null;
		});
	}

	@Override
	public void render() {
		ScreenUtils.clear(Color.BLACK);

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
				workingBackground.defaultTexture = assetManager.get(assetManager.textures.menuBackgrounds.random());
				if (workingBackground.getCurrentTexture().equals(tempBackgroundTexture)) {
					workingBackground.setBackground(workingBackground.defaultTexture);
				}
				tempBackgroundTexture.dispose();
				setScreen(new IntroScreen(this));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		// CENTERINGHELPER SETTING SHOLD BE THE FIRST CALL AFTER VIEWPORT UPDATE ALWAYS
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
}

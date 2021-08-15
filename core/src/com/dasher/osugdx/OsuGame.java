package com.dasher.osugdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Audio.AudioManager;
import com.dasher.osugdx.Config.UIConfig;
import com.dasher.osugdx.Framework.Graphics.Shaperendering.BuffedShapeRenderer;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.GameScenes.Intro.IntroScreen;
import com.dasher.osugdx.Graphics.Fonts;
import com.dasher.osugdx.IO.Beatmaps.BeatMapStore;
import com.dasher.osugdx.IO.GameIO;
import com.dasher.osugdx.assets.GameAssetManager;

public class OsuGame extends Game {
	public SpriteBatch batch;
	public Viewport viewport;
	public GameAssetManager assetManager;
	public AudioManager audioManager;
	public Camera camera;
	public UIConfig uiConfig;
	public InputMultiplexer inputMultiplexer;
	public BuffedShapeRenderer shapeRenderer;
	public GlyphLayout glyphLayout;
	public Fonts fonts;
	public GameIO gameIO;
	public String gameName;
	public BeatMapStore beatMapStore;
	public Json json;

	private final int WORLD_WIDTH = 1280;
	private final int WORLD_HEIGHT = 720;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		gameName = "osu!gdx";
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		viewport.apply();
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
		json = new Json();
		batch = new SpriteBatch();
		shapeRenderer = new BuffedShapeRenderer();
		assetManager = new GameAssetManager();
		audioManager = new AudioManager();
		glyphLayout = new GlyphLayout();
		uiConfig = new UIConfig();
		inputMultiplexer = new InputMultiplexer();
		assetManager.load();
		gameIO = new GameIO();
		gameIO.setup(gameName);
		beatMapStore = new BeatMapStore(gameIO, json);
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		Gdx.input.setInputProcessor(inputMultiplexer);

		ScreenUtils.clear(Color.BLACK);

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
				setScreen(new IntroScreen(this));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		CenteringHelper.WORLD_WIDTH = viewport.getWorldWidth();
		CenteringHelper.WORLD_HEIGHT = viewport.getWorldHeight();
	}

	@Override
	public void dispose () {
		assetManager.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}
}

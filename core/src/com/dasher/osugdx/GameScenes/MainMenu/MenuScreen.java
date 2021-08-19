package com.dasher.osugdx.GameScenes.MainMenu;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.dasher.osugdx.Framework.Stages.SwitcherStage;
import com.dasher.osugdx.GameScenes.MainMenu.Actors.MenuLogo;
import com.dasher.osugdx.GameScenes.ScreenWithBackgroundMusic;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class MenuScreen extends UIScreen implements ScreenWithBackgroundMusic {
    private Texture logoTexture;
    private MenuLogo menuLogo;
    private SwitcherStage menuStage;

    public MenuScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        logoTexture = assetManager.get(assetManager.textures.logo);
        menuLogo = new MenuLogo(game, logoTexture, true);
        menuStage = new SwitcherStage(game, viewport, true);
        menuStage.addActor(menuLogo);
        beatmapManager.startMusicPlaying();
        beatFactory.addListener(menuLogo);
    }

    @Override
    public void render(float delta) {
        menuLogo.update(delta);
        menuStage.act(delta);
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        beatFactory.removeListener(menuLogo);
    }

    @Override
    public void dispose() {
        //menuStage.dispose();
    }
}

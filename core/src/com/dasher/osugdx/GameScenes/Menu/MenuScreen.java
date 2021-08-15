package com.dasher.osugdx.GameScenes.Menu;


import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.ScreenWithBackgroundMusic;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class MenuScreen extends GameScreen implements ScreenWithBackgroundMusic {
    private MenuStage menuStage;

    public MenuScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        Texture menuBackgroundTexture = assetManager.get(assetManager.textures.menuBackground);

       // menuStage = new MenuStage(
       //         game, viewport, menuBackgroundTexture, menuTitleTexture, menuPlayButtonTexture,
       //         menuButtonClickSound
       // );

        //game.inputMultiplexer.addProcessor(menuStage);
    }

    @Override
    public void render(float delta) {
        //menuStage.act(delta);
        //menuStage.draw();
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

    }

    @Override
    public void dispose() {
        //menuStage.dispose();
    }
}

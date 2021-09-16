package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.Input.InputHelper;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class SoundSelectScreen extends UIScreen {
    private Texture beatmapSetSelectorTexture;
    public Stage beatmapSetSelectorStage;
    public Array<BeatmapSetSelector> beatmapSetSelectors;

    public SoundSelectScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        beatmapSetSelectors = new Array<>();
        beatmapSetSelectorStage = new Stage(viewport, batch);
        beatmapSetSelectorTexture = skinManager.getSelectedSkin().menuButtonBG.getTexture();

        beatMapStore.getBeatMapSets().forEach((b) -> {
                BeatmapSetSelector beatmapSetSelector = new BeatmapSetSelector(beatmapSetSelectorTexture);
                beatmapSetSelectors.add(beatmapSetSelector);
                beatmapSetSelectorStage.addActor(beatmapSetSelector);
            }
        );
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        viewport.apply(true);
        backgroundStage.act(delta);
        backgroundStage.draw();
        beatmapSetSelectorStage.act(delta);
        beatmapSetSelectorStage.draw();


        if (InputHelper.isBackPressed()) {
            this.switchScreen(new MenuScreen(game));
        }

        renderFade(delta);
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

    }
}

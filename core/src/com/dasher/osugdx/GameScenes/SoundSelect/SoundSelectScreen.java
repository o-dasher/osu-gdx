package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Scrollers.Scrollable;
import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.Input.InputHelper;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinElement;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class SoundSelectScreen extends UIScreen implements BeatmapManagerListener, GestureDetector.GestureListener {
    public Scrollable beatmapSetSelectorStage;
    public Array<BeatmapSetSelector> beatmapSetSelectors;

    public SoundSelectScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        beatmapSetSelectors = new Array<>();
        beatmapSetSelectorStage = new Scrollable(viewport, batch);
        beatmapSetSelectorStage.setHeightMultiplier(0.5f);
        beatmapSetSelectorStage.setScrollable(false, true);
        SkinElement beatmapSetSelectorElement = skinManager.getSelectedSkin().menuButtonBG;
        Array<BeatMapSet> beatMapSets = beatMapStore.getBeatMapSets();
        for (BeatMapSet beatMapSet: beatMapSets) {
            BeatmapSetSelector beatmapSetSelector = new BeatmapSetSelector(
                    game, beatmapSetSelectorElement, beatMapSet, beatmapManager
            );
            beatmapSetSelectors.add(beatmapSetSelector);
            beatmapSetSelectorStage.addActor(beatmapSetSelector);
        }
        beatmapSetSelectorStage.layout();
        beatmapManager.addListener(this);
        inputMultiplexer.addProcessor(new GestureDetector(beatmapSetSelectorStage));
        inputMultiplexer.addProcessor(beatmapSetSelectorStage);
    }

    public boolean scrollToSelectedBeatmapSet() {
        BeatMapSet currentSelectedBeatmap = beatmapManager.getCurrentBeatmapSet();
        if (!beatmapSetSelectorStage.isLayouting()) {
            for (BeatmapSetSelector beatmapSetSelector: beatmapSetSelectors) {
                if (beatmapSetSelector.beatMapSet == currentSelectedBeatmap) {
                    beatmapSetSelectorStage.addAction(
                            Actions.moveTo(
                                    beatmapSetSelectorStage.getRoot().getX(),
                                    -(beatmapSetSelector.getY() + beatmapSetSelector.getHeight()),
                                    1
                            )
                    );
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean scrolledToBeatmapSetAtStart = false;

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!scrolledToBeatmapSetAtStart) {
            scrolledToBeatmapSetAtStart = scrollToSelectedBeatmapSet();
        }

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
        beatmapManager.removeListener(this);
    }

    @Override
    public void dispose() {
        beatmapSetSelectorStage.dispose();
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {

    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}

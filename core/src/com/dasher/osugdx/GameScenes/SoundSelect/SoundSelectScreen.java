package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
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

public class SoundSelectScreen extends UIScreen implements BeatmapManagerListener {
    public BeatmapSetSelector selectedSelector;
    public Scrollable<BeatmapSetSelector> beatmapSetSelectorStage;
    public Array<BeatmapSetSelector> beatmapSetSelectors;
    public boolean isScrollingToNextBeatmapSet = true;

    public SoundSelectScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        beatmapSetSelectors = new Array<>();
        beatmapSetSelectorStage = new Scrollable<>(viewport, batch);
        beatmapSetSelectorStage.setHeightMultiplier(0.5f);
        beatmapSetSelectorStage.setScrollable(false, true);
        SkinElement beatmapSetSelectorElement = skinManager.getSelectedSkin().menuButtonBG;
        Array<BeatMapSet> beatMapSets = beatMapStore.getBeatMapSets();
        for (BeatMapSet beatMapSet: beatMapSets) {
            BeatmapSetSelector beatmapSetSelector = new BeatmapSetSelector(
                    game, beatmapSetSelectorElement, beatMapSet, beatmapManager, this
            );
            beatmapSetSelectors.add(beatmapSetSelector);
            beatmapSetSelectorStage.addItem(beatmapSetSelector);
        }
        beatmapSetSelectorStage.setAlign(Align.right);
        beatmapSetSelectorStage.layout();
        beatmapManager.addListener(this);
        inputMultiplexer.addProcessor(new GestureDetector(beatmapSetSelectorStage));
        inputMultiplexer.addProcessor(beatmapSetSelectorStage);
    }

    public boolean scrollToSelectedBeatmapSet() {
        BeatMapSet currentSelectedBeatmap = beatmapManager.getCurrentBeatmapSet();
        if (beatmapSetSelectorStage.isNotLayouting()) {
            for (BeatmapSetSelector beatmapSetSelector: beatmapSetSelectors) {
                if (beatmapSetSelector.beatMapSet == currentSelectedBeatmap) {
                    beatmapSetSelectorStage.addAction(
                            Actions.sequence(
                                    Actions.run(() -> isScrollingToNextBeatmapSet = true),
                                    Actions.moveTo(
                                            beatmapSetSelectorStage.getRoot().getX(),
                                            -beatmapSetSelector.getY() + beatmapSetSelector.getHeight() * 2,
                                            1
                                    ),
                                    Actions.run(() -> isScrollingToNextBeatmapSet = false)
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
        viewport.apply();
        beatmapSetSelectorStage.act(delta);
        beatmapSetSelectorStage.draw();

        if (InputHelper.isBackPressed()) {
            this.switchScreen(new MenuScreen(game));
        }

        renderFade(delta);
    }

    @Override
    public void resize(int width, int height) {
        beatmapSetSelectorStage.layout();
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
}

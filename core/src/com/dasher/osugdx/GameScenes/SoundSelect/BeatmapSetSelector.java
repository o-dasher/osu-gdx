package com.dasher.osugdx.GameScenes.SoundSelect;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public class BeatmapSetSelector extends Selector {
    public final BeatMapSet beatMapSet;
    public boolean isLayouted = false;

    public BeatmapSetSelector(OsuGame game, @NotNull Skin skin, BeatMapSet beatMapSet, BeatmapManager beatmapManager, BitmapFont font, SoundSelectScreen soundSelectScreen) {
        super(game, skin, beatmapManager, soundSelectScreen, font);
        this.beatMapSet = beatMapSet;
        initLabels();
        adjustColor();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public BeatmapMetadata metadata() {
        return beatMapSet.beatmaps.first().getMetadata();
    }

    @Override
    public boolean mapChangeCondition() {
        return !(isThisMapSelected()) && !soundSelectScreen.isScrollingToNextBeatmapSet;
    }

    public void layoutBeatmaps() {
        safeChangeSelectedSelector();
        for (Beatmap beatmap: beatMapSet.beatmaps) {
            BeatmapSelector beatmapSelector =  new BeatmapSelector(
                    game, skin, beatmapManager, soundSelectScreen, font, beatMapSet, beatmap
            );
            if (soundSelectScreen.beatmapSetSelectorStage != null) {
                soundSelectScreen.beatmapSetSelectorStage.addItem(beatmapSelector);
            }
        }
        soundSelectScreen.rearrangeSelectors();
        isLayouted = true;
    }

    @Override
    public void changeMap() {
        beatmapManager.setCurrentBeatmapSet(beatMapSet);
        layoutBeatmaps();
    }

    @Override
    public boolean isThisMapSelected() {
        boolean isSelected = beatmapManager.getCurrentBeatmapSet() == beatMapSet;
        if (isSelected && !isLayouted) {
            layoutBeatmaps();
        }
        return isSelected;
    }

    @Override
    public void changeSelectedSelector() {
        disableSelector(soundSelectScreen.selectedBeatmapSet);
        soundSelectScreen.selectedBeatmapSet = this;
    }

    @Override
    public Selector getSelectedSelector() {
        return soundSelectScreen.selectedBeatmapSet;
    }
}

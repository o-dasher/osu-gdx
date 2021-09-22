package com.dasher.osugdx.GameScenes.SoundSelect;

import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class BeatmapSelector extends Selector {
    public Beatmap beatmap;
    public BeatMapSet beatmapSet;

    public BeatmapSelector(OsuGame game, @NotNull Skin skin, BeatmapManager beatmapManager, SoundSelectScreen soundSelectScreen, BeatMapSet beatmapSet, Beatmap beatmap) {
        super(game, skin, beatmapManager, soundSelectScreen);
        this.beatmap = beatmap;
        this.beatmapSet = beatmapSet;
        adjustColor();
    }

    @Override
    public boolean mapChangeCondition() {
        return beatmapManager.getCurrentMap() != beatmap && beatmapManager.getCurrentBeatmapSet() == beatmapSet;
    }

    @Override
    public void changeMap() {
        if (beatmapManager.getCurrentBeatmapSet() == beatmapSet) {
            beatmapManager.setCurrentMap(beatmap);
        }
    }

    @Override
    public boolean isThisMapSelected() {
        return beatmapManager.getCurrentMap() == beatmap;
    }

    @Override
    public void changeSelectedSelector() {
        disableSelector(soundSelectScreen.selectedBeatmap);
        soundSelectScreen.selectedBeatmap = this;
    }

    @Override
    public Selector getSelectedSelector() {
        return soundSelectScreen.selectedBeatmap;
    }
}

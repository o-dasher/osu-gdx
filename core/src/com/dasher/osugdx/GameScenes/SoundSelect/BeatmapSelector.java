package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public class BeatmapSelector extends Selector {
    protected Beatmap beatmap;
    protected BeatMapSet beatmapSet;

    public BeatmapSelector(OsuGame game, @NotNull Skin skin, BeatmapManager beatmapManager, SoundSelectScreen soundSelectScreen, BitmapFont font, BeatMapSet beatmapSet, @NotNull Beatmap beatmap) {
        super(game, skin, beatmapManager, soundSelectScreen, font);
        this.beatmap = beatmap;
        this.beatmapSet = beatmapSet;
        initLabels();
        Label diffLabel = new Label(metadata().getVersion(), labelStyle);
        diffLabel.setPosition(middleLabel.getX(), middleLabel.getY() - font.getXHeight() * 2);
        addActor(diffLabel);
        adjustColor();
    }

    @Override
    public BeatmapMetadata metadata() {
        return beatmap.getMetadata();
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

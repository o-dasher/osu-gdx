package com.dasher.osugdx.GameScenes.SoundSelect;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;


import java.util.Comparator;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public class BeatmapSetSelector extends Selector {
    public final BeatMapSet beatMapSet;
    public boolean isLayouted = false;

    public BeatmapSetSelector(
            OsuGame game, @NotNull Skin skin, BeatMapSet beatMapSet, BeatmapManager beatmapManager,
            BitmapFont font, Label.LabelStyle labelStyle,
            SoundSelectScreen soundSelectScreen
    ) {
        super(game, skin, beatmapManager, soundSelectScreen, font, labelStyle);
        this.beatMapSet = beatMapSet;
        initLabels();
        adjustColor();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public Color activeColor() {
        return Color.PINK;
    }

    @Override
    public Color inactiveColor() {
        return Color.PINK;
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
        Array<BeatmapSelector> beatmapSelectors = new Array<>();
        Color inactiveBeatmapColor = new Color(0xadd8e6ff);
        for (Beatmap beatmap: beatMapSet.beatmaps) {
            BeatmapSelector beatmapSelector = new BeatmapSelector(
                    game, skin, beatmapManager, soundSelectScreen, font, labelStyle,
                        beatMapSet, beatmap, inactiveBeatmapColor
            );
            beatmapSelectors.add(beatmapSelector);
        }
        beatmapSelectors.sort((o1, o2) ->
            (int) (o1.beatmap.getDifficulty().getStars() - o2.beatmap.getDifficulty().getStars())
        );
        for (BeatmapSelector beatmapSelector: beatmapSelectors) {
            soundSelectScreen.beatmapSetSelectorStage.addItem(beatmapSelector);
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
        return beatmapManager.getCurrentBeatmapSet() == beatMapSet;
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

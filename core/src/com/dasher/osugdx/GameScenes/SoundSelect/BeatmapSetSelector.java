package com.dasher.osugdx.GameScenes.SoundSelect;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public class BeatmapSetSelector extends Selector {
    public final BeatMapSet beatMapSet;
    public boolean isLayouted = false;
    public final Array<BeatmapSelector> beatmapSelectors = new Array<>();
    private final Color inactiveBeatmapColor = new Color(0xadd8e6ff);

    public BeatmapSetSelector(
            OsuGame game, @NotNull Skin skin, @NotNull BeatMapSet beatMapSet, BeatmapManager beatmapManager,
            BitmapFont font, Label.LabelStyle labelStyle,
            SoundSelectScreen soundSelectScreen, boolean allowThumbnails
    ) {
        super(game, skin, beatmapManager, soundSelectScreen, font, labelStyle, allowThumbnails);
        this.beatMapSet = beatMapSet;
        initLabels();
        adjustColor();
        for (int i = 0; i < beatMapSet.beatmaps.size; i++) {
            Beatmap beatmap = beatMapSet.beatmaps.get(i);
            BeatmapSelector beatmapSelector = createBeatmapSelector(beatmap);
            game.modManager.addListener(beatmapSelector);
            game.beatmapManager.addListener(beatmapSelector);
            beatmapSelectors.add(beatmapSelector);
        }
        try {
            beatmapSelectors.sort((o1, o2) ->
                    Double.compare(o1.beatmap.getBaseStars(), o2.beatmap.getBaseStars())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuBackground.setColor(inactiveColor());
    }

    public BeatmapSelector createBeatmapSelector(Beatmap beatmap) {
        return new BeatmapSelector(
                game, skin, beatmapManager, soundSelectScreen, font, labelStyle,
                beatMapSet, beatmap, inactiveBeatmapColor, allowThumbnails, this
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public Color activeColor() {
        return Color.PINK;
    }

    // TODO: PROPER BEATMAPSET COLOR BASED ON WHETER IT HAS A SINGLE BEATMAP ETC
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
        return !(isThisMapSelected()) && !soundSelectScreen.isMovingSelectors();
    }

    public void layoutBeatmaps() {
        safeChangeSelectedSelector();
        for (BeatmapSelector beatmapSelector: beatmapSelectors) {
            beatmapSelector.animateStars();
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

    @Override
    public void disableSelector(Selector selector) {
        super.disableSelector(selector);
        if (selector instanceof BeatmapSetSelector) {
            for (BeatmapSelector beatmapSelector : ((BeatmapSetSelector) selector).beatmapSelectors) {
                beatmapSelector.generateStarsTasks.clear();
                soundSelectScreen.beatmapSetSelectorStage.removeItem(beatmapSelector);
                for (GameImage star: beatmapSelector.stars) {
                    star.getActions().clear();
                    star.getColor().a = 0;
                }
            }
            ((BeatmapSetSelector) selector).isLayouted = false;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (BeatmapSelector beatmapSelector: beatmapSelectors) {
            beatmapSelector.dispose();
            beatmapManager.removeListener(beatmapSelector);
            game.modManager.removeListener(beatmapSelector);
        }
    }
}

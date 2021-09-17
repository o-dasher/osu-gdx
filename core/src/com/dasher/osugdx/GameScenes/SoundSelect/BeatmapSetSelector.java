package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinElement;

import org.jetbrains.annotations.NotNull;

public class BeatmapSetSelector extends GameImage {
    private final BeatmapManager beatmapManager;
    private final SkinElement element;
    private final SoundSelectScreen soundSelectScreen;
    public final BeatMapSet beatMapSet;

    public BeatmapSetSelector(
            OsuGame game, @NotNull SkinElement element,
            BeatMapSet beatMapSet, BeatmapManager beatmapManager, SoundSelectScreen soundSelectScreen
    ) {
        super(game, element.getTexture(), false);
        this.beatmapManager = beatmapManager;
        this.element = element;
        this.beatMapSet = beatMapSet;
        this.soundSelectScreen = soundSelectScreen;
        adjustColor();
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (
                        beatmapManager.getCurrentBeatmapSet() != beatMapSet
                        && !soundSelectScreen.isScrollingToNextBeatmapSet
                ) {
                    beatmapManager.setCurrentBeatmapSet(beatMapSet);
                    adjustColor();
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    public void adjustColor() {
        if (beatmapManager.getCurrentBeatmapSet() == this.beatMapSet) {
            if (soundSelectScreen.selectedSelector != null) {
                soundSelectScreen.selectedSelector.setColor(
                        element.getSkin().getSongSelectInactiveTextColor()
                );
            }
            soundSelectScreen.selectedSelector = this;
            setColor(element.getSkin().getSongSelectActiveTextColor());
        } else {
            setColor(element.getSkin().getSongSelectInactiveTextColor());
        }
    }
}

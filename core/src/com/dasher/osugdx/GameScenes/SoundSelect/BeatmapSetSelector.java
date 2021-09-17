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
    public final BeatMapSet beatMapSet;

    public BeatmapSetSelector(
            OsuGame game, @NotNull SkinElement element,
            BeatMapSet beatMapSet, BeatmapManager beatmapManager
    ) {
        super(game, element.getTexture(), false);
        this.beatMapSet = beatMapSet;
        setColor(element.getSkin().getSongSelectInactiveTextColor());
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (beatmapManager.getCurrentBeatmapSet() != beatMapSet) {
                    beatmapManager.setCurrentBeatmapSet(beatMapSet);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}

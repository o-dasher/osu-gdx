package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.osu.Mods.ModManagerListener;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public class BeatmapSelector extends Selector implements BeatmapManagerListener, ModManagerListener {
    protected Beatmap beatmap;
    protected BeatMapSet beatmapSet;
    private final Label diffLabel;
    private final Color inactiveColor;

    public BeatmapSelector(
            OsuGame game, @NotNull Skin skin, BeatmapManager beatmapManager,
            SoundSelectScreen soundSelectScreen,
            BitmapFont font, Label.LabelStyle labelStyle,
            BeatMapSet beatmapSet, @NotNull Beatmap beatmap, Color inactiveColor
    ) {
        super(game, skin, beatmapManager, soundSelectScreen, font, labelStyle);
        this.beatmap = beatmap;
        this.beatmapSet = beatmapSet;
        this.inactiveColor = inactiveColor;
        initLabels();
        diffLabel = createLabel(metadata().getVersion());
        diffLabel.setPosition(middleLabel.getX(), middleLabel.getY() - middleLabel.getHeight() * labelScale);
        adjustColor();
        onBeatmapCalculated(beatmap);
    }

    private void generateStars() {
        float starX = diffLabel.getX();
        // 10 - 1 to account for last star which can be floated
        float maxStars = Math.min(10 - 1, (int) beatmap.getBaseStars());
        float starScale = 0.25f;
        for (int i = 0; i < maxStars + 1; i++) {
            Sprite starSprite = skin.star1.getSprite();
            if (i == maxStars) {
                // Last star can be "float"
                float floatingStar = (float) (beatmap.getBaseStars() - i);
                TextureRegion region = new TextureRegion(
                        starSprite.getTexture(),
                        0, 0,
                        (int) (starSprite.getWidth() * floatingStar * 2), (int) (starSprite.getHeight() * 2)
                );
                starSprite = new Sprite(region);
            }
            GameImage star = new GameImage(game, starSprite, false);
            star.setScale(starScale);
            star.setPosition(
                    starX,
                    diffLabel.getY() - star.getHeight() * starScale * labelScale
            );
            star.setTouchable(Touchable.disabled);
            starX += star.getWidth() * starScale;
            addActor(star);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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
    public void adjustColor() {
        super.adjustColor();
        if (isThisMapSelected()) {
            for (Label label: labels) {
                label.addAction(Actions.color(skin.getSongSelectActiveTextColor()));
            }
        }
    }

    @Override
    public Color activeColor() {
        return Color.WHITE;
    }

    @Override
    public Color inactiveColor() {
        return inactiveColor;
    }

    @Override
    public void disableSelector(Selector selector) {
        if (selector != null) {
            super.disableSelector(selector);
            for (Label label: selector.labels) {
                label.addAction(Actions.color(skin.getSongSelectInactiveTextColor(), colorSelectTime));
            }
        }
    }

    @Override
    public boolean isThisMapSelected() {
        return beatmapManager.getCurrentMap().beatmapFilePath.equals(beatmap.beatmapFilePath);
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


    @Override
    public void onBeatmapCalculated(Beatmap beatmap) {
        if (beatmap.beatmapFilePath.equals(this.beatmap.beatmapFilePath)) {
            generateStars();
        }
    }
}

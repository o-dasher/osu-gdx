package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public class BeatmapSelector extends Selector {
    protected Beatmap beatmap;
    protected BeatMapSet beatmapSet;
    private final Label diffLabel;

    public BeatmapSelector(
            OsuGame game, @NotNull Skin skin, BeatmapManager beatmapManager,
            SoundSelectScreen soundSelectScreen,
            BitmapFont font, Label.LabelStyle labelStyle,
            BeatMapSet beatmapSet, @NotNull Beatmap beatmap
    ) {
        super(game, skin, beatmapManager, soundSelectScreen, font, labelStyle);
        this.beatmap = beatmap;
        this.beatmapSet = beatmapSet;
        initLabels();
        diffLabel = new Label(metadata().getVersion(), labelStyle);
        diffLabel.setPosition(middleLabel.getX(), middleLabel.getY() - font.getXHeight() * 2);
        diffLabel.setTouchable(Touchable.disabled);
        addActor(diffLabel);
        generateStars();
        adjustColor();
    }

    private void generateStars() {
        float starX = diffLabel.getX();
        float maxStars = Math.min(10, (int) beatmap.getDifficulty().getStars());
        float starScale = 0.25f;
        for (int i = 0; i < maxStars; i++) {
            Sprite starSprite = skin.star1.getSprite();
            if (i == maxStars - 1) {
                // Last star can be "float"
                float floatingStar = (float) (beatmap.getDifficulty().getStars() - i - 1);
                TextureRegion region = new TextureRegion(
                        starSprite.getTexture(),
                        0, 0,
                        (int) (starSprite.getWidth() * floatingStar * 2), (int) (starSprite.getHeight() * 2)
                );
                starSprite = new Sprite(region);
            }
            GameImage star = new GameImage(game, starSprite, false);
            star.setScale(starScale);
            star.setPosition(starX, diffLabel.getY() - star.getHeight() * starScale);
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

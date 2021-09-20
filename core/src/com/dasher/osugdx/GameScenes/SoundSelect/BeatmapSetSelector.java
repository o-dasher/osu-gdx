package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.Framework.Actors.ActorHelper;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;


public class BeatmapSetSelector extends Group implements BeatmapManagerListener {
    private final GameImage menuBackground;
    private final GameImage thumbnail;
    private final Skin skin;
    private final BeatmapManager beatmapManager;
    private final SoundSelectScreen soundSelectScreen;
    private boolean addedThumbnail = false;
    public final BeatMapSet beatMapSet;

    public BeatmapSetSelector(
            OsuGame game, @NotNull Skin skin,
            BeatMapSet beatMapSet, BeatmapManager beatmapManager, SoundSelectScreen soundSelectScreen
    ) {
        super();
        Sprite menuButtonBG = skin.menuButtonBG.getSprite();
        float w = menuButtonBG.getWidth();
        float h = menuButtonBG.getHeight();
        this.menuBackground = new GameImage(game, menuButtonBG, false);
        this.thumbnail = new GameImage(game, new SpriteDrawable(new Sprite()), false);
        menuBackground.setSize(w, h);
        this.beatmapManager = beatmapManager;
        this.skin = skin;
        this.beatMapSet = beatMapSet;
        this.soundSelectScreen = soundSelectScreen;
        adjustColor();
        this.addActor(menuBackground);
        setSize(w, h);
        setOrigin(Align.right);
        setScale(0.9f);
        thumbnail.setSize(115, 85);
        thumbnail.setPosition(18, getHeight() / 2 - thumbnail.getHeight() / 2);
        menuBackground.setPosition(getWidth() / 2 - w / 2f, getHeight() / 2 - h / 2f);
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

    private boolean isThumbnailHidden = true;

    @Override
    public void act(float delta) {
        super.act(delta);
        SpriteDrawable thumbDrawable = ((SpriteDrawable) thumbnail.getDrawable());
        Sprite thumbSprite = thumbDrawable.getSprite();
        if (soundSelectScreen.beatmapSetSelectorStage.isNotLayouting()) {
            if (ActorHelper.actorIsVisible(this)) {
                if (thumbSprite.getTexture() == null || isThumbnailHidden) {
                    Texture thumbnailTexture = BeatmapUtils.getBackground(beatMapSet.beatmaps.first());
                    if (thumbnailTexture != null) {
                        isThumbnailHidden = false;
                        thumbDrawable.setSprite(new Sprite(thumbnailTexture));
                        if (!addedThumbnail) {
                            addedThumbnail = true;
                            addActor(thumbnail);
                        }
                    }
                }
            } else if (addedThumbnail && thumbSprite.getTexture() != null) {
                thumbSprite.getTexture().dispose();
                thumbSprite.getColor().a = 0;
                isThumbnailHidden = true;
            }
        }
    }

    public void adjustColor() {
        if (beatmapManager.getCurrentBeatmapSet() == this.beatMapSet) {
            if (soundSelectScreen.selectedSelector != null) {
                soundSelectScreen.selectedSelector.setColor(skin.getSongSelectInactiveTextColor());
            }
            soundSelectScreen.selectedSelector = this;
            menuBackground.setColor(skin.getSongSelectActiveTextColor());
        } else {
            menuBackground.setColor(skin.getSongSelectInactiveTextColor());
        }
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {

    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {
        adjustColor();
    }
}

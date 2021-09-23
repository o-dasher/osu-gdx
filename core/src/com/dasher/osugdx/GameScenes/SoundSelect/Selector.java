package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;


public abstract class Selector extends Group implements BeatmapManagerListener {
    protected final GameImage thumbnail;
    protected final OsuGame game;
    protected final GameImage menuBackground;
    protected final Skin skin;
    protected final BeatmapManager beatmapManager;
    protected final SoundSelectScreen soundSelectScreen;
    protected final BitmapFont font;
    protected boolean addedThumbnail = false;
    protected Label.LabelStyle labelStyle;
    protected Label titleLabel;
    protected Label middleLabel;
    private final float colorSelectTime = 0.25f;

    public Selector(
            OsuGame game, @NotNull Skin skin,
            BeatmapManager beatmapManager, SoundSelectScreen soundSelectScreen, BitmapFont font
    ) {
        super();
        this.game = game;
        this.font = font;
        Sprite menuButtonBG = skin.menuButtonBG.getSprite();
        float w = menuButtonBG.getWidth();
        float h = menuButtonBG.getHeight();
        this.menuBackground = new GameImage(game, menuButtonBG, false);
        this.thumbnail = new GameImage(game, new SpriteDrawable(new Sprite()), false);
        menuBackground.setSize(w, h);
        this.beatmapManager = beatmapManager;
        this.skin = skin;
        this.soundSelectScreen = soundSelectScreen;
        adjustColor();
        this.addActor(menuBackground);
        setSize(w, h);
        setOrigin(Align.right);
        thumbnail.setSize(115, 85);
        float thumbnailX = 18;
        if (skin.menuButtonBG.isHD()) {
            thumbnailX /= 2;
        }
        thumbnail.setPosition(thumbnailX, getHeight() / 2 - thumbnail.getHeight() / 2);
        menuBackground.setPosition(getWidth() / 2 - w / 2f, getHeight() / 2 - h / 2f);
        setScale(0.9f);
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mapChangeCondition()) {
                    changeMap();
                    adjustColor();
                }
                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }

    public void initLabels() {
        BeatmapMetadata metadata = metadata();
        labelStyle = new Label.LabelStyle(font, null);
        titleLabel = new Label(metadata.getTitle(), labelStyle);
        titleLabel.setPosition(getWidth() * 0.2f, getHeight() * 0.7f);
        titleLabel.setFontScale(1);
        addActor(titleLabel);
        middleLabel = new Label(metadata.getArtistRomanized() + "//" + metadata.getCreator(), labelStyle);
        middleLabel.setPosition(titleLabel.getX(), titleLabel.getY() - font.getXHeight() * 2);
        addActor(middleLabel);
    }

    public abstract BeatmapMetadata metadata();
    public abstract boolean mapChangeCondition();
    public abstract void changeMap();
    public abstract boolean isThisMapSelected();

    public boolean isThumbnailTextureLoaded = false;
    public boolean isLazyLoadingThumbnail = false;

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void adjustColor() {
        if (isThisMapSelected()) {
            if (getColor() != skin.getSongSelectActiveTextColor()) {
                safeChangeSelectedSelector();
                menuBackground.addAction(Actions.color(skin.getSongSelectActiveTextColor(), colorSelectTime));
            }
        } else {
            disableSelector(this);
        }
    }

    public void disableSelector(Selector selector) {
        if (selector != null) {
            selector.menuBackground.addAction(
                    Actions.color(skin.getSongSelectInactiveTextColor(), colorSelectTime)
            );
        }
    }

    public void safeChangeSelectedSelector() {
        if (getSelectedSelector() != this) {
            changeSelectedSelector();
        }
    }

    public abstract void changeSelectedSelector();
    public abstract Selector getSelectedSelector();

    @Override
    public void onNewBeatmap(Beatmap beatmap) {
        adjustColor();
    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {
        adjustColor();
    }
}

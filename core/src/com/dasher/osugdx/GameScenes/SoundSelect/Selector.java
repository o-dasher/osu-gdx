package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTexture;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;


public abstract class Selector extends Group implements BeatmapManagerListener, Disposable {
    protected GameImage thumbnail = null;
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
    protected final Array<Label> labels = new Array<>();
    protected final float labelScale = 0.5f;
    protected final float colorSelectTime = 0.25f;
    protected boolean allowThumbnails;

    public Selector(
            OsuGame game, @NotNull Skin skin,
            BeatmapManager beatmapManager, SoundSelectScreen soundSelectScreen,
            BitmapFont font, Label.LabelStyle labelStyle, boolean allowThumbnails
    ) {
        super();
        this.labelStyle = labelStyle;
        this.game = game;
        this.font = font;
        this.allowThumbnails = allowThumbnails;
        game.beatmapManager.addListener(this);
        Sprite menuButtonBG = skin.menuButtonBG.getSprite();
        float w = 699;
        float h = 103;
        this.menuBackground = new GameImage(game, menuButtonBG, false);
        if (allowThumbnails) {
            this.thumbnail = new GameImage(game, new SpriteDrawable(new Sprite()), false);
        }
        menuBackground.setSize(w, h);
        this.beatmapManager = beatmapManager;
        this.skin = skin;
        this.soundSelectScreen = soundSelectScreen;
        this.addActor(menuBackground);
        setSize(w, h);
        setOrigin(Align.center);
        if (thumbnail != null) {
            thumbnail.setSize(115, 85);
            float thumbnailX = 9;
            thumbnail.setPosition(thumbnailX, getHeight() / 2 - thumbnail.getHeight() / 2);
        }
        menuBackground.setPosition(getWidth() / 2 - w / 2f, getHeight() / 2 - h / 2f);
        setScale(0.9f);
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mapChangeCondition() && !soundSelectScreen.isMovingSelectors()) {
                    try {
                        changeMap();
                        adjustColor();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        titleLabel = createLabel(metadata.getTitleRomanized());
        titleLabel.setPosition(getWidth() * 0.2f, getHeight() - titleLabel.getHeight());
        middleLabel = createLabel(metadata.getArtistRomanized() + " // " + metadata.getCreator());
        middleLabel.setPosition(titleLabel.getX(), titleLabel.getY() - titleLabel.getHeight() * labelScale);
    }

    public abstract BeatmapMetadata metadata();
    public abstract boolean mapChangeCondition();
    public abstract void changeMap();
    public abstract boolean isThisMapSelected();

    public boolean isThumbnailTextureLoaded = false;
    public boolean isLazyLoadingThumbnail = false;

    protected Label createLabel(@Null CharSequence text) {
        Label label = new Label(text, labelStyle);
        label.setTouchable(Touchable.disabled);
        label.setFontScale(labelScale);
        labels.add(label);
        addActor(label);
        return label;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void adjustColor() {
        if (isThisMapSelected()) {
            if (getColor() != skin.getSongSelectActiveTextColor()) {
                safeChangeSelectedSelector();
                menuBackground.addAction(Actions.color(activeColor(), colorSelectTime));
            }
        } else {
            disableSelector(this);
        }
    }

    public abstract Color activeColor();
    public abstract Color inactiveColor();

    public void disableSelector(Selector selector) {
        if (selector != null) {
            if (selector.inactiveColor() != null) {
                selector.menuBackground.addAction(
                        Actions.color(selector.inactiveColor(), colorSelectTime)
                );
            }
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

    @Override
    public void onPreBeatmapChange() {
        adjustColor();
    }

    @Override
    public void dispose() {
        game.beatmapManager.removeListener(this);
        if (thumbnail != null) {
            SpriteDrawable drawable =  ((SpriteDrawable) thumbnail.getDrawable());
            ReusableTexture texture =  ((ReusableTexture) drawable.getSprite().getTexture());
            if (
                    texture != null
                            && !texture.isDisposed()
                            && (((TextureRegionDrawable) game.workingBackground.getDrawable()).getRegion().getTexture() != texture)
            ) {
                texture.forceDispose();
            }
        }
    }
}

package com.dasher.osugdx.GameScenes.SoundSelect;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.crashinvaders.vfx.effects.MotionBlurEffect;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.dasher.osugdx.Framework.Actors.ActorHelper;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTexture;
import com.dasher.osugdx.Framework.Scrollers.Scrollable;
import com.dasher.osugdx.Framework.Tasks.ClockTask;
import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.Input.InputHelper;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;

public class SoundSelectScreen extends UIScreen implements BeatmapManagerListener {
    public BeatmapSetSelector selectedBeatmapSet;
    public BeatmapSelector selectedBeatmap;
    public Scrollable<Selector> beatmapSetSelectorStage;
    public Array<BeatmapSetSelector> beatmapSetSelectors;
    public boolean isScrollingToNextBeatmapSet = true;
    private boolean isBaseShowing = false;
    private float thumbnailLazyLoadingTime;
    private Label.LabelStyle selectorLabelStyle;
    private boolean allowThumbnails;
    private MotionBlurEffect scrollMotionBlur;

    public SoundSelectScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        selectorLabelStyle = new Label.LabelStyle(fonts.allerFont, null);
        thumbnailLazyLoadingTime = 0.1f;
        beatmapSetSelectorStage = new Scrollable<>(viewport);
        inputMultiplexer.addProcessor(new GestureDetector(beatmapSetSelectorStage));
        inputMultiplexer.addProcessor(beatmapSetSelectorStage);
        beatmapSetSelectorStage.setYMultiplier(0.5f);
        beatmapSetSelectorStage.setScrollable(false, true);
        beatmapSetSelectorStage.setAlignX(Align.right);
        beatmapSetSelectorStage.setXMultiplier(0.9f);
        beatmapSetSelectorStage.setStairCased(true);
        beatmapSetSelectorStage.setStairCaseMultiplier(25);
        beatmapSetSelectorStage.setStairCaseAdjustTime(0.25f);
        beatmapSetSelectorStage.setHoverAbleItems(true);
        beatmapSetSelectorStage.setHoverXMultiplier(0.05f);
        beatmapSetSelectorStage.setStartFromSidesStaircase(true);
        beatmapSetSelectorStage.setExponentialAlpha(true);
        allowThumbnails = Gdx.app.getType() != Application.ApplicationType.Android;
        scrollMotionBlur = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MAX, 1);
        resetSelectors();
        beatmapManager.addListener(this);
    }

    public void resetSelectors() {
        Array<BeatMapSet> beatMapSets = beatMapStore.getBeatMapSets();
        beatmapSetSelectors = new Array<>();
        for (BeatMapSet beatMapSet: beatMapSets) {
            BeatmapSetSelector beatmapSetSelector = new BeatmapSetSelector(
                    game, game.skinManager.getSelectedSkin(), beatMapSet,
                    beatmapManager, fonts.baseBitmapFont, selectorLabelStyle, this, allowThumbnails
            );
            beatmapSetSelectors.add(beatmapSetSelector);
        }
        rearrangeSelectors();
    }

    public void rearrangeSelectors() {
        if (isBaseShowing) {
            return;
        }
        long time = TimeUtils.millis();
        isBaseShowing = true;
        //beatmapSetSelectors.sort((a, b) ->
        //        (int) (b.beatMapSet.beatmaps.get(b.beatMapSet.beatmaps.size - 1).getBaseStars()
        //                - a.beatMapSet.beatmaps.get(a.beatMapSet.beatmaps.size - 1).getBaseStars())
        //);
        beatmapSetSelectorStage.getItems().clear();
        for (Actor actor: beatmapSetSelectorStage.getActors()) {
            actor.clearActions();
            actor.addAction(Actions.removeActor());
        }
        for (BeatmapSetSelector beatmapSetSelector: beatmapSetSelectors) {
            if (beatmapSetSelector.beatMapSet == beatMapStore.getMainDefaultBeatmapSet()) {
                continue;
            }
            if (beatmapSetSelector.isThisMapSelected()) {
                beatmapSetSelector.layoutBeatmaps();
            } else if (!beatmapSetSelectorStage.getItems().contains(beatmapSetSelector, true)) {
                beatmapSetSelectorStage.addItem(beatmapSetSelector);
            }
        }
        beatmapSetSelectorStage.layout();
        scrollToSelectedBeatmapSet();
        isBaseShowing = false;
        System.out.println(TimeUtils.timeSinceMillis(time)+"ms to rearrange selectors");
    }

    public boolean scrollToSelectedBeatmapSet() {
        isScrollingToNextBeatmapSet = false;
        return true;
        /*
        BeatMapSet currentSelectedBeatmapSet = beatmapManager.getCurrentBeatmapSet();
        if (beatmapSetSelectorStage.isNotLayouting()) {
            for (BeatmapSetSelector beatmapSetSelector: beatmapSetSelectors) {
                if (beatmapSetSelector.beatMapSet == currentSelectedBeatmapSet) {
                    Vector2 pos = new Vector2(
                            beatmapSetSelectorStage.getRoot().getX(),
                            0       // I DON'T KNOW THE FORMULA FOR THIS
                    );
                    beatmapSetSelectorStage.addAction(
                            Actions.sequence(
                                    Actions.run(() -> isScrollingToNextBeatmapSet = true),
                                    Actions.moveTo(pos.x, pos.y, 0.1f, Interpolation.bounce),
                                    Actions.run(() -> isScrollingToNextBeatmapSet = false)
                            )
                    );
                    break;
                }
            }
            return true;
        }
        return false;
         */
    }

    private boolean scrolledToBeatmapSetAtStart = false;
    private ClockTask clockTask;

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!scrolledToBeatmapSetAtStart) {
            scrolledToBeatmapSetAtStart = scrollToSelectedBeatmapSet();
        }

        renderBackground(delta);


        beatmapSetSelectorStage.act(delta);

        vfxManager.addEffect(scrollMotionBlur);
        vfxManager.beginInputCapture();

        beatmapSetSelectorStage.draw();

        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
        vfxManager.removeEffect(scrollMotionBlur);

        renderFade(delta);

        // loading thumbnails is rather slow on these platforms
        if (allowThumbnails) {
            updateSelectorThumbnails(delta);
        }

        if (InputHelper.isBackPressed()) {
            if (!game.calledToSwitchScreen) {
                switchScreen(new MenuScreen(game, this));
            }
        }
    }

    public void updateSelectorThumbnails(float delta) {
        if (clockTask != null) {
            clockTask.update(delta);
        }
        if (beatmapSetSelectorStage.isNotLayouting() && !isScrollingToNextBeatmapSet && !isBaseShowing) {
            for (Selector selector: beatmapSetSelectorStage.getItems()) {
                SpriteDrawable thumbDrawable = ((SpriteDrawable) selector.thumbnail.getDrawable());
                Sprite thumbSprite = thumbDrawable.getSprite();
                if (selector.getStage() != null && ActorHelper.actorIsVisible(selector)) {
                    if (
                            (thumbSprite.getTexture() == null || !selector.isThumbnailTextureLoaded)
                                    && !selector.isLazyLoadingThumbnail
                                    && (clockTask == null || clockTask.isCancelled())
                    ) {
                        Beatmap beatmap = null;
                        if (selector instanceof BeatmapSetSelector) {
                            beatmap = ((BeatmapSetSelector) selector).beatMapSet.beatmaps.first();
                        } else if (selector instanceof BeatmapSelector) {
                            beatmap = ((BeatmapSelector) selector).beatmap;
                        }
                        if (beatmap == null) {
                            continue;
                        }

                        Texture thumbnailTexture = beatmapUtils.getBackground(beatmap, texture -> {
                            if (
                                    ((TextureRegionDrawable) workingBackground.getDrawable()).getRegion().getTexture() == texture
                                            || isMovingSelectors()
                                            || selectedBeatmap == selector
                                            || selector.getStage() != null && ActorHelper.actorIsVisible(selector)
                            ) {
                                return false;
                            }
                            unloadSelectorThumbnail(selector);
                            return true;
                        });
                        selector.isLazyLoadingThumbnail = true;
                        if (thumbnailTexture != null) {
                            clockTask = new ClockTask(thumbnailLazyLoadingTime) {
                                @Override
                                public void run() {
                                    selector.isLazyLoadingThumbnail = false;
                                    selector.thumbnail.getColor().a = 0;
                                    if (selector.getStage() != null && ActorHelper.actorIsVisible(selector) && !selector.isThumbnailTextureLoaded) {
                                        selector.thumbnail.clearActions();
                                        thumbDrawable.setSprite(new Sprite(thumbnailTexture));
                                        selector.thumbnail.addAction(Actions.fadeIn(1));
                                        selector.isThumbnailTextureLoaded = true;
                                        if (!selector.addedThumbnail) {
                                            selector.addedThumbnail = true;
                                            selector.addActor(selector.thumbnail);
                                        }
                                    } else {
                                        if (!isMovingSelectors()) {
                                            selector.isThumbnailTextureLoaded = false;
                                            unloadSelectorThumbnail(selector);
                                        }
                                    }
                                }
                            };
                        }
                    }
                } else if (selector.addedThumbnail && thumbSprite.getTexture() != null && selector.isThumbnailTextureLoaded && !isMovingSelectors()) {
                    thumbSprite.getTexture().dispose();
                    unloadSelectorThumbnail(selector);
                }
            }
        }
    }

    protected boolean isMovingSelectors() {
        return !beatmapSetSelectorStage.isNotLayouting() || isScrollingToNextBeatmapSet || isBaseShowing;
    }

    private void unloadSelectorThumbnail(@NotNull Selector selector) {
        selector.thumbnail.clearActions();
        selector.thumbnail.getColor().a = 0;
        selector.isThumbnailTextureLoaded = false;
    }

    @Override
    public void resize(int width, int height) {
        beatmapSetSelectorStage.layout();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        beatmapManager.removeListener(this);
    }

    @Override
    public void dispose() {
        for (Selector selector: beatmapSetSelectorStage.getItems()) {
            selector.dispose();
        }
        beatmapSetSelectorStage.dispose();
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {

    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {
        scrollToSelectedBeatmapSet();
        rearrangeSelectors();
    }

    @Override
    public void onPreBeatmapChange() {

    }

    public boolean isBaseShowing() {
        return isBaseShowing;
    }
}

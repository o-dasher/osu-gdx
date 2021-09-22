package com.dasher.osugdx.GameScenes.SoundSelect;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Actors.ActorHelper;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTexture;
import com.dasher.osugdx.Framework.Scrollers.Scrollable;
import com.dasher.osugdx.Framework.Tasks.ClockTask;
import com.dasher.osugdx.GameScenes.MainMenu.MenuScreen;
import com.dasher.osugdx.GameScenes.UIScreen;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
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
    private final float thumbnailLazyLoadingTime = 0.25f;

    public SoundSelectScreen(@NotNull OsuGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        beatmapSetSelectorStage = new Scrollable<>(viewport);
        inputMultiplexer.addProcessor(new GestureDetector(beatmapSetSelectorStage));
        inputMultiplexer.addProcessor(beatmapSetSelectorStage);
        beatmapSetSelectorStage.setYMultiplier(0.5f);
        beatmapSetSelectorStage.setScrollable(false, true);
        beatmapSetSelectorStage.setAlignX(Align.right);
        beatmapSetSelectorStage.setXMultiplier(0.95f);
        beatmapSetSelectorStage.setStairCased(true);
        beatmapSetSelectorStage.setStairCaseAdjustTime(0.5f);
        beatmapSetSelectorStage.setHoverAbleItems(true);
        beatmapSetSelectorStage.setHoverXMultiplier(0.075f);
        resetSelectors();
        beatmapManager.addListener(this);
    }

    public void resetSelectors() {
        Array<BeatMapSet> beatMapSets = beatMapStore.getBeatMapSets();
        beatmapSetSelectors = new Array<>();
        for (BeatMapSet beatMapSet: beatMapSets) {
            BeatmapSetSelector beatmapSetSelector = new BeatmapSetSelector(
                    game, game.skinManager.getSelectedSkin(), beatMapSet, beatmapManager, this
            );
            beatmapSetSelectors.add(beatmapSetSelector);
        }
        rearrangeSelectors();
    }

    public void rearrangeSelectors() {
        if (isBaseShowing) {
            return;
        }
        isBaseShowing = true;
        beatmapSetSelectors.sort((a, b) -> a.beatMapSet.getTitle().compareTo(b.beatMapSet.getTitle()));
        scrolledToBeatmapSetAtStart = false;
        beatmapSetSelectorStage.resetItemPositionsToBase();
        beatmapSetSelectorStage.getItems().clear();
        for (Actor actor: beatmapSetSelectorStage.getActors()) {
            actor.clearActions();
            actor.addAction(Actions.removeActor());
        }
        beatmapSetSelectorStage.act(Gdx.graphics.getDeltaTime());
        for (BeatmapSetSelector beatmapSetSelector: beatmapSetSelectors) {
            if (!beatmapSetSelectorStage.getItems().contains(beatmapSetSelector, true)) {
                beatmapSetSelectorStage.addItem(beatmapSetSelector);
                beatmapManager.addListener(beatmapSetSelector);
            }
            if (beatmapSetSelector.isThisMapSelected()) {
                beatmapSetSelector.layoutBeatmaps();
            }
        }
        beatmapSetSelectorStage.layout();
        isBaseShowing = false;
    }

    public boolean scrollToSelectedBeatmapSet() {
        BeatMapSet currentSelectedBeatmapSet = beatmapManager.getCurrentBeatmapSet();
        if (beatmapSetSelectorStage.isNotLayouting()) {
            for (BeatmapSetSelector beatmapSetSelector: beatmapSetSelectors) {
                if (beatmapSetSelector.beatMapSet == currentSelectedBeatmapSet) {
                    Vector2 pos = new Vector2(
                            beatmapSetSelectorStage.getRoot().getX(),
                            -beatmapSetSelector.getY() + beatmapSetSelector.getHeight() * 2
                    );
                    beatmapSetSelectorStage.addAction(
                            Actions.sequence(
                                    Actions.run(() -> isScrollingToNextBeatmapSet = true),
                                    Actions.moveTo(pos.x, pos.y, 0.25f, Interpolation.bounce),
                                    Actions.run(() -> isScrollingToNextBeatmapSet = false)
                            )

                    );
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean scrolledToBeatmapSetAtStart = false;
    private ClockTask clockTask;

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!scrolledToBeatmapSetAtStart) {
            scrolledToBeatmapSetAtStart = scrollToSelectedBeatmapSet();
        }

        viewport.apply(true);
        backgroundStage.act(delta);
        backgroundStage.draw();
        viewport.apply();
        beatmapSetSelectorStage.act(delta);
        beatmapSetSelectorStage.draw();

        renderFade(delta);

        // TODO: TEST
        /*
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            return;
        }
         */

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

        if (InputHelper.isBackPressed()) {
            this.switchScreen(new MenuScreen(game));
        }
    }

    private boolean isMovingSelectors() {
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
            SpriteDrawable drawable =  ((SpriteDrawable) selector.thumbnail.getDrawable());
            ReusableTexture texture =  ((ReusableTexture) drawable.getSprite().getTexture());
            if (
                    texture != null
                    && !texture.isDisposed()
                    && (((TextureRegionDrawable) workingBackground.getDrawable()).getRegion().getTexture() != texture)
            ) {
                texture.forceDispose();
            }
        }
        beatmapSetSelectorStage.dispose();
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {

    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {
        scrollToSelectedBeatmapSet();
    }

    public boolean isBaseShowing() {
        return isBaseShowing;
    }
}
